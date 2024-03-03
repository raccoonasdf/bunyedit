package fun.raccoon.bunyedit.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fun.raccoon.bunyedit.Cursor;
import fun.raccoon.bunyedit.data.selection.Selection;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.server.entity.player.EntityPlayerMP;


@Mixin(value = EntityPlayerMP.class, remap = false)
public abstract class CursorSwingMixin {
    @Inject(method = "swingItem", at = @At("TAIL"))
    private void swingItemCheckCursor(CallbackInfo ci) {
        EntityPlayer player = (EntityPlayer)(Object)this;
        
        if (Cursor.isCursorItem(player.inventory.getCurrentItem()))
            Cursor.select(player, Selection.Slot.PRIMARY);
    }
}
