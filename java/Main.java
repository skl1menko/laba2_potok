import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int size = 10000;
        int[] array = new int[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(1000);
        }
        array[rand.nextInt(size)] = -rand.nextInt(100); // мінімум

        int threadCount = 100;
        Thread[] threads = new Thread[threadCount];
        MinResult result = new MinResult();
        //розбиття масиву на частини рівні кількості потоків
        int chunkSize = size / threadCount;

        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            //рівномірне розбиття масиву
            //останній потік обробляє залишок масиву
            int end = (i == threadCount - 1) ? size : start + chunkSize;
            threads[i] = new MinFinder(array, start, end, result);
            threads[i].start();
        }

        while (true) {
            boolean allDone = true;
            for (Thread t : threads) {
                if (t.isAlive()) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) break;
        }

        System.out.println("Minimum value: " + result.minValue);
        System.out.println("Index: " + result.minIndex);
    }
}
