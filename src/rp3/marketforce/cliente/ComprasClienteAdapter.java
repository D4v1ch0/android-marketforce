package rp3.marketforce.cliente;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.ComprasCliente;
import rp3.marketforce.models.pedido.EstadoCuenta;

/**
 * Created by magno_000 on 05/05/2017.
 */

public class ComprasClienteAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ComprasCliente> detalles;
    private DecimalFormat df;
    private NumberFormat numberFormat;
    private SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

    public ComprasClienteAdapter(Context context, List<ComprasCliente> detalles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.detalles = detalles;
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    @Override
    public int getCount() {
        return detalles.size();
    }

    @Override
    public ComprasCliente getItem(int position) {
        return detalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_compras_cliente), null);

        if(detalles != null && detalles.size() > position) {
            ComprasCliente detalle = detalles.get(position);
            ((TextView) convertView.findViewById(R.id.compras_cliente_cantidad)).setText(detalle.getCantidad()+ "");
            ((TextView) convertView.findViewById(R.id.compras_cliente_item)).setText(detalle.getItem() + " - " + detalle.getDescripcion());
            ((TextView) convertView.findViewById(R.id.compras_cliente_precio)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getPrecio()));
            ((TextView) convertView.findViewById(R.id.compras_cliente_fecha)).setText(format1.format(detalle.getFechaCompra()));
            ((TextView) convertView.findViewById(R.id.compras_cliente_stock)).setText(detalle.getStock() + "");
        }
        else
        {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }
}
