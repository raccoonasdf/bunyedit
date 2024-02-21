package fun.raccoon.bunyedit.command.action;

import fun.raccoon.bunyedit.BunyEdit;
import fun.raccoon.bunyedit.data.PlayerData;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.player.gamemode.Gamemode;

public interface IPlayerAction extends IAction {
    public boolean apply(I18n i18n, CommandSender sender, EntityPlayer player, PlayerData playerData, String[] argv);

    @Override
    default public boolean apply(I18n i18n, CommandSender sender, String[] argv) {
        EntityPlayer player = sender.getPlayer();
        if (player == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.notaplayer"));
        
        if (!sender.isAdmin()) {
            if (
                (player.gamemode.equals(Gamemode.creative) && !BunyEdit.ALLOWED_CREATIVE)
                || (player.gamemode.equals(Gamemode.survival) && !BunyEdit.ALLOWED_SURVIVAL)
            ) {
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.insufficientperms"));
            }
        }
        
        return apply(i18n, sender, sender.getPlayer(), PlayerData.get(player), argv);
    }
}
