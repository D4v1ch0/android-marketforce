package rp3.auna.ventanueva;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.db.Contract;
import rp3.auna.models.Cliente;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.Tarea;
import rp3.auna.sync.SyncAdapter;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;

@SuppressLint("NewApi")
public class CrearLlamadaFragment extends BaseFragment{

    private static final String TAG = CrearLlamadaFragment.class.getSimpleName();
    public static String ARG_AGENDA = "agenda";
    public static String ARG_IDAGENDA = "idagenda";
    public static String ARG_FROM = "from";

    public static String ARG_CLIENTE = "idcliente";

    public static final int ID_DURACION = 0;
    public static final int ID_TIEMPO = 1;

    public static final int DIALOG_CREAR_LLAMADA = 1;

    private TextView cliente_auto;
    private SimpleGeneralValueAdapter adapterDuracion;
    private ArrayList<String> list_nombres;
    private List<Cliente> list_cliente;
    private List<Tarea> list_tareas;
    private TimePicker desdePicker;
    private Calendar fecha, fecha_hora,fechaLlamada;
    private TextView Duracion, TiempoViaje, DesdeText;
    private int TIME_PICKER_INTERVAL = 5;
    NumberPicker minutePicker;
    List<String> displayedValues;
    SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
    private int duracion = 0, tiempo = 0;

    protected boolean mIgnoreEvent;
    private CaldroidFragment caldroidFragment;
    private String[] arrayDuracion;

    /**
     * Atributos para la llamadaVta ganadora
     */
    Cliente cliente = null;
    rp3.auna.models.ventanueva.AgendaVta agendaVta = null;
    LlamadaVta llamadaVta = null;
    EditText etDescripcion ;
    int idAgente = 0;
    long idCliente = 0;

    public static CrearLlamadaFragment newInstance(long id, String text,long idCliente) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_IDAGENDA, id);
        arguments.putString(ARG_FROM, text);
        arguments.putLong(ARG_CLIENTE,idCliente);
        Log.d(TAG,"idCliente:"+idCliente);
        CrearLlamadaFragment fragment = new CrearLlamadaFragment();
        fragment.setArguments(arguments);
        Log.d(TAG,"ARG_IDAGENDA:"+id);
        Log.d(TAG,"ARG_FROM"+text);
        Log.d(TAG,"ARG_CLIENTE"+idCliente);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach...");
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        String lastText = "";
        list_nombres = new ArrayList<String>();
        llamadaVta = new LlamadaVta();
        agendaVta =new AgendaVta();
        cliente_auto = (TextView) rootView.findViewById(R.id.crear_llamada_cliente);
        etDescripcion = (EditText) rootView.findViewById(R.id.crear_llamada_descripcion);
        fecha_hora = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);

         idCliente = getArguments().getLong(ARG_CLIENTE);
        Log.d(TAG,"idCliente = "+idCliente);

        //cliente = Cliente.getClienteID(getDataBase(), idCliente, false);
        for(Cliente cliente : Cliente.getClientAndContacts(getDataBase())){
            Log.d(TAG,"for:"+cliente.toString());
            if(idCliente == cliente.getIdCliente()){
                cliente_auto.setText(cliente.getNombreCompleto());
                break;
            }
        }
        Log.d(TAG,"IdAgente:"+PreferenceManager.getInt(Contants.KEY_IDAGENTE));
        idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        llamadaVta.setIdAgente(idAgente);

        adapterDuracion = new SimpleGeneralValueAdapter(this.getContext(), getDataBase(), Contants.GENERAL_TABLE_DURACION_VISITA);
        //Duracion = ((TextView) rootView.findViewById(R.id.crear_llamada_cliente));
        DesdeText = ((TextView) rootView.findViewById(R.id.crear_llamada_desde_text));


        setCalendar();
        DesdeText.setText(format1.format(fecha.getTime()));

        List<String> stringList = new ArrayList<String>();
        for(int i = 0; i < adapterDuracion.getCount(); i ++)
            stringList.add(adapterDuracion.getGeneralValue(i).getValue());

        arrayDuracion = new String[stringList.size()];
        stringList.toArray(arrayDuracion);


        DesdeText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimePicker(1, fecha_hora.get(Calendar.HOUR_OF_DAY), fecha_hora.get(Calendar.MINUTE), TIME_PICKER_INTERVAL);
            }
        });

    }

    @Override
    public void onDailogTimePickerChange(int id, int hours, int minutes) {
        Log.d(TAG,"onDailogTimePickerChange..."+id+hours+minutes);
        fecha.set(Calendar.HOUR_OF_DAY, hours);
        fecha.set(Calendar.MINUTE, minutes);
        fechaLlamada.set(Calendar.HOUR_OF_DAY, hours);
        fechaLlamada.set(Calendar.MINUTE, minutes);
        DesdeText.setText(format1.format(fecha.getTime()));
        Log.d(TAG,"Horas:"+hours+" minutes:"+minutes);
        super.onDailogTimePickerChange(id, hours, minutes);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        setRetainInstance(true);
        super.setContentView(R.layout.layout_crear_llamada);
        fecha = Calendar.getInstance();
        fechaLlamada = Calendar.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:


                /*if(TextUtils.isEmpty((((EditText) getRootView().findViewById(R.id.crear_llamada_descripcion))).getText())){
                    Toast.makeText(getActivity(), "Llene una descripcion porfavor", Toast.LENGTH_SHORT).show();
                    break;
                };*/


                llamadaVta.setLatitud(1111111);
                llamadaVta.setLongitud(222222);
                llamadaVta.setInsertado(1);
                llamadaVta.setEstado(0);
                llamadaVta.setFechaFinLlamada(null);
                llamadaVta.setFechaInicioLlamada(null);
                llamadaVta.setDescripcion(((EditText) getRootView().findViewById(R.id.crear_llamada_descripcion)).getText().toString().trim());
                llamadaVta.setObservacion(null);
                llamadaVta.setIdCliente(Integer.parseInt(String.valueOf(idCliente)));
                llamadaVta.setMotivoReprogramacionValue(null);
                llamadaVta.setMotivoReprogramacionTabla(0);//Motivo del porque reprogramo
                llamadaVta.setMotivoVisitaTabla(0);
                llamadaVta.setMotivoVisitaValue(null);//Motivo del porque no llamo
                llamadaVta.setReferidoTabla(0);
                llamadaVta.setReferidoValue(null);//Dio un referido o no
                llamadaVta.setLlamadaTabla(Contants.GENERAL_TABLE_ESTADOS_LLAMADA);
                llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE);//Realizo o no la llamada
                llamadaVta.setLlamadaValor(0);//Concreto una visita
                llamadaVta.setDuracion(0);
                if(TextUtils.isEmpty((((EditText) getRootView().findViewById(R.id.crear_llamada_descripcion))).getText())){
                    llamadaVta.setDescripcion(null);
                }else{
                    llamadaVta.setDescripcion(etDescripcion.getText().toString().trim());
                }
                Log.d(TAG,"fechallamada:"+fechaLlamada.getTime().toString());
                llamadaVta.setFechaLlamada(fechaLlamada.getTime());
                Log.d(TAG, "idCliente"+idCliente);
                llamadaVta.setIdCliente(Integer.parseInt(String.valueOf(idCliente)));
                llamadaVta.setEstado(0);
                //agendaVta.setLlamada(llamadaVta);
                Log.d(TAG, llamadaVta.toString());
                showDialogConfirmation(DIALOG_CREAR_LLAMADA,"¿Desea agendar llamada?","Llamada");

                /*Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_INSERTAR_AGENDA);
                bundle.putLong(ARG_AGENDA, agenda.getID());
                requestSync(bundle);


                finish();*/
                break;
            case R.id.action_cancel:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveConfirmation(int id) {
        Log.d(TAG,"onPositiveConfirmation...");
        switch (id)
        {
            case DIALOG_CREAR_LLAMADA:
                Log.d(TAG,"DIALOG_CREAR_LLAMADA onPositiveConfirmation...");
                agendaVta.setIdCliente(Integer.parseInt(String.valueOf(idCliente)));
                agendaVta.setIdAgente(Integer.parseInt(String.valueOf(idAgente)));
                agendaVta.setIdAgenda(0);
                agendaVta.setLatitud(111111);
                agendaVta.setLongitud(22222);
                agendaVta.setVisita(null);
                agendaVta.setOrigenTabla(Contants.GENERAL_TABLE_ORIGENES_AGENDA);
                agendaVta.setOrigenValue(Contants.GENERAL_VALUE_ORIGEN_AGENDA_MOVIL);
                agendaVta.setEstado(0);
                agendaVta.setInsertado(1);
                boolean result = AgendaVta.insert(getDataBase(), agendaVta);
                if(result){
                    Log.d(TAG,"inserto agenda...");
                }
                else{
                    Log.d(TAG,"No se inserto agenda...");
                }
                int idAgendaMax = AgendaVta.getMaxIdAgendaVta(getDataBase());
                Log.d(TAG,"idMaxAgenda:"+idAgendaMax);
                Log.d(TAG,llamadaVta.toString());
                agendaVta.setLlamada(llamadaVta);
                String json = new Gson().toJson(agendaVta);
                Log.d(TAG,json);
                LlamadaVta.insert(getDataBase(),llamadaVta);
                int idLlamadaMax = LlamadaVta.getMaxIdAgendaVta(getDataBase());
                Log.d(TAG,"idMaxLlamada:"+idLlamadaMax);
                Log.d(TAG,"Cantidad de agendas:"+ AgendaVta.getAgendasInsert(getDataBase()).size());
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_INSERTAR_AGENDAVTA);
                bundle.putLong(ARG_AGENDA, 0);
                requestSync(bundle);
                finish();
                break;
        }
        super.onPositiveConfirmation(id);
    }

    private void setCalendar() {
        Log.d(TAG,"setCalendar...");
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        caldroidFragment.setArguments(args);

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.crear_llamada_calendar, caldroidFragment);
        t.commit();

        caldroidFragment.setMinDate(Calendar.getInstance().getTime());
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                caldroidFragment.setCalendarDate(date);
                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, fecha.getTime());
                caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, date);
                int horas = fecha.get(Calendar.HOUR_OF_DAY);
                int minutos = fecha.get(Calendar.MINUTE);
                fecha.setTime(date);
                fecha.set(Calendar.HOUR_OF_DAY, horas);
                fecha.set(Calendar.MINUTE, minutos);
                DesdeText.setText(format1.format(fecha.getTime()));
                fechaLlamada.setTime(date);
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
        caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, Calendar.getInstance().getTime());
        caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, fecha.getTime());
    }


}
