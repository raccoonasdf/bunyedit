package fun.raccoon.bunyedit.command;

import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.mask.IMask;
import fun.raccoon.bunyedit.data.mask.masks.Masks;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

public class MaskCommand extends Command {
    public MaskCommand() { super("/mask"); }

    public boolean opRequired(String[] argv) { return true; }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("mask <shape>");
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] argv) {
        I18n i18n = I18n.getInstance();
        
        EntityPlayer player = sender.getPlayer();
        if (sender.getPlayer() == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.notaplayer"));
        PlayerData playerData = PlayerData.get(player);

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
