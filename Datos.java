
import java.util.Random;
 
public class Datos
    {
    private static final int MIN = 1;
    private static final int MAX = 1000000;
    
    public static int[] generar(int s) 
        {
        return generar(s, MIN, MAX);
        }
    
    public static int[] generar(int s, int m, int M) 
        {
        Random random = new Random();
        int[] ARREGLO = new int[s];
        
        for(int i = 0; i < s; i++)
            {
            ARREGLO[i] = random.nextInt(M - m + 1) + m;
            }
        
        return ARREGLO;
        }
    
    }