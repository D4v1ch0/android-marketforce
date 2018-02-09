package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jesus Villa on 08/11/2017.
 */

public class VisitaVta {
    public int IdVisita ;
    public String Descripcion ;
    public String Observacion ;
    public long FechaVisita ;
    public long FechaInicio ;
    public long FechaFin ;
    public String IdClienteDireccion ;
    public int IdCliente ;
    public int IdAgente ;
    public float Latitud ;
    public float Longitud ;
    public int MotivoReprogramacionTabla ;
    public String MotivoReprogramacionValue ;
    public int MotivoVisitaTabla ;
    public String MotivoVisitaValue ;
    public int VisitaTabla ;
    public String VisitaValue ;
    public int ReferidoTabla ;
    public String ReferidoValue ;
    public String TiempoCode ;
    public String DuracionCode;
    public String TipoVenta ;
    public int ReferidoCount ;
    public int VisitaId;
    public int LlamadaId;
    public int Estado;
    public List<FotoVisita> Fotos ;

    @Override
    public String toString() {
        return "VisitaVta{" +
                ", IdVisita=" + IdVisita +
                ", Descripcion='" + Descripcion + '\'' +
                ", Observacion='" + Observacion + '\'' +
                ", FechaVisita=" + FechaVisita +
                ", FechaInicio=" + FechaInicio +
                ", FechaFin=" + FechaFin +
                ", IdClienteDireccion='" + IdClienteDireccion + '\'' +
                ", IdCliente=" + IdCliente +
                ", IdAgente=" + IdAgente +
                ", Latitud=" + Latitud +
                ", Longitud=" + Longitud +
                ", MotivoReprogramacionTabla=" + MotivoReprogramacionTabla +
                ", MotivoReprogramacionValue='" + MotivoReprogramacionValue + '\'' +
                ", MotivoVisitaTabla=" + MotivoVisitaTabla +
                ", MotivoVisitaValue='" + MotivoVisitaValue + '\'' +
                ", VisitaTabla=" + VisitaTabla +
                ", VisitaValue='" + VisitaValue + '\'' +
                ", ReferidoTabla=" + ReferidoTabla +
                ", ReferidoValue='" + ReferidoValue + '\'' +
                ", TiempoCode='" + TiempoCode + '\'' +
                ", DuracionCode='" + DuracionCode + '\'' +
                ", TipoVenta='" + TipoVenta + '\'' +
                ", ReferidoCount=" + ReferidoCount +
                ", VisitaId=" + VisitaId +
                ", LlamadaId=" + LlamadaId +
                ", Estado=" + Estado +
                ", Fotos=" + Fotos +
                '}';
    }

    //region Encapsulamiento
    public int getVisitaId() {
        return VisitaId;
    }

    public void setVisitaId(int visitaId) {
        VisitaId = visitaId;
    }

    public int getLlamadaId() {
        return LlamadaId;
    }

    public void setLlamadaId(int llamadaId) {
        LlamadaId = llamadaId;
    }

    public int getIdVisita() {
        return IdVisita;
    }

    public void setIdVisita(int idVisita) {
        IdVisita = idVisita;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public long getFechaVisita() {
        return FechaVisita;
    }

    public void setFechaVisita(long fechaVisita) {
        FechaVisita = fechaVisita;
    }

    public long getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(long fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public long getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(long fechaFin) {
        FechaFin = fechaFin;
    }

    public String getIdClienteDireccion() {
        return IdClienteDireccion;
    }

    public void setIdClienteDireccion(String idClienteDireccion) {
        IdClienteDireccion = idClienteDireccion;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public int getIdAgente() {
        return IdAgente;
    }

    public void setIdAgente(int idAgente) {
        IdAgente = idAgente;
    }

    public float getLatitud() {
        return Latitud;
    }

    public void setLatitud(float latitud) {
        Latitud = latitud;
    }

    public float getLongitud() {
        return Longitud;
    }

    public void setLongitud(float longitud) {
        Longitud = longitud;
    }

    public int getMotivoReprogramacionTabla() {
        return MotivoReprogramacionTabla;
    }

    public void setMotivoReprogramacionTabla(int motivoReprogramacionTabla) {
        MotivoReprogramacionTabla = motivoReprogramacionTabla;
    }

    public String getMotivoReprogramacionValue() {
        return MotivoReprogramacionValue;
    }

    public void setMotivoReprogramacionValue(String motivoReprogramacionValue) {
        MotivoReprogramacionValue = motivoReprogramacionValue;
    }

    public int getMotivoVisitaTabla() {
        return MotivoVisitaTabla;
    }

    public void setMotivoVisitaTabla(int motivoVisitaTabla) {
        MotivoVisitaTabla = motivoVisitaTabla;
    }

    public String getMotivoVisitaValue() {
        return MotivoVisitaValue;
    }

    public void setMotivoVisitaValue(String motivoVisitaValue) {
        MotivoVisitaValue = motivoVisitaValue;
    }

    public int getVisitaTabla() {
        return VisitaTabla;
    }

    public void setVisitaTabla(int visitaTabla) {
        VisitaTabla = visitaTabla;
    }

    public String getVisitaValue() {
        return VisitaValue;
    }

    public void setVisitaValue(String visitaValue) {
        VisitaValue = visitaValue;
    }

    public int getReferidoTabla() {
        return ReferidoTabla;
    }

    public void setReferidoTabla(int referidoTabla) {
        ReferidoTabla = referidoTabla;
    }

    public String getReferidoValue() {
        return ReferidoValue;
    }

    public void setReferidoValue(String referidoValue) {
        ReferidoValue = referidoValue;
    }

    public String getTiempoCode() {
        return TiempoCode;
    }

    public void setTiempoCode(String tiempoCode) {
        TiempoCode = tiempoCode;
    }

    public String getDuracionCode() {
        return DuracionCode;
    }

    public void setDuracionCode(String duracionCode) {
        DuracionCode = duracionCode;
    }

    public String getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        TipoVenta = tipoVenta;
    }

    public int getReferidoCount() {
        return ReferidoCount;
    }

    public void setReferidoCount(int referidoCount) {
        ReferidoCount = referidoCount;
    }

    public List<FotoVisita> getFotos() {
        return Fotos;
    }

    public void setFotos(List<FotoVisita> fotos) {
        Fotos = fotos;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }
    //endregion

}
