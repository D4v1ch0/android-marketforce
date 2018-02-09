package rp3.auna.bean;

/**
 * Created by Jesus Villa on 20/11/2017.
 */

public class SolicitudFisicaValidar {
    private int Cut;
    private String NumeroSolicitud;
    private int TipoVenta;
    private String Usuario;
    private AfiliadoValidar AfiliadoValidar;

    @Override
    public String toString() {
        return "SolicitudFisicaValidar{" +
                "Cut=" + Cut +
                ", NumeroSolicitud='" + NumeroSolicitud + '\'' +
                ", TipoVenta=" + TipoVenta +
                ", Usuario='" + Usuario + '\'' +
                ", AfiliadoValidar=" + AfiliadoValidar +
                '}';
    }

    //region Encapsulamiento

    public int getCut() {
        return Cut;
    }

    public void setCut(int cut) {
        Cut = cut;
    }

    public String getNumeroSolicitud() {
        return NumeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        NumeroSolicitud = numeroSolicitud;
    }

    public int getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(int tipoVenta) {
        TipoVenta = tipoVenta;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public rp3.auna.bean.AfiliadoValidar getAfiliadoValidar() {
        return AfiliadoValidar;
    }

    public void setAfiliadoValidar(rp3.auna.bean.AfiliadoValidar afiliadoValidar) {
        AfiliadoValidar = afiliadoValidar;
    }
    //endregion


}
