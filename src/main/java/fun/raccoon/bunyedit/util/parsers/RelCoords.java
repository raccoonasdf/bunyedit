package fun.raccoon.bunyedit.util.parsers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fun.raccoon.bunyedit.data.look.LookAxis;
import fun.raccoon.bunyedit.data.look.LookDirection;
import fun.raccoon.bunyedit.util.PosMath;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.chunk.ChunkPosition;

public class RelCoords {
    // TODO: parser combinator
    public static ChunkPosition playerPos(@Nonnull EntityPlayer player, boolean fromHead) {
        return new ChunkPosition( 
            (int)Math.floor(player.x),
            ((int)Math.floor(player.y)) - (fromHead ? 2 : 1),
            (int)Math.floor(player.z));
    }

    public static @Nullable ChunkPosition from(ChunkPosition origin, LookDirection lookDir, String triple) {
        int[] origin_ = {origin.x, origin.y, origin.z};
        String[] triple_ = triple.split(",");

        int[] res = new int[3];

        LookAxis[] lookAxes = LookAxis.values();
        for (int i = 0; i < triple_.length; ++i) {
            if (!triple_[i].startsWith("^"))
                return from(origin, triple);
            
            int axis = lookDir.globalAxis(lookAxes[i]).ordinal();
            boolean inv = lookDir.globalInv(lookAxes[i]);

            res[axis] = origin_[axis];

            if (triple_[i].length() > 1) {
                try {
                    res[axis] += (inv ? -1 : 1) * Integer.parseInt(triple_[i].substring(1));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        return PosMath.fromArray(res);
    }
    
    public static @Nullable ChunkPosition from(ChunkPosition origin, String triple) {
        int[] origin_ = {origin.x, origin.y, origin.z};
        String[] triple_ = triple.split(",");

        int[] res = {0, 0, 0};

        for (int i = 0; i < triple_.length; ++i) {
            if (triple_[i].startsWith("~")) {
                res[i] = origin_[i];
                if (triple_[i].length() > 1) {
                    try {
                        res[i] += Integer.parseInt(triple_[i].substring(1));
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            } else {
                try {
                    res[i] += Integer.parseInt(triple_[i]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return PosMath.fromArray(res);
    }

    public static @Nullable ChunkPosition from(@Nonnull EntityPlayer player, String triple) {
        return from(playerPos(player, false), new LookDirection(player), triple);
    }
}
