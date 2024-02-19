package fun.raccoon.bunyedit.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fun.raccoon.bunyedit.command.*;
import fun.raccoon.bunyedit.data.Selection.Slot;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.Commands;

@Mixin(value = Commands.class, remap = false)
public abstract class InitCommandsMixin {
    @Shadow public static List<Command> commands;

    @Inject(method = "initCommands", at = @At("TAIL"))
    private static void initMyCommands(CallbackInfo ci) {
        commands.add(new CursorCommand());
        commands.add(new SetCommand());
        commands.add(new GetSelectionCommand());
        commands.add(new UndoCommand());
        commands.add(new RedoCommand());
        commands.add(new CopyCommand());
        commands.add(new PasteCommand());
        commands.add(new MaskCommand());
        commands.add(new SetSelectionCommand(1, Slot.PRIMARY));
        commands.add(new SetSelectionCommand(2, Slot.SECONDARY));
    }
}
