package fun.raccoon.bunyedit.command.action.actions;

import java.util.List;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.buffer.BlockBuffer;
import fun.raccoon.bunyedit.data.buffer.BlockData;
import fun.raccoon.bunyedit.data.buffer.WorldBuffer;
import fun.raccoon.bunyedit.data.look.LookDirection;
import fun.raccoon.bunyedit.data.selection.ValidSelection;
import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.util.parsers.RelCoords;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

    // TODO: actually implement entity copying
public class MoveAction implements ISelectionAction {
    @Override
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, ValidSelection selection, List<String> argv
    ) {
        ChunkPosition copyOrigin = selection.getPrimary();
        ChunkPosition pasteOrigin;
        switch (argv.size()) {
            case 0:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toofewargs"));
            case 1:
                pasteOrigin = RelCoords.from(copyOrigin, new LookDirection(player), argv.get(0));
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        BlockData air = new BlockData();

        WorldBuffer before = selection.copyWorld(false);
        WorldBuffer after = new WorldBuffer();

        

        // three seperate loops to obviate previous bug where copy and paste positions overlap

        // populate undo
        BlockBuffer copyBuffer = selection.copy(true);
        copyBuffer.forEach((pos, blockData) -> {
            ChunkPosition copyPos = PosMath.add(pos, copyOrigin);
            ChunkPosition pastePos = PosMath.add(pos, pasteOrigin);

            before.blocks.put(copyPos, blockData);
            before.blocks.put(pastePos, new BlockData(player.world, pastePos));
        });

        // clear
        copyBuffer.forEach((pos, blockData) -> {
            ChunkPosition copyPos = PosMath.add(pos, copyOrigin);

            after.blocks.placeRaw(player.world, copyPos, air);
        });

        // paste
        copyBuffer.forEach((pos, blockData) -> {
            ChunkPosition pastePos = PosMath.add(pos, pasteOrigin);

            after.blocks.placeRaw(player.world, pastePos, blockData);
        });
        after.blocks.finalize(player.world);
        
        playerData.getUndoTape(player.world).push(before, after);

        return true;
    }
}
