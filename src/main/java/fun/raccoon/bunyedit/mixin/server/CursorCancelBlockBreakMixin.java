package fun.raccoon.bunyedit.mixin.server;

import fun.raccoon.bunyedit.Cursor;
import net.minecraft.core.net.packet.Packet53BlockChange;
import net.minecraft.core.util.helper.Side;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.handler.NetServerHandler;
import net.minecraft.server.world.ServerPlayerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * im gonna be honest i have no idea what im doing
 * <p>
 * but the gist is that we need to assume this mod only exists on serverside
 * and clientside just assumes that when it breaks something it can just go
 * ahead and delete that block from its local world
 * <p>
 * so we need to make sure we send {@link Packet53BlockChange} to not get
 * desynced with annoying ghosts
 */
@Mixin(value = NetServerHandler.class, remap = false)
public abstract class CursorCancelBlockBreakMixin {
    @Shadow
    private EntityPlayerMP playerEntity;
    
    @Redirect(
        method = "handleBlockDig",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerPlayerController;destroyBlock(IIILnet/minecraft/core/util/helper/Side;)Z"))
    private boolean destroyBlockRedirect(
            ServerPlayerController playerController, int x, int y, int z, Side side
    ) {
        if (Cursor.isCursorItem(playerEntity.inventory.getCurrentItem()))
            return false;
        return playerController.destroyBlock(x, y, z, side);
    }

    @Redirect(
        method = "handleBlockDig",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerPlayerController;startMining(IIILnet/minecraft/core/util/helper/Side;)V"))
    private void startMiningRedirect(
            ServerPlayerController playerController, int x, int y, int z, Side side
    ) {
        if (Cursor.isCursorItem(playerEntity.inventory.getCurrentItem())) {
            playerEntity.playerNetServerHandler.sendPacket(
                new Packet53BlockChange(x, y, z, playerEntity.world));

            return;
        }
        
        playerController.startMining(x, y, z, side);
    }
}