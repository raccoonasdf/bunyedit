package fun.raccoon.bunyedit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.google.common.base.Predicates;

import fun.raccoon.bunyedit.command.action.IAction;

public class EditorCommandBuilder {
    private String name;
    private List<String> aliases = new ArrayList<>();

    private List<String> usage = new ArrayList<>();
    private List<String> helpLines = new ArrayList<>();

    private Predicate<String[]> requiresOp = Predicates.alwaysTrue();

    private IAction action;

    public EditorCommandBuilder(String name) {
        this.name = name;
    }

    public EditorCommandBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EditorCommandBuilder aliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    public EditorCommandBuilder requiresOp(Predicate<String[]> requiresOp) {
        this.requiresOp = requiresOp;
        return this;
    }

    public EditorCommandBuilder usage(String... usage) {
        this.usage = Arrays.asList(usage);
        return this;
    }

    public EditorCommandBuilder help(String... helpLines) {
        this.helpLines = Arrays.asList(helpLines);
        return this;
    }

    public EditorCommandBuilder action(IAction action) {
        this.action = action;
        return this;
    }

    public EditorCommand build() {
        if (this.usage.isEmpty())
            this.usage.add("");

        return new EditorCommand(name, aliases, usage, helpLines, requiresOp, action);
    }
}
