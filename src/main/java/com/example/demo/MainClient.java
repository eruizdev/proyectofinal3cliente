package com.example.demo;

import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.dto.*;
import com.example.demo.model.*;

import retrofit2.Call;
import retrofit2.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        boolean exit = false;

        while (!exit) {
            System.out.println("\n----- MENU PRINCIPAL -----");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Gestión de Mascotas");
            System.out.println("4. Gestión de Clientes");
            System.out.println("5. Historial Médico");
            System.out.println("6. Agenda de Citas");
            System.out.println("7. Inventario");
            System.out.println("8. Facturación");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");

            int option = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (option) {
                case 1:
                    login(sc, apiService);
                    break;
                case 2:
                    register(sc, apiService);
                    break;
                case 3:
                    gestionarMascotas(sc, apiService);
                    break;
                case 4:
                    gestionarClientes(sc, apiService);
                    break;
                case 5:
                    gestionarHistorialMedico(sc, apiService);
                    break;
                case 6:
                    gestionarCitas(sc, apiService);
                    break;
                case 7:
                    gestionarInventario(sc, apiService);
                    break;
                case 8:
                    gestionarFacturacion(sc, apiService);
                    break;
                case 9:
                    exit = true;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        sc.close();
    }

    // MÉTODO LOGIN MODIFICADO:
    // Se obtiene el rol devuelto por el backend. Si es "admin" o "veterinario", se permite acceso al menú.
    private static void login(Scanner sc, ApiService apiService) {
        try {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            LoginDTO loginDTO = new LoginDTO(username, password);
            Call<String> call = apiService.login(loginDTO);
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                String rol = response.body();
                // Tanto "admin" como "veterinario" tienen acceso al menú
                if (rol.equalsIgnoreCase("admin") || rol.equalsIgnoreCase("veterinario")) {
                    System.out.println("Login exitoso. Rol: " + rol + ". Accediendo al menú de administración...");
                    // Aquí puedes invocar el método que muestre el menú de administración
                } else {
                    System.out.println("Login exitoso. Rol: " + rol + ". No tienes acceso a ningún menú.");
                }
            } else {
                System.out.println("Error en login: " + response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODO REGISTER CON ERROR MOSTRADO COMPLETO:
    private static void register(Scanner sc, ApiService apiService) {
        try {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            System.out.print("Rol (admin/veterinario/usuario normal): ");
            String rol = sc.nextLine();

            RegisterDTO registerDTO = new RegisterDTO(username, password, rol);
            Call<String> call = apiService.register(registerDTO);
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                System.out.println("Respuesta: " + response.body());
            } else {
                String error = response.errorBody() != null ? response.errorBody().string() : "Sin detalles";
                System.out.println("Error en registro: " + error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void gestionarMascotas(Scanner sc, ApiService apiService) {
        System.out.println("\n--- GESTIÓN DE MASCOTAS ---");
        System.out.println("1. Guardar nueva mascota");
        System.out.println("2. Ver historial de mascotas");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1:
                try {
                    System.out.print("Nombre de la mascota: ");
                    String nombre = sc.nextLine();
                    System.out.print("Especie: ");
                    String especie = sc.nextLine();
                    System.out.print("Raza: ");
                    String raza = sc.nextLine();
                    System.out.print("Edad: ");
                    int edad = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nombre del dueño: ");
                    String dueno = sc.nextLine();
                    System.out.print("Síntomas: ");
                    String sintomas = sc.nextLine();

                    MascotaDTO dto = new MascotaDTO(nombre, especie, raza, edad, dueno, sintomas);
                    Call<String> call = apiService.guardarMascota(dto);
                    Response<String> response = call.execute();
                    if (response.isSuccessful()) {
                        System.out.println(response.body());
                    } else {
                        System.out.println("Error al guardar mascota: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Call<List<Mascota>> call = apiService.getHistorialMascotas();
                    Response<List<Mascota>> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        List<Mascota> mascotas = response.body();
                        System.out.println("HISTORIAL DE MASCOTAS:");
                        for (Mascota m : mascotas) {
                            System.out.println("ID: " + m.getId() + ", Nombre: " + m.getNombre() + ", Dueño: " + m.getNombreDueno());
                        }
                    } else {
                        System.out.println("Error al obtener historial: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Opción inválida en Gestión de Mascotas.");
        }
    }

    private static void gestionarClientes(Scanner sc, ApiService apiService) {
        System.out.println("\n--- GESTIÓN DE CLIENTES ---");
        System.out.println("1. Guardar nuevo cliente");
        System.out.println("2. Ver historial de clientes");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1:
                try {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = sc.nextLine();
                    System.out.print("Correo: ");
                    String correo = sc.nextLine();
                    System.out.print("Nombre de la mascota: ");
                    String nombreMascota = sc.nextLine();

                    ClienteDTO dto = new ClienteDTO(nombre, telefono, correo, nombreMascota);
                    Call<String> call = apiService.guardarCliente(dto);
                    Response<String> response = call.execute();
                    if (response.isSuccessful()) {
                        System.out.println(response.body());
                    } else {
                        System.out.println("Error al guardar cliente: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Call<List<Cliente>> call = apiService.getHistorialClientes();
                    Response<List<Cliente>> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        List<Cliente> clientes = response.body();
                        System.out.println("HISTORIAL DE CLIENTES:");
                        for (Cliente c : clientes) {
                            System.out.println("ID: " + c.getId() + ", Nombre: " + c.getNombre() + ", Mascota: " + c.getNombreMascota());
                        }
                    } else {
                        System.out.println("Error al obtener historial de clientes: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Opción inválida en Gestión de Clientes.");
        }
    }

    private static void gestionarHistorialMedico(Scanner sc, ApiService apiService) {
        System.out.println("\n--- HISTORIAL MÉDICO ---");
        System.out.println("1. Guardar nuevo historial");
        System.out.println("2. Ver historial médico");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1:
                try {
                    System.out.print("ID de la mascota: ");
                    Long idMascota = sc.nextLong();
                    sc.nextLine();
                    System.out.print("Fecha (dd/mm/aaaa): ");
                    String fecha = sc.nextLine();
                    System.out.print("Diagnóstico: ");
                    String diag = sc.nextLine();
                    System.out.print("Tratamiento: ");
                    String trat = sc.nextLine();

                    HistorialMedicoDTO dto = new HistorialMedicoDTO(idMascota, fecha, diag, trat);
                    Call<String> call = apiService.guardarHistorialMedico(dto);
                    Response<String> response = call.execute();
                    if (response.isSuccessful()) {
                        System.out.println(response.body());
                    } else {
                        System.out.println("Error al guardar historial médico: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Call<List<HistorialMedico>> call = apiService.getHistorialMedico();
                    Response<List<HistorialMedico>> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        List<HistorialMedico> historiales = response.body();
                        System.out.println("HISTORIAL MÉDICO:");
                        for (HistorialMedico h : historiales) {
                            System.out.println("ID Mascota: " + h.getIdMascota() + ", Fecha: " + h.getFecha() +
                                    ", Diagnóstico: " + h.getDiagnostico() + ", Tratamiento: " + h.getTratamiento());
                        }
                    } else {
                        System.out.println("Error al obtener historial médico: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Opción inválida en Historial Médico.");
        }
    }

    private static void gestionarCitas(Scanner sc, ApiService apiService) {
        System.out.println("\n--- AGENDA DE CITAS ---");
        System.out.println("1. Guardar nueva cita");
        System.out.println("2. Ver historial de citas");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1:
                try {
                    System.out.print("ID de la cita (manual): ");
                    String idCita = sc.nextLine();
                    System.out.print("Nombre del dueño: ");
                    String dueno = sc.nextLine();
                    System.out.print("Hora de la cita (ej. 5pm): ");
                    String hora = sc.nextLine();
                    System.out.print("Veterinario asignado: ");
                    String vet = sc.nextLine();
                    System.out.print("Nombre de la mascota: ");
                    String nomMascota = sc.nextLine();
                    System.out.print("Fecha (dd/mm/aaaa): ");
                    String fecha = sc.nextLine();
                    System.out.print("Motivo de la cita: ");
                    String motivo = sc.nextLine();

                    CitaDTO dto = new CitaDTO(idCita, dueno, hora, vet, nomMascota, fecha, motivo);
                    Call<String> call = apiService.guardarCita(dto);
                    Response<String> response = call.execute();
                    if (response.isSuccessful()) {
                        System.out.println(response.body());
                    } else {
                        System.out.println("Error al guardar cita: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Call<List<Cita>> call = apiService.getHistorialCitas();
                    Response<List<Cita>> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        List<Cita> citas = response.body();
                        System.out.println("HISTORIAL DE CITAS:");
                        for (Cita c : citas) {
                            System.out.println("ID: " + c.getIdCita() + ", Mascota: " + c.getNombreMascota() +
                                    ", Dueño: " + c.getNombreDueno() + ", Fecha: " + c.getFecha());
                        }
                    } else {
                        System.out.println("Error al obtener historial de citas: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Opción inválida en Agenda de Citas.");
        }
    }

    private static void gestionarInventario(Scanner sc, ApiService apiService) {
        System.out.println("\n--- INVENTARIO ---");
        System.out.println("1. Agregar producto");
        System.out.println("2. Eliminar producto");
        System.out.println("3. Ver historial de productos");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1:
                try {
                    System.out.print("Nombre del producto: ");
                    String nombre = sc.nextLine();
                    System.out.print("Categoría (alimento/medicamento/accesorio): ");
                    String categoria = sc.nextLine();
                    System.out.print("Cantidad disponible: ");
                    int cantidad = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Precio unitario: ");
                    double precio = sc.nextDouble();
                    sc.nextLine();

                    ProductoDTO dto = new ProductoDTO(nombre, categoria, cantidad, precio);
                    Call<String> call = apiService.agregarProducto(dto);
                    Response<String> response = call.execute();
                    if (response.isSuccessful()) {
                        System.out.println(response.body());
                    } else {
                        System.out.println("Error al agregar producto: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    System.out.print("ID del producto a eliminar: ");
                    Long id = sc.nextLong();
                    sc.nextLine();
                    Call<String> call = apiService.eliminarProducto(id);
                    Response<String> response = call.execute();
                    if (response.isSuccessful()) {
                        System.out.println(response.body());
                    } else {
                        System.out.println("Error al eliminar producto: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    Call<List<Producto>> call = apiService.getHistorialProductos();
                    Response<List<Producto>> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        List<Producto> productos = response.body();
                        System.out.println("HISTORIAL DE PRODUCTOS:");
                        for (Producto p : productos) {
                            System.out.println("ID: " + p.getId() + ", Nombre: " + p.getNombre() +
                                    ", Categoría: " + p.getCategoria() + ", Cantidad: " + p.getCantidadDisponible());
                        }
                    } else {
                        System.out.println("Error al obtener historial de productos: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Opción inválida en Inventario.");
        }
    }

    private static void gestionarFacturacion(Scanner sc, ApiService apiService) {
        System.out.println("\n--- FACTURACIÓN ---");
        System.out.println("1. Generar factura");
        System.out.println("2. Ver historial de facturas");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1:
                try {
                    System.out.print("ID de la factura (manual): ");
                    String idFactura = sc.nextLine();
                    System.out.print("Nombre del cliente: ");
                    String nomCliente = sc.nextLine();
                    System.out.print("Nombre de la mascota: ");
                    String nomMascota = sc.nextLine();
                    System.out.print("Servicio realizado (consulta o medicación/alimento/accesorio): ");
                    String servicio = sc.nextLine();
                    System.out.print("Total a pagar: ");
                    double total = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Método de pago (efectivo/tarjeta/transferencia): ");
                    String metodoPago = sc.nextLine();

                    LocalDate fecha = LocalDate.now();

                    FacturaDTO dto = new FacturaDTO(idFactura, nomCliente, nomMascota, servicio, fecha, total, metodoPago);
                    Call<String> call = apiService.generarFactura(dto);
                    Response<String> response = call.execute();
                    if (response.isSuccessful()) {
                        System.out.println(response.body());
                    } else {
                        System.out.println("Error al generar factura: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Call<List<Factura>> call = apiService.getHistorialFacturas();
                    Response<List<Factura>> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        List<Factura> facturas = response.body();
                        System.out.println("HISTORIAL DE FACTURAS:");
                        for (Factura f : facturas) {
                            System.out.println("ID Factura: " + f.getIdFactura() + ", Cliente: " + f.getNombreCliente() +
                                    ", Mascota: " + f.getNombreMascota() + ", Total: " + f.getTotal());
                        }
                    } else {
                        System.out.println("Error al obtener historial de facturas: " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Opción inválida en Facturación.");
        }
    }
}
