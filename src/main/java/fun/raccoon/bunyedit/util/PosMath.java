package fun.raccoon.bunyedit.util;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.chunk.ChunkPosition;

public class PosMath {
    public static ChunkPosition mapPositions(
        BiFunction<Integer, Integer, Integer> f,
        ChunkPosition a, ChunkPosition b
    ) {
        return new ChunkPosition(
            f.apply(a.x, b.x),
            f.apply(a.y, b.y),
            f.apply(a.z, b.z));
    }

    public static ChunkPosition mapPosition(
        Function<Integer, Integer> f,
        ChunkPosition a
    ) {
        return new ChunkPosition(
            f.apply(a.x),
            f.apply(a.y),
            f.apply(a.z));
    }

    public static ChunkPosition min(ChunkPosition a, ChunkPosition b) {
        return mapPositions((x, y) -> Math.min(x, y), a, b);
    }

    public static ChunkPosition max(ChunkPosition a, ChunkPosition b) {
        return mapPositions((x, y) -> Math.max(x, y), a, b);
    }

    public static boolean inside(ChunkPosition s1, ChunkPosition s2, ChunkPosition a) {
        ChunkPosition min = PosMath.min(s1, s2);
        ChunkPosition max = PosMath.max(s1, s2);
        return a.x >= min.x && a.x <= max.x
            && a.y >= min.y && a.y <= max.y
            && a.z >= min.z && a.z <= max.z;
    }

    public static ChunkPosition add(ChunkPosition a, ChunkPosition b) {
        return mapPositions(((x, y) -> x + y), a, b);
    }

    public static ChunkPosition sub(ChunkPosition a, ChunkPosition b) {
        return mapPositions(((x, y) -> x - y), a, b);
    }

    public static ChunkPosition mul(ChunkPosition a, ChunkPosition b) {
        return mapPositions(((x, y) -> x * y), a, b);
    }

    public static ChunkPosition abs(ChunkPosition a) {
        return mapPosition((x -> Math.abs(x)), a);
    }

    public static ChunkPosition all(int x) {
        return new ChunkPosition(x, x, x);
    }

    public static int[] toArray(ChunkPosition a) {
        return new int[]{a.x, a.y, a.z};
    }

    public static ChunkPosition fromArray(int[] a) {
        return new ChunkPosition(a[0], a[1], a[2]);
    }

    public static ChunkPosition directionOffset(Direction direction) {
        return new ChunkPosition(
            direction.getOffsetX(),
            direction.getOffsetY(),
            direction.getOffsetZ());
    }
}
