package fun.raccoon.bunyedit.command.action.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.LookAxis;
import fun.raccoon.bunyedit.data.LookDirection;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.Selection.Slot;
import fun.raccoon.bunyedit.util.DirectionHelper;
import fun.raccoon.bunyedit.util.PosMath;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.chunk.ChunkPosition;

public class GrowSelAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
    ) {
        ChunkPosition s1 = selection.getPrimary();
        ChunkPosition s2 = selection.getSecondary();
        ChunkPosition offset1 = PosMath.all(0);
        ChunkPosition offset2 = PosMath.all(0);
        String[] growArgs;
        switch (argv.length) {
            case 0:
                growArgs = new String[]{ "*" };
                break;
            case 1:
                growArgs = argv[0].split(",");
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        Map<Direction, Slot> which = new HashMap<>();
        LookDirection heading = new LookDirection(player.yRot);

        which.put(Direction.EAST,  s1.x > s2.x ? Slot.PRIMARY : Slot.SECONDARY);
        which.put(Direction.WEST,  s1.x > s2.x ? Slot.SECONDARY : Slot.PRIMARY);
        which.put(Direction.UP,    s1.y > s2.y ? Slot.PRIMARY : Slot.SECONDARY);
        which.put(Direction.DOWN,  s1.y > s2.y ? Slot.SECONDARY : Slot.PRIMARY);
        which.put(Direction.SOUTH, s1.z > s2.z ? Slot.PRIMARY : Slot.SECONDARY);
        which.put(Direction.NORTH, s1.z > s2.z ? Slot.SECONDARY : Slot.PRIMARY);

        for (String growArg : growArgs) {
            if (growArg.length() == 0)
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidgrowdir"));

            String directionStr = growArg.substring(0, 1).toUpperCase();
            Direction direction = DirectionHelper.fromAbbrev(directionStr);
            if (direction == null) {
                switch (directionStr) {
                    case "F": direction = heading.globalDir(LookAxis.SURGE); break;
                    case "B": direction = heading.globalDir(LookAxis.SURGE); break;
                    case "L": direction = heading.globalDir(LookAxis.SWAY); break;
                    case "R": direction = heading.globalDir(LookAxis.SWAY); break;
                    case "*": direction = Direction.NONE; break;
                    default:
                        throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidgrowdir"));
                }
            }

            int magnitude = 1;
            if (growArg.length() > 1) {
                String magnitudeStr = growArg.substring(1);
                try {
                    magnitude = Integer.parseInt(magnitudeStr);
                } catch (NumberFormatException e) {
                    throw new CommandError("bunyedit.cmd.err.invalidnumber");
                }
            }

            List<Direction> directions = new ArrayList<>();
            if (direction.equals(Direction.NONE)) {
                directions.add(Direction.EAST);
                directions.add(Direction.WEST);
                directions.add(Direction.SOUTH);
                directions.add(Direction.NORTH);
                directions.add(Direction.UP);
                directions.add(Direction.DOWN);
            } else {
                directions.add(direction);
            }

            for (Direction dir : directions) {
                Slot slotToOffset = which.get(dir);
                ChunkPosition offset = PosMath.mul(PosMath.directionOffset(dir), PosMath.all(magnitude));
                switch (slotToOffset) {
                    case PRIMARY:
                        offset1 = PosMath.add(offset1, offset);
                        break;
                    case SECONDARY:
                        offset2 = PosMath.add(offset2, offset);
                }
            }
        }

        selection.setPrimary(player.world, PosMath.add(s1, offset1));
        selection.setSecondary(player.world, PosMath.add(s2, offset2));

        return true;
    }
}
