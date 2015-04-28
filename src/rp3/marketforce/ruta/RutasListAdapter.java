package rp3.marketforce.ruta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.ruta.RutasListFragment.TransactionListFragmentListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class RutasListAdapter extends BaseAdapter{
	
	private boolean action = true;
	private LayoutInflater inflater;
	private Context contex;
	private ArrayList<Agenda> one_list;
	private List<Agenda> list_agenda;
	private ArrayList<String> header;
	private TransactionListFragmentListener transactionListFragmentCallback;
	private String hour_inicio="";
	private String hour_fin="";
	private String str_range;
	private SimpleDateFormat format1;
	private SimpleDateFormat format4;
	private Date date;
	private int row_ = -1;
	private int section_ = -1;
	
	public RutasListAdapter(Context c, List<Agenda> list_agenda ,TransactionListFragmentListener transactionListFragmentCallback){
		this.inflater = LayoutInflater.from(c);
		this.contex = c;
		this.list_agenda = list_agenda;
		this.transactionListFragmentCallback = transactionListFragmentCallback;
		format1 = new SimpleDateFormat("EEEE dd MMMM yyyy");
		format4= new SimpleDateFormat("HH:mm");
	}
	
	@Override
	public long getItemId(int pos) {			
		return 0;
	}
	 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		Agenda agd = list_agenda.get(position);
		
		if(agd.getNombreCompleto() == null)
		{
			convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.headerlist_ruta_list), null);	
			((TextView) convertView.findViewById(R.id.textView_headerlist_ruta_list)).setText(format1.format(agd.getFechaInicio()));
		}
		else
		{
		
			convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.rowlist_rutas_list), null);
			
			date = agd.getFechaInicio();
			 hour_inicio = format4.format(date);
			 hour_fin = format4.format(agd.getFechaFin());
			 //str_range =hour_inicio+" - "+hour_fin;
			 str_range =hour_inicio;
			
			((TextView) convertView.findViewById(R.id.textView_horas)).setText(str_range);
			if(agd.getIdContacto() == 0)
			{
				((TextView) convertView.findViewById(R.id.textView_nombre)).setText(""+agd.getNombreCompleto().trim());
				try
				{
					((TextView) convertView.findViewById(R.id.textView_cargo_canal)).setText(""+agd.getCliente().getCanalDescripcion());
				}
				catch(Exception ex)
				{
				}
			}
			else
			{
				String apellido = "";
				String cargo = "";
				if(agd.getContacto().getApellido() != null)
					apellido = agd.getContacto().getApellido();
				if(agd.getContacto().getCargo() != null)
					cargo = agd.getContacto().getCargo() + " - ";
				((TextView) convertView.findViewById(R.id.textView_nombre)).setText(""+agd.getContacto().getNombre() + " " + apellido);
				((TextView) convertView.findViewById(R.id.textView_cargo_canal)).setText(""+cargo + agd.getNombreCompleto());
			}
			
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
			
			//convertView.setBackgroundResource(R.drawable.border_bottom);
		}
		return convertView;
	}
	
	public void changeList(List<Agenda> list_agenda)
	{
		this.list_agenda = list_agenda;
		notifyDataSetChanged();
	}
	
	@SuppressLint("DefaultLocale")
	private String capitalizeFirstLetter(String original){
	    if(original.length() == 0)
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
		return list_agenda.size();
	}

	@Override
	public Agenda getItem(int position) {
		// TODO Auto-generated method stub
		return list_agenda.get(position);
	}	
}