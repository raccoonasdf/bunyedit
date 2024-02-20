package fun.raccoon.bunyedit.command.action.actions;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.BlockData;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
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
        String pattern;
        String filter;
        switch (argv.length) {
            case 0:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.set.err.nopattern"));
            case 1:
                filter = null;
                pattern = argv[0];
                break;
            case 2:
                filter = argv[0];
                pattern = argv[1];
                break;
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        BlockData blockData = BlockData.fromString(pattern);
        if (blockData == null)
            throw new CommandError(i18n.translateKeyAndFormat("bunyedit.cmd.err.nosuchblock", pattern));

        Stream<ChunkPosition> stream = selection.coordStream();

        if (filter != null) {
            stream = stream
                .filter(pos -> {
                    BlockData filterData = BlockData.fromString(filter);
                    if (filterData == null)
                        throw new CommandError(i18n.translateKeyAndFormat("bunyedit.cmd.err.nosuchblock", filter));

                    return filterData.idMetaMatches(player.world, pos);
                })
                // this might look silly. but we need the filter to be greedy
                //
                // consider //set sugarcane <...>: if we aren't greedy here,
                // the sugarcane will break before we can find it
                .collect(Collectors.toList()).stream();
        }
        
        BlockBuffer before = selection.copy(false);
        BlockBuffer after = new BlockBuffer();

        stream.forEach(pos -> {
            blockData.place(player.world, pos);
            after.put(pos, new BlockData(player.world, pos));
        });
        playerData.undoTape.push(before, after);

        return true;
    }
}