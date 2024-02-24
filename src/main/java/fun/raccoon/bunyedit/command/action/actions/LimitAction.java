package fun.raccoon.bunyedit.command.action.actions;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.PlayerData;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;

public class LimitAction implements IPlayerAction {
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, String[] argv
    ) {
        switch (argv.length) {
            case 0:
                sender.sendMessage(i18n.translateKeyAndFormat("bunyedit.cmd.limit.print", playerData.selectionLimit));
            case 1:
                if (argv[0].equals("no")) {
                    playerData.selectionLimit = null;
                } else {
                    long newLimit;
                    try {
                        newLimit = Long.parseLong(argv[0]);
                        if (newLimit < 0)
                            throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidnumber"));
                    }
                    playerData.selectionLimit = newLimit;
                }

                sender.sendMessage(i18n.translateKey("bunyedit.cmd.limit.success"));
        }

        return true;
    }
}
