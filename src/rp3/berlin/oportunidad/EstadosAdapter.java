package rp3.berlin.oportunidad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import rp3.data.models.GeneralValue;
import rp3.berlin.R;

/**
 * Created by magno_000 on 28/05/2015.
 */
public class EstadosAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GeneralValue> estados;
    private int id_icon;
    private int id_color;
    private int subetapa;
    private int subtarea;

    public EstadosAdapter(Context context, List<GeneralValue> estados)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.estados = estados;
    }

    @Override
    public int getCount() {
        return estados.size();
    }

    @Override
    public GeneralValue getItem(int position) {
        return estados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_estado), null);

        if (estados.get(position).getCode().equalsIgnoreCase("A"))
            ((TextView) convertView.findViewById(R.id.rowlist_estado)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_flag, 0, 0, 0);
        if (estados.get(position).getCode().equalsIgnoreCase("S"))
            ((TextView) convertView.findViewById(R.id.rowlist_estado)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.blue_flag, 0, 0, 0);
        if (estados.get(position).getCode().equalsIgnoreCase("NC"))
            ((TextView) convertView.findViewById(R.id.rowlist_estado)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.gray_flag, 0, 0, 0);

        ((TextView) convertView.findViewById(R.id.rowlist_estado)).setText(estados.get(position).getValue());


        return convertView;
    }
}
