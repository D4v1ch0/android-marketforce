package rp3.auna.oportunidad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import rp3.auna.R;
import rp3.auna.models.oportunidad.OportunidadTarea;

/**
 * Created by magno_000 on 18/05/2015.
 */
public class EtapaTareasAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<OportunidadTarea> oportunidadTareas;
    private int id_icon;
    private int orden;
    private int subetapa;
    private int subtarea;

    public EtapaTareasAdapter(Context context, List<OportunidadTarea> oportunidadTareas, int orden)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.oportunidadTareas = oportunidadTareas;
        this.orden = orden;
    }

    @Override
    public int getCount() {
        return oportunidadTareas.size();
    }

    @Override
    public OportunidadTarea getItem(int position) {
        return oportunidadTareas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(oportunidadTareas.get(position).getIdTarea() == 0)
        {
            subetapa++;
            subtarea = 0;
            convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_subetapa), null);
            ((TextView) convertView.findViewById(R.id.subetapa_text)).setText(orden + "." + subetapa + " " + oportunidadTareas.get(position).getObservacion());
            convertView.setOnClickListener(null);
        }
        else {
            subtarea++;
            convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_tarea), null);


            if (oportunidadTareas.get(position).getEstado().equals("P")) {
                id_icon = R.drawable.x_red;
            } else {
                id_icon = R.drawable.check;
            }

            ((TextView) convertView.findViewById(R.id.map_phone)).setCompoundDrawablesWithIntrinsicBounds(0, 0, id_icon, 0);
            //((TextView) convertView.findViewById(R.id.map_phone)).setBackgroundColor(context.getResources().getColor(id_color));
            //((TextView) convertView.findViewById(R.id.map_phone)).setText(agendaTarea.get(position).getEstadoTareaDescripcion());

            ((TextView) convertView.findViewById(R.id.detail_agenda_estado)).setText(oportunidadTareas.get(position).getTarea().getNombreTarea());
            ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setText(subtarea + "");
        }

        return convertView;
    }

}

