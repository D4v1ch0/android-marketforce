package rp3.marketforce.cliente;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleDictionaryAdapter;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.data.models.GeneralValue;
import rp3.data.models.GeopoliticalStructure;
import rp3.data.models.IdentificationType;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Canal;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.widget.ViewPager;

public class CrearClienteFragment extends BaseFragment {
	
	public static CrearClienteFragment newInstance(long id_cliente)
	{
		CrearClienteFragment fragment = new CrearClienteFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_CLIENTE, id_cliente);
		fragment.setArguments(args);
		return fragment;
	}

	public static String ARG_CLIENTE = "cliente";
	private ViewPager PagerDetalles;
	private DetailsPageAdapter pagerAdapter;
	private LayoutInflater inflater;
	private ImageButton TabInfo;
	private ImageButton TabDirecciones;
	private ImageButton TabContactos;
	private LinearLayout ContactosContainer, DireccionContainer;
	private List<LinearLayout> listViewDirecciones, listViewContactos;
	private int curentPage;
	private long idCliente = 0;
	Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
	boolean isClient;
	int posContact = -1;
	public Cliente cliente;
	public List<String> contactPhotos;
	private FrameLayout info;
	private FrameLayout direccion;
	private FrameLayout contacto;
	private ImageView ArrowInfo;
	private ImageView ArrowCont;
	private ImageView ArrowDir;
	private List<GeopoliticalStructure> ciudades;
	private List<String> ciudades_string;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cliente = new Cliente();
        contactPhotos = new ArrayList<String>();
	}
	
	@Override
	public void onAttach(Activity activity) {    	
	    super.onAttach(activity);
	    tryEnableGooglePlayServices(true);
	    setContentView(R.layout.fragment_crear_cliente, R.menu.fragment_crear_cliente);
	    setRetainInstance(true);
	}
	    
	@Override
	public void onResume() {
	    super.onResume();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.action_save:
			if(Validaciones())
			{
				Grabar();
				finish();
			}
			break;
		case R.id.action_cancel:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void Grabar() {
		Cliente cli = new Cliente();
			if(idCliente != 0)
				cli = Cliente.getClienteID(getDataBase(), idCliente, true);
			cli.setIdCanal((int) ((Spinner)info.findViewById(R.id.cliente_canal)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_canal)).getSelectedItemPosition()));
			cli.setIdTipoIdentificacion((int) ((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition()));
			cli.setIdentificacion(((EditText)info.findViewById(R.id.cliente_identificacion)).getText().toString());
			cli.setTipoPersona(((GeneralValue)((Spinner)info.findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItem()).getCode());
			cli.setIdTipoCliente((int) ((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).getSelectedItemPosition()));
			if(cliente.getURLFoto() != null && !cliente.getURLFoto().trim().equals(""))
				cli.setURLFoto(cliente.getURLFoto());
			if(((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItemPosition() == 1)
			{
				cli.setNombre1(((EditText)info.findViewById(R.id.cliente_primer_nombre)).getText().toString());
				cli.setNombre2(((EditText)info.findViewById(R.id.cliente_segundo_nombre)).getText().toString());
				cli.setApellido1(((EditText)info.findViewById(R.id.cliente_primer_apellido)).getText().toString());
				cli.setApellido2(((EditText)info.findViewById(R.id.cliente_segundo_apellido)).getText().toString());
				cli.setCorreoElectronico(((EditText)info.findViewById(R.id.cliente_correo)).getText().toString());
				cli.setGenero(((GeneralValue)((Spinner)info.findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
				cli.setEstadoCivil(((GeneralValue)((Spinner)info.findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
				cli.setNombreCompleto(cli.getNombre1() + " " + cli.getNombre2() + " " + cli.getApellido1() + " " + cli.getApellido2());
				cli.setFechaNacimiento(cliente.getFechaNacimiento());
			}
			else
			{
				cli.setApellido1("");
				cli.setNombre1(((EditText)info.findViewById(R.id.cliente_nombre)).getText().toString());
				cli.setActividadEconomica(((EditText)info.findViewById(R.id.cliente_actividad_economica)).getText().toString());
				cli.setCorreoElectronico(((EditText)info.findViewById(R.id.cliente_correo_juridico)).getText().toString());
				cli.setRazonSocial(((EditText)info.findViewById(R.id.cliente_razon_social)).getText().toString());
				cli.setPaginaWeb(((EditText)info.findViewById(R.id.cliente_pagina_web)).getText().toString());
				cli.setGenero(((GeneralValue)((Spinner)info.findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
				cli.setEstadoCivil(((GeneralValue)((Spinner)info.findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
				cli.setNombreCompleto(cli.getNombre1());
			}
			cli.setPendiente(true);
			List<Contacto> cliContactos = cli.getContactos();
			cli.setContactos(null);
			List<ClienteDireccion> cliDirecciones = cli.getClienteDirecciones();
			cli.setClienteDirecciones(null);
			if(cli.getID() == 0 )
				Cliente.insert(getDataBase(), cli);
			else
				Cliente.update(getDataBase(), cli);

			for(int i = 0; i < listViewDirecciones.size(); i ++)
			{
				ClienteDireccion cliDir = new ClienteDireccion();
				if(cliDirecciones != null && cliDirecciones.size() > i)
				{
					cliDir = cliDirecciones.get(i);
				}
				else
				{
					//cliDir.setIdClienteDireccion(i+1);
					if(idCliente != 0)
						cliDir.setIdCliente(cli.getIdCliente());
				}
				cliDir.set_idCliente(cli.getID());
				cliDir.setDireccion(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).getText().toString());
				cliDir.setTipoDireccion(((GeneralValue)((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).getSelectedItem()).getCode());
				cliDir.setEsPrincipal(((CheckBox)listViewDirecciones.get(i).findViewById(R.id.cliente_es_principal)).isChecked());
				cliDir.setTelefono1(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono1)).getText().toString());
				cliDir.setTelefono2(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono2)).getText().toString());
				cliDir.setReferencia(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_referencia)).getText().toString());
				cliDir.setCiudadDescripcion(((AutoCompleteTextView)listViewDirecciones.get(i).findViewById(R.id.cliente_ciudad)).getText().toString());
				try
				{
					cliDir.setIdCiudad((int) ciudades.get(ciudades_string.indexOf(((AutoCompleteTextView)listViewDirecciones.get(i).findViewById(R.id.cliente_ciudad)).getText().toString())).getID());
				}
				catch(Exception ex)
				{
					Toast.makeText(getContext(), "Ingrese correctamente la ciudad en una de las direcciones ingresadas.", Toast.LENGTH_LONG).show();
					return;
				}
				if(!((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).getText().toString().equals(""))
				{
					cliDir.setLongitud(Double.parseDouble(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).getText().toString()));
					cliDir.setLatitud(Double.parseDouble(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_latitud)).getText().toString()));
				}
				if(cliDir.getID() == 0)
					ClienteDireccion.insert(getDataBase(), cliDir);
				else
					ClienteDireccion.update(getDataBase(), cliDir);
				
				if(i == 0)
				{
					cli.setDireccion(cliDir.getDireccion());
					cli.setTelefono(cliDir.getTelefono1());
					Cliente.update(getDataBase(), cli);
				}
			}

			for(int i = 0; i < listViewContactos.size(); i ++)
			{
				Contacto cliCont = new Contacto();
				if(cliContactos != null && cliContactos.size() > i)
				{
					cliCont = cliContactos.get(i);
				}
				else
				{
					//cliCont.setIdContacto(i+1);
					if(idCliente != 0)
						cliCont.setIdCliente(cli.getIdCliente());
				}
				cliCont.set_idCliente(cli.getID());
				cliCont.setNombre(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_nombres)).getText().toString());
				cliCont.setApellido(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_apellidos)).getText().toString());
				cliCont.setCargo(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_cargo)).getText().toString());
				cliCont.setTelefono1(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono1_contacto)).getText().toString());
				cliCont.setTelefono2(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono2_contacto)).getText().toString());
				cliCont.setCorreo(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).getText().toString());
				cliCont.setIdClienteDireccion(((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).getSelectedItemPosition()+1);
				cliCont.setURLFoto(contactPhotos.get(i));
				
				if(cliCont.getID() == 0)
					Contacto.insert(getDataBase(), cliCont);
				else
					Contacto.update(getDataBase(), cliCont);
			}
			
			if(ConnectionUtils.isNetAvailable(getActivity()))
			{
				Bundle bundle = new Bundle();
				if(idCliente != 0)
					bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE_FULL);
				else
					bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_CREATE);
				bundle.putLong(ARG_CLIENTE, cli.getID());
				requestSync(bundle);
			}
	}

	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		if(info == null)
		{
		listViewDirecciones = new ArrayList<LinearLayout>();
		listViewContactos = new ArrayList<LinearLayout>();
		ciudades = GeopoliticalStructure.getGeopoliticalStructureByType(getDataBase(), 3);
		ciudades_string = new ArrayList<String>();
		
		for(GeopoliticalStructure gs : ciudades)
			ciudades_string.add(gs.getName());
		
		TabInfo = (ImageButton) getRootView().findViewById((R.id.detail_tab_info));
		TabDirecciones = (ImageButton) getRootView().findViewById((R.id.detail_tab_direccion));
		TabContactos = (ImageButton) getRootView().findViewById((R.id.detail_tab_contactos));
		ArrowInfo = (ImageView) getRootView().findViewById((R.id.detail_tab_info_arrow));
		ArrowDir = (ImageView) getRootView().findViewById((R.id.detail_tab_direccion_arrow));
		ArrowCont = (ImageView) getRootView().findViewById((R.id.detail_tab_contactos_arrow));
		PagerDetalles = (ViewPager) rootView.findViewById(R.id.crear_cliente_pager);
		
		pagerAdapter = new DetailsPageAdapter();
		inflater = (LayoutInflater) this.getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		info = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_info, null);
		direccion = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_direccion, null);
		((TextView) direccion.findViewById(R.id.agregar_direccion)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				addDireccion();
				
			}});
		DireccionContainer = (LinearLayout) direccion.findViewById(R.id.crear_cliente_container_direccion);
		
		contacto = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_contacto, null);
		((TextView) contacto.findViewById(R.id.agregar_contacto)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				addContacto();
				
			}});
		ContactosContainer = (LinearLayout) contacto.findViewById(R.id.crear_cliente_container_contacto);
		pagerAdapter.addView(info);
		pagerAdapter.addView(direccion);
		pagerAdapter.addView(contacto);
		PagerDetalles.setAdapter(pagerAdapter);
		setPageConfig(PagerDetalles.getCurrentItem());
		
		SimpleGeneralValueAdapter tipoPersonaAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_TIPO_PERSONA);
		SimpleGeneralValueAdapter tipoEstadoCivilAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_ESTADO_CIVIL);
		SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_GENERO);
		SimpleIdentifiableAdapter tipoCliente = new SimpleIdentifiableAdapter(getContext(), TipoCliente.getTipoCliente(getDataBase(), ""));
		SimpleIdentifiableAdapter tipoCanal = new SimpleIdentifiableAdapter(getContext(), Canal.getCanal(getDataBase(), ""));
		SimpleDictionaryAdapter tipoIdentificacion = new SimpleDictionaryAdapter(getContext(), IdentificationType.getAll(getDataBase()));
		
		((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).setAdapter(tipoPersonaAdapter);
		((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 1)
				{
					((LinearLayout) info.findViewById(R.id.crear_cliente_content_natural)).setVisibility(View.VISIBLE);
					((LinearLayout) info.findViewById(R.id.crear_cliente_content_juridico)).setVisibility(View.GONE);
				}
				else
				{
					((LinearLayout) info.findViewById(R.id.crear_cliente_content_natural)).setVisibility(View.GONE);
					((LinearLayout) info.findViewById(R.id.crear_cliente_content_juridico)).setVisibility(View.VISIBLE);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}});
		((Spinner) info.findViewById(R.id.cliente_estado_civil)).setAdapter(tipoEstadoCivilAdapter);
		((Spinner) info.findViewById(R.id.cliente_genero)).setAdapter(tipoGeneroAdapter);
		((Spinner) info.findViewById(R.id.cliente_tipo_cliente)).setAdapter(tipoCliente);
		((Spinner) info.findViewById(R.id.cliente_canal)).setAdapter(tipoCanal);
		((Spinner) info.findViewById(R.id.cliente_tipo_identificacion)).setAdapter(tipoIdentificacion);
		((ImageButton) info.findViewById(R.id.cliente_foto)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				isClient = true;	
				takePicture(1);
			}});
		((EditText) info.findViewById(R.id.cliente_fecha_nacimiento)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showDialogDatePicker(0);
			}});
		}
		else
		{
			TabInfo = (ImageButton) getRootView().findViewById((R.id.detail_tab_info));
			TabDirecciones = (ImageButton) getRootView().findViewById((R.id.detail_tab_direccion));
			TabContactos = (ImageButton) getRootView().findViewById((R.id.detail_tab_contactos));
			PagerDetalles = (ViewPager) rootView.findViewById(R.id.crear_cliente_pager);
			pagerAdapter = new DetailsPageAdapter();
			pagerAdapter.addView(info);
			pagerAdapter.addView(direccion);
			pagerAdapter.addView(contacto);
			PagerDetalles.setAdapter(pagerAdapter);
			setPageConfig(PagerDetalles.getCurrentItem());
		}
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
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				setPageConfig(arg0);
				
			}});
		if(getArguments().containsKey(ARG_CLIENTE) && getArguments().getLong(ARG_CLIENTE) != 0)
		{
			idCliente = getArguments().getLong(ARG_CLIENTE);
			setDatosClientes();
		}
	}
	
	private void setDatosClientes() {
		DrawableManager DManager = new DrawableManager();
		Cliente cli = Cliente.getClienteID(getDataBase(), idCliente, true);
		((Spinner)info.findViewById(R.id.cliente_canal)).setSelection(getPosition(((Spinner)info.findViewById(R.id.cliente_canal)).getAdapter(), cli.getIdCanal()));
		((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).setSelection(getPosition(((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).getAdapter(), cli.getTipoIdentificacionId()));
		((EditText)info.findViewById(R.id.cliente_identificacion)).setText(cli.getIdentificacion());
		((Spinner)info.findViewById(R.id.crear_cliente_tipo_persona)).setSelection(getPosition(((Spinner)info.findViewById(R.id.crear_cliente_tipo_persona)).getAdapter(), cli.getTipoPersona()));
		((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).setSelection(getPosition(((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).getAdapter(), cli.getIdTipoCliente()));
		DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getActivity(), cli.getURLFoto()), 
				(ImageButton)info.findViewById(R.id.cliente_foto));
		if(cli.getTipoPersona().equalsIgnoreCase("N"))
		{
			((EditText)info.findViewById(R.id.cliente_primer_nombre)).setText(cli.getNombre1());
			((EditText)info.findViewById(R.id.cliente_segundo_nombre)).setText(cli.getNombre2());
			((EditText)info.findViewById(R.id.cliente_primer_apellido)).setText(cli.getApellido1());
			((EditText)info.findViewById(R.id.cliente_segundo_apellido)).setText(cli.getApellido2());
			((EditText)info.findViewById(R.id.cliente_correo)).setText(cli.getCorreoElectronico());
			((Spinner)info.findViewById(R.id.cliente_genero)).setSelection(getPosition(((Spinner)info.findViewById(R.id.cliente_genero)).getAdapter(), cli.getGenero()));
			((Spinner)info.findViewById(R.id.cliente_estado_civil)).setSelection(getPosition(((Spinner)info.findViewById(R.id.cliente_estado_civil)).getAdapter(), cli.getGenero()));
		}
		else
		{
			((EditText)info.findViewById(R.id.cliente_nombre)).setText(cli.getNombre1());
			((EditText)info.findViewById(R.id.cliente_actividad_economica)).setText(cli.getActividadEconomica());
			((EditText)info.findViewById(R.id.cliente_correo_juridico)).setText(cli.getCorreoElectronico());
			((EditText)info.findViewById(R.id.cliente_razon_social)).setText(cli.getRazonSocial());
			((EditText)info.findViewById(R.id.cliente_pagina_web)).setText(cli.getPaginaWeb());
		}
		
		if(!cli.isNuevo())
		{
			((Spinner)info.findViewById(R.id.cliente_canal)).setEnabled(false);
			((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_identificacion)).setEnabled(false);
			((Spinner)info.findViewById(R.id.crear_cliente_tipo_persona)).setEnabled(false);
			((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_primer_nombre)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_segundo_nombre)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_primer_apellido)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_segundo_apellido)).setEnabled(false);
			((Spinner)info.findViewById(R.id.cliente_genero)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_nombre)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_actividad_economica)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_razon_social)).setEnabled(false);
			((EditText)info.findViewById(R.id.cliente_pagina_web)).setEnabled(false);
			((ImageButton)info.findViewById(R.id.cliente_foto)).setEnabled(false);
		}
		
		for(int i = 0; i < cli.getClienteDirecciones().size(); i ++)
		{
			addDireccion();
			((Button)listViewDirecciones.get(i).findViewById(R.id.eliminar_direccion)).setVisibility(View.GONE);
			((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).setText(cli.getClienteDirecciones().get(i).getDireccion());
			((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).setSelection(
					getPosition(((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).getAdapter(), cli.getClienteDirecciones().get(i).getTipoDireccion()));
			((CheckBox)listViewDirecciones.get(i).findViewById(R.id.cliente_es_principal)).setChecked(cli.getClienteDirecciones().get(i).getEsPrincipal());
			((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono1)).setText(cli.getClienteDirecciones().get(i).getTelefono1());
			((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono2)).setText(cli.getClienteDirecciones().get(i).getTelefono2());
			((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_referencia)).setText(cli.getClienteDirecciones().get(i).getReferencia());
			((AutoCompleteTextView)listViewDirecciones.get(i).findViewById(R.id.cliente_ciudad)).setText(cli.getClienteDirecciones().get(i).getCiudadDescripcion());
			if(cli.getClienteDirecciones().get(i).getLongitud() != 0)
			{
				((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).setText("" + cli.getClienteDirecciones().get(i).getLongitud());
				((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_latitud)).setText("" + cli.getClienteDirecciones().get(i).getLatitud());
			}
			if(!cli.isNuevo())
				((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).setEnabled(false);
		}
		
		for(int i = 0; i < cli.getContactos().size(); i ++)
		{
			addContacto();
			((Button)listViewContactos.get(i).findViewById(R.id.eliminar_contacto)).setVisibility(View.GONE);
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_nombres)).setText(cli.getContactos().get(i).getNombre());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_apellidos)).setText(cli.getContactos().get(i).getApellido());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_cargo)).setText(cli.getContactos().get(i).getCargo());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono1_contacto)).setText(cli.getContactos().get(i).getTelefono1());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono2_contacto)).setText(cli.getContactos().get(i).getTelefono2());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).setText(cli.getContactos().get(i).getCorreo());
			((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).setSelection(
					getPosition(((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).getAdapter(), (int) cli.getContactos().get(i).getIdClienteDireccion()));
			DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
					rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getActivity(), cli.getContactos().get(i).getURLFoto()), 
					(ImageButton)listViewContactos.get(i).findViewById(R.id.cliente_contacto_foto));
		}
		
		
	}

	@Override
	public void onDailogDatePickerChange(int id, Calendar c) {
		SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM yyyy");
		((EditText) info.findViewById(R.id.cliente_fecha_nacimiento)).setText(format1.format(c.getTime()));
		cliente.setFechaNacimiento(c.getTime());
		super.onDailogDatePickerChange(id, c);
	}
	
	protected void takePicture(final int idView) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this.getActivity());
	    myAlertDialog.setTitle("Grabar Foto");
	    myAlertDialog.setMessage("De donde desea obtener su foto?");

	    myAlertDialog.setPositiveButton("Galería",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface arg0, int arg1) {
	                	Intent galleryIntent = new Intent();
	            	    galleryIntent.setType("image/*");
	            	    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
	                    galleryIntent.putExtra("return-data", true);
	                    getActivity().startActivityForResult(galleryIntent, idView);
	                }
	            });

	    myAlertDialog.setNegativeButton("Camara",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface arg0, int arg1) {
	                	Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	            	    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
	                    getActivity().startActivityForResult(captureIntent, idView);

	                }
	            });
	    myAlertDialog.show();	
	}

	private void addDireccion()
	{
		final LinearLayout direccion = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_cliente_direccion_detail, null);
		((Button) direccion.findViewById(R.id.eliminar_direccion)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				listViewDirecciones.remove(direccion);
				DireccionContainer.removeView(direccion);		
			}});
		final int pos = listViewDirecciones.size();
		((ImageButton) direccion.findViewById(R.id.cliente_ubicacion)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (GooglePlayServicesUtils.servicesConnected((BaseActivity)getActivity())) {

					final Location location = getLastLocation();

					((EditText)listViewDirecciones.get(pos).findViewById(R.id.cliente_longitud)).setText(""+location.getLongitude());
					((EditText)listViewDirecciones.get(pos).findViewById(R.id.cliente_latitud)).setText(""+location.getLatitude());
				}
				
			}});
		SimpleGeneralValueAdapter tipoDireccionAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_TIPO_DIRECCION);
		((Spinner) direccion.findViewById(R.id.cliente_tipo_direccion_spinner)).setAdapter(tipoDireccionAdapter);
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,ciudades_string);
		((AutoCompleteTextView)direccion.findViewById(R.id.cliente_ciudad)).setAdapter(adapter);
		((AutoCompleteTextView)direccion.findViewById(R.id.cliente_ciudad)).setThreshold(1);
		
		DireccionContainer.addView(direccion);
		listViewDirecciones.add(direccion);
	}
	
	private void addContacto()
	{
		final LinearLayout contacto = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_cliente_contacto_detail, null);
		final int pos = listViewContactos.size();
		((Button) contacto.findViewById(R.id.eliminar_contacto)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				listViewContactos.remove(contacto);
				ContactosContainer.removeView(contacto);
				contactPhotos.remove(pos);
			}});
		contactPhotos.add("");
		((Spinner) contacto.findViewById(R.id.cliente_direccion_contacto)).setAdapter(getDirecciones());
		((ImageButton) contacto.findViewById(R.id.cliente_contacto_foto)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				posContact = pos;
				isClient = false;	
				takePicture(1);
			}});
		ContactosContainer.addView(contacto);
		listViewContactos.add(contacto);
	}
	
	private void setPageConfig(int page){
		curentPage = page;
		String title = pagerAdapter.getPageTitle(page).toString();
		if(title.equalsIgnoreCase("Info"))
		{
			TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
			TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
			TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
			ArrowInfo.setVisibility(View.VISIBLE);
			ArrowDir.setVisibility(View.INVISIBLE);
			ArrowCont.setVisibility(View.INVISIBLE);
		}
		if(title.equalsIgnoreCase("Direcciones"))
		{
			TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
			TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_activated));
			TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
			ArrowInfo.setVisibility(View.INVISIBLE);
			ArrowDir.setVisibility(View.VISIBLE);
			ArrowCont.setVisibility(View.INVISIBLE);
		}
		if(title.equalsIgnoreCase("Contactos"))
		{
			TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
			TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
			TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_activated));
			ArrowInfo.setVisibility(View.INVISIBLE);
			ArrowDir.setVisibility(View.INVISIBLE);
			ArrowCont.setVisibility(View.VISIBLE);
			for(int i = 0; i < listViewContactos.size(); i ++)
			{
				((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).setAdapter(getDirecciones());
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String path = "";
			if(data == null)
				path = photo.getPath();
			else
				path = Utils.getPath(data.getData(), getActivity());
			if(isClient)
			{
				((ImageButton) info.findViewById(R.id.cliente_foto)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500));
				cliente.setURLFoto(path);
			}
			else
			{
				((ImageButton) listViewContactos.get(posContact).findViewById(R.id.cliente_contacto_foto)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500));
				contactPhotos.set(posContact, path);
			}        
	    }
	}
	
	public ArrayAdapter<String> getDirecciones()
	{
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < listViewDirecciones.size(); i ++)
		{
			list.add(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).getText().toString());
		}
		return new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, list);
	}
	
	public boolean Validaciones()
	{
		if(listViewDirecciones.size() <= 0)
		{
			Toast.makeText(getContext(), "No se puede agregar clientes sin dirección.", Toast.LENGTH_LONG).show();
			return false;
		}
		if(((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItemPosition() == 1)
		{
			if(((EditText)info.findViewById(R.id.cliente_primer_nombre)).getText().toString().trim().length() <= 0)
			{
				Toast.makeText(getContext(), "Falta primer nombre de cliente.", Toast.LENGTH_LONG).show();
				return false;
			}
			if(((EditText)info.findViewById(R.id.cliente_primer_apellido)).getText().toString().trim().length() <= 0)
			{
				Toast.makeText(getContext(), "Falta primer apellido de cliente.", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		else
		{
			if(((EditText)info.findViewById(R.id.cliente_nombre)).getText().toString().trim().length() <= 0)
			{
				Toast.makeText(getContext(), "Falta nombre de cliente.", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		if(((EditText)info.findViewById(R.id.cliente_identificacion)).getText().toString().trim().length() <= 0)
		{
			Toast.makeText(getContext(), "Falta identificación del cliente.", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	private int getPosition(SpinnerAdapter spinnerAdapter, int i)
	{
		int position = -1;
		for(int f = 0; f < spinnerAdapter.getCount(); f++)
		{
			if(spinnerAdapter.getItemId(f) == i)
				position = f;
		}
		return position;
	}
	private int getPosition(SpinnerAdapter spinnerAdapter, String i)
	{
		int position = -1;
		for(int f = 0; f < spinnerAdapter.getCount(); f++)
		{
			if(((GeneralValue)spinnerAdapter.getItem(f)).getCode().equals(i))
				position = f;
		}
		return position;
	}

}
