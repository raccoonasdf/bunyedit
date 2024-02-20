package fun.raccoon.bunyedit.util;

import java.util.function.BiFunction;

import net.minecraft.core.world.chunk.ChunkPosition;

public class PosMath {
    public static ChunkPosition mapComponents(
        BiFunction<Integer, Integer, Integer> f,
        ChunkPosition a, ChunkPosition b
    ) {
        return new ChunkPosition(
            f.apply(a.x, b.x),
            f.apply(a.y, b.y),
            f.apply(a.z, b.z));
    }

    public static ChunkPosition add(ChunkPosition a, ChunkPosition b) {
        return mapComponents(((x, y) -> x + y), a, b);
    }

    public static ChunkPosition sub(ChunkPosition a, ChunkPosition b) {
        return mapComponents(((x, y) -> x - y), a, b);
    }
}
