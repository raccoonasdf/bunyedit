package fun.raccoon.bunyedit.data.mask;

import java.util.function.BiPredicate;

import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.world.chunk.ChunkPosition;

public interface IMaskCommand {
    public String usage();

    public BiPredicate<Selection, ChunkPosition> build(String[] argv)
        throws CommandError;
}
