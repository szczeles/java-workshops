package pl.jeppesen.workshops.flights.testing;

import com.google.common.base.Stopwatch;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mariusz.strzelecki on 03.11.16.
 */
public class StackManager implements Runnable {

    private Stack stack;
    private int maxFill;

    public StackManager(Stack stack, int maxFill) {
        this.stack = stack;
        this.maxFill = maxFill;
    }

    @Override
    public void run() {
        System.out.println("manager start");
        for (int i = 0; i < maxFill; i++) {
            stack.push(i);
        }
        for (int i = 0; i < maxFill; i++) {
            stack.pop();
        }
        System.out.println("manager end");
    }

    public static class Stack {
        private AtomicInteger index = new AtomicInteger(0);
        public static int MAX = 100_000_000;
        int[] tab = new int[MAX];

        void push(int i) {
            tab[index.getAndIncrement()] = i * i / (i+1);
        }

        int pop() {
            return tab[index.decrementAndGet()];
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Stack stack = new Stack();
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread manager1 = new Thread(new StackManager(stack, Stack.MAX/2));
        manager1.start();
        Thread manager2 = new Thread(new StackManager(stack, Stack.MAX/2));
        manager2.start();
        manager1.join();
        manager2.join();

        System.out.println(stopwatch);

    }
}
