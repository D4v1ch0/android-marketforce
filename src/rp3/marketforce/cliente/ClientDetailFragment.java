package rp3.marketforce.cliente;

import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.util.Screen;
import rp3.widget.ViewPager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class ClientDetailFragment extends rp3.app.BaseFragment implements ClienteEditFragment.OnClienteEditListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "rp3.pos.transactionid";
	public static final String ARG_ITEM_TIPO_PERSONA = "rp3.pos.tipo_persona";
	public static final String ARG_PARENT_SOURCE = "rp3.pos.parentsource";
	public static final String PARENT_SOURCE_LIST = "LIST";
	public static final String PARENT_SOURCE_SEARCH = "SEARCH";

	public static final String STATE_CLIENT_ID = "clientId";
	public static final String STATE_CLIENT_TIPO_PERSONA = "tipoPersona";

	private long clientId;
	private String tipoPersona;
	private LayoutInflater inflater;
	private String[] testArrayDetails;
	private String[] testArrayDetailsAdress;
	private LinearLayout linearLayoutRigth;
	private LinearLayout linearLayoutAdress;
	private LinearLayout linearLayoutContact;
	private ViewPager PagerDetalles;
	private DetailsPageAdapter pagerAdapter;
	private ImageButton TabInfo;
	private ImageButton TabDirecciones;
	private ImageButton TabContactos;

	/*
	 * Posiciones y pivots de campos para clientes naturales y contactos
	 */
	private static final int ITEM_CEDULA = 0;
	private static final int ITEM_MAIL = 1;
	private static final int ITEM_CUMPLE = 2;
	private static final int ITEM_GENERO = 3;
	private static final int ITEM_ESTADO_CIVIL = 4;
	private static final int ITEM_CARGO = 5;

	/*
	 * Posiciones y pivots de campos para direcciones
	 */
	private static final int ITEM_DIRECCION = 0;
	private static final int ITEM_TELEFONO_1 = 1;
	private static final int ITEM_TELEFONO_2 = 2;
	private static final int ITEM_REFERENCIA = 3;
	private static final int ITEM_CIUDAD = 4;
	
	/*
	 * Posiciones y pivots de campos para clientes jur�dicos
	 */
	private static final int ITEM_RAZON_SOCIAL = 1;
	private static final int ITEM_ACTIVIDAD = 2;
	private static final int ITEM_EMAIL = 3;
	private static final int ITEM_PAGINA_WEB = 4;

	private String str_titulo;
	private final int REQUEST_CODE_DETAIL_EDIT = 3;
	private boolean flag = false;
	private Cliente client;
	private Contacto contacto;
	private DrawableManager DManager;
	
	private ClienteDetailFragmentListener clienteDetailFragmentCallback;

	public interface ClienteDetailFragmentListener{
		void onClienteChanged(Cliente cliente);
	}

	public static ClientDetailFragment newInstance(Cliente cl) {
		return newInstance(cl.getID(), cl.getTipoPersona());
	}
	
	public static ClientDetailFragment newInstance(long id) {
		return newInstance(id, "");
	}
	
	public static ClientDetailFragment newInstance(long id, String tipoPersona) {
		Bundle arguments = new Bundle();
		arguments.putLong(ClientDetailFragment.ARG_ITEM_ID, id);
		arguments.putString(ClientDetailFragment.ARG_ITEM_TIPO_PERSONA, tipoPersona);
		ClientDetailFragment fragment = new ClientDetailFragment();
		fragment.setArguments(arguments);
		
		return fragment;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * Se instancia Drawable Manager para carga de imagenes;
		 */
		DManager = new DrawableManager();
		
		if (getParentFragment() == null)
			setRetainInstance(true);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			clientId = getArguments().getLong(ARG_ITEM_ID);
		} else if (savedInstanceState != null) {
			clientId = savedInstanceState.getLong(STATE_CLIENT_ID);
		}
		if (getArguments().containsKey(ARG_ITEM_TIPO_PERSONA)) {
			tipoPersona = getArguments().getString(ARG_ITEM_TIPO_PERSONA);
		} else if (savedInstanceState != null) {
			tipoPersona = savedInstanceState.getString(STATE_CLIENT_TIPO_PERSONA);
		}
		
		if (clientId != 0) {
			if(tipoPersona != null && tipoPersona.equalsIgnoreCase("C"))
			{
				contacto = Contacto.getContactoId(getDataBase(), clientId);
			}
			else
			{
				client = Cliente.getClienteID(getDataBase(), clientId, true);
			}
		}
		
		if (client != null || contacto != null) {
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
		else{
			clienteDetailFragmentCallback = (ClienteDetailFragmentListener)activity;
			setRetainInstance(true);
		}
	}	

	@SuppressLint("InflateParams")
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {

		if(client==null && contacto==null) return;
		PagerDetalles = (ViewPager) rootView.findViewById(R.id.detail_client_pager);
		TabInfo = (ImageButton) rootView.findViewById((R.id.detail_tab_info));
		TabDirecciones = (ImageButton) rootView.findViewById((R.id.detail_tab_direccion));
		TabContactos = (ImageButton) rootView.findViewById((R.id.detail_tab_contactos));
		
		TabInfo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				PagerDetalles.setCurrentItem(0);
			}});
		
		TabDirecciones.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				PagerDetalles.setCurrentItem(1);
			}});
		
		TabContactos.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				PagerDetalles.setCurrentItem(2);
			}});
		
		PagerDetalles.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageSelected(int arg0) {
				String title = pagerAdapter.getPageTitle(arg0).toString();
				if(title.equalsIgnoreCase("Info"))
				{
					TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
					TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
					TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
				}
				if(title.equalsIgnoreCase("Direcciones"))
				{
					TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
					TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_activated));
					TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
				}
				if(title.equalsIgnoreCase("Contactos"))
				{
					TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
					TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
					TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_activated));
				}
				
			}});
		pagerAdapter = new DetailsPageAdapter();
		if (client != null) {
			if(client.getTipoPersona().equalsIgnoreCase("N"))
			{
				renderClienteNatural(rootView);
			}
			if(client.getTipoPersona().equalsIgnoreCase("J"))
			{
				renderClienteJuridico(rootView);
			}
		}
		if(contacto != null)
		{
			renderContacto(rootView);
		}
	}
	
	private void renderClienteNatural(View rootView)
	{
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
		linearLayoutContact = (LinearLayout) rootView
				.findViewById(R.id.linearLayout_content_contactos);

		String etiqueta = "";
		for (int x = 0; x < testArrayDetails.length; x++) {
			etiqueta = "";
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
			
			if(!etiqueta.equalsIgnoreCase(""))
			{
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
		}
		FrameLayout fl = new FrameLayout(getActivity());
		((ViewGroup)linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
		fl.addView(linearLayoutRigth);
		pagerAdapter.addView(fl);

		if (client.getClienteDirecciones() != null
				&& client.getClienteDirecciones().size() > 0) {
			setViewVisibility(R.id.linearLayout_content_adress,
					View.VISIBLE);
			setViewVisibility(R.id.detail_tab_direccion, View.VISIBLE);
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
			fl = new FrameLayout(getActivity());
			((ViewGroup)linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
			fl.addView(linearLayoutAdress);
			pagerAdapter.addView(fl);
		}
		
		if(client.getContactos() != null && client.getContactos().size() > 0)
		{
			setViewVisibility(R.id.linearLayout_content_contactos,
					View.VISIBLE);
			setViewVisibility(R.id.detail_tab_contactos, View.VISIBLE);
			testArrayDetails = this.getActivity().getResources()
					.getStringArray(R.array.testArrayDetails);

			LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 4);
			par.setMargins(0, 10, 0, 15);

			for (int x = 0; x < client.getContactos().size(); x++) {
				if (x > 0) {
					View view = new View(getActivity());
					view.setLayoutParams(par);
					view.setBackgroundResource(R.color.color_background_header);
					linearLayoutContact.addView(view);
				}			

					View view_rowlist = inflater.inflate(
							R.layout.rowlist_client_detail, null);
					((TextView) view_rowlist
							.findViewById(R.id.textView_title)).setText(client.getContactos().get(x).getCargo());

					((TextView) view_rowlist
							.findViewById(R.id.textView_content))
							.setText(client.getContactos().get(x).getNombre() + " " +
									client.getContactos().get(x).getApellido());
					linearLayoutContact.addView(view_rowlist);
				
			}
			fl = new FrameLayout(getActivity());
			((ViewGroup)linearLayoutContact.getParent()).removeView(linearLayoutContact);
			fl.addView(linearLayoutContact);
			pagerAdapter.addView(fl);
		}
		/*
		 * Se cambia la carga de foto por un lazy load
		 */
		//setImageViewBitmapFromInternalStorageAsync(R.id.imageView_foto, client.getFotoFileName());
		DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + client.getURLFoto(),
				(ImageView) this.getRootView().findViewById(R.id.imageView_foto));
		setTextViewText(R.id.textView_tipo_canal,
				client.getCanalDescripcion());
		setTextViewText(R.id.textView_tipo_cliente,
				client.getTipoClienteDescripcion());
		((RatingBar) rootView.findViewById(R.id.ratingBar_status))
				.setRating(client.getCalificacion());
		setTextViewText(R.id.textView_client, client.getNombreCompleto());
		TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
		
		PagerDetalles.setAdapter(pagerAdapter);
	}
	
	private void renderClienteJuridico(View rootView)
	{
		hideDialogConfirmation();
		(rootView.findViewById(R.id.imageView_edit_detail_client))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onDetailItemEdit(clientId);
					}
				});

		testArrayDetails = this.getActivity().getResources()
				.getStringArray(R.array.testArrayDetailsJuridico);
		inflater = (LayoutInflater) this.getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		linearLayoutRigth = (LinearLayout) rootView
				.findViewById(R.id.linearLayout_content_rigth);
		linearLayoutAdress = (LinearLayout) rootView
				.findViewById(R.id.linearLayout_content_adress);
		linearLayoutContact = (LinearLayout) rootView
				.findViewById(R.id.linearLayout_content_contactos);

		String etiqueta = "";
		for (int x = 0; x < testArrayDetails.length; x++) {
			etiqueta = "";
			switch (x) {

			case ITEM_CEDULA:

				str_titulo = "";
				etiqueta = client.getTipoIdentificacionDescripcion();
				if (client.getIdentificacion() != null)
					if (!client.getIdentificacion().equals("null"))
						str_titulo = "" + client.getIdentificacion();

				flag = false;
				break;

			case ITEM_EMAIL:

				str_titulo = "";
				etiqueta = testArrayDetails[x];
				if (client.getCorreoElectronico() != null)
					if (!client.getCorreoElectronico().equals("null"))
						str_titulo = "" + client.getCorreoElectronico();

				flag = false;
				break;
				
			case ITEM_RAZON_SOCIAL:

				str_titulo = "";
				etiqueta = testArrayDetails[x];
				if (client.getRazonSocial() != null)
					if (!client.getRazonSocial().equals("null"))
						str_titulo = "" + client.getRazonSocial();

				flag = false;
				break;
				
			case ITEM_PAGINA_WEB:

				str_titulo = "";
				etiqueta = testArrayDetails[x];
				if (client.getPaginaWeb() != null)
					if (!client.getPaginaWeb().equals("null"))
						str_titulo = "" + client.getPaginaWeb();

				flag = false;
				break;
				
			case ITEM_ACTIVIDAD:

				str_titulo = "";
				etiqueta = testArrayDetails[x];
				if (client.getActividadEconomica() != null)
					if (!client.getActividadEconomica().equals("null"))
						str_titulo = "" + client.getActividadEconomica();

				flag = false;
				break;

			default:
				break;
			}
			
			if(!etiqueta.equalsIgnoreCase(""))
			{
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
		}
		FrameLayout fl = new FrameLayout(getActivity());
		((ViewGroup)linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
		fl.addView(linearLayoutRigth);
		pagerAdapter.addView(fl);

		if (client.getClienteDirecciones() != null
				&& client.getClienteDirecciones().size() > 0) {
			setViewVisibility(R.id.linearLayout_content_adress,
					View.VISIBLE);
			setViewVisibility(R.id.detail_tab_direccion, View.VISIBLE);
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
			fl = new FrameLayout(getActivity());
			((ViewGroup)linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
			fl.addView(linearLayoutAdress);
			pagerAdapter.addView(fl);
		}
		
		if(client.getContactos() != null && client.getContactos().size() > 0)
		{
			setViewVisibility(R.id.linearLayout_content_contactos,
					View.VISIBLE);
			setViewVisibility(R.id.detail_tab_contactos, View.VISIBLE);
			testArrayDetails = this.getActivity().getResources()
					.getStringArray(R.array.testArrayDetails);

			LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 4);
			par.setMargins(0, 10, 0, 15);

			for (int x = 0; x < client.getContactos().size(); x++) {
				if (x > 0) {
					View view = new View(getActivity());
					view.setLayoutParams(par);
					view.setBackgroundResource(R.color.color_background_header);
					linearLayoutContact.addView(view);
				}			

					View view_rowlist = inflater.inflate(
							R.layout.rowlist_client_detail, null);
					((TextView) view_rowlist
							.findViewById(R.id.textView_title)).setText(client.getContactos().get(x).getCargo());

					((TextView) view_rowlist
							.findViewById(R.id.textView_content))
							.setText(client.getContactos().get(x).getNombre() + " " +
									client.getContactos().get(x).getApellido());
					linearLayoutContact.addView(view_rowlist);
				
			}
			fl = new FrameLayout(getActivity());
			((ViewGroup)linearLayoutContact.getParent()).removeView(linearLayoutContact);
			fl.addView(linearLayoutContact);
			pagerAdapter.addView(fl);
		}
		/*
		 * Se cambia la carga de foto por un lazy load
		 */
		//setImageViewBitmapFromInternalStorageAsync(R.id.imageView_foto, client.getFotoFileName());
		DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + client.getURLFoto(),
				(ImageView) this.getRootView().findViewById(R.id.imageView_foto));
		setTextViewText(R.id.textView_tipo_canal,
				client.getCanalDescripcion());
		setTextViewText(R.id.textView_tipo_cliente,
				client.getTipoClienteDescripcion());
		((RatingBar) rootView.findViewById(R.id.ratingBar_status))
				.setRating(client.getCalificacion());
		setTextViewText(R.id.textView_client, client.getNombreCompleto());
		
		PagerDetalles.setAdapter(pagerAdapter);
		
		if(client.getContactos() != null && client.getContactos().size() > 0)
		{
			TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_activated));
			PagerDetalles.setCurrentItem(2);
		}
		else
		{
			TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
		}
		
	}
	
	private void renderContacto(View rootView)
	{
		hideDialogConfirmation();
		(rootView.findViewById(R.id.imageView_edit_detail_client)).setVisibility(View.GONE);

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
			etiqueta = "";
			switch (x) {

			case ITEM_MAIL:

				str_titulo = "";
				etiqueta = testArrayDetails[x];
				if (contacto.getCorreo() != null)
					if (!contacto.getCorreo().equals("null"))
						str_titulo = "" + contacto.getCorreo();

				flag = false;
				break;
				
			case ITEM_CARGO:

				str_titulo = "";
				etiqueta = testArrayDetails[x];
				if (contacto.getCargo() != null)
					if (!contacto.getCorreo().equals("null"))
						str_titulo = "" + contacto.getCargo();

				flag = false;
				break;

			default:
				break;
			}
			
			if(!etiqueta.equalsIgnoreCase(""))
			{
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
		}
		
		FrameLayout fl = new FrameLayout(getActivity());
		((ViewGroup)linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
		fl.addView(linearLayoutRigth);
		pagerAdapter.addView(fl);
		
		if (contacto.getIdClienteDireccion() != 0) {
			setViewVisibility(R.id.linearLayout_content_adress,
					View.VISIBLE);
			testArrayDetailsAdress = this.getActivity().getResources()
					.getStringArray(R.array.testArrayDetailsAdress);

			LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 4);
			par.setMargins(0, 10, 0, 15);

				for (int y = 0; y < testArrayDetailsAdress.length; y++) {

					switch (y) {
					case ITEM_DIRECCION:
						str_titulo = "";
						ClienteDireccion cd = ClienteDireccion.getClienteDireccionIdDireccion(getDataBase(), contacto.getIdCliente(), contacto.getIdClienteDireccion());
						if (cd != null)
							if (!cd.getDireccion().equals("null"))
								str_titulo = ""
										+ cd.getDireccion();

						break;

					case ITEM_TELEFONO_1:
						str_titulo = "";
							if (!contacto.getTelefono1().equals("null"))
								str_titulo = ""
										+ contacto.getTelefono1();

						break;

					case ITEM_TELEFONO_2:
						str_titulo = "";
							if (!contacto.getTelefono2().equals("null"))
								str_titulo = "" + contacto.getTelefono2();

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
				fl = new FrameLayout(getActivity());
				((ViewGroup)linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
				fl.addView(linearLayoutAdress);
				pagerAdapter.addView(fl);
			}
		

		/*
		 * Se cambia la carga de foto por un lazy load
		 */
		//setImageViewBitmapFromInternalStorageAsync(R.id.imageView_foto, client.getFotoFileName());
		DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + contacto.getURLFoto(),
				(ImageView) this.getRootView().findViewById(R.id.imageView_foto));
		setTextViewText(R.id.textView_client, contacto.getNombre() + " " + contacto.getApellido());
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
