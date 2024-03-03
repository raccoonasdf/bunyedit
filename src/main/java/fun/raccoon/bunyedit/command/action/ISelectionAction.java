package fun.raccoon.bunyedit.command.action;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;

public interface ISelectionAction extends IPlayerAction {
    public boolean apply(I18n i18n, CommandSender sender, @Nonnull EntityPlayer player, PlayerData playerData, Selection selection, String[] argv);
    
    @Override
    default public boolean apply(I18n i18n, CommandSender sender, @Nonnull EntityPlayer player, PlayerData playerData, String[] argv) {
        Selection selection = playerData.selection;
        if (!selection.isValid())
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.incompleteselection"));

        return apply(i18n, sender, player, playerData, selection, argv);
    }
}
