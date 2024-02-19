package fun.raccoon.bunyedit.data;

import java.util.HashMap;

import net.minecraft.core.world.chunk.ChunkPosition;

/**
 * Mapping from {@link ChunkPosition} to {@link BlockData}.
 */
public class BlockBuffer extends HashMap<ChunkPosition, BlockData> {
    public BlockBuffer() {
        super();
    }
}
