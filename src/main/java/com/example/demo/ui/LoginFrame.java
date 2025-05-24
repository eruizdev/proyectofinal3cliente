package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.LoginDTO;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton, registerButton;
    private ApiService apiService;

    public LoginFrame() {
        setTitle("Veterinaria - Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializar Retrofit
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Veterinaria");
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

        // Botón Login
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; 
        gbc.gridy = 3; 
        gbc.gridwidth = 1;
        panel.add(loginButton, gbc);

        // Botón Register
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 1; 
        gbc.gridy = 3; 
        panel.add(registerButton, gbc);

        // Acciones
        loginButton.addActionListener(e -> doLogin());
        registerButton.addActionListener(e -> {
            // Abrir ventana de registro
            new RegisterFrame().setVisible(true);
            // Podríamos cerrar la ventana de login, o no. Depende de tu flujo:
            // this.dispose();
        });

        add(panel);
    }

    private void doLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        LoginDTO loginDTO = new LoginDTO(username, password);

        try {
            Call<String> call = apiService.login(loginDTO);
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                String rol = response.body();
                if ("admin".equalsIgnoreCase(rol) || "veterinario".equalsIgnoreCase(rol)) {
                    JOptionPane.showMessageDialog(this, 
                        "Login exitoso. Rol: " + rol, 
                        "Información", 
                        JOptionPane.INFORMATION_MESSAGE);
                    // Abre AdminMenuFrame
                    AdminMenuFrame menuFrame = new AdminMenuFrame();
                    menuFrame.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Login exitoso. Rol: " + rol + ". Sin acceso a menú.", 
                        "Información", 
                        JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error en login: " + response.message(), 
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
