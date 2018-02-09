package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 13/12/2017.
 */

public class Wallet {
    @SerializedName("CodAsoCardHolderWallet")
    private String codAsoCardHolderWallet;

    public String getCodAsoCardHolderWallet() {
        return codAsoCardHolderWallet;
    }

    public void setCodAsoCardHolderWallet(String codAsoCardHolderWallet) {
        this.codAsoCardHolderWallet = codAsoCardHolderWallet;
    }
}
