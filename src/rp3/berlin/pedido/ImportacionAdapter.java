package rp3.berlin.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import rp3.berlin.R;
import rp3.berlin.models.pedido.Importacion;

/**
 * Created by magno_000 on 27/04/2017.
 */

public class ImportacionAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Importacion> detalles;
    private NumberFormat numberFormat;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ImportacionAdapter(Context context, List<Importacion> detalles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.detalles = detalles;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setMinimumFractionDigits(0);
    }

    @Override
    public int getCount() {
        return detalles.size();
    }

    @Override
    public Importacion getItem(int position) {
        return detalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_importacion), null);

        if(detalles != null && detalles.size() > position) {
            Importacion detalle = detalles.get(position);
            ((TextView) convertView.findViewById(R.id.importacion_tipo_orden)).setText(detalle.getTipoOC());
            ((TextView) convertView.findViewById(R.id.importacion_procedencia)).setText(detalle.getIdPais());
            ((TextView) convertView.findViewById(R.id.importacion_fecha_pedido)).setText(dateFormat.format(detalle.getFechaPedido()));
            ((TextView) convertView.findViewById(R.id.importacion_fecha_entrega)).setText(dateFormat.format(detalle.getFechaPlanRecepcion()));
            ((TextView) convertView.findViewById(R.id.importacion_cantidad)).setText(numberFormat.format(detalle.getCantidad()));
        }
        else
        {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }
}
