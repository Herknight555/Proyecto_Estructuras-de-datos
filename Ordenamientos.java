import java.util.ArrayList;

public final class Ordenamientos {
    private Ordenamientos() {
    }

    public static void bubble(int[] values) {
        for (int end = values.length - 1; end > 0; end--) {
            boolean changed = false;
            for (int index = 0; index < end; index++) {
                if (values[index] > values[index + 1]) {
                    swap(values, index, index + 1);
                    changed = true;
                }
            }
            if (!changed) return;
        }
    }

    public static void select(int[] values) {
        for (int index = 0; index < values.length - 1; index++) {
            int minimum = index;
            for (int candidate = index + 1; candidate < values.length; candidate++) {
                if (values[candidate] < values[minimum]) minimum = candidate;
            }
            swap(values, index, minimum);
        }
    }

    public static void insert(int[] values) {
        for (int index = 1; index < values.length; index++) {
            int value = values[index];
            int position = index - 1;
            while (position >= 0 && values[position] > value) {
                values[position + 1] = values[position--];
            }
            values[position + 1] = value;
        }
    }

    public static void merge(int[] values) {
        mergeSort(values, 0, values.length - 1, new int[values.length]);
    }

    private static void mergeSort(int[] values, int left, int right, int[] buffer) {
        if (left >= right) return;
        int middle = left + (right - left) / 2;
        mergeSort(values, left, middle, buffer);
        mergeSort(values, middle + 1, right, buffer);
        merge(values, left, middle, right, buffer);
    }

    private static void merge(int[] values, int left, int middle, int right, int[] buffer) {
        int first = left, second = middle + 1, target = left;
        while (first <= middle && second <= right) buffer[target++] = values[first] <= values[second] ? values[first++] : values[second++];
        while (first <= middle) buffer[target++] = values[first++];
        while (second <= right) buffer[target++] = values[second++];
        for (int index = left; index <= right; index++) values[index] = buffer[index];
    }

    public static void quick(int[] values) {
        quickSort(values, 0, values.length - 1);
    }

    private static void quickSort(int[] values, int left, int right) {
        if (left >= right) return;
        int pivot = values[left + (right - left) / 2];
        int lower = left, upper = right;
        while (lower <= upper) {
            while (values[lower] < pivot) lower++;
            while (values[upper] > pivot) upper--;
            if (lower <= upper) swap(values, lower++, upper--);
        }
        quickSort(values, left, upper);
        quickSort(values, lower, right);
    }

    public static void shell(int[] values) {
        for (int gap = values.length / 2; gap > 0; gap /= 2) {
            for (int index = gap; index < values.length; index++) {
                int value = values[index];
                int position = index;
                while (position >= gap && values[position - gap] > value) {
                    values[position] = values[position - gap];
                    position -= gap;
                }
                values[position] = value;
            }
        }
    }

    public static void bubble(ArrayList<Integer> values) { bubbleSortList(values); }
    public static void select(ArrayList<Integer> values) { selectionSortList(values); }
    public static void insert(ArrayList<Integer> values) { insertionSortList(values); }
    public static void merge(ArrayList<Integer> values) { mergeSortList(values, 0, values.size() - 1); }
    public static void quick(ArrayList<Integer> values) { quickSortList(values, 0, values.size() - 1); }
    public static void shell(ArrayList<Integer> values) {
        for (int gap = values.size() / 2; gap > 0; gap /= 2) {
            for (int index = gap; index < values.size(); index++) {
                int value = values.get(index);
                int position = index;
                while (position >= gap && values.get(position - gap) > value) {
                    values.set(position, values.get(position - gap));
                    position -= gap;
                }
                values.set(position, value);
            }
        }
    }

    private static void bubbleSortList(ArrayList<Integer> values) {
        for (int end = values.size() - 1; end > 0; end--) {
            boolean changed = false;
            for (int index = 0; index < end; index++) if (values.get(index) > values.get(index + 1)) { swap(values, index, index + 1); changed = true; }
            if (!changed) return;
        }
    }

    private static void selectionSortList(ArrayList<Integer> values) {
        for (int index = 0; index < values.size() - 1; index++) {
            int minimum = index;
            for (int candidate = index + 1; candidate < values.size(); candidate++) if (values.get(candidate) < values.get(minimum)) minimum = candidate;
            swap(values, index, minimum);
        }
    }

    private static void insertionSortList(ArrayList<Integer> values) {
        for (int index = 1; index < values.size(); index++) {
            int value = values.get(index), position = index - 1;
            while (position >= 0 && values.get(position) > value) values.set(position + 1, values.get(position--));
            values.set(position + 1, value);
        }
    }

    private static void mergeSortList(ArrayList<Integer> values, int left, int right) {
        if (left >= right) return;
        int middle = left + (right - left) / 2;
        mergeSortList(values, left, middle);
        mergeSortList(values, middle + 1, right);
        ArrayList<Integer> merged = new ArrayList<>(right - left + 1);
        int first = left, second = middle + 1;
        while (first <= middle && second <= right) merged.add(values.get(first) <= values.get(second) ? values.get(first++) : values.get(second++));
        while (first <= middle) merged.add(values.get(first++));
        while (second <= right) merged.add(values.get(second++));
        for (int index = 0; index < merged.size(); index++) values.set(left + index, merged.get(index));
    }

    private static void quickSortList(ArrayList<Integer> values, int left, int right) {
        if (left >= right) return;
        int pivot = values.get(left + (right - left) / 2), lower = left, upper = right;
        while (lower <= upper) {
            while (values.get(lower) < pivot) lower++;
            while (values.get(upper) > pivot) upper--;
            if (lower <= upper) swap(values, lower++, upper--);
        }
        quickSortList(values, left, upper);
        quickSortList(values, lower, right);
    }

    private static void swap(int[] values, int first, int second) { int temporary = values[first]; values[first] = values[second]; values[second] = temporary; }
    private static void swap(ArrayList<Integer> values, int first, int second) { Integer temporary = values.get(first); values.set(first, values.get(second)); values.set(second, temporary); }
}
