import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Comparator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Grafica {
    private Grafica() {
    }
    
    public static void mostrar(String description, List<Ejecutor.Resultado> results) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Resultados de ordenamiento");
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.add(new PanelGrafica(description, results));
            ventana.setSize(1_000, 700);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }

    private static class PanelGrafica extends JPanel {
        private static final int MARGEN_IZQUIERDO = 230;
        private static final int MARGEN_DERECHO = 80;
        private static final int MARGEN_SUPERIOR = 70;
        private static final int ALTURA_FILA = 42;
        private static final Color COLOR_ARREGLO = new Color(41, 98, 255);
        private static final Color COLOR_ARRAY_LIST = new Color(0, 150, 136);

        private final String descripcion;
        private final List<Ejecutor.Resultado> resultados;

        private PanelGrafica(String descripcion, List<Ejecutor.Resultado> resultados) {
            this.descripcion = descripcion;
            this.resultados = resultados.stream()
                .sorted(Comparator.comparingDouble(Ejecutor.Resultado::milisegundos))
                .toList();
            setPreferredSize(new Dimension(980, MARGEN_SUPERIOR + ALTURA_FILA * resultados.size() + 40));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics graficos) {
            super.paintComponent(graficos);
            Graphics2D lienzo = (Graphics2D) graficos.create();
            lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            lienzo.setColor(new Color(25, 35, 50));
            lienzo.setFont(new Font("SansSerif", Font.BOLD, 20));
            lienzo.drawString("Tiempo de ejecución por ordenamiento", 28, 32);
            lienzo.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lienzo.drawString(descripcion, 28, 54);

            double maximo = resultados.stream().mapToDouble(Ejecutor.Resultado::milisegundos).max().orElse(1.0);
            int anchoDisponible = getWidth() - MARGEN_IZQUIERDO - MARGEN_DERECHO;
            int posicionY = MARGEN_SUPERIOR;
            for (Ejecutor.Resultado resultado : resultados) {
                int anchoBarra = (int) Math.round(resultado.milisegundos() / maximo * anchoDisponible);
                if (resultado.milisegundos() > 0 && anchoBarra == 0) anchoBarra = 1;

                lienzo.setColor(new Color(45, 55, 70));
                lienzo.setFont(new Font("SansSerif", Font.PLAIN, 13));
                lienzo.drawString(resultado.algoritmo() + " - " + resultado.estructura(), 28, posicionY + 17);
                lienzo.setColor(resultado.estructura().equals("Arreglo") ? COLOR_ARREGLO : COLOR_ARRAY_LIST);
                lienzo.fillRoundRect(MARGEN_IZQUIERDO, posicionY + 4, anchoBarra, 24, 6, 6);
                lienzo.setColor(new Color(45, 55, 70));
                lienzo.drawString(String.format("%.3f ms", resultado.milisegundos()),
                        MARGEN_IZQUIERDO + anchoBarra + 8, posicionY + 21);
                posicionY += ALTURA_FILA;
            }

            int posicionLeyenda = posicionY + 16;
            lienzo.setColor(COLOR_ARREGLO);
            lienzo.fillRect(28, posicionLeyenda, 14, 14);
            lienzo.setColor(new Color(45, 55, 70));
            lienzo.drawString("Arreglo", 48, posicionLeyenda + 12);
            lienzo.setColor(COLOR_ARRAY_LIST);
            lienzo.fillRect(120, posicionLeyenda, 14, 14);
            lienzo.setColor(new Color(45, 55, 70));
            lienzo.drawString("ArrayList", 140, posicionLeyenda + 12);
            lienzo.dispose();
        }
    }
}