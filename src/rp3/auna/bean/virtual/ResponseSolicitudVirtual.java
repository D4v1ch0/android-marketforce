package rp3.auna.bean.virtual;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 20/03/2018.
 */

public class ResponseSolicitudVirtual {
    @SerializedName("IndTabla")
    private String indTabla;
    @SerializedName("IdReg")
    private String idReg;
    @SerializedName("NumeroSolicitud")
    private String numeroSolicitud;
    @SerializedName("Result")
    private String result;
    @SerializedName("Info")
    private String info;

    //region Encapsulamiento

    public String getIndTabla() {
        return indTabla;
    }

    public void setIndTabla(String indTabla) {
        this.indTabla = indTabla;
    }

    public String getIdReg() {
        return idReg;
    }

    public void setIdReg(String idReg) {
        this.idReg = idReg;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    //endregion


}
