package fun.raccoon.bunyedit.command.action.actions;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.LookDirection;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.util.RelCoords;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class MoveSelAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
    ) {
        ChunkPosition origin;
        switch (argv.length) {
            case 0:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toofewargs"));
            case 1:
                origin = RelCoords.from(selection.getPrimary(), new LookDirection(player), argv[0]);
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        selection.setPrimary(player.world, origin);
        selection.setSecondary(player.world, PosMath.add(origin, selection.getRelSecondary()));

        return true;
    }
}
