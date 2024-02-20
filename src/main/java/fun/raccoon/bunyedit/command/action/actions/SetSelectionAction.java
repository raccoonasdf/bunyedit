package fun.raccoon.bunyedit.command.action.actions;

import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.ChatString;
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

    private static ChunkPosition interpretArgs(EntityPlayer player, String[] argv) {
        int[] playerCoords = {
            (int)Math.floor(player.x),
            ((int)Math.floor(player.y))-1,
            (int)Math.floor(player.z)
        };
        int[] coords = {0, 0, 0};

        for (int i = 0; i < argv.length; ++i) {
            if(argv[i].startsWith("~")) {
                coords[i] = playerCoords[i];
                if (argv[i].length() == 1) {
                    continue;
                } else {
                    try {
                        coords[i] += Integer.parseInt(argv[i].substring(1));
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            } else {
                try {
                    coords[i] += Integer.parseInt(argv[i]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        return new ChunkPosition(coords[0], coords[1], coords[2]);
    }    

    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, String[] argv
    ) {
        ChunkPosition pos;
        switch (argv.length) {
            case 0:
                pos = new ChunkPosition(
                    (int)Math.floor(player.x),
                    ((int)Math.floor(player.y))-1,
                    (int)Math.floor(player.z));
                break;
            case 1:
            case 2:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.coordneedsthree"));
            case 3:
                pos = interpretArgs(player, argv);
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
