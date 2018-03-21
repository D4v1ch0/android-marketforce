package rp3.auna.bean.virtual;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jesus Villa on 21/03/2018.
 */

public class AfiliaciónVirtual {
    @SerializedName("SolicitudVirtual")
    private SolicitudVirtual solicitudVirtual;
    @SerializedName("PreAfiliado")
    private List<PreAfiliado> preAfiliado;
    @SerializedName("ResponseSolicitudVirtual")
    private List<ResponseSolicitudVirtual> responseSolicitudVirtual;
    @SerializedName("Flag")
    private int flag;//Representa el estado de la transacción
    @SerializedName("Message")
    private String message;

    //region Encapsulamiento

    public SolicitudVirtual getSolicitudVirtual() {
        return solicitudVirtual;
    }

    public void setSolicitudVirtual(SolicitudVirtual solicitudVirtual) {
        this.solicitudVirtual = solicitudVirtual;
    }

    public List<PreAfiliado> getPreAfiliado() {
        return preAfiliado;
    }

    public void setPreAfiliado(List<PreAfiliado> preAfiliado) {
        this.preAfiliado = preAfiliado;
    }

    public List<ResponseSolicitudVirtual> getResponseSolicitudVirtual() {
        return responseSolicitudVirtual;
    }

    public void setResponseSolicitudVirtual(List<ResponseSolicitudVirtual> responseSolicitudVirtual) {
        this.responseSolicitudVirtual = responseSolicitudVirtual;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//endregion

    @Override
    public String toString() {
        return "AfiliaciónVirtual{" +
                "solicitudVirtual=" + solicitudVirtual +
                ", preAfiliado=" + preAfiliado +
                ", responseSolicitudVirtual=" + responseSolicitudVirtual +
                ", flag=" + flag +
                ", message='" + message + '\'' +
                '}';
    }
}
