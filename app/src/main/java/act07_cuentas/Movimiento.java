package act07_cuentas;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Movimiento implements Serializable {
    private String concepto;
    private double monto;
    private Date fecha;
    private String tipo;

    private final Calendar calendar = Calendar.getInstance();

    /**
     * Constructor
     * 
     * @param concepto
     * @param monto
     * @param fecha
     * @param tipo
      */
    public Movimiento(String concepto, double monto, Date fecha, String tipo) {
        this.concepto = concepto;
        this.monto = monto;
        this.fecha = fecha;
        this.tipo = tipo;
        calendar.setTime(fecha);
    }

    /**
     * Getters y setters
      */
    public String getConcepto() {
        return this.concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getMonto() {
        return this.monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /**
     * Obtener fecha
      */
    public int getDay(){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth(){
        return calendar.get(Calendar.MONTH);
    }

    public int getYear(){
        return calendar.get(Calendar.YEAR);
    }
    
    public static String obtenerMes(int month){
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4: 
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7: 
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "Movimiento {" + 
            ((!getConcepto().equals("")) ? ("\n\tConcepto: '" + getConcepto() + "'") : "" )+
            "\n\tMonto: $" + getMonto() +
            "\n\tFecha: " + obtenerMes(getMonth()) + "-" + getDay() + "-" + getYear() +
            "\n\tTipo: " + getTipo() +
        "\n}";
    }
}
