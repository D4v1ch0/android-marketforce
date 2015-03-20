package rp3.marketforce.ruta;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SpinnerAdapter;
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
	public static String ARG_IDAGENDA = "idagenda";
	public static String ARG_FROM = "from";

    public static final int DURACION_15_MINUTOS = 0;
    public static final int DURACION_30_MINUTOS = 1;
    public static final int DURACION_45_MINUTOS = 2;
    public static final int DURACION_60_MINUTOS = 3;
    public static final int DURACION_90_MINUTOS = 4;
    public static final int DURACION_120_MINUTOS = 5;
    public static final int DURACION_180_MINUTOS = 6;

    public static final int ID_DURACION = 0;
    public static final int ID_TIEMPO = 1;
	
	private AutoCompleteTextView cliente_auto;
	private ArrayList<String> list_nombres;
	private List<Cliente> list_cliente;
	private List<Tarea> list_tareas;
	private TimePicker desdePicker;
	private Calendar fecha;
    private TextView Duracion, TiempoViaje, DesdeText;
    private int TIME_PICKER_INTERVAL = 5;
     NumberPicker minutePicker;
     List<String> displayedValues;
     SimpleDateFormat format1 = new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm");
    private int duracion = 0, tiempo = 0;

	protected boolean mIgnoreEvent;
	private CaldroidFragment caldroidFragment;
    private String[] arrayDuracion;

    public static CrearVisitaFragment newInstance(long id, String text) {
		Bundle arguments = new Bundle();
        arguments.putLong(ARG_IDAGENDA, id);
        arguments.putString(ARG_FROM, text);
		CrearVisitaFragment fragment = new CrearVisitaFragment();
		fragment.setArguments(arguments);
		return fragment;
    }
	

	@Override
	public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		list_nombres = new ArrayList<String>();
		list_tareas = new ArrayList<Tarea>();
        arrayDuracion = this.getActivity().getResources()
                .getStringArray(R.array.arrayDuracion);

		cliente_auto = (AutoCompleteTextView) rootView.findViewById(R.id.crear_visita_cliente);
	    desdePicker = (TimePicker) rootView.findViewById(R.id.reprogramar_visita_desde);
	    
	    setTimePickerInterval(desdePicker);
	    
	    desdePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE) / 5);
		
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    
		list_cliente = Cliente.getClientAndContacts(getDataBase());
		for(Cliente cli : list_cliente)
		{
			list_nombres.add(cli.getNombreCompleto().trim());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),R.layout.spinner_small_text,list_nombres);

        SpinnerAdapter adapterDuracion = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, arrayDuracion);
        Duracion = ((TextView) rootView.findViewById(R.id.crear_visita_duracion));
        TiempoViaje = ((TextView) rootView.findViewById(R.id.crear_visita_tiempo_viaje));
        DesdeText = ((TextView) rootView.findViewById(R.id.crear_visita_desde_text));

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
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
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
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
						((Spinner) rootView.findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
					}
					
				}
			});
		}
		
		((Button) rootView.findViewById(R.id.crear_visita_conf_tarea)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				showDialogFragment(TareasFragment.newInstance(list_tareas), "Tareas");
			}});
		
		setCalendar();
        DesdeText.setText(format1.format(fecha.getTime()));
        Duracion.setText(arrayDuracion[0]);
        TiempoViaje.setText(arrayDuracion[0]);

        Duracion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDuracion(ID_DURACION);
            }
        });
        TiempoViaje.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDuracion(ID_TIEMPO);
            }
        });

        ((LinearLayout) rootView.findViewById(R.id.crear_visita_desde_clickable)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rootView.findViewById(R.id.crear_visita_desde).getVisibility() == View.VISIBLE)
                    rootView.findViewById(R.id.crear_visita_desde).setVisibility(View.GONE);
                else
                    rootView.findViewById(R.id.crear_visita_desde).setVisibility(View.VISIBLE);
            }
        });
		
		if(getArguments().getString(ARG_FROM).equalsIgnoreCase("Cliente"))
			setDatosCliente(getArguments().getLong(ARG_IDAGENDA));
		else
			setDatos(getArguments().getLong(ARG_IDAGENDA));
	}

	private void setDatosCliente(long id) {
		if(id != 0)
		{
			Cliente cli = Cliente.getClienteID(getDataBase(), id, false);

			cliente_auto.setText(cli.getNombreCompleto().trim());
			
			cliente_auto.dismissDropDown();
			ArrayList<String> direcciones = new ArrayList<String>();
			int position = list_nombres.indexOf(cliente_auto.getText().toString());
			if(position != -1)
			{
				for(ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones())
				{
					direcciones.add(cliDir.getDireccion());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
				((Spinner) getRootView().findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
			}
		}		
		
	}

	private void setDatos(long id) {
		if(id != 0)
		{
			Agenda agd = Agenda.getAgenda(getDataBase(), id);
			if(agd.getIdContacto() != 0 || agd.get_idContacto() != 0)
			{
				cliente_auto.setText(agd.getContacto().getApellido() + " " + agd.getContacto().getNombre() + " - " + agd.getContacto().getCargo()
				+ " de " + Cliente.getClienteIDServer(getDataBase(), agd.getIdCliente(), false).getNombreCompleto().trim());
			}
			else
			{
				cliente_auto.setText(agd.getCliente().getNombreCompleto());
			}
			
			cliente_auto.dismissDropDown();
			ArrayList<String> direcciones = new ArrayList<String>();
			int position = list_nombres.indexOf(cliente_auto.getText().toString());
			if(position != -1)
			{
				for(ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones())
				{
					direcciones.add(cliDir.getDireccion());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
				((Spinner) getRootView().findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
			}
		}		
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		super.setContentView(R.layout.layout_crear_visita);
		fecha = Calendar.getInstance();
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
                Field field2 = classForid.getField("hour");
                minutePicker = (NumberPicker) timePicker
                        .findViewById(field.getInt(null));

                minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                        fecha.set(Calendar.MINUTE, i2 * TIME_PICKER_INTERVAL);
                        DesdeText.setText(format1.format(fecha.getTime()));
                    }
                });

                ((NumberPicker) timePicker
                     .findViewById(field2.getInt(null))).setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                     @Override
                     public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                         fecha.set(Calendar.HOUR, i2);
                         DesdeText.setText(format1.format(fecha.getTime()));
                     }
                });

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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.action_save:
			Agenda agenda = new Agenda();

            switch(duracion)
            {
                case DURACION_15_MINUTOS: agenda.setDuracion(15); break;
                case DURACION_30_MINUTOS: agenda.setDuracion(30); break;
                case DURACION_45_MINUTOS: agenda.setDuracion(45); break;
                case DURACION_60_MINUTOS: agenda.setDuracion(60); break;
                case DURACION_90_MINUTOS: agenda.setDuracion(90); break;
                case DURACION_120_MINUTOS: agenda.setDuracion(120); break;
                case DURACION_180_MINUTOS: agenda.setDuracion(180); break;
            }

            switch(tiempo)
            {
                case DURACION_15_MINUTOS: agenda.setTiempoViaje(15); break;
                case DURACION_30_MINUTOS: agenda.setTiempoViaje(30); break;
                case DURACION_45_MINUTOS: agenda.setTiempoViaje(45); break;
                case DURACION_60_MINUTOS: agenda.setTiempoViaje(60); break;
                case DURACION_90_MINUTOS: agenda.setTiempoViaje(90); break;
                case DURACION_120_MINUTOS: agenda.setTiempoViaje(120); break;
                case DURACION_180_MINUTOS: agenda.setTiempoViaje(180); break;
            }
			
			Calendar cal_hoy = Calendar.getInstance();
			//if((fecha.get(Calendar.YEAR) < cal_hoy.get(Calendar.YEAR)) || (fecha.get(Calendar.MONTH) < cal_hoy.get(Calendar.MONTH))
			//		|| (fecha.get(Calendar.DATE) < cal_hoy.get(Calendar.DATE)) )
			//{
			//	Toast.makeText(getActivity(), "Fecha no puede ser anterior a hoy.", Toast.LENGTH_LONG).show();
			//	return true;
			//}
			
			Calendar cal = Calendar.getInstance();
			Calendar calFin = Calendar.getInstance();
			
			cal.set(Calendar.DATE, fecha.get(Calendar.DATE));
			cal.set(Calendar.MONTH, fecha.get(Calendar.MONTH));
			cal.set(Calendar.YEAR, fecha.get(Calendar.YEAR));
			
			calFin.set(Calendar.DATE, fecha.get(Calendar.DATE));
			calFin.set(Calendar.MONTH, fecha.get(Calendar.MONTH));
			calFin.set(Calendar.YEAR, fecha.get(Calendar.YEAR));
			
			cal.set(Calendar.HOUR_OF_DAY, desdePicker.getCurrentHour());
			cal.set(Calendar.MINUTE, desdePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);

			calFin.set(Calendar.HOUR_OF_DAY, desdePicker.getCurrentHour());
			calFin.set(Calendar.MINUTE, desdePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
            calFin.add(Calendar.MINUTE, (int) agenda.getDuracion());
			
			agenda.setFechaInicio(cal.getTime());
			agenda.setFechaFin(calFin.getTime());
			
			if(list_nombres.indexOf(cliente_auto.getText().toString()) == -1)
			{
				Toast.makeText(getContext(), "Nombre de Cliente o Contacto incorrecto.", Toast.LENGTH_LONG).show();
				return true;
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
			agenda.setCiudad(agenda.getCliente().getClienteDirecciones().get(getSpinnerSelectedPosition(R.id.crear_visita_direccion)).getCiudadDescripcion());
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
            int last = getDataBase().getIntLastInsertRowId();
            long last2 = getDataBase().getLongLastInsertRowId();

			
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
			break;
		case R.id.action_cancel:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
	
	private void setCalendar()
	{
		caldroidFragment = new CaldroidFragment();
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
		args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

		caldroidFragment.setArguments(args);

	    FragmentTransaction t = getFragmentManager().beginTransaction();
	    t.replace(R.id.crear_visita_calendar, caldroidFragment);
	    t.commit();

	    caldroidFragment.setMinDate(Calendar.getInstance().getTime());
	 	final CaldroidListener listener = new CaldroidListener() {

	 			@Override
	 			public void onSelectDate(Date date, View view) {
	 				caldroidFragment.setCalendarDate(date);
	 				caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, fecha.getTime());
	 				caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, date);
	 				fecha.setTime(date);
                    fecha.set(Calendar.HOUR_OF_DAY, desdePicker.getCurrentHour());
                    fecha.set(Calendar.MINUTE, desdePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                    DesdeText.setText(format1.format(fecha.getTime()));
	 			}

	 			@Override
	 			public void onChangeMonth(int month, int year) {
	 			}

	 			@Override
	 			public void onLongClickDate(Date date, View view) {
	 			}

	 			@Override
	 			public void onCaldroidViewCreated() {
	 			}

	 		};
	 	caldroidFragment.setCaldroidListener(listener);
	 	caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, fecha.getTime());
	}

    public void showDuracion(final int type)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                getContext());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.select_dialog_singlechoice, arrayDuracion);
        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type == ID_DURACION)
                        {
                            duracion = which;
                            Duracion.setText(arrayAdapter.getItem(which));
                            dialog.dismiss();
                        }
                        else if(type == ID_TIEMPO)
                        {
                            tiempo = which;
                            TiempoViaje.setText(arrayAdapter.getItem(which));
                            dialog.dismiss();
                        }

                    }
                });
        builderSingle.show();
    }
}
