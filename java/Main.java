import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        int size = 10000;
        int[] array = new int[size];
        Random rand = new Random();

        // Генерація випадкового масиву
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(1000);
        }
        array[rand.nextInt(size)] = -rand.nextInt(100); // Випадковий мінімум

        int threadCount = 100;
        Thread[] threads = new Thread[threadCount];
        MinResult result = new MinResult();

        CountDownLatch latch = new CountDownLatch(threadCount); // Синхронізація завершення потоків

        int chunkSize = size / threadCount;

        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            int end = (i == threadCount - 1) ? size : start + chunkSize;

            // Кожен потік зменшує лічильник після завершення
            threads[i] = new Thread(() -> {
                int localMin = array[start];
                int localIndex = start;
                for (int j = start + 1; j < end; j++) {
                    if (array[j] < localMin) {
                        localMin = array[j];
                        localIndex = j;
                    }
                }

                synchronized (result) {
                    if (localMin < result.minValue) {
                        result.minValue = localMin;
                        result.minIndex = localIndex;
                    }
                }

                latch.countDown(); // Потік завершився
            });

            threads[i].start();
        }

        try {
            latch.await(); // Чекаємо завершення всіх потоків
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Minimum value: " + result.minValue);
        System.out.println("Index: " + result.minIndex);
    }
}
