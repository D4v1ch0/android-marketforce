package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 23/11/2017.
 */

public class RegistroPago {
    @SerializedName("OperationNumber")
    private int operationNumber;
    @SerializedName("Monto")
    private double monto;
    @SerializedName("Moneda")
    private String moneda;
    @SerializedName("EntidadProcesadora")
    private String entidadProcesadora;
    @SerializedName("NumeroTarjeta")
    private String numeroTarjeta;
    @SerializedName("NumeroOperacion")
    private String numeroOperacion;
    @SerializedName("NumeroTransaccion")
    private String numeroTransaccion;
    @SerializedName("CardNumber")
    private String cardNumber;
    @SerializedName("AnswerCode")
    private String answerCode;
    @SerializedName("AnswerMessage")
    private String answerMessage;
    @SerializedName("IdVisita")
    private int idVisita;
    @SerializedName("FechaPago")
    private long fechaPago;
    @SerializedName("ErrorCode")
    private String errorCode;
    @SerializedName("ErrorMessage")
    private String errorMessage;
    @SerializedName("IdAfiliadoContratante")
    private int idAfiliadoContratante;
    @SerializedName("CodAsoCardHolderWallet")
    private String codAsoCardHolderWallet;
    @SerializedName("EmailAgente")
    private String emailAgente;
    @SerializedName("EmailProspecto")
    private String emailProspecto;
    @SerializedName("NombreProspecto")
    private String nombreProspecto;
    @SerializedName("MarcaTarjeta")
    private String marcaTarjeta;
    @SerializedName("Estado")
    private int estado;
    @SerializedName("NumeroSolicitud")
    private String numeroSolicitud;
    @SerializedName("IdAgente")
    private int idAgente;

    //region Encapsulamiento


    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        idAgente = idAgente;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getEmailAgente() {
        return emailAgente;
    }

    public void setEmailAgente(String emailAgente) {
        this.emailAgente = emailAgente;
    }

    public String getEmailProspecto() {
        return emailProspecto;
    }

    public void setEmailProspecto(String emailProspecto) {
        this.emailProspecto = emailProspecto;
    }

    public String getNombreProspecto() {
        return nombreProspecto;
    }

    public void setNombreProspecto(String nombreProspecto) {
        this.nombreProspecto = nombreProspecto;
    }

    public String getMarcaTarjeta() {
        return marcaTarjeta;
    }

    public void setMarcaTarjeta(String marcaTarjeta) {
        this.marcaTarjeta = marcaTarjeta;
    }

    public int getIdAfiliadoContratante() {
        return idAfiliadoContratante;
    }

    public void setIdAfiliadoContratante(int idAfiliadoContratante) {
        this.idAfiliadoContratante = idAfiliadoContratante;
    }

    public String getCodAsoCardHolderWallet() {
        return codAsoCardHolderWallet;
    }

    public void setCodAsoCardHolderWallet(String codAsoCardHolderWallet) {
        this.codAsoCardHolderWallet = codAsoCardHolderWallet;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(int operationNumber) {
        this.operationNumber = operationNumber;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
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

    public String getNumeroOperacion() {
        return numeroOperacion;
    }

    public void setNumeroOperacion(String numeroOperacion) {
        this.numeroOperacion = numeroOperacion;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
    }

    public String getAnswerMessage() {
        return answerMessage;
    }

    public void setAnswerMessage(String answerMessage) {
        this.answerMessage = answerMessage;
    }

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public long getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(long fechaPago) {
        this.fechaPago = fechaPago;
    }

    //endregion

    @Override
    public String toString() {
        return "RegistroPago{" +
                "operationNumber=" + operationNumber +
                ", monto=" + monto +
                ", moneda='" + moneda + '\'' +
                ", entidadProcesadora='" + entidadProcesadora + '\'' +
                ", numeroTarjeta='" + numeroTarjeta + '\'' +
                ", numeroOperacion='" + numeroOperacion + '\'' +
                ", numeroTransaccion='" + numeroTransaccion + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", answerCode='" + answerCode + '\'' +
                ", answerMessage='" + answerMessage + '\'' +
                ", idVisita=" + idVisita +
                ", fechaPago=" + fechaPago +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", idAfiliadoContratante=" + idAfiliadoContratante +
                ", codAsoCardHolderWallet='" + codAsoCardHolderWallet + '\'' +
                ", emailAgente='" + emailAgente + '\'' +
                ", emailProspecto='" + emailProspecto + '\'' +
                ", nombreProspecto='" + nombreProspecto + '\'' +
                ", marcaTarjeta='" + marcaTarjeta + '\'' +
                ", estado=" + estado +
                ", numeroSolicitud='" + numeroSolicitud + '\'' +
                ", idAgente=" + idAgente +
                '}';
    }
}
