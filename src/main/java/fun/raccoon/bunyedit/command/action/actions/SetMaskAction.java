package fun.raccoon.bunyedit.command.action.actions;

import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.mask.IMask;
import fun.raccoon.bunyedit.data.mask.masks.Masks;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;

public class SetMaskAction implements IPlayerAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, String[] argv
    ) {
        String maskName;
        switch (argv.length) {
            case 0:
                maskName = "cuboid";
                break;
            case 1:
                maskName = argv[0];
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        if (maskName.equals("list")) {
            sender.sendMessage(String.format("%s: %s",
                i18n.translateKey("bunyedit.cmd.mask.list.header"),
                //Masks.MASKS.keySet().stream().collect(Collectors.joining(", "))
                "cube|cuboid, sphere|ellipsoid, line"));
            sender.sendMessage(i18n.translateKey("bunyedit.cmd.mask.list.hollow"));
            
            return true;
        }

        IMask mask = Masks.MASKS.get(maskName);
        if (mask == null)
            throw new CommandError(i18n.translateKeyAndFormat("bunyedit.cmd.mask.err.nosuchmask", maskName));

        Selection selection = playerData.selection;

        selection.setMask(mask);

        sender.sendMessage(i18n.translateKey("bunyedit.cmd.mask.success"));

        return true;
    }
}
