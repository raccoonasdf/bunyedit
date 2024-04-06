package fun.raccoon.bunyedit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.world.World;

@Mixin(value = World.class, remap = false)
public interface WorldAccessor {
    @Accessor
    public long getRuntime();
}
