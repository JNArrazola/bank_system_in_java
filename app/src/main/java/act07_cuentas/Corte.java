package act07_cuentas;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Corte implements Serializable {
    private final Date fechaCorte;
    private final double cantidadTotal;
    private double pagoRealizado;
    private String estado;
    
    /**
     * @param fechaCorte
     * @param cantidadTotal
     * @param pagoRealizado
     * @param estado
      */
    Corte(Date fechaCorte, double cantidadTotal, double pagoRealizado, String estado){
        this.fechaCorte = fechaCorte;
        this.cantidadTotal = cantidadTotal;
        this.pagoRealizado = pagoRealizado;
        this.estado = estado;
    }

    public double getCantidadTotal() {
        return cantidadTotal;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getPagoRealizado() {
        return pagoRealizado;
    }

    public void setPagoRealizado(double pagoRealizado){
        this.pagoRealizado = pagoRealizado;
    }

    public int getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaCorte);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    public int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaCorte);
        return calendar.get(Calendar.MONTH);
    }
    
    public int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaCorte);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Función que imprime el corte, y si se pagó o no
     * @param porcentajeMinimo
     * @return
      */
    public String imprimirCorte(double porcentajeMinimo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = (fechaCorte != null) ? dateFormat.format(fechaCorte) : "N/A";
        
        return "Corte { " +
        "\n\tFecha corte: " + formattedDate + 
        "\n\tPago realizado: $" + pagoRealizado +
        "\n\tPago minimo: $" + cantidadTotal * porcentajeMinimo + 
        "\n\tPago para no generar intereses: $" + cantidadTotal + 
        "\n\tEstado: " + estado + 
        "\n}";
    }
}
