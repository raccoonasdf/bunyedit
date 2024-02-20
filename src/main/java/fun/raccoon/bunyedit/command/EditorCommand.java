package fun.raccoon.bunyedit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import fun.raccoon.bunyedit.command.action.IAction;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

public class EditorCommand extends Command {
    private List<String> usage = new ArrayList<>();
    private List<String> help = new ArrayList<>();

    private Predicate<String[]> requiresOp;

    private IAction action;

    protected EditorCommand(
        String name,
        List<String> aliases,

        List<String> usage,
        List<String> helpLines,

        Predicate<String[]> requiresOp,
        IAction action
    ) {
        super("/"+name, aliases.stream().map(alias -> "/"+alias).toArray(String[]::new));

        for (String line : usage) {
            this.usage.add(String.format("//%s %s", name, line));
        }

        this.help.addAll(helpLines);

        this.requiresOp = requiresOp;

        this.action = action;
    }

    public boolean opRequired(String[] argv) {
        return requiresOp.test(argv);
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        I18n i18n = I18n.getInstance();

        for (String line : usage) {
            sender.sendMessage(line);
        }
        for (String line : help) {
            sender.sendMessage(i18n.translateKey(line));
        }
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] argv) {
        return this.action.apply(I18n.getInstance(), sender, argv);
    }
}
