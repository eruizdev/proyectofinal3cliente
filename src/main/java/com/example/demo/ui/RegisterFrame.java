package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.RegisterDTO;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JComboBox<String> rolCombo;
    private JButton registerButton;
    private ApiService apiService;

    public RegisterFrame() {
        setTitle("Veterinaria - Register");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicializar Retrofit
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 250, 240)); // un color crema suave
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        userField = new JTextField(20);
        gbc.gridx = 1; 
        gbc.gridy = 1;
        panel.add(userField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; 
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        passField = new JPasswordField(20);
        gbc.gridx = 1; 
        gbc.gridy = 2;
        panel.add(passField, gbc);

        // Rol
        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; 
        gbc.gridy = 3;
        panel.add(rolLabel, gbc);

        String[] roles = {"admin", "veterinario", "usuario normal"};
        rolCombo = new JComboBox<>(roles);
        gbc.gridx = 1; 
        gbc.gridy = 3;
        panel.add(rolCombo, gbc);

        // Botón Register
        registerButton = new JButton("Registrar");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; 
        gbc.gridy = 4; 
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> doRegister());

        add(panel);
    }

    private void doRegister() {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        String rol = (String) rolCombo.getSelectedItem();

        RegisterDTO registerDTO = new RegisterDTO(username, password, rol);

        try {
            Call<String> call = apiService.register(registerDTO);
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(this, 
                    "Registro exitoso: " + response.body(), 
                    "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
                // Puedes cerrar la ventana de registro si quieres:
                this.dispose();
            } else {
                String error = response.errorBody() != null ? response.errorBody().string() : "Sin detalles";
                JOptionPane.showMessageDialog(this, 
                    "Error en registro: " + error, 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Ocurrió un error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
