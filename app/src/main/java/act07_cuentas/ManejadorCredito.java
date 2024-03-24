package act07_cuentas;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class ManejadorCredito {
    private final Scanner in = new Scanner(System.in);
    private static HashMap<String, ArrayList<Credito>> cuentasCredito = FileManagement.deserializarCuentasCredito();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    // Obtain all the credit accounts of a client
    public static ArrayList<Credito> obtenerCuentas(String rfc) {
        if(cuentasCredito.containsKey(rfc)){
            return cuentasCredito.get(rfc);
        } else {
            return null;
        }
    }

    // TODO: Agregar cuenta
    // TODO: Cancelar cuenta
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

    
}
