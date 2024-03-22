package act07_cuentas;

public class Menus {
    public static void menuPrincipal(){
        System.out.println("¿Qué deseas hacer?");
        System.out.println("1) Gestionar clientes");
        System.out.println("2) Gestionar cuentas");
        System.out.println("0) Salir");
    }

    public static void submenuGestionarClientes(){
        System.out.println("¿Qué deseas hacer?");
        System.out.println("1) Consultar clientes");
        System.out.println("2) Registrar cliente");
        System.out.println("3) Modificar cliente");
        System.out.println("4) Borrar un cliente");
        System.out.println("0) Salir");
    }

    public static void submenuListarClientes(){
        System.out.println("¿Por qué criterio desea listarlos?");
        System.out.println("1) RFC");
        System.out.println("2) Por nombre");
        System.out.println("3) Por apellido");
        System.out.println("4) Por nombre completo");
        System.out.println("5) Listar todos");
        System.out.println("0) Salir");
    }

    public static void menuModificaciones() {
        System.out.println("¿Qué deseas modificar?");
        System.out.println("1) Nombre");
        System.out.println("2) Apellido");
        System.out.println("3) Fecha de nacimiento");
        System.out.println("4) Email");
        System.out.println("0) Salir"); 
    }

    public static void menuGestionarCuentas(){
        System.out.println("¿Qué cuenta deseas gestionar?");
        System.out.println("1) Débito");
        System.out.println("2) Crédito");
        System.out.println("0) Salir");
    }

    public static void submenuGestionarDebito(){
        System.out.println("¿Qué deseas hacer?");
        System.out.println("1) Crear una nueva cuenta de débito");
        System.out.println("2) Registrar nuevo movimiento");
        System.out.println("3) Consultar movimientos");
        System.out.println("4) Cancelar cuenta");
        System.out.println("5) Mostrar cuentas");
        System.out.println("0) Salir");
    }

    public static void subsubmenuRegistrarNuevoMovimiento(){
        System.out.println("¿Qué movimiento desea hacer?");
        System.out.println("1) Depósito");
        System.out.println("2) Retiro");
        System.out.println("0) Salir");
    }

    public static void subsubMenuListarMovimientos(){
        System.out.println("¿Cómo deseas listar?");
        System.out.println("1) Por año-mes");
        System.out.println("2) Listar todos");
        System.out.println("0) Salir");
    }
}
