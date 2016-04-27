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
import java.text.NumberFormat;
import java.util.List;

import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
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
    private DecimalFormat df;
    private NumberFormat numberFormat;
    private boolean isDetail = true;

    public PedidoDetalleAdapter(Context context, List<PedidoDetalle> detalles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.detalles = detalles;
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    public boolean isDetail() {
        return isDetail;
    }

    public void setIsDetail(boolean isDetail) {
        this.isDetail = isDetail;
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

        ((TextView) convertView.findViewById(R.id.pedido_detalle_sku)).setText(detalle.getCodigoExterno());
        ((TextView) convertView.findViewById(R.id.pedido_detalle_descuento)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getValorDescuentoAutomaticoTotal() + detalle.getValorDescuentoManualTotal()));
        ((TextView) convertView.findViewById(R.id.pedido_detalle_impuesto)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getValorImpuestoTotal()));
        ((TextView) convertView.findViewById(R.id.pedido_detalle_descripcion)).setText(detalle.getDescripcion());
        ((TextView) convertView.findViewById(R.id.pedido_detalle_cantidad)).setText((detalle.getCantidad())+"");

        ((TextView) convertView.findViewById(R.id.pedido_detalle_unitario)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getValorUnitario()));
        ((TextView) convertView.findViewById(R.id.pedido_detalle_valor_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getValorTotal()));

        return convertView;
    }

    public void setList(List<PedidoDetalle> pedidoDetalles) {
        detalles = pedidoDetalles;
    }
}