package fun.raccoon.bunyedit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fun.raccoon.bunyedit.Cursor;
import fun.raccoon.bunyedit.data.Selection;
import net.minecraft.client.entity.player.EntityClientPlayerMP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

@Mixin(value = Item.class, remap = false)
public abstract class CursorRightClickMixin {
    @Inject(method = "onItemRightClick", at = @At("TAIL"))
    private void onItemRightClickCheckCursor(ItemStack itemstack, World world, EntityPlayer entityplayer, CallbackInfoReturnable<ItemStack> cir) {
        // serverside has this covered in multiplayer
        if (!(entityplayer instanceof EntityClientPlayerMP)
                && Cursor.isCursorItem(itemstack))
            Cursor.select(entityplayer, Selection.Slot.SECONDARY);
    }
}
