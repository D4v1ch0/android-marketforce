package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.pedido.Categoria;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.models.pedido.SubCategoria;

/**
 * Created by magno_000 on 22/02/2016.
 */
public class ControlCajaAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ControlCaja> controles;
    private SimpleDateFormat format1, format2, format3, format5;

    public ControlCajaAdapter(Context context, List<ControlCaja> controles)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.controles = controles;
        format1 = new SimpleDateFormat("EEEE");
        format2 = new SimpleDateFormat("dd");
        format3 = new SimpleDateFormat("MMMM");
        format5 = new SimpleDateFormat("yyyy");
    }

    @Override
    public int getCount() {
        return controles.size();
    }

    @Override
    public ControlCaja getItem(int position) {
        return controles.get(position);

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_control_caja), null);

        ControlCaja controlCaja = controles.get(position);
        Calendar cal = Calendar.getInstance();
        cal.setTime(controlCaja.getFechaApertura());
        ((TextView) convertView.findViewById(R.id.control_list_fechas)).setText(format1.format(cal.getTime()) + ", " + format2.format(cal.getTime()) + " de " +
                format3.format(cal.getTime()) + " del " + format5.format(cal.getTime()));
        if(controlCaja.getFechaCierre() != null && controlCaja.getFechaCierre().getTime() <= 0)
            ((TextView) convertView.findViewById(R.id.control_list_activo)).setText("Activo");


        return convertView;
    }
}
