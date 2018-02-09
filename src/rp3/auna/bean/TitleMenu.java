package rp3.auna.bean;

import android.graphics.drawable.Drawable;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Jesus Villa on 23/01/2018.
 */

public class TitleMenu extends ExpandableGroup<ItemSubTitle> {
    private String titulo;
    private android.graphics.drawable.Drawable Drawable;

    public TitleMenu(String title, List<ItemSubTitle> items, android.graphics.drawable.Drawable drawable) {
        super(title, items);
        this.titulo = title;
        this.Drawable=drawable;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo (String imageUrl) {
        this.titulo = imageUrl;
    }

    public android.graphics.drawable.Drawable getDrawable() {
        return Drawable;
    }

    public void setDrawable(android.graphics.drawable.Drawable drawable) {
        Drawable = drawable;
    }

    @Override
    public String toString() {
        return "TitleMenu{" +
                "titulo='" + titulo + '\'' +
                ", Drawable=" + Drawable +
                '}';
    }
}
