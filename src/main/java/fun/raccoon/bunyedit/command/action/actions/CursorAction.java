package fun.raccoon.bunyedit.command.action.actions;

import java.util.List;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.Cursor;
import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.PlayerData;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;

public class CursorAction implements IPlayerAction {
    @Override
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, List<String> argv
    ) {
        if (argv.size() > 0)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        
        player.inventory.insertItem(Cursor.getCursorItem(), true);

        return true;
    }
}
