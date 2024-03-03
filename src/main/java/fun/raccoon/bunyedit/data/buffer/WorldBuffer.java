package fun.raccoon.bunyedit.data.buffer;

import net.minecraft.core.util.collection.Pair;

public class WorldBuffer extends Pair<BlockBuffer, EntityBuffer> {
    protected WorldBuffer(BlockBuffer left, EntityBuffer right) {
        super(left, right);
    }

    public static WorldBuffer of(BlockBuffer left, EntityBuffer right) {
        return new WorldBuffer(left, right);
    }
}
