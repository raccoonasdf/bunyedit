package fun.raccoon.bunyedit.data.mask.masks;

import java.util.HashMap;
import java.util.Map;

import fun.raccoon.bunyedit.data.mask.IMask;

public class Masks {
    public static final Map<String, IMask> MASKS = new HashMap<>();

    static {
        MASKS.put("cube", new Cuboid(false));
        MASKS.put("hcube", new Cuboid(true));
        MASKS.put("cuboid", new Cuboid(false));
        MASKS.put("hcuboid", new Cuboid(true));

        MASKS.put("sphere", new Ellipsoid(false));
        MASKS.put("hsphere", new Ellipsoid(true));
        MASKS.put("ellipsoid", new Ellipsoid(false));
        MASKS.put("hellipsoid", new Ellipsoid(true));
        
        MASKS.put("line", new Line());
    }
}
