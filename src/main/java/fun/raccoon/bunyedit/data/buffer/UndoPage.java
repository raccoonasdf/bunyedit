package fun.raccoon.bunyedit.data.buffer;


import net.minecraft.core.util.collection.Pair;

public class UndoPage extends Pair<WorldBuffer, WorldBuffer> {
    protected UndoPage(WorldBuffer left, WorldBuffer right) {
        super(left, right);
    }

    public static UndoPage of(WorldBuffer left, WorldBuffer right) {
        return new UndoPage(left, right);
    }
}
