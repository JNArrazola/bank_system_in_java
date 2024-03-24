package act07_cuentas;

import java.io.Serializable;
import java.sql.Date;

public class Corte implements Serializable {
    private final Date fechaCorte;
    private final double cantidadTotal;
    private final double pagoRealizado;
    /**
     * La idea acá es que al momento de pagarse se registre el corte, y en 
     * 'estado' se coloque si se pago o se omitió el pago
     * @param fechaCorte
     * @param cantidadTotal
     * @param estado
     * 
     * @author Joshua
      */
    Corte(Date fechaCorte, double cantidadTotal, double pagoRealizado){
        this.fechaCorte = fechaCorte;
        this.cantidadTotal = cantidadTotal;
        this.pagoRealizado = pagoRealizado;
    }

    public double getCantidadTotal() {
        return cantidadTotal;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    /**
     * Función que imprime el corte, y si se pagó o no
     * @param porcentajeMinimo
     * @return
      */
    public String imprimirCorte(double porcentajeMinimo) {
        return "Corte { " +
        "\n\tFecha corte: " + fechaCorte + 
        "\n\tPago minimo: " + cantidadTotal * porcentajeMinimo + 
        "\n\tPago para no generar intereses: " + cantidadTotal + 
        "\n\tPago realizado" + pagoRealizado +
        "\n\tEstado: " + ((pagoRealizado != 0) ? "Pagado" : "No pagado") + 
        "}";
    }
}
