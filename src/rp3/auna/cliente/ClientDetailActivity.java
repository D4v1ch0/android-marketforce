package rp3.auna.cliente;

import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.cliente.ClientDetailFragment.ClienteDetailFragmentListener;
import rp3.auna.models.Cliente;
import rp3.auna.models.ClienteDireccion;
import rp3.auna.models.Contacto;
import rp3.auna.utils.DetailsPageAdapter;
import rp3.auna.utils.DrawableManager;
import rp3.auna.utils.Utils;
import rp3.util.BitmapUtils;
import rp3.util.Screen;
import rp3.widget.ViewPager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class ClientDetailActivity extends rp3.app.BaseActivity implements ClienteDetailFragmentListener {

	private long transactionId;
	private final String STATE_TRANSACTIONID = "transactionId";
	private ClientDetailFragment transactionDetailFragment;
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
	private DrawableManager DManager;

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
	
	public final static String EXTRA_TRANSACTIONID = "transactionId";
	private String str_titulo;
	private final int REQUEST_CODE_DETAIL_EDIT = 3;
	private boolean flag = false;
	private Cliente client;
	private Contacto contacto;
	private int curentPage = -1;
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DManager = new DrawableManager();
        setHomeAsUpEnabled(true, true);	

		if (getIntent().getExtras().containsKey(ARG_ITEM_ID)) {
			clientId = getIntent().getExtras().getLong(ARG_ITEM_ID);
		} else if (savedInstanceState != null) {
			clientId = savedInstanceState.getLong(STATE_CLIENT_ID);
		}
		if (getIntent().getExtras().containsKey(ARG_ITEM_TIPO_PERSONA)) {
			tipoPersona = getIntent().getExtras().getString(ARG_ITEM_TIPO_PERSONA);
		} else if (savedInstanceState != null) {
			tipoPersona = savedInstanceState.getString(STATE_CLIENT_TIPO_PERSONA);
		}
		
		if (clientId != 0) {
			setContentView(R.layout.fragment_cliente_detalle, R.menu.fragment_cliente_detail);
		} else {
			setContentView(R.layout.base_content_no_selected_item);
		}
    }
    
    @Override
	public void onResume() {		
		super.onResume();
		
		if (clientId != 0) {
			if(tipoPersona != null && tipoPersona.equalsIgnoreCase("C"))
			{
				contacto = Contacto.getContactoId(getDataBase(), clientId);
			}
			else
			{
				client = Cliente.getClienteIDServer(getDataBase(), clientId, true);
			}
		}
		
		if(client==null && contacto==null) return;
		PagerDetalles = (ViewPager) getRootView().findViewById(R.id.detail_client_pager);
		TabInfo = (ImageButton) getRootView().findViewById((R.id.detail_tab_info));
		TabDirecciones = (ImageButton) getRootView().findViewById((R.id.detail_tab_direccion));
		TabContactos = (ImageButton) getRootView().findViewById((R.id.detail_tab_contactos));
		
		linearLayoutRigth = (LinearLayout) getRootView()
				.findViewById(R.id.linearLayout_content_rigth);
		linearLayoutAdress = (LinearLayout) getRootView()
				.findViewById(R.id.linearLayout_content_adress);
		linearLayoutContact = (LinearLayout) getRootView()
				.findViewById(R.id.linearLayout_content_contactos);
		linearLayoutRigth.removeAllViews();
		linearLayoutAdress.removeAllViews();
		//linearLayoutContact.removeAllViews();
		
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
				setPageConfig(arg0);
				
			}});
		pagerAdapter = new DetailsPageAdapter();
		
		
		setPageConfig(PagerDetalles.getCurrentItem());
		if (client != null) {
			if(client.getTipoPersona().equalsIgnoreCase("N"))
			{
				renderClienteNatural(getRootView());
			}
			if(client.getTipoPersona().equalsIgnoreCase("J"))
			{
				renderClienteJuridico(getRootView());
			}
		}
		if(contacto != null)
		{
			renderContacto(getRootView());
		}
	}
    
    private void setPageConfig(int page){
		curentPage = page;
		String title = pagerAdapter.getPageTitle(page).toString();
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
	}
	
	private void renderClienteNatural(View rootView)
	{
		hideDialogConfirmation();


		testArrayDetails = this.getResources()
				.getStringArray(R.array.testArrayDetails);
		inflater = (LayoutInflater) this.getSystemService(
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

                if(client.getFechaNacimiento() != null && client.getFechaNacimiento().getTime() != 0)
				    setTextViewDateText(R.id.textView_client, client.getFechaNacimiento());

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
		ScrollView fl = new ScrollView(this);
		((ViewGroup)linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
		fl.addView(linearLayoutRigth);
		pagerAdapter.addView(fl);

		if (client.getClienteDirecciones() != null
				&& client.getClienteDirecciones().size() > 0) {
			setViewVisibility(R.id.linearLayout_content_adress,
					View.VISIBLE);
			setViewVisibility(R.id.detail_tab_direccion, View.VISIBLE);
			testArrayDetailsAdress = this.getResources()
					.getStringArray(R.array.testArrayDetailsAdress);

			LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 4);
			par.setMargins(0, 10, 0, 15);

			for (int x = 0; x < client.getClienteDirecciones().size(); x++) {
				if (x > 0) {
					View view = new View(this);
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
			fl = new ScrollView(this);
			((ViewGroup)linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
			fl.addView(linearLayoutAdress);
			pagerAdapter.addView(fl);
		}
		
		if(client.getContactos() != null && client.getContactos().size() > 0)
		{
			setViewVisibility(R.id.linearLayout_content_contactos,
					View.VISIBLE);
			setViewVisibility(R.id.detail_tab_contactos, View.VISIBLE);
			testArrayDetails = this.getResources()
					.getStringArray(R.array.testArrayDetails);

			LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 4);
			par.setMargins(0, 10, 0, 15);

			for (int x = 0; x < client.getContactos().size(); x++) {
				if (x > 0) {
					View view = new View(this);
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
					
					((TextView) view_rowlist
							.findViewById(R.id.textView_content)).setClickable(true);
					
				
			}
			fl = new ScrollView(getApplicationContext());
			((ViewGroup)linearLayoutContact.getParent()).removeView(linearLayoutContact);
			fl.addView(linearLayoutContact);
			pagerAdapter.addView(fl);
		}
		/*
		 * Se cambia la carga de foto por un lazy load
		 */
		//setImageViewBitmapFromInternalStorageAsync(R.id.imageView_foto, client.getFotoFileName());
		((ImageView) this.getRootView().findViewById(R.id.imageView_foto)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.user), 
				getResources().getDimensionPixelOffset(R.dimen.image_size)));
		DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getApplicationContext(), client.getURLFoto()),
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


		testArrayDetails = this.getApplicationContext().getResources()
				.getStringArray(R.array.testArrayDetailsJuridico);
		inflater = (LayoutInflater) this.getApplicationContext().getSystemService(
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
        ScrollView fl = new ScrollView(getApplicationContext());
		((ViewGroup)linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
		fl.addView(linearLayoutRigth);
		pagerAdapter.addView(fl);

		if (client.getClienteDirecciones() != null
				&& client.getClienteDirecciones().size() > 0) {
			setViewVisibility(R.id.linearLayout_content_adress,
					View.VISIBLE);
			setViewVisibility(R.id.detail_tab_direccion, View.VISIBLE);
			testArrayDetailsAdress = this.getApplicationContext().getResources()
					.getStringArray(R.array.testArrayDetailsAdress);

			LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 4);
			par.setMargins(0, 10, 0, 15);

			for (int x = 0; x < client.getClienteDirecciones().size(); x++) {
				if (x > 0) {
					View view = new View(getApplicationContext());
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
			fl = new ScrollView(getApplicationContext());
			((ViewGroup)linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
			fl.addView(linearLayoutAdress);
			pagerAdapter.addView(fl);
		}
		

		/*
		 * Se cambia la carga de foto por un lazy load
		 */
		//setImageViewBitmapFromInternalStorageAsync(R.id.imageView_foto, client.getFotoFileName());
		((ImageView) this.getRootView().findViewById(R.id.imageView_foto)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.user), 
				getResources().getDimensionPixelOffset(R.dimen.image_size)));
		DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getApplicationContext(), client.getURLFoto()),
				(ImageView) this.getRootView().findViewById(R.id.imageView_foto));
		setTextViewText(R.id.textView_tipo_canal,
				client.getCanalDescripcion());
		setTextViewText(R.id.textView_tipo_cliente,
				client.getTipoClienteDescripcion());
		((RatingBar) rootView.findViewById(R.id.ratingBar_status))
				.setRating(client.getCalificacion());
		setTextViewText(R.id.textView_client, client.getNombreCompleto());
		
		PagerDetalles.setAdapter(pagerAdapter);
		
	}
	
	private void renderContacto(View rootView)
	{
        boolean telf = false, email = false;
		hideDialogConfirmation();
        String email_str = "";

		testArrayDetails = this.getApplicationContext().getResources()
				.getStringArray(R.array.testArrayDetails);
		inflater = (LayoutInflater) this.getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

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

                email_str = str_titulo;
                email = true;
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

                if(email)
                {
                    ((TextView) view_rowlist
                            .findViewById(R.id.textView_content)).setClickable(true);
                    final String finalEmail_str = email_str;
                    ((TextView) view_rowlist
                            .findViewById(R.id.textView_content)).setOnClickListener(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", finalEmail_str, null));
                            startActivity(Intent.createChooser(intent, "Send Email"));
                        }});
                    ((TextView) view_rowlist.findViewById(R.id.textView_content)).setPaintFlags(((TextView) view_rowlist.findViewById(R.id.textView_content)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    ((TextView) view_rowlist.findViewById(R.id.textView_content)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                    email = false;
                }
				
				((TextView) view_rowlist
						.findViewById(R.id.textView_title)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_medium_size));
				((TextView) view_rowlist
						.findViewById(R.id.textView_content)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_medium_size));

			
				linearLayoutRigth.addView(view_rowlist);

				if (x + 1 == testArrayDetails.length)
					view_rowlist.findViewById(R.id.view_bottom).setVisibility(
							View.GONE);
			}
		}
		
		FrameLayout fl = new FrameLayout(getApplicationContext());
		((ViewGroup)linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
		fl.addView(linearLayoutRigth);
		pagerAdapter.addView(fl);
		
		if (contacto.getIdClienteDireccion() != 0) {
			setViewVisibility(R.id.linearLayout_content_adress,
					View.VISIBLE);
			setViewVisibility(R.id.detail_tab_direccion, View.VISIBLE);
			testArrayDetailsAdress = this.getApplicationContext().getResources()
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
					
					((TextView) view_rowlist
							.findViewById(R.id.textView_title)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_medium_size));
					((TextView) view_rowlist
							.findViewById(R.id.textView_content)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_medium_size));
					
					linearLayoutAdress.addView(view_rowlist);

					if (y + 1 == testArrayDetailsAdress.length)
						view_rowlist.findViewById(R.id.view_bottom)
								.setVisibility(View.GONE);
				}
				fl = new FrameLayout(getApplicationContext());
				((ViewGroup)linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
				fl.addView(linearLayoutAdress);
				pagerAdapter.addView(fl);
			}
		

		/*
		 * Se cambia la carga de foto por un lazy load
		 */
		//setImageViewBitmapFromInternalStorageAsync(R.id.imageView_foto, client.getFotoFileName());
		((ImageView) this.getRootView().findViewById(R.id.imageView_foto)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.user), 
				getResources().getDimensionPixelOffset(R.dimen.image_size)));
		DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getApplicationContext(), contacto.getURLFoto()),
				(ImageView) findViewById(R.id.imageView_foto));
		setTextViewText(R.id.textView_tipo_canal,
				contacto.getCargo());
		setTextViewText(R.id.textView_tipo_cliente,
				contacto.getEmpresa().trim());
		setTextViewText(R.id.textView_client, contacto.getNombre() + " " + contacto.getApellido());
		PagerDetalles.setAdapter(pagerAdapter);
		TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
	}
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    	outState.putLong(STATE_TRANSACTIONID,transactionId);    	
    }

	@Override
	public void onClienteChanged(Cliente cliente) {
		finish();		
	}    
    
	private void showDetailDialog(long transactionDetailId) {
		ClienteEditFragment fragment = ClienteEditFragment
				.newInstance(transactionDetailId);
		showDialogFragment(fragment, "detailDialog");
	}

	private void startDetailEditActivity(long transactionDetailId) {
		startActivityForResult(ClienteEditActivity.newIntent(this,
				transactionDetailId), REQUEST_CODE_DETAIL_EDIT);
	}

	public void onDetailItemEdit(long transactionDetailId) {
		if (Screen.isMinLargeLayoutSize(this))
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
            case R.id.action_edit:
                Intent intent2 = new Intent(this, CrearClienteActivity.class);
                intent2.putExtra(CrearClienteActivity.ARG_IDCLIENTE, client.getID());
                startActivity(intent2);
                break;

        }
		return super.onOptionsItemSelected(item);
	}
    
}
