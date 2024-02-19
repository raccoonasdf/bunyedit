package fun.raccoon.bunyedit;

import fun.raccoon.bunyedit.data.PlayerData;
import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.util.ChatString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.HitResult;
import net.minecraft.core.HitResult.HitType;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;
import net.minecraft.server.entity.player.EntityPlayerMP;

/**
 * Tool to assist players with inputting selection coordinates.
 */
public class Cursor {
    /** 
     * from serverside's perspective, entity view vectors are stepped to fit into a byte
     * <p>
     * this causes some annoying imprecision if you try to select from extreme distances
     * so, prefer just limiting the range outright
     */
    private static final int TRACE_DISTANCE = 50;

    /**
     * default item used for the cursor, deliberately chosen to be reasonably unique
     * for editor plugins i've used: worldedit prefers the wooden axe, voxelsniper
     * prefers arrow and gunpowder.
     * <p>
     * i chose a feather because it looks vaguely like a mouse pointer if you squint.
     */
    private static final ItemStack DEFAULT_CURSOR_ICON = new ItemStack(Item.featherChicken);

    /**
     * get {@link ItemStack} for the default cursor
     */
    public static ItemStack getCursorItem() {
        ItemStack cursor = DEFAULT_CURSOR_ICON;
        cursor.setCustomName("Cursor");
        cursor.getData().putBoolean("bunyedit.cursor", true);

        return cursor;
    }

    /**
     * does existing {@link ItemStack} carry our cursor tag?
     * <p>
     * for flexibility, deliberately choosing to not be strict about the {@link Item}
     */
    public static boolean isCursorItem(ItemStack stack) {
        return stack != null && stack.getData().getBoolean("bunyedit.cursor");
    }

    /**
     * CommandSender provides sendMessage, which unifies sending raw chat messages
     * to an individual player in the context of both singleplayer and multiplayer.
     * <p>
     * for some reason, {@link EntityPlayer} does not provide this except in the special
     * case of one plain constant translation key. so, we provide
     * {@link net.minecraft.core.net.command.CommandSender}'s API here for convenience.
     */
    private static void sendMessage(EntityPlayer player, String s) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet3Chat(s));
        } else if (player instanceof EntityPlayerSP) {
            Minecraft.getMinecraft((Object)player).ingameGUI.addChatMessage(s);
        }
    }

    /**
     * Trace along player's view vector to find the nearest solid block, and place its
     * coordinates in the specified selection slot. Additionally provide slot,
     * coordinate, and selected block in chat.
     */
    public static void select(EntityPlayer player, Selection.Slot slot) {
        I18n i18n = I18n.getInstance();
        PlayerData playerData = PlayerData.get(player);
        World world = player.world;

        // don't pay attention to duplicate interactions on the same tick
        if (!playerData.lastInteract.tryUpdate(world.getWorldTime()))
            return;

        Vec3d start = player.getPosition(0);
        // EntityPlayerMP doesn't know about its height offset?
        if (player instanceof EntityPlayerMP) {
            if (player.isDwarf())
                start.yCoord += 0.62;
            else
                start.yCoord += 1.62;
        }
        Vec3d direction = player.getViewVector(0);
        Vec3d end = start.addVector(
            direction.xCoord * TRACE_DISTANCE,
            direction.yCoord * TRACE_DISTANCE,
            direction.zCoord * TRACE_DISTANCE);
        HitResult hit = world.checkBlockCollisionBetweenPoints(start, end);
        ChunkPosition pos = new ChunkPosition(hit.x, hit.y, hit.z);

        if (hit != null && hit.hitType.equals(HitType.TILE)) {
            playerData.selection.set(slot, world, pos);

            sendMessage(player, ChatString.gen_select_action(slot, world, pos));
        } else {
            sendMessage(player,
                TextFormatting.formatted(
                    i18n.translateKey("bunyedit.cursor.outofrange"),
                    TextFormatting.RED));
        }
    }
}

