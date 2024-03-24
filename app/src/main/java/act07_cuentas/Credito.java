package act07_cuentas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Credito implements Serializable {
    private final String rfc;
    private final String identificadorCuenta;
    private final double limiteCredito;
    private final double interesMensual;
    private final double porcentajeMinimo;
    private ArrayList<Movimiento> historial = new ArrayList<>();
    private ArrayList<Corte> cortes = new ArrayList<>();
    private final int numBound = 100;
    private double saldo;

    Credito(String rfc, double limiteCredito, double interesMensual, double porcentajeMinimo, double saldo) {
        this.rfc = rfc;
        this.limiteCredito = limiteCredito;
        this.interesMensual = interesMensual;
        this.porcentajeMinimo = porcentajeMinimo;
        this.saldo = saldo;
        this.identificadorCuenta = generarRandom(rfc);
    }

    public double getInteresMensual() {
        return interesMensual;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public double getPorcentajeMinimo() {
        return porcentajeMinimo;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getRfc() {
        return rfc;
    }

    public String getIdentificadorCuenta() {
        return identificadorCuenta;
    }

    // Setters
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // Functions
    private String generarRandom(String rfc){
        Random r = new Random();
        int numero = r.nextInt(numBound);
        
        do {
            if(ManejadorCredito.verificarIdentificador(rfc, numero)) break;
            else numero = r.nextInt(numBound);
        } while (true);

        return rfc + numero;
    }

    public void añadirHistorial(Movimiento m){
        historial.add(m);   
    }

    public void añadirCorte(Corte c){
        cortes.add(c);
    }
}
