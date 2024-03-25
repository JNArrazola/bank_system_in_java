package act07_cuentas;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

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
            return null;
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
        
        cuentasCredito.get(rfc).add(new Credito(rfc, limiteCredito, interesMensual, porcentajeMinimo));
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
        double saldo = cuenta.getSaldo();

        if(saldo == 0){
            System.out.println("La cuenta no tiene saldo pendiente");
            return;
        }

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
            if(!(cuenta.getEsCorte())){
                return true;
            } else {
                System.out.println("Ya se hizo un corte en esta fecha");
                return false;
            }
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

    /**
     * Function to list the movement on the cc by year and month
     * @return void
     */
    public static void listarPorMesYAnio(Credito cuenta) {
        if (cuenta.getCortes().isEmpty()) {
            System.out.println("No hay cortes registrados");
            return;
        }

        System.out.println("Ingrese el año");
        int anio = Integer.parseInt(in.nextLine());
        System.out.println("Ingrese el mes");
        int mes = Integer.parseInt(in.nextLine());

        // sort the cuts by date
        Collections.sort(cuenta.getCortes(), new Comparator<Corte>() {
            @Override
            public int compare(Corte o1, Corte o2) {
                return o1.getFechaCorte().compareTo(o2.getFechaCorte());
            }
        });

        for (Corte c : cuenta.getCortes()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(c.getFechaCorte());
            if (calendar.get(Calendar.YEAR) == anio && calendar.get(Calendar.MONTH) == mes) {
                System.out.println(c.imprimirCorte(cuenta.getPorcentajeMinimo()));
            }
        }
    }

    /**
     * Function to do the payment of the cc
     * @return
     * @throws ParseException 
     */
    public static void realizarPago (Credito cuenta) throws ParseException {
        double saldo = cuenta.getSaldo();
        double porcentajeMinimo = cuenta.getPorcentajeMinimo();
        double cantidadTotal = saldo + saldo * cuenta.getInteresMensual();
        double pagoRealizado = 0;

        if (saldo == 0) {
            System.out.println("La cuenta no tiene saldo pendiente");
            return;
        }

        System.out.println("El saldo pendiente es de: $" + saldo);
        System.out.println("El pago mínimo es de: $" + cantidadTotal * porcentajeMinimo);
        System.out.println("El pago para no generar intereses es de: $" + cantidadTotal);

        double pago;
        do {
            System.out.println("Ingresa la cantidad a pagar: ");
            pago = Double.parseDouble(in.nextLine());

            if (pago > saldo) {
                System.out.println("No puedes pagar más que el saldo pendiente");
            } else if (pago <= 0) {
                System.out.println("Cantidad inválida");
            } else {
                pagoRealizado = pago;
                break;
            }
        } while (true);

        String fechaStr;
        Date fechaPago;
        do {
            System.out.println("Ingresa la fecha de la operación (mm/dd/yyyy): ");
            fechaStr = in.nextLine();
            if (ManejadorClientes.isValidDate(fechaStr)) {
                fechaPago = sdf.parse(fechaStr);
                if (validateDate(fechaPago, cuenta)) break;
            } else {
                System.out.println("Fecha inválida");
            }
        } while (true);

        cuenta.setSaldo(saldo - pago);
        cuenta.añadirHistorial(new Movimiento(
            "Pago de Credito", pago, fechaPago, "Pago"));
        cuenta.añadirCorte(new Corte(fechaPago, cantidadTotal, pagoRealizado));
        System.out.println("=== OPERACIÓN ÉXITOSA === \n\n");
    }

    /**
     * Function to print the cc cuts
     * @return
     */
    public static void consultarCorte(Credito cuenta) {
        if (cuenta.getCortes().isEmpty()) {
            System.out.println("No hay cortes registrados");
            return;
        }

        for (Corte c : cuenta.getCortes()) {
            System.out.println(c.imprimirCorte(cuenta.getPorcentajeMinimo()));
        }
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

    public static void save(){
        FileManagement.serializeDate(actualDate);
        FileManagement.serializarCuentasCredito(cuentasCredito);
    }

    public static String getActualDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(actualDate);

        return "Período actual: " + Movimiento.obtenerMes(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.YEAR);
    }

    /**
     * Function to do a cut of the cc
     */
    public static void realizarCorte() {
        /* 
        1. se va a avanzar un mes
        2. se van a recorrer todas las tarjetas de crédito registradas 
            y se va a colocar el booleano esCorte como true
        3. si al momento de recorrer las tarjetas de crédito, hay una tarjeta 
            cuyo bool esCorte ya era true, eso quiere decir que no se pago el 
            mes anterior, porque al momento de pagar el booleano esCorte va a 
            volver a ser false indicando que el usuario ya pagó y no puede 
            volver a hacer un nuevo pago del corte.
            si esCorte es true quiere decir entonces que el usuario no pagó el 
            anterior período, y va a tocar registrar la operación y agregar los 
            respectivos intereses de ese mes al saldo
         */
        // 1
        moveMonth();

        // 2
        for (ArrayList<Credito> tarjetas : cuentasCredito.values()) {
            // 3
            for (Credito c : tarjetas) {
                if (c.getEsCorte()) {
                    double saldo = c.getSaldo();
                    double interes = saldo * c.getInteresMensual();
                    c.setSaldo(saldo + interes);
                    c.añadirHistorial(new Movimiento("Intereses", interes, actualDate, "Intereses"));
                }
                c.setEsCorte(true);
            }
        }

    }
}
