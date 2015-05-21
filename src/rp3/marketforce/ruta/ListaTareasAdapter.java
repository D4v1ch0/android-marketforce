package rp3.marketforce.ruta;

import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListaTareasAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private List<AgendaTarea> agendaTarea;
	private int id_icon;
    private int id_color;

    public ListaTareasAdapter(Context context, List<AgendaTarea> agendaTarea)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.agendaTarea = agendaTarea;
	}
	
	@Override
	public int getCount() {
		return agendaTarea.size();
	}

	@Override
	public AgendaTarea getItem(int position) {
		return agendaTarea.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_tarea), null);


        if (agendaTarea.get(position).getEstadoTareaDescripcion().equals("Pendiente")) {
            id_icon = R.drawable.checkbox_off;
        } else {
            id_icon = R.drawable.checkbox_on;
        }

        ((TextView) convertView.findViewById(R.id.map_phone)).setCompoundDrawablesWithIntrinsicBounds(0, 0, id_icon, 0);
        //((TextView) convertView.findViewById(R.id.map_phone)).setBackgroundColor(context.getResources().getColor(id_color));
        //((TextView) convertView.findViewById(R.id.map_phone)).setText(agendaTarea.get(position).getEstadoTareaDescripcion());

        ((TextView) convertView.findViewById(R.id.detail_agenda_estado)).setText(agendaTarea.get(position).getNombreTarea());
        ((TextView) convertView.findViewById(R.id.detail_tarea_num)).setText(position + 1 + "");

        return convertView;
    }

}
