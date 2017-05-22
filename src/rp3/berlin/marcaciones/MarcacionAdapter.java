package rp3.berlin.marcaciones;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import rp3.berlin.R;

/**
 * Created by magno_000 on 19/06/2015.
 */
public class MarcacionAdapter extends BaseAdapter {
    private SimpleDateFormat format5, format4;
    private Context context;
    private LayoutInflater inflater;
    private List<MarcacionFragment.DiaMarcacion> marcaciones;

    public MarcacionAdapter(Context context, List<MarcacionFragment.DiaMarcacion> marcaciones)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.marcaciones = marcaciones;
        format5 = new SimpleDateFormat("dd/MM/yy");
        format4= new SimpleDateFormat("HH:mm");
    }

    @Override
    public int getCount() {
        return marcaciones.size();
    }

    @Override
    public MarcacionFragment.DiaMarcacion getItem(int position) {
        return marcaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_marcaciones), null);

        MarcacionFragment.DiaMarcacion setter = marcaciones.get(position);
        ((TextView) convertView.findViewById(R.id.marcacion_dia)).setText(setter.dia);
        ((TextView) convertView.findViewById(R.id.marcacion_fecha)).setText(setter.fecha);
        if(setter.inicio_jornada1 != null)
            ((TextView) convertView.findViewById(R.id.marcacion_ini_jor1)).setText(setter.inicio_jornada1);
        if(setter.inicio_jornada2 != null)
            ((TextView) convertView.findViewById(R.id.marcacion_ini_for2)).setText(setter.inicio_jornada2);
        if(setter.fin_jornada1 != null)
            ((TextView) convertView.findViewById(R.id.marcacion_fin_jor1)).setText(setter.fin_jornada1);
        if(setter.fin_jornada2 != null)
            ((TextView) convertView.findViewById(R.id.marcacion_fin_jor2)).setText(setter.fin_jornada2);

        if(setter.atraso)
            ((ImageView) convertView.findViewById(R.id.marcacion_atraso)).setImageResource(R.drawable.circle_unvisited);
        else
            ((ImageView) convertView.findViewById(R.id.marcacion_atraso)).setImageResource(R.drawable.circle_visited);

        return convertView;
    }
}
