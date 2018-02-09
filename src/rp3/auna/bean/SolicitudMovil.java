package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jesus Villa on 08/11/2017.
 */

public class SolicitudMovil {
    @SerializedName("IdSolicitud")
    private Integer idSolicitud;
    private String NumeroSolicitud;
    private Integer IdPrograma;
    private String IdTipoSolicitud;
    private String IdGeneraSolicitud;
    private String ModoTarifa;
    private List<SolicitudAfiliadoMovil> Afiliados;

    //region Encapsulamiento
    public String getModoTarifa() {
        return ModoTarifa;
    }

    public void setModoTarifa(String modoTarifa) {
        ModoTarifa = modoTarifa;
    }
    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        idSolicitud = idSolicitud;
    }

    public String getNumeroSolicitud() {
        return NumeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        NumeroSolicitud = numeroSolicitud;
    }

    public Integer getIdPrograma() {
        return IdPrograma;
    }

    public void setIdPrograma(Integer idPrograma) {
        IdPrograma = idPrograma;
    }

    public String getIdTipoSolicitud() {
        return IdTipoSolicitud;
    }

    public void setIdTipoSolicitud(String idTipoSolicitud) {
        IdTipoSolicitud = idTipoSolicitud;
    }

    public String getIdGeneraSolicitud() {
        return IdGeneraSolicitud;
    }

    public void setIdGeneraSolicitud(String idGeneraSolicitud) {
        IdGeneraSolicitud = idGeneraSolicitud;
    }

    public List<SolicitudAfiliadoMovil> getAfiliados() {
        return Afiliados;
    }

    public void setAfiliados(List<SolicitudAfiliadoMovil> afiliados) {
        Afiliados = afiliados;
    }
    //endregion

    @Override
    public String toString() {
        return "SolicitudMovil{" +
                "idSolicitud=" + idSolicitud +
                ", NumeroSolicitud='" + NumeroSolicitud + '\'' +
                ", IdPrograma=" + IdPrograma +
                ", IdTipoSolicitud='" + IdTipoSolicitud + '\'' +
                ", IdGeneraSolicitud='" + IdGeneraSolicitud + '\'' +
                ", ModoTarifa='" + ModoTarifa + '\'' +
                ", Afiliados=" + Afiliados +
                '}';
    }
}
