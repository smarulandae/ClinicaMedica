package vista;

import javax.swing.*;
//import java.awt.*;

// Ventana principal de la aplicación
 // Aplica SRP: Solo coordina la navegación entre paneles

public class VentanaPrincipal extends JFrame {
    
    public VentanaPrincipal() {
        inicializarVentana();
        agregarComponentes();
    }
    
    private void inicializarVentana() {
        setTitle("Sistema de Gestión de Citas Médicas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void agregarComponentes() {
        JTabbedPane pestanas = new JTabbedPane();
        
        /*
        PanelPacientes panelPacientes = new PanelPacientes();
        PanelMedicos panelMedicos = new PanelMedicos();
        PanelCitas panelCitas = new PanelCitas();
        */
        
        // Agregar paneles
        pestanas.addTab("Pacientes", new PanelPacientes());
        pestanas.addTab("Médicos", new PanelMedicos());
        pestanas.addTab("Citas", new PanelCitas());
        
        
        add(pestanas);
    
        /*
    // Actualizar paneles
    pestanas.addChangeListener(e -> {
        int indice = pestanas.getSelectedIndex();
        if (indice == 2){
            panelCitas.actualizarCombos();
        }
        });
         */
    
    }
}