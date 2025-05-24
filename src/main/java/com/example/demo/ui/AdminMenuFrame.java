package com.example.demo.ui;

import javax.swing.*;
import java.awt.*;

public class AdminMenuFrame extends JFrame {

    public AdminMenuFrame() {
        setTitle("Menú Administrativo - Veterinaria");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        // Panel principal con BorderLayout y fondo suave
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 250, 240)); // Un tono muy claro, casi blanco

        // Título del menú
        JLabel titleLabel = new JLabel("Menú Administrativo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 153));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel para botones con GridLayout (4 filas x 2 columnas)
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBackground(new Color(250, 250, 240));

        JButton btnMascotas = new JButton("Gestión de Mascotas");
        JButton btnClientes = new JButton("Gestión de Clientes");
        JButton btnHistorial = new JButton("Historial Médico");
        JButton btnCitas = new JButton("Agenda de Citas");
        JButton btnInventario = new JButton("Inventario");
        JButton btnFacturacion = new JButton("Facturación");
        JButton btnReportes = new JButton("Reportes");
        JButton btnSalir = new JButton("Salir");

        // Configuración de fuente y colores
        Font btnFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color btnColor = new Color(230, 230, 250);
        JButton[] buttons = {btnMascotas, btnClientes, btnHistorial, btnCitas, btnInventario, btnFacturacion, btnReportes, btnSalir};
        for (JButton btn : buttons) {
            btn.setFont(btnFont);
            btn.setBackground(btnColor);
            buttonPanel.add(btn);
        }

        // Agregar ActionListeners para cada botón
        btnMascotas.addActionListener(e -> {
            new MascotasFrame().setVisible(true);
        });
        btnClientes.addActionListener(e -> {
            new ClientesFrame().setVisible(true);
        });
        btnHistorial.addActionListener(e -> {
            new HistorialMedicoFrame().setVisible(true);
        });
        btnCitas.addActionListener(e -> {
            new CitasFrame().setVisible(true);
        });
        btnInventario.addActionListener(e -> {
            new InventarioFrame().setVisible(true);
        });
        btnClientes.addActionListener(e -> {
            new ClientesFrame().setVisible(true);
        });

        btnHistorial.addActionListener(e -> {
            new HistorialMedicoFrame().setVisible(true);
        });

        btnCitas.addActionListener(e -> {
            new CitasFrame().setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            new ReportesFrame().setVisible(true);
        });

        btnInventario.addActionListener(e -> {
            new InventarioFrame().setVisible(true);
        });

        btnFacturacion.addActionListener(e -> {
            new FacturacionFrame().setVisible(true);
        });

        btnFacturacion.addActionListener(e -> {
            new FacturacionFrame().setVisible(true);
        });
        btnReportes.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Ventana de reportes no implementada.", "Información", JOptionPane.INFORMATION_MESSAGE);
        });
        btnSalir.addActionListener(e -> {
            dispose();
        });

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
}
