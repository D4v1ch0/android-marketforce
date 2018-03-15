package rp3.auna.bean.models;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleChild {

    public String option;
    public String subtitle;

    public TitleChild(String option, String subtitle) {
        this.option = option;
        this.subtitle = subtitle;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option1) {
        this.option = option1;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String option2) {
        this.subtitle = option2;
    }
}
