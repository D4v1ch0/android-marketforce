package rp3.auna.bean;

/**
 * Created by Jesus Villa on 23/01/2018.
 */

public class ItemDrawerSubtitle {
    private String option;
    private String subTitle;

    public ItemDrawerSubtitle(String option, String subTitle) {
        this.option = option;
        this.subTitle = subTitle;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public String toString() {
        return "ItemDrawerSubtitle{" +
                "option='" + option + '\'' +
                ", subTitle='" + subTitle + '\'' +
                '}';
    }
}
