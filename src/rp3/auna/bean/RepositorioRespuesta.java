package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 04/01/2018.
 */

public class RepositorioRespuesta {
    @SerializedName("Orden")
    private int orden;
    @SerializedName("Respuesta")
    private String respuesta;
    @SerializedName("TipoDocumento")
    private String tipoDocumento;
    @SerializedName("Documento")
    private String documento;
    @SerializedName("Estado")
    private int estado;
    //Estado 0.-Formato Incorrecto 1.-Correcto 2.-Ya existe

    //region Encapsulamiento

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    //endregion

    @Override
    public String toString() {
        return "RepositorioRespuesta{" +
                "orden=" + orden +
                ", respuesta='" + respuesta + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", documento='" + documento + '\'' +
                ", estado=" + estado +
                '}';
    }
}
