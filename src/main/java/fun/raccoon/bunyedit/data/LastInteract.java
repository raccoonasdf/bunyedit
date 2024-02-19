package fun.raccoon.bunyedit.data;

/**
 * Utility for debouncing interactions that occur on the same time-step.
 */
public class LastInteract {
    private long lastInteract = 0;

    /**
     * Updates the time-step.
     * @param now the time-step to write
     * @return true if new time-step is different from the old one
     */
    public boolean tryUpdate(long now) {
        boolean canUpdate = now != this.lastInteract;
        this.lastInteract = now;

        return canUpdate;
    }
}
