package rp3.auna.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jesus Villa on 22/01/2018.
 */

public class ItemSubTitle implements Parcelable {
    private String option;
    private String subTitle;

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "ItemSubTitle{" +
                "option='" + option + '\'' +
                ", subTitle='" + subTitle + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subTitle);
        dest.writeString(this.option);
    }

    public ItemSubTitle() {
    }

    protected ItemSubTitle(Parcel in) {
        this.subTitle = in.readString();
        this.option = in.readString();
    }

    public static final Creator<ItemSubTitle> CREATOR = new Creator<ItemSubTitle>() {
        @Override
        public ItemSubTitle createFromParcel(Parcel source) {
            return new ItemSubTitle(source);
        }

        @Override
        public ItemSubTitle[] newArray(int size) {
            return new ItemSubTitle[size];
        }
    };
}
