package fun.raccoon.bunyedit.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A sequence of {@link BlockBuffer} pairs each representing the state of a
 * region before and after an action was performed.
 * <p>
 * Acts somewhat like a stack, but the tape head moves across the tape without
 * popping items, allowing you to {@link #undo} and then {@link #redo} later
 * without losing history as long as the tape isn't edited in the meantime.
 */
public class UndoTape {
    private List<BlockBuffer[]> pages = new ArrayList<>();
    private int head = -1;

    /**
     * Pushes a new action to the tape, erasing any available {@link #redo}'s.
     */
    public void push(BlockBuffer before, BlockBuffer after) {
        pages.subList(head + 1, pages.size()).clear();
        pages.add(new BlockBuffer[]{before, after});
        head += 1;
    }

    /**
     * @return a {@link BlockBuffer} that can be used to undo the most recent
     * action.
     */
    public BlockBuffer undo() {
        if (head <= -1)
            return null;
        
        BlockBuffer page = pages.get(head)[0];
        head -= 1;

        return page;
    }

    /**
     * @return a {@link BlockBuffer} that can be used to redo the action that
     * was most recently undone.
     */
    public BlockBuffer redo() {
        if (head + 1 >= pages.size())
            return null;
        
        head += 1;
        BlockBuffer page = pages.get(head)[1];

        return page;
    }
}
