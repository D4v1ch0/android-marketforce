package rp3.marketforce.ruta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rp3.marketforce.R;
import rp3.marketforce.R.color;
import rp3.marketforce.R.id;
import rp3.marketforce.R.layout;
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
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class RutasListAdapter extends SectionAdapter{
	
	private LayoutInflater inflater;
	private Context contex;
	private ArrayList<ArrayList<Agenda>> list_agenda;
	private ArrayList<String> header;
	private TransactionListFragmentListener transactionListFragmentCallback;
	private String day_week= "";
	private String day = "";
	private String month = "";
	private String hour_inicio="";
	private String hour_fin="";
	private String str_range;
	private SimpleDateFormat format1; 
	private SimpleDateFormat format2; 
	private SimpleDateFormat format3;
	private SimpleDateFormat format4;
	private Date date;
	private int section_ = -1;
	private int row_ = -1;
	
	public RutasListAdapter(Context c, ArrayList<ArrayList<Agenda>> list_agenda ,TransactionListFragmentListener transactionListFragmentCallback,
			ArrayList<String> header){
		this.inflater = LayoutInflater.from(c);
		this.contex = c;
		this.list_agenda = list_agenda;
		this.header = header;
		this.transactionListFragmentCallback = transactionListFragmentCallback;
		
		 format1 = new SimpleDateFormat("EEEE"); 
		 format2 = new SimpleDateFormat("dd"); 
		 format3= new SimpleDateFormat("MMMM");
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
	      convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.headerlist_client_list), null);
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
		 day_week = format1.format(date);
		 day = format2.format(date);
		 month = format3.format(date);
		 
		 hour_inicio = format4.format(date);
		 hour_fin = format4.format(agd.getFechaFin());
		 str_range =hour_inicio+" - "+hour_fin;
		
		((TextView) convertView.findViewById(R.id.textView_dia)).setText(""+capitalizeFirstLetter(day_week));
		((TextView) convertView.findViewById(R.id.textView_num_dia)).setText(""+day);
		((TextView) convertView.findViewById(R.id.textView_mes)).setText(""+capitalizeFirstLetter(month));
		((TextView) convertView.findViewById(R.id.textView_estado)).setText(""+agd.getEstadoAgenda());
		((TextView) convertView.findViewById(R.id.textView_horas)).setText(str_range);
		 
		
		if(agd.getCliente() != null)
		{
			((TextView) convertView.findViewById(R.id.textView_nombre)).setText(""+agd.getCliente().getNombreCompleto());
			
			if(agd.getCliente().getCorreoElectronico() != null)
				((TextView) convertView.findViewById(R.id.textView_mail)).setText(""+agd.getCliente().getCorreoElectronico());
			else
				((TextView) convertView.findViewById(R.id.textView_mail)).setVisibility(View.GONE);
		}
		
		if(agd.getClienteDireccion() != null)
		{
			((TextView) convertView.findViewById(R.id.textView_address)).setText(""+agd.getClienteDireccion().getDireccion());
			
			if(agd.getClienteDireccion().getTelefono1() != null)
				((TextView) convertView.findViewById(R.id.textView_movil)).setText(""+agd.getClienteDireccion().getTelefono1());
			else
				((TextView) convertView.findViewById(R.id.textView_movil)).setVisibility(View.GONE);
			
		}
		
		
		if(ClientFragment. mTwoPane)
		{
			if (section == section_)
				if (row == row_)
					convertView.setBackgroundColor(contex.getResources().getColor(R.color.color_background_selector));
				else
					convertView.setBackgroundColor(contex.getResources().getColor(R.color.color_background_white));
			else
				convertView.setBackgroundColor(contex.getResources().getColor(R.color.color_background_white));
		}
		
		
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
	
	@Override
    public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
        super.onRowItemClick(parent, view, section, row, id);
        
        if(ClientFragment. mTwoPane)
        {
        	section_ = section;
        	row_ = row;
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