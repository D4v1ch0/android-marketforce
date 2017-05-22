package rp3.berlin.oportunidad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import rp3.berlin.R;
import rp3.berlin.models.oportunidad.OportunidadEtapa;

/**
 * Created by magno_000 on 30/11/2015.
 */
public class EtapaDefinicionAdapter extends BaseAdapter {
    private SimpleDateFormat format1;
    private Context context;
    private LayoutInflater inflater;
    private List<OportunidadEtapa> oportunidadEtapas;
    private int id_icon;
    private int subetapa;
    private int subtarea;

    public EtapaDefinicionAdapter(Context context, List<OportunidadEtapa> oportunidadEtapas) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.oportunidadEtapas = oportunidadEtapas;
        format1 = new SimpleDateFormat("EE dd/MM/yy");
    }

    @Override
    public int getCount() {
        return oportunidadEtapas.size();
    }

    @Override
    public OportunidadEtapa getItem(int position) {
        return oportunidadEtapas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void changeList(List<OportunidadEtapa> oportunidadEtapas) {
        this.oportunidadEtapas = oportunidadEtapas;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OportunidadEtapa oportunidadEtapa = oportunidadEtapas.get(position);
        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_etapa_definir), null);
        if(oportunidadEtapa.getEtapa().getIdEtapaPadre() != 0)
            convertView.setPadding(15, 5, 5, 5);

        ((TextView) convertView.findViewById(R.id.detail_agenda_estado)).setText(oportunidadEtapa.getEtapa().getDescripcion());
        ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setText(oportunidadEtapa.getEtapa().getOrden() + "");
        if(oportunidadEtapa.getFechaFinPlan() !=  null)
            ((TextView) convertView.findViewById(R.id.etapa_fecha)).setText(format1.format(oportunidadEtapa.getFechaFinPlan()));

        if(!oportunidadEtapa.getEtapa().isEsVariable())
        {
            ((TextView) convertView.findViewById(R.id.etapa_fecha)).setTextColor(context.getResources().getColor(R.color.color_grey));
        }
            switch (oportunidadEtapa.getEtapa().getOrden()) {
                case 1:
                    ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa1));
                    break;
                case 2:
                    ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa2));
                    break;
                case 3:
                    ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa3));
                    break;
                case 4:
                    ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa4));
                    break;
                case 5:
                    ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setBackgroundColor(context.getResources().getColor(R.color.color_etapa5));
                    break;
            }


        return convertView;
    }
}