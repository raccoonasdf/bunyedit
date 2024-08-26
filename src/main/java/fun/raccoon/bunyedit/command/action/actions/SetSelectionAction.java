package fun.raccoon.bunyedit.command.action.actions;

import java.util.List;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.selection.Selection;
import fun.raccoon.bunyedit.util.ChatString;
import fun.raccoon.bunyedit.util.parsers.RelCoords;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class SetSelectionAction implements IPlayerAction {
    private Selection.Slot slot;

    public SetSelectionAction(Selection.Slot slot) {
        this.slot = slot;
    }

    @Override
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, List<String> argv
    ) {
        ChunkPosition pos;
        switch (argv.size()) {
            case 0:
                pos = RelCoords.playerPos(player, false);
                break;
            case 1:
                pos = RelCoords.from(player, argv.get(0));
                if (pos == null)
                    throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidcoords"));
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }
        
        playerData.selection.set(slot, player.world, pos);

        sender.sendMessage(ChatString.gen_select_action(slot, player.world, pos));

        return true;

    }
}
