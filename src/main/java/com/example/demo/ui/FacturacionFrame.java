package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.FacturaDTO;
import com.example.demo.model.Cliente;
import com.example.demo.model.Mascota;
import com.example.demo.model.Factura;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FacturacionFrame extends JFrame {
    private JTextField txtIdFactura;
    private JComboBox<String> comboClientes;
    private JComboBox<String> comboMascotas;
    private JComboBox<String> cbServicio;
    private JTextField txtTotal;
    private JComboBox<String> cbMetodoPago;
    private JButton btnGenerar, btnVerHistorial;

    private ApiService apiService;

    public FacturacionFrame() {
        setTitle("Facturación");
        setSize(450, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        initComponents();
        cargarClientesEnCombo();
        cargarMascotasEnCombo();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 250, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID Factura
        JLabel lblIdFactura = new JLabel("ID Factura:");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        panel.add(lblIdFactura, gbc);
        txtIdFactura = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(txtIdFactura, gbc);

        // Cliente
        JLabel lblCliente = new JLabel("Cliente (ID - Nombre):");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(lblCliente, gbc);
        comboClientes = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(comboClientes, gbc);

        // Mascota
        JLabel lblMascota = new JLabel("Mascota (ID - Nombre):");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(lblMascota, gbc);
        comboMascotas = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(comboMascotas, gbc);

        // Servicio
        JLabel lblServicio = new JLabel("Servicio:");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(lblServicio, gbc);
        cbServicio = new JComboBox<>(new String[]{"consulta","medicación/alimento/accesorio"});
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(cbServicio, gbc);

        // Total
        JLabel lblTotal = new JLabel("Total:");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panel.add(lblTotal, gbc);
        txtTotal = new JTextField();
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(txtTotal, gbc);

        // Método de Pago
        JLabel lblMetodo = new JLabel("Método de Pago:");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panel.add(lblMetodo, gbc);
        cbMetodoPago = new JComboBox<>(new String[]{"efectivo","tarjeta","transferencia"});
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(cbMetodoPago, gbc);

        // Botones
        btnGenerar = new JButton("Generar Factura");
        btnVerHistorial = new JButton("Ver Historial");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        panel.add(btnGenerar, gbc);
        gbc.gridy = 7;
        panel.add(btnVerHistorial, gbc);

        btnGenerar.addActionListener(e -> generarFactura());
        btnVerHistorial.addActionListener(e -> verHistorialFacturas());

        add(panel);
    }

    private void cargarClientesEnCombo() {
        try {
            Call<List<Cliente>> call = apiService.getHistorialClientes();
            Response<List<Cliente>> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null) {
                comboClientes.removeAllItems();
                for (Cliente c : resp.body()) {
                    comboClientes.addItem(c.getId() + " - " + c.getNombre());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        }
    }

    private void generarFactura() {
        String idFactura = txtIdFactura.getText().trim();
        if (idFactura.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID de factura es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selCliente = (String) comboClientes.getSelectedItem();
        String nombreCliente = selCliente != null
            ? selCliente.split(" - ", 2)[1]
            : "";

        String selMascota = (String) comboMascotas.getSelectedItem();
        String nombreMascota = selMascota != null
            ? selMascota.split(" - ", 2)[1]
            : "";

        String servicio = (String) cbServicio.getSelectedItem();
        String totalStr = txtTotal.getText().trim();
        String metodoPago = (String) cbMetodoPago.getSelectedItem();

        if (nombreCliente.isEmpty() || nombreMascota.isEmpty() || totalStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total;
        try {
            total = Double.parseDouble(totalStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Total inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        FacturaDTO dto = new FacturaDTO(
            idFactura, nombreCliente, nombreMascota,
            servicio, LocalDate.now(), total, metodoPago
        );

        try {
            Call<String> call = apiService.generarFactura(dto);
            Response<String> resp = call.execute();
            if (resp.isSuccessful()) {
                JOptionPane.showMessageDialog(this, resp.body(), "Información", JOptionPane.INFORMATION_MESSAGE);
                txtIdFactura.setText("");
                txtTotal.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + resp.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verHistorialFacturas() {
        try {
            Call<List<Factura>> call = apiService.getHistorialFacturas();
            Response<List<Factura>> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null) {
                List<Factura> lista = resp.body();
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay facturas registradas.", "Historial", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (Factura f : lista) {
                    sb.append("ID: ").append(f.getIdFactura())
                      .append(", Cliente: ").append(f.getNombreCliente())
                      .append(", Mascota: ").append(f.getNombreMascota())
                      .append(", Total: ").append(f.getTotal())
                      .append(", Fecha: ").append(f.getFecha())
                      .append("\n");
                }
                JTextArea ta = new JTextArea(sb.toString(), 10, 40);
                ta.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Historial de Facturas", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + resp.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
