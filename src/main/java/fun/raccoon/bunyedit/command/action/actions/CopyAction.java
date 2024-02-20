package fun.raccoon.bunyedit.command.action.actions;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;

public class CopyAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
    ) {
        if (argv.length > 0)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        
        playerData.copyBuffer = selection.copy(true);

        sender.sendMessage(i18n.translateKey("bunyedit.cmd.copy.success"));

        return true;
    }
}
