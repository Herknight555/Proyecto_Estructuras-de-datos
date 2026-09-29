import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
 
public class Main
   
    {
    public static void main(String[] args) 
        {
        
        Scanner input = new Scanner(System.in);
        int AUXILIAR = 1;
        int seleccion;
        int x;
        int size = 0;
        int minimo = 1;
        int maximo = 1000000;
        String desc = "";
        
        do
            {
            if(AUXILIAR == 1) //menu de datos de prueba
                {
                System.out.print("\n\tSELECCIONA LOS DATOS DE PRUEBA\n"
                + "1. 100 ELEMENTOS ALEATORIOS\n" //los aleatorios van en aux 2
                + "2. 50,000 ELEMENTOS ALEATORIOS\n" 
                + "3. 100,000 ELEMENTOS ALEATORIOS\n"
                + "4. 100,000 ELEMENTOS ENTRE 1 Y 5\n"
                + "5. INGRESAR LA CANTIDAD DE ELEMENTOS\n" //auxiliar 3
                + "6. SALIR\n" //auxiliar 0
                + "\n -  ");
                
                try
                    {
                    seleccion = input.nextInt();
                    input.nextLine();
                    }
                catch(InputMismatchException e)
                    {
                    System.out.println("\nERROR. Escribir un número.\n");
                    input.nextLine();
                    seleccion = 0;
                    }
                
                switch(seleccion)
                    {
                    case 1:
                        size = 100;
                        minimo = 1;
                        maximo = 1000000;
                        desc = "100 elementos aleatorios";
                        AUXILIAR = 2;
                        break;
                        
                    case 2:
                        size = 50000;
                        minimo = 1;
                        maximo = 1000000;
                        desc = "50,000 elementos aleatorios";
                        AUXILIAR = 2;
                        break;
                        
                    case 3:
                        size = 100000;
                        minimo = 1;
                        maximo = 1000000;
                        desc = "100,000 elementos aleatorios";
                        AUXILIAR = 2;
                        break;
                        
                    case 4:
                        size = 100000;
                        minimo = 1;
                        maximo = 5;
                        desc = "100,000 elementos entre 1 y 5";
                        AUXILIAR = 2;
                        break;
                        
                    case 5:
                        AUXILIAR = 3;
                        break;
                        
                    case 6:
                        AUXILIAR = 0;
                        break;
                        
                    case 0:
                        break;
                        
                    default:
                        System.out.println("\nSelecciona una opción entre 1 y 6.\n");
                        break;
                    }
                
                }
            else if(AUXILIAR == 2) //ejecutar ordenamientos
                {
                System.out.println("\nGenerando datos:  " + desc + "...");
                int[] DATOS = Datos.generar(size, minimo, maximo);
                
                List<Ejecutor.Resultado> RESULTADOS = new Ejecutor().ejecutar(DATOS);
                
                ArrayList<Ejecutor.Resultado> ordenados = new ArrayList<>(RESULTADOS);
                ordenados.sort(Comparator.comparingDouble(Ejecutor.Resultado::milisegundos));
                
                System.out.println("\n\tRESULTADOS DE ORDENAMIENTO\n"
                        + "Prueba: " + desc + "\n"
                        + "Elementos: " + size + "\n");
                System.out.printf("%-4s %-10s %-12s %-14s %-10s %s%n", "Pos.", "Algoritmo", "Estructura", "Tiempo (ms)", "¿Ordenó?", "Complejidad");
                
                for(int i = 0; i < ordenados.size(); i++)
                    {
                    Ejecutor.Resultado actual = ordenados.get(i);
                        System.out.printf("%-4d %-10s %-12s %-14.3f %-10s %s%n", i + 1, actual.algoritmo(), actual.estructura(),
                            actual.milisegundos(), actual.ordenado() ? "Sí" : "No", actual.complejidad());
                    }
                
                Ejecutor.Resultado MASRAPIDO = ordenados.get(0);
                System.out.println("\nMenor tiempo registrado: " + MASRAPIDO.algoritmo() + " (" + MASRAPIDO.estructura() + ")");
                
                Grafica.mostrar(desc, RESULTADOS);
                
                AUXILIAR = 1;
                }
            else if(AUXILIAR == 3) //cantidad personalizada
                {
                System.out.print("\n¿Cuántos elementos vas a ordenar? \n\t");
                
                try
                    {
                    x = input.nextInt();
                    input.nextLine();
                    if(x > 0)
                        {
                        size = x;
                        minimo = 1;
                        maximo = 1000000;
                        desc = size + " elementos aleatorios";
                        AUXILIAR = 2;
                        }
                    else
                        {
                        System.out.println("\nERROR. Ingresa un número mayor que 0.\n");
                        }
                    }
                catch(InputMismatchException e)
                    {
                    System.out.println("\nERROR. Escribir un número entero.\n");
                    input.nextLine();
                    }
                
                }
            }
        while(AUXILIAR != 0);
        
        input.close();
        
        }
    
    }