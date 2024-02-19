package fun.raccoon.bunyedit.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fun.raccoon.bunyedit.Cursor;
import net.minecraft.client.player.controller.PlayerControllerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;

@Mixin(value = PlayerControllerSP.class, remap = false)
public abstract class CursorCancelBlockBreakMixin {
    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void destroyBlockCancel(
        int x, int y, int z, Side side, EntityPlayer player,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (Cursor.isCursorItem(player.inventory.getCurrentItem())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
