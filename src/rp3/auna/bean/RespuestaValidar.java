package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 21/11/2017.
 */

public class RespuestaValidar {
    @SerializedName("Result")
    private String result;
    @SerializedName("Valor")
    private String valor;
    @SerializedName("Info")
    private String info;

    //region Encapsulamiento
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    //endregion

    @Override
    public String toString() {
        return "RespuestaValidar{" +
                "result='" + result + '\'' +
                ", valor='" + valor + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
