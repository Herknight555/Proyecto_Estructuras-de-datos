import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Ejecutor {
    private ConcurrentHashMap<Integer, Resultado> resultados = new ConcurrentHashMap<>();

    public record Resultado(String algoritmo, String estructura,
                            String complejidad, double milisegundos, boolean ordenado) {
    }

    public List<Resultado> ejecutar(int[] datosOriginales) {
        List<Thread> hilos = new ArrayList<>();
        List<DefinicionAlgoritmo> definiciones = obtenerDefiniciones();

        for (int indice = 0; indice < definiciones.size(); indice++) {
            DefinicionAlgoritmo definicion = definiciones.get(indice);
            Thread hilo = new Thread(() -> ejecutarAlgoritmo(definicion, datosOriginales),
                    definicion.algoritmo + "-" + definicion.estructura);
            hilos.add(hilo);
            hilo.start();
        }

        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("La espera de los hilos fue interrumpida.", exception);
            }
        }

        return resultados.entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByKey())
                .map(java.util.Map.Entry::getValue)
                .toList();
    }

    private void ejecutarAlgoritmo(DefinicionAlgoritmo definicion, int[] datosOriginales) {
        long inicio;
        long tiempoTranscurrido;
        boolean ordenado;

        if (definicion.ordenadorArreglo != null) {
            int[] copia = Arrays.copyOf(datosOriginales, datosOriginales.length);
            inicio = System.nanoTime();
            definicion.ordenadorArreglo.accept(copia);
            tiempoTranscurrido = System.nanoTime() - inicio;
            ordenado = estaOrdenado(copia);
        } else {
            ArrayList<Integer> copia = new ArrayList<>(datosOriginales.length);
            for (int valor : datosOriginales) copia.add(valor);
            inicio = System.nanoTime();
            definicion.ordenadorLista.accept(copia);
            tiempoTranscurrido = System.nanoTime() - inicio;
            ordenado = estaOrdenado(copia);
        }

        resultados.put(definicion.orden, new Resultado(definicion.algoritmo, definicion.estructura,
                definicion.complejidad, tiempoTranscurrido / 1_000_000.0, ordenado));
    }

    private static boolean estaOrdenado(int[] valores) {
        for (int indice = 1; indice < valores.length; indice++) if (valores[indice - 1] > valores[indice]) return false;
        return true;
    }

    private static boolean estaOrdenado(ArrayList<Integer> valores) {
        for (int indice = 1; indice < valores.size(); indice++) if (valores.get(indice - 1) > valores.get(indice)) return false;
        return true;
    }

    private static List<DefinicionAlgoritmo> obtenerDefiniciones() {
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

    private static DefinicionAlgoritmo definicion(int orden, String algoritmo, String complejidad,
                                                   Consumer<int[]> ordenadorArreglo, Consumer<ArrayList<Integer>> ordenadorLista,
                                                   String estructura) {
        return new DefinicionAlgoritmo(orden, algoritmo, complejidad, ordenadorArreglo, ordenadorLista, estructura);
    }

    private record DefinicionAlgoritmo(int orden, String algoritmo, String complejidad,
                                       Consumer<int[]> ordenadorArreglo, Consumer<ArrayList<Integer>> ordenadorLista,
                                       String estructura) {
    }
}
