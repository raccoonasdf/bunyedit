package fun.raccoon.bunyedit.command.action.actions;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.LookDirection;
import fun.raccoon.bunyedit.util.PosMath;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class FlipAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
    ) {
        int axis;
        switch (argv.length) {
            case 0:
                axis = new LookDirection(player.yRot, player.xRot).getGlobalAxis(2);
                break;
            case 1:
                switch (argv[0].toUpperCase()) {
                    case "X":
                        axis = 0;
                        break;
                    case "Y":
                        axis = 1;
                        break;
                    case "Z":
                        axis = 2;
                        break;
                    case "^":
                        axis = new LookDirection(player.yRot, player.xRot).getGlobalAxis(2);
                        break;
                    default:
                        throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidaxis"));
                }
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        ChunkPosition s1 = selection.getPrimary();
        ChunkPosition s2 = selection.getSecondary();

        int[] s1a = PosMath.toArray(s1);
        int[] s2a = PosMath.toArray(s2);

        int side1 = s1a[axis];
        int side2 = s2a[axis];

        BlockBuffer before = selection.copy(false);
        BlockBuffer after = new BlockBuffer();

        BlockBuffer copyBuffer = selection.copy(false);
        copyBuffer.forEach((pos, blockData) -> {
            int[] posa = PosMath.toArray(pos);
            posa[axis] = side2 - (posa[axis] - side1);
            pos = PosMath.fromArray(posa);
            after.put(pos, blockData);
            blockData.place(player.world, pos);
        });
        playerData.undoTape.push(before, after);

        return true;
    }
}
