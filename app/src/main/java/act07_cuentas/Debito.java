package act07_cuentas;

import java.io.Serializable;
import java.util.*;

public class Debito implements Serializable {
    private final String identificadorCuenta;
    private double saldo;
    private String rfc;
    private ArrayList<Movimiento> historial = new ArrayList<>();
    private final int numBound = 100;
    
    /**
     * Constructor
     */
    public Debito(double saldo, String rfc) {
        this.saldo = saldo;
        this.rfc = rfc;
        this.identificadorCuenta = generarRandom(rfc);
    }

    /**
     * Getters and Setters
     */
    public double getSaldo() {
        return this.saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getRfc() {
        return this.rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getIdentificadorCuenta() {
        return identificadorCuenta;
    }

    public ArrayList<Movimiento> getHistorial () {
        return historial;
    }

    /**
     * Métodos y funciones
      */
    public void imprimirHistorial(){
        Boolean flag = false;
        
        System.out.println("Historial de movimientos: ");
        
        
        for(Movimiento movimiento : historial){
            System.out.println(movimiento.toString());
            flag = true;
        }

        if(!flag) System.out.println("No hay movimientos registrados");
    }

    private String generarRandom(String rfc){
        Random r = new Random();
        int numero = r.nextInt(numBound);
        
        do {
            if(ManejadorDebito.verificarIdentificador(rfc, numero)) break;
            else numero = r.nextInt(numBound);
        } while (true);

        return rfc + numero;
    }
    
    public void agregarMovimiento(Movimiento m){
        historial.add(m);
    }

    /**
     * toString override
     */
    @Override
    public String toString() {
        return "Débito {" + 
            "\n\tIdentificador: " + identificadorCuenta + 
            "\n\tSaldo: " + saldo + 
            "\n\tRFC: " + rfc + 
        "\n}";
    }
}
