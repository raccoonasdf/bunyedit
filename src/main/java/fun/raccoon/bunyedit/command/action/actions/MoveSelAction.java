package fun.raccoon.bunyedit.command.action.actions;

import java.util.List;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.look.LookDirection;
import fun.raccoon.bunyedit.data.selection.ValidSelection;
import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.util.parsers.RelCoords;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class MoveSelAction implements ISelectionAction {
    @Override
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, ValidSelection selection, List<String> argv
    ) {
        ChunkPosition origin;
        switch (argv.size()) {
            case 0:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toofewargs"));
            case 1:
                origin = RelCoords.from(selection.getPrimary(), new LookDirection(player), argv.get(0));
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        playerData.selection.setPrimary(player.world, origin);
        playerData.selection.setSecondary(player.world, PosMath.add(origin, PosMath.sub(selection.getSecondary(), selection.getPrimary())));

        return true;
    }
}
