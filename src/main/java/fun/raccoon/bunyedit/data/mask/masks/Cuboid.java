package fun.raccoon.bunyedit.data.mask.masks;

import java.util.function.BiPredicate;

import fun.raccoon.bunyedit.data.Selection;
import fun.raccoon.bunyedit.data.mask.IMaskCommand;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.world.chunk.ChunkPosition;

public class Cuboid implements IMaskCommand {
    public String usage() {
        return "[h]";
    }

    public BiPredicate<Selection, ChunkPosition> build(String[] argv) {
        I18n i18n = I18n.getInstance();
        
        switch (argv.length) {
            case 0:
                return (selection, pos) -> true;
            case 1:
                if (!argv[0].equals("h"))
                    throw new CommandError(i18n.translateKey("bunyedit.cmd.err.invalidhollow"));
                return (selection, pos) -> {
                    ChunkPosition s1 = selection.getPrimary();
                    ChunkPosition s2 = selection.getSecondary();

                    return pos.x == s1.x || pos.x == s2.x || pos.y == s1.y || pos.y == s2.y || pos.z == s1.z || pos.z == s2.z;
                };
            default:
                throw new CommandError(i18n.translateKey("bunyedit.cmd.err.toomanyargs"));
        }
    }
}
