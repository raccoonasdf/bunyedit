package fun.raccoon.bunyedit.command.action;

import fun.raccoon.bunyedit.data.PlayerData;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;

public interface IPlayerAction extends IAction {
    public boolean apply(I18n i18n, CommandSender sender, EntityPlayer player, PlayerData playerData, String[] argv);

    @Override
    default public boolean apply(I18n i18n, CommandSender sender, String[] argv) {
        EntityPlayer player = sender.getPlayer();
        if (player == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.notaplayer"));
        
        return apply(i18n, sender, sender.getPlayer(), PlayerData.get(player), argv);
    }
}
