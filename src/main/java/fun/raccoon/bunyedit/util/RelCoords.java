package fun.raccoon.bunyedit.util;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.chunk.ChunkPosition;

public class RelCoords {
    public static ChunkPosition playerPosition(EntityPlayer player, boolean fromHead) {
        return new ChunkPosition( 
            (int)Math.floor(player.x),
            ((int)Math.floor(player.y)) - (fromHead ? 2 : 1),
            (int)Math.floor(player.z));
    }
    
    public static ChunkPosition from(ChunkPosition origin, String triple) {
        int[] origin_ = {origin.x, origin.y, origin.z};
        String[] triple_ = triple.split(",");

        int[] res = {0, 0, 0};

        for (int i = 0; i < triple_.length; ++i) {
            if(triple_[i].startsWith("~")) {
                res[i] = origin_[i];
                if (triple_[i].length() == 1) {
                    continue;
                } else {
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
        return from(playerPosition(player, false), triple);
    }
}
