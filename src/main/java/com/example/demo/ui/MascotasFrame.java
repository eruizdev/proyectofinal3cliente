package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.MascotaDTO;
import com.example.demo.model.Mascota;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MascotasFrame extends JFrame {

    private JTextField txtNombre, txtEspecie, txtRaza, txtEdad, txtDueno, txtSintomas;
    private JButton btnGuardar, btnVerHistorial;
    private ApiService apiService;

    public MascotasFrame() {
        setTitle("Gestión de Mascotas");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicializar ApiService (Retrofit)
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 255, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Gestión de Mascotas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        gbc.gridx = 0; 
        gbc.gridy = 1;
        panel.add(lblNombre, gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1; 
        gbc.gridy = 1;
        panel.add(txtNombre, gbc);

        // Especie
        JLabel lblEspecie = new JLabel("Especie:");
        gbc.gridx = 0; 
        gbc.gridy = 2;
        panel.add(lblEspecie, gbc);
        txtEspecie = new JTextField(15);
        gbc.gridx = 1; 
        gbc.gridy = 2;
        panel.add(txtEspecie, gbc);

        // Raza
        JLabel lblRaza = new JLabel("Raza:");
        gbc.gridx = 0; 
        gbc.gridy = 3;
        panel.add(lblRaza, gbc);
        txtRaza = new JTextField(15);
        gbc.gridx = 1; 
        gbc.gridy = 3;
        panel.add(txtRaza, gbc);

        // Edad
        JLabel lblEdad = new JLabel("Edad:");
        gbc.gridx = 0; 
        gbc.gridy = 4;
        panel.add(lblEdad, gbc);
        txtEdad = new JTextField(5);
        gbc.gridx = 1; 
        gbc.gridy = 4;
        panel.add(txtEdad, gbc);

    
        // Síntomas
        JLabel lblSintomas = new JLabel("Síntomas:");
        gbc.gridx = 0; 
        gbc.gridy = 6;
        panel.add(lblSintomas, gbc);
        txtSintomas = new JTextField(15);
        gbc.gridx = 1; 
        gbc.gridy = 6;
        panel.add(txtSintomas, gbc);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnVerHistorial = new JButton("Ver Historial");
        gbc.gridx = 0; 
        gbc.gridy = 7;
        panel.add(btnGuardar, gbc);
        gbc.gridx = 1; 
        gbc.gridy = 7;
        panel.add(btnVerHistorial, gbc);

        // ActionListeners
        btnGuardar.addActionListener(e -> guardarMascota());
        btnVerHistorial.addActionListener(e -> verHistorialMascotas());

        add(panel);
    }

    private void guardarMascota() {
        // Tomar los valores de los campos
        String nombre = txtNombre.getText();
        String especie = txtEspecie.getText();
        String raza = txtRaza.getText();
        String edadStr = txtEdad.getText();
        String dueno = txtDueno.getText();
        String sintomas = txtSintomas.getText();

        // Validaciones mínimas
        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || edadStr.isEmpty() || dueno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el DTO
        MascotaDTO dto = new MascotaDTO(nombre, especie, raza, edad, dueno, sintomas);

        // Llamada a la API (síncrona)
        try {
            Call<String> call = apiService.guardarMascota(dto);
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                // El backend retorna algo como "Mascota guardada exitosamente"
                String respuesta = response.body();
                JOptionPane.showMessageDialog(this, respuesta, "Información", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                txtNombre.setText("");
                txtEspecie.setText("");
                txtRaza.setText("");
                txtEdad.setText("");
                txtDueno.setText("");
                txtSintomas.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + response.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verHistorialMascotas() {
        // Llamada a la API para obtener la lista de mascotas
        try {
            Call<List<Mascota>> call = apiService.getHistorialMascotas();
            Response<List<Mascota>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                List<Mascota> lista = response.body();
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay mascotas registradas.", "Historial", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                // Mostrar el historial en un cuadro de texto
                StringBuilder sb = new StringBuilder();
                for (Mascota m : lista) {
                    sb.append("ID: ").append(m.getId())
                      .append(", Nombre: ").append(m.getNombre())
                      .append(", Dueño: ").append(m.getNombreDueno())
                      .append(", Especie: ").append(m.getEspecie())
                      .append("\n");
                }
                JTextArea textArea = new JTextArea(sb.toString(), 10, 30);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(this, scrollPane, "Historial de Mascotas", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener historial: " + response.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}