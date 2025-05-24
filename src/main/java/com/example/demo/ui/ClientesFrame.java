package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.model.Cliente;
import com.example.demo.model.Mascota;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientesFrame extends JFrame {
    private JTextField txtBuscarClienteId;
    private JButton btnBuscarCliente;

    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JComboBox<String> comboMascotas;

    private JButton btnGuardar;
    private JButton btnVerHistorial;

    private ApiService apiService;
    private List<Mascota> listaMascotas;

    public ClientesFrame() {
        setTitle("Gestión de Clientes");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        initComponents();
        cargarMascotasEnCombo();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 245, 238));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Buscar Cliente ID
        JLabel lblBuscar = new JLabel("Buscar Cliente (ID):");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        panel.add(lblBuscar, gbc);

        txtBuscarClienteId = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtBuscarClienteId, gbc);

        btnBuscarCliente = new JButton("Buscar");
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(btnBuscarCliente, gbc);
        btnBuscarCliente.addActionListener(e -> buscarClientePorId());

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(lblNombre, gbc);

        txtNombre = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(txtNombre, gbc);

        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(lblTelefono, gbc);

        txtTelefono = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(txtTelefono, gbc);

        // Correo
        JLabel lblCorreo = new JLabel("Correo:");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(lblCorreo, gbc);

        txtCorreo = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(txtCorreo, gbc);

        // Mascota: sustituye el campo de texto por un combo
        JLabel lblMascota = new JLabel("Mascota (ID - Nombre):");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panel.add(lblMascota, gbc);

        comboMascotas = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(comboMascotas, gbc);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnVerHistorial = new JButton("Ver Historial");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        panel.add(btnGuardar, gbc);
        gbc.gridy = 6;
        panel.add(btnVerHistorial, gbc);

        btnGuardar.addActionListener(e -> guardarCliente());
        btnVerHistorial.addActionListener(e -> verHistorialClientes());

        setContentPane(panel);
    }

    private void cargarMascotasEnCombo() {
        try {
            Call<List<Mascota>> call = apiService.getHistorialMascotas();
            Response<List<Mascota>> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null) {
                listaMascotas = resp.body();
                comboMascotas.removeAllItems();
                for (Mascota m : listaMascotas) {
                    comboMascotas.addItem(m.getId() + " - " + m.getNombre());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void buscarClientePorId() {
        String idStr = txtBuscarClienteId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un ID de cliente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Long id = Long.parseLong(idStr);
            Call<Cliente> call = apiService.getClienteById(id);
            Response<Cliente> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null) {
                Cliente c = resp.body();
                txtNombre.setText(c.getNombre());
                txtTelefono.setText(c.getTelefono());
                txtCorreo.setText(c.getCorreo());
                // seleccionar mascota en el combo
                for (int i = 0; i < comboMascotas.getItemCount(); i++) {
                    if (comboMascotas.getItemAt(i).endsWith(" - " + c.getNombreMascota())) {
                        comboMascotas.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    resp.code()==404
                      ? "No se encontró el cliente con ID: " + id
                      : "Error: " + resp.message(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarCliente() {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String sel = (String) comboMascotas.getSelectedItem();
        String nombreMascota = sel != null ? sel.split(" - ",2)[1] : "";

        if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty() || nombreMascota.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClienteDTO dto = new ClienteDTO(nombre, telefono, correo, nombreMascota);
        try {
            Call<String> call = apiService.guardarCliente(dto);
            Response<String> resp = call.execute();
            if (resp.isSuccessful()) {
                JOptionPane.showMessageDialog(this, resp.body(), "Información", JOptionPane.INFORMATION_MESSAGE);
                // después de guardar, recargar el histórico de clientes si fuera necesario...
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + resp.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verHistorialClientes() {
        try {
            Call<List<Cliente>> call = apiService.getHistorialClientes();
            Response<List<Cliente>> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null) {
                List<Cliente> lista = resp.body();
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay clientes registrados.", "Historial", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (Cliente c : lista) {
                    sb.append("ID: ").append(c.getId())
                      .append(", Nombre: ").append(c.getNombre())
                      .append(", Mascota: ").append(c.getNombreMascota())
                      .append("\n");
                }
                JTextArea ta = new JTextArea(sb.toString(), 10, 40);
                ta.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Historial de Clientes", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + resp.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
