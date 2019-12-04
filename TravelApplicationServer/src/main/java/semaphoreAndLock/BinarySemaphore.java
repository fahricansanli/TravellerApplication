package semaphoreAndLock;

public class BinarySemaphore {
    public boolean value;

    public BinarySemaphore(){
        this.value = true;
    }

    public synchronized void P(){
        while (!this.value){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.value = false;
    }

    public synchronized void V(){
        this.value = true;
        notify();
    }
}
