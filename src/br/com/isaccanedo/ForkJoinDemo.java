package br.com.isaccanedo;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * @author Isac Canedo
 */

public class ForkJoinDemo {

    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool(3);
        int size = 3_900_000;
        double vals[] = new double[size];

        for (int i = 0; i < size; i++)
            vals[i] = (double) i;

        for (int i = 1000; i < 2000; i += 10)
            System.out.format("%.4f ", vals[i]);
        System.out.println();

        SqRootTransform task = new SqRootTransform(vals, 0, size);

        long start = System.currentTimeMillis();

        fjp.invoke(task);
        //task.invoke();

        long end = System.currentTimeMillis();

        System.out.println("Transformar o tempo = " + (end - start));
        System.out.println("Dados transformados:");
        for (int i = 1000; i < 2000; i += 10)
            System.out.format("00%.4f ", vals[i]);

        double nums[] = new double[size];

        for (int i = 0; i < size; i++)
            nums[i] = (double) i;

        for (int i = 1000; i < 2000; i += 10)
            System.out.format("%.4f ", nums[i]);
        System.out.println();

        SqRoot root = new SqRoot(nums);
        start = System.currentTimeMillis();
        root.transform();
        end = System.currentTimeMillis();

        System.out.println("Transformar o tempo = " + (end - start));
        System.out.println("Dados transformados:");
        for (int i = 1000; i < 2000; i += 10)
            System.out.format("00%.4f ", nums[i]);
        System.out.println();
        System.out.println(ForkJoinPool.getCommonPoolParallelism() + " " + fjp.getParallelism());
    }

}

class SqRootTransform extends RecursiveAction {
    final int threshold = 20_000;
    int start, end;
    double[] arr;

    public SqRootTransform(double d[], int s, int e) {
        arr = d;
        start = s;
        end = e;
        // System.out.println("Created SqRootTransform for start=" + start + " and end="
        // + end);
    }

    @Override
    protected void compute() {
        if ((end - start) < threshold) {
            for (int i = start; i < end; i++) {
                arr[i] = Math.sqrt(arr[i]);
            }
        } else {
            int middle = (start + end) / 2;
            invokeAll(new SqRootTransform(arr, start, middle), new SqRootTransform(arr, middle, end));
        }
    }
}

class SqRoot {
    double[] vals;

    SqRoot(double[] nums) {
        vals = nums;
    }

    void transform() {
        for (int i = 0; i < vals.length; i++) {
            vals[i] = Math.sqrt(vals[i]);
        }
    }
}