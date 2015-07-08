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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.oportunidad.Etapa;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.ruta.RutasListFragment;
import rp3.util.CalendarUtils;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadListAdapter extends BaseAdapter {

    private boolean action = true;
    private LayoutInflater inflater;
    private Context contex;
    private List<Oportunidad> list_oportunidad;
    private OportunidadListFragment.OportunidadListFragmentListener transactionListFragmentCallback;
    private SimpleDateFormat format1;
    private NumberFormat numberFormat;

    public OportunidadListAdapter(Context c, List<Oportunidad> list_oportunidad, OportunidadListFragment.OportunidadListFragmentListener transactionListFragmentCallback) {
        this.inflater = LayoutInflater.from(c);
        this.contex = c;
        this.list_oportunidad = list_oportunidad;
        this.transactionListFragmentCallback = transactionListFragmentCallback;
        numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setMinimumFractionDigits(0);
        format1 = new SimpleDateFormat("dd/MM/yy");
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Oportunidad opt = list_oportunidad.get(position);
        convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.rowlist_oportunidad), null);

        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_nombre)).setText(opt.getDescripcion());
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_probabilidad)).setText("Probabilidad: " + opt.getProbabilidad() + "%");
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_agente)).setText(opt.getAgente().getNombre());
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_number)).setText((position + 1)+ "");
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_importe)).setText(numberFormat.format(opt.getImporte()));
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_contactado)).setText("Contactado: " + format1.format(opt.getFechaCreacion()));
        if(opt.getEstado().equalsIgnoreCase("S"))
            ((ImageView) convertView.findViewById(R.id.rowlist_oportunidad_prioridad)).setImageResource(R.drawable.blue_flag);
        if(opt.getEstado().equalsIgnoreCase("C"))
            ((ImageView) convertView.findViewById(R.id.rowlist_oportunidad_prioridad)).setImageResource(R.drawable.green_flag);
        if(opt.getEstado().equalsIgnoreCase("NC"))
            ((ImageView) convertView.findViewById(R.id.rowlist_oportunidad_prioridad)).setImageResource(R.drawable.gray_flag);
        Calendar cal = Calendar.getInstance();
        Calendar calFecha = Calendar.getInstance();
        calFecha.setTime(opt.getFechaCreacion());
        long dias = CalendarUtils.DayDiff(cal, calFecha);
        if(dias == 0 && cal.get(Calendar.DAY_OF_YEAR) != calFecha.get(Calendar.DAY_OF_YEAR))
            dias = 1;
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_dias)).setText("Días transcurridos: " + dias);
        ((RatingBar) convertView.findViewById(R.id.rowlist_oportunidad_calificacion)).setRating(opt.getCalificacion());
        if(opt.getEtapa().getOrden() > 1)
            convertView.findViewById(R.id.rowlist_oportunidad_etapa1).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa1));
        if(opt.getEtapa().getOrden() > 2)
            convertView.findViewById(R.id.rowlist_oportunidad_etapa2).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa2));
        if(opt.getEtapa().getOrden() > 3)
            convertView.findViewById(R.id.rowlist_oportunidad_etapa3).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa3));
        if(opt.getEtapa().getOrden() > 4)
            convertView.findViewById(R.id.rowlist_oportunidad_etapa4).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa4));
        if(opt.getEstado().equalsIgnoreCase("C"))
            convertView.findViewById(R.id.rowlist_oportunidad_etapa5).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa5));

        return convertView;
    }

    public void changeList(List<Oportunidad> list_oportunidad) {
        this.list_oportunidad = list_oportunidad;
        notifyDataSetChanged();
    }

    @SuppressLint("DefaultLocale")
    private String capitalizeFirstLetter(String original) {
        if (original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_oportunidad.size();
    }

    @Override
    public Oportunidad getItem(int position) {
        // TODO Auto-generated method stub
        return list_oportunidad.get(position);
    }
}