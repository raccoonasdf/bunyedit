package fun.raccoon.bunyedit.command.action.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.buffer.BlockBuffer;
import fun.raccoon.bunyedit.data.buffer.BlockData;
import fun.raccoon.bunyedit.data.selection.ValidSelection;
import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.util.parsers.Bound;
import fun.raccoon.bunyedit.util.parsers.Filter;
import fun.raccoon.bunyedit.util.parsers.Pattern;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;

public class FillAction implements ISelectionAction {
    private void recurse(
        World world,
        Set<ChunkPosition> set,
        Predicate<BlockData> filter,
        Pair<ChunkPosition, ChunkPosition> bound,
        ChunkPosition origin,
        ChunkPosition pos
    ) {
        if (
            PosMath.inside(bound.getLeft(), bound.getRight(), PosMath.sub(pos, origin))
            && filter.test(new BlockData(world, pos))
        ) {
            set.add(pos);
            for (Direction dir : Direction.directions) {
                ChunkPosition newPos = PosMath.add(pos, PosMath.directionOffset(dir));

                if (!set.contains(newPos))
                    recurse(world, set, filter, bound, origin, newPos);
            }
        }        
    }

    @Override
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, ValidSelection selection, List<String> argv
    ) {
        if (argv.size() < 2)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toofewargs"));
        if (argv.size() > 3)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        
        Predicate<BlockData> filter = Filter.fromString(argv.get(0));
        if (filter == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidfilter"));   
            
        Function<BlockData, BlockData> pattern = Pattern.fromString(sender, argv.get(1));
        if (pattern == null)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidpattern"));
     
        Pair<ChunkPosition, ChunkPosition> bound;
        if (argv.size() == 3) {
            bound = Bound.fromString(selection, player, "*16,"+argv.get(2));
            if (bound == null)
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidbound"));        
        } else {
            bound = Bound.fromString(selection, player, "*16");
        }

        Set<ChunkPosition> set = new HashSet<>();
        for (ChunkPosition pos : selection.coordStream().collect(Collectors.toSet())) {
            recurse(player.world, set, filter, bound, pos, pos);
        }
        
        BlockBuffer before = new BlockBuffer();
        BlockBuffer after = new BlockBuffer();

        set.forEach(pos -> {
            BlockData blockData = new BlockData(player.world, pos);
            before.put(pos, blockData);
            after.placeRaw(player.world, pos, pattern.apply(blockData));
        });
        after.finalize(player.world);
        
        playerData.getUndoTape(player.world).push(before, after);

        return true;
    }
}
