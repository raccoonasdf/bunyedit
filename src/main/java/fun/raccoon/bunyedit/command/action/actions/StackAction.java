package fun.raccoon.bunyedit.command.action.actions;

import java.util.Map;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.BlockData;
import fun.raccoon.bunyedit.data.LookDirection;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.DirectionHelper;
import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.util.RelCoords;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.chunk.ChunkPosition;

public class StackAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, EntityPlayer player,
        PlayerData playerData, Selection selection, String[] argv
    ) {
        if (argv.length > 3)
            throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));

        LookDirection lookDir = new LookDirection(player);

        Direction direction = DirectionHelper.from(lookDir);
        int times = 1;
        ChunkPosition offset = PosMath.all(0);
        if (argv.length >= 1) {
            try {
                times = Integer.parseInt(argv[0]);
            } catch (NumberFormatException e) {
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidnumber"));
            }
            if (times < 0)
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidnumber"));
        }
        if (argv.length >= 2) {
            if (!argv[1].equals("^")) {
                direction = DirectionHelper.fromAbbrev(argv[1].toUpperCase());
                if (direction == null)
                    throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invaliddirection"));
            }
        }
        if (argv.length == 3) {
            offset = RelCoords.from(offset, lookDir, argv[2]);
        }
        
        ChunkPosition s1 = playerData.selection.getPrimary();
        ChunkPosition s2 = playerData.selection.getSecondary();
        ChunkPosition sdim = PosMath.add(PosMath.abs(PosMath.sub(s2, s1)), PosMath.all(1));

        offset = PosMath.add(
            offset,
            PosMath.mul(PosMath.directionOffset(direction), sdim)
        );

        BlockBuffer before = selection.copy(false);
        BlockBuffer after = new BlockBuffer();

        BlockBuffer copyBuffer = selection.copy(true);
        for(int i = 0; i < times; ++i) {
            for (Map.Entry<ChunkPosition, BlockData> entry : copyBuffer.entrySet()) {
                ChunkPosition pos = entry.getKey();
                BlockData blockData = entry.getValue();

                pos = PosMath.add(pos, s1);
                pos = PosMath.add(pos, PosMath.mul(offset, PosMath.all(i+1)));

                before.put(pos, new BlockData(player.world, pos));

                after.placeRaw(player.world, pos, blockData);
            }
        }
        after.finalize(player.world);

        playerData.undoTape.push(before, after);

        return true;
    }
}
