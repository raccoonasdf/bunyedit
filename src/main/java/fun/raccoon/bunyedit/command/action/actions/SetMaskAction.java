package fun.raccoon.bunyedit.command.action.actions;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fun.raccoon.bunyedit.command.action.IPlayerAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.ValidSelection;
import fun.raccoon.bunyedit.data.mask.IMaskCommand;
import fun.raccoon.bunyedit.data.mask.Masks;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class SetMaskAction implements IPlayerAction {
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, String[] argv
    ) {
        String maskName;
        switch (argv.length) {
            case 0:
                sender.sendMessage(String.format("%s: %s",
                    i18n.translateKey("bunyedit.cmd.mask.current"),
                    playerData.selection.getMaskName()));
                return true;
            default:
                maskName = argv[0];
                break;
        }

        if (maskName.equals("list")) {
            sender.sendMessage(String.format("%s:",
                i18n.translateKey("bunyedit.cmd.mask.list.header")));
            
            for (Entry<String, IMaskCommand> entry : Masks.MASKS.entrySet()) {
                sender.sendMessage(String.format("%s %s",
                    entry.getKey(),
                    entry.getValue().usage()));
            }
            
            return true;
        }

        @Nullable IMaskCommand maskCmd = Masks.MASKS.get(maskName);
        if (maskCmd == null)
            throw new CommandError(i18n.translateKeyAndFormat("bunyedit.cmd.mask.err.nosuchmask", maskName));

        String[] maskArgv = {};
        if (argv.length >= 1) {
            maskArgv = Arrays.copyOfRange(argv, 1, argv.length);
        }

        BiPredicate<ValidSelection, ChunkPosition> mask = maskCmd.build(maskArgv);

        playerData.selection.setMask(maskName+" "+Arrays.stream(maskArgv).collect(Collectors.joining(" ")), mask);

        sender.sendMessage(i18n.translateKey("bunyedit.cmd.mask.success"));

        return true;
    }
}
