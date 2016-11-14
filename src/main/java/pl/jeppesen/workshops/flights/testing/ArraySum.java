package pl.jeppesen.workshops.flights.testing;

import com.google.common.base.Stopwatch;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ArraySum extends RecursiveTask<Long> {
    public static final int THRESHOLD = 5000;
    private int[] arr;
    private int from;
    private int to;

    public ArraySum(int[] arr, int from, int to) {
        this.arr = arr;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Long compute() {
        if (to - from < THRESHOLD) {
            Long sum = 0L;
            for (int i = from; i < to; i++) {
                sum += arr[i];
            }
            return sum;
        } else {
            int mid = (from + to) >> 1;
            ArraySum left = new ArraySum(arr, from, mid);
            ArraySum right = new ArraySum(arr, mid, to);
            left.fork();
            long rightResult = right.compute();
            return left.join() + rightResult;
        }
    }

    public static long sumArray(int[] array) {
        long result = 0;
        for (int i = 0; i < array.length; i++) {
            result += array[i];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] ints = new int[100000000];
        for (int i = 0; i<ints.length; i++) {
            ints[i] = i+1;
        }

        Stopwatch sw = Stopwatch.createStarted();
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        //System.out.println(new ArraySum(ints, 0, ints.length).compute());
        System.out.println(forkJoinPool.invoke(new ArraySum(ints, 0, ints.length)));
        System.out.println(sw);
    }
}
