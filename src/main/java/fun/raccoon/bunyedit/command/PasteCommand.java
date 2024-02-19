package fun.raccoon.bunyedit.command;

import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.BlockData;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class PasteCommand extends Command {
    public PasteCommand() { super("/paste", "/p"); }

    public boolean opRequired(String[] argv) { return true; }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("paste|p");
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] argv) {
        I18n i18n = I18n.getInstance();
        
        EntityPlayer player = sender.getPlayer();
        if (sender.getPlayer() == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.notaplayer"));
        PlayerData playerData = PlayerData.get(player);

        if (argv.length > 0)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        
        Selection selection = playerData.selection;
        if (!selection.isValid())
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.incompleteselection"));

        ChunkPosition origin = selection.getPrimary();
            
        BlockBuffer before = new BlockBuffer();
        playerData.copyBuffer.forEach((pos, blockData) -> {
            pos = new ChunkPosition(pos.x + origin.x, pos.y + origin.y, pos.z + origin.z);
            before.put(pos, new BlockData(player.world, pos));
        });

        BlockBuffer after = new BlockBuffer();
        playerData.copyBuffer.forEach((pos, blockData) -> {
            pos = new ChunkPosition(pos.x + origin.x, pos.y + origin.y, pos.z + origin.z);
            after.put(pos, blockData);
            blockData.place(player.world, pos);
        });
        playerData.undoTape.push(before, after);

        return true;
    }
}
