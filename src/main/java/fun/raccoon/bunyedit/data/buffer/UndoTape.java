package fun.raccoon.bunyedit.data.buffer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A sequence of {@link BlockBuffer} pairs each representing the state of a
 * region before and after an action was performed.
 * <p>
 * Acts somewhat like a stack, but the tape head moves across the tape without
 * popping items, allowing you to {@link #undo} and then {@link #redo} later
 * without losing history as long as the tape isn't edited in the meantime.
 */
public class UndoTape {
    private List<UndoPage> pages = new ArrayList<>();
    private int head = -1;

    /**
     * Pushes a new action to the tape, erasing any available {@link #redo}'s.
     */
    public void push(@Nonnull BlockBuffer before, @Nonnull BlockBuffer after) {
        pages.subList(head + 1, pages.size()).clear();
        pages.add(UndoPage.of(WorldBuffer.of(before, null), WorldBuffer.of(after, null)));
        head += 1;
    }

    /**
     * @return a {@link WorldBuffer} that can be used to undo the most recent
     * action.
     */
    public @Nullable WorldBuffer undo() {
        if (head <= -1)
            return null;
        
        WorldBuffer page = pages.get(head).getLeft();
        head -= 1;

        return page;
    }

    /**
     * @return a {@link WorldBuffer} that can be used to redo the action that
     * was most recently undone.
     */
    public @Nullable WorldBuffer redo() {
        if (head + 1 >= pages.size())
            return null;
        
        head += 1;
        WorldBuffer page = pages.get(head).getRight();

        return page;
    }
}
