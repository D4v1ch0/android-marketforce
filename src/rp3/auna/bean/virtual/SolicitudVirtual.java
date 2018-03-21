package rp3.auna.bean.virtual;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 20/03/2018.
 */

public class SolicitudVirtual {
    @SerializedName("Cut")
    private int Cut;
    @SerializedName("Origen")
    private String origen;
    @SerializedName("Canal")
    private String canal;
    @SerializedName("FlagPago")
    private String flagPago;
    @SerializedName("CodUnix")
    private String codUnix;
    @SerializedName("CodUnixCorporativo")
    private String codUnixCorporativo;
    @SerializedName("TipoOperacion")
    private String tipoOperacion;
    @SerializedName("CodigoPrograma")
    private String codigoPrograma;
    @SerializedName("CodigoProcedencia")
    private String codigoProcedencia;
    @SerializedName("TipoTarifa")
    private String tipoTarifa;
    @SerializedName("ModoTarifa")
    private String modoTarifa;
    @SerializedName("FrecuenciaPago")
    private String frecuenciaPago;
    @SerializedName("FechaVenta")
    private String fechaVenta;
    @SerializedName("FlagKitBienvenida")
    private String flagKitBienvenida;
    @SerializedName("FlagPublicidad")
    private String flagPublicidad;
    @SerializedName("TipoDocumentoVenta")
    private String tipoDocumentoVenta;
    @SerializedName("TipoPersonaContratante")
    private String tipoPersonaContratante;
    @SerializedName("RazonSocial")
    private String razonSocial;
    @SerializedName("Ruc")
    private String ruc;
    @SerializedName("Agencia")
    private String agencia;
    @SerializedName("CodigoEmpresa")
    private String codigoEmpresa;
    @SerializedName("EntidadProcesadoraRecurrencia")
    private String entidadProcesadoraRecurrencia;
    @SerializedName("NumeroTarjeta")
    private String numeroTarjeta;
    @SerializedName("FechaVencimiento")
    private String fechaVencimiento;
    @SerializedName("TitularTarjeta")
    private String titularTarjeta;
    @SerializedName("Moneda")
    private String moneda;
    @SerializedName("NumeroCuotas")
    private String numeroCuotas;
    @SerializedName("MontoCotizado")
    private String montoCotizado;
    @SerializedName("MontoCobrar")
    private String montoCobrar;
    @SerializedName("CampoAuxiliar")
    private String campoAuxiliar;
    @SerializedName("UsuarioBandeja")
    private String usuarioBandeja;
    @SerializedName("FechaUsuarioReviso")
    private String fechaUsuarioReviso;
    @SerializedName("IndicadorBandeja")
    private String indicadorBandeja;

    //region Encapsulamiento

    public int getCut() {
        return Cut;
    }

    public void setCut(int cut) {
        Cut = cut;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getFlagPago() {
        return flagPago;
    }

    public void setFlagPago(String flagPago) {
        this.flagPago = flagPago;
    }

    public String getCodUnix() {
        return codUnix;
    }

    public void setCodUnix(String codUnix) {
        this.codUnix = codUnix;
    }

    public String getCodUnixCorporativo() {
        return codUnixCorporativo;
    }

    public void setCodUnixCorporativo(String codUnixCorporativo) {
        this.codUnixCorporativo = codUnixCorporativo;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }

    public String getCodigoProcedencia() {
        return codigoProcedencia;
    }

    public void setCodigoProcedencia(String codigoProcedencia) {
        this.codigoProcedencia = codigoProcedencia;
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    public String getModoTarifa() {
        return modoTarifa;
    }

    public void setModoTarifa(String modoTarifa) {
        this.modoTarifa = modoTarifa;
    }

    public String getFrecuenciaPago() {
        return frecuenciaPago;
    }

    public void setFrecuenciaPago(String frecuenciaPago) {
        this.frecuenciaPago = frecuenciaPago;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getFlagKitBienvenida() {
        return flagKitBienvenida;
    }

    public void setFlagKitBienvenida(String flagKitBienvenida) {
        this.flagKitBienvenida = flagKitBienvenida;
    }

    public String getFlagPublicidad() {
        return flagPublicidad;
    }

    public void setFlagPublicidad(String flagPublicidad) {
        this.flagPublicidad = flagPublicidad;
    }

    public String getTipoDocumentoVenta() {
        return tipoDocumentoVenta;
    }

    public void setTipoDocumentoVenta(String tipoDocumentoVenta) {
        this.tipoDocumentoVenta = tipoDocumentoVenta;
    }

    public String getTipoPersonaContratante() {
        return tipoPersonaContratante;
    }

    public void setTipoPersonaContratante(String tipoPersonaContratante) {
        this.tipoPersonaContratante = tipoPersonaContratante;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getEntidadProcesadoraRecurrencia() {
        return entidadProcesadoraRecurrencia;
    }

    public void setEntidadProcesadoraRecurrencia(String entidadProcesadoraRecurrencia) {
        this.entidadProcesadoraRecurrencia = entidadProcesadoraRecurrencia;
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

    public String getTitularTarjeta() {
        return titularTarjeta;
    }

    public void setTitularTarjeta(String titularTarjeta) {
        this.titularTarjeta = titularTarjeta;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(String numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public String getMontoCotizado() {
        return montoCotizado;
    }

    public void setMontoCotizado(String montoCotizado) {
        this.montoCotizado = montoCotizado;
    }

    public String getMontoCobrar() {
        return montoCobrar;
    }

    public void setMontoCobrar(String montoCobrar) {
        this.montoCobrar = montoCobrar;
    }

    public String getCampoAuxiliar() {
        return campoAuxiliar;
    }

    public void setCampoAuxiliar(String campoAuxiliar) {
        this.campoAuxiliar = campoAuxiliar;
    }

    public String getUsuarioBandeja() {
        return usuarioBandeja;
    }

    public void setUsuarioBandeja(String usuarioBandeja) {
        this.usuarioBandeja = usuarioBandeja;
    }

    public String getFechaUsuarioReviso() {
        return fechaUsuarioReviso;
    }

    public void setFechaUsuarioReviso(String fechaUsuarioReviso) {
        this.fechaUsuarioReviso = fechaUsuarioReviso;
    }

    public String getIndicadorBandeja() {
        return indicadorBandeja;
    }

    public void setIndicadorBandeja(String indicadorBandeja) {
        this.indicadorBandeja = indicadorBandeja;
    }

    //endregion

    @Override
    public String toString() {
        return "SolicitudVirtual{" +
                "Cut=" + Cut +
                ", origen='" + origen + '\'' +
                ", canal='" + canal + '\'' +
                ", flagPago='" + flagPago + '\'' +
                ", codUnix='" + codUnix + '\'' +
                ", codUnixCorporativo='" + codUnixCorporativo + '\'' +
                ", tipoOperacion='" + tipoOperacion + '\'' +
                ", codigoPrograma='" + codigoPrograma + '\'' +
                ", codigoProcedencia='" + codigoProcedencia + '\'' +
                ", tipoTarifa='" + tipoTarifa + '\'' +
                ", modoTarifa='" + modoTarifa + '\'' +
                ", frecuenciaPago='" + frecuenciaPago + '\'' +
                ", fechaVenta='" + fechaVenta + '\'' +
                ", flagKitBienvenida='" + flagKitBienvenida + '\'' +
                ", flagPublicidad='" + flagPublicidad + '\'' +
                ", tipoDocumentoVenta='" + tipoDocumentoVenta + '\'' +
                ", tipoPersonaContratante='" + tipoPersonaContratante + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", ruc='" + ruc + '\'' +
                ", agencia='" + agencia + '\'' +
                ", codigoEmpresa='" + codigoEmpresa + '\'' +
                ", entidadProcesadoraRecurrencia='" + entidadProcesadoraRecurrencia + '\'' +
                ", numeroTarjeta='" + numeroTarjeta + '\'' +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                ", titularTarjeta='" + titularTarjeta + '\'' +
                ", moneda='" + moneda + '\'' +
                ", numeroCuotas='" + numeroCuotas + '\'' +
                ", montoCotizado='" + montoCotizado + '\'' +
                ", montoCobrar='" + montoCobrar + '\'' +
                ", campoAuxiliar='" + campoAuxiliar + '\'' +
                ", usuarioBandeja='" + usuarioBandeja + '\'' +
                ", fechaUsuarioReviso='" + fechaUsuarioReviso + '\'' +
                ", indicadorBandeja='" + indicadorBandeja + '\'' +
                '}';
    }
}
