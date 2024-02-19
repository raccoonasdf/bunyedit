package fun.raccoon.bunyedit.data.mask.masks;

import java.util.HashSet;

import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.mask.IterativeMask;
import net.minecraft.core.world.chunk.ChunkPosition;

public class Line extends IterativeMask {
    public Line() { }

    @Override
    public void fillCache(HashSet<ChunkPosition> cache, Selection selection) {

        // bresenham algo i took from this stackoverflow answer
        // https://stackoverflow.com/a/50516870

        ChunkPosition s1 = selection.getPrimary();
        ChunkPosition s2 = selection.getSecondary();

        float dx = s2.x - s1.x;
        float dy = s2.y - s1.y;
        float dz = s2.z - s1.z;

        float deltaErrorY = Math.abs(dy / dx);
        float deltaErrorZ = Math.abs(dz / dx);

        float errorY = 0;
        float errorZ = 0;

        int y = s1.y;
        int z = s1.z;

        for (int x = s1.x; x < s2.x; x++) { 
            cache.add(new ChunkPosition(x, y, z));
            errorY += deltaErrorY;
            while (errorY >= 0.5) {
                 y += Math.signum(dy);
                 errorY--;
            }
            errorZ += deltaErrorZ;
            while (errorZ >= 0.5) {
                z += Math.signum(dz);
                errorZ--;
            }
        }
    }
}
