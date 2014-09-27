package rp3.marketforce.edit;

import java.util.Date;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.marketforce.DateDisplayPicker;
import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationClient;

@SuppressLint("InflateParams")
public class TransactionEditItemFragment extends BaseFragment {
	
	public static final String ARG_TRANSACTION_DETAIL_ID = "transactionDetailId";
	
	private long transactionDetailId = 0;
	private Cliente client;	
	private DateDisplayPicker datePicker;
	private EditText editTextMail;
	private LinearLayout linearLayout_directions;
	private LocationClient locationClient;
	private String[] datos ;
	private LayoutInflater inflater;
	private static final int ID_SELECTOR = 222;
	private double longitude;
	private double latitude;
//	private TransactionEditItemListener callback;
	
	public interface TransactionEditItemListener{
		
		void onItemEditAcceptAction(Cliente transactionDetail);
		void onItemEditCancelAction(Cliente transactionDetail);
		void onItemEditDiscardAction(Cliente transactionDetail);
		
	}
	
	
	public static TransactionEditItemFragment newInstance(long transactionDetailId){
		Bundle arguments = new Bundle();
		arguments.putLong(TransactionEditItemFragment.ARG_TRANSACTION_DETAIL_ID, transactionDetailId);
		TransactionEditItemFragment fragment = new TransactionEditItemFragment();
		fragment.setArguments(arguments);		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_transaction_edit_item);
//		setRetainInstance(true);
		
		Bundle arguments = getArguments();
		if(arguments!=null && arguments.containsKey(ARG_TRANSACTION_DETAIL_ID)){
			transactionDetailId = arguments.getLong(ARG_TRANSACTION_DETAIL_ID,0);
		}
		
		if(transactionDetailId!=0){
			client = Cliente.getClienteID(getDataBase(), transactionDetailId,true);
		}else{
			client =  new Cliente();
		}			
	}
	
	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);
		if(getParentFragment() != null){
//			if(!(getParentFragment() instanceof TransactionEditItemListener)) {
//				throw new IllegalStateException("Parent Fragment must implement fragment's TransactionEditItemListener.");				
//			}
//			callback = (TransactionEditItemListener)getParentFragment();
		}
		else if (!(activity instanceof TransactionEditItemListener)) {
			throw new IllegalStateException("Activity must implement fragment's TransactionEditItemListener.");
        }		
//		callback = (TransactionEditItemListener)activity;
		
		locationClient = LocationUtils.getLocationClient((BaseActivity)this.getActivity());
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		
		if(client != null)
		{
			
		String name = ""+client.getNombre1()+" "+client.getApellido1();
		
		if(isDialog())
		{
			
			getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			this.getDialog().setTitle(name);
			setViewVisibility(R.id.textView_name_client, View.GONE);
		}else{
			setViewVisibility(R.id.textView_name_client, View.VISIBLE);
			setTextViewText(R.id.textView_name_client, name);
		}
		
		setImageButtonClickListener(R.id.button_accept, new View.OnClickListener() {
			
			public void onClick(View v) {				
				updateTransactionDetail();				
//				callback.onItemEditAcceptAction(client);
				finish();
			}
		});
				
		
//		setImageButtonClickListener(R.id.imageButton_get_Localitation, new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
////				setTextViewText(R.id.textView_pos, getLocation());
//			}
//		});
		
		setImageButtonClickListener(R.id.button_cancel, new View.OnClickListener() {
			
			public void onClick(View v) {				
//				callback.onItemEditCancelAction(transactionDetail);
				finish();
			}
		});
		
		if(savedInstanceState==null){	
			
			datos = getActivity(). getResources().getStringArray(R.array.testArrayEstadoCivil);
			inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			setSpinnerAdapter(R.id.spinner_state_civil, new rp3.content.SimpleGeneralValueAdapter(this.getActivity(),getDataBase(),1002));
			setSpinnerGeneralValueSelection(R.id.spinner_state_civil,client.getEstadoCivil());
			
			linearLayout_directions = (LinearLayout) rootView.findViewById(R.id.linearLayout_directions);
			editTextMail = (EditText) rootView.findViewById(R.id.editText_correo);
			editTextMail.setText(""+client.getCorreoElectronico());
			
			datePicker = (DateDisplayPicker) rootView.findViewById(R.id.datePicker);
			datePicker.setParent(this);	
			
			setTextViewDateText(datePicker.getId(), client.getFechaNacimiento());
			
			LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,2); 
    		par.setMargins(0, 15, 0, 5);
			
    		if(client.getClienteDirecciones() != null && client.getClienteDirecciones().size() > 0)
    		{
    			setViewVisibility(R.id.textView_direcciones_title, View.VISIBLE);
    			setViewVisibility(R.id.view_separator, View.VISIBLE);
    		
			for(int x = 0; x < client.getClienteDirecciones().size(); x++)
			{
				if(x > 0)
				{
					View view = new View(getActivity());
					view.setLayoutParams(par);
					view.setBackgroundResource(R.color.color_background_selector);
					linearLayout_directions.addView(view);
				}
					
					View view_rowlist = inflater.inflate(R.layout.row_list_direction, null);
					view_rowlist.setId(ID_SELECTOR+x);
	    			linearLayout_directions.addView(view_rowlist);
	    			
	    			((EditText) view_rowlist.findViewById(R.id.editText_direccion)).setText(""+client.getClienteDirecciones().get(x).getDireccion());
	    			((EditText) view_rowlist.findViewById(R.id.editText_telefono1)).setText(""+client.getClienteDirecciones().get(x).getTelefono1());
	    			((EditText) view_rowlist.findViewById(R.id.editText_telefono2)).setText(""+client.getClienteDirecciones().get(x).getTelefono2());
	    			if(client.getClienteDirecciones().get(x).getReferencia() != null)
	    				if(!client.getClienteDirecciones().get(x).getReferencia().equals("null"))
	    					((EditText) view_rowlist.findViewById(R.id.editText_referencia)).setText(""+client.getClienteDirecciones().get(x).getReferencia());
	    			
	    			final TextView text_pos = (TextView) view_rowlist.findViewById(R.id.textView_pos);
	    			
	    			if(client.getClienteDirecciones().get(x).getLongitud() != 0)
	    				text_pos.setText(client.getClienteDirecciones().get(x).getLongitud()+" , "+client.getClienteDirecciones().get(x).getLatitud());
	    			
	    			
	    			((ImageButton) view_rowlist.findViewById(R.id.imageButton_get_Localitation)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							getLocation();
							text_pos.setText(""+longitude+" , "+latitude);
						}
					});
			}

    		}
			
		}	
		
//		if(transactionDetail.getQuantity() == 0){
//			EditText textViewQuantity = (EditText)getRootView().findViewById(R.id.editText_transactiondetail_quantity);
//			textViewQuantity.requestFocus();
//			textViewQuantity.selectAll();
//		}
		
	 }
	}
	
	public void beginDeleteDetail(){
//		super.showDialogConfirmation(0,R.string.message_confirmation_transaction_detail_delete);
	}

	@Override
	public void onPositiveConfirmation(int id) {		
		super.onPositiveConfirmation(id);
		deleteDetail();
	}
		
	
	private void deleteDetail(){				
//		TransactionDetail.delete(getDataBase(), transactionDetailId);
//		callback.onItemEditDiscardAction(transactionDetail);
		finish();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		setTransactionDetailFromViewValues();
	}
	
	private void setTransactionDetailFromViewValues(){
		String mail = getTextViewString(R.id.editText_correo);
		Date da = datePicker.getDate();
		
		if(da != null)
			client.setFechaNacimiento(da);
			
		client.setCorreoElectronico(mail);
		client.setEstadoCivil(datos[getSpinnerSelectedPosition(R.id.spinner_state_civil)]);
		
		int x = 0;
		for(ClienteDireccion d : client.getClienteDirecciones())
		{
			View row;
			
			if(isDialog())
			    row = getDialog().findViewById(ID_SELECTOR+(x++));
			else
				row = getActivity().findViewById(ID_SELECTOR+(x++));
			    
    			d.setDireccion(((EditText) row.findViewById(R.id.editText_direccion)).getText().toString());
    			d.setTelefono1(((EditText) row.findViewById(R.id.editText_telefono1)).getText().toString());
    			d.setTelefono2(((EditText) row.findViewById(R.id.editText_telefono2)).getText().toString());
    			d.setReferencia(((EditText) row.findViewById(R.id.editText_referencia)).getText().toString());
    			
			if(((TextView) row.findViewById(R.id.textView_pos)).getText().length() > 0)
			{
				d.setLongitud(longitude);
				d.setLatitud(latitude);
			}
    			
		}
		
	}
	
	private void updateTransactionDetail(){
		setTransactionDetailFromViewValues();
		Cliente.update(getDataBase(), client);
	}
	
	@Override
	public void onResume() {		
		super.onResume();
		if(GooglePlayServicesUtils.servicesConnected((BaseActivity)this.getActivity()))
		{
				locationClient.connect();
				Log.e("Location","Conectado");
		}
	}
	
	@Override
	public void onStop() {		
		super.onStop();
		if(GooglePlayServicesUtils.servicesConnected((BaseActivity)this.getActivity()))
		{
			locationClient.disconnect();
			Log.e("Location","Desconectado");
		}
	}
	
	private void getLocation(){
		
		if(GooglePlayServicesUtils.servicesConnected((BaseActivity) this.getActivity())){
			
			if (!ConnectionUtils.isNetAvailable(this.getActivity())) {						
				showDialogMessage(R.string.message_error_sync_no_net_available);
				return;
			}
			
			final Location location = locationClient.getLastLocation();
			
//			showDialogProgress(R.string.message_title_connecting, R.string.message_please_wait);
			
			longitude = location.getLongitude();
			latitude = location.getLatitude();
//			Date orderDate = DateTime.getCurrentDateTime();
//			String code = String.valueOf(orderDate.getTime());
					
//			Log.e("ADDRESS",""+LocationUtils.getAddress(getActivity(), location));
			
//			return LocationUtils.getAddress(getActivity(), location);
	   }
	}
	
}
