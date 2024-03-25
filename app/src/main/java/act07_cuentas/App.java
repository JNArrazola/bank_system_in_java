package act07_cuentas;

import java.text.ParseException;
import java.util.*;

public class App {
    private final Scanner in = new Scanner(System.in);
    // test in branch
    // test 2
    public static void main(String[] args) throws ParseException {
        App app = new App();
        try {
            app.run();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error en la aplicación...");
            System.out.println(e.getMessage());
        }
    }

    void run() throws Exception{
        FileManagement.verificacionInicial();
        ManejadorCredito.verificarDate();

        System.out.println("=== Bienvenido al sistema bancario ===");
        int opt;
        do {
            Menus.menuPrincipal();
            opt = Integer.parseInt(in.nextLine());
            switch (opt) {
                case 1: // Gestionar clientes
                    gestionarClientes();
                    break;
                case 2: // Gestionar cuentas
                    gestionarCuentas();
                    break;
                case 3: // Realizar corte de tarjetas de crédito
                    
                    break;
                default:
                    break;
            }
        } while (opt!=0);

        ManejadorClientes.save();
        ManejadorDebito.save();
        ManejadorCredito.save();
    }

    void gestionarClientes() throws ParseException{
        int opt; 

        do {
            Menus.submenuGestionarClientes();
            opt = Integer.parseInt(in.nextLine());
            switch (opt) {
                case 1: // Consultar clientes
                    listarClientes();
                    break;
                case 2: // Registrar cliente
                    ManejadorClientes.crearCliente();
                    break;
                case 3: // Modificar cliente
                    ManejadorClientes.modificarCliente();
                    break;
                case 4: // Borrar cliente
                    ManejadorClientes.borrarCliente();
                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }

    void listarClientes(){
        int opt;
        do {
            Menus.submenuListarClientes();
            opt = Integer.parseInt(in.nextLine());
            switch (opt) {
                case 1: // RFC
                    System.out.println("Ingresa el RFC del usuario a buscar: ");
                    String rfc = in.nextLine();
                    ManejadorClientes.listarPorRfc(rfc);
                    break;
                case 2: // Nombre
                    System.out.println("Ingresa el nombre del usuario a buscar: ");
                    String nombre = in.nextLine();
                    ManejadorClientes.listarNombre(nombre);
                    break;
                case 3: // Apellido
                    System.out.println("Ingresa el apellido del usuario a buscar: ");
                    String apellido = in.nextLine();
                    ManejadorClientes.listarApellido(apellido);
                    break;
                case 4:
                    System.out.println("Ingresa el nombre completo: ");
                    String nombreCompleto = in.nextLine();
                    ManejadorClientes.listarNombreCompleto(nombreCompleto);
                    break;
                case 5:
                    ManejadorClientes.listarClientes();
                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }

    void gestionarCuentas() throws Exception {
        System.out.println("Ingrese el RFC: ");
        String rfc = in.nextLine();
        Cliente c = ManejadorClientes.buscarCliente(rfc);

        if(c==null){
            System.out.println("El cliente no fue encontrado\n\n");
            return;
        }
        
        int opt;
        do {
            Menus.menuGestionarCuentas();
            opt = Integer.parseInt(in.nextLine());
            switch (opt) {
                case 1: // Gestionar cuentas débito
                    gestionarCuentasDebito(rfc);
                    break;
                case 2: // Gestionar cuentas crédito
                    gestionarCuentasCredito(rfc);
                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }

    void gestionarCuentasDebito(String rfc) throws Exception {
        int opt;
        // limpiar pantalla
        // System.out.println("\033[H\033[2J");
        System.out.println("=======================================");
        System.out.println("Usted ingreso al usuario: " + rfc);
        System.out.println("=======================================");
        do {
            Menus.submenuGestionarDebito();
            opt = Integer.parseInt(in.nextLine());

            switch (opt) {
                case 1: // Crear nueva cuenta
                    ManejadorDebito.crearCuenta(rfc);
                    break;
                case 2: // Registrar nuevo movimiento
                    registrarNuevoMovimiento(rfc);
                    break;
                case 3: // Listar movimiento
                    listarMovimientos(rfc);
                    break;
                case 4: // Cancelar cuenta
                    System.out.println("Ingresa el identificador de la cuenta: ");
                    String identificador = in.nextLine();
                    
                    ManejadorDebito.eliminarCuenta(ManejadorDebito.buscarCuentaEspecifica(rfc, identificador));
                    break;
                case 5: // Mostrar cuentas
                    ManejadorDebito.mostrarCuentasRegistradas(rfc);
                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }

    void registrarNuevoMovimiento(String rfc) throws Exception{
        System.out.println("Ingresa el identificador de la cuenta: ");
        String id = in.nextLine();

        Debito cuenta = ManejadorDebito.buscarCuentaEspecifica(rfc, id);
        if(cuenta == null){
            System.out.println("No se encontró la cuenta\n");
            return;
        }

        int opt;
        do {
            Menus.subsubmenuRegistrarNuevoMovimiento();
            opt = Integer.parseInt(in.nextLine());

            switch (opt) {
                case 1: // depósito
                    ManejadorDebito.realizarDeposito(cuenta);
                    break;
                case 2: // retiro
                    ManejadorDebito.realizarRetiro(cuenta);
                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }

    /**
     * Menus para listar movimientos
      */
    void listarMovimientos(String rfc){
        int opt;
        
        System.out.println("Ingresa el identificador de la cuenta: ");
        String identificador = in.nextLine();

        Debito cuenta = ManejadorDebito.buscarCuentaEspecifica(rfc, identificador);

        if(cuenta == null){
            System.out.println("No se encontró la cuenta a listar");
            return;
        }

        do {
            Menus.subsubMenuListarMovimientos();
            opt = Integer.parseInt(in.nextLine());
            switch (opt) {
                case 1: // Listar por año-mes
                    ManejadorDebito.consultarMovimientosPorAnioMes(cuenta);
                    break;
                case 2: // Listar en general
                    ManejadorDebito.listarGeneral(cuenta);
                default:
                    break;
            }
        } while (opt!=0);
    }
    
    void gestionarCuentasCredito(String rfc) throws ParseException{
        int opt;
        
        System.out.println("=======================================");
        System.out.println("Usted ingreso al usuario: " + rfc);
        System.out.println("=======================================");

        do {
            Menus.subMenuGestionarCuentasCredito();
            opt = Integer.parseInt(in.nextLine());
            switch (opt) {
                case 1: // Crear cuenta de crédito
                    ManejadorCredito.crearCuentaCredito(rfc);
                    break;
                case 2: // Ingresar a una cuenta
                    System.out.println("Ingresa el identificador de la cuenta a ingresar: ");
                    String id = in.nextLine();
                    ingresarACuenta(rfc, id);
                    break;
                case 3: // Listar cuentas de crédito
                    ManejadorCredito.listarTarjetasCredito(rfc);
                    break;
                case 4: // Cancelar cuenta de crédito
                    ManejadorCredito.cancelarCuenta(rfc);
                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }

    void ingresarACuenta(String rfc, String identificador) throws ParseException{
        Credito cuenta = ManejadorCredito.obtenerCuentaEspecifica(rfc, identificador);
        if(cuenta == null){
            System.out.println("OPERACIÓN FALLIDA: No se encontró la cuenta\n");
            return;
        }

        System.out.println("===================================");
        System.out.println("¡Bienvenido al sistema!");
        System.out.println(ManejadorCredito.getActualDate());
        System.out.println("===================================");
        
        int opt;
        do {
            Menus.subsubMenuIngresarCuenta();
            opt = Integer.parseInt(in.nextLine());

            switch (opt) {
                case 1: // Registrar movimiento
                    registrarMovimientoCredito(cuenta);
                    break;
                case 2: // Consultar movimientos 
                    consultarMovimientosCredito(cuenta);
                    break;
                case 3: // Consultar corte

                    break;
                case 4: // Consultar detalles del corte

                    break;
                case 5: // Realizar pago

                    break;
                case 6: // imprimir datos de cuenta
                    ManejadorCredito.imprimirDatosCuenta(cuenta);
                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }

    void registrarMovimientoCredito(Credito cuenta) throws ParseException{
        int opt;
        do {

            Menus.subsubsubMenuRegistrarMovimientoCredito();
            opt = Integer.parseInt(in.nextLine());
            switch (opt) {
                case 1: // Abonar
                    ManejadorCredito.abonoCredito(cuenta);
                    break;
                case 2: // Retirar
                    ManejadorCredito.retiroCredito(cuenta);
                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }

    void consultarMovimientosCredito(Credito cuenta){
        int opt;

        do {
            Menus.subsubsubMenuListar();
            opt = Integer.parseInt(in.nextLine());

            switch (opt) {
                case 1: // Listar general/todos
                    ManejadorCredito.imprimirHistorialGeneral(cuenta);
                    break;
                case 2: // Listar por año-mes

                    break;
                default:
                    break;
            }
        } while (opt!=0);
    }
}
