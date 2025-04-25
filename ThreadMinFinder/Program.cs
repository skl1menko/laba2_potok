using System;
using System.Threading;

class Program
{
    static int[] array;
    static int globalMin = int.MaxValue;
    static int globalMinIndex = -1;
    static object lockObj = new object();
    static CountdownEvent countdown;

    static void Main(string[] args)
    {
        int size = 1000;
        int threadCount = 100;
        array = GenerateArray(size);

        countdown = new CountdownEvent(threadCount); // Очікуємо завершення threadCount потоків
        int chunkSize = size / threadCount;

        for (int i = 0; i < threadCount; i++)
        {
            int start = i * chunkSize;
            int end = (i == threadCount - 1) ? size : start + chunkSize;

            Thread thread = new Thread(() => {
                FindLocalMin(start, end);
                countdown.Signal(); // Потік завершив роботу
            });

            thread.Start();
        }

        countdown.Wait(); // Очікуємо завершення всіх потоків

        Console.WriteLine($"Minimum value: {globalMin}");
        Console.WriteLine($"Index: {globalMinIndex}");
    }

    static int[] GenerateArray(int size)
    {
        Random rand = new Random();
        int[] result = new int[size];
        for (int i = 0; i < size; i++)
        {
            result[i] = rand.Next(1, 1000); // Додатні значення
        }

        // Вставляємо випадкове від’ємне число
        result[rand.Next(size)] = -rand.Next(1, 100);
        return result;
    }

    static void FindLocalMin(int start, int end)
    {
        int localMin = array[start];
        int localIndex = start;

        for (int i = start + 1; i < end; i++)
        {
            if (array[i] < localMin)
            {
                localMin = array[i];
                localIndex = i;
            }
        }

        lock (lockObj)
        {
            if (localMin < globalMin)
            {
                globalMin = localMin;
                globalMinIndex = localIndex;
            }
        }
    }
}
