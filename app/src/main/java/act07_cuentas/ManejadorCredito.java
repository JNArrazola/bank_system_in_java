package act07_cuentas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class ManejadorCredito {
    private static final Scanner in = new Scanner(System.in);
    private static HashMap<String, ArrayList<Credito>> cuentasCredito = FileManagement.deserializarCuentasCredito();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private static Date actualDate = FileManagement.deserializeDate();

    // Obtains all the credit accounts of a client
    public static ArrayList<Credito> obtenerCuentas(String rfc) {
        if(cuentasCredito.containsKey(rfc)){
            return cuentasCredito.get(rfc);
        } else {
            return new ArrayList<Credito>();
        }
    }

    // Verify if there is a date registered in the system
    public static void verificarDate() throws ParseException {
        if(actualDate != null) return;
        
        String fechaStr;
        do {
            System.out.println("Ingrese la fecha actual del sistema: ");
            fechaStr = in.nextLine();
            if(ManejadorClientes.isValidDate(fechaStr)){
                actualDate = sdf.parse(fechaStr);
                break;
            } else {
                System.out.println("Fecha inválida");
            }
        } while (true);
    }

    /**
     * Function to register a new credit card in the system
     * @param rfc
      */
    public static void crearCuentaCredito(String rfc){
        System.out.println("Ingresa el límite de crédito de la tarjeta: ");
        double limiteCredito = Double.parseDouble(in.nextLine());

        double interesMensual;
        do {
            System.out.println("Ingresa el porcentaje de interés mensual [1-100]:");
            interesMensual = Double.parseDouble(in.nextLine());
            if(interesMensual<1||interesMensual>100){
                System.out.println("Porcentaje de interés mensual inválido");
            } else break;
        } while (true);
        interesMensual/=100;

        double porcentajeMinimo;
        do {
            System.out.println("Ingresa el porcentaje mínimo a pagar de la tarjeta [1-100]:");
            porcentajeMinimo = Double.parseDouble(in.nextLine());
            if(porcentajeMinimo<1||porcentajeMinimo>100){
                System.out.println("Porcentaje mínimo inválido");
            } else break;
        } while (true);
        porcentajeMinimo/=100;
    
        if(!cuentasCredito.containsKey(rfc))
            cuentasCredito.put(rfc, new ArrayList<Credito>());
        
        System.out.println("Cuenta creada con éxito\n");
        cuentasCredito.get(rfc).add(new Credito(rfc, limiteCredito, interesMensual, porcentajeMinimo));
        System.out.println("Su identificador de cuenta es: " + cuentasCredito.get(rfc).get(cuentasCredito.get(rfc).size()-1).getIdentificadorCuenta() + "\n");
    }

    /**
     * Function that looks for an specific account of a user
     * @param rfc
     * @param identificadorCuenta
     * @return
      */
    public static Credito obtenerCuentaEspecifica(String rfc, String identificadorCuenta){
        if(!cuentasCredito.containsKey(rfc))
            return null;
        
        ArrayList<Credito> tarjetas = obtenerCuentas(rfc);

        for(Credito d : tarjetas)
            if(d.getIdentificadorCuenta().equals(identificadorCuenta))
                return d;
        return null;
    }

    /**
     * Function that deletes an account from the hashmap
     * @param rfc
      */
    public static void cancelarCuenta(String rfc){
        System.out.println("Ingresa el identificador de la cuenta: ");
        String identificadorCuenta = in.nextLine();

        Credito cuenta = obtenerCuentaEspecifica(rfc, identificadorCuenta);

        if(cuenta == null){
            System.out.println("No se encontró la cuenta\n");
            return;
        }

        if(cuenta.getSaldo()!=0){
            System.out.println("OPERACIÓN FALLIDA: La cuenta tiene un saldo distinto de cero (saldo : $"+ cuenta.getSaldo() +" )");
            return;
        }

        cuentasCredito.get(rfc).remove(cuenta);
        System.out.println("OPERACIÓN ÉXITOSA: Cuenta eliminada con éxito");
    }
    

    /**
     * Boolean function that returns true if the array of accounts of a certain user is empty
     * The purpose of this function is being called by the ManejadorClientes class to verify if 
     * we can delete a client
     * 
     * REMINDER: We can remove a client if and only if he doesnt have any active account
     * @param rfc
     * @return boolean
      */
    public static boolean tieneCuentas(String rfc){
        return cuentasCredito.get(rfc).isEmpty();
    }
    
    // Function that verifies if an identifier is already in use
    public static boolean verificarIdentificador(String rfc, int num){
        if(!cuentasCredito.containsKey(rfc))
            return true;
        
        ArrayList<Credito> cuentas = obtenerCuentas(rfc);

        String possibleId = rfc + String.valueOf(num);
        for(Credito c : cuentas)
            if(c.getRfc().equals(possibleId))
                return false;
        return true;
    }

    public static void listarTarjetasCredito(String rfc){
        ArrayList<Credito> tarjetas = obtenerCuentas(rfc);

        if(tarjetas.isEmpty()){
            System.out.println("No hay tarjetas de crédito registradas para el usuario\n\n");
            return;
        }

        System.out.println("Tarjetas de crédito: ");
        for(Credito d : tarjetas)
            System.out.println(d.toString());
    }

    /**
     * Function to payments to a certain cc
     * @param cuenta
     * @throws ParseException
     */
    public static void abonoCredito(Credito cuenta) throws ParseException{
        if(cuenta.getCorteAPagar()!=null){
            System.out.println("Antes de abonar paga el saldo al corte.");
            return;
        }
        
        double saldo = cuenta.getSaldo();

        if(saldo == 0){
            System.out.println("La cuenta no tiene saldo pendiente");
            return;
        }

        mostrarBalance(cuenta);
        double abono;
        do {
            System.out.println("Ingresa la cantidad a abonar: ");
            abono = Double.parseDouble(in.nextLine());

            if(abono>saldo){
                System.out.println("No puedes abonar más que el saldo pendiente");
            } else if(abono <= 0){
                System.out.println("Cantidad inválida");
            } else break;
        } while (true);

        String fechaStr;
        Date fechaAbono;
        do {
            System.out.println("Ingresa la fecha de la operación (mm/dd/yyyy): ");
            fechaStr = in.nextLine();
            if(ManejadorClientes.isValidDate(fechaStr)){
                fechaAbono = sdf.parse(fechaStr);
                if(validateDate(fechaAbono, cuenta))break;
            } else {
                System.out.println("Fecha inválida");
            }
        } while (true);

        cuenta.setSaldo(saldo - abono);

        cuenta.añadirHistorial(new Movimiento("", abono, fechaAbono, "Abono"));
        System.out.println("=== OPERACIÓN ÉXITOSA === \n\n");
    }
    
    public static void imprimirCorteActual(Credito cuenta){
        if(cuenta.getCorteAPagar()==null){
            System.out.println("No hay ningún corte vigente");
            return;
        }
        
        System.out.println("El corte vigente es: ");
        System.out.println(cuenta.getCorteAPagar().imprimirCorte(cuenta.getPorcentajeMinimo()));
    }

    public static void imprimirHistorialCortes(Credito cuenta){
        ArrayList<Corte> cortes = cuenta.getCortes();

        if(cortes.isEmpty()){
            System.out.print("No hay cortes registrados\n");
            return;
        }

        Collections.sort(cortes, new Comparator<Corte>() {
        
            @Override
            public int compare(Corte o1, Corte o2) {
                return o1.getFechaCorte().compareTo(o2.getFechaCorte());
            }
        });

        for(Corte c : cortes){
            System.out.println(c.imprimirCorte(cuenta.getPorcentajeMinimo()));
        }
    }

    public static void retiroCredito(Credito cuenta) throws ParseException{
        double saldo = cuenta.getSaldo();
        
        if(saldo >= cuenta.getLimiteCredito()){
            System.out.println("Su saldo superó el crédito disponible");
            return;
        }

        System.out.println("\n=================================================");
        System.out.println("El crédito disponible es de: $" + (cuenta.getLimiteCredito() - saldo));
        System.out.println("=================================================\n\n");
        
        double retiro; 
        do {
            System.out.println("Ingresa la cantidad a retirar: ");
            retiro = Double.parseDouble(in.nextLine());
            
            if(retiro > (cuenta.getLimiteCredito() - saldo)){
                System.out.println("No cuentas con esa cantidad de crédito disponible");
            } else if(retiro <= 0){
                System.out.println("Cantidad inválida");
            } else break;
        } while (true);
        
        String fechaStr;
        Date fechaRetiro;
        do {
            System.out.println("Ingresa la fecha de la operación (mm/dd/yyyy): ");
            fechaStr = in.nextLine();
            if(ManejadorClientes.isValidDate(fechaStr)){
                fechaRetiro = sdf.parse(fechaStr);
                if(validateDate(fechaRetiro, cuenta))break;
            } else {
                System.out.println("Fecha inválida");
            }
        } while (true);
        
        cuenta.setSaldo(saldo + retiro);
        cuenta.añadirHistorial(new Movimiento("", retiro, fechaRetiro, "Retiro"));
        System.out.println("=== OPERACIÓN ÉXITOSA === \n\n");
    }
    
    /**
     * Check if a date is in the actual period, then, it verifies if there is no cut in it
     * @param fechaOperacion
     * @param cuenta
     * @return
      */
    private static boolean validateDate(Date fechaOperacion, Credito cuenta){
        Calendar calendarAbono = Calendar.getInstance();
        calendarAbono.setTime(fechaOperacion);
        Calendar fechaActual = Calendar.getInstance();
        fechaActual.setTime(actualDate);

        if((fechaActual.get(Calendar.YEAR)==calendarAbono.get(Calendar.YEAR))&&
        (fechaActual.get(Calendar.MONTH)==calendarAbono.get(Calendar.MONTH))){
            return true;
        } else {
            System.out.println("La fecha esta fuera del período actual");
            return false;
        }
    }

    /**
     * Function that prints the entire history of transactions of an account
     * @param cuenta
      */
    public static void imprimirHistorialGeneral(Credito cuenta){
        ArrayList<Movimiento> movimientos = cuenta.getHistorial();

        if(movimientos.isEmpty()){
            System.out.print("No hay movimientos registrados\n");
            return;
        }

        Collections.sort(movimientos, new Comparator<Movimiento>() {
        
            @Override
            public int compare(Movimiento o1, Movimiento o2) {
                return o1.getFecha().compareTo(o2.getFecha());
            }
        });

        for(Movimiento c : movimientos){
            System.out.println(c.toString());
        }
    }

    public static void imprimirDatosCuenta(Credito cuenta){
        System.out.println(cuenta.toString());
    }

    /**
     * Function that moves one month ahead, intended to help with the cc cut timeline
      */
    public static void moveMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(actualDate);

        if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER){
            calendar.add(Calendar.YEAR, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
        } else {
            calendar.add(Calendar.MONTH, 1);
        }
        actualDate = calendar.getTime();
    }

    public static void realizarCorte(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(actualDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDayOfMonth = calendar.getTime();
        
        for(ArrayList<Credito> cuentas : cuentasCredito.values()){
            for(Credito c : cuentas){
                if(c.getCorteAPagar()!=null){
                    c.getCorteAPagar().setEstado("NO SE PAGÓ");
                    c.setCorteAPagar(null);
                }
                
                if(c.getSaldo()==0){
                    c.añadirCorte(new Corte(lastDayOfMonth, 0, 0, "Pagado"));
                    continue;
                }

                Corte corte = new Corte(lastDayOfMonth, c.getSaldo() + c.getSaldo()*c.getInteresMensual(), 0, "NO PAGADO PERO VIGENTE");
                c.setCorteAPagar(corte);
                c.añadirCorte(corte);
                c.añadirHistorial(new Movimiento("Interes corte", c.getSaldo()*c.getInteresMensual(), lastDayOfMonth, "Interes"));
                c.setSaldo(c.getSaldo() + c.getSaldo()*c.getInteresMensual());
            }
        }
        moveMonth();
        System.out.println("===================================");
        System.out.println("=== Corte realizado con éxito ===");
        System.out.println("===================================");
    }

    public static void realizarPagoCorte(Credito cuenta) throws ParseException{
        if(cuenta.getCorteAPagar()==null){
            System.out.println("No hay cortes pendientes, si requiere abonar a la deuda puede seleccionar la opción 'abonar'\n\n");
            return;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String fechaFormateada = dateFormat.format(cuenta.getCorteAPagar().getFechaCorte());

        System.out.println("=== BIENVENIDO AL MENÚ DE CORTES ===");
        System.out.println("INFORMACIÓN: ");
        System.out.println("Pago para no generar interes: $" + cuenta.getSaldo());
        System.out.println("Pago mínimo: $" + cuenta.getSaldo() * cuenta.getPorcentajeMinimo());
        System.out.println("Fecha de corte: " + fechaFormateada);

        System.out.println("¿Desea pagar el corte?");
        System.out.println("1) Sí");
        System.out.println("2) No");
        int opt = Integer.parseInt(in.nextLine()); 

        if(opt==2){
            System.out.println("Saliendo...\n\n");
            return;
        }
    
        double cantidad;
        do {
            System.out.println("Ingresa la cantidad a abonar: ");
            cantidad = Double.parseDouble(in.nextLine());
            if(cantidad > cuenta.getSaldo()){
                System.out.println("No puedes pagar más del monto máximo");
            } else if(cantidad <= 0){
                System.out.println("Cantidad inválida");
            } else if(cantidad < cuenta.getSaldo()*cuenta.getPorcentajeMinimo()){
                System.out.println("No puedes depositar menos que la cantidad mínima");
            } else break;
        } while (true);

        System.out.println("Ingresa la fecha del pago: ");
        String fechaStr;
        Date fechaPago;
        do {
            System.out.println("Ingresa la fecha de la operación (mm/dd/yyyy): ");
            fechaStr = in.nextLine();
            if(ManejadorClientes.isValidDate(fechaStr)){
                fechaPago = sdf.parse(fechaStr);
                if(validateDate(fechaPago, cuenta))break;
            } else {
                System.out.println("Fecha inválida");
            }
        } while (true);

        cuenta.setSaldo(cuenta.getSaldo() - cantidad);
        cuenta.añadirHistorial(new Movimiento("PAGO CORTE", cantidad, fechaPago, "Pago de corte"));
        cuenta.getCorteAPagar().setPagoRealizado(cantidad);
        cuenta.getCorteAPagar().setEstado("Pagado");
        cuenta.setCorteAPagar(null);
    }

    public static boolean cancelarCuenta(Credito cuenta){
        if(cuenta.getSaldo()!=0){
            System.out.print("La cuenta aún tiene saldo, no se puede cancelar\n\n");
            return false;
        }
        
        System.out.println("\n¿Esta seguro que desea cancelar su cuenta?");
        System.out.println("1) Si");
        System.out.println("2) No");
        int opt = Integer.parseInt(in.nextLine());

        if(opt!=1){
            System.out.println("\nOperación cancelada...\n");
            return false;
        }

        ArrayList<Credito> cuentasUsuario = obtenerCuentas(cuenta.getRfc());
        cuentasUsuario.remove(cuenta);
        System.out.println("============================");
        System.out.println("Cuenta borrada éxitosamente");
        System.out.println("============================");
        return true;
    }

    public static void listarAñoMes(Credito cuenta) throws ParseException{
        ArrayList<Corte> listaCortes = cuenta.getCortes();
        if (listaCortes == null||listaCortes.isEmpty()) {
            System.out.println("La cuenta no tiene cortes registrados\n\n");
            return;
        }

        System.out.println("Ingresa el año para listar: ");
        int year = Integer.parseInt(in.nextLine());
        System.out.println("Ingresa el mes para listar: ");
        int month = Integer.parseInt(in.nextLine()) - 1;

        boolean flag = false;
        for (Corte m : listaCortes) {
            if (m.getYear() == year && m.getMonth() == month) {
                System.out.println(m.imprimirCorte(cuenta.getPorcentajeMinimo()));
                flag = true;
            }
        }

        if(!flag) System.out.println("No hay cortes ese mes\n");
    }

    public static void listarMovimientosAnioMes(Credito credito){


        // validar que la cuenta tenga movimientos
        ArrayList<Movimiento> movimientos = credito.getHistorial();
        if (movimientos == null||movimientos.isEmpty()) {
            System.out.println("La cuenta no tiene movimientos\n\n");
            return;
        }

        System.out.println("Ingresa el año para listar: ");
        int year = Integer.parseInt(in.nextLine());
        System.out.println("Ingresa el mes para listar: ");
        int month = Integer.parseInt(in.nextLine()) - 1;

        Collections.sort(movimientos, new Comparator<Movimiento>() {
            @Override
            public int compare(Movimiento o1, Movimiento o2) {
                return o1.getFecha().compareTo(o2.getFecha());
            }
        });

        boolean flag = false;
        // imprimir los movimientos
        for (Movimiento m : movimientos) {
            if (m.getYear() == year && m.getMonth() == month) {
                System.out.println(m.toString());
                flag = true;
            }
        }

        if(!flag) System.out.println("No se encontraron movimientos este mes\n");
    }
    // *******************************************************************
    // UTILITIES
    // *******************************************************************
    public static int getActualYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(actualDate);
        return calendar.get(Calendar.YEAR);
    }

    public static int getActualMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(actualDate);
        return calendar.get(Calendar.MONTH);
    }

    public static void cerrarCortes(){
        for(ArrayList<Credito> cuentas : cuentasCredito.values())
            for(Credito tarjeta : cuentas)
                if(tarjeta.getCorteAPagar().getEstado().equals("NO PAGADO PERO VIGENTE")){
                    tarjeta.getCorteAPagar().setEstado("NO SE PAGÓ");
                    tarjeta.setCorteAPagar(null);
                }
    }

    public static void save(){
        cerrarCortes();
        FileManagement.serializeDate(actualDate);
        FileManagement.serializarCuentasCredito(cuentasCredito);
    }

    public static String getActualDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(actualDate);

        return "Período actual: " + Movimiento.obtenerMes(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.YEAR);
    }

    public static void mostrarBalance(Credito credito){
        System.out.println("============================================================");
        System.out.println("El saldo actual de la cuenta es de: " + credito.getSaldo());
        System.out.println("============================================================");
    }
}
