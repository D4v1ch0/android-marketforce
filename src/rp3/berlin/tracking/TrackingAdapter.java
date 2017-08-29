package rp3.berlin.tracking;

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

import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.pedido.EstadoCuenta;
import rp3.berlin.models.pedido.PedidoView;
import rp3.configuration.PreferenceManager;

/**
 * Created by Gustavo Meregildo on 25/08/2017.
 */

public class TrackingAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<PedidoView> detalles;
    private SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

    public TrackingAdapter(Context context, List<PedidoView> detalles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.detalles = detalles;

    }

    @Override
    public int getCount() {
        return detalles.size();
    }

    @Override
    public PedidoView getItem(int position) {
        return detalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_pedido_view), null);

        if(detalles != null && detalles.size() > position) {
            PedidoView detalle = detalles.get(position);
            ((TextView) convertView.findViewById(R.id.tracking_cliente)).setText(detalle.getNombreCliente());
            ((TextView) convertView.findViewById(R.id.tracking_pedido)).setText(detalle.getIdPedido() + "");

            ((TextView) convertView.findViewById(R.id.tracking_fecha)).setText(format1.format(detalle.getFechaIngreso()));
            ((TextView) convertView.findViewById(R.id.tracking_estado)).setText(detalle.getEstado());
            ((TextView) convertView.findViewById(R.id.tracking_agente)).setText(detalle.getAgente());
            ((TextView) convertView.findViewById(R.id.tracking_orden)).setText(detalle.getOrden());
            ((TextView) convertView.findViewById(R.id.tracking_estado_infor)).setText(detalle.getEstadoInfor());
            if(detalle.isBloqueado())
            {
                if(detalle.getAprobado() == -1)
                    ((TextView) convertView.findViewById(R.id.tracking_estado)).setText("Por Aprobar");
                if(detalle.getAprobado() == 0)
                    ((TextView) convertView.findViewById(R.id.tracking_estado)).setText("Rechazado");
            }
        }
        else
        {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }
}