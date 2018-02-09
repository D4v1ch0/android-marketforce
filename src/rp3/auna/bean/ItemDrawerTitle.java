package rp3.auna.bean;

import android.graphics.drawable.Drawable;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jesus Villa on 23/01/2018.
 */

public class ItemDrawerTitle implements ParentObject {
    private List<Object> mChildrenList;
    private UUID _id;
    private String titulo;
    private android.graphics.drawable.Drawable Drawable;

    public ItemDrawerTitle(String title, android.graphics.drawable.Drawable drawable) {
        this.titulo = title;
        _id = UUID.randomUUID();
        this.Drawable = drawable;
    }

    public UUID get_id() {
        return _id;
    }

    public void set_id(UUID _id) {
        this._id = _id;
    }

    public String getTitle() {
        return titulo;
    }

    public void setTitle(String title) {
        this.titulo = title;
    }

    public Drawable getDrawable(){return Drawable;}

    public void setDrawable(Drawable drawable){
        this.Drawable=drawable;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }

    @Override
    public String toString() {
        return "ItemDrawerTitle{" +
                "mChildrenList=" + mChildrenList +
                ", _id=" + _id +
                ", titulo='" + titulo + '\'' +
                ", Drawable=" + Drawable +
                '}';
    }
}
