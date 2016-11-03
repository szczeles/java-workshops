package pl.jeppesen.workshops.flights.testing;

import com.google.common.util.concurrent.Uninterruptibles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Cooperation {
    public static void main(String[] args) {

        TaskQueue taskQueue = new TaskQueue();
        new Thread(new Producer(taskQueue)).start();
        new Thread(new Producer(taskQueue)).start();
        new Thread(new Consumer(taskQueue)).start();
        new Thread(new Consumer(taskQueue)).start();
    }

    public static class Producer implements Runnable {

        private TaskQueue queue;

        public Producer(TaskQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                String task = "New task: " + LocalDateTime.now();
                System.out.println("Produced " + task);
                queue.addTask(task);
                Uninterruptibles.sleepUninterruptibly(random.nextInt(100), TimeUnit.MILLISECONDS);
            }

        }
    }
    public static class Consumer implements Runnable {

        private TaskQueue queue;

        public Consumer(TaskQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                System.out.println("Consumed " + queue.getTask());
                Uninterruptibles.sleepUninterruptibly(random.nextInt(80), TimeUnit.MILLISECONDS);
            }

        }
    }

    public static class TaskQueue {
        private List<String> queue = Collections.synchronizedList(new ArrayList<>());

        void addTask(String task) {
            queue.add(task);
            synchronized (this) {
                notify();
            }
        }

        String getTask() {
            synchronized (this) {
                while (queue.size() == 0) {
                    System.out.println("thread " + Thread.currentThread().getName() + " is waiting");
                    try {
                        wait();
                        System.out.println("thread " + Thread.currentThread().getName() + " notified!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return queue.remove(0);
        }
    }
}
