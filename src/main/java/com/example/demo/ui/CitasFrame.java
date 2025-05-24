package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.CitaDTO;
import com.example.demo.model.Cita;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CitasFrame extends JFrame {
    private JTextField txtIdCita, txtNombreDueno, txtHora, txtVeterinario, txtNombreMascota, txtFecha, txtMotivo;
    private JButton btnGuardar, btnVerHistorial;
    private ApiService apiService;

    public CitasFrame() {
        setTitle("Agenda de Citas");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 248, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Agenda de Citas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;

        // ID Cita
        JLabel lblIdCita = new JLabel("ID Cita:");
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblIdCita, gbc);
        txtIdCita = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtIdCita, gbc);

        // Nombre del Dueño
        JLabel lblDueno = new JLabel("Nombre Dueño:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblDueno, gbc);
        txtNombreDueno = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtNombreDueno, gbc);

        // Hora
        JLabel lblHora = new JLabel("Hora (ej. 5pm):");
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblHora, gbc);
        txtHora = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(txtHora, gbc);

        // Veterinario
        JLabel lblVeterinario = new JLabel("Veterinario:");
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblVeterinario, gbc);
        txtVeterinario = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(txtVeterinario, gbc);

        // Nombre de la Mascota
        JLabel lblMascota = new JLabel("Nombre Mascota:");
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(lblMascota, gbc);
        txtNombreMascota = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(txtNombreMascota, gbc);

        // Fecha
        JLabel lblFecha = new JLabel("Fecha (dd/mm/aaaa):");
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(lblFecha, gbc);
        txtFecha = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(txtFecha, gbc);

        // Motivo
        JLabel lblMotivo = new JLabel("Motivo:");
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(lblMotivo, gbc);
        txtMotivo = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 7;
        panel.add(txtMotivo, gbc);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnVerHistorial = new JButton("Ver Historial");
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(btnGuardar, gbc);
        gbc.gridx = 1; gbc.gridy = 8;
        panel.add(btnVerHistorial, gbc);

        btnGuardar.addActionListener(e -> guardarCita());
        btnVerHistorial.addActionListener(e -> verHistorialCitas());

        add(panel);
    }

    private void guardarCita() {
        String idCita = txtIdCita.getText();
        String nombreDueno = txtNombreDueno.getText();
        String hora = txtHora.getText();
        String veterinario = txtVeterinario.getText();
        String nombreMascota = txtNombreMascota.getText();
        String fecha = txtFecha.getText();
        String motivo = txtMotivo.getText();

        if (idCita.isEmpty() || nombreDueno.isEmpty() || hora.isEmpty() || veterinario.isEmpty() 
            || nombreMascota.isEmpty() || fecha.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CitaDTO dto = new CitaDTO(idCita, nombreDueno, hora, veterinario, nombreMascota, fecha, motivo);

        try {
            Call<String> call = apiService.guardarCita(dto);
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(this, response.body(), "Información", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar
                txtIdCita.setText("");
                txtNombreDueno.setText("");
                txtHora.setText("");
                txtVeterinario.setText("");
                txtNombreMascota.setText("");
                txtFecha.setText("");
                txtMotivo.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + response.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verHistorialCitas() {
        try {
            Call<List<Cita>> call = apiService.getHistorialCitas();
            Response<List<Cita>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                List<Cita> lista = response.body();
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay citas registradas.", "Historial", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (Cita c : lista) {
                    sb.append("ID: ").append(c.getIdCita())
                      .append(", Mascota: ").append(c.getNombreMascota())
                      .append(", Dueño: ").append(c.getNombreDueno())
                      .append(", Fecha: ").append(c.getFecha())
                      .append(", Hora: ").append(c.getHora())
                      .append("\n");
                }
                JTextArea textArea = new JTextArea(sb.toString(), 10, 30);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(this, scrollPane, "Historial de Citas", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener historial: " + response.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
