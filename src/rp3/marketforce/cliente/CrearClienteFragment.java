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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.content.SimpleDictionaryAdapter;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.data.models.GeneralValue;
import rp3.data.models.IdentificationType;
import rp3.marketforce.R;
import rp3.marketforce.models.Canal;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.Utils;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.widget.ViewPager;

public class CrearClienteFragment extends BaseFragment {
	
	public static CrearClienteFragment newInstance()
	{
		CrearClienteFragment fragment = new CrearClienteFragment();
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
	Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
	boolean isClient;
	int posContact = -1;
	public Cliente cliente;
	public List<String> contactPhotos;
	private FrameLayout info;
	private FrameLayout direccion;
	private FrameLayout contacto;
	
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
		JSONObject jObject = new JSONObject();
		try
		{
			jObject.put("IdCanal", ((Spinner)info.findViewById(R.id.cliente_canal)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_canal)).getSelectedItemPosition()));
			jObject.put("IdTipoIdentificacion", ((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition()));
			jObject.put("Identificacion", ((EditText)info.findViewById(R.id.cliente_identificacion)).getText().toString());
			jObject.put("TipoPersona", ((GeneralValue)((Spinner)info.findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItem()).getCode());
			jObject.put("IdTipoCliente", ((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).getSelectedItemPosition()));
			jObject.put("Foto", cliente.getURLFoto());
			if(((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItemPosition() == 1)
			{
				jObject.put("Nombre1", ((EditText)info.findViewById(R.id.cliente_primer_nombre)).getText().toString());
				jObject.put("Nombre2", ((EditText)info.findViewById(R.id.cliente_segundo_nombre)).getText().toString());
				jObject.put("Apellido1", ((EditText)info.findViewById(R.id.cliente_primer_apellido)).getText().toString());
				jObject.put("Apellido2", ((EditText)info.findViewById(R.id.cliente_segundo_apellido)).getText().toString());
				jObject.put("CorreoElectronico", ((EditText)info.findViewById(R.id.cliente_correo)).getText().toString());
				jObject.put("Genero", ((GeneralValue)((Spinner)info.findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
				jObject.put("EstadoCivil", ((GeneralValue)((Spinner)info.findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
			}
			else
			{
				jObject.put("Apellido1", "");
				jObject.put("Nombre1", ((EditText)info.findViewById(R.id.cliente_nombre)).getText().toString());
				jObject.put("ActividadEconomica", ((EditText)info.findViewById(R.id.cliente_actividad_economica)).getText().toString());
				jObject.put("CorreoElectronico", ((EditText)info.findViewById(R.id.cliente_correo_juridico)).getText().toString());
				jObject.put("RazonSocial", ((EditText)info.findViewById(R.id.cliente_razon_social)).getText().toString());
				jObject.put("PaginaWeb", ((EditText)info.findViewById(R.id.cliente_pagina_web)).getText().toString());
				jObject.put("Genero", ((GeneralValue)((Spinner)info.findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
				jObject.put("EstadoCivil", ((GeneralValue)((Spinner)info.findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
			}
			JSONArray jArrayDirecciones = new JSONArray();
			for(int i = 0; i < listViewDirecciones.size(); i ++)
			{
				JSONObject cliDir = new JSONObject();
				cliDir.put("IdClienteDireccion", i+1);
				cliDir.put("Direccion", ((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).getText().toString());
				cliDir.put("TipoDireccion", ((GeneralValue)((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).getSelectedItem()).getCode());
				cliDir.put("EsPrincipal", ((CheckBox)listViewDirecciones.get(i).findViewById(R.id.cliente_es_principal)).isChecked());
				cliDir.put("Telefono1", ((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono1)).getText().toString());
				cliDir.put("Telefono2", ((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono2)).getText().toString());
				cliDir.put("Referencia", ((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_referencia)).getText().toString());
				if(!((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).getText().toString().equals(""))
				{
					cliDir.put("Longitud", Double.parseDouble(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).getText().toString()));
					cliDir.put("Latitud", Double.parseDouble(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_latitud)).getText().toString()));
				}
				jArrayDirecciones.put(cliDir);
			}
			jObject.put("ClienteDirecciones",jArrayDirecciones);
			JSONArray jArrayContactos = new JSONArray();
			for(int i = 0; i < listViewContactos.size(); i ++)
			{
				JSONObject cliCont = new JSONObject();
				cliCont.put("IdClienteContacto", i+1);
				cliCont.put("Nombre", ((EditText)listViewContactos.get(i).findViewById(R.id.cliente_nombres)).getText().toString());
				cliCont.put("Apellido", ((EditText)listViewContactos.get(i).findViewById(R.id.cliente_apellidos)).getText().toString());
				cliCont.put("Cargo", ((EditText)listViewContactos.get(i).findViewById(R.id.cliente_cargo)).getText().toString());
				cliCont.put("Telefono1", ((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono1_contacto)).getText().toString());
				cliCont.put("Telefono2", ((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono2_contacto)).getText().toString());
				cliCont.put("CorreoElectronico", ((EditText)listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).getText().toString());
				cliCont.put("IdClienteDireccion", ((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).getSelectedItemPosition()+1);
				cliCont.put("Foto", contactPhotos.get(i));
				jArrayContactos.put(cliCont);
			}
			jObject.put("ClienteContactos", jArrayContactos);
			
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_CREATE);
			bundle.putString(ARG_CLIENTE, jObject.toString());
			requestSync(bundle);
		}
		catch(Exception ex)
		{
			Log.e("Error", ex.getMessage());
		}
		
	}

	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		if(info == null)
		{
		listViewDirecciones = new ArrayList<LinearLayout>();
		listViewContactos = new ArrayList<LinearLayout>();
		
		TabInfo = (ImageButton) getRootView().findViewById((R.id.detail_tab_info));
		TabDirecciones = (ImageButton) getRootView().findViewById((R.id.detail_tab_direccion));
		TabContactos = (ImageButton) getRootView().findViewById((R.id.detail_tab_contactos));
		PagerDetalles = (ViewPager) rootView.findViewById(R.id.crear_cliente_pager);
		
		pagerAdapter = new DetailsPageAdapter();
		inflater = (LayoutInflater) this.getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		info = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_info, null);
		direccion = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_direccion, null);
		((Button) direccion.findViewById(R.id.agregar_direccion)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				addDireccion();
				
			}});
		DireccionContainer = (LinearLayout) direccion.findViewById(R.id.crear_cliente_container_direccion);
		
		contacto = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_contacto, null);
		((Button) contacto.findViewById(R.id.agregar_contacto)).setOnClickListener(new OnClickListener(){

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
		((ImageView) info.findViewById(R.id.cliente_foto)).setOnClickListener(new OnClickListener(){
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

					if (!ConnectionUtils.isNetAvailable(getActivity())) {
						showDialogMessage(R.string.message_error_sync_no_net_available);
						return;
					}

					final Location location = getLastLocation();

					((EditText)listViewDirecciones.get(pos).findViewById(R.id.cliente_longitud)).setText(""+location.getLongitude());
					((EditText)listViewDirecciones.get(pos).findViewById(R.id.cliente_latitud)).setText(""+location.getLatitude());
				}
				
			}});
		SimpleGeneralValueAdapter tipoDireccionAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_TIPO_DIRECCION);
		
		((Spinner) direccion.findViewById(R.id.cliente_tipo_direccion_spinner)).setAdapter(tipoDireccionAdapter);
		
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
		((ImageView) contacto.findViewById(R.id.cliente_contacto_foto)).setOnClickListener(new OnClickListener(){

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
				((ImageView) info.findViewById(R.id.cliente_foto)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500));
				cliente.setURLFoto(path);
			}
			else
			{
				((ImageView) listViewContactos.get(posContact).findViewById(R.id.cliente_contacto_foto)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500));
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

}
