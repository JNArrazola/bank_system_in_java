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
    private Corte corteAPagar = null;
    private ArrayList<Corte> cortes = new ArrayList<>();
    private final int numBound = 100;
    private double saldo = 0;


    Credito(String rfc, double limiteCredito, double interesMensual, double porcentajeMinimo) {
        this.rfc = rfc;
        this.limiteCredito = limiteCredito;
        this.interesMensual = interesMensual;
        this.porcentajeMinimo = porcentajeMinimo;
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

    public ArrayList<Corte> getCortes() {
        return cortes;
    }

    public Corte getCorteAPagar() {
        return corteAPagar;
    }

    // Setters
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setCorteAPagar(Corte corteAPagar) {
        this.corteAPagar = corteAPagar;
    }

    ArrayList<Movimiento> getHistorial(){
        return this.historial;
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

    @Override
    public String toString() {
        return "Crédito {" + 
        "\n\tIdentificador: " + identificadorCuenta + 
        "\n\tRFC: " + rfc + 
        "\n\tSaldo: $" + saldo + 
        "\n\tLímite de crédito: $" + limiteCredito + 
        "\n\tPorcentaje mínimo de pago: %" + porcentajeMinimo + 
        "\n\tPorcentaje de interés mensual: %" + interesMensual + 
        "\n\tCorte pendiente: " + ((corteAPagar!=null) ? "Si" : "No") +
        "\n}";
    }
}
