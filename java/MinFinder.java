public class MinFinder extends Thread {
    private final int[] array;
    private final int start;
    private final int end;
    private final MinResult result;

    public MinFinder(int[] array, int start, int end, MinResult result) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.result = result;
    }

    @Override
    public void run() {
        // Знаходження локального мінімуму в частині масиву
        int localMin = array[start];
        int localIndex = start;
        for (int i = start + 1; i < end; i++) {
            if (array[i] < localMin) {
                localMin = array[i];
                localIndex = i;
            }
        }
        //синхронізація та оновлення результату
        synchronized (result) {
            if (localMin < result.minValue) {
                result.minValue = localMin;
                result.minIndex = localIndex;
            }
        }
    }
}
