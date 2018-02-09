package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 06/12/2017.
 */

public class VentaRegularData {
    @SerializedName("EmailAgente")
    private String emailAgente;
    @SerializedName("EmailProspecto")
    private String emailProspecto;
    @SerializedName("NombreProspecto")
    private String nombreProspecto;
    @SerializedName("MontoTotal")
    private String montoTotal;
    @SerializedName("NumeroSolicitud")
    private String numeroSolicitud;
    @SerializedName("IdAgente")
    private int idAgente;

    //region Encapsulamiento

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
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

    public String getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(String montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }
//endregion

    @Override
    public String toString() {
        return "VentaRegularData{" +
                "emailAgente='" + emailAgente + '\'' +
                ", emailProspecto='" + emailProspecto + '\'' +
                ", nombreProspecto='" + nombreProspecto + '\'' +
                ", montoTotal='" + montoTotal + '\'' +
                ", numeroSolicitud='" + numeroSolicitud + '\'' +
                ", idAgente=" + idAgente +
                '}';
    }
}
