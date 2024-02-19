package fun.raccoon.bunyedit.data;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.entity.player.EntityPlayer;

/**
 * Edit-session data for an individual player.
 */
public class PlayerData {
    private static final Map<String, PlayerData> playerMap = new HashMap<>();

    public static PlayerData get(String username) {
        PlayerData playerData = PlayerData.playerMap.get(username);
        if (playerData == null) {
            playerData = new PlayerData();
            PlayerData.playerMap.put(username, playerData);
        }

        return playerData;
    }

    public static PlayerData get(EntityPlayer player) {
        return PlayerData.get(player.username);
    }

    /**
     * Selection specified using the cursor.
     */
    public Selection selection = new Selection();

    /**
     * World time of last cursor interaction, for debouncing purposes.
     * This seems to be necessary in multiplayer, as `swingItem` is forced
     * twice in the same moment when the player is (1) in creative and (2) in
     * arms' reach of their target block.
     */
    public LastInteract lastInteract = new LastInteract();

    /**
     * Undo history for this player's actions.
     */
    public UndoTape undoTape = new UndoTape();

    /**
     * Buffer for //copy and //paste.
     */
    public BlockBuffer copyBuffer = new BlockBuffer();
}
