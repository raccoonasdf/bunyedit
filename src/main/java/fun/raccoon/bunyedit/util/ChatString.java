package fun.raccoon.bunyedit.util;

import fun.raccoon.bunyedit.data.BlockData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.Selection.Slot;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;

/**
 * Uniformly styled ingame chat representations of various types.
 */
public class ChatString {
    private static String gen_coord(int x, int y, int z) {
        String repr = String.format("[%d, %d, %d]", x, y, z);
        return TextFormatting.formatted(repr, TextFormatting.LIGHT_BLUE);
    }

    public static String gen(ChunkPosition pos) {
        return ChatString.gen_coord(pos.x, pos.y, pos.z);
    }

    public static String gen(HitResult hit) {
        return ChatString.gen_coord(hit.x, hit.y, hit.z);
    }

    public static String gen(BlockData blockData) {
        String repr;
        if (blockData.id == 0) {
            repr = "air";
        } else {
            Block block = Block.getBlock(blockData.id);

            repr = block.getKey();
            // remove "tile."
            repr = repr.substring(5);

            if (blockData.meta != 0) {
                repr += TextFormatting.formatted(
                    ":" + blockData.meta, TextFormatting.LIGHT_GRAY);
            }

            if (blockData.nbt != null) {
                repr += TextFormatting.formatted("+NBT", TextFormatting.MAGENTA);
            }
        }

        return TextFormatting.formatted(repr, TextFormatting.LIME);
    }

    public static String gen(Selection.Slot slot) {
        I18n i18n = I18n.getInstance();

        switch (slot) {
            case PRIMARY:
                return TextFormatting.formatted(i18n.translateKey("bunyedit.selection.primary"), TextFormatting.PINK);
            case SECONDARY:
                return TextFormatting.formatted(i18n.translateKey("bunyedit.selection.secondary"), TextFormatting.YELLOW);
            default:
                return null;
        }
    }

    public static String gen(Selection sel) {
        return String.format("%s ... %s", ChatString.gen(sel.getPrimary()), ChatString.gen(sel.getSecondary()));
    }

    public static String gen_select_action(Slot slot, World world, ChunkPosition pos) {
        I18n i18n = I18n.getInstance();

        return String.format("%s: %s",
            ChatString.gen(slot),
            i18n.translateKeyAndFormat("bunyedit.cursor.select",
                ChatString.gen(new BlockData(world, pos)),
                ChatString.gen(pos)));        
    }    
}
