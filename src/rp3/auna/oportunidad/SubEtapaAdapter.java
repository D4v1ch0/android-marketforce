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
 * Created by magno_000 on 01/06/2015.
 */
public class SubEtapaAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<OportunidadTarea> oportunidadTareas;
    private int id_icon;
    private int subetapa;
    private int subtarea;

    public SubEtapaAdapter(Context context, List<OportunidadTarea> oportunidadTareas) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.oportunidadTareas = oportunidadTareas;
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

        if (oportunidadTareas.get(position).getObservacion() != null &&
                (oportunidadTareas.get(position).getObservacion().equalsIgnoreCase("Tareas") || oportunidadTareas.get(position).getObservacion().equalsIgnoreCase("Sub Etapas"))) {
            convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_subetapa), null);
            ((TextView) convertView.findViewById(R.id.subetapa_text)).setText(oportunidadTareas.get(position).getObservacion());
            convertView.setOnClickListener(null);
            subetapa = 0;
            subtarea = 0;
        } else if(oportunidadTareas.get(position).getIdTarea() != 0)
        {
            convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_tarea), null);
            subtarea++;

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
        else
        {
            convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_oportunidad_etapa), null);
            subetapa++;

            if (oportunidadTareas.get(position).getEstado().equals("P")) {
                id_icon = R.drawable.x_red;
            } else {
                id_icon = R.drawable.check;
            }

            ((TextView) convertView.findViewById(R.id.map_phone)).setCompoundDrawablesWithIntrinsicBounds(0, 0, id_icon, 0);
            convertView.findViewById(R.id.grey_line).setVisibility(View.GONE);
            ((TextView) convertView.findViewById(R.id.detail_agenda_estado)).setText(oportunidadTareas.get(position).getObservacion());
            ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setText(subetapa + "");
            switch (subetapa) {
                case 1: ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa1)); break;
                case 2: ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa2)); break;
                case 3: ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa3)); break;
                case 4: ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa4)); break;
                case 5: ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa5)); break;
            }
        }

        return convertView;
    }
}
