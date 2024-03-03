package fun.raccoon.bunyedit.command.action.actions;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.ValidSelection;
import fun.raccoon.bunyedit.util.Bound;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.world.chunk.ChunkPosition;

public class GrowSelectionAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, ValidSelection selection, String[] argv
    ) {
        Pair<ChunkPosition, ChunkPosition> growBy;
        switch (argv.length) {
            case 0:
                growBy = Bound.fromString(selection, player, "*1");
                break;
            case 1:
                growBy = Bound.fromString(selection, player, argv[0]);
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        if (growBy == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidbound"));

        playerData.selection.setBound(growBy);

        return true;
    }
}
