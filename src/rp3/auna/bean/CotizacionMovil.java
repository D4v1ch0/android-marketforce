package rp3.auna.bean;

import java.util.List;

/**
 * Created by Jesus Villa on 24/10/2017.
 */

public class CotizacionMovil {
    private int IdCotizacion;
    private String Agente;
    private String Programa;
    private int IdAgente;
    private int IdVisita;
    private String Email;
    private String CantidadCuota;
    private String PrecioCuota;
    private String NombreProspecto;
    private String TipoVenta;
    private String EmailAgente;
    private List<AfiliadoMovil> Afiliados;
    private List<Cotizacion> Respuesta;
    private int Estado;
    private int Flag;

    @Override
    public String toString() {
        return "CotizacionMovil{" +
                "IdCotizacion=" + IdCotizacion +
                ", Agente='" + Agente + '\'' +
                ", Programa='" + Programa + '\'' +
                ", IdAgente=" + IdAgente +
                ", IdVisita=" + IdVisita +
                ", Email='" + Email + '\'' +
                ", CantidadCuota='" + CantidadCuota + '\'' +
                ", PrecioCuota='" + PrecioCuota + '\'' +
                ", NombreProspecto='" + NombreProspecto + '\'' +
                ", TipoVenta='" + TipoVenta + '\'' +
                ", Afiliados=" + Afiliados +
                ", Respuesta=" + Respuesta +
                ", Estado=" + Estado +
                ", Flag=" + Flag +
                '}';
    }


    //region Encapsulamiento
    public String getEmailAgente() {
        return EmailAgente;
    }

    public void setEmailAgente(String emailAgente) {
        EmailAgente = emailAgente;
    }
    public String getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        TipoVenta = tipoVenta;
    }
    public String getNombreProspecto() {
        return NombreProspecto;
    }

    public void setNombreProspecto(String nombreProspecto) {
        NombreProspecto = nombreProspecto;
    }
    public String getCantidadCuota() {
        return CantidadCuota;
    }

    public void setCantidadCuota(String cantidadCuota) {
        CantidadCuota = cantidadCuota;
    }

    public String getPrecioCuota() {
        return PrecioCuota;
    }

    public void setPrecioCuota(String precioCuota) {
        PrecioCuota = precioCuota;
    }

    public int getIdCotizacion() {
        return IdCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        IdCotizacion = idCotizacion;
    }

    public String getAgente() {
        return Agente;
    }

    public void setAgente(String agente) {
        Agente = agente;
    }

    public String getPrograma() {
        return Programa;
    }

    public void setPrograma(String programa) {
        Programa = programa;
    }

    public int getIdAgente() {
        return IdAgente;
    }

    public void setIdAgente(int idAgente) {
        IdAgente = idAgente;
    }

    public int getIdVisita() {
        return IdVisita;
    }

    public void setIdVisita(int idVisita) {
        IdVisita = idVisita;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public List<AfiliadoMovil> getAfiliados() {
        return Afiliados;
    }

    public void setAfiliados(List<AfiliadoMovil> afiliados) {
        Afiliados = afiliados;
    }

    public List<Cotizacion> getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(List<Cotizacion> respuesta) {
        Respuesta = respuesta;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }

    //endregion
}
