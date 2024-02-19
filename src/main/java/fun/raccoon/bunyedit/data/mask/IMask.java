package fun.raccoon.bunyedit.data.mask;

import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.core.world.chunk.ChunkPosition;

/**
 * Coordinate-wise filter for selections.
 */
public interface IMask {
    /**
     * Whether the mask lets the block at this position through.
     */
    public boolean isOver(Selection selection, ChunkPosition pos);
}
