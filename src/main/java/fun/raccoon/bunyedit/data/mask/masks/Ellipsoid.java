package fun.raccoon.bunyedit.data.mask.masks;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.mask.IMask;
import net.minecraft.core.world.chunk.ChunkPosition;

public class Ellipsoid implements IMask {
    private boolean hollow;

    public Ellipsoid(boolean hollow) {
        this.hollow = hollow;
    }

    private static boolean inside(
        double a, double b, double c,
        double x, double y, double z
    ) {
        return (x*x)/(a*a) + (y*y)/(b*b) + (z*z)/(c*c) <= 1;
    }
    
    private Stream<Integer> rangeClosed(int from, int to) {
        return IntStream.rangeClosed(Math.min(from, to), Math.max(from, to)).boxed();
    }

    public boolean isOver(Selection selection, ChunkPosition pos) {
        ChunkPosition s1 = selection.getPrimary();
        ChunkPosition s2 = selection.getSecondary();

        double a = Math.abs(s2.x - s1.x)/2.0+0.5;
        double b = Math.abs(s2.y - s1.y)/2.0+0.5;
        double c = Math.abs(s2.z - s1.z)/2.0+0.5;

        double cx = (s1.x + s2.x)/2.0;
        double cy = (s1.y + s2.y)/2.0;
        double cz = (s1.z + s2.z)/2.0;

        double x = pos.x - cx;
        double y = pos.y - cy;
        double z = pos.z - cz;

        if (!hollow) {
            return inside(a, b, c, x, y, z);
        } else {
            boolean exposed = rangeClosed(-1, 1)
                .flatMap(dx -> rangeClosed(-1, 1)
                    .flatMap(dy -> rangeClosed(-1, 1)
                        .filter(dz -> dx == 0 || dy == 0 || dz == 0)
                        .filter(dz -> !inside(a, b, c, x+dx, y+dy, z+dz))))
                .anyMatch(p -> true);

            return inside(a, b, c, x, y, z) && exposed;
        }
    }
}
