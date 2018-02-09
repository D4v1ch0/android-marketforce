package rp3.auna.bean;

import java.util.List;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class SignIn {
    private boolean IsValid;
    private List<Message> Messages;
    private Message Message;
    private Data Data;

    public boolean isValid() {
        return IsValid;
    }

    public void setValid(boolean valid) {
        IsValid = valid;
    }

    public List<rp3.auna.bean.Message> getMessages() {
        return Messages;
    }

    public void setMessages(List<rp3.auna.bean.Message> messages) {
        Messages = messages;
    }

    public rp3.auna.bean.Message getMessage() {
        return Message;
    }

    public void setMessage(rp3.auna.bean.Message message) {
        Message = message;
    }

    public rp3.auna.bean.Data getData() {
        return Data;
    }

    public void setData(rp3.auna.bean.Data data) {
        Data = data;
    }
}
