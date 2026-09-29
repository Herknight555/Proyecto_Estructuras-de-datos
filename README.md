# Comparador concurrente de ordenamientos

Aplicación de consola Java que compara seis algoritmos sobre un `int[]` y un `ArrayList<Integer>`: bubble, select, insert, merge, shell y quick.

## Ejecución

Requiere Java 17 o posterior porque se usan `record` y `Stream.toList()`.

```bash
javac *.java
java Main
```

El programa muestra un menú con cuatro escenarios: 100, 50,000 o 100,000 elementos aleatorios, y 100,000 elementos restringidos a valores entre 1 y 5. Genera los mismos datos para las 12 pruebas y crea un `Thread` independiente para cada una. Las copias se preparan antes de medir; cada hilo mide únicamente con `System.nanoTime()` el ordenamiento, verifica el resultado y registra datos en `ConcurrentHashMap`. Al finalizar, abre una gráfica de barras usando Swing, sin dependencias externas.

## Complejidad

- Bubble: mejor `O(n)` con salida temprana; promedio/peor `O(n^2)`.
- Select: mejor, promedio y peor `O(n^2)`.
- Insert: mejor `O(n)`; promedio/peor `O(n^2)`.
- Merge: mejor, promedio y peor `O(n log n)`.
- Quick: promedio/mejor `O(n log n)`; peor `O(n^2)`.
- Shell: depende de la secuencia de incrementos; con esta implementación usa incrementos divididos entre dos.

Burbuja, selección e inserción pueden tardar demasiado con 50,000 o 100,000 datos porque su crecimiento cuadrático domina. Para esas cantidades conviene documentar la demora o usar una muestra menor para la demostración. Los hilos hacen que los tiempos varíen por competencia entre tareas y disponibilidad de CPU; el menor tiempo de una ejecución no prueba que un algoritmo sea siempre superior.
