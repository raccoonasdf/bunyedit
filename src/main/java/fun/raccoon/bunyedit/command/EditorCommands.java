package fun.raccoon.bunyedit.command;

import java.util.ArrayList;
import java.util.List;

import fun.raccoon.bunyedit.command.action.actions.*;
import fun.raccoon.bunyedit.command.action.actions.UndoRedoAction.Which;
import fun.raccoon.bunyedit.data.Selection.Slot;

public class EditorCommands {
    public static final List<EditorCommand> LIST = new ArrayList<>();

    static {
        LIST.add(
            new EditorCommandBuilder("copy")
                .action(new CopyAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("cursor")
                .aliases("cur")
                .action(new CursorAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("flip")
                .usage("[axis]")
                .help("bunyedit.cmd.flip.help.1")
                .action(new FlipAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("selection")
                .aliases("sel")
                .action(new GetSelectionAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("growsel")
                .usage("[bound=*1]")
                .action(new GrowSelectionAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("move")
                .usage("<x>,<y>,<z>")
                .action(new MoveAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("movesel")
                .usage("<x>,<y>,<z>")
                .action(new MoveSelAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("paste")
                .action(new PasteAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("set")
                .aliases("replace")
                .usage("[filter] <pattern>")
                .action(new SetAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("mask")
                .usage("<shape>", "list")
                .action(new SetMaskAction())
                .build()
        );
        EditorCommandBuilder setSel =
            new EditorCommandBuilder(null)
                .usage("", "<x>,<y>,<z>")
                .help(
                    "bunyedit.cmd.setsel.help.1",
                    "bunyedit.cmd.setsel.help.2");
        LIST.add(setSel.name("1").action(new SetSelectionAction(Slot.PRIMARY)).build());
        LIST.add(setSel.name("2").action(new SetSelectionAction(Slot.SECONDARY)).build());
        LIST.add(
            new EditorCommandBuilder("stack")
                .usage("[times] [direction] [offset]")
                .help(
                    "bunyedit.cmd.stack.help.1",
                    "bunyedit.cmd.stack.help.2",
                    "bunyedit.cmd.stack.help.3")
                .action(new StackAction())
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("undo")
                .action(new UndoRedoAction(Which.UNDO))
                .build()
        );
        LIST.add(
            new EditorCommandBuilder("redo")
                .action(new UndoRedoAction(Which.REDO))
                .build()
        );
    }
}
