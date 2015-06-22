package rp3.marketforce.marcaciones;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.marcacion.Marcacion;

/**
 * Created by magno_000 on 19/06/2015.
 */
public class MarcacionAdapter extends BaseAdapter {
    private SimpleDateFormat format5, format4;
    private Context context;
    private LayoutInflater inflater;
    private List<Marcacion> marcaciones;

    public MarcacionAdapter(Context context, List<Marcacion> marcaciones)
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
    public Marcacion getItem(int position) {
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

        Marcacion setter = marcaciones.get(position);
        ((TextView) convertView.findViewById(R.id.marcacion_minutos)).setText(setter.getMintutosAtraso() + "");
        ((TextView) convertView.findViewById(R.id.marcacion_fecha)).setText(format4.format(setter.getFecha()));
        ((TextView) convertView.findViewById(R.id.marcacion_hora)).setText(format5.format(setter.getFecha()));
        if(setter.getTipo().equals("J1"))
            ((TextView) convertView.findViewById(R.id.marcacion_jornada)).setText("Inicio Jornada");
        if(setter.getTipo().equals("J2"))
            ((TextView) convertView.findViewById(R.id.marcacion_jornada)).setText("Break");
        if(setter.getTipo().equals("J3"))
            ((TextView) convertView.findViewById(R.id.marcacion_jornada)).setText("Fin Break");
        if(setter.getTipo().equals("J4"))
            ((TextView) convertView.findViewById(R.id.marcacion_jornada)).setText("Fin Jornada");

        return convertView;
    }
}
