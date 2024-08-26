package fun.raccoon.bunyedit.data.selection;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fun.raccoon.bunyedit.data.buffer.BlockBuffer;
import fun.raccoon.bunyedit.data.buffer.BlockData;
import fun.raccoon.bunyedit.data.buffer.EntityBuffer;
import fun.raccoon.bunyedit.data.buffer.WorldBuffer;
import fun.raccoon.bunyedit.data.selection.Selection.Slot;
import fun.raccoon.bunyedit.util.PosMath;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.chunk.ChunkPosition;

/**
 * Composition over a snapshot of a valid Selection that provides useful methods for
 * interpreting the selection but does not expose the original mutating methods.
 */
public class ValidSelection {
    public class InvalidSelectionException extends Exception {
    }

    private Selection selection;

    public ValidSelection(Selection selection) throws InvalidSelectionException {
        if (selection == null || !selection.isValid())
            throw new InvalidSelectionException();
        
        // a copy, so that it doesn't become invalid underneath us
        this.selection = new Selection(selection);
    }

    public static @Nullable ValidSelection fromSelection(Selection selection) {
        try {
            return new ValidSelection(selection);
        } catch (InvalidSelectionException e) {
            return null;
        }
    }

    public Selection copyInner() {
        return new Selection(this.selection);
    }

    public boolean equals(ValidSelection other) {
        return this.getPrimary().equals(other.getPrimary())
            && this.getSecondary().equals(other.getSecondary())
            && this.selection.getWorld() == other.selection.getWorld()
            && this.selection.getMask() == other.selection.getMask();
    }

    @SuppressWarnings("null") // presupposed by constructor
    public @Nonnull ChunkPosition get(Selection.Slot slot) {
        return this.selection.get(slot);
    }

    public @Nonnull ChunkPosition getPrimary() {
        return this.get(Slot.PRIMARY);
    }

    public @Nonnull ChunkPosition getSecondary() {
        return this.get(Slot.SECONDARY);
    }

    private Stream<Integer> rangeClosed(int from, int to) {
        return IntStream.rangeClosed(Math.min(from, to), Math.max(from, to)).boxed();
    }

    /**
     * @return a Stream of every coordinate triple inside the selection
     */
    public Stream<ChunkPosition> coordStream() {
        ChunkPosition primary = this.getPrimary();
        ChunkPosition secondary = this.getSecondary();

        return this.rangeClosed(primary.x, secondary.x).flatMap(
            x -> this.rangeClosed(primary.y, secondary.y).flatMap(
                y -> this.rangeClosed(primary.z, secondary.z).map(
                    z -> new ChunkPosition(x, y, z))))
            .filter(pos -> this.isOver(pos));
    }

    public boolean isOver(ChunkPosition pos) {
        return this.selection.getMask().test(this, pos);
    }

    /**
     * @param relative whether to use relative coordinates (where the primary
     * selection is considered [0, 0, 0])
     * @return a {@link BlockBuffer} representing every block in this selection.
     */
    public @Nonnull BlockBuffer copy(boolean relative) {
        ChunkPosition origin = this.getPrimary();
        BlockBuffer page = new BlockBuffer();

        this.coordStream().forEach(pos -> {
            page.put(
                relative ? PosMath.sub(pos, origin) : pos,
                new BlockData(this.selection.getWorld(), pos)
            );
        });

        return page;
    }

    public @Nonnull WorldBuffer copyWorld(boolean relative) {
        ChunkPosition min = PosMath.min(this.getPrimary(), this.getSecondary());
        ChunkPosition max = PosMath.max(this.getPrimary(), this.getSecondary());

        List<Entity> entities = this.selection.getWorld().getEntitiesWithinAABB(
            Entity.class,
            new AABB(
                min.x, min.y, min.z,
                max.x, max.y, max.z));

        EntityBuffer page = new EntityBuffer();
        
        for (Entity entity : entities) {
            if (this.isOver(new ChunkPosition((int)entity.x, (int)entity.y, (int)entity.z))) {
                page.add(entity);
            }
        }

        return new WorldBuffer(this.copy(relative), page);
    }
}
