package rp3.auna.bean;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class Message {
     private String TextMessage;
     private int MessageType;
     private String Title;
     private String Text;

    public String getTextMessage() {
        return TextMessage;
    }

    public void setTextMessage(String textMessage) {
        TextMessage = textMessage;
    }

    public int getMessageType() {
        return MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
