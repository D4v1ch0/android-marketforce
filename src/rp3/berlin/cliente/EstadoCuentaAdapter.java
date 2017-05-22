package rp3.berlin.cliente;

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
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.pedido.EstadoCuenta;

/**
 * Created by magno_000 on 04/05/2017.
 */

public class EstadoCuentaAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<EstadoCuenta> detalles;
    private DecimalFormat df;
    private NumberFormat numberFormat;
    private SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

    public EstadoCuentaAdapter(Context context, List<EstadoCuenta> detalles) {
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
    public EstadoCuenta getItem(int position) {
        return detalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_estado_cuenta), null);

        if(detalles != null && detalles.size() > position) {
            EstadoCuenta detalle = detalles.get(position);
            ((TextView) convertView.findViewById(R.id.estado_cuenta_documento)).setText(detalle.getDocumento());
            ((TextView) convertView.findViewById(R.id.estado_cuenta_emision)).setText(format1.format(detalle.getFechaEmision()));
            if(detalle.getFechaVencimiento() != null)
                ((TextView) convertView.findViewById(R.id.estado_cuenta_vence)).setText(format1.format(detalle.getFechaVencimiento()));
            ((TextView) convertView.findViewById(R.id.estado_cuenta_dias_vencido)).setText(detalle.getDiasVencidos() + "");
            ((TextView) convertView.findViewById(R.id.estado_cuenta_valor)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getValor()));

            ((TextView) convertView.findViewById(R.id.estado_cuenta_abono)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getAbono()));
            ((TextView) convertView.findViewById(R.id.estado_cuenta_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getSaldo()));
        }
        else
        {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }
}
