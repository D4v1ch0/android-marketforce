package rp3.auna.pedido;
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
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.pedido.Producto;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class ProductoAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Producto> productos;
    private NumberFormat numberFormat;

    public ProductoAdapter(Context context, List<Producto> productos)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.productos = productos;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Producto getItem(int position) {
        return productos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_producto), null);

        Producto producto = productos.get(position);
        String impuesto = "";
        if(producto.getPorcentajeImpuesto() == 0)
            impuesto = "*";

        ((TextView) convertView.findViewById(R.id.producto_descripcion)).setText(producto.getDescripcion() + impuesto);
        ((TextView) convertView.findViewById(R.id.producto_precio)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(producto.getValorUnitario()));
        ((TextView) convertView.findViewById(R.id.producto_codigo)).setText("SKU: " + producto.getCodigoExterno());

        return convertView;
    }

}