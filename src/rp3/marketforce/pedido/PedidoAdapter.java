package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.pedido.Pedido;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Pedido> pedidos;
    private int id_icon;
    private int id_color;

    public PedidoAdapter(Context context, List<Pedido> pedidos)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pedidos = pedidos;
    }

    @Override
    public int getCount() {
        return pedidos.size();
    }

    @Override
    public Pedido getItem(int position) {
        return pedidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_pedido), null);

        Pedido pedido = pedidos.get(position);
        if (pedido.getEstado().equals("P")) {
            ((ImageView) convertView.findViewById(R.id.pedido_estado)).setImageDrawable(context.getResources().getDrawable(R.drawable.circle_pending));
        } else {
            ((ImageView) convertView.findViewById(R.id.pedido_estado)).setImageDrawable(context.getResources().getDrawable(R.drawable.circle_visited));
        }

        ((TextView) convertView.findViewById(R.id.pedido_cliente)).setText(pedido.getCliente().getNombreCompleto());
        ((TextView) convertView.findViewById(R.id.pedido_cantidad)).setText(pedido.getPedidoDetalles().size());
        ((TextView) convertView.findViewById(R.id.pedido_valor)).setText("$" + pedido.getValorTotal());

        return convertView;
    }

}
