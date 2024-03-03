package fun.raccoon.bunyedit.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import fun.raccoon.bunyedit.util.PosMath;
import fun.raccoon.bunyedit.command.action.ISelectionAction;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;

/**
 * Pair of coordinates representing a cuboid region of the world.
 * <p>
 * May be in an incomplete state (one or both corner positions are null). Nullable getters are
 * not annotated as such. Make sure to check {@link #isValid} if you need to. Implementers of
 * {@link ISelectionAction} do not need to worry about this.
 */
public class Selection {
    /**
     * Identifier for one of the two defining corners of a selection.
     */
    public enum Slot {
        PRIMARY,
        SECONDARY;

        /**
         * The other corner.
         */
        public Slot other() {
            switch (this) {
                case PRIMARY:
                    return SECONDARY;
                case SECONDARY:
                    return PRIMARY;
                default:
                    return null;
            }
        }
    }

    private Map<Slot, ChunkPosition> selection;
    private World world;
    // this is not part of mask because we might someday want to set a programmatic
    // BiPredicate that doesn't come from any particular IMaskCommand
    private String maskName = "cube";
    private BiPredicate<Selection, ChunkPosition> mask = (selection, pos) -> true;

    public Selection() {
        this.selection = new HashMap<>();
        this.selection.put(Slot.PRIMARY, null);
        this.selection.put(Slot.SECONDARY, null);        
    }

    public Selection(Selection selection) {
        this.selection = selection.selection;
        this.world = selection.world;
        this.mask = selection.mask;
    }

    public boolean equals(Selection other) {
        return this.getPrimary().equals(other.getPrimary())
            && this.getSecondary().equals(other.getSecondary())
            && this.world == other.world
            && this.mask == other.mask;
    }

    public ChunkPosition get(Slot slot) {
        return this.selection.get(slot);
    }

    public void set(Slot slot, World world, ChunkPosition coords) {
        if (this.world != world) {
            this.world = world;
            this.set(slot.other(), world, null);
        }
        this.selection.put(slot, coords);
    }

    public ChunkPosition getPrimary() {
        return this.get(Slot.PRIMARY);
    }

    public void setPrimary(World world, ChunkPosition coords) {
        this.set(Slot.PRIMARY, world, coords);
    }

    public ChunkPosition getSecondary() {
        return this.get(Slot.SECONDARY);
    }

    public ChunkPosition getRelSecondary() {
        ChunkPosition s1 = this.getPrimary();
        ChunkPosition s2 = this.getSecondary();

        return PosMath.sub(s2, s1);
    }

    public void setSecondary(World world, ChunkPosition coords) {
        this.set(Slot.SECONDARY, world, coords);
    }

    public void setMask(String name, BiPredicate<Selection, ChunkPosition> mask) {
        this.maskName = name;
        this.mask = mask;
    }

    public String getMaskName() {
        return this.maskName;
    }

    public BiPredicate<Selection, ChunkPosition> getMask() {
        return this.mask;
    }

    /**
     * @return true if both corners are set.
     */
    public boolean isValid() {
        return this.getPrimary() != null && this.getSecondary() != null;
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
            .filter(pos -> mask.test(this, pos));
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
                new BlockData(this.world, pos)
            );
        });

        return page;
    }

    public void setBound(@Nonnull Pair<ChunkPosition, ChunkPosition> bound) {
        this.setPrimary(this.world,
            PosMath.add(this.getPrimary(), bound.getLeft()));
        this.setSecondary(this.world,
            PosMath.add(this.getSecondary(), bound.getRight()));
    }
}
