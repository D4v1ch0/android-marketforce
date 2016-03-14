package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.pedido.Pedido;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoAdapter extends BaseExpandableListAdapter {

    private final SimpleDateFormat format1, format2, format3, format5;
    private Context context;
    private LayoutInflater inflater;
    private List<String> listHeader, identifiers;
    private HashMap<String, List<Pedido>> pedidos;
    private NumberFormat numberFormat;
    private int group = -1, child = -1;

    public PedidoAdapter(Context context, HashMap<String, List<Pedido>> pedidos, List<String> listHeader, List<String> identifiers)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pedidos = pedidos;
        this.listHeader = listHeader;
        this.identifiers = identifiers;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        format1 = new SimpleDateFormat("EEEE");
        format2 = new SimpleDateFormat("dd");
        format3 = new SimpleDateFormat("MMMM");
        format5 = new SimpleDateFormat("yyyy");
    }

    public HashMap<String, List<Pedido>> getPedidos() {
        return pedidos;
    }

    public void setPedidos(HashMap<String, List<Pedido>> pedidos) {
        this.pedidos = pedidos;
    }

    public void setChildAndGroup(int group, int child)
    {
        this.child = child;
        this.group = group;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return pedidos.get(identifiers.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Pedido getChild(int groupPosition, int childPosition) {
        return pedidos.get(identifiers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowgroup_pedido), null);
        ((TextView) convertView.findViewById(R.id.lblListHeader)).setText(listHeader.get(groupPosition));
        ((TextView) convertView.findViewById(R.id.group_pedido_numero)).setText(pedidos.get(identifiers.get(groupPosition)).size() + " Item(s)");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_pedido), null);

        Pedido pedido = pedidos.get(identifiers.get(groupPosition)).get(childPosition);
        if (pedido.getEstado().equals("P")) {
            ((ImageView) convertView.findViewById(R.id.pedido_estado)).setImageDrawable(context.getResources().getDrawable(R.drawable.circle_pending));
        } else if (pedido.getEstado().equals("C")) {
            ((ImageView) convertView.findViewById(R.id.pedido_estado)).setImageDrawable(context.getResources().getDrawable(R.drawable.circle_visited));
        }

        ((TextView) convertView.findViewById(R.id.pedido_cliente)).setText(pedido.getNombre());
        ((TextView) convertView.findViewById(R.id.pedido_fecha)).setText(format1.format(pedido.getFechaCreacion()) + ", " + format2.format(pedido.getFechaCreacion()) + " de " + format3.format(pedido.getFechaCreacion()) + " del " + format5.format(pedido.getFechaCreacion()));
        ((TextView) convertView.findViewById(R.id.pedido_numero_documento)).setText(pedido.getNumeroDocumento());
        ((TextView) convertView.findViewById(R.id.pedido_tipo_documento)).setText(pedido.getTransaccion().getValue());
        ((TextView) convertView.findViewById(R.id.pedido_items)).setText(CrearPedidoFragment.getPedidoCantidad(pedido.getPedidoDetalles()) + "");
        ((TextView) convertView.findViewById(R.id.pedido_valor)).setText("$ " + numberFormat.format(pedido.getValorTotal()));

        if(childPosition == child && groupPosition == group)
        {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.color_background_selector));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
