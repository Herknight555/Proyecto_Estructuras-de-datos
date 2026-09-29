import java.util.Random;

public class Datos {
    private static final int DatoMinimo = 1;
    private static final int DatoMaximo = 1000000;

    public static int[] generar(int size) {
        return generar(size, DatoMinimo, DatoMaximo);
    }

    public static int[] generar(int size, int minimum, int maximum) {
        Random random = new Random();
        int[] values = new int[size];
        for (int index = 0; index < size; index++) {
            values[index] = random.nextInt(maximum - minimum + 1) + minimum;
        }
        return values;
    }
}
