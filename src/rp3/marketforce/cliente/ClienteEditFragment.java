package rp3.marketforce.cliente;

import java.util.Date;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.marketforce.DateDisplayPicker;
import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.sync.ClienteActualizacion;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
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

public class ClienteEditFragment extends BaseFragment {

	private static final String TAG = ClienteEditFragment.class.getSimpleName();
	public static final String ARG_CLIENT_ID = "clientId";

	private long clientId = 0;
	private Cliente client;
	private DateDisplayPicker datePicker;
	private EditText editTextMail;
	private LinearLayout linearLayout_directions;	
	private LayoutInflater inflater;
	private static final int ID_SELECTOR = 222;
	private double longitude;
	private double latitude;	
	private OnClienteEditListener onClienteEditCallback;
	
	public interface OnClienteEditListener{
		void onClienteUpdate(Cliente cliente);
	}
	
	public static ClienteEditFragment newInstance(long transactionDetailId) {
		Bundle arguments = new Bundle();
		arguments.putLong(ClienteEditFragment.ARG_CLIENT_ID,
				transactionDetailId);
		ClienteEditFragment fragment = new ClienteEditFragment();
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
		setContentView(R.layout.fragment_transaction_edit_item);

		Bundle arguments = getArguments();
		if (arguments != null && arguments.containsKey(ARG_CLIENT_ID)) {
			clientId = arguments.getLong(ARG_CLIENT_ID, 0);
		}

		if (clientId != 0) {
			client = Cliente.getClienteIDServer(getDataBase(), clientId, true);
		} else {
			client = new Cliente();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG,"onAttach...");
		tryEnableGooglePlayServices(true);				
		
		if(getParentFragment()!=null)
			onClienteEditCallback = (OnClienteEditListener)getParentFragment();
		else
			onClienteEditCallback = (OnClienteEditListener)activity;
	}

	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		Log.d(TAG,"onFragmentCreateView...");
		if (client != null) {

			String name = "" + client.getNombre1() + " "
					+ client.getApellido1();

			if (isDialog()) {
				getDialog()
						.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				this.getDialog().setTitle(name);
				setViewVisibility(R.id.textView_name_client, View.GONE);
			} else {
				setViewVisibility(R.id.textView_name_client, View.VISIBLE);
				setTextViewText(R.id.textView_name_client, name);
			}

			setImageButtonClickListener(R.id.button_accept,
					new View.OnClickListener() {

						public void onClick(View v) {
							if(updateCliente()){								
								finish();
								onClienteEditCallback.onClienteUpdate(client);
							}
						}
					});

			setImageButtonClickListener(R.id.button_cancel,
					new View.OnClickListener() {

						public void onClick(View v) {
							finish();
						}
					});

			if (savedInstanceState == null) {
				
				inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				setSpinnerAdapter(R.id.spinner_state_civil,
						new rp3.content.SimpleGeneralValueAdapter(this.getActivity(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_ESTADO_CIVIL));
				
				setSpinnerGeneralValueSelection(R.id.spinner_state_civil,client.getEstadoCivil());

				linearLayout_directions = (LinearLayout) rootView
						.findViewById(R.id.linearLayout_directions);
				editTextMail = (EditText) rootView
						.findViewById(R.id.editText_correo);
				editTextMail.setText("" + client.getCorreoElectronico());

				datePicker = (DateDisplayPicker) rootView
						.findViewById(R.id.datePicker);
				datePicker.setParent(this);

				setTextViewDateText(datePicker.getId(),
						client.getFechaNacimiento());

				LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, 2);
				par.setMargins(0, 15, 0, 5);

				if (client.getClienteDirecciones() != null
						&& client.getClienteDirecciones().size() > 0) {
					setViewVisibility(R.id.textView_direcciones_title,
							View.VISIBLE);
					setViewVisibility(R.id.view_separator, View.VISIBLE);

					for (int x = 0; x < client.getClienteDirecciones().size(); x++) {
						if (x > 0) {
							View view = new View(getActivity());
							view.setLayoutParams(par);
							view.setBackgroundResource(R.color.color_background_selector);
							linearLayout_directions.addView(view);
						}

						View view_rowlist = inflater.inflate(
								R.layout.row_list_direction, null);
						view_rowlist.setId(ID_SELECTOR + x);
						linearLayout_directions.addView(view_rowlist);

						((EditText) view_rowlist
								.findViewById(R.id.editText_direccion))
								.setText(""
										+ client.getClienteDirecciones().get(x)
												.getDireccion());
						((EditText) view_rowlist
								.findViewById(R.id.editText_telefono1))
								.setText(""
										+ client.getClienteDirecciones().get(x)
												.getTelefono1());
						((EditText) view_rowlist
								.findViewById(R.id.editText_telefono2))
								.setText(""
										+ client.getClienteDirecciones().get(x)
												.getTelefono2());
						if (client.getClienteDirecciones().get(x)
								.getReferencia() != null)
							if (!client.getClienteDirecciones().get(x)
									.getReferencia().equals("null"))
								((EditText) view_rowlist
										.findViewById(R.id.editText_referencia))
										.setText(""
												+ client.getClienteDirecciones()
														.get(x).getReferencia());

						final TextView text_pos = (TextView) view_rowlist
								.findViewById(R.id.textView_pos);

						if (client.getClienteDirecciones().get(x).getLongitud() != 0)
							text_pos.setText(client.getClienteDirecciones()
									.get(x).getLongitud()
									+ " , "
									+ client.getClienteDirecciones().get(x)
											.getLatitud());

						((ImageButton) view_rowlist
								.findViewById(R.id.imageButton_get_Localitation))
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										getLocation();
										text_pos.setText("" + longitude + " , "
												+ latitude);
									}
								});
					}

				}

			}

		}
	}		

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setClienteUpdateViews();
		Log.d(TAG,"onSaveInstanceState...");
	}

	private void setClienteUpdateViews() {
		Log.d(TAG,"setClienteUPDATEvIEWS...");
		String mail = getTextViewString(R.id.editText_correo);
		Date da = datePicker.getDate();

		if (da != null)
			client.setFechaNacimiento(da);

		client.setCorreoElectronico(mail);
		client.setEstadoCivil(getSpinnerGeneralValueSelectedCode(R.id.spinner_state_civil));

		int x = 0;
		TextView textViewPos;
		
		for (ClienteDireccion d : client.getClienteDirecciones()) {
			View row;

			if (isDialog())
				row = getDialog().findViewById(ID_SELECTOR + (x++));
			else
				row = getActivity().findViewById(ID_SELECTOR + (x++));

			d.setDireccion(((EditText) row
					.findViewById(R.id.editText_direccion)).getText()
					.toString());
			d.setTelefono1(((EditText) row
					.findViewById(R.id.editText_telefono1)).getText()
					.toString());
			d.setTelefono2(((EditText) row
					.findViewById(R.id.editText_telefono2)).getText()
					.toString());
			d.setReferencia(((EditText) row
					.findViewById(R.id.editText_referencia)).getText()
					.toString());

			textViewPos = ((TextView) row.findViewById(R.id.textView_pos));
			
			if (textViewPos.getText().length() > 0) {
				
				String[] dat = textViewPos.getText().toString().split(" , ");
				
				if(dat != null)
					if(dat.length == 2)
					{
						d.setLongitud(Double.parseDouble(dat[0]));
						d.setLatitud(Double.parseDouble(dat[1]));
					}
			}
		}
	}

	private boolean updateCliente() {
		setClienteUpdateViews();
		ClienteDireccion d = client.getClienteDireccionPrincipal();
		if(d!=null){
			client.setDireccion(d.getDireccion());
			client.setTelefono(d.getTelefono1());
		}
		
		boolean result = Cliente.update(getDataBase(), client);
		if(result)
			ejecutarSync();
		
		return result;
	}
	
	private void ejecutarSync(){
		Bundle settingsBundle = new Bundle();
		settingsBundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE);
		settingsBundle.putLong(ClienteActualizacion.ARG_CLIENTE_ID, client.getID());
		requestSync(settingsBundle);
	}


	private void getLocation() {

		if (GooglePlayServicesUtils.servicesConnected((BaseActivity) this
				.getActivity())) {

			if (!ConnectionUtils.isNetAvailable(this.getActivity())) {
				showDialogMessage(R.string.message_error_sync_no_net_available);
				Log.d(TAG,"No hay conexion...");
				return;
			}

			final Location location = getLastLocation();

			// showDialogProgress(R.string.message_title_connecting,
			// R.string.message_please_wait);

			longitude = location.getLongitude();
			latitude = location.getLatitude();
			// Date orderDate = DateTime.getCurrentDateTime();
			// String code = String.valueOf(orderDate.getTime());

			// Log.e("ADDRESS",""+LocationUtils.getAddress(getActivity(),
			// location));

			// return LocationUtils.getAddress(getActivity(), location);
		}
	}

	/**
	 *
	 * Ciclo de vida
	 *
	 */
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG,"onStart...");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"onResume...");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}

}
