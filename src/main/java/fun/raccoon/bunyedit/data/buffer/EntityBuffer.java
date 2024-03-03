package fun.raccoon.bunyedit.data.buffer;

import java.util.List;

import com.mojang.nbt.CompoundTag;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.world.World;

/**
 * Buffer of entities that may or may not exist in the world.
 */
public class EntityBuffer {
    private List<Pair<Entity, CompoundTag>> buffer;

    public void createIn(World world) {
        buffer.forEach(p -> {
            if (p.getLeft() == null) {
                EntityDispatcher.createEntityFromNBT(p.getRight(), world);
            }
        });
    }

    public void destroyIn(World world) {
        buffer.forEach(p -> {
            if (p.getLeft() != null) {
                world.setEntityDead(p.getLeft());
            }
        });
    }

    public void add(Entity entity) throws IllegalArgumentException {
        CompoundTag tag = new CompoundTag();
        if (!entity.save(tag))
            throw new IllegalArgumentException("Tried to add a null or removed entity");
        buffer.add(Pair.of(entity, tag));
    }
}
