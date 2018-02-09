package rp3.auna.events;

import android.os.Bundle;

/**
 * Created by Jesus Villa on 20/10/2017.
 */

public class Events {
    public static class Message {
        public Bundle message;
        public Message(Bundle message) {
            this.message = message;
        }
        public Bundle getMessage() {
            return message;
        }
        public void setMessage(Bundle message){
            this.message=message;
        }
    }
}
