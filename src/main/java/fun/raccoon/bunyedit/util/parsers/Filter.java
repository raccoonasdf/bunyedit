package fun.raccoon.bunyedit.util.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import fun.raccoon.bunyedit.data.buffer.BlockData;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.LiquidMaterial;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.MaterialColor;

public class Filter {
    // TODO: parser combinator
    private static @Nonnull Predicate<BlockData> join(@Nonnull List<Predicate<BlockData>> predicates) {
        @Nullable Predicate<BlockData> predicate = predicates.stream().reduce((p, q) -> p.or(q)).get();

        if (predicate == null)
            return b -> false;
        
        return predicate;
    }

    private static @Nullable Predicate<Integer> range(String rangeStr) {
        String[] rangeStrSplit = rangeStr.split("\\.\\.");

        String fromStr;
        String toStr;        
        switch (rangeStrSplit.length) {
            case 1:
                if (rangeStr.endsWith("..")) {
                    fromStr = rangeStrSplit[0];
                    toStr = "";
                } else {
                    return null;
                }
                break;
            case 2:
                fromStr = rangeStrSplit[0];
                toStr = rangeStrSplit[1];
            default:
                return null;
        }

        Optional<Integer> min = Optional.empty();
        Optional<Integer> max = Optional.empty();
        if (!fromStr.equals("")) {
            try {
                min = Optional.of(Integer.parseInt(fromStr));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        if (!toStr.equals("")) {
            try {
                max = Optional.of(Integer.parseInt(toStr));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        
        if (min.isPresent() && !max.isPresent()) {
            int min_ = min.get();
            return n -> n >= min_;
        } else if (!min.isPresent() && max.isPresent()) {
            int max_ = max.get();
            return n -> n <= max_;
        } else if (min.isPresent() && max.isPresent()) {
            int min_ = min.get();
            int max_ = max.get();
            return n -> n >= min_ && n <= max_;
        } else {
            return null;
        }
    }

    private static @Nullable Predicate<BlockData> materialFilter(String materialFilterStr) {
        // ugh
        Material material;
        Predicate<Block> p = null;
        switch (materialFilterStr) {
            case "grass": material = Material.grass; break;
            case "dirt": material = Material.dirt; break;
            case "wood": material = Material.wood; break;
            case "stone": material = Material.stone; break;
            case "metal": material = Material.metal; break;
            case "water": material = Material.water; break;
            case "lava": material = Material.lava; break;
            case "leaves": material = Material.leaves; break;
            case "plant": material = Material.plant; break;
            case "sand": material = Material.sand; break;
            case "glass": material = Material.glass; break;
            case "ice": material = Material.ice; break;
            case "piston": material = Material.piston; break;
            case "snow":
                material = null;
                p = block -> block.blockMaterial.color.equals(MaterialColor.snow);
                break;
            case "liquid":
                material = null;
                p = block -> block.blockMaterial instanceof LiquidMaterial;
                break;
            default: return null;
        }

        if (material != null) {
            p = block -> block.blockMaterial.equals(material);
        }

        if (p == null)
            return null;
        
        // java is dumb
        Predicate<Block> p_ = p;

        return blockData -> {
            Block block = Block.getBlock(blockData.id);
            if (block == null)
                return false;
            return p_.test(block);
        };
    }

    private static @Nullable Predicate<Integer> blockMetas(String blockMetasStr) {
        if (blockMetasStr.equals("*"))
            return Predicates.alwaysTrue();

        Predicate<Integer> r = range(blockMetasStr);
        if (r != null)
            return r;

        try {
            int otherN = Integer.parseInt(blockMetasStr);
            return n -> n == otherN;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static @Nullable Predicate<Integer> blockIds(String blockIdsStr) {
        if (blockIdsStr.equals("air"))
            return n -> n == 0;
        
        if (blockIdsStr.equals("*"))
            return Predicates.alwaysTrue();

        Predicate<Integer> r = range(blockIdsStr);
        if (r != null)
            return r;

        try {
            int otherN = Integer.parseInt(blockIdsStr);
            return n -> n == otherN;
        } catch (NumberFormatException e) {
            Set<Integer> blocks = Arrays.stream(Block.blocksList)
                .filter(b -> {
                    if (b == null)
                        return false;

                    String[] parts = b.getKey().substring(5).split("\\.");
                    String[] filterParts = blockIdsStr.split("\\.");

                    for (int i = 0; i < filterParts.length; ++i) {
                        if (parts.length <= i)
                            return false;

                        if (filterParts[i].endsWith(";")) {
                            if (i != filterParts.length-1)
                                return false;
                            if (parts.length != filterParts.length)
                                return false;
                            return filterParts[i].substring(0, filterParts[i].length()-1).equals(parts[i]);
                        }

                        if (!filterParts[i].equals(parts[i]))
                            return false;
                    }

                    return true;
                }).map(block -> block.id).collect(Collectors.toSet());
            
            if (blocks.isEmpty())
                return null;
            
            return n -> blocks.contains(n);
        }
    }

    public static @Nullable Predicate<BlockData> blockFilter(String blockFilterStr) {
        String[] parts = blockFilterStr.split(":");
        if (parts.length > 2)
            return null;
        
        Predicate<Integer> ids = blockIds(parts[0]);
        if (ids == null)
            return null;

        Predicate<Integer> metas;
        if (parts.length == 2) {
            metas = blockMetas(parts[1]);
            if (metas == null)
                return null;
        } else {
            metas = Predicates.alwaysTrue();
        }

        return blockData -> ids.test(blockData.id) && metas.test(blockData.meta);
    }

    public static @Nullable Predicate<BlockData> fromString(String filterArg) {
        List<Predicate<BlockData>> predicates = new ArrayList<>();
        String[] filterStrings = filterArg.split("/");

        for (String filterString : filterStrings) {
            boolean invert = false;
            if (filterString.startsWith("!")) {
                invert = true;
                filterString = filterString.substring(1);
            }
            Predicate<BlockData> p;
            if (filterString.startsWith("#")) {
                Predicate<BlockData> material = materialFilter(filterString.substring(1));
                if (material == null)
                    return null;
                p = material;
            } else {
                Predicate<BlockData> block = blockFilter(filterString);
                if (block == null)
                    return null;
                p = block;
            }

            if (invert)
                p = p.negate();

            predicates.add(p);
        }

        return join(predicates);
    }
}
