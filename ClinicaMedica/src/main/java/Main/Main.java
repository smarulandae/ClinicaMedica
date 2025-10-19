package Main;

import vista.VentanaPrincipal;
import javax.swing.*;

/**
 * Clase principal que inicia la aplicación
 */
public class Main {
    public static void main(String[] args) {
        // Iniciar aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Establecer look and feel del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
