package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.pedido.Pedido;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoAdapter extends BaseAdapter {

    private final SimpleDateFormat format1, format2, format3, format5;
    private Context context;
    private LayoutInflater inflater;
    private List<Pedido> pedidos;
    private NumberFormat numberFormat;

    public PedidoAdapter(Context context, List<Pedido> pedidos)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pedidos = pedidos;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        format1 = new SimpleDateFormat("EEEE");
        format2 = new SimpleDateFormat("dd");
        format3 = new SimpleDateFormat("MMMM");
        format5 = new SimpleDateFormat("yyyy");
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
        } else if (pedido.getEstado().equals("C")) {
            ((ImageView) convertView.findViewById(R.id.pedido_estado)).setImageDrawable(context.getResources().getDrawable(R.drawable.circle_visited));
        }

        ((TextView) convertView.findViewById(R.id.pedido_cliente)).setText(pedido.getCliente().getNombreCompleto());
        ((TextView) convertView.findViewById(R.id.pedido_fecha)).setText(format1.format(pedido.getFechaCreacion()) + ", " + format2.format(pedido.getFechaCreacion()) + " de " + format3.format(pedido.getFechaCreacion()) + " del " + format5.format(pedido.getFechaCreacion()));
        ((TextView) convertView.findViewById(R.id.pedido_numero_documento)).setText(pedido.getNumeroDocumento());
        if(pedido.getTipoDocumento().equalsIgnoreCase("FA"))
            ((TextView) convertView.findViewById(R.id.pedido_tipo_documento)).setText("Factura");
        if(pedido.getTipoDocumento().equalsIgnoreCase("NC"))
            ((TextView) convertView.findViewById(R.id.pedido_tipo_documento)).setText("Nota de Crédito");
        ((TextView) convertView.findViewById(R.id.pedido_items)).setText(CrearPedidoFragment.getPedidoCantidad(pedido.getPedidoDetalles()) + "");
        ((TextView) convertView.findViewById(R.id.pedido_valor)).setText("$ " + numberFormat.format(pedido.getValorTotal()));

        return convertView;
    }

}
