package act07_cuentas;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class ManejadorCredito {
    private static final Scanner in = new Scanner(System.in);
    private static HashMap<String, ArrayList<Credito>> cuentasCredito = FileManagement.deserializarCuentasCredito();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private static Date actualDate = FileManagement.deserializeDate();

    // Obtain all the credit accounts of a client
    public static ArrayList<Credito> obtenerCuentas(String rfc) {
        if(cuentasCredito.containsKey(rfc)){
            return cuentasCredito.get(rfc);
        } else {
            return null;
        }
    }

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
     * @param identificadorCuenta
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
    // TODO: clienteTieneCuentas
    
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

    // UTILITIES
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
}
