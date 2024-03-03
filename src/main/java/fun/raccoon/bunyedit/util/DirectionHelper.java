package fun.raccoon.bunyedit.util;

import javax.annotation.Nullable;

import fun.raccoon.bunyedit.data.LookAxis;
import fun.raccoon.bunyedit.data.LookDirection;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;

public class DirectionHelper {    
    public static Direction from(Axis axis, boolean inv) {
        if (!inv) {
            switch (axis) {
                case X:
                    return Direction.EAST;
                case Y:
                    return Direction.UP;
                case Z:
                    return Direction.SOUTH;
                case NONE:
                    return Direction.NONE;
            }
        } else {
            switch (axis) {
                case X:
                    return Direction.WEST;
                case Y:
                    return Direction.DOWN;
                case Z:
                    return Direction.NORTH;
                case NONE:
                    return Direction.NONE;
            }
        }
        return null;
    }

    public static @Nullable Direction fromAbbrev(String string) {
        switch (string) {
            case "E": return Direction.EAST;
            case "W": return Direction.WEST;
            case "U": return Direction.UP;
            case "D": return Direction.DOWN;
            case "S": return Direction.SOUTH;
            case "N": return Direction.NORTH;
            default: return null;
        }
    }

    /**
     * From surge direction.
     */
    public static Direction from(LookDirection lookDir) {
        return from(lookDir.globalAxis(LookAxis.SURGE), lookDir.globalInv(LookAxis.SURGE));
    }
}
