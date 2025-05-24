package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.model.Cita;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;

public class ReportesFrame extends JFrame {
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTextArea textArea;
    private ApiService apiService;

    public ReportesFrame() {
        setTitle("Reportes - Buscar Cita por ID");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 250, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBuscar = new JLabel("ID de la Cita:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblBuscar, gbc);

        txtBuscar = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(txtBuscar, gbc);

        btnBuscar = new JButton("Buscar");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(btnBuscar, gbc);

        textArea = new JTextArea(8, 30);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scroll, gbc);

        btnBuscar.addActionListener(e -> buscarCita());

        add(panel);
    }

    private void buscarCita() {
        String idCita = txtBuscar.getText().trim();
        if (idCita.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un ID de cita.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Call<Cita> call = apiService.getCitaById(idCita);
            Response<Cita> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                Cita cita = response.body();
                StringBuilder sb = new StringBuilder();
                sb.append("ID Cita: ").append(cita.getIdCita()).append("\n");
                sb.append("Dueño: ").append(cita.getNombreDueno()).append("\n");
                sb.append("Mascota: ").append(cita.getNombreMascota()).append("\n");
                sb.append("Fecha: ").append(cita.getFecha()).append("\n");
                sb.append("Hora: ").append(cita.getHora()).append("\n");
                sb.append("Veterinario: ").append(cita.getVeterinarioAsignado()).append("\n");
                sb.append("Motivo: ").append(cita.getMotivo()).append("\n");

                textArea.setText(sb.toString());
            } else {
                textArea.setText("");
                if (response.code() == 404) {
                    JOptionPane.showMessageDialog(this, "No se encontró la cita con ID: " + idCita,
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error: " + response.message(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
