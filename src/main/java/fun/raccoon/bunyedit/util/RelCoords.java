package fun.raccoon.bunyedit.util;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.chunk.ChunkPosition;

public class RelCoords {
    public static ChunkPosition playerPos(EntityPlayer player, boolean fromHead) {
        return new ChunkPosition( 
            (int)Math.floor(player.x),
            ((int)Math.floor(player.y)) - (fromHead ? 2 : 1),
            (int)Math.floor(player.z));
    }

    public static ChunkPosition from(ChunkPosition origin, float yRot, float xRot, String triple) {
        // yRot: 0..360F, xRot: -90..90F

        yRot = (yRot % 360F);
        if (yRot < 0)
            yRot = yRot + 360F;

        int yRotInt = Math.round(yRot/90F)%4;
        int xRotInt = Math.round(xRot/90F);
        
        int[] origin_ = {origin.x, origin.y, origin.z};
        String[] triple_ = triple.split(",");

        // 0=sway 1=heave 2=surge
        int[] map = new int[3];
        boolean[] invMap = new boolean[3];

        // heading along z on even, x on odd
        int yRotDir = (yRotInt&1) == 0 ? 2 : 0;
        // middle semicircle heads negative
        boolean invYRotDir = (yRotInt&1) != ((yRotInt&2)>>1);

        // swaying along x on even, z on odd
        map[0] = (yRotInt&1) == 0 ? 0 : 2;
        // latter semicircle sways negative
        invMap[0] = (yRotInt&2) == 2;


        // looking up or down surges along Y
        map[2] = (xRotInt != 0) ? 1 : yRotDir;
        // looking up surges negative
        invMap[2] = (xRotInt != 0) ? (xRotInt == 1) : invYRotDir;

        // looking up or down heaves along heading
        map[1] = (xRotInt != 0) ? yRotDir : 1;
        // looking up heaves negative
        invMap[1] = (xRotInt != 0) ? (xRotInt == -1)^invYRotDir : false;
            
        int[] res = new int[3];

        for (int i = 0; i < triple_.length; ++i) {
            if (!triple_[i].startsWith("^"))
                return from(origin, triple);

            res[map[i]] = origin_[map[i]];

            if (triple_[i].length() > 1) {
                try {
                    res[map[i]] += (invMap[i] ? -1 : 1) * Integer.parseInt(triple_[i].substring(1));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        return new ChunkPosition(res[0], res[1], res[2]);
    }
    
    public static ChunkPosition from(ChunkPosition origin, String triple) {
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

        return new ChunkPosition(res[0], res[1], res[2]);
    }


    public static ChunkPosition from(EntityPlayer player, String triple) {
        return from(playerPos(player, false), player.yRot, player.xRot, triple);
    }
}
