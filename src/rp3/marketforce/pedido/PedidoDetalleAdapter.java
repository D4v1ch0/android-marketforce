package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.PedidoDetalle;

/**
 * Created by magno_000 on 13/10/2015.
 */
public class PedidoDetalleAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<PedidoDetalle> detalles;
    private int id_icon;
    private int id_color;
    private DecimalFormat df;

    public PedidoDetalleAdapter(Context context, List<PedidoDetalle> detalles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.detalles = detalles;
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
    }

    @Override
    public int getCount() {
        return detalles.size();
    }

    @Override
    public PedidoDetalle getItem(int position) {
        return detalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_pedido_detalle), null);

        PedidoDetalle detalle = detalles.get(position);

        ((TextView) convertView.findViewById(R.id.pedido_detalle_descripcion)).setText(detalle.getDescripcion() + " x " + detalle.getCantidad());
        ((TextView) convertView.findViewById(R.id.pedido_detalle_unitario)).setText("$ " + df.format(detalle.getValorUnitario()));
        ((TextView) convertView.findViewById(R.id.pedido_detalle_valor_total)).setText("$ " + df.format(detalle.getValorTotal()));

        return convertView;
    }

    public void setList(List<PedidoDetalle> pedidoDetalles) {
        detalles = pedidoDetalles;
    }
}