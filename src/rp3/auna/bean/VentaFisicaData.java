package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 27/11/2017.
 */

public class VentaFisicaData {
    @SerializedName("EmailAgente")
    private String emailAgente;
    @SerializedName("EmailProspecto")
    private String emailProspecto;
    @SerializedName("NombreProspecto")
    private String nombreProspecto;
    @SerializedName("MontoTotal")
    private String MontoTotal;
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
    public String getNombreProspecto() {
        return nombreProspecto;
    }

    public void setNombreProspecto(String nombreProspecto) {
        this.nombreProspecto = nombreProspecto;
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

    public String getMontoTotal() {
        return MontoTotal;
    }

    public void setMontoTotal(String montoTotal) {
        MontoTotal = montoTotal;
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
        return "VentaFisicaData{" +
                "emailAgente='" + emailAgente + '\'' +
                ", emailProspecto='" + emailProspecto + '\'' +
                ", nombreProspecto='" + nombreProspecto + '\'' +
                ", MontoTotal='" + MontoTotal + '\'' +
                ", numeroSolicitud='" + numeroSolicitud + '\'' +
                ", idAgente=" + idAgente +
                '}';
    }
}
