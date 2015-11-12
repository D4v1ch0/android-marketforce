package rp3.marketforce.ruta;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.maps.utils.SphericalUtil;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.actividades.ActividadActivity;
import rp3.marketforce.actividades.ActualizacionActivity;
import rp3.marketforce.actividades.CheckboxActivity;
import rp3.marketforce.actividades.GrupoActivity;
import rp3.marketforce.actividades.MultipleActivity;
import rp3.marketforce.actividades.SeleccionActivity;
import rp3.marketforce.actividades.TextoActivity;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.ruta.ObservacionesFragment.ObservacionesFragmentListener;
import rp3.marketforce.sync.AsyncUpdater;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.BitmapUtils;
import rp3.util.LocationUtils;
import rp3.util.LocationUtils.OnLocationResultListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class RutasDetailFragment extends rp3.app.BaseFragment implements ObservacionesFragmentListener {
    
    public static final String ARG_ITEM_ID = "idagenda";
    public static final String ARG_AGENDA_ID = "agenda";
    public static final String ARG_RUTA_ID = "ruta";

    public static final String ARG_LONGITUD = "longitud";
    public static final String ARG_LATITUD = "latitud";
    public static final String ARG_SOLO_VISTA = "solovista";

    public static final String PARENT_SOURCE_LIST = "LIST";
    public static final String PARENT_SOURCE_SEARCH = "SEARCH";
    
    public static final String STATE_IDAGENDA = "state_idagenda";
    
    private long idAgenda;        
    private Agenda agenda;
    private ListaTareasAdapter adapter;
    private ListView lista_tarea;
    private DrawableManager DManager;
    private boolean soloVista = true, clienteNull = false;
	private SimpleDateFormat format1;
	private SimpleDateFormat format2;
	protected ObservacionesFragment obsFragment;
    public boolean reDoMenu = true;
    Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
    private Menu menuRutas;
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
    public void onAfterCreateOptionsMenu(Menu menu) {
        if(reDoMenu)
        {
            menuRutas = menu;
            RefreshMenu();
        }

    	super.onAfterCreateOptionsMenu(menu);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
        if(agenda == null) {
            agenda = Agenda.getAgendaClienteNull(getDataBase(), idAgenda);
            clienteNull = true;
        }
        if (agenda.getIdContacto() != 0) {
                String apellido = "";
                if (agenda.getContacto().getApellido() != null)
                    apellido = agenda.getContacto().getApellido();
                setTextViewText(R.id.textView_name, agenda.getContacto().getNombre() + " " + apellido);
                ((ImageView) this.getRootView().findViewById(R.id.map_image)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.user),
                        getResources().getDimensionPixelOffset(R.dimen.image_size)));
                DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") +
                                rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agenda.getContacto().getURLFoto(),
                        (ImageView) this.getRootView().findViewById(R.id.map_image));
                if (agenda.getContacto().getCargo() != null)
                    setTextViewText(R.id.textView_tipo_canal, agenda.getContacto().getCargo().trim());
                else
                    setTextViewText(R.id.textView_tipo_canal, "");
                setTextViewText(R.id.textView_tipo_cliente, agenda.getContacto().getEmpresa().trim());
            } else {
                ((ImageView) this.getRootView().findViewById(R.id.map_image)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.user),
                        getResources().getDimensionPixelOffset(R.dimen.image_size)));
                DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") +
                                rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agenda.getCliente().getURLFoto(),
                        (ImageView) this.getRootView().findViewById(R.id.map_image));

                setTextViewText(R.id.textView_name, agenda.getCliente().getNombreCompleto());
                if(agenda.getCliente() != null)
                {
                    setTextViewText(R.id.textView_tipo_canal, agenda.getCliente().getCanalDescripcion());
                    setTextViewText(R.id.textView_tipo_cliente, agenda.getCliente().getTipoClienteDescripcion());
                }
                else
                {
                    setTextViewText(R.id.textView_tipo_canal, "");
                    setTextViewText(R.id.textView_tipo_cliente, "");
                }

            }
            if (agenda.getObservaciones() != null && agenda.getObservaciones().length() > 0) {
                setTextViewText(R.id.detail_agenda_observacion, agenda.getObservaciones());
                ((TextView) getRootView().findViewById(R.id.detail_agenda_observacion)).setTextColor(getResources().getColor(R.color.default_text));
            }
            else
            {
                if (!agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
                    setTextViewText(R.id.detail_agenda_observacion, getString(R.string.label_sin_observaciones));
            }


            if (agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO)) {
                ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
                setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
                setViewVisibility(R.id.detail_agenda_button_fin, View.VISIBLE);
                setViewVisibility(R.id.detail_agenda_button_cancelar, View.VISIBLE);
            }
            if (agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_NO_VISITADO)) {
                ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_unvisited);
                setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
                setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
                setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
            }
            if (agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_PENDIENTE)) {
                ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_pending);
                //getRootView().findViewById(R.id.detail_agenda_observacion).setClickable(false);
            }
            if (agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO)) {
                ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_reprogramed);
                //getRootView().findViewById(R.id.detail_agenda_observacion).setClickable(false);
            }
            if (agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_VISITADO)) {
                //getRootView().findViewById(R.id.detail_agenda_observacion).setClickable(false);
                ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
                setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
                setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
                setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
                setViewVisibility(R.id.detail_agenda_button_modificar, View.VISIBLE);
            }
            setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());

            if (!ValidarAgendas()) {
                setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
                setViewVisibility(R.id.detail_agenda_button_modificar, View.GONE);
            }

            ((ImageView) this.getRootView().findViewById(R.id.map_image)).setClickable(true);
            ((ImageView) this.getRootView().findViewById(R.id.map_image)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), FotoActivity.class);
                    intent.putExtra(ARG_ITEM_ID, agenda.getID());
                    startActivity(intent);
                }
            });
            adapter = new ListaTareasAdapter(getActivity(), agenda.getAgendaTareas());
            lista_tarea.setAdapter(adapter);
            adapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String path = "";
            if (data == null)
                path = photo.getPath();
            else
                path = Utils.getPath(data.getData(), getActivity());
            ((ImageView) this.getRootView().findViewById(R.id.map_image)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500));
            Cliente cli = Cliente.getClienteID(getDataBase(), agenda.getCliente().getID(), false);
            cli.setURLFoto(path);
            Cliente.update(getDataBase(), cli);
        }
    }
      
    
	@Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {    	
    	 
		if(idAgenda != 0){        	
        	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
        }
        if(agenda == null)
            agenda = Agenda.getAgendaClienteNull(getDataBase(), idAgenda);
		if(agenda != null){
           if(agenda.getCliente() != null)
           {
               if(agenda.getCliente().getCorreoElectronico() != null && agenda.getCliente().getCorreoElectronico().length() > 0) {
                   setTextViewText(R.id.textView_mail, agenda.getCliente().getCorreoElectronico());
                   ((TextView) rootView.findViewById(R.id.textView_mail)).setClickable(true);
                   ((TextView) rootView.findViewById(R.id.textView_mail)).setOnClickListener(new OnClickListener(){

                       @Override
                       public void onClick(View v) {
                           Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                   "mailto",agenda.getCliente().getCorreoElectronico(), null));
                           startActivity(Intent.createChooser(intent, "Send Email"));
                       }});
                   ((TextView) rootView.findViewById(R.id.textView_mail)).setPaintFlags(((TextView) rootView.findViewById(R.id.textView_mail)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                   ((TextView) rootView.findViewById(R.id.textView_mail)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
               }
               else {
                   setTextViewText(R.id.textView_mail, getResources().getString(R.string.label_sin_especificar));
                   ((TextView) rootView.findViewById(R.id.textView_mail)).setClickable(false);
               }

               if(agenda.getClienteDireccion().getTelefono1() != null && agenda.getClienteDireccion().getTelefono1().length() > 0) {
                   setTextViewText(R.id.textView_movil, agenda.getClienteDireccion().getTelefono1());
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
                   ((TextView) rootView.findViewById(R.id.textView_movil)).setPaintFlags(((TextView) rootView.findViewById(R.id.textView_movil)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                   ((TextView) rootView.findViewById(R.id.textView_movil)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
               }
               else {
                   setTextViewText(R.id.textView_movil, getResources().getString(R.string.label_sin_especificar));
                   ((TextView) rootView.findViewById(R.id.textView_movil)).setClickable(false);
               }
               setTextViewText(R.id.textView_address, agenda.getClienteDireccion().getDireccion());
           }
           else
           {
               setTextViewText(R.id.textView_address, agenda.getDireccion());
           }

		   setTextViewText(R.id.textView_fecha, format1.format(agenda.getFechaInicio()) + " - " + format2.format(agenda.getFechaFin()));		
		   if(agenda.getObservaciones() != null && agenda.getObservaciones().length() > 0)
		   {
			   setTextViewText(R.id.detail_agenda_observacion, agenda.getObservaciones());
			   ((TextView)rootView.findViewById(R.id.detail_agenda_observacion)).setTextColor(getResources().getColor(R.color.default_text));
		   }
		   
		   rootView.findViewById(R.id.detail_agenda_observacion).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ObservacionesActivity.class);
				intent.putExtra(ARG_ITEM_ID, agenda.getID());
                intent.putExtra(ARG_SOLO_VISTA, !agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO));
				startActivity(intent);
			}
            		   });
			
			
		   setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());

		   setButtonClickListener(R.id.detail_agenda_button_iniciar, new OnClickListener(){

			@Override
			public void onClick(View v) {
                setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
                setViewVisibility(R.id.detail_agenda_button_fin, View.VISIBLE);
                setViewVisibility(R.id.detail_agenda_button_cancelar, View.VISIBLE);
                getRootView().findViewById(R.id.detail_agenda_observacion).setClickable(true);
                agenda.setEstadoAgenda(Contants.ESTADO_GESTIONANDO);
                agenda.setEstadoAgendaDescripcion(Contants.DESC_GESTIONANDO);
                agenda.setFechaInicioReal(Calendar.getInstance().getTime());
                Agenda.update(getDataBase(), agenda);
                ((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
                setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
                if (agenda.getObservaciones() == null || agenda.getObservaciones().length() <= 0)
                    setTextViewText(R.id.detail_agenda_observacion, getString(R.string.label_agregue_observacion));
                LocationUtils.getLocation(getContext(), new OnLocationResultListener() {
                    @Override
                    public void getLocationResult(Location location) {

                    }
                });
            }});
		   
		   setButtonClickListener(R.id.detail_agenda_button_modificar, new OnClickListener() {

               @Override
               public void onClick(View v) {
                   setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
                   setViewVisibility(R.id.detail_agenda_button_fin, View.VISIBLE);
                   setViewVisibility(R.id.detail_agenda_button_cancelar, View.VISIBLE);
                   setViewVisibility(R.id.detail_agenda_button_modificar, View.GONE);
                   agenda = Agenda.getAgenda(getDataBase(), idAgenda);
                   if (agenda == null)
                       agenda = Agenda.getAgendaClienteNull(getDataBase(), idAgenda);
                   agenda.setEstadoAgenda(Contants.ESTADO_GESTIONANDO);
                   agenda.setEstadoAgendaDescripcion(Contants.DESC_GESTIONANDO);
                   getRootView().findViewById(R.id.detail_agenda_observacion).setClickable(true);
                   agenda.setFechaInicioReal(Calendar.getInstance().getTime());
                   Agenda.update(getDataBase(), agenda);
                   ((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
                   setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
                   if (agenda.getObservaciones() == null || agenda.getObservaciones().length() <= 0)
                       setTextViewText(R.id.detail_agenda_observacion, getString(R.string.label_agregue_observacion));

               }
           });
		   
		   setButtonClickListener(R.id.detail_agenda_button_cancelar, new OnClickListener() {

               @Override
               public void onClick(View v) {
                   setViewVisibility(R.id.detail_agenda_button_iniciar, View.VISIBLE);
                   setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
                   setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
                   //getRootView().findViewById(R.id.detail_agenda_observacion).setClickable(false);
                   if (agenda.getFechaFinReal() == null || agenda.getFechaFinReal().getTime() < 0) {
                       agenda.setEstadoAgenda(Contants.ESTADO_PENDIENTE);
                       agenda.setEstadoAgendaDescripcion(Contants.DESC_PENDIENTE);
                       ((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_pending);
                   } else {
                       agenda.setEstadoAgenda(Contants.ESTADO_VISITADO);
                       agenda.setEstadoAgendaDescripcion(Contants.DESC_VISITADO);
                       ((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
                   }
                   Agenda.update(getDataBase(), agenda);
                   setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
                   if (agenda.getObservaciones() == null || agenda.getObservaciones().length() <= 0)
                       setTextViewText(R.id.detail_agenda_observacion, getString(R.string.label_sin_observaciones));
               }
           });
		   
		   setButtonClickListener(R.id.detail_agenda_button_fin, new OnClickListener() {

               @Override
               public void onClick(View v) {
                   setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
                   setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
                   setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
                   setViewVisibility(R.id.detail_agenda_button_modificar, View.VISIBLE);
                   //getRootView().findViewById(R.id.detail_agenda_observacion).setClickable(false);
                   agenda = Agenda.getAgenda(getDataBase(), idAgenda);
                   if (agenda == null)
                       agenda = Agenda.getAgendaClienteNull(getDataBase(), idAgenda);
                   agenda.setEstadoAgenda(Contants.ESTADO_VISITADO);
                   agenda.setEstadoAgendaDescripcion(Contants.DESC_VISITADO);
                   agenda.setFechaFinReal(Calendar.getInstance().getTime());
                   final Context ctx = getContext();
                   Location location = LocationUtils.getLastLocation(ctx);
                   if (location != null) {
                       agenda.setLatitud(location.getLatitude());
                       agenda.setLongitud(location.getLongitude());
                       LatLng pos = new LatLng(agenda.getLatitud(), agenda.getLongitud());
                       LatLng cli = new LatLng(agenda.getClienteDireccion().getLatitud(), agenda.getClienteDireccion().getLongitud());
                       agenda.setDistancia((long) SphericalUtil.computeDistanceBetween(pos, cli));
                   }
                   agenda.setEnviado(false);
                   Agenda.update(getDataBase(), agenda);
                   if (agenda.getObservaciones() == null || agenda.getObservaciones().length() <= 0)
                       setTextViewText(R.id.detail_agenda_observacion, getString(R.string.label_sin_observaciones));
                   Bundle bundle = new Bundle();
                   bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ENVIAR_AGENDA);
                   bundle.putInt(ARG_AGENDA_ID, (int) idAgenda);
                   requestSync(bundle);
                   //new AsyncUpdater.UpdateAgenda().execute((int) idAgenda);
                   if (location == null) {
                       try {
                           LocationUtils.getLocation(ctx, new OnLocationResultListener() {

                               @Override
                               public void getLocationResult(Location location) {
                                   if (location != null) {
                                       agenda.setLatitud(location.getLatitude());
                                       agenda.setLongitud(location.getLongitude());
                                   }
                                   agenda.setEnviado(false);
                                   LatLng pos = new LatLng(agenda.getLatitud(), agenda.getLongitud());
                                   LatLng cli = new LatLng(agenda.getClienteDireccion().getLatitud(), agenda.getClienteDireccion().getLongitud());
                                   agenda.setDistancia((long) SphericalUtil.computeDistanceBetween(pos, cli));
                                   BaseActivity act = (BaseActivity) ctx;
                                   Agenda.update(act.getDataBase(), agenda);
                                   Bundle bundle = new Bundle();
                                   bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AGENDA_GEOLOCATION);
                                   bundle.putInt(ARG_AGENDA_ID, (int) idAgenda);
                                   bundle.putDouble(ARG_LONGITUD, location.getLongitude());
                                   bundle.putDouble(ARG_LATITUD, location.getLatitude());
                                   act.requestSync(bundle);

                               }
                           });
                       } catch (Exception ex) {
                       }
                   }


                   ((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
                   setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
               }
           });
		   
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

                    if(soloVista)
                        Toast.makeText(getContext(), R.string.message_solo_vista_tarea, Toast.LENGTH_LONG).show();

					AgendaTarea setter = adapter.getItem(position);
					if(setter.getTipoTarea().equalsIgnoreCase("A") || setter.getTipoTarea().equalsIgnoreCase("R"))
					{
						Actividad ata = Actividad.getActividadSimple(getDataBase(), setter.getIdTarea());
						if(ata.getTipo() != null)
						{
							if(ata.getTipo().equalsIgnoreCase("C") || ata.getTipo().equalsIgnoreCase("V"))
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
                    if(setter.getTipoTarea().equalsIgnoreCase("ADC") && !soloVista)
                        showTareaActualizacion(setter);
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
		if(Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_GESTIONANDO, 0, Agenda.getLastAgenda(getDataBase())) > 0 && !agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
		{
			Toast.makeText(getContext(), "No puede gestionar otra agenda, si existe otra con estado Gestionando.", Toast.LENGTH_LONG).show();
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

    protected void takePicture(final int idView) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this.getActivity());
        myAlertDialog.setTitle("Fotografía");
        myAlertDialog.setMessage("Obtener de");

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

        myAlertDialog.setNegativeButton("Cámara",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
                        getActivity().startActivityForResult(captureIntent, idView);

                    }
                });
        myAlertDialog.show();
    }
	
	public void showTareaTexto(Actividad ata, AgendaTarea setter)
	{
		Intent intent = new Intent(getContext(), TextoActivity.class);
		intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, setter.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
        intent.putExtra(ActividadActivity.ARG_AGENDA_INT, setter.get_idAgenda());
        intent.putExtra(ActividadActivity.ARG_TAREA, setter.getIdTarea());
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

    public void showTareaActualizacion(AgendaTarea agt)
    {
        if(!clienteNull) {
            Intent intent = new Intent(getContext(), ActualizacionActivity.class);
            intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
            intent.putExtra(ARG_AGENDA_ID, agt.getIdAgenda());
            intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
            intent.putExtra(ActividadActivity.ARG_AGENDA_INT, agenda.getID());
            intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
            intent.putExtra(ActividadActivity.ARG_TITULO, agt.getNombreTarea());
            startActivity(intent);
        }
        else
            Toast.makeText(this.getContext(), "Cliente esta eliminado de la ruta. No se puede actualizar.", Toast.LENGTH_LONG).show();
    }

	@Override
	public void onResumir() {
		this.onResume();
		
	}

    public void RefreshMenu()
    {
        menuRutas.findItem(R.id.action_search_ruta).setVisible(false);
        menuRutas.findItem(R.id.action_crear_visita).setVisible(false);
        Agenda agendaNoClient = Agenda.getAgenda(getDataBase(), idAgenda);
        for(int i = 0; i < menuRutas.size(); i ++)
        {
            if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
            {
                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(agendaNoClient != null);
                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(agendaNoClient != null);
                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_suspender_agenda).setVisible(true);
                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(true);
                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(true);
                if(agenda.getPedido().getID() != 0)
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setTitle("Editar Pedido");
            }
        }
        if(idAgenda != 0)
        {
            String estado = Agenda.getAgendaEstado(getDataBase(), idAgenda);
            if(estado.equalsIgnoreCase(Contants.ESTADO_NO_VISITADO) || estado.equalsIgnoreCase(Contants.ESTADO_VISITADO)) {
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(false);
                        menuRutas.findItem(R.id.submenu_agenda).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                    }
                }
            }
            else
                menuRutas.findItem(R.id.submenu_agenda).setVisible(true);
            if(!estado.equalsIgnoreCase(Contants.ESTADO_PENDIENTE) && !estado.equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
            {
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_suspender_agenda).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                    }
                }
            }
            if(estado.equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
            {
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(true);
                    }
                }
            }
        }
    }
}
