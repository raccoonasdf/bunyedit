package fun.raccoon.bunyedit.command.action;

import java.util.List;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.mask.IMaskCommand;
import fun.raccoon.bunyedit.data.mask.Masks;
import fun.raccoon.bunyedit.data.selection.Selection;
import fun.raccoon.bunyedit.data.selection.ValidSelection;
import fun.raccoon.bunyedit.util.parsers.CmdArgs;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public interface ISelectionAction extends IPlayerAction {
    public boolean apply(I18n i18n, CommandSender sender, @Nonnull EntityPlayer player, PlayerData playerData, ValidSelection selection, List<String> argv);
    
    @Override
    default public boolean apply(I18n i18n, CommandSender sender, @Nonnull EntityPlayer player, PlayerData playerData, List<String> argv) {
        Selection selection = playerData.selection;
        
        int i = 0;
        while (i < argv.size()) {
            switch(argv.get(i)) {
                case "-m":
                    argv.remove(i);
                    try {
                        String maskArgStr = argv.get(i);
                        List<String> maskArgs = CmdArgs.parse(maskArgStr);
                        String maskName = maskArgs.get(0);

                        @Nullable IMaskCommand maskCmd = Masks.MASKS.get(maskName);
                        if (maskCmd == null)
                            throw new CommandError(i18n.translateKeyAndFormat("bunyedit.cmd.mask.err.nosuchmask", maskName));                        

                        String[] maskArgv = {};
                        if (maskArgs.size() >= 1) {
                            maskArgv = maskArgs.subList(1, maskArgs.size()).toArray(new String[0]);
                        }

                        BiPredicate<ValidSelection, ChunkPosition> mask = maskCmd.build(maskArgv);

                        selection = new Selection(selection);
                        selection.setMask(argv.get(i), mask);
                    } catch (IndexOutOfBoundsException e) {
                        throw new CommandError("-m: " + i18n.translateKey("bunyedit.cmd.err.toofewargs"));
                    }
                    argv.remove(i);
                    break;
                default:
                    ++i;
            }
        }

        ValidSelection validSelection = ValidSelection.fromSelection(selection);
        if (validSelection == null || !selection.getWorld().equals(player.world))
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.incompleteselection"));        

        Long limit = playerData.selectionLimit;
        if (limit != null) {
            long vol = validSelection.coordStream().count();
            if (vol > limit)
                throw new CommandError(i18n.translateKeyAndFormat("bunyedit.cmd.err.selectiontoolarge", vol));
        }
        return apply(i18n, sender, player, playerData, validSelection, argv);
    }
}
