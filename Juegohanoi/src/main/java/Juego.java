import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class Juego extends JFrame {
    private Panel panel;

    public Juego(int numPalos, int numDiscos) {
        setTitle("Torres de Hanoi");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new Panel(numPalos, numDiscos);
        add(panel);
        setVisible(true);
        new Thread(panel::resolver).start();
    }

    public static void main(String[] args) {
        int numPalos;
        do {
            numPalos = Integer.parseInt(JOptionPane.showInputDialog("¿Con cuántas torres quieres jugar (mínimo 3)?"));
        } while (numPalos < 3);

        int numDiscos = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántos discos quieres poner en las torres?"));
        new Juego(numPalos, numDiscos);
    }
}

class Panel extends JPanel {
    private Stack<Integer>[] torres;
    private int numPalos, numDiscos;
    private boolean terminado = false;

    @SuppressWarnings("unchecked")
    public Panel(int numPalos, int numDiscos) {
        this.numPalos = numPalos;
        this.numDiscos = numDiscos;
        torres = new Stack[numPalos];

        for (int i = 0; i < numPalos; i++) {
            torres[i] = new Stack<>();
        }

        for (int i = numDiscos; i > 0; i--) {
            torres[0].push(i);
        }
    }

    public void resolver() {
        moverNPalos(numDiscos, 0, numPalos - 1);
        terminado = true;
        repaint();
    }

    private void moverNPalos(int n, int origen, int destino) {
        if (n == 1) {
            torres[destino].push(torres[origen].pop());
            repaint();
            esperar();
            return;
        }

        int k = (int) Math.round(n - Math.sqrt(2 * n + 1)) + 1;
        int auxiliar = encontrarAuxiliar(origen, destino);
        moverNPalos(k, origen, auxiliar);
        moverNPalos(n - k, origen, destino);
        moverNPalos(k, auxiliar, destino);
    }

    private int encontrarAuxiliar(int origen, int destino) {
        for (int i = 0; i < numPalos; i++) {
            if (i != origen && i != destino) return i;
        }
        return -1;
    }

    private void esperar() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int ancho = getWidth();
        int alto = getHeight();
        int anchoTorre = 10;
        int alturaBase = alto - 50;
        int alturaDisco = 20;

        for (int i = 0; i < numPalos; i++) {
            int x = (i + 1) * ancho / (numPalos + 1);
            g.setColor(Color.BLUE);
            g.fillRect(x - anchoTorre / 2, 50, anchoTorre, alturaBase - 50);
            int y = alturaBase;
            for (int j = 0; j < torres[i].size(); j++) {
                int disco = torres[i].get(j);
                int anchoDisco = disco * 20;
                g.setColor(Color.RED);
                g.fillRect(x - anchoDisco / 2, y - alturaDisco, anchoDisco, alturaDisco);
                y -= alturaDisco;
            }
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.drawString("Francisco Rene Samayoa Valle", 20, alto - 20);

        if (terminado) {
            String mensaje = "Juego Completado";
            FontMetrics fm = g.getFontMetrics();
            int textoAncho = fm.stringWidth(mensaje);
            int x = (ancho - textoAncho) / 2;
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.BLACK);
            g.drawString(mensaje, x, 30);
        }
    }
}


