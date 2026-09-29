import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int option = leerEscenario(scanner);

            int size;
            String description;
            int[] originalData;

            if (option == 5) {
                size = leerCantidadElementos(scanner);
                description = size + " elementos aleatorios";
                originalData = Datos.generar(size);
            } else {
                size = switch (option) {
                    case 1 -> 100;
                    case 2 -> 50_000;
                    case 3, 4 -> 100_000;
                    default -> throw new IllegalStateException("Opción no válida.");
                };
                description = switch (option) {
                    case 1 -> "100 elementos aleatorios";
                    case 2 -> "50,000 elementos aleatorios";
                    case 3 -> "100,000 elementos aleatorios";
                    case 4 -> "100,000 elementos entre 1 y 5";
                    default -> "";
                };
                originalData = option == 4 ? Datos.generar(size, 1, 5) : Datos.generar(size);
            }

            System.out.println("\nGenerando datos: " + description + "...");
            List<Ejecutor.Resultado> results = new Ejecutor().ejecutar(originalData);
            mostrarResultados(description, size, results);
            Grafica.mostrar(description, results);
        }
    }

    private static int leerEscenario(Scanner scanner) {
        while (true) {
            System.out.println("\nSELECCIONA LOS DATOS DE PRUEBA");
            System.out.println("1. 100 elementos aleatorios");
            System.out.println("2. 50,000 elementos aleatorios");
            System.out.println("3. 100,000 elementos aleatorios");
            System.out.println("4. 100,000 elementos con valores entre 1 y 5");
            System.out.println("5. Ingresar la cantidad de elementos");
            System.out.print("Opción: ");
            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                if (option >= 1 && option <= 5) return option;
            } else {
                scanner.next();
            }
            System.out.println("Selecciona una opción entre 1 y 5.");
        }
    }

    private static int leerCantidadElementos(Scanner scanner) {
        while (true) {
            System.out.print("\n¿Cuántos elementos vas a ordenar? ");
            if (scanner.hasNextInt()) {
                int cantidad = scanner.nextInt();
                if (cantidad > 0) return cantidad;
                System.out.println("Ingresa un número entero positivo (mayor que 0).");
            } else {
                System.out.println("Entrada inválida. Ingresa un número entero positivo.");
                scanner.next();
            }
        }
    }

    private static void mostrarResultados(String description, int size, List<Ejecutor.Resultado> results) {
        List<Ejecutor.Resultado> ordered = results.stream()
                .sorted(Comparator.comparingDouble(Ejecutor.Resultado::milliseconds))
                .toList();
        System.out.println("\nRESULTADOS DE ORDENAMIENTO");
        System.out.println("Prueba: " + description);
        System.out.println("Elementos: " + size);
        System.out.printf("%-4s %-16s %-12s %-14s %-10s Complejidad%n", "Pos.", "Algoritmo", "Estructura", "Tiempo (ms)", "¿Ordenó?");
        for (int index = 0; index < ordered.size(); index++) {
            Ejecutor.Resultado result = ordered.get(index);
            System.out.printf("%-4d %-16s %-12s %-14.3f %-10s %s%n", index + 1, result.algorithm(), result.structure(),
                    result.milliseconds(), result.sorted() ? "Sí" : "No", result.complexity());
        }
        Ejecutor.Resultado fastest = ordered.get(0);
        System.out.printf("%nImplementación con menor tiempo registrado: %s (%s)%n", fastest.algorithm(), fastest.structure());
    }
}
