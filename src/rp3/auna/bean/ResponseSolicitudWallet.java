package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 15/01/2018.
 */

public class ResponseSolicitudWallet {
    @SerializedName("Cut")
    private int cut;
    @SerializedName("CodeResponse")
    private String codeResponse;
    @SerializedName("Response")
    private String response;
    @SerializedName("CodAsoCardHolderWallet")
    private String codAsoCardHolderWallet;
    @SerializedName("Email")
    private String email;
    @SerializedName("Flag")
    private int flag;

    /**
     * Flag:
     * 1.-Error en Validar Solicitud:Mostrar el codigo de error con GeneralValues y el Message
     * 2.-Error en Wallet:Mostrar un mensaje de Wallet
     * 3.-Error el PaymeWallet llego vacio
     * 4.-Error desconocido: Mostrar el mensaje de la Exception
     * 5.-Error OperationNumber: Vacio el numero de operation
     * @return
     */

    //region Encapsulamiento

    public int getCut() {
        return cut;
    }

    public void setCut(int cut) {
        this.cut = cut;
    }

    public String getCodeResponse() {
        return codeResponse;
    }

    public void setCodeResponse(String codeResponse) {
        this.codeResponse = codeResponse;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCodAsoCardHolderWallet() {
        return codAsoCardHolderWallet;
    }

    public void setCodAsoCardHolderWallet(String codAsoCardHolderWallet) {
        this.codAsoCardHolderWallet = codAsoCardHolderWallet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    //endregion


    @Override
    public String toString() {
        return "ResponseSolicitudWallet{" +
                "cut=" + cut +
                ", codeResponse='" + codeResponse + '\'' +
                ", response='" + response + '\'' +
                ", codAsoCardHolderWallet='" + codAsoCardHolderWallet + '\'' +
                ", email='" + email + '\'' +
                ", flag=" + flag +
                '}';
    }
}
