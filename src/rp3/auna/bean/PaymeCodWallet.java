package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 15/01/2018.
 */

public class PaymeCodWallet {
    @SerializedName("IdEntCommerce")
    private String idEntCommerce;
    @SerializedName("CodCardHolderCommerce")
    private String codCardHolderCommerce;
    @SerializedName("Names")
    private String names;
    @SerializedName("LastNames")
    private String lastNames;
    @SerializedName("Mail")
    private String mail;
    @SerializedName("Reserved1")
    private String reserved1;
    @SerializedName("Reserved2")
    private String reserved2;
    @SerializedName("Reserved3")
    private String reserved3;
    @SerializedName("Prefix")
    private String prefix;

    //region Encapsulamiento

    public String getIdEntCommerce() {
        return idEntCommerce;
    }

    public void setIdEntCommerce(String idEntCommerce) {
        this.idEntCommerce = idEntCommerce;
    }

    public String getCodCardHolderCommerce() {
        return codCardHolderCommerce;
    }

    public void setCodCardHolderCommerce(String codCardHolderCommerce) {
        this.codCardHolderCommerce = codCardHolderCommerce;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    //endregion


    @Override
    public String toString() {
        return "PaymeCodWallet{" +
                "idEntCommerce='" + idEntCommerce + '\'' +
                ", codCardHolderCommerce='" + codCardHolderCommerce + '\'' +
                ", names='" + names + '\'' +
                ", lastNames='" + lastNames + '\'' +
                ", mail='" + mail + '\'' +
                ", reserved1='" + reserved1 + '\'' +
                ", reserved2='" + reserved2 + '\'' +
                ", reserved3='" + reserved3 + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
