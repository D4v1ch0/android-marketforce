package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 15/01/2018.
 */

public class SolicitudOncosys {
    //Esto lo obtendre det OperationNumber
    @SerializedName("Cut")
    private int cut;
    @SerializedName("NumeroSolicitud")
    private String numeroSolicitud;
    @SerializedName("CodigoPrograma")
    private String codigoPrograma;
    @SerializedName("CodigoCanal")
    private String codigoCanal;
    //Unix: Esto lo obtendre consultando al DAL según el agente
    @SerializedName("CodigoAsesor")
    private String codigoAsesor;
    @SerializedName("Agente")
    private int agente;
    //
    @SerializedName("CodigoPrcCampaña")
    private String codigoPrcCampaña;
    //Siempre 01
    @SerializedName("TipoTarifa")
    private String tipoTarifa;
    @SerializedName("ModoTarifa")
    private String modoTarifa;
    @SerializedName("FrencuenciaPago")
    private String frecuenciaPago;
    @SerializedName("TipoTarjeta")
    private String tipoTarjeta;
    //El monto acepta decimal valor.fraccion("00")
    @SerializedName("MontoIngresado")
    private String montoIngresado;
    //Procesadora Según elija de la tabla EQ (Movil Combo Tipo Tarjeta)
    @SerializedName("EntidadProcesadora")
    private String entidadProcesadora;
    //Numero de la tarjeta (Nuevo campo en movil)
    @SerializedName("NumeroTarjeta")
    private String numeroTarjeta;
    //Fecha de vencimiento de la tarjeta(nuevo campo en movil) MM/YYYY
    @SerializedName("FechaVencimiento")
    private String fechaVencimiento;
    //Nombre completo del Titular
    @SerializedName("Titular")
    private String titular;
    //Por default PEN según documento
    @SerializedName("Moneda")
    private String moneda;
    //Boleta o Factura:Siempre 01 Boleta por default según documento
    @SerializedName("TipoDocumentoVenta")
    private String tipoDocumentoVenta;
    //FlgTipo: Aportante Siempre el valor de 2
    @SerializedName("TipoRegistro")
    private String tipoRegistro;
    //Según sea elegido de las entidades procesadoras DNI:01
    @SerializedName("TipoDocumentoIdentificacion")
    private String tipoDocumentoIdentificacion;
    //Según se ingrese el numero del documento identificacion
    @SerializedName("NumeroDocumentoIdentificacion")
    private String numeroDocumentoIdentificacion;
    @SerializedName("ApellidoPaterno")
    private String apellidoPaterno;
    @SerializedName("Nombre1")
    private String nombre1;
    @SerializedName("Correo")
    private String correo;
    @SerializedName("IdVisita")
    private int idVisita;
    @SerializedName("IdAfiliadoContratante")
    private int idAfiliadoContratante;
    //Setear el PaymeWallet
    @SerializedName("PaymeCodWallet")
    PaymeCodWallet paymeCodWallet;

    //region Encapsulamiento

    public String getFrecuenciaPago() {
        return frecuenciaPago;
    }

    public void setFrecuenciaPago(String frecuenciaPago) {
        this.frecuenciaPago = frecuenciaPago;
    }

    public String getModoTarifa() {
        return modoTarifa;
    }

    public void setModoTarifa(String modoTarifa) {
        this.modoTarifa = modoTarifa;
    }

    public int getIdAfiliadoContratante() {
        return idAfiliadoContratante;
    }

    public void setIdAfiliadoContratante(int idAfiliadoContratante) {
        this.idAfiliadoContratante = idAfiliadoContratante;
    }

    public int getCut() {
        return cut;
    }

    public void setCut(int cut) {
        this.cut = cut;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }

    public String getCodigoCanal() {
        return codigoCanal;
    }

    public void setCodigoCanal(String codigoCanal) {
        this.codigoCanal = codigoCanal;
    }

    public String getCodigoAsesor() {
        return codigoAsesor;
    }

    public void setCodigoAsesor(String codigoAsesor) {
        this.codigoAsesor = codigoAsesor;
    }

    public int getAgente() {
        return agente;
    }

    public void setAgente(int agente) {
        this.agente = agente;
    }

    public String getCodigoPrcCampaña() {
        return codigoPrcCampaña;
    }

    public void setCodigoPrcCampaña(String codigoPrcCampaña) {
        this.codigoPrcCampaña = codigoPrcCampaña;
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getMontoIngresado() {
        return montoIngresado;
    }

    public void setMontoIngresado(String montoIngresado) {
        this.montoIngresado = montoIngresado;
    }

    public String getEntidadProcesadora() {
        return entidadProcesadora;
    }

    public void setEntidadProcesadora(String entidadProcesadora) {
        this.entidadProcesadora = entidadProcesadora;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getTipoDocumentoVenta() {
        return tipoDocumentoVenta;
    }

    public void setTipoDocumentoVenta(String tipoDocumentoVenta) {
        this.tipoDocumentoVenta = tipoDocumentoVenta;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getTipoDocumentoIdentificacion() {
        return tipoDocumentoIdentificacion;
    }

    public void setTipoDocumentoIdentificacion(String tipoDocumentoIdentificacion) {
        this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion;
    }

    public String getNumeroDocumentoIdentificacion() {
        return numeroDocumentoIdentificacion;
    }

    public void setNumeroDocumentoIdentificacion(String numeroDocumentoIdentificacion) {
        this.numeroDocumentoIdentificacion = numeroDocumentoIdentificacion;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public PaymeCodWallet getPaymeCodWallet() {
        return paymeCodWallet;
    }

    public void setPaymeCodWallet(PaymeCodWallet paymeCodWallet) {
        this.paymeCodWallet = paymeCodWallet;
    }

    //endregion

    @Override
    public String toString() {
        return "SolicitudOncosys{" +
                "cut=" + cut +
                ", numeroSolicitud='" + numeroSolicitud + '\'' +
                ", codigoPrograma='" + codigoPrograma + '\'' +
                ", codigoCanal='" + codigoCanal + '\'' +
                ", codigoAsesor='" + codigoAsesor + '\'' +
                ", agente=" + agente +
                ", codigoPrcCampaña='" + codigoPrcCampaña + '\'' +
                ", tipoTarifa='" + tipoTarifa + '\'' +
                ", modoTarifa='" + modoTarifa + '\'' +
                ", frecuenciaPago='" + frecuenciaPago + '\'' +
                ", tipoTarjeta='" + tipoTarjeta + '\'' +
                ", montoIngresado='" + montoIngresado + '\'' +
                ", entidadProcesadora='" + entidadProcesadora + '\'' +
                ", numeroTarjeta='" + numeroTarjeta + '\'' +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                ", titular='" + titular + '\'' +
                ", moneda='" + moneda + '\'' +
                ", tipoDocumentoVenta='" + tipoDocumentoVenta + '\'' +
                ", tipoRegistro='" + tipoRegistro + '\'' +
                ", tipoDocumentoIdentificacion='" + tipoDocumentoIdentificacion + '\'' +
                ", numeroDocumentoIdentificacion='" + numeroDocumentoIdentificacion + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", nombre1='" + nombre1 + '\'' +
                ", correo='" + correo + '\'' +
                ", idVisita=" + idVisita +
                ", idAfiliadoContratante=" + idAfiliadoContratante +
                ", paymeCodWallet=" + paymeCodWallet +
                '}';
    }
}
