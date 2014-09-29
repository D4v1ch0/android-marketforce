package rp3.marketforce.cliente;

import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import rp3.util.Screen;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ClientDetailFragment extends rp3.app.BaseFragment implements ClienteEditFragment.OnClienteEditListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "rp3.pos.transactionid";
	public static final String ARG_PARENT_SOURCE = "rp3.pos.parentsource";
	public static final String PARENT_SOURCE_LIST = "LIST";
	public static final String PARENT_SOURCE_SEARCH = "SEARCH";

	public static final String STATE_CLIENT_ID = "clientId";

	private long clientId;
	private LayoutInflater inflater;
	private String[] testArrayDetails;
	private String[] testArrayDetailsAdress;
	private LinearLayout linearLayoutRigth;
	private LinearLayout linearLayoutAdress;

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
	private final int REQUEST_CODE_DETAIL_EDIT = 3;
	private boolean flag = false;
	private Cliente client;
	
	private ClienteDetailFragmentListener clienteDetailFragmentCallback;

	public interface ClienteDetailFragmentListener{
		void onClienteChanged(Cliente cliente);
	}

	public static ClientDetailFragment newInstance(long clientId) {
		Bundle arguments = new Bundle();
		arguments.putLong(ClientDetailFragment.ARG_ITEM_ID, clientId);
		ClientDetailFragment fragment = new ClientDetailFragment();
		fragment.setArguments(arguments);
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getParentFragment() == null)
			setRetainInstance(true);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			clientId = getArguments().getLong(ARG_ITEM_ID);
		} else if (savedInstanceState != null) {
			clientId = savedInstanceState.getLong(STATE_CLIENT_ID);
		}
		
		if (clientId != 0) {
			client = Cliente.getClienteID(getDataBase(), clientId, true);
		}
		
		if (client != null) {
			super.setContentView(R.layout.fragment_cliente_detalle);
		} else {
			super.setContentView(R.layout.base_content_no_selected_item);
		}

	}

	@Override
	public void onResume() {		
		super.onResume();
		
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);		

		if(getParentFragment()!=null)
			clienteDetailFragmentCallback = (ClienteDetailFragmentListener)getParentFragment();
		else
			clienteDetailFragmentCallback = (ClienteDetailFragmentListener)activity;
	}	

	@SuppressLint("InflateParams")
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {

		if (client != null) {

			hideDialogConfirmation();
			(rootView.findViewById(R.id.imageView_edit_detail_client))
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							onDetailItemEdit(clientId);
						}
					});

			testArrayDetails = this.getActivity().getResources()
					.getStringArray(R.array.testArrayDetails);
			inflater = (LayoutInflater) this.getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);

			linearLayoutRigth = (LinearLayout) rootView
					.findViewById(R.id.linearLayout_content_rigth);
			linearLayoutAdress = (LinearLayout) rootView
					.findViewById(R.id.linearLayout_content_adress);

			String etiqueta = "";
			for (int x = 0; x < testArrayDetails.length; x++) {

				switch (x) {

				case ITEM_CEDULA:

					str_titulo = "";
					etiqueta = client.getTipoIdentificacionDescripcion();
					if (client.getIdentificacion() != null)
						if (!client.getIdentificacion().equals("null"))
							str_titulo = "" + client.getIdentificacion();

					flag = false;
					break;

				case ITEM_MAIL:

					str_titulo = "";
					etiqueta = testArrayDetails[x];
					if (client.getCorreoElectronico() != null)
						if (!client.getCorreoElectronico().equals("null"))
							str_titulo = "" + client.getCorreoElectronico();

					flag = false;
					break;

				case ITEM_CUMPLE:

					etiqueta = testArrayDetails[x];
					setTextViewText(R.id.textView_client, "");

					setTextViewDateText(R.id.textView_client,
							client.getFechaNacimiento());

					flag = true;
					break;

				case ITEM_GENERO:
					etiqueta = testArrayDetails[x];
					str_titulo = "";

					if (client.getGeneroDescripcion() != null)
						if (!client.getGeneroDescripcion().equals("null"))
							str_titulo = "" + client.getGeneroDescripcion();

					flag = false;
					break;

				case ITEM_ESTADO_CIVIL:
					etiqueta = testArrayDetails[x];
					str_titulo = "";

					if (client.getEstadoCivilDescripcion() != null)
						if (!client.getEstadoCivilDescripcion().equals("null"))
							str_titulo = ""
									+ client.getEstadoCivilDescripcion();

					flag = false;
					break;

				default:
					break;
				}
				
				
				View view_rowlist = inflater.inflate(
						R.layout.rowlist_client_detail, null);
				((TextView) view_rowlist.findViewById(R.id.textView_title))
						.setText(etiqueta);

				
				
				if (flag)
					((TextView) view_rowlist
							.findViewById(R.id.textView_content))
							.setText(getTextViewString(R.id.textView_client));
				else
					((TextView) view_rowlist
							.findViewById(R.id.textView_content))
							.setText(str_titulo);

				linearLayoutRigth.addView(view_rowlist);

				if (x + 1 == testArrayDetails.length)
					view_rowlist.findViewById(R.id.view_bottom).setVisibility(
							View.GONE);
			}

			if (client.getClienteDirecciones() != null
					&& client.getClienteDirecciones().size() > 0) {
				setViewVisibility(R.id.linearLayout_content_adress,
						View.VISIBLE);
				testArrayDetailsAdress = this.getActivity().getResources()
						.getStringArray(R.array.testArrayDetailsAdress);

				LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, 4);
				par.setMargins(0, 10, 0, 15);

				for (int x = 0; x < client.getClienteDirecciones().size(); x++) {
					if (x > 0) {
						View view = new View(getActivity());
						view.setLayoutParams(par);
						view.setBackgroundResource(R.color.color_background_header);
						linearLayoutAdress.addView(view);
					}

					for (int y = 0; y < testArrayDetailsAdress.length; y++) {

						switch (y) {
						case ITEM_DIRECCION:
							str_titulo = "";

							if (client.getClienteDirecciones().get(x)
									.getDireccion() != null)
								if (!client.getClienteDirecciones().get(x)
										.getDireccion().equals("null"))
									str_titulo = ""
											+ client.getClienteDirecciones()
													.get(x).getDireccion();

							break;

						case ITEM_TELEFONO_1:
							str_titulo = "";
							if (client.getClienteDirecciones().get(x)
									.getTelefono1() != null)
								if (!client.getClienteDirecciones().get(x)
										.getTelefono1().equals("null"))
									str_titulo = ""
											+ client.getClienteDirecciones()
													.get(x).getTelefono1();

							break;

						case ITEM_TELEFONO_2:
							str_titulo = "";
							if (client.getClienteDirecciones().get(x).getTelefono2() != null)
								if (!client.getClienteDirecciones().get(x).getTelefono2().equals("null"))
									str_titulo = "" + client.getClienteDirecciones().get(x).getTelefono2();

							break;

						case ITEM_REFERENCIA:
							str_titulo = "";
							if (client.getClienteDirecciones().get(x)
									.getReferencia() != null)
								if (!client.getClienteDirecciones().get(x)
										.getReferencia().equals("null"))
									str_titulo = ""
											+ client.getClienteDirecciones()
													.get(x).getReferencia();

							break;

						case ITEM_CIUDAD:
							str_titulo = client.getClienteDirecciones().get(x)
									.getCiudadDescripcion();
							break;

						default:
							break;
						}

						View view_rowlist = inflater.inflate(
								R.layout.rowlist_client_detail, null);
						((TextView) view_rowlist
								.findViewById(R.id.textView_title)).setText(""
								+ testArrayDetailsAdress[y]);

						((TextView) view_rowlist
								.findViewById(R.id.textView_content))
								.setText(str_titulo);
						linearLayoutAdress.addView(view_rowlist);

						if (y + 1 == testArrayDetailsAdress.length)
							view_rowlist.findViewById(R.id.view_bottom)
									.setVisibility(View.GONE);
					}
				}
			}
			setImageViewBitmapFromInternalStorageAsync(R.id.imageView_foto, client.getFotoFileName());
			setTextViewText(R.id.textView_tipo_canal,
					client.getCanalDescripcion());
			setTextViewText(R.id.textView_tipo_cliente,
					client.getTipoClienteDescripcion());
			((RatingBar) rootView.findViewById(R.id.ratingBar_status))
					.setRating(client.getCalificacion());
			setTextViewText(R.id.textView_client, client.getNombreCompleto());
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putLong(STATE_CLIENT_ID, clientId);
	}

	private void showDetailDialog(long transactionDetailId) {
		ClienteEditFragment fragment = ClienteEditFragment
				.newInstance(transactionDetailId);
		showDialogFragment(fragment, "detailDialog");
	}

	private void startDetailEditActivity(long transactionDetailId) {
		startActivityForResult(ClienteEditActivity.newIntent(getContext(),
				transactionDetailId), REQUEST_CODE_DETAIL_EDIT);
	}

	public void onDetailItemEdit(long transactionDetailId) {
		if (Screen.isMinLargeLayoutSize(getActivity()))
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

	@Override
	public void onClienteUpdate(Cliente cliente) {		
		clienteDetailFragmentCallback.onClienteChanged(cliente);
	}
}
