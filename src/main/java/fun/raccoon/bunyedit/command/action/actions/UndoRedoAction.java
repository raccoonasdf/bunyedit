package fun.raccoon.bunyedit.command.action.actions;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.UndoTape;
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
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, String[] argv
    ) {
        UndoTape undoTape = playerData.getUndoTape(player.world);
        BlockBuffer page = this.which.equals(Which.UNDO)
            ? undoTape.undo()
            : undoTape.redo();

        if (page == null) {
            throw new CommandError(i18n.translateKeyAndFormat(
                "bunyedit.cmd.undoredo.err.nopages",
                i18n.translateKey(this.which.equals(Which.UNDO)
                    ? "bunyedit.cmd.undoredo.undo"
                    : "bunyedit.cmd.undoredo.redo")));
        }

        BlockBuffer after = new BlockBuffer();
        page.forEach((pos, blockData) -> {
            after.placeRaw(player.world, pos, blockData);
        });
        after.finalize(player.world);

        return true;
    }
}
