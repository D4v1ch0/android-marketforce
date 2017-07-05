package rp3.berlin.dashboard;

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
import rp3.berlin.models.pedido.Meta;
import rp3.configuration.PreferenceManager;

/**
 * Created by magno_000 on 27/06/2017.
 */

public class MetasAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Meta> detalles;
    private DecimalFormat df;
    private NumberFormat numberFormat;
    private SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

    public MetasAdapter(Context context, List<Meta> detalles) {
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
    public Meta getItem(int position) {
        return detalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_metas), null);

        if (detalles != null && detalles.size() > position) {
            Meta detalle = detalles.get(position);
            ((TextView) convertView.findViewById(R.id.estado_cuenta_documento)).setText(detalle.getGrupoComisionDescripcion());
            ((TextView) convertView.findViewById(R.id.estado_cuenta_emision)).setText(numberFormat.format(detalle.getcReal()));
            ((TextView) convertView.findViewById(R.id.estado_cuenta_vence)).setText(numberFormat.format(detalle.getcPresupuestado()));
            ((TextView) convertView.findViewById(R.id.estado_cuenta_dias_vencido)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getReal()));
            ((TextView) convertView.findViewById(R.id.estado_cuenta_valor)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getPresupuestado()));
            ((TextView) convertView.findViewById(R.id.estado_cuenta_abono)).setText(numberFormat.format(detalle.getPorCumplir()) + "%");
            ((TextView) convertView.findViewById(R.id.meta_dist_varia)).setText(numberFormat.format(detalle.getPorcDisVaria()) + "%");
            ((TextView) convertView.findViewById(R.id.meta_a_pagar)).setText(numberFormat.format(detalle.getPorAPagas()) + "%");
            ((TextView) convertView.findViewById(R.id.meta_varia_distr)).setText(numberFormat.format(detalle.getVarDistri()));
            ((TextView) convertView.findViewById(R.id.estado_cuenta_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(detalle.getUsdTotal()));
        } else {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }
}