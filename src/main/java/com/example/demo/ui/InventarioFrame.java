package com.example.demo.ui;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.ProductoDTO;
import com.example.demo.model.Producto;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioFrame extends JFrame {
    private JTextField txtNombreProducto, txtCantidad, txtPrecio;
    private JComboBox<String> cbCategoria;
    private JButton btnAgregar, btnRefrescar, btnEliminarSeleccionado;
    private JList<Producto> listProductos;
    private DefaultListModel<Producto> listModel; // para manejar la lista
    private ApiService apiService;

    public InventarioFrame() {
        setTitle("Inventario de Productos");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicializar Retrofit
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Inventario de Productos");
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
        txtNombreProducto = new JTextField(15);
        gbc.gridx = 1; 
        gbc.gridy = 1;
        panel.add(txtNombreProducto, gbc);

        // Categoría
        JLabel lblCategoria = new JLabel("Categoría:");
        gbc.gridx = 0; 
        gbc.gridy = 2;
        panel.add(lblCategoria, gbc);
        String[] categorias = {"alimento", "medicamento", "accesorio"};
        cbCategoria = new JComboBox<>(categorias);
        gbc.gridx = 1; 
        gbc.gridy = 2;
        panel.add(cbCategoria, gbc);

        // Cantidad
        JLabel lblCantidad = new JLabel("Cantidad:");
        gbc.gridx = 0; 
        gbc.gridy = 3;
        panel.add(lblCantidad, gbc);
        txtCantidad = new JTextField(10);
        gbc.gridx = 1; 
        gbc.gridy = 3;
        panel.add(txtCantidad, gbc);

        // Precio
        JLabel lblPrecio = new JLabel("Precio Unitario:");
        gbc.gridx = 0; 
        gbc.gridy = 4;
        panel.add(lblPrecio, gbc);
        txtPrecio = new JTextField(10);
        gbc.gridx = 1; 
        gbc.gridy = 4;
        panel.add(txtPrecio, gbc);

        // Botón Agregar
        btnAgregar = new JButton("Agregar");
        gbc.gridx = 0; 
        gbc.gridy = 5; 
        panel.add(btnAgregar, gbc);

        // Acción Agregar
        btnAgregar.addActionListener(e -> agregarProducto());

        // Panel para la lista y botones de refrescar/eliminar
        gbc.gridx = 0; 
        gbc.gridy = 6; 
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; 
        gbc.weightx = 1.0;

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(new Color(240, 255, 255));

        // Lista de productos
        listModel = new DefaultListModel<>();
        listProductos = new JList<>(listModel);
        listProductos.setCellRenderer(new ProductoCellRenderer()); 
        /*
         * ProductoCellRenderer: 
         * para mostrar "ID - Nombre (Cantidad)" en la lista, lo definimos abajo.
         */

        JScrollPane scrollPane = new JScrollPane(listProductos);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones (Refrescar y Eliminar Seleccionado)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnRefrescar = new JButton("Refrescar Lista");
        btnEliminarSeleccionado = new JButton("Eliminar Seleccionado");
        bottomPanel.add(btnRefrescar);
        bottomPanel.add(btnEliminarSeleccionado);

        // Acciones
        btnRefrescar.addActionListener(e -> refrescarLista());
        btnEliminarSeleccionado.addActionListener(e -> eliminarSeleccionado());

        listPanel.add(bottomPanel, BorderLayout.SOUTH);
        panel.add(listPanel, gbc);

        add(panel);
    }

    private void agregarProducto() {
        String nombre = txtNombreProducto.getText().trim();
        String categoria = (String) cbCategoria.getSelectedItem();
        String cantStr = txtCantidad.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        if (nombre.isEmpty() || cantStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa los campos de producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        double precio;
        try {
            cantidad = Integer.parseInt(cantStr);
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad o precio inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProductoDTO dto = new ProductoDTO(nombre, categoria, cantidad, precio);

        try {
            Call<String> call = apiService.agregarProducto(dto);
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(this, response.body(), "Información", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                txtNombreProducto.setText("");
                txtCantidad.setText("");
                txtPrecio.setText("");
                // Refrescar la lista para ver el nuevo producto
                refrescarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar: " + response.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescarLista() {
        try {
            Call<List<Producto>> call = apiService.getHistorialProductos();
            Response<List<Producto>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                List<Producto> lista = response.body();
                listModel.clear(); // limpiamos la lista
                for (Producto p : lista) {
                    listModel.addElement(p);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener historial: " + response.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        Producto seleccionado = listProductos.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long id = seleccionado.getId();

        try {
            Call<String> call = apiService.eliminarProducto(id);
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(this, response.body(), "Información", JOptionPane.INFORMATION_MESSAGE);
                // Refrescar lista
                refrescarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + response.message(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Renderizador para mostrar ID y nombre en la lista
     */
    private static class ProductoCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Producto) {
                Producto p = (Producto) value;
                // Ejemplo de texto: "ID: 3 - Croquetas (10 uds)"
                setText("ID: " + p.getId() + " - " + p.getNombre() + " (" + p.getCantidadDisponible() + " uds)");
            }
            return this;
        }
    }
}
