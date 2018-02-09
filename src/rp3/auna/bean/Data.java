package rp3.auna.bean;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class Data {
    private int UserId;
    private String AuthToken;
    private String Name;
    private String LogonName;
    private boolean IsValid;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLogonName() {
        return LogonName;
    }

    public void setLogonName(String logonName) {
        LogonName = logonName;
    }

    public boolean isValid() {
        return IsValid;
    }

    public void setValid(boolean valid) {
        IsValid = valid;
    }
}
