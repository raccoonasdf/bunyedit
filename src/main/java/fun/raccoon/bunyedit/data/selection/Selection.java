package fun.raccoon.bunyedit.data.selection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    private BiPredicate<ValidSelection, ChunkPosition> mask = (selection, pos) -> true;

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

    public @Nullable ChunkPosition get(Slot slot) {
        return this.selection.get(slot);
    }

    public void set(Slot slot, World world, ChunkPosition coords) {
        if (this.world != world) {
            this.world = world;
            this.set(slot.other(), world, null);
        }
        this.selection.put(slot, coords);
    }

    public @Nullable ChunkPosition getPrimary() {
        return this.get(Slot.PRIMARY);
    }

    public void setPrimary(World world, ChunkPosition coords) {
        this.set(Slot.PRIMARY, world, coords);
    }

    public @Nullable ChunkPosition getSecondary() {
        return this.get(Slot.SECONDARY);
    }

    public void setSecondary(World world, ChunkPosition coords) {
        this.set(Slot.SECONDARY, world, coords);
    }

    public void setMask(String name, @Nonnull BiPredicate<ValidSelection, ChunkPosition> mask) {
        this.maskName = name;
        this.mask = mask;
    }

    public String getMaskName() {
        return this.maskName;
    }

    public BiPredicate<ValidSelection, ChunkPosition> getMask() {
        return this.mask;
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * @return true if both corners are set.
     */
    public boolean isValid() {
        return this.getPrimary() != null && this.getSecondary() != null;
    }

    public void setBound(@Nonnull Pair<ChunkPosition, ChunkPosition> bound) {
        this.setPrimary(this.world,
            PosMath.add(this.getPrimary(), bound.getLeft()));
        this.setSecondary(this.world,
            PosMath.add(this.getSecondary(), bound.getRight()));
    }
}
