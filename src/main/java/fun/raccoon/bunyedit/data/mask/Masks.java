package fun.raccoon.bunyedit.data.mask;

import java.util.HashMap;
import java.util.Map;

import fun.raccoon.bunyedit.data.mask.masks.Cuboid;
import fun.raccoon.bunyedit.data.mask.masks.Ellipsoid;
import fun.raccoon.bunyedit.data.mask.masks.Line;

public class Masks {
    public static final Map<String, IMaskCommand> MASKS = new HashMap<>();

    static {
        MASKS.put("cube", new Cuboid());
        MASKS.put("sphere", new Ellipsoid());
        MASKS.put("line", new Line());
    }
}
