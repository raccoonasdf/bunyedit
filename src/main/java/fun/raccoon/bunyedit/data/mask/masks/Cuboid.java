package fun.raccoon.bunyedit.data.mask.masks;

import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.mask.IMask;
import net.minecraft.core.world.chunk.ChunkPosition;

public class Cuboid implements IMask {
    private boolean hollow;

    public Cuboid(boolean hollow) {
        this.hollow = hollow;
    }

    public boolean isOver(Selection selection, ChunkPosition pos) {
        if (!hollow) {
            return true;
        } else {
            ChunkPosition s1 = selection.getPrimary();
            ChunkPosition s2 = selection.getSecondary();

            return pos.x == s1.x || pos.x == s2.x || pos.y == s1.y || pos.y == s2.y || pos.z == s1.z || pos.z == s2.z;
        }
    }
}
