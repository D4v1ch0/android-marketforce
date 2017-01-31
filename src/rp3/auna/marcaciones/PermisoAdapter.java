package rp3.auna.marcaciones;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import rp3.auna.R;
import rp3.auna.models.marcacion.Justificacion;

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
        ((TextView) convertView.findViewById(R.id.permiso_agente)).setText(setter.getAgente());
        if(setter.isAusencia())
            ((TextView) convertView.findViewById(R.id.permiso_atraso)).setText(R.string.label_ausencia_just);
        else
            ((TextView) convertView.findViewById(R.id.permiso_atraso)).setText(R.string.label_atraso);
        ((TextView) convertView.findViewById(R.id.permiso_motivo)).setText(setter.getTipoDescripcion());
        ((TextView) convertView.findViewById(R.id.permiso_fecha)).setText(format5.format(setter.getFecha()));

        return convertView;
    }
}