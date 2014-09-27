package rp3.marketforce;

import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class RutasDetailFragment extends rp3.app.BaseFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "rp3.pos.transactionid";
    public static final String ARG_PARENT_SOURCE = "rp3.pos.parentsource";

    public static final String PARENT_SOURCE_LIST = "LIST";
    public static final String PARENT_SOURCE_SEARCH = "SEARCH";
    
    public static final String STATE_TRANSACTIONID = "transactionid";
    
    private long transactionId;
    
//    private LayoutInflater inflater;
    
    private Agenda agenda;
    private ListaTareasAdapter adapter;
    private ListView lista_tarea;
//    private boolean mTwoPane = false;
    
//    private TransactionDetailListener transactionDetailCallback;
    
//    private Cliente client;
    
    public interface TransactionDetailListener{
    	public void onDeleteSuccess(Cliente transaction);
    }
    
    public static RutasDetailFragment newInstance(long transactionId)
    {
    	Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, transactionId);
        RutasDetailFragment fragment = new RutasDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
            
    public RutasDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                

        if(getParentFragment()==null)
        	setRetainInstance(true);
        
        if (getArguments().containsKey(ARG_ITEM_ID)) {            
            transactionId = getArguments().getLong(ARG_ITEM_ID);   
        }else if(savedInstanceState!=null)
        {
        	transactionId = savedInstanceState.getLong(STATE_TRANSACTIONID);
        }    
        
        if(transactionId != 0)
        {        	
        	agenda = Agenda.getAgendaID(getDataBase(), transactionId);
        }
        
        if(agenda != null){
        	super.setContentView(R.layout.fragment_transaction_detail_rutas);
        }
        else{
        	super.setContentView(R.layout.base_content_no_selected_item);
        }      
        
       
        
//     super.setContentView(R.layout.fragment_tramnsaction_detail, R.menu.fragment_cliente_detail);
            
    }

    @Override
    public void onAttach(Activity activity) {    	
    	super.onAttach(activity);
    	
    }
    
    @Override
    public void onPositiveConfirmation(int id) {
    	super.onPositiveConfirmation(id);
    }
    
    @SuppressLint("InflateParams")
	@Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {    	
    	 
		if(agenda != null)
		{
			
	      String[] datos = getActivity(). getResources().getStringArray(R.array.testArrayEstado);
	//			inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  setSpinnerSimpleAdapter(R.id.spinner_state, datos);
			
		   setTextViewText(R.id.textView_name, agenda.getCliente().getNombreCompleto());
		   setTextViewText(R.id.textView_movil, agenda.getClienteDireccion().getTelefono1());
		   setTextViewText(R.id.textView_mail, agenda.getCliente().getCorreoElectronico());
		   setTextViewText(R.id.textView_address, agenda.getClienteDireccion().getDireccion());
		   setTextViewDateText(R.id.textView_fecha, agenda.getFechaInicio());
		   
		   
		   if(agenda.getAgendaTareaList() != null)
		   {
			   adapter = new ListaTareasAdapter(getActivity(), agenda.getAgendaTareaList());
			   lista_tarea = (ListView) rootView.findViewById(R.id.listView_tareas);
			   lista_tarea.setAdapter(adapter);
		   }
		   
		}
    }
      
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	outState.putLong(STATE_TRANSACTIONID, transactionId);    	
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    
}
