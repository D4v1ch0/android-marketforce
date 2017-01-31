package rp3.auna.oportunidad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.Agente;

/**
 * Created by magno_000 on 18/02/2016.
 */
public class AgenteOportunidadAdapter extends BaseAdapter {

    List<Agente> list_resumen;
    ArrayList<Integer> listNotShowed;
    LayoutInflater inflater;
    Context ctx;

    public AgenteOportunidadAdapter(Context c, List<Agente> list_resumen, ArrayList<Integer> listNotShowed){
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
    public Agente getItem(int position) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = (View) inflater.inflate(this.ctx.getApplicationContext().getResources().getLayout(R.layout.rowlist_agente_radar), null);

        Agente agd = list_resumen.get(position);
        CheckBox checkBox =((CheckBox) convertView.findViewById(R.id.is_showed));

        ((TextView) convertView.findViewById(R.id.agente_name)).setText(agd.getNombre());
        if(listNotShowed.contains(agd.getIdAgente())) {
            checkBox.setChecked(true);
            if(agd.getIdAgente() ==  PreferenceManager.getInt(Contants.KEY_IDAGENTE))
                checkBox.setEnabled(false);
        }
        checkBox.setId(agd.getIdAgente());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    listNotShowed.add(buttonView.getId());
                else
                    listNotShowed.remove(new Integer(buttonView.getId()));
            }
        });
        return convertView;
    }
}
