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

import rp3.berlin.R;
import rp3.berlin.models.pedido.Stock;

/**
 * Created by magno_000 on 27/04/2017.
 */

public class StockAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Stock> detalles;
    private NumberFormat numberFormat;

    public StockAdapter(Context context, List<Stock> detalles) {
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
    public Stock getItem(int position) {
        return detalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_stock), null);

        if(detalles != null && detalles.size() > position) {
            Stock detalle = detalles.get(position);
            ((TextView) convertView.findViewById(R.id.stock_bodega)).setText(detalle.getBodegaDescripcion());
            ((TextView) convertView.findViewById(R.id.stock_disponible)).setText(numberFormat.format(detalle.getStockDisponible()));
            ((TextView) convertView.findViewById(R.id.stock_fisico)).setText(numberFormat.format(detalle.getStockFisico()));
        }
        else
        {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }

    public void setList(List<Stock> pedidoDetalles) {
        detalles = pedidoDetalles;
    }
}
