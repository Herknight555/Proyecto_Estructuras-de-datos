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
            JFrame frame = new JFrame("Resultados de ordenamiento");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new BarChartPanel(description, results));
            frame.setSize(1_000, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static class BarChartPanel extends JPanel {
        private static final int LEFT_MARGIN = 230;
        private static final int RIGHT_MARGIN = 80;
        private static final int TOP_MARGIN = 70;
        private static final int ROW_HEIGHT = 42;
        private static final Color ARRAY_COLOR = new Color(41, 98, 255);
        private static final Color ARRAY_LIST_COLOR = new Color(0, 150, 136);

        private final String description;
        private final List<Ejecutor.Resultado> results;

        private BarChartPanel(String description, List<Ejecutor.Resultado> results) {
            this.description = description;
            this.results = results.stream()
                .sorted(Comparator.comparingDouble(Ejecutor.Resultado::milliseconds))
                    .toList();
            setPreferredSize(new Dimension(980, TOP_MARGIN + ROW_HEIGHT * results.size() + 40));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D canvas = (Graphics2D) graphics.create();
            canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            canvas.setColor(new Color(25, 35, 50));
            canvas.setFont(new Font("SansSerif", Font.BOLD, 20));
            canvas.drawString("Tiempo de ejecución por ordenamiento", 28, 32);
            canvas.setFont(new Font("SansSerif", Font.PLAIN, 14));
            canvas.drawString(description, 28, 54);

            double maximum = results.stream().mapToDouble(Ejecutor.Resultado::milliseconds).max().orElse(1.0);
            int availableWidth = getWidth() - LEFT_MARGIN - RIGHT_MARGIN;
            int y = TOP_MARGIN;
            for (Ejecutor.Resultado result : results) {
                int barWidth = (int) Math.round(result.milliseconds() / maximum * availableWidth);
                if (result.milliseconds() > 0 && barWidth == 0) barWidth = 1;

                canvas.setColor(new Color(45, 55, 70));
                canvas.setFont(new Font("SansSerif", Font.PLAIN, 13));
                canvas.drawString(result.algorithm() + " - " + result.structure(), 28, y + 17);
                canvas.setColor(result.structure().equals("Arreglo") ? ARRAY_COLOR : ARRAY_LIST_COLOR);
                canvas.fillRoundRect(LEFT_MARGIN, y + 4, barWidth, 24, 6, 6);
                canvas.setColor(new Color(45, 55, 70));
                canvas.drawString(String.format("%.3f ms", result.milliseconds()),
                        LEFT_MARGIN + barWidth + 8, y + 21);
                y += ROW_HEIGHT;
            }

            int legendY = y + 16;
            canvas.setColor(ARRAY_COLOR);
            canvas.fillRect(28, legendY, 14, 14);
            canvas.setColor(new Color(45, 55, 70));
            canvas.drawString("Arreglo", 48, legendY + 12);
            canvas.setColor(ARRAY_LIST_COLOR);
            canvas.fillRect(120, legendY, 14, 14);
            canvas.setColor(new Color(45, 55, 70));
            canvas.drawString("ArrayList", 140, legendY + 12);
            canvas.dispose();
        }
    }
}