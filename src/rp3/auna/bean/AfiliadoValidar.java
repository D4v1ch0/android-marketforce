package rp3.auna.bean;

/**
 * Created by Jesus Villa on 20/11/2017.
 */

public class AfiliadoValidar {
    private String Cut;
    private String TipoDocumento;
    private String NumeroDocumento;
    private String Usuario;
    private TarjetaValidar Tarjeta;

    //region Encapsulamiento
    public String getCut() {
        return Cut;
    }

    public void setCut(String cut) {
        Cut = cut;
    }

    public String getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return NumeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        NumeroDocumento = numeroDocumento;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public TarjetaValidar getTarjeta() {
        return Tarjeta;
    }

    public void setTarjeta(TarjetaValidar tarjeta) {
        Tarjeta = tarjeta;
    }
    //endregion

    @Override
    public String toString() {
        return "AfiliadoValidar{" +
                "Cut='" + Cut + '\'' +
                ", TipoDocumento='" + TipoDocumento + '\'' +
                ", NumeroDocumento='" + NumeroDocumento + '\'' +
                ", Usuario='" + Usuario + '\'' +
                ", Tarjeta=" + Tarjeta +
                '}';
    }
}
