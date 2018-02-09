package rp3.auna.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jesus Villa on 06/10/2017.
 */

public class DrawerItem {
    String title;
    Drawable icon;

    public DrawerItem(String title, Drawable icon) {
        this.title = title;
        this.icon = icon;
    }

    //region Encapsulamiento

    public String getTitle() {
        return title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    //endregion


    @Override
    public String toString() {
        return "DrawerItem{" +
                "title='" + title + '\'' +
                ", icon=" + icon +
                '}';
    }



}
