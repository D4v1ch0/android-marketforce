package rp3.auna.bean;

/**
 * Created by Jesus Villa on 25/10/2017.
 */

public class Cotizacion {
    private String IdOperacion;
    private String IdRegistro;
    private String Result;
    private String Anual;
    private String TC;
    private String TD;
    private String Info;

    //region Encapsulamiento

    public String getIdOperacion() {
        return IdOperacion;
    }

    public void setIdOperacion(String idOperacion) {
        IdOperacion = idOperacion;
    }

    public String getIdRegistro() {
        return IdRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        IdRegistro = idRegistro;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getAnual() {
        return Anual;
    }

    public void setAnual(String anual) {
        Anual = anual;
    }

    public String getTC() {
        return TC;
    }

    public void setTC(String TC) {
        this.TC = TC;
    }

    public String getTD() {
        return TD;
    }

    public void setTD(String TD) {
        this.TD = TD;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    //endregion

    @Override
    public String toString() {
        return "Cotizacion{" +
                "IdOperacion='" + IdOperacion + '\'' +
                ", IdRegistro='" + IdRegistro + '\'' +
                ", Result='" + Result + '\'' +
                ", Anual='" + Anual + '\'' +
                ", TC='" + TC + '\'' +
                ", TD='" + TD + '\'' +
                ", Info='" + Info + '\'' +
                '}';
    }
}
