package fun.raccoon.bunyedit.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fun.raccoon.bunyedit.Cursor;
import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.client.entity.player.EntityClientPlayerMP;
import net.minecraft.core.entity.player.EntityPlayer;


@Mixin(value = EntityPlayer.class, remap = false)
public abstract class CursorSwingMixin {
    @Inject(method = "swingItem", at = @At("TAIL"))
    private void swingItemCheckCursor(CallbackInfo ci) {
        EntityPlayer player = (EntityPlayer)(Object)this;
        
        // serverside has this covered in multiplayer
        if (!(player instanceof EntityClientPlayerMP)
                && Cursor.isCursorItem(player.inventory.getCurrentItem()))
            Cursor.select(player, Selection.Slot.PRIMARY);
    }
}
