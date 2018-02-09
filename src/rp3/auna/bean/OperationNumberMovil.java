package rp3.auna.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 21/11/2017.
 */

public class OperationNumberMovil {
    @SerializedName("OperationNumber")
    private int operationNumber;

    public int getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(int operationNumber) {
        this.operationNumber = operationNumber;
    }

    @Override
    public String toString() {
        return "OperationNumberMovil{" +
                "operationNumber=" + operationNumber +
                '}';
    }
}
