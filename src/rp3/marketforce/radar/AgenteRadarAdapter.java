package rp3.marketforce.radar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.AgenteResumen;
import rp3.marketforce.models.AgenteUbicacion;

/**
 * Created by magno_000 on 20/08/2015.
 */
public class AgenteRadarAdapter extends BaseAdapter {

    List<AgenteUbicacion> list_resumen;
    ArrayList<Integer> listNotShowed;
    LayoutInflater inflater;
    Context ctx;

    public AgenteRadarAdapter(Context c, List<AgenteUbicacion> list_resumen, ArrayList<Integer> listNotShowed){
        this.inflater = LayoutInflater.from(c);
        this.listNotShowed = listNotShowed;
        this.ctx = c;
        this.list_resumen = list_resumen;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_resumen.size() ;
    }

    @Override
    public AgenteUbicacion getItem(int position) {
        // TODO Auto-generated method stub
        return list_resumen.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public ArrayList<Integer> getListNotShowed()
    {
        return listNotShowed;
    }

    public void SelectAll(boolean select)
    {
        listNotShowed.clear();
        if(!select)
        {
            for(AgenteUbicacion ag : list_resumen)
                listNotShowed.add(ag.getIdAgente());
        }

        this.notifyDataSetChanged();


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = (View) inflater.inflate(this.ctx.getApplicationContext().getResources().getLayout(R.layout.rowlist_agente_radar), null);

        AgenteUbicacion agd = list_resumen.get(position);
        CheckBox checkBox =((CheckBox) convertView.findViewById(R.id.is_showed));

        ((TextView) convertView.findViewById(R.id.agente_name)).setText(agd.getNombres() + " " + agd.getApellidos());
        if(!listNotShowed.contains(agd.getIdAgente()))
            checkBox.setChecked(true);
        checkBox.setId(agd.getIdAgente());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    listNotShowed.remove(new Integer(buttonView.getId()));
                else
                    listNotShowed.add(buttonView.getId());
            }
        });
        return convertView;
    }
}
