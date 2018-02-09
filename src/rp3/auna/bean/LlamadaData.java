package rp3.auna.bean;

/**
 * Created by Jesus Villa on 19/10/2017.
 */

public class LlamadaData {

    private int incoming;
    private int outgoing;
    private int duration;

    public int getIncoming() {
        return incoming;
    }

    public void setIncoming(int incoming) {
        this.incoming = incoming;
    }

    public int getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(int outgoing) {
        this.outgoing = outgoing;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "LlamadaData{" +
                "incoming=" + incoming +
                ", outgoing=" + outgoing +
                ", duration=" + duration +
                '}';
    }
}
