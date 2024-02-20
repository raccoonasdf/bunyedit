package fun.raccoon.bunyedit.command.action.actions;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.BlockData;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.LookDirection;
import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.util.RelCoords;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class MoveAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
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
            after.put(pastePos, blockData);
            blockData.place(player.world, pastePos);

            // and copyPos has nothing
            after.put(copyPos, air);
            air.place(player.world, copyPos);
        });
        playerData.undoTape.push(before, after);

        return true;
    }
}