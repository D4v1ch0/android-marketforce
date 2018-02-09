package rp3.auna.bean.Models;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

import rp3.auna.R;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleCreator {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;

    public TitleCreator(Context context) {
        _titleParents = new ArrayList<>();
        final String[] drawerTitles = context.getResources().getStringArray(R.array.drawer);
        final TypedArray drawerIcons = context.getResources().obtainTypedArray(R.array. drawerIcons);
        for(int i=0;i<drawerTitles.length;i++)
        {
            if(i==2){
                List<Object> childList = new ArrayList<>();
                childList.add(new TitleChild("REPVTNGRA","Seguimiento"));
                childList.add(new TitleChild("REPVTNINF","Agentes"));
                childList.add(new TitleChild("REPVTNJEF","Jefatura"));
                childList.add(new TitleChild("REPVTNGER","Gerencia"));
                childList.add(new TitleChild("REPVTNFUN","Funnel"));
                TitleParent title = new TitleParent(drawerTitles[i],drawerIcons.getDrawable(i));
                title.setChildObjectList(childList);
                _titleParents.add(title);
            }else{
                TitleParent title = new TitleParent(drawerTitles[i],drawerIcons.getDrawable(i));
                _titleParents.add(title);
            }

        }
    }

    public static TitleCreator get(Context context)
    {
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context);
        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}
