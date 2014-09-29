package rp3.marketforce;

import java.util.List;

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
	private String estado;
	
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
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		  convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_tarea), null);
		
		  
//		  if(agendaTarea.get(position).getTipoTarea() == 1)
//		  {
//			  id_icon = R.drawable.ic_action_accept;
//			  estado = "Visitado";
//		  }
//		  else
//		  {
//			  id_icon = R.drawable.ic_action_cancel;
//			  estado = "Pendiente";
//		  }
		  
			((TextView) convertView.findViewById(R.id.textView2)).setCompoundDrawablesWithIntrinsicBounds(0, 0, id_icon, 0);
			((TextView) convertView.findViewById(R.id.textView2)).setText(estado);
			
			((TextView) convertView.findViewById(R.id.textView1)).setText(""+agendaTarea.get(position).getNombreTarea());
		
		return convertView;
	}

}
