package fun.raccoon.bunyedit.command.action.actions;

import java.util.List;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.selection.ValidSelection;
import fun.raccoon.bunyedit.util.parsers.Bound;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.world.chunk.ChunkPosition;

public class GrowSelectionAction implements ISelectionAction {
    @Override
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, ValidSelection selection, List<String> argv
    ) {
        Pair<ChunkPosition, ChunkPosition> growBy;
        switch (argv.size()) {
            case 0:
                growBy = Bound.fromString(selection, player, "*1");
                break;
            case 1:
                growBy = Bound.fromString(selection, player, argv.get(0));
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
