package fun.raccoon.bunyedit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.block.BlockSign;

@Mixin(value = BlockSign.class, remap = false)
public interface BlockSignAccessor {
    @Accessor
    public boolean getIsFreestanding();
}
