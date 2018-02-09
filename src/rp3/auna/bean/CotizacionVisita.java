package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jesus Villa on 28/12/2017.
 */

public class CotizacionVisita {
    @SerializedName("IdCotizacion")
    private int idCotizacion;
    @SerializedName("IdAgente")
    private int idAgente;
    @SerializedName("IdVisita")
    private int idVisita;
    @SerializedName("Email")
    private String email;
    @SerializedName("CantidadCuota")
    private String cantidadCuota;
    @SerializedName("PrecioCuota")
    private String precioCuota;
    @SerializedName("TipoVenta")
    private String tipoVenta;
    @SerializedName("FecIng")
    private long fecIng;
    @SerializedName("Afiliados")
    private List<AfiliadoMovil> afiliados;
    @SerializedName("Respuesta")
    private List<Cotizacion> respuesta;
    @SerializedName("Estado")
    private int estado;
    @SerializedName("Flag")
    private int flag;

    //region Encapsulamiento

    public int getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCantidadCuota() {
        return cantidadCuota;
    }

    public void setCantidadCuota(String cantidadCuota) {
        this.cantidadCuota = cantidadCuota;
    }

    public String getPrecioCuota() {
        return precioCuota;
    }

    public void setPrecioCuota(String precioCuota) {
        this.precioCuota = precioCuota;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public long getFecIng() {
        return fecIng;
    }

    public void setFecIng(long fecIng) {
        this.fecIng = fecIng;
    }

    public List<AfiliadoMovil> getAfiliados() {
        return afiliados;
    }

    public void setAfiliados(List<AfiliadoMovil> afiliados) {
        this.afiliados = afiliados;
    }

    public List<Cotizacion> getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(List<Cotizacion> respuesta) {
        this.respuesta = respuesta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    //endregion


    @Override
    public String toString() {
        return "CotizacionVisita{" +
                "idCotizacion=" + idCotizacion +
                ", idAgente=" + idAgente +
                ", idVisita=" + idVisita +
                ", email=" + email +
                ", cantidadCuota=" + cantidadCuota +
                ", precioCuota=" + precioCuota +
                ", tipoVenta=" + tipoVenta +
                ", fecIng=" + fecIng +
                ", afiliados=" + afiliados +
                ", respuesta=" + respuesta +
                ", estado=" + estado +
                ", flag=" + flag +
                '}';
    }
}
