package pl.jeppesen.workshops.flights.testing;

/**
 * Created by mariusz.strzelecki on 03.11.16.
 */
public class SimpleThreads implements Runnable {

    @Override
    public void run() {
        SimpleThreads.countTo("thread", 10);
    }

    private static void countTo(String message, int i) {
        while (i-- > 0) {
            System.out.println(message + " (" + i + ")");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new SimpleThreads());
        thread.setDaemon(true);
        thread.start();
        countTo("main", 5);
        thread.stop();
    }
}
