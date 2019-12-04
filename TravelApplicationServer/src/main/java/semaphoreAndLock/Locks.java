package semaphoreAndLock;

import java.util.HashMap;

public class Locks {
    public static BinarySemaphore userSemaphore = new BinarySemaphore();
    public static HashMap<String,BinarySemaphore> commentLocks = new HashMap<String,BinarySemaphore>();
}
