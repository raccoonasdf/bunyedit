package fun.raccoon.bunyedit.command;

import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

public class CopyCommand extends Command {
    public CopyCommand() { super("/copy", "/c"); }

    public boolean opRequired(String[] argv) { return true; }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("copy|c");
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

        playerData.copyBuffer = selection.copy(true);

        sender.sendMessage(i18n.translateKey("bunyedit.cmd.copy.success"));

        return true;
    }
}
