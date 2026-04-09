package multithreading;

/**
 * Utility class for common multithreading patterns.
 */
public class ThreadUtil {

    /**
     * Sleeps for the specified number of milliseconds.
     * Handles InterruptedException by re-interrupting the current thread.
     * 
     * @param milliseconds Time to sleep in ms
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted during sleep: " + Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        }
    }
}
