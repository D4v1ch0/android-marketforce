package rp3.marketforce.ruta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AutoCompleteTextView.OnDismissListener;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;
import rp3.app.BaseFragment;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.Tarea;
import rp3.marketforce.ruta.TareasFragment.EditTareasDialogListener;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.Convert;

@SuppressLint("NewApi")
public class CrearVisitaFragment extends BaseFragment implements EditTareasDialogListener {
	
	public static String ARG_AGENDA = "agenda";
	
	private AutoCompleteTextView cliente_auto;
	private ArrayList<String> list_nombres;
	private List<Cliente> list_cliente;
	private List<Tarea> list_tareas;
	private DatePicker calendar;
	private TimePicker desdePicker;
	private TimePicker hastaPicker;
	TimePicker timePicker;
    private int TIME_PICKER_INTERVAL = 5;
     NumberPicker minutePicker;
     List<String> displayedValues;

	protected boolean mIgnoreEvent;

	public static CrearVisitaFragment newInstance() {
		CrearVisitaFragment fragment = new CrearVisitaFragment();
		return fragment;
    }
	

	@Override
	public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		list_nombres = new ArrayList<String>();
		list_tareas = new ArrayList<Tarea>();
		
		cliente_auto = (AutoCompleteTextView) rootView.findViewById(R.id.crear_visita_cliente);
		calendar = (DatePicker) rootView.findViewById(R.id.reprogramar_ruta_calendario);
	    desdePicker = (TimePicker) rootView.findViewById(R.id.reprogramar_visita_desde);
	    hastaPicker = (TimePicker) rootView.findViewById(R.id.reprogramar_visita_hasta);
	    
	    setTimePickerInterval(desdePicker);
	    setTimePickerInterval(hastaPicker);
	    
	    if(Calendar.getInstance().get(Calendar.MINUTE) > 30)
	    	hastaPicker.setCurrentHour(desdePicker.getCurrentHour() + 1);
	    else
		    hastaPicker.setCurrentHour(desdePicker.getCurrentHour());
	    
	    hastaPicker.setCurrentMinute(desdePicker.getCurrentMinute() + 30);
		
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.setMinDate(cal.getTimeInMillis());
	    
		list_cliente = Cliente.getClientAndContacts(getDataBase());
		for(Cliente cli : list_cliente)
		{
			list_nombres.add(cli.getNombreCompleto().trim());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,list_nombres);

		cliente_auto.setAdapter(adapter);
		cliente_auto.setThreshold(1);
		
		if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN){
		cliente_auto.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				ArrayList<String> direcciones = new ArrayList<String>();
				int position = list_nombres.indexOf(adapter.getItem(pos));
				if(position != -1)
				{
					for(ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones())
					{
						direcciones.add(cliDir.getDireccion());
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, direcciones);
					((Spinner) rootView.findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
				}
				
			}});
		} else{
			cliente_auto.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					ArrayList<String> direcciones = new ArrayList<String>();
					int position = list_nombres.indexOf(cliente_auto.getText().toString());
					if(position != -1)
					{
						for(ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones())
						{
							direcciones.add(cliDir.getDireccion());
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, direcciones);
						((Spinner) rootView.findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
					}
					
				}
			});
		}
		
		
		//Listener de botones a utilizar en pantalla
		((Button) rootView.findViewById(R.id.actividad_aceptar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Agenda agenda = new Agenda();
				
				Calendar cal = Calendar.getInstance();
				Calendar calFin = Calendar.getInstance();
				
				cal.set(Calendar.DATE, calendar.getDayOfMonth());
				cal.set(Calendar.MONTH, calendar.getMonth());
				cal.set(Calendar.YEAR, calendar.getYear());
				
				calFin.set(Calendar.DATE, calendar.getDayOfMonth());
				calFin.set(Calendar.MONTH, calendar.getMonth());
				calFin.set(Calendar.YEAR, calendar.getYear());
				
				cal.set(Calendar.HOUR_OF_DAY, desdePicker.getCurrentHour());
				cal.set(Calendar.MINUTE, desdePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
				
				calFin.set(Calendar.HOUR_OF_DAY, hastaPicker.getCurrentHour());
				calFin.set(Calendar.MINUTE, hastaPicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
				
				if(cal.getTime().getTime() > calFin.getTime().getTime())
				{
					Toast.makeText(getActivity(), "Fecha Desde no puede ser mayor a Fecha Hasta.", Toast.LENGTH_LONG).show();
					return;
				}
				
				agenda.setFechaInicio(cal.getTime());
				agenda.setFechaFin(calFin.getTime());
				
				if(list_nombres.indexOf(cliente_auto.getText().toString()) == -1)
				{
					Toast.makeText(getContext(), "Nombre de Cliente o Contacto incorrecto.", Toast.LENGTH_LONG).show();
					return;
				}
				Cliente cli = list_cliente.get(list_nombres.indexOf(cliente_auto.getText().toString()));
				if(cli.getTipoPersona().equalsIgnoreCase("C"))
				{
					Contacto cont = Contacto.getContactoId(getDataBase(), cli.getID());
					agenda.setIdContacto((int) cont.getIdContacto());
					agenda.set_idContacto(cli.getID());
				}
				if(cli.getIdCliente() == 0)
					agenda.setCliente(Cliente.getClienteID(getDataBase(), cli.getID(), true));
				else
					agenda.setCliente(Cliente.getClienteIDServer(getDataBase(), cli.getIdCliente(), true));
				agenda.setClienteDireccion(agenda.getCliente().getClienteDirecciones().get(getSpinnerSelectedPosition(R.id.crear_visita_direccion)));
				agenda.setCiudad(agenda.getCliente().getClienteDireccionPrincipal().getCiudadDescripcion());
				agenda.setDireccion(agenda.getCliente().getClienteDirecciones().get(getSpinnerSelectedPosition(R.id.crear_visita_direccion)).getDireccion());
				agenda.setEstadoAgenda(Contants.ESTADO_PENDIENTE);
				agenda.setEstadoAgendaDescripcion(Contants.DESC_PENDIENTE);
				agenda.setIdCliente((int) agenda.getCliente().getIdCliente());
				agenda.set_idCliente(agenda.getCliente().getID());
				agenda.setIdClienteDireccion(agenda.getClienteDireccion().getIdClienteDireccion());
				agenda.set_idClienteDireccion(agenda.getClienteDireccion().getID());
				agenda.setIdRuta(0);
				agenda.setNombreCompleto(agenda.getCliente().getNombreCompleto().trim());
				//agenda.setID(0);
				agenda.setIdAgenda(0);
				agenda.setEnviado(false);
				Agenda.insert(getDataBase(), agenda);
				
				for(Tarea tarea : list_tareas)
				{
					AgendaTarea agendaTarea = new AgendaTarea();
					agendaTarea.setIdAgenda(0);
					agendaTarea.set_idAgenda(agenda.getID());
					agendaTarea.setEstadoTarea("P");
					agendaTarea.setIdRuta(0);
					agendaTarea.setIdTarea(tarea.getIdTarea());
					AgendaTarea.insert(getDataBase(), agendaTarea);
				}
				
				//agenda.setAgendaTareaList(agendaTareas);
				//String json = AgendaToJSON(agenda);
				
				Bundle bundle = new Bundle();
				bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_INSERTAR_AGENDA);
				bundle.putLong(ARG_AGENDA, agenda.getID());
				requestSync(bundle);
				
				finish();
				
			}});
		
		((Button) rootView.findViewById(R.id.actividad_cancelar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}});
		
		((Button) rootView.findViewById(R.id.crear_visita_conf_tarea)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				showDialogFragment(TareasFragment.newInstance(list_tareas), "Tareas");
			}});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_crear_visita);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return super.onCreateView(inflater, container, savedInstanceState);
	}


	@Override
	public void onFinishTareasDialog(List<Tarea> tareas) {
		this.list_tareas = tareas;
	}
	
	@SuppressLint("NewApi")
    private void setTimePickerInterval(TimePicker timePicker) {
         try {
                Class<?> classForid = Class.forName("com.android.internal.R$id");
               // Field timePickerField = classForid.getField("timePicker");  

                Field field = classForid.getField("minute");
                minutePicker = (NumberPicker) timePicker
                        .findViewById(field.getInt(null));

                minutePicker.setMinValue(0);
                minutePicker.setMaxValue(11);
                displayedValues = new ArrayList<String>();
                for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                    displayedValues.add(String.format("%02d", i));
                }
                for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                    displayedValues.add(String.format("%02d", i));
                }
                minutePicker.setDisplayedValues(displayedValues
                        .toArray(new String[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
	
	public String AgendaToJSON(Agenda agenda)
	{
		JSONObject jObject = new JSONObject();
		try
		{
			jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(agenda.getFechaInicio()));
			jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(agenda.getFechaFin()));
			jObject.put("IdCliente", agenda.getIdCliente());
			jObject.put("IdClienteDireccion", agenda.getIdClienteDireccion());
			jObject.put("Ciudad", agenda.getCiudad());
			jObject.put("NombresCompletos", agenda.getNombreCompleto());
			jObject.put("Direccion", agenda.getDireccion());
			//jObject.put("IdClienteContacto", agenda.getIdContacto());
			
			JSONArray jArrayTareas = new JSONArray();
			for(AgendaTarea agt : agenda.getAgendaTareas())
			{
				JSONObject jObjectTarea = new JSONObject();
				jObjectTarea.put("IdTarea", agt.getIdTarea());				
				jArrayTareas.put(jObjectTarea);
			}
			
			jObject.put("AgendaTareas", jArrayTareas);
		}
		catch(Exception ex)
		{
			
		}
		return jObject.toString();
	}
}
