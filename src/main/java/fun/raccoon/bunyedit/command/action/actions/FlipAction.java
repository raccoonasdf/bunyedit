package fun.raccoon.bunyedit.command.action.actions;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.command.action.ISelectionAction;
import fun.raccoon.bunyedit.data.BlockBuffer;
import fun.raccoon.bunyedit.data.LookAxis;
import fun.raccoon.bunyedit.data.LookDirection;
import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.ValidSelection;
import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.util.Reorient;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.world.chunk.ChunkPosition;

public class FlipAction implements ISelectionAction {
    public boolean apply(
        I18n i18n, CommandSender sender, @Nonnull EntityPlayer player,
        PlayerData playerData, ValidSelection selection, String[] argv
    ) {
        Axis axis = new LookDirection(player.yRot, player.xRot).globalAxis(LookAxis.SURGE);
        switch (argv.length) {
            case 0: break;
            case 1:
                if (argv[1] == "^")
                    break;
                try {
                    Axis.valueOf(argv[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invaliddirection"));
                }
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }

        ChunkPosition s1 = selection.getPrimary();
        ChunkPosition s2 = selection.getSecondary();

        int[] s1a = PosMath.toArray(s1);
        int[] s2a = PosMath.toArray(s2);

        int side1 = s1a[axis.ordinal()];
        int side2 = s2a[axis.ordinal()];

        BlockBuffer before = selection.copy(false);
        BlockBuffer after = new BlockBuffer();

        BlockBuffer copyBuffer = selection.copy(false);
        copyBuffer.forEach((pos, blockData) -> {
            blockData = Reorient.flipped(blockData, axis);

            int[] posa = PosMath.toArray(pos);
            posa[axis.ordinal()] = side2 - (posa[axis.ordinal()] - side1);
            pos = PosMath.fromArray(posa);

            after.placeRaw(player.world, pos, blockData);
        });
        after.finalize(player.world);
        
        playerData.getUndoTape(player.world).push(before, after);

        return true;
    }
}
