import java.util.Random;

public final class Datos {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 1_000_000;

    private Datos() {
    }

    public static int[] generar(int size) {
        return generar(size, MIN_VALUE, MAX_VALUE);
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
