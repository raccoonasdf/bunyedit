package fun.raccoon.bunyedit.data.mask;

import java.util.function.BiPredicate;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.data.selection.ValidSelection;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.world.chunk.ChunkPosition;

public interface IMaskCommand {
    public String usage();

    public @Nonnull BiPredicate<ValidSelection, ChunkPosition> build(String[] argv)
        throws CommandError;
}
