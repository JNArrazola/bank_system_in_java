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
                if(!validateDate(fechaAbono)) 
                    System.out.println("La fecha esta fuera del período actual");
                else break;
            } else {
                System.out.println("Fecha inválida");
            }
        } while (true);

        cuenta.añadirHistorial(new Movimiento("", abono, fechaAbono, "Abono"));
    }

    public static void retiroCredito(Credito cuenta){

    }

    private static boolean validateDate(Date fechaOperacion){
        Calendar calendarAbono = Calendar.getInstance();
        calendarAbono.setTime(fechaOperacion);
        Calendar fechaActual = Calendar.getInstance();
        fechaActual.setTime(actualDate);

        return ((fechaActual.get(Calendar.YEAR)==calendarAbono.get(Calendar.YEAR))&&
        (fechaActual.get(Calendar.MONTH)==calendarAbono.get(Calendar.MONTH)));
    }
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
}
