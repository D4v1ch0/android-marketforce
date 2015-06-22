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
import rp3.marketforce.models.marcacion.Justificacion;
import rp3.marketforce.models.marcacion.Marcacion;
import rp3.marketforce.models.marcacion.Permiso;

/**
 * Created by magno_000 on 19/06/2015.
 */
public class PermisoAdapter extends BaseAdapter {
    private SimpleDateFormat format5;
    private Context context;
    private LayoutInflater inflater;
    private List<Justificacion> permisos;

    public PermisoAdapter(Context context, List<Justificacion> permisos)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.permisos = permisos;
        format5 = new SimpleDateFormat("dd/MM/yy");
    }

    @Override
    public int getCount() {
        return permisos.size();
    }

    @Override
    public Justificacion getItem(int position) {
        return permisos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_permiso), null);

        Justificacion setter = permisos.get(position);
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