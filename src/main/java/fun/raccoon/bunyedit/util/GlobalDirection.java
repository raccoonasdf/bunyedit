package fun.raccoon.bunyedit.util;

import net.minecraft.core.world.chunk.ChunkPosition;

public enum GlobalDirection {
    E,
    W,

    U,
    D,

    S,
    N;
    
    public static GlobalDirection from(int axis, boolean inv) {
        if (!inv) {
            switch (axis) {
                case 0:
                    return E;
                case 1:
                    return U;
                case 2:
                    return S;
            }
        } else {
            switch (axis) {
                case 0:
                    return W;
                case 1:
                    return D;
                case 2:
                    return N;
            }
        }
        return null;
    }

    /**
     * From surge direction.
     */
    public static GlobalDirection from(LookDirection lookDir) {
        return from(lookDir.getGlobalAxis(2), lookDir.getGlobalInv(2));
    }

    public ChunkPosition signum() {
        switch (this) {
            case E: return new ChunkPosition(1, 0, 0);
            case W: return new ChunkPosition(-1, 0, 0);

            case U: return new ChunkPosition(0, 1, 0);
            case D: return new ChunkPosition(0, -1, 0);

            case S: return new ChunkPosition(0, 0, 1);
            case N: return new ChunkPosition(0, 0, -1);

            default: return null;
        }
    }
}
