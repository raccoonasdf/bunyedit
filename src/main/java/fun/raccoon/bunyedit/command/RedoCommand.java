package fun.raccoon.bunyedit.command;

import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.BlockBuffer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

public class RedoCommand extends Command {
    public RedoCommand() { super("/redo"); }

    public boolean opRequired(String[] argv) { return true; }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("redo");
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] argv) {
        I18n i18n = I18n.getInstance();
        
        EntityPlayer player = sender.getPlayer();
        if (sender.getPlayer() == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.notaplayer"));
        PlayerData playerData = PlayerData.get(player);

        if (argv.length > 0) {
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        BlockBuffer page = playerData.undoTape.redo();
        if (page == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.redo.err.noredopages"));

        page.forEach((pos, blockData) -> {
            blockData.place(player.world, pos);
        });

        return true;
    }
}
