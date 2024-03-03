package fun.raccoon.bunyedit.data.mask;

import java.util.HashSet;
import java.util.function.BiPredicate;

import fun.raccoon.bunyedit.data.ValidSelection;
import net.minecraft.core.world.chunk.ChunkPosition;

/**
 * Mask implemented with an iterative algorithm.
 */
public abstract class IterativeMask implements BiPredicate<ValidSelection, ChunkPosition> {
    private HashSet<ChunkPosition> cachedResponse;
    private ValidSelection cachedSelection = null;

    public abstract void fillCache(HashSet<ChunkPosition> cache, ValidSelection selection);

    public boolean test(ValidSelection selection, ChunkPosition pos) {
        if (cachedSelection == null || !cachedSelection.equals(selection)) {
            cachedSelection = selection;
            cachedResponse = new HashSet<>();
            this.fillCache(cachedResponse, selection);
        }

        return cachedResponse.contains(pos);
    }
}
