package rp3.auna.bean.virtual;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import rp3.auna.bean.AfiliadoMovil;
import rp3.auna.bean.Cotizacion;

/**
 * Created by Jesus Villa on 15/03/2018.
 */

public class CotizacionVirtual {
    @SerializedName("IdCotizacion")
    private int idCotizacion;
    @SerializedName("Agente")
    private String agente;
    @SerializedName("Programa")
    private String programa;
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
    @SerializedName("NombreProspecto")
    private String nombreProspecto;
    @SerializedName("TipoVenta")
    private String tipoVenta;
    @SerializedName("EmailAgente")
    private String emailAgente;
    @SerializedName("Afiliados")
    private List<AfiliadoMovil> afiliados;
    @SerializedName("Respuesta")
    private List<Cotizacion> respuesta;
    @SerializedName("FechaVenta")
    private String fechaVenta;
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

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
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

    public String getNombreProspecto() {
        return nombreProspecto;
    }

    public void setNombreProspecto(String nombreProspecto) {
        this.nombreProspecto = nombreProspecto;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public String getEmailAgente() {
        return emailAgente;
    }

    public void setEmailAgente(String emailAgente) {
        this.emailAgente = emailAgente;
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

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
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
        return "CotizacionVirtual{" +
                "idCotizacion=" + idCotizacion +
                ", agente='" + agente + '\'' +
                ", programa='" + programa + '\'' +
                ", idAgente=" + idAgente +
                ", idVisita=" + idVisita +
                ", email='" + email + '\'' +
                ", cantidadCuota='" + cantidadCuota + '\'' +
                ", precioCuota='" + precioCuota + '\'' +
                ", nombreProspecto='" + nombreProspecto + '\'' +
                ", tipoVenta='" + tipoVenta + '\'' +
                ", emailAgente='" + emailAgente + '\'' +
                ", afiliados=" + afiliados +
                ", respuesta=" + respuesta +
                ", fechaVenta='" + fechaVenta + '\'' +
                ", estado=" + estado +
                ", flag=" + flag +
                '}';
    }
}
