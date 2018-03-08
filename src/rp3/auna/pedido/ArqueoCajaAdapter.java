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
import rp3.auna.models.pedido.Pago;

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

        if(pago.getIdFormaPago() == 0)
            ((TextView) convertView.findViewById(R.id.arqueo_descripcion)).setText("(-) Nota de Crédito");
        else if(pago.getIdFormaPago() == -1)
            ((TextView) convertView.findViewById(R.id.arqueo_descripcion)).setText("Apertura");
        else if(pago.getIdFormaPago() == -2) {
            convertView.findViewById(R.id.arqueo_descripcion).setPadding(12, 0, 0, 0);
            ((TextView) convertView.findViewById(R.id.arqueo_descripcion)).setText(pago.getBancoDescripcion());
        }
        else if(pago.getIdFormaPago() == -3) {
            convertView.findViewById(R.id.arqueo_descripcion).setPadding(12, 0, 0, 0);
            ((TextView) convertView.findViewById(R.id.arqueo_descripcion)).setText(pago.getBancoDescripcion() + " - " + pago.getMarcaTarjetaDescripcion());
        }
        else
            ((TextView) convertView.findViewById(R.id.arqueo_descripcion)).setText(pago.getFormaPago().getDescripcion());
        ((TextView) convertView.findViewById(R.id.arqueo_valor)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pago.getValor()));
        ((TextView) convertView.findViewById(R.id.arqueo_cantidad)).setText(pago.getIdPago() + "");

        return convertView;
    }
}