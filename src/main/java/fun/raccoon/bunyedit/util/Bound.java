package fun.raccoon.bunyedit.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fun.raccoon.bunyedit.data.LookAxis;
import fun.raccoon.bunyedit.data.LookDirection;
import fun.raccoon.bunyedit.data.ValidSelection;
import fun.raccoon.bunyedit.data.Selection.Slot;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.chunk.ChunkPosition;

public class Bound {
    /**
     * Parses a bound argument.
     * @param selection selection to offset
     * @param player player to use for look-relative directions
     * @param boundStr string to parse
     * @return Pair of ChunkPositions representing the offset from the given selection
     * @throws NullPointerException if selection is not valid.
     */
    public static @Nullable Pair<ChunkPosition, ChunkPosition> fromString(
        ValidSelection selection,
        @Nonnull EntityPlayer player,
        String boundStr)
    {
        LookDirection heading = new LookDirection(player.yRot);
        Map<Direction, Integer> directionMagnitudes = new HashMap<>();

        for (String component : boundStr.split(",")) {
            if (component.length() == 0)
                return null;
            
            HashSet<Direction> directions = new HashSet<>();
            int i;
            for (i = 0; i < component.length(); ++i) {
                char c = component.charAt(i);
                if (Character.isLetter(c) || c == '*') {
                    String cStr = Character.toString(c).toUpperCase();
                    Direction direction = DirectionHelper.fromAbbrev(cStr);
                    if (direction != null) {
                        directions.add(direction);
                    } else {
                        switch (cStr) {
                            case "F":
                                directions.add(heading.globalDir(LookAxis.SURGE));
                                break;
                            case "B":
                                directions.add(heading.globalDir(LookAxis.SURGE).getOpposite());
                                break;
                            case "L":
                                directions.add(heading.globalDir(LookAxis.SWAY));
                                break;
                            case "R":
                                directions.add(heading.globalDir(LookAxis.SWAY).getOpposite());
                                break;
                            case "*":
                                directions.add(Direction.SOUTH);
                                directions.add(Direction.NORTH);
                                directions.add(Direction.WEST);
                                directions.add(Direction.EAST);
                                directions.add(Direction.UP);
                                directions.add(Direction.DOWN);
                                break;
                            default:
                                return null;
                        }
                    }
                } else {
                    break;
                }
            }
            if (directions.size() == 0)
                return null;

            component = component.substring(i);
            int magnitude;
            if (component.length() > 0) {
                try {
                    magnitude = Integer.parseInt(component);
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                magnitude = 0;
            }

            for (Direction direction : directions) {
                directionMagnitudes.put(direction, magnitude);
            }
        }

        ChunkPosition s1 = selection.getPrimary();
        ChunkPosition s2 = selection.getSecondary();
        ChunkPosition offset1 = PosMath.all(0);
        ChunkPosition offset2 = PosMath.all(0);
        
        Map<Direction, Slot> whichToOffset = new HashMap<>();
        whichToOffset.put(Direction.EAST,  s1.x > s2.x ? Slot.PRIMARY : Slot.SECONDARY);
        whichToOffset.put(Direction.WEST,  s1.x > s2.x ? Slot.SECONDARY : Slot.PRIMARY);
        whichToOffset.put(Direction.UP,    s1.y > s2.y ? Slot.PRIMARY : Slot.SECONDARY);
        whichToOffset.put(Direction.DOWN,  s1.y > s2.y ? Slot.SECONDARY : Slot.PRIMARY);
        whichToOffset.put(Direction.SOUTH, s1.z > s2.z ? Slot.PRIMARY : Slot.SECONDARY);
        whichToOffset.put(Direction.NORTH, s1.z > s2.z ? Slot.SECONDARY : Slot.PRIMARY);

        for (Entry<Direction, Integer> entry : directionMagnitudes.entrySet()) {
            Direction d = entry.getKey();
            int m = entry.getValue();
            ChunkPosition directionOffset = PosMath.mul(
                PosMath.directionOffset(d),
                PosMath.all(m));
            switch (whichToOffset.get(d)) {
                case PRIMARY:
                    offset1 = PosMath.add(offset1, directionOffset);
                    break;
                case SECONDARY:
                    offset2 = PosMath.add(offset2, directionOffset);
                    break;
            }
        }

        return Pair.of(offset1, offset2);
    }
}
