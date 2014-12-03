package rp3.marketforce.ruta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RutasMapaAdapter extends BaseAdapter {
	
	private List<Agenda> list;
	private LayoutInflater inflater;
	private Context ctx;
	private String hour_inicio="";
	private String str_range;
	private SimpleDateFormat format4;
	private Date date;
	
	public RutasMapaAdapter(Context c, List<Agenda> list)
	{
		this.list = list;
		ctx = c;
		inflater = LayoutInflater.from(c);
		format4= new SimpleDateFormat("HH:mm");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = (View) inflater.inflate(this.ctx.getApplicationContext().getResources().getLayout(R.layout.rowlist_rutas_mapa), null);
		
		Agenda agd = list.get(position);
		date = agd.getFechaInicio();
		 hour_inicio = format4.format(date);
		 str_range =hour_inicio;
		
		((TextView) convertView.findViewById(R.id.textView_horas)).setText(str_range);
		 
		((TextView) convertView.findViewById(R.id.textView_nombre)).setText(""+agd.getCliente().getNombreCompleto());
		
		if(agd.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
			((ImageView) convertView.findViewById(R.id.itemlist_rutas_estado)).setImageResource(R.drawable.circle_in_process);
		if(agd.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_NO_VISITADO))
			((ImageView) convertView.findViewById(R.id.itemlist_rutas_estado)).setImageResource(R.drawable.circle_unvisited);
		if(agd.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_PENDIENTE))
			((ImageView) convertView.findViewById(R.id.itemlist_rutas_estado)).setImageResource(R.drawable.circle_pending);
		if(agd.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
			((ImageView) convertView.findViewById(R.id.itemlist_rutas_estado)).setImageResource(R.drawable.circle_reprogramed);
		if(agd.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_VISITADO))
			((ImageView) convertView.findViewById(R.id.itemlist_rutas_estado)).setImageResource(R.drawable.circle_visited);
		
		
		if(agd.getClienteDireccion() != null)
			((TextView) convertView.findViewById(R.id.textView_address)).setText(""+agd.getClienteDireccion().getDireccion());
		
		return convertView;
	}

}
