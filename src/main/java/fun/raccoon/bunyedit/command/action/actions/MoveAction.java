package fun.raccoon.bunyedit.command.action.actions;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.buffer.BlockBuffer;
import fun.raccoon.bunyedit.data.buffer.BlockData;
import fun.raccoon.bunyedit.data.look.LookDirection;
import fun.raccoon.bunyedit.data.selection.ValidSelection;
import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.util.argparse.RelCoords;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class MoveAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, ValidSelection selection, String[] argv
    ) {
        ChunkPosition copyOrigin = selection.getPrimary();
        ChunkPosition pasteOrigin;
        switch (argv.length) {
            case 0:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toofewargs"));
            case 1:
                pasteOrigin = RelCoords.from(copyOrigin, new LookDirection(player), argv[0]);
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        BlockData air = new BlockData();

        BlockBuffer before = selection.copy(false);
        BlockBuffer after = new BlockBuffer();

        BlockBuffer copyBuffer = selection.copy(true);
        copyBuffer.forEach((pos, blockData) -> {
            ChunkPosition copyPos = PosMath.add(pos, copyOrigin);
            ChunkPosition pastePos = PosMath.add(pos, pasteOrigin);

            // copyPos had what's in the copyBuffer
            before.put(copyPos, blockData);
            // and pastePos had what's in the world right now
            before.put(pastePos, new BlockData(player.world, pastePos));

            // but now pastePos has what's in the copyBuffer
            after.placeRaw(player.world, pastePos, blockData);

            // and copyPos has nothing
            after.placeRaw(player.world, copyPos, air);
        });
        after.finalize(player.world);
        
        playerData.getUndoTape(player.world).push(before, after);

        return true;
    }
}
