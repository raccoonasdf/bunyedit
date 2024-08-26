package fun.raccoon.bunyedit.command.action;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fun.raccoon.bunyedit.BunyEdit;
import fun.raccoon.bunyedit.data.PlayerData;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.player.gamemode.Gamemode;

public interface IPlayerAction extends IAction {
    public boolean apply(I18n i18n, CommandSender sender, @Nonnull EntityPlayer player, PlayerData playerData, List<String> argv);

    @Override
    default public boolean apply(I18n i18n, CommandSender sender, List<String> argv) {
        @Nullable EntityPlayer player = sender.getPlayer();
        if (player == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.notaplayer"));
        
        boolean allowed = false;
        if (sender.isAdmin()) {
            allowed = true;
        } else {
            if (player.gamemode.equals(Gamemode.creative) && BunyEdit.ALLOWED_CREATIVE) {
                allowed = true;
            } else if (player.gamemode.equals(Gamemode.survival) && BunyEdit.ALLOWED_SURVIVAL) {
                allowed = true;
            }
        }

        if (!allowed)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.insufficientperms"));
        
        return apply(i18n, sender, player, PlayerData.get(player), argv);
    }
}
