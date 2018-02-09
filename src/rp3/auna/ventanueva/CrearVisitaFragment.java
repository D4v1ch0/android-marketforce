package rp3.auna.ventanueva;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import rp3.auna.models.Cliente;
import rp3.auna.models.ClienteDireccion;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.sync.SyncAdapter;
import rp3.configuration.PreferenceManager;

/**
 * Created by Jesus Villa on 22/09/2017.
 */

public class CrearVisitaFragment extends BaseFragment {

    private static final String TAG = CrearVisitaFragment.class.getSimpleName();
    public static String ARG_CLIENTE = "idcliente";
    public static final int DIALOG_CREAR_VISITA = 1;
    private Calendar fechaLlamada, fecha,fecha_hora;
    private TextView cliente_auto;
    private TextView desdeText;
    private EditText etDescripcion;
    private CaldroidFragment caldroidFragment;
    private SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
    private long idCliente;
    private int idAgente;
    private int idClienteDireccion;
    private Cliente cliente;
    private VisitaVta visita;
    private Spinner spDireccion;
    private ArrayAdapter adapter;
    private ArrayList<String> listDirecciones;
    private List<ClienteDireccion> direccions;
    private ClienteDireccion direccionSelected;
    private int TIME_PICKER_INTERVAL = 5;
    private AgendaVta agendaVta;

    public static CrearVisitaFragment newInstance(long id, String text,long idCliente) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_CLIENTE,idCliente);
        Log.d(TAG,"idCliente:"+idCliente);
        CrearVisitaFragment fragment = new CrearVisitaFragment();
        fragment.setArguments(arguments);
        Log.d(TAG,"ARG_CLIENTE"+idCliente+" crear visita");
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
        cliente_auto = (TextView) rootView.findViewById(R.id.crear_visita_prospecto_cliente);
        fecha_hora = Calendar.getInstance();
        etDescripcion = (EditText) rootView.findViewById(R.id.etcrear_visita_prospecto_descripcion);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);

        idCliente = getArguments().getLong(ARG_CLIENTE);
        Log.d(TAG,"idCliente = "+idCliente);
        for(Cliente cliente : Cliente.getClientAndContacts(getDataBase())){
            Log.d(TAG,"for:"+cliente.toString());
            if(idCliente == cliente.getIdCliente()){
                this.cliente = cliente;
                cliente_auto.setText(cliente.getNombreCompleto());
                break;
            }
        }
        Log.d(TAG,cliente.toString());

        agendaVta = new AgendaVta();
        Log.d(TAG,"IdAgente:"+ PreferenceManager.getInt(Contants.KEY_IDAGENTE));
        idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        visita = new VisitaVta();
        spDireccion = (Spinner)rootView.findViewById(R.id.crear_visita_prospecto_direccion);

        listDirecciones = new ArrayList<>();
        direccions = cliente.getClienteDirecciones();
        for(ClienteDireccion clienteDireccion:cliente.getClienteDirecciones()){
            listDirecciones.add(clienteDireccion.getDireccion());
        }
        adapter = new ArrayAdapter(getContext(),R.layout.spinner_small_text,listDirecciones);
        spDireccion.setAdapter(adapter);
        spDireccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                direccionSelected = direccions.get(position);
                Log.d(TAG,"onItemSelected Direccion:"+direccionSelected.getDireccion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        desdeText = ((TextView) rootView.findViewById(R.id.crear_visita_prospecto_desde_text));
        setCalendar();
        desdeText.setText(format1.format(fecha.getTime()));
        desdeText.setOnClickListener(new View.OnClickListener() {
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
        desdeText.setText(format1.format(fecha.getTime()));
        Log.d(TAG,"Horas:"+hours+" minutes:"+minutes);
        super.onDailogTimePickerChange(id, hours, minutes);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate visita fragment...");
        setRetainInstance(true);
        super.setContentView(R.layout.layout_crear_visita_prospecto);
        fecha = Calendar.getInstance();
        fechaLlamada = Calendar.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        return super.onCreateView(inflater, container, savedInstanceState);
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
        t.replace(R.id.crear_visita_prospecto_calendar, caldroidFragment);
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
                desdeText.setText(format1.format(fecha.getTime()));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:


                if(TextUtils.isEmpty((((EditText) getRootView().findViewById(R.id.etcrear_visita_prospecto_descripcion))).getText())){
                    Toast.makeText(getActivity(), "Llene una descripcion porfavor", Toast.LENGTH_SHORT).show();
                    break;
                }

                showDialogConfirmation(DIALOG_CREAR_VISITA,"¿Desea agendar visita?","Visita");

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
            case DIALOG_CREAR_VISITA:
                Log.d(TAG,"DIALOG_CREAR_VISITA onPositiveConfirmation...");
                agendaVta.setInsertado(1);
                agendaVta.setLlamada(null);
                agendaVta.setOrigenTabla(Contants.GENERAL_TABLE_ORIGENES_AGENDA);
                agendaVta.setLatitud(11111111.0);
                agendaVta.setLongitud(222222.0);
                agendaVta.setEstado(0);
                agendaVta.setOrigenValue(Contants.GENERAL_VALUE_ORIGEN_AGENDA_MOVIL);
                agendaVta.setIdAgente(idAgente);
                agendaVta.setIdCliente(Integer.parseInt(String.valueOf(idCliente)));
                int idMaxAntes = AgendaVta.getMaxIdAgendaVta(getDataBase());
                Log.d(TAG,"idMax antes:"+idMaxAntes);
                boolean result = AgendaVta.insert(getDataBase(),agendaVta);
                if(result){
                    Log.d(TAG,"Agenda insertada...");
                }else{
                    Log.d(TAG,"No inserto agenda...");
                }
                int idMaxDespues = AgendaVta.getMaxIdAgendaVta(getDataBase());
                Log.d(TAG,"idMax despues:"+idMaxDespues);

                visita.setFechaVisita(fechaLlamada.getTime());
                visita.setFotos(null);
                visita.setInsertado(1);
                if(TextUtils.isEmpty(etDescripcion.getText().toString().trim())){
                    visita.setDescripcion(null);
                }else{
                    visita.setDescripcion(etDescripcion.getText().toString().trim());
                }
                visita.setEstado(1);
                visita.setFechaFin(null);
                visita.setFechaInicio(null);
                visita.setIdAgente(idAgente);
                visita.setIdCliente(Integer.parseInt(String.valueOf(idCliente)));
                //visita.setIdClienteDireccion(direccionSelected.getIdClienteDireccion());
                visita.setLatitud(111111.0);
                visita.setLongitud(22222.0);
                visita.setMotivoReprogramacionTabla(0);
                visita.setMotivoReprogramacionValue(null);//Indisponibilidad del cliente
                visita.setObservacion(null);
                visita.setMotivoVisitaTabla(Contants.GENERAL_TABLE_ESTADOS_VISITA);
                visita.setMotivoVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE);
                visita.setReferidoTabla(Contants.GENERAL_TABLE_ESTADOS_CONSULTA_REFERIDO);
                visita.setReferidoValue(Contants.GENERAL_VALUE_CONSULTA_REFERIDO_NO);
                visita.setVisitaTabla(Contants.GENERAL_TABLE_ESTADOS_VISITA);
                visita.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE);
                VisitaVta.insert(getDataBase(),visita);
                Log.d(TAG,"Cantidad de visitas:"+ LlamadaVta.getLlamadasInsert(getDataBase()).size());
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_INSERTAR_AGENDAVTA);
               // bundle.putLong(ARG_AGENDA, 0);
                requestSync(bundle);
                finish();
                break;
        }
        super.onPositiveConfirmation(id);
    }
}
