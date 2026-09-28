import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class Ejecutor {
    private final ConcurrentHashMap<Integer, Resultado> results = new ConcurrentHashMap<>();

    public record Resultado(String algorithm, String structure,
                            String complexity, double milliseconds, boolean sorted) {
    }

    public List<Resultado> ejecutar(int[] originalData) {
        List<Thread> threads = new ArrayList<>();
        List<AlgorithmDefinition> definitions = definitions();

        for (int index = 0; index < definitions.size(); index++) {
            AlgorithmDefinition definition = definitions.get(index);
            Thread thread = new Thread(() -> execute(definition, originalData), definition.algorithm + "-" + definition.structure);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("La espera de los hilos fue interrumpida.", exception);
            }
        }

        return results.entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByKey())
                .map(java.util.Map.Entry::getValue)
                .toList();
    }

    private void execute(AlgorithmDefinition definition, int[] originalData) {
        long start;
        long elapsed;
        boolean sorted;

        if (definition.arraySorter != null) {
            int[] copy = Arrays.copyOf(originalData, originalData.length);
            start = System.nanoTime();
            definition.arraySorter.accept(copy);
            elapsed = System.nanoTime() - start;
            sorted = isSorted(copy);
        } else {
            ArrayList<Integer> copy = new ArrayList<>(originalData.length);
            for (int value : originalData) copy.add(value);
            start = System.nanoTime();
            definition.listSorter.accept(copy);
            elapsed = System.nanoTime() - start;
            sorted = isSorted(copy);
        }

        results.put(definition.order, new Resultado(definition.algorithm, definition.structure,
                definition.complexity, elapsed / 1_000_000.0, sorted));
    }

    private static boolean isSorted(int[] values) {
        for (int index = 1; index < values.length; index++) if (values[index - 1] > values[index]) return false;
        return true;
    }

    private static boolean isSorted(ArrayList<Integer> values) {
        for (int index = 1; index < values.size(); index++) if (values.get(index - 1) > values.get(index)) return false;
        return true;
    }

    private static List<AlgorithmDefinition> definitions() {
        return List.of(
                definicion(1, "bubble", "O(n) / O(n^2)", Ordenamientos::bubble, null, "Arreglo"),
                definicion(2, "bubble", "O(n) / O(n^2)", null, Ordenamientos::bubble, "ArrayList"),
                definicion(3, "select", "O(n^2)", Ordenamientos::select, null, "Arreglo"),
                definicion(4, "select", "O(n^2)", null, Ordenamientos::select, "ArrayList"),
                definicion(5, "insert", "O(n) / O(n^2)", Ordenamientos::insert, null, "Arreglo"),
                definicion(6, "insert", "O(n) / O(n^2)", null, Ordenamientos::insert, "ArrayList"),
                definicion(7, "merge", "O(n log n)", Ordenamientos::merge, null, "Arreglo"),
                definicion(8, "merge", "O(n log n)", null, Ordenamientos::merge, "ArrayList"),
                definicion(9, "quick", "O(n log n) / O(n^2)", Ordenamientos::quick, null, "Arreglo"),
                definicion(10, "quick", "O(n log n) / O(n^2)", null, Ordenamientos::quick, "ArrayList"),
                definicion(11, "shell", "Depende de la secuencia de incrementos", Ordenamientos::shell, null, "Arreglo"),
                definicion(12, "shell", "Depende de la secuencia de incrementos", null, Ordenamientos::shell, "ArrayList")
        );
    }

    private static AlgorithmDefinition definicion(int order, String algorithm, String complexity,
                                                   Consumer<int[]> arraySorter, Consumer<ArrayList<Integer>> listSorter,
                                                   String structure) {
        return new AlgorithmDefinition(order, algorithm, complexity, arraySorter, listSorter, structure);
    }

    private record AlgorithmDefinition(int order, String algorithm, String complexity,
                                       Consumer<int[]> arraySorter, Consumer<ArrayList<Integer>> listSorter,
                                       String structure) {
    }
}
