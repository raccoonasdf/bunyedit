package fun.raccoon.bunyedit.command;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.BlockData;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.world.chunk.ChunkPosition;

public class SetCommand extends Command {
    public SetCommand() { super("/set", "/s", "/replace", "/re"); }

    public boolean opRequired(String[] argv) { return true; }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("set|s|replace|re [filter] <pattern>");
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] argv) {
        I18n i18n = I18n.getInstance();

        EntityPlayer player = sender.getPlayer();
        if (sender.getPlayer() == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.notaplayer"));
        PlayerData playerData = PlayerData.get(player);

        Selection selection = playerData.selection;
        if (!selection.isValid())
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.incompleteselection"));
        
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
            after.put(pos, new BlockData(player.world, pos));
            blockData.place(player.world, pos);
        });
        playerData.undoTape.push(before, after);

        return true;
    }
}
