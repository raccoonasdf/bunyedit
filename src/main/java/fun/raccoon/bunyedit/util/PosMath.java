package fun.raccoon.bunyedit.util;

import java.util.function.BiFunction;
import java.util.function.Function;

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
}
