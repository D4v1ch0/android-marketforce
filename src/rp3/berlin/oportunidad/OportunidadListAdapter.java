package rp3.berlin.oportunidad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import rp3.berlin.R;
import rp3.berlin.models.oportunidad.Oportunidad;
import rp3.util.CalendarUtils;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadListAdapter extends BaseExpandableListAdapter {

    private boolean action = true;
    private LayoutInflater inflater;
    private Context contex;
    private List<String> listHeader;
    private HashMap<String, List<Oportunidad>> list_oportunidad;
    private OportunidadListFragment.OportunidadListFragmentListener transactionListFragmentCallback;
    private SimpleDateFormat format1;
    private NumberFormat numberFormat;

    public OportunidadListAdapter(Context c, HashMap<String, List<Oportunidad>> list_oportunidad, List<String> listHeader,
                                  OportunidadListFragment.OportunidadListFragmentListener transactionListFragmentCallback) {
        this.inflater = LayoutInflater.from(c);
        this.contex = c;
        this.list_oportunidad = list_oportunidad;
        this.transactionListFragmentCallback = transactionListFragmentCallback;
        this.listHeader = listHeader;
        numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setMinimumFractionDigits(0);
        format1 = new SimpleDateFormat("dd/MM/yy");
    }

    public void changeList(HashMap<String, List<Oportunidad>> list_oportunidad) {
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

    public void setList(HashMap<String, List<Oportunidad>> lista) {
        list_oportunidad = lista;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list_oportunidad.get(listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list_oportunidad.get(listHeader.get(groupPosition)).get(childPosition);
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
        convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.rowgroup_oportunidad), null);
        ((TextView) convertView.findViewById(R.id.lblListHeader)).setText(listHeader.get(groupPosition));
        ((TextView) convertView.findViewById(R.id.group_oportunidad_numero)).setText(list_oportunidad.get(listHeader.get(groupPosition)).size() + " Item(s)");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Oportunidad opt = list_oportunidad.get(listHeader.get(groupPosition)).get(childPosition);
        convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.rowlist_oportunidad), null);

        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_nombre)).setText(opt.getDescripcion());
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_probabilidad)).setText("Probabilidad: " + opt.getProbabilidad() + "%");
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_agente)).setText(opt.getAgente().getNombre());
        ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_number)).setText((childPosition + 1)+ "");
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
        if(opt.getEstado().equalsIgnoreCase("C")) {
            convertView.findViewById(R.id.rowlist_oportunidad_etapa1).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa1));
            convertView.findViewById(R.id.rowlist_oportunidad_etapa2).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa2));
            convertView.findViewById(R.id.rowlist_oportunidad_etapa3).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa3));
            convertView.findViewById(R.id.rowlist_oportunidad_etapa4).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa4));
            convertView.findViewById(R.id.rowlist_oportunidad_etapa5).setBackgroundColor(contex.getResources().getColor(R.color.color_etapa5));
        }

        if(opt.getMaxEtapas() < 5)
            convertView.findViewById(R.id.rowlist_oportunidad_etapa5).setVisibility(View.INVISIBLE);
        if(opt.getMaxEtapas() < 4)
            convertView.findViewById(R.id.rowlist_oportunidad_etapa4).setVisibility(View.INVISIBLE);
        if(opt.getMaxEtapas() < 3)
            convertView.findViewById(R.id.rowlist_oportunidad_etapa3).setVisibility(View.INVISIBLE);
        if(opt.getMaxEtapas() < 2)
            convertView.findViewById(R.id.rowlist_oportunidad_etapa2).setVisibility(View.INVISIBLE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}