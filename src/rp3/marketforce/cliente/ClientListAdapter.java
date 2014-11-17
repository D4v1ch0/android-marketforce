package rp3.marketforce.cliente;

import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientListFragment.ClienteListFragmentListener;
import rp3.marketforce.headerlistview.SectionAdapter;
import rp3.marketforce.models.Cliente;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ClientListAdapter extends SectionAdapter{
	
//	static class ViewHolderHeader {
//		TextView textView_headerlist_client_list;
//	}
	
//	static class ViewHolderRow {
//		TextView textView_clientlist_name;
//		TextView textView_clientlist_date;
//		TextView textView_clientlist_movil;
//		TextView textView_clientlist_mail;
//		TextView textView_clientlist_address;
//	}
	
	private ArrayList<ArrayList<rp3.marketforce.models.Cliente>> dataList;
	private LayoutInflater inflater;
	private Context contex;
	private String string_client;
	private int section_ = -1;
	private int row_ = -1;
	private List<String> headersortList;
	private int order;
	private ClienteListFragmentListener clienteListFragmentCallback;
	
	private final int ORDER_BY_NAME = 5;
    private final int ORDER_BY_LAST_NAME = 6;
	
	public ClientListAdapter(Context c,ArrayList<ArrayList<rp3.marketforce.models.Cliente>> data, List<String> headersortList, 
			int order,ClienteListFragmentListener clienteListFragmentCallback){
		this.dataList = data;
		this.inflater = LayoutInflater.from(c);
		this.contex = c;
		this.headersortList = headersortList;
		this.order= order;
		this.clienteListFragmentCallback = clienteListFragmentCallback;
	}
	
	@Override
	public Object getItem(int pos) {	
		return dataList.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {			
		return 0;
	}

	@Override
	public int numberOfSections() {
		return dataList.size();
	}

	@Override
	public int numberOfRows(int section) {
		return dataList.get(section).size();
	}
	
	 @Override
     public boolean hasSectionHeaderView(int section) {
         return true;
     }
	
	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		
//		ViewHolderHeader holder_header;
//		 
//	     if (convertView == null) {
//	          convertView = (View) inflater.inflate(R.layout.headerlist_client_list, null);
//	          
//	          holder_header = new ViewHolderHeader();	
//	          holder_header.textView_headerlist_client_list = (TextView) convertView.findViewById(R.id.textView_headerlist_client_list);
//	          
//	          convertView.setTag(holder_header);
//	       }else{
//				holder_header = (ViewHolderHeader)convertView.getTag();
//			}
//	     
//	           holder_header.textView_headerlist_client_list.setText("A");
//	          
//	        return convertView;
		
//		if (convertView == null)
	      convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.headerlist_client_list), null);
		
		((TextView) convertView.findViewById(R.id.textView_headerlist_client_list)).setText(""+headersortList.get(section));
		
		return convertView;
	}

	@Override
	public View getRowView(int section, int row, View convertView,
			ViewGroup parent) {
		
//        ViewHolderRow holder_row;
//		
//		if (convertView == null) {
//			convertView = inflater.inflate(R.layout.rowlist_client_list, null);
//			
//			holder_row = new ViewHolderRow();			
//			holder_row.textView_clientlist_name = (TextView) convertView.findViewById(R.id.textView_clientlist_name);
//			holder_row.textView_clientlist_date = (TextView) convertView.findViewById(R.id.textView_clientlist_date);
//			holder_row.textView_clientlist_movil = (TextView) convertView.findViewById(R.id.textView_clientlist_movil);
//			holder_row.textView_clientlist_mail = (TextView) convertView.findViewById(R.id.textView_clientlist_mail);
//			holder_row.textView_clientlist_address = (TextView) convertView.findViewById(R.id.textView_clientlist_address);
//			
//			convertView.setTag(holder_row);
//			
//		}else{
//			holder_row = (ViewHolderRow)convertView.getTag();
//		}
//		
////		Cliente current = (Cliente)getItem(0);
//		
//		holder_row.textView_clientlist_name.setText(current.getNombre1()+" "+current.getApellido1());
//		holder_row.textView_clientlist_date.setText("17 Abril");
//		holder_row.textView_clientlist_movil .setText("0930306573");
//		holder_row.textView_clientlist_mail.setText(current.getCorreoElectronico());
//		holder_row.textView_clientlist_address.setText("9 de Octubre y Rumichaca");
//		
//		return convertView;
		 
//		if (convertView == null)
	    convertView = (View) inflater.inflate(this.contex.getApplicationContext().getResources().getLayout(R.layout.rowlist_client_list), null);
		
		Cliente current =  dataList.get(section).get(row);
				
		switch (order) {
		case ORDER_BY_NAME:
			if(!current.getTipoPersona().equalsIgnoreCase("J"))
				string_client = current.getNombre1()+" "+current.getApellido1();
			else
				string_client = current.getNombre1();
			
		break;
		case ORDER_BY_LAST_NAME:
			if(!current.getTipoPersona().equalsIgnoreCase("J"))
				string_client = current.getApellido1()+" "+current.getNombre1();
			else
				string_client = current.getNombre1();
			break;
		}
		
		((TextView) convertView.findViewById(R.id.textView_clientlist_name)).setText(""+string_client);
		
		if(current.getCorreoElectronico()!= null)
			if(!current.getCorreoElectronico().equals("null"))
				((TextView) convertView.findViewById(R.id.textView_clientlist_mail)).setText(current.getCorreoElectronico());
		
	   ((TextView) convertView.findViewById(R.id.textView_clientlist_movil)).setText(""+current.getTelefono());
		((TextView) convertView.findViewById(R.id.textView_clientlist_address)).setText(""+current.getDireccion());
		
		if(clienteListFragmentCallback.allowSelectedItem())
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
        
        section_ = section;
    	row_ = row;
    	
        if(clienteListFragmentCallback.allowSelectedItem()){        	
        	notifyDataSetChanged();
        }
        
        if(clienteListFragmentCallback != null)
        	clienteListFragmentCallback.onClienteSelected(dataList.get(section).get(row));        
       
       }
	
}