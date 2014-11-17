package rp3.marketforce.ruta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientFragment;
import rp3.marketforce.headerlistview.SectionAdapter;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.ruta.RutasListFragment.TransactionListFragmentListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class RutasListAdapter extends SectionAdapter{
	
	private LayoutInflater inflater;
	private Context contex;
	private ArrayList<ArrayList<Agenda>> list_agenda;
	private ArrayList<String> header;
	private TransactionListFragmentListener transactionListFragmentCallback;
	private String hour_inicio="";
	private String hour_fin="";
	private String str_range;
	private SimpleDateFormat format4;
	private Date date;
	private int row_ = -1;
	private int section_ = -1;
	
	public RutasListAdapter(Context c, ArrayList<ArrayList<Agenda>> list_agenda ,TransactionListFragmentListener transactionListFragmentCallback,
			ArrayList<String> header){
		this.inflater = LayoutInflater.from(c);
		this.contex = c;
		this.list_agenda = list_agenda;
		this.header = header;
		this.transactionListFragmentCallback = transactionListFragmentCallback;
		
		 format4= new SimpleDateFormat("HH:mm");
	}
	
	@Override
	public Object getItem(int pos) {	
		return null;
	}
	
	@Override
	public long getItemId(int pos) {			
		return 0;
	}

	@Override
	public int numberOfSections() {
		return header.size();
	}

	@Override
	public int numberOfRows(int section) {
		return list_agenda.get(section).size();
	}
	
	 @Override
     public boolean hasSectionHeaderView(int section) {
         return true;
     }
	
	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		
		
//		if (convertView == null)
	      convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.headerlist_ruta_list), null);
//		
		((TextView) convertView.findViewById(R.id.textView_headerlist_client_list)).setText(""+header.get(section));
		
		return convertView;
	}

	@Override
	public View getRowView(int section, int row, View convertView,
			ViewGroup parent) {
		
		 
//		if (convertView == null)
	        convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.rowlist_rutas_list), null);
		
		Agenda agd = list_agenda.get(section).get(row);
		date = agd.getFechaInicio();
		 hour_inicio = format4.format(date);
		 hour_fin = format4.format(agd.getFechaFin());
		 //str_range =hour_inicio+" - "+hour_fin;
		 str_range =hour_inicio;
		
		((TextView) convertView.findViewById(R.id.textView_horas)).setText(str_range);
		 
		((TextView) convertView.findViewById(R.id.textView_nombre)).setText(""+agd.getNombreCompleto());
		
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
		
		View vi = (View) convertView.findViewById(R.id.view_vertical);
		if(section == section_)
			vi.setVisibility(View.VISIBLE);
		else
			vi.setVisibility(View.GONE);
			
		
		convertView.setBackgroundResource(R.drawable.border_bottom);
		if(transactionListFragmentCallback.allowSelectedItem())
			if (section == section_)
				if (row == row_)
					convertView.setBackgroundColor(contex.getResources().getColor(R.color.color_background_selector));
		
		return convertView;
	}
	
	@Override
    public int getSectionHeaderViewTypeCount() {
        return 2;
    }

    @Override
    public int getSectionHeaderItemViewType(int section) {
        return section % 2;
    }

	@Override
	public Object getRowItem(int section, int row) {
		return null;
	}
	
	public void setNewList(ArrayList<ArrayList<Agenda>> new_list_agenda)
	{
		this.list_agenda = new_list_agenda;
		notifyDataSetChanged();
	}
	
	@Override
    public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
        super.onRowItemClick(parent, view, section, row, id);
        
        RutasListFragment.SECTION = section;
        section_= section;
    	row_ = row;
        
        if(transactionListFragmentCallback.allowSelectedItem())
        {
        	notifyDataSetChanged();
        }
        
        if(transactionListFragmentCallback != null)
        	transactionListFragmentCallback.onTransactionSelected(list_agenda.get(section).get(row).getID());
       }
	
	@SuppressLint("DefaultLocale")
	private String capitalizeFirstLetter(String original){
	    if(original.length() == 0)
	        return original;
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	
}