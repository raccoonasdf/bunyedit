package fun.raccoon.bunyedit.command.action.actions;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.BlockData;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.Filter;
import fun.raccoon.bunyedit.util.Pattern;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class SetAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
    ) {
        String patternStr;
        String filterStr;
        switch (argv.length) {
            case 0:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toofewargs"));
            case 1:
                filterStr = null;
                patternStr = argv[0];
                break;
            case 2:
                filterStr = argv[0];
                patternStr = argv[1];
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        Function<BlockData, BlockData> pattern = Pattern.fromString(sender, patternStr);
        if (pattern == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidpattern"));

        Stream<ChunkPosition> stream = selection.coordStream();

        if (filterStr != null) {
            Predicate<BlockData> filter = Filter.fromString(filterStr);
            if (filter == null)
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidfilter"));
            stream = stream
                .filter(pos -> filter.test(new BlockData(player.world, pos)))
                // this might look silly. but we need the filter to be greedy
                //
                // consider //set sugarcane <...>: if we aren't greedy here,
                // the sugarcane will break before we can find it
                .collect(Collectors.toList()).stream();
        }
        
        BlockBuffer before = selection.copy(false);
        BlockBuffer after = new BlockBuffer();

        stream.forEach(pos -> {
            BlockData blockData = pattern.apply(new BlockData(player.world, pos));
            after.placeRaw(player.world, pos, blockData);
        });
        after.finalize(player.world);
        
        playerData.undoTape.push(before, after);

        return true;
    }
}
