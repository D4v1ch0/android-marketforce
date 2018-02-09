package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 01/01/2018.
 */

public class ValidateDocument {
    @SerializedName("Documento")
    private String documento;
    @SerializedName("Flag")
    private int Flag;
    //0.-Aceptable 1.-Existe en Prospecto 2.-Existe en Lista Negra

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }

    @Override
    public String toString() {
        return "ValidateDocument{" +
                "documento='" + documento + '\'' +
                ", Flag=" + Flag +
                '}';
    }
}
