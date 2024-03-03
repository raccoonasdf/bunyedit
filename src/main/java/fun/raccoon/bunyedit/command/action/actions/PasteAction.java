package fun.raccoon.bunyedit.command.action.actions;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.BlockData;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.PosMath;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class PasteAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
    ) {
        if (argv.length > 0)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        
        ChunkPosition origin = selection.getPrimary();
            
        BlockBuffer before = new BlockBuffer();
        playerData.copyBuffer.forEach((pos, blockData) -> {
            pos = PosMath.add(pos, origin);
            before.put(pos, new BlockData(player.world, pos));
        });

        BlockBuffer after = new BlockBuffer();
        playerData.copyBuffer.forEach((pos, blockData) -> {
            pos = PosMath.add(pos, origin);
            after.placeRaw(player.world, pos, blockData);
        });
        after.finalize(player.world);
        
        playerData.undoTape.push(before, after);

        return true;
    }
}
