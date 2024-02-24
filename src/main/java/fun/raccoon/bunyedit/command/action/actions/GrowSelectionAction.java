package fun.raccoon.bunyedit.command.action.actions;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.Bound;
import fun.raccoon.bunyedit.util.PosMath;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.world.chunk.ChunkPosition;

public class GrowSelectionAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
    ) {
        Pair<ChunkPosition, ChunkPosition> growBy;
        switch (argv.length) {
            case 0:
                growBy = Bound.fromString(selection, player, "*1");
                break;
            case 1:
                growBy = Bound.fromString(selection, player, argv[0]);
                if (growBy == null)
                    throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidbound"));
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        selection.setPrimary(
            player.world, PosMath.add(selection.getPrimary(), growBy.getLeft()));
        selection.setSecondary(
            player.world, PosMath.add(selection.getSecondary(), growBy.getRight()));

        return true;
    }
}
