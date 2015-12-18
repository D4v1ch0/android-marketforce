package rp3.marketforce.pedido;

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
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.Pago;

/**
 * Created by magno_000 on 17/12/2015.
 */
public class ArqueoCajaAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Pago> pagos;
    private NumberFormat numberFormat;

    public ArqueoCajaAdapter(Context context, List<Pago> pagos) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pagos = pagos;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    @Override
    public int getCount() {
        return pagos.size();
    }

    @Override
    public Pago getItem(int position) {
        return pagos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_arqueo_caja), null);

        Pago pago = pagos.get(position);

        ((TextView) convertView.findViewById(R.id.arqueo_descripcion)).setText(pago.getFormaPago().getDescripcion());
        ((TextView) convertView.findViewById(R.id.arqueo_valor)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pago.getValor()));
        ((TextView) convertView.findViewById(R.id.arqueo_cantidad)).setText(pago.getIdPago() + "");

        return convertView;
    }
}