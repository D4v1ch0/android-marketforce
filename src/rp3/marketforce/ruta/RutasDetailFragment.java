package rp3.marketforce.ruta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.actividades.ActividadActivity;
import rp3.marketforce.actividades.CheckboxActivity;
import rp3.marketforce.actividades.GrupoActivity;
import rp3.marketforce.actividades.MultipleActivity;
import rp3.marketforce.actividades.SeleccionActivity;
import rp3.marketforce.actividades.TextoActivity;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.ruta.ObservacionesFragment.ObservacionesFragmentListener;
import rp3.marketforce.sync.EnviarUbicacion;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.BitmapUtils;
import rp3.util.LocationUtils;
import rp3.util.LocationUtils.OnLocationResultListener;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Intents;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RutasDetailFragment extends rp3.app.BaseFragment implements ObservacionesFragmentListener {
    
    public static final String ARG_ITEM_ID = "idagenda";
    public static final String ARG_AGENDA_ID = "agenda";
    public static final String ARG_RUTA_ID = "ruta";

    public static final String PARENT_SOURCE_LIST = "LIST";
    public static final String PARENT_SOURCE_SEARCH = "SEARCH";
    
    public static final String STATE_IDAGENDA = "state_idagenda";
    
    private long idAgenda;        
    private Agenda agenda;
    private ListaTareasAdapter adapter;
    private ListView lista_tarea;
    private DrawableManager DManager;
    private boolean soloVista = true;
	private SimpleDateFormat format1;
	private SimpleDateFormat format2;
	protected ObservacionesFragment obsFragment;
    
    public interface TransactionDetailListener{
    	public void onDeleteSuccess(Cliente transaction);
    }
    
    public static RutasDetailFragment newInstance(long idAgenda){
    	Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, idAgenda);
        RutasDetailFragment fragment = new RutasDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
      

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        format1 = new SimpleDateFormat("EEEE dd MMMM yyyy, HH:mm");
        format2 = new SimpleDateFormat("HH:mm");

        if(getParentFragment()==null)
        	setRetainInstance(true);
        
        if (getArguments().containsKey(ARG_ITEM_ID)) {            
            idAgenda = getArguments().getLong(ARG_ITEM_ID);   
        }else if(savedInstanceState!=null){
        	idAgenda = savedInstanceState.getLong(STATE_IDAGENDA);
        }    
        
        DManager = new DrawableManager();
        
        if(idAgenda != 0){
        	super.setContentView(R.layout.fragment_rutas_detalle);
        }
        else{
        	super.setContentView(R.layout.base_content_no_selected_item);
        }                        
    }

    @Override
    public void onAttach(Activity activity) {    	
    	super.onAttach(activity);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
    	if(agenda.getIdContacto() != 0)
		  {
	    	  String apellido = "";
			  if(agenda.getContacto().getApellido() != null)
				  apellido = agenda.getContacto().getApellido();
			  setTextViewText(R.id.textView_name, agenda.getContacto().getNombre() + " " + apellido);
			  ((ImageView) this.getRootView().findViewById(R.id.map_image)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
						BitmapFactory.decodeResource(getResources(), R.drawable.user), 
						getResources().getDimensionPixelOffset(R.dimen.image_size)));
			  DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") + 
						rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agenda.getContacto().getURLFoto(),
						(ImageView) this.getRootView().findViewById(R.id.map_image));
			  if(agenda.getContacto().getCargo() != null)
				  setTextViewText(R.id.textView_tipo_canal, agenda.getContacto().getCargo().trim());
			  else
				  setTextViewText(R.id.textView_tipo_canal, "");
			  setTextViewText(R.id.textView_tipo_cliente, agenda.getContacto().getEmpresa().trim());
		  }
		  else
		  {
			  ((ImageView) this.getRootView().findViewById(R.id.map_image)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
						BitmapFactory.decodeResource(getResources(), R.drawable.user), 
						getResources().getDimensionPixelOffset(R.dimen.image_size)));
			  DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") + 
						rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agenda.getCliente().getURLFoto(),
						(ImageView) this.getRootView().findViewById(R.id.map_image));
						   
			  setTextViewText(R.id.textView_name, agenda.getNombreCompleto());
		  }
    	if(agenda.getObservaciones() != null && agenda.getObservaciones().length() > 0)
		   {
			   setTextViewText(R.id.detail_agenda_observacion, agenda.getObservaciones());
			   ((TextView)getRootView().findViewById(R.id.detail_agenda_observacion)).setTextColor(getResources().getColor(R.color.default_text));
		   }
    	
    	if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
		   {
				((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
				setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_fin, View.VISIBLE);
				setViewVisibility(R.id.detail_agenda_button_cancelar, View.VISIBLE);
		   }
			if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_NO_VISITADO))
			{
				((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_unvisited);
				setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
			}
			if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_PENDIENTE))
				((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_pending);
			if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
				((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_reprogramed);
			if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_VISITADO))
			{
				((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
				setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_modificar, View.VISIBLE);
			}
			
			if(!ValidarAgendas())
				   setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
    	
    	adapter = new ListaTareasAdapter(getActivity(), agenda.getAgendaTareas());
    	lista_tarea.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    }
      
    
	@Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {    	
    	 
		if(idAgenda != 0){        	
        	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
        }
		if(agenda != null){					  
		   setTextViewText(R.id.textView_movil, agenda.getClienteDireccion().getTelefono1());
		   setTextViewText(R.id.textView_mail, agenda.getCliente().getCorreoElectronico());
		   setTextViewText(R.id.textView_address, agenda.getClienteDireccion().getDireccion());
		   setTextViewText(R.id.textView_fecha, format1.format(agenda.getFechaInicio()) + " - " + format2.format(agenda.getFechaFin()));		
		   if(agenda.getObservaciones() != null && agenda.getObservaciones().length() > 0)
		   {
			   setTextViewText(R.id.detail_agenda_observacion, agenda.getObservaciones());
			   ((TextView)rootView.findViewById(R.id.detail_agenda_observacion)).setTextColor(getResources().getColor(R.color.default_text));
		   }
		   
		   rootView.findViewById(R.id.detail_agenda_observacion).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				obsFragment = ObservacionesFragment.newInstance(agenda.getID());
				showDialogFragment(obsFragment, "");
			}
		   });
			
			
		   setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());

				((TextView) rootView.findViewById(R.id.textView_mail)).setClickable(true);
				((TextView) rootView.findViewById(R.id.textView_mail)).setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
							            "mailto",agenda.getCliente().getCorreoElectronico(), null));
								startActivity(Intent.createChooser(intent, "Send Email"));
							}});
				((TextView) rootView.findViewById(R.id.textView_movil)).setClickable(true);
				((TextView) rootView.findViewById(R.id.textView_movil)).setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v) {
								String uri = "tel:" + agenda.getClienteDireccion().getTelefono1();
								Intent intent = new Intent(Intent.ACTION_DIAL);
								intent.setData(Uri.parse(uri));
								Uri mUri = Uri.parse("smsto:" + Utils.convertToSMSNumber(agenda.getClienteDireccion().getTelefono1()));
					            Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
					            mIntent.putExtra("chat",true);
					            Intent chooserIntent = Intent.createChooser(mIntent, "Seleccionar Acción");
					            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent });
					            startActivity(chooserIntent);
							}});
		   setButtonClickListener(R.id.detail_agenda_button_iniciar, new OnClickListener(){

			@Override
			public void onClick(View v) {
					setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
					setViewVisibility(R.id.detail_agenda_button_fin, View.VISIBLE);
					setViewVisibility(R.id.detail_agenda_button_cancelar, View.VISIBLE);
					agenda.setEstadoAgenda(Contants.ESTADO_GESTIONANDO);
					agenda.setEstadoAgendaDescripcion(Contants.DESC_GESTIONANDO);
					agenda.setFechaInicioReal(Calendar.getInstance().getTime());
					Agenda.update(getDataBase(), agenda);
					((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
					 setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
				
			}});
		   
		   setButtonClickListener(R.id.detail_agenda_button_modificar, new OnClickListener(){

				@Override
				public void onClick(View v) {
					setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
					setViewVisibility(R.id.detail_agenda_button_fin, View.VISIBLE);
					setViewVisibility(R.id.detail_agenda_button_cancelar, View.VISIBLE);
					setViewVisibility(R.id.detail_agenda_button_modificar, View.GONE);
					agenda.setEstadoAgenda(Contants.ESTADO_GESTIONANDO);
					agenda.setEstadoAgendaDescripcion(Contants.DESC_GESTIONANDO);
					agenda.setFechaInicioReal(Calendar.getInstance().getTime());
					Agenda.update(getDataBase(), agenda);
					((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
					 setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
					
				}});
		   
		   setButtonClickListener(R.id.detail_agenda_button_cancelar, new OnClickListener(){

				@Override
				public void onClick(View v) {
					setViewVisibility(R.id.detail_agenda_button_iniciar, View.VISIBLE);
					setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
					setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
					if(agenda.getFechaFinReal() == null || agenda.getFechaFinReal().getTime() < 0)
					{
						agenda.setEstadoAgenda(Contants.ESTADO_PENDIENTE);
						agenda.setEstadoAgendaDescripcion(Contants.DESC_PENDIENTE);
						((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_pending);
					}
					else
					{
						agenda.setEstadoAgenda(Contants.ESTADO_VISITADO);
						agenda.setEstadoAgendaDescripcion(Contants.DESC_VISITADO);
						((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
					}
					Agenda.update(getDataBase(), agenda);
					 setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
				}});
		   
		   setButtonClickListener(R.id.detail_agenda_button_fin, new OnClickListener(){

				@Override
				public void onClick(View v) {
					setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
					setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
					setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
					setViewVisibility(R.id.detail_agenda_button_modificar, View.VISIBLE);
					agenda = Agenda.getAgenda(getDataBase(), idAgenda);
					agenda.setEstadoAgenda(Contants.ESTADO_VISITADO);
					agenda.setEstadoAgendaDescripcion(Contants.DESC_VISITADO);
					agenda.setFechaFinReal(Calendar.getInstance().getTime());
					final Context ctx = getContext();
					try
					{
					LocationUtils.getLocation(ctx, new OnLocationResultListener() {
						
						@Override
						public void getLocationResult(Location location) {				
							if(location!=null){		
								agenda.setLatitud(location.getLatitude());
								agenda.setLongitud(location.getLongitude());
							}
								BaseActivity act = (BaseActivity) ctx;
								Agenda.update(act.getDataBase(), agenda);
								Bundle bundle = new Bundle();
								bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ENVIAR_AGENDA);
								bundle.putInt(ARG_AGENDA_ID, (int) idAgenda);
								act.requestSync(bundle);
	
						}
					});
					}
					catch(Exception ex)
					{	}
					Agenda.update(getDataBase(), agenda);
					((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
					setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());	
				}});
		   
		   if(agenda.getAgendaTareas() != null){
			   adapter = new ListaTareasAdapter(getActivity(), agenda.getAgendaTareas());
			   lista_tarea = (ListView) rootView.findViewById(R.id.listView_tareas);
			   lista_tarea.setAdapter(adapter);
			   lista_tarea.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
						soloVista = false;
					else
						soloVista = true;
					
					AgendaTarea setter = adapter.getItem(position);
					if(setter.getTipoTarea().equalsIgnoreCase("A") || setter.getTipoTarea().equalsIgnoreCase("R"))
					{
						Actividad ata = Actividad.getActividadSimple(getDataBase(), setter.getIdTarea());
						if(ata.getTipo() != null)
						{
							if(ata.getTipo().equalsIgnoreCase("C"))
								showTareaCheckbox(ata, setter);	
							if(ata.getTipo().equalsIgnoreCase("M"))
								showTareaMultiSeleccion(ata, setter);
							if(ata.getTipo().equalsIgnoreCase("S"))
								showTareaSeleccion(ata, setter);
							if(ata.getTipo().equalsIgnoreCase("T"))
								showTareaTexto(ata, setter);
						}
					}
					if(setter.getTipoTarea().equalsIgnoreCase("E"))
						showTareaGrupo(setter);	
				}});
			   
			   if(agenda.getAgendaTareas().size() == 0)
			   {
				   rootView.findViewById(R.id.listView_tareas).setVisibility(View.GONE);
				   rootView.findViewById(R.id.detail_agenda_empty_tareas).setVisibility(View.VISIBLE);
			   }
		   }
		   else
		   {
			   rootView.findViewById(R.id.listView_tareas).setVisibility(View.GONE);
			   rootView.findViewById(R.id.detail_agenda_empty_tareas).setVisibility(View.VISIBLE);
		   }
		   
		}
    }
      
    protected boolean ValidarAgendas() {
		if(Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_GESTIONANDO, 0, Agenda.getLastAgenda(getDataBase())) > 0)
		{
			return false;
		}
		
		Calendar cal = Calendar.getInstance();
		Calendar cal_agenda = Calendar.getInstance();
		cal_agenda.setTime(agenda.getFechaInicio());
		if(cal.get(Calendar.DAY_OF_MONTH) != cal_agenda.get(Calendar.DAY_OF_MONTH) ||
				cal.get(Calendar.MONTH) != cal_agenda.get(Calendar.MONTH) ||
				cal.get(Calendar.YEAR) != cal_agenda.get(Calendar.YEAR))
		{
			return false;
		}
		return true;
	}


	@Override
    public void onSaveInstanceState(Bundle outState) {
    	outState.putLong(STATE_IDAGENDA, idAgenda);    	
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
	
	public void showTareaTexto(Actividad ata, AgendaTarea setter)
	{
		Intent intent = new Intent(getContext(), TextoActivity.class);
		intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID,(long) setter.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
		intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
		intent.putExtra(ActividadActivity.ARG_TITULO, setter.getNombreTarea());
		startActivity(intent);
	}
	
	public void showTareaSeleccion(Actividad ata, AgendaTarea setter)
	{
		Intent intent = new Intent(getContext(), SeleccionActivity.class);
		intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, setter.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
		intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
		intent.putExtra(ActividadActivity.ARG_TITULO, setter.getNombreTarea());
		startActivity(intent);
	}
	public void showTareaMultiSeleccion(Actividad ata, AgendaTarea setter)
	{
		Intent intent = new Intent(getContext(), MultipleActivity.class);
		intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, setter.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
		intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
		intent.putExtra(ActividadActivity.ARG_TITULO, setter.getNombreTarea());
		startActivity(intent);
	}
    
	public void showTareaCheckbox(Actividad ata, AgendaTarea setter)
	{
		Intent intent = new Intent(getContext(), CheckboxActivity.class);
		intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, setter.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
		intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
		intent.putExtra(ActividadActivity.ARG_TITULO, setter.getNombreTarea());
		startActivity(intent);
	}
	
	public void showTareaGrupo(AgendaTarea agt)
	{
		Intent intent = new Intent(getContext(), GrupoActivity.class);
		intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, agt.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
		intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
		intent.putExtra(ActividadActivity.ARG_TITULO, agt.getNombreTarea());
		startActivity(intent);
	}


	@Override
	public void onResumir() {
		this.onResume();
		
	}
}
