package rp3.berlin.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import rp3.configuration.PreferenceManager;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.pedido.LibroPrecio;

/**
 * Created by magno_000 on 02/05/2017.
 */

public class PrecioAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<LibroPrecio> detalles;
    private NumberFormat numberFormat;

    public PrecioAdapter(Context context, List<LibroPrecio> detalles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.detalles = detalles;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    @Override
    public int getCount() {
        return detalles.size();
    }

    @Override
    public LibroPrecio getItem(int position) {
        return detalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_precio), null);

        if(detalles != null && detalles.size() > position) {
            LibroPrecio detalle = detalles.get(position);
            ((TextView) convertView.findViewById(R.id.precio_item)).setText(detalle.getItem());
            ((TextView) convertView.findViewById(R.id.precio_descripcion)).setText(detalle.getDescripcion());
            ((TextView) convertView.findViewById(R.id.precio_medida)).setText(detalle.getMedida());
            ((TextView) convertView.findViewById(R.id.precio_precio)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getPrecio()));
        }
        else
        {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }
}
