package rp3.auna.bean;

/**
 * Created by Jesus Villa on 20/11/2017.
 */

public class TarjetaValidar {
    private String Cut;
    private String Tarjeta;
    private String TipoTarjeta;
    private String TipoVenta;
    private String Usuario;

    //region Encapsulamiento
    public String getCut() {
        return Cut;
    }

    public void setCut(String cut) {
        Cut = cut;
    }

    public String getTarjeta() {
        return Tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        Tarjeta = tarjeta;
    }

    public String getTipoTarjeta() {
        return TipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        TipoTarjeta = tipoTarjeta;
    }

    public String getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        TipoVenta = tipoVenta;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    //endregion


    @Override
    public String toString() {
        return "TarjetaValidar{" +
                "Cut='" + Cut + '\'' +
                ", Tarjeta='" + Tarjeta + '\'' +
                ", TipoTarjeta='" + TipoTarjeta + '\'' +
                ", TipoVenta='" + TipoVenta + '\'' +
                ", Usuario='" + Usuario + '\'' +
                '}';
    }
}
