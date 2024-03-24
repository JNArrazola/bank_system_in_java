package act07_cuentas;

import java.io.Serializable;
import java.sql.Date;

public class Corte implements Serializable {
    private final Date fechaCorte;
    private final double cantidadTotal;
    private final String estado;

    // TODO: pago minimo es calculado

    /**
     * La idea acá es que al momento de pagarse se registre el corte, y en 
     * 'estado' se coloque si se pago o se omitió el pago
     * @param fechaCorte
     * @param cantidadTotal
     * @param estado
     * 
     * @author Joshua
      */
    Corte(Date fechaCorte, double cantidadTotal, String estado){
        this.fechaCorte = fechaCorte;
        this.cantidadTotal = cantidadTotal;
        this.estado = estado;
    }

    public double getCantidadTotal() {
        return cantidadTotal;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public String getEstado() {
        return estado;
    }
}
