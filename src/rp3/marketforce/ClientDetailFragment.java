package rp3.marketforce;

import rp3.marketforce.edit.TransactionEditItemActivity;
import rp3.marketforce.edit.TransactionEditItemFragment;
import rp3.marketforce.models.Cliente;
import rp3.util.Screen;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ClientDetailFragment extends rp3.app.BaseFragment {
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
//    private boolean mTwoPane = false;
    private LayoutInflater inflater;
    private String[] testArrayDetails;
    private String[] testArrayDetailsAdress;
    private LinearLayout linearLayoutRigth;
    private LinearLayout linearLayoutAdress;
//    private LinearLayout.LayoutParams params;
    
    private static final int ITEM_CEDULA = 0;
    private static final int ITEM_MAIL = 1;
    private static final int ITEM_CUMPLE = 2;
	private static final int ITEM_GENERO = 3;
    private static final int ITEM_ESTADO_CIVIL = 4;
    
    private static final int ITEM_DIRECCION = 0;
    private static final int ITEM_TELEFONO_1 = 1;
    private static final int ITEM_TELEFONO_2 = 2;
	private static final int ITEM_REFERENCIA = 3;
    private static final int ITEM_CIUDAD = 4;
    
    
    private String str_titulo;
    private final int REQUEST_CODE_DETAIL_EDIT  = 3;
    private boolean flag = false;
    
    
//    private TransactionDetailListener transactionDetailCallback;
    
    private Cliente client;
    
    public interface TransactionDetailListener{
    	public void onDeleteSuccess(Cliente transaction);
    }
    
    public static ClientDetailFragment newInstance(long transactionId)
    {
    	Bundle arguments = new Bundle();
        arguments.putLong(ClientDetailFragment.ARG_ITEM_ID, transactionId);
        ClientDetailFragment fragment = new ClientDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
            
    public ClientDetailFragment() {
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
        	client = Cliente.getClienteID(getDataBase(), transactionId, true);
        }
        
        if(client != null){
        	super.setContentView(R.layout.fragment_transaction_detail);
        }
        else{
        	super.setContentView(R.layout.base_content_no_selected_item);
        }        
        
//        	super.setContentView(R.layout.fragment_tramnsaction_detail, R.menu.fragment_cliente_detail);
            
    }

    @Override
    public void onAttach(Activity activity) {    	
    	super.onAttach(activity);
    	
//    	if(!(activity instanceof TransactionDetailListener))
//    		throw new IllegalStateException("Activity must implements Fragment TransactionDetailListener");
//    	transactionDetailCallback = (TransactionDetailListener)activity;
    	
    }
    
    @Override
    public void onPositiveConfirmation(int id) {
    	super.onPositiveConfirmation(id);
//    	Transaction.delete(getDataBase(), transaction);
//    	transactionDetailCallback.onDeleteSuccess(transaction);
    }
    
    @SuppressLint("InflateParams")
	@Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {    	
    	 
    	if(client != null)
    	{
    		
    		hideDialogConfirmation();
    		(rootView.findViewById(R.id.imageView_edit_detail_client)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onDetailItemEdit(transactionId);
				}
			});
    		
    	 testArrayDetails = this.getActivity().getResources().getStringArray(R.array.testArrayDetails);
    	 inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    	 params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//    	 params.setMargins(0, 0, 0, 0);
    	
//    	if (rootView.findViewById(R.id.linearLayout_content_left) != null) {     
//			mTwoPane = true;
//			linearLayoutLeft = (LinearLayout) rootView.findViewById(R.id.linearLayout_content_left);
//        }
    	
    	linearLayoutRigth = (LinearLayout) rootView.findViewById(R.id.linearLayout_content_rigth);
    	linearLayoutAdress = (LinearLayout) rootView.findViewById(R.id.linearLayout_content_adress);
					
    	for(int x = 0 ; x < testArrayDetails.length ; x++)
    	{
    		
    		 switch (x) {
    			   				
    			case ITEM_CEDULA:
    				
    				str_titulo = "";
    				
    				if(client.getIdentificacion() != null)
    					if(!client.getIdentificacion().equals("null"))
    						str_titulo = ""+client.getIdentificacion();
    				
    				flag = false;
    				break;
    				
    			case ITEM_MAIL:
    				
    				str_titulo = "";
    				
    				if(client.getCorreoElectronico() != null)
    					if(!client.getCorreoElectronico().equals("null"))
    						str_titulo = ""+client.getCorreoElectronico();
    				
    				flag = false;
    				break;
    				
    			case ITEM_CUMPLE:
    				
    				setTextViewText(R.id.textView_client, "");
    				
    			  setTextViewDateText(R.id.textView_client, client.getFechaNacimiento());
    			  Log.e("DATE",""+client.getFechaNacimiento());
    				
    				flag = true;
    				break;
    				
    			case ITEM_GENERO:
    				
    				str_titulo = "";
    				
    				if(client.getGeneroDescripcion() != null)
    					if(!client.getGeneroDescripcion().equals("null"))
    						str_titulo = ""+client.getGeneroDescripcion();
    						
    				flag = false;
    				break;
    				
    			case ITEM_ESTADO_CIVIL:
    				
    				str_titulo = "";
    				
    				if(client.getEstadoCivilDescripcion() != null)
    					if(!client.getEstadoCivilDescripcion().equals("null"))
    							str_titulo = ""+client.getEstadoCivilDescripcion();
    				
    				flag = false;
    				break;
    				

    			default:
    				break;
    			}
    		
	    		View view_rowlist = inflater.inflate(R.layout.rowlist_client_detail, null);
    			((TextView) view_rowlist.findViewById(R.id.textView_title)).setText(""+testArrayDetails[x]);
    			
    			if(flag)
    				((TextView) view_rowlist.findViewById(R.id.textView_content)).setText(getTextViewString(R.id.textView_client));
    			else
    			((TextView) view_rowlist.findViewById(R.id.textView_content)).setText(str_titulo);
    			
//    			view_rowlist.setLayoutParams(params);
    			
	    		linearLayoutRigth.addView(view_rowlist);
	    		
	    		if(x+1 == testArrayDetails.length)
	    			view_rowlist.findViewById(R.id.view_bottom).setVisibility(View.GONE);
    	}
    	
    	if(client.getClienteDirecciones() != null && client.getClienteDirecciones().size() > 0)
		{
    		setViewVisibility(R.id.linearLayout_content_adress, View.VISIBLE);
    		testArrayDetailsAdress = this.getActivity().getResources().getStringArray(R.array.testArrayDetailsAdress);
    		
    		LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,4); 
    		par.setMargins(0, 10, 0, 15);
    		
			for(int x = 0; x < client.getClienteDirecciones().size(); x++)
			{
				if(x > 0)
				{
					View view = new View(getActivity());
					view.setLayoutParams(par);
					view.setBackgroundResource(R.color.color_background_header);
					linearLayoutAdress.addView(view);
				}
				
				for(int y = 0 ; y < testArrayDetailsAdress.length ; y++)
				{
					
					 switch (y) {
		   				
		    			case ITEM_DIRECCION:
		    				
		    				str_titulo = "";
		    				
		    				if(client.getClienteDirecciones().get(x).getDireccion() != null)
		    					if(!client.getClienteDirecciones().get(x).getDireccion().equals("null"))
		    						str_titulo = ""+client.getClienteDirecciones().get(x).getDireccion();
		    				
		    				break;
		    				
		    			case ITEM_TELEFONO_1:
		    				
		    				str_titulo = "";
		    				
		    				if(client.getClienteDirecciones().get(x).getTelefono1() != null)
		    					if(!client.getClienteDirecciones().get(x).getTelefono1().equals("null"))
		    						str_titulo = ""+client.getClienteDirecciones().get(x).getTelefono1();
		    				
		    						break;
		    				
		    			case ITEM_TELEFONO_2:
		    				
		    				str_titulo = "";
		    				
		    				if(client.getClienteDirecciones().get(x).getTelefono2() != null)
		    					if(!client.getClienteDirecciones().get(x).getTelefono2().equals("null"))
		    						str_titulo = ""+client.getClienteDirecciones().get(x).getTelefono2();
		    				
		    				break;
		    				
		    			case ITEM_REFERENCIA:
		    				
		    				str_titulo = "";
		    				
		    				if(client.getClienteDirecciones().get(x).getReferencia() != null)
		    					if(!client.getClienteDirecciones().get(x).getReferencia().equals("null"))
		    						str_titulo = ""+client.getClienteDirecciones().get(x).getReferencia();
		    				
		    				break;
		    				
		    			case ITEM_CIUDAD:
		    				
		    				str_titulo = client.getClienteDirecciones().get(x).getCiudadDescripcion();
		    				
		    				break;
		    				

		    			default:
		    				break;
		    			}
					
					
					View view_rowlist = inflater.inflate(R.layout.rowlist_client_detail, null);
	    			((TextView) view_rowlist.findViewById(R.id.textView_title)).setText(""+testArrayDetailsAdress[y]);
	    			
	    			((TextView) view_rowlist.findViewById(R.id.textView_content)).setText(str_titulo);
//	    			view_rowlist.setLayoutParams(params);
	    			linearLayoutAdress.addView(view_rowlist);
	    			
	    			if(y+1 == testArrayDetailsAdress.length)
		    			view_rowlist.findViewById(R.id.view_bottom).setVisibility(View.GONE);
				}
			}
		}
    	
    	setTextViewText(R.id.textView_tipo_canal, client.getCanalDescripcion());
    	setTextViewText(R.id.textView_tipo_cliente, client.getTipoClienteDescripcion());
    	((RatingBar) rootView.findViewById(R.id.ratingBar_status)).setRating(client.getCalificacion());
    	setTextViewText(R.id.textView_client, client.getNombre1()+" "+client.getApellido1());
    }
    }
      
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	outState.putLong(STATE_TRANSACTIONID, transactionId);    	
    }
    
    private void showDetailDialog(long transactionDetailId){
		TransactionEditItemFragment fragment = TransactionEditItemFragment.newInstance(transactionDetailId);		
		showDialogFragment(fragment,"detailDialog");
	}
	
	private void startDetailEditActivity(long transactionDetailId){				
		startActivityForResult( TransactionEditItemActivity.newIntent(getContext(), transactionDetailId),REQUEST_CODE_DETAIL_EDIT);		
	}
	
	public void onDetailItemEdit(long transactionDetailId) {		
		if(Screen.isMinLargeLayoutSize(getActivity()))
			showDetailDialog(transactionDetailId);
		else
			startDetailEditActivity(transactionDetailId);			
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
