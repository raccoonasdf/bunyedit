package fun.raccoon.bunyedit.data.mask;

import java.util.HashSet;
import java.util.function.BiPredicate;

import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.core.world.chunk.ChunkPosition;

/**
 * {@link IMask} implemented with an iterative algorithm.
 */
public abstract class IterativeMask implements BiPredicate<Selection, ChunkPosition> {
    private HashSet<ChunkPosition> cachedResponse;
    private Selection cachedSelection = null;

    public abstract void fillCache(HashSet<ChunkPosition> cache, Selection selection);

    public boolean test(Selection selection, ChunkPosition pos) {
        if (cachedSelection == null || !cachedSelection.equals(selection)) {
            cachedResponse = new HashSet<>();
            this.fillCache(cachedResponse, selection);
        }

        return cachedResponse.contains(pos);
    }
}
