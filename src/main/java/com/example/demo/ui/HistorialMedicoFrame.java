package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.HistorialMedicoDTO;
import com.example.demo.model.HistorialMedico;
import com.example.demo.model.Mascota;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HistorialMedicoFrame extends JFrame {
    private JComboBox<String> comboMascotas;
    private JTextField txtFecha, txtDiagnostico, txtTratamiento;
    private JButton btnGuardar, btnVerHistorial;
    private ApiService apiService;

    public HistorialMedicoFrame() {
        setTitle("Historial Médico");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        initComponents();
        cargarMascotasEnCombo();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 255, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Combo de Mascotas
        JLabel lblMascota = new JLabel("Mascota (ID - Nombre):");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        panel.add(lblMascota, gbc);

        comboMascotas = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(comboMascotas, gbc);

        // Fecha
        JLabel lblFecha = new JLabel("Fecha (dd/mm/aaaa):");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(lblFecha, gbc);
        txtFecha = new JTextField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(txtFecha, gbc);

        // Diagnóstico
        JLabel lblDiagnostico = new JLabel("Diagnóstico:");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(lblDiagnostico, gbc);
        txtDiagnostico = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(txtDiagnostico, gbc);

        // Tratamiento
        JLabel lblTratamiento = new JLabel("Tratamiento:");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(lblTratamiento, gbc);
        txtTratamiento = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(txtTratamiento, gbc);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnVerHistorial = new JButton("Ver Historial");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        panel.add(btnGuardar, gbc);
        gbc.gridy = 5;
        panel.add(btnVerHistorial, gbc);

        btnGuardar.addActionListener(e -> guardarHistorial());
        btnVerHistorial.addActionListener(e -> verHistorialMedico());

        add(panel);
    }

    private void cargarMascotasEnCombo() {
        try {
            Call<List<Mascota>> call = apiService.getHistorialMascotas();
            Response<List<Mascota>> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null) {
                comboMascotas.removeAllItems();
                for (Mascota m : resp.body()) {
                    comboMascotas.addItem(m.getId() + " - " + m.getNombre());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // opcional: mostrar mensaje de error
        }
    }

    private void guardarHistorial() {
        String seleccionado = (String) comboMascotas.getSelectedItem();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una mascota.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long idMascota = Long.parseLong(seleccionado.split(" - ")[0]);
        String fecha = txtFecha.getText().trim();
        String diagnostico = txtDiagnostico.getText().trim();
        String tratamiento = txtTratamiento.getText().trim();

        if (fecha.isEmpty() || diagnostico.isEmpty() || tratamiento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HistorialMedicoDTO dto = new HistorialMedicoDTO(idMascota, fecha, diagnostico, tratamiento);
        try {
            Call<String> call = apiService.guardarHistorialMedico(dto);
            Response<String> resp = call.execute();
            if (resp.isSuccessful()) {
                JOptionPane.showMessageDialog(this, resp.body(), "Información", JOptionPane.INFORMATION_MESSAGE);
                // limpiar
                txtFecha.setText("");
                txtDiagnostico.setText("");
                txtTratamiento.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + resp.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verHistorialMedico() {
        try {
            Call<List<HistorialMedico>> call = apiService.getHistorialMedico();
            Response<List<HistorialMedico>> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null) {
                List<HistorialMedico> lista = resp.body();
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay historial médico.", "Historial", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (HistorialMedico hm : lista) {
                    sb.append("ID Mascota: ").append(hm.getIdMascota())
                      .append(", Fecha: ").append(hm.getFecha())
                      .append(", Diagnóstico: ").append(hm.getDiagnostico())
                      .append(", Tratamiento: ").append(hm.getTratamiento())
                      .append("\n");
                }
                JTextArea textArea = new JTextArea(sb.toString(), 10, 30);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(this, scrollPane, "Historial Médico", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener historial: " + resp.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
