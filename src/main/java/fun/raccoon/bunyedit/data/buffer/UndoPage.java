package fun.raccoon.bunyedit.data.buffer;


import javax.annotation.Nonnull;

import net.minecraft.core.util.collection.Pair;

public class UndoPage extends Pair<WorldBuffer, WorldBuffer> {
    protected UndoPage(WorldBuffer left, WorldBuffer right) {
        super(left, right);
    }

    public static @Nonnull UndoPage of(WorldBuffer left, WorldBuffer right) {
        return new UndoPage(left, right);
    }
}
