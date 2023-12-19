import java.util.*;

enum SortingType {
    BUBBLE, SHELL, MERGE, QUICK
}

interface Sorter {
    ArrayList<Integer> sort(ArrayList<Integer> input);
}

class BubbleSorting implements Sorter {
    @Override
    public ArrayList<Integer> sort(ArrayList<Integer> input) {
        ArrayList<Integer> sorted = new ArrayList<>(input);
        int n = sorted.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sorted.get(j) > sorted.get(j + 1)) {
                    Collections.swap(sorted, j, j + 1);
                }
            }
        }
        return sorted;
    }
}

class ShellSorting implements Sorter {
    @Override
    public ArrayList<Integer> sort(ArrayList<Integer> input) {
        ArrayList<Integer> sorted = new ArrayList<>(input);
        int n = sorted.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = sorted.get(i);
                int j;
                for (j = i; j >= gap && sorted.get(j - gap) > temp; j -= gap) {
                    sorted.set(j, sorted.get(j - gap));
                }
                sorted.set(j, temp);
            }
        }
        return sorted;
    }
}

class MergeSorting implements Sorter {
    @Override
    public ArrayList<Integer> sort(ArrayList<Integer> input) {
        ArrayList<Integer> sorted = new ArrayList<>(input);
        mergesort2(sorted, 0, sorted.size() - 1);
        return sorted;
    }

    private void mergesort2(ArrayList<Integer> arr, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;
            mergesort2(arr, left, middle);
            mergesort2(arr, middle + 1, right);
            mergesort3(arr, left, middle, right);
        }
    }

    private void mergesort3(ArrayList<Integer> arr, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
        ArrayList<Integer> L = new ArrayList<>(arr.subList(left, left + n1));
        ArrayList<Integer> R = new ArrayList<>(arr.subList(middle + 1, middle + 1 + n2));
        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (L.get(i) <= R.get(j)) {
                arr.set(k, L.get(i));
                i++;
            } else {
                arr.set(k, R.get(j));
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr.set(k, L.get(i));
            i++;
            k++;
        }
        while (j < n2) {
            arr.set(k, R.get(j));
            j++;
            k++;
        }
    }
}

class QuickSorting implements Sorter {
    @Override
    public ArrayList<Integer> sort(ArrayList<Integer> input) {
        ArrayList<Integer> sorted = new ArrayList<>(input);
        quicksort2(sorted, 0, sorted.size() - 1);
        return sorted;
    }

    private void quicksort2(ArrayList<Integer> arr, int start, int end) {
        if (start < end) {
            int pi = quicksort3(arr, start, end);
            quicksort2(arr, start, pi - 1);
            quicksort2(arr, pi + 1, end);
        }
    }

    private int quicksort3(ArrayList<Integer> arr, int start, int end) {
        int pi = arr.get(end);
        int i = (start - 1);
        for (int j = start; j < end; j++) {
            if (arr.get(j) < pi) {
                i++;
                Collections.swap(arr, i, j);
            }
        }
        Collections.swap(arr, i + 1, end);
        return i + 1;
    }
}

public class Main {
    public static void main(String[] args) {
        for (int count : new int[]{10, 1000, 10000, 1000000}) {
            System.out.println("\nArray size - " + count + "\n");
            ArrayList<Integer> input = RandomArray(count);
            for (SortingType type : SortingType.values()) {
                Sorter sorter = getSorter(type);
                long StartTime = System.nanoTime();
                ArrayList<Integer> sorted = sorter.sort(input);
                long EndTime = System.nanoTime();
                long tspent = (EndTime - StartTime) / 1000000;
                System.out.println("Sorting type:" + type + " | Time spent:" + tspent + "ms");
                if (count <= 50) {
                    System.out.println("Sorted array:" + sorted);
                } else {
                    System.out.print("First 50 elements: " + sorted.subList(0, 50) + "\n");
                }
            }
        }
    }

    private static ArrayList<Integer> RandomArray(int size) {
        ArrayList<Integer> array = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array.add(random.nextInt(size));
        }
        return array;
    }

    private static Sorter getSorter(SortingType type) {
        return switch (type) {
            case BUBBLE -> new BubbleSorting();
            case SHELL -> new ShellSorting();
            case MERGE -> new MergeSorting();
            case QUICK -> new QuickSorting();
        };
    }
}