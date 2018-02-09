package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jesus Villa on 27/12/2017.
 */

public class VisitaVtaDetalle {
    @SerializedName("IdVisita")
    private int idVisita;
    @SerializedName("Descripcion")
    private String descripcion;
    @SerializedName("Observacion")
    private String observacion;
    @SerializedName("FechaVisita")
    private long fechaVisita;
    @SerializedName("FechaInicio")
    private long fechaInicio;
    @SerializedName("FechaFin")
    private long fechaFin;
    @SerializedName("IdClienteDireccion")
    private String idClienteDireccion;
    @SerializedName("IdCliente")
    private int idCliente;
    @SerializedName("IdAgente")
    private int idAgente;
    @SerializedName("Latitud")
    private float latitud;
    @SerializedName("Longitud")
    private float longitud;
    @SerializedName("MotivoReprogramacionTabla")
    private int motivoReprogramacionTabla;
    @SerializedName("MotivoReprogramacionValue")
    private String motivoReprogramacionValue;
    @SerializedName("MotivoVisitaTabla")
    private int motivoVisitaTabla;
    @SerializedName("MotivoVisitaValue")
    private String motivoVisitaValue;
    @SerializedName("VisitaTabla")
    private int visitaTabla;
    @SerializedName("VisitaValue")
    private String visitaValue;
    @SerializedName("ReferidoTabla")
    private int referidoTabla;
    @SerializedName("ReferidoValue")
    private String referidoValue;
    @SerializedName("TiempoCode")
    private String tiempoCode;
    @SerializedName("DuracionCode")
    private String duracionCode;
    @SerializedName("TipoVenta")
    private String tipoVenta;
    @SerializedName("ReferidoCount")
    private int referidoCount;
    @SerializedName("Estado")
    private int estado;
    @SerializedName("VisitaId")
    private int visitaId;
    @SerializedName("LlamadaId")
    private int llamadaId;
    @SerializedName("Fotos")
    private List<FotoVisita> fotos;
    @SerializedName("Cotizacion")
    private List<CotizacionVisita> cotizacion;
    @SerializedName("Solicitud")
    private SolicitudMovil solicitud;
    @SerializedName("Pago")
    private RegistroPago pago;
    @SerializedName("Prospecto")
    private ProspectoVta prospectoVta;

    //region Encapsulamiento


    public ProspectoVta getProspectoVta() {
        return prospectoVta;
    }

    public void setProspectoVta(ProspectoVta prospectoVta) {
        this.prospectoVta = prospectoVta;
    }

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public long getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(long fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public long getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(long fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIdClienteDireccion() {
        return idClienteDireccion;
    }

    public void setIdClienteDireccion(String idClienteDireccion) {
        this.idClienteDireccion = idClienteDireccion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public int getMotivoReprogramacionTabla() {
        return motivoReprogramacionTabla;
    }

    public void setMotivoReprogramacionTabla(int motivoReprogramacionTabla) {
        this.motivoReprogramacionTabla = motivoReprogramacionTabla;
    }

    public String getMotivoReprogramacionValue() {
        return motivoReprogramacionValue;
    }

    public void setMotivoReprogramacionValue(String motivoReprogramacionValue) {
        this.motivoReprogramacionValue = motivoReprogramacionValue;
    }

    public int getMotivoVisitaTabla() {
        return motivoVisitaTabla;
    }

    public void setMotivoVisitaTabla(int motivoVisitaTabla) {
        this.motivoVisitaTabla = motivoVisitaTabla;
    }

    public String getMotivoVisitaValue() {
        return motivoVisitaValue;
    }

    public void setMotivoVisitaValue(String motivoVisitaValue) {
        this.motivoVisitaValue = motivoVisitaValue;
    }

    public int getVisitaTabla() {
        return visitaTabla;
    }

    public void setVisitaTabla(int visitaTabla) {
        this.visitaTabla = visitaTabla;
    }

    public String getVisitaValue() {
        return visitaValue;
    }

    public void setVisitaValue(String visitaValue) {
        this.visitaValue = visitaValue;
    }

    public int getReferidoTabla() {
        return referidoTabla;
    }

    public void setReferidoTabla(int referidoTabla) {
        this.referidoTabla = referidoTabla;
    }

    public String getReferidoValue() {
        return referidoValue;
    }

    public void setReferidoValue(String referidoValue) {
        this.referidoValue = referidoValue;
    }

    public String getTiempoCode() {
        return tiempoCode;
    }

    public void setTiempoCode(String tiempoCode) {
        this.tiempoCode = tiempoCode;
    }

    public String getDuracionCode() {
        return duracionCode;
    }

    public void setDuracionCode(String duracionCode) {
        this.duracionCode = duracionCode;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public int getReferidoCount() {
        return referidoCount;
    }

    public void setReferidoCount(int referidoCount) {
        this.referidoCount = referidoCount;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getVisitaId() {
        return visitaId;
    }

    public void setVisitaId(int visitaId) {
        this.visitaId = visitaId;
    }

    public int getLlamadaId() {
        return llamadaId;
    }

    public void setLlamadaId(int llamadaId) {
        this.llamadaId = llamadaId;
    }

    public List<FotoVisita> getFotos() {
        return fotos;
    }

    public void setFotos(List<FotoVisita> fotos) {
        this.fotos = fotos;
    }

    public List<CotizacionVisita> getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(List<CotizacionVisita> cotizacion) {
        this.cotizacion = cotizacion;
    }

    public SolicitudMovil getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudMovil solicitud) {
        this.solicitud = solicitud;
    }

    public RegistroPago getPago() {
        return pago;
    }

    public void setPago(RegistroPago pago) {
        this.pago = pago;
    }


    //endregion


    @Override
    public String toString() {
        return "VisitaVtaDetalle{" +
                "idVisita=" + idVisita +
                ", descripcion='" + descripcion + '\'' +
                ", observacion='" + observacion + '\'' +
                ", fechaVisita=" + fechaVisita +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", idClienteDireccion='" + idClienteDireccion + '\'' +
                ", idCliente=" + idCliente +
                ", idAgente=" + idAgente +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", motivoReprogramacionTabla=" + motivoReprogramacionTabla +
                ", motivoReprogramacionValue='" + motivoReprogramacionValue + '\'' +
                ", motivoVisitaTabla=" + motivoVisitaTabla +
                ", motivoVisitaValue='" + motivoVisitaValue + '\'' +
                ", visitaTabla=" + visitaTabla +
                ", visitaValue='" + visitaValue + '\'' +
                ", referidoTabla=" + referidoTabla +
                ", referidoValue='" + referidoValue + '\'' +
                ", tiempoCode='" + tiempoCode + '\'' +
                ", duracionCode='" + duracionCode + '\'' +
                ", tipoVenta='" + tipoVenta + '\'' +
                ", referidoCount=" + referidoCount +
                ", estado=" + estado +
                ", visitaId=" + visitaId +
                ", llamadaId=" + llamadaId +
                ", fotos=" + fotos +
                ", cotizacion=" + cotizacion +
                ", solicitud=" + solicitud +
                ", pago=" + pago +
                ", prospectoVta=" + prospectoVta +
                '}';
    }
}
