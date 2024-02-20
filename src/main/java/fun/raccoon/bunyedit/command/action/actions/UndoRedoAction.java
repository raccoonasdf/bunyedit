package fun.raccoon.bunyedit.command.action.actions;

import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.PlayerData;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;

public class UndoRedoAction implements IPlayerAction {
    public enum Which {
        UNDO,
        REDO
    }

    private Which which;

    public UndoRedoAction(Which which) {
        this.which = which;
    }

    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, String[] argv
    ) {
        BlockBuffer page = this.which.equals(Which.UNDO)
            ? playerData.undoTape.undo()
            : playerData.undoTape.redo();

        if (page == null) {
            throw new CommandError(i18n.translateKeyAndFormat(
                "bunyedit.cmd.undoredo.err.nopages",
                i18n.translateKey(this.which.equals(Which.UNDO)
                    ? "bunyedit.cmd.undoredo.undo"
                    : "bunyedit.cmd.undoredo.redo")));
        }

        page.forEach((pos, blockData) -> {
            blockData.place(player.world, pos);
        });

        return true;
    }
}
