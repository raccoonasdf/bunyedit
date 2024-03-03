package fun.raccoon.bunyedit.util.argparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import fun.raccoon.bunyedit.data.buffer.BlockData;
import net.minecraft.core.block.Block;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.collection.Pair;

public class Pattern {
    private static @Nullable BlockData blockDataFromString(CommandSender sender, String patternStr)
        throws CommandError
    {
        I18n i18n = I18n.getInstance();

        String[] parts = patternStr.split(":");
        if (parts.length > 2)
            return null;

        String key = parts[0];
        if (key.length() == 0)
            return null;

        int id;
        int meta = 0;

        if (key.equals("air"))
            return new BlockData(0, 0, null);

        try {
            id = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            key = key.toLowerCase();
            String origKey = key;

            // remap common terms
            switch (key) {
                case "water": key = "fluid.water"; break;
                case "lava": key = "fluid.lava"; break;
                case "redstone": key = "wire.redstone"; break;
                case "torch": key = "torch.coal"; break;
                case "piston": key = "piston.base"; break;
                case "furnace": key = "furnace.stone"; break;
            }

            // find all prefix matches
            String key_ = key;
            List<Block> blocks = Arrays.stream(Block.blocksList)
                .filter(b -> b != null && b.getKey().startsWith("tile." + key_))
                .collect(Collectors.toList());
            
            // if there's more than one match, eliminate states that probably aren't what you want
            if (blocks.size() > 1) {
                blocks = blocks.stream().filter(b -> {
                    String[] keyParts = b.getKey().split("\\.");
                    switch (keyParts[keyParts.length-1]) {
                        case "flowing": return false;
                        case "active": return false;
                        default: return true;
                    }
                }).collect(Collectors.toList());
            }

            // if there's an exact match, only consider that
            Optional<Block> maybeBlock = blocks.stream().filter(b -> b.getKey().equals("tile." + key_)).findAny();
            if (maybeBlock.isPresent()) {
                blocks = new ArrayList<>();
                blocks.add(maybeBlock.get());
            }

            switch (blocks.size()) {
                case 0: return null;
                case 1:
                    Block block_ = blocks.get(0);
                    String key__ = block_.getKey().substring(5);
                    id = block_.id;
                    if (!key__.equals(origKey)) {
                        sender.sendMessage(
                            TextFormatting.formatted(
                                String.format("%s %s",
                                    i18n.translateKeyAndFormat("bunyedit.cmd.err.ambiguouskey", origKey),
                                    i18n.translateKeyAndFormat("bunyedit.cmd.err.ambiguouskey.remap", key__)),
                                TextFormatting.ORANGE));
                    }
                    break;
                default:
                    Stream<String> keyStream = blocks.stream().map(block -> block.getKey().substring(5+key_.length()));

                    if (blocks.size() > 8)
                        keyStream = Stream.concat(keyStream.limit(8), Stream.of("..."));

                    throw new CommandError(String.format("%s! %s",
                        i18n.translateKeyAndFormat("bunyedit.cmd.err.ambiguouskey", key_),
                        i18n.translateKeyAndFormat("bunyedit.cmd.err.ambiguouskey.try",
                            keyStream.collect(Collectors.joining(", ")))));
            }
        }

        if (parts.length == 2) {
            try {
                meta = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        
        return new BlockData(id, meta, null);
    }

    private static @Nullable Function<BlockData, BlockData> subPattern(CommandSender sender, String patternStr)
        throws CommandError
    {
        BlockData blockData_ = blockDataFromString(sender, patternStr);
        if (blockData_ == null)
            return null;
        return blockData -> blockData_;
    }

    public static @Nullable Function<BlockData, BlockData> fromString(CommandSender sender, String patternArg) {
        List<Pair<Integer, Function<BlockData, BlockData>>> subPatterns = new ArrayList<>();
        String[] patternStrings = patternArg.split("/");

        int totalWeight = 0;

        for (String patternString : patternStrings) {
            String[] weightPatternParts = patternString.split("\\*");

            int weight;
            String patternStr;
            switch (weightPatternParts.length) {
                case 2:
                    try {
                        weight = Integer.parseInt(weightPatternParts[0]);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    patternStr = weightPatternParts[1];
                    break;
                case 1:
                    weight = 1;
                    patternStr = weightPatternParts[0];
                    break;
                default:
                    return null;
            }

            totalWeight += weight;

            Function<BlockData, BlockData> subPattern = subPattern(sender, patternStr);
            if (subPattern == null)
                return null;

            subPatterns.add(Pair.of(weight, subPattern));
        }

        int totalWeight_ = totalWeight;
        return blockData -> {
            int rand = new Random().nextInt(totalWeight_+1);
            for (Pair<Integer, Function<BlockData, BlockData>> pair : subPatterns) {
                rand -= pair.getLeft();
                if (rand <= 0)
                    return pair.getRight().apply(blockData);
            }
            return null;
        };
    }
}
