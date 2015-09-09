package rp3.marketforce.oportunidad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadBitacora;
import rp3.util.CalendarUtils;

/**
 * Created by magno_000 on 07/09/2015.
 */
public class OportunidadBitacoraAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context contex;
    private List<OportunidadBitacora> list_bitacora;
    private SimpleDateFormat format1;

    public OportunidadBitacoraAdapter(Context c, List<OportunidadBitacora> list_bitacora) {
        this.inflater = LayoutInflater.from(c);
        this.contex = c;
        this.list_bitacora = list_bitacora;
        format1 = new SimpleDateFormat("dd/MM/yy HH:mm");
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OportunidadBitacora opt = list_bitacora.get(position);
        convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.rowlist_oportunidad_bitacora), null);

        ((TextView) convertView.findViewById(R.id.bitacora_agente)).setText(opt.getAgente().getNombre());
        ((TextView) convertView.findViewById(R.id.bitacora_detalle)).setText(opt.getDetalle());
        ((TextView) convertView.findViewById(R.id.bitacora_fecha)).setText(format1.format(opt.getFecha()));
        return convertView;
    }

    public void changeList(List<OportunidadBitacora> list_bitacora) {
        this.list_bitacora = list_bitacora;
        notifyDataSetChanged();
    }

    @SuppressLint("DefaultLocale")
    private String capitalizeFirstLetter(String original) {
        if (original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_bitacora.size();
    }

    @Override
    public OportunidadBitacora getItem(int position) {
        // TODO Auto-generated method stub
        return list_bitacora.get(position);
    }

    public void setList(List<OportunidadBitacora> lista) {
        list_bitacora = lista;
    }
}
