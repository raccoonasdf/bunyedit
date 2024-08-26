package fun.raccoon.bunyedit.command.action.actions;

import java.util.List;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.selection.ValidSelection;
import fun.raccoon.bunyedit.util.ChatString;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;

public class GetSelectionAction implements ISelectionAction {
    @Override
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, ValidSelection selection, List<String> argv
    ) {
        if (argv.size() > 0)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));

        sender.sendMessage(String.format("%s: %s",
            i18n.translateKey("bunyedit.selection"),
            ChatString.gen(playerData.selection)));

        return true;
    }
}
