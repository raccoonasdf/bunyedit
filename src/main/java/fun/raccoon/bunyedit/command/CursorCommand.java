package fun.raccoon.bunyedit.command;

import fun.raccoon.bunyedit.Cursor;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

public class CursorCommand extends Command {
    public CursorCommand() { super("/cursor", "/cur"); }

    public boolean opRequired(String[] argv) { return true; }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("cursor|cur");
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] argv) {
        I18n i18n = I18n.getInstance();

        EntityPlayer player = sender.getPlayer();
        if (sender.getPlayer() == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.notaplayer"));
        
        if (argv.length > 0)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));

        player.inventory.insertItem(Cursor.getCursorItem(), true);
        
        return true;
    }
}
