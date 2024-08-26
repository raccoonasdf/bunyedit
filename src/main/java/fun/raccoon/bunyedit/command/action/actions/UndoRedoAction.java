package fun.raccoon.bunyedit.command.action.actions;

import java.util.List;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.buffer.BlockBuffer;
import fun.raccoon.bunyedit.data.buffer.EntityBuffer;
import fun.raccoon.bunyedit.data.buffer.UndoPage;
import fun.raccoon.bunyedit.data.buffer.UndoTape;
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

    @Override
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, List<String> argv
    ) {
        if (argv.size() > 0)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));

        UndoTape undoTape = playerData.getUndoTape(player.world);
        UndoPage page = this.which.equals(Which.UNDO)
            ? undoTape.undo()
            : undoTape.redo();

        if (page == null) {
            throw new CommandError(i18n.translateKeyAndFormat(
                "bunyedit.cmd.undoredo.err.nopages",
                i18n.translateKey(this.which.equals(Which.UNDO)
                    ? "bunyedit.cmd.undoredo.undo"
                    : "bunyedit.cmd.undoredo.redo")));
        }

        BlockBuffer newBlocks = page.getRight().blocks;
        EntityBuffer oldEnts = page.getLeft().entities;
        EntityBuffer newEnts = page.getRight().entities;

        BlockBuffer after = new BlockBuffer();
        newBlocks.forEach((pos, blockData) -> {
            after.placeRaw(player.world, pos, blockData);
        });
        after.finalize(player.world);

        oldEnts.destroyIn(player.world);
        newEnts.createIn(player.world);

        return true;
    }
}
