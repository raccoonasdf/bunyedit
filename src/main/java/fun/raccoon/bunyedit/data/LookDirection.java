package fun.raccoon.bunyedit.data;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;

import fun.raccoon.bunyedit.util.DirectionHelper;


/**
 * Determines a mapping from [x, y, z] to [sway, heave, surge]
 */
public class LookDirection {
    private Map<LookAxis, Axis> map = new HashMap<>();
    private Map<LookAxis, Boolean> invMap = new HashMap<>();
    
    private static int[] snapRot(float yRot, float xRot) {
        yRot = (yRot % 360F);
        if (yRot < 0)
            yRot = yRot + 360F;

        int yRotInt = Math.round(yRot/90F)%4;
        int xRotInt = Math.round(xRot/90F);

        return new int[]{yRotInt, xRotInt};
    }

    public LookDirection(float yRot, float xRot) {
        int[] rotInt = snapRot(yRot, xRot);

        int yRotInt = rotInt[0];
        int xRotInt = rotInt[1];

        // 0=sway 1=heave 2=surge
        //int[] map = new int[3];
        //boolean[] invMap = new boolean[3];

        // heading along z on even, x on odd
        Axis yRotDir = (yRotInt&1) == 0 ? Axis.Z : Axis.X;
        // middle semicircle heads negative
        boolean invYRotDir = (yRotInt&1) != ((yRotInt&2)>>1);

        // swaying along x on even, z on odd
        map.put(LookAxis.SWAY, (yRotInt&1) == 0 ? Axis.X : Axis.Z);
        // latter semicircle sways negative
        invMap.put(LookAxis.SWAY, (yRotInt&2) == 2);


        // looking up or down surges along Y
        map.put(LookAxis.SURGE, (xRotInt != 0) ? Axis.Y : yRotDir);
        // looking up surges negative
        invMap.put(LookAxis.SURGE, (xRotInt != 0) ? (xRotInt == 1) : invYRotDir);

        // looking up or down heaves along heading
        map.put(LookAxis.HEAVE, (xRotInt != 0) ? yRotDir : Axis.Y);
        // looking up heaves negative
        invMap.put(LookAxis.HEAVE, (xRotInt != 0) ? (xRotInt == -1)^invYRotDir : false);
    }

    public LookDirection(EntityPlayer player) {
        this(player.yRot, player.xRot);
    }

    /**
     * Mapping from local coordinate axes to global ones.
     */
    public Axis globalAxis(LookAxis localComponent) {
        return map.get(localComponent);
    }

    /**
     * Whether a local component is heading negative in its global axis mapping.
     */
    public boolean globalInv(LookAxis localComponent) {
        return invMap.get(localComponent);
    }

    public Direction global() {
        return DirectionHelper.from(this);
    }
}
