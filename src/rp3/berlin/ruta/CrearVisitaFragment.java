package rp3.berlin.ruta;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AutoCompleteTextView.OnDismissListener;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.Agenda;
import rp3.berlin.models.AgendaTarea;
import rp3.berlin.models.Cliente;
import rp3.berlin.models.ClienteDireccion;
import rp3.berlin.models.Contacto;
import rp3.berlin.models.Tarea;
import rp3.berlin.models.pedido.Pedido;
import rp3.berlin.ruta.TareasFragment.EditTareasDialogListener;
import rp3.berlin.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import rp3.util.Convert;

@SuppressLint("NewApi")
public class CrearVisitaFragment extends BaseFragment implements EditTareasDialogListener {

    public static String ARG_AGENDA = "agenda";
    public static String ARG_IDAGENDA = "idagenda";
    public static String ARG_FROM = "from";

    public static final int ID_DURACION = 0;
    public static final int ID_TIEMPO = 1;

    public static final int DIALOG_PEDIDO_INCONCLUSO = 1;

    private AutoCompleteTextView cliente_auto;
    private SimpleGeneralValueAdapter adapterDuracion;
    private ArrayList<String> list_nombres;
    private List<Cliente> list_cliente;
    private List<Tarea> list_tareas;
    private TimePicker desdePicker;
    private Calendar fecha, fecha_hora;
    private TextView Duracion, TiempoViaje, DesdeText;
    private int TIME_PICKER_INTERVAL = 5;
    NumberPicker minutePicker;
    List<String> displayedValues;
    SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        String lastText = "";
        list_nombres = new ArrayList<String>();
        if (list_tareas == null)
            list_tareas = new ArrayList<Tarea>();

        Tarea tareaDefault = Tarea.getTareaId(getDataBase(), 244);
        list_tareas.add(tareaDefault);
        onFinishTareasDialog(list_tareas);
        getRootView().findViewById(R.id.crear_visita_valor_layout).setVisibility(View.VISIBLE);

        if (cliente_auto != null)
            lastText = cliente_auto.getText().toString();

        cliente_auto = (AutoCompleteTextView) rootView.findViewById(R.id.crear_visita_cliente);
        fecha_hora = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);

        list_cliente = Cliente.getClientAndContacts(getDataBase());
        for (Cliente cli : list_cliente) {
            if(cli.getIdExterno() == null || cli.getIdExterno().trim().length() <= 0)
                list_nombres.add(cli.getNombreCompleto().trim());
            else
                list_nombres.add(cli.getIdExterno() + " - " + cli.getNombreCompleto().trim());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_small_text, list_nombres);

        adapterDuracion = new SimpleGeneralValueAdapter(this.getContext(), getDataBase(), Contants.GENERAL_TABLE_DURACION_VISITA);
        Duracion = ((TextView) rootView.findViewById(R.id.crear_visita_duracion));
        TiempoViaje = ((TextView) rootView.findViewById(R.id.crear_visita_tiempo_viaje));
        DesdeText = ((TextView) rootView.findViewById(R.id.crear_visita_desde_text));

        cliente_auto.setAdapter(adapter);
        cliente_auto.setThreshold(1);

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cliente_auto.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int pos, long id) {
                    ArrayList<String> direcciones = new ArrayList<String>();
                    int position = list_nombres.indexOf(adapter.getItem(pos));
                    if (position != -1) {
                        for (ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones()) {
                            direcciones.add(cliDir.getDireccion());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
                        ((Spinner) rootView.findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
                    }

                }
            });


        } else {
            cliente_auto.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    ArrayList<String> direcciones = new ArrayList<String>();
                    int position = list_nombres.indexOf(cliente_auto.getText().toString());
                    if (position != -1) {
                        for (ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones()) {
                            direcciones.add(cliDir.getDireccion());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
                        ((Spinner) rootView.findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
                    }

                }
            });
        }

        if (!lastText.equalsIgnoreCase("")) {
            ArrayList<String> direcciones = new ArrayList<String>();
            int position = list_nombres.indexOf(lastText);
            if (position != -1) {
                for (ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones()) {
                    direcciones.add(cliDir.getDireccion());
                }
                ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
                ((Spinner) rootView.findViewById(R.id.crear_visita_direccion)).setAdapter(adapterDir);
            }
        }

        ((Button) rootView.findViewById(R.id.crear_visita_conf_tarea)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogFragment(TareasFragment.newInstance(list_tareas), "Tareas");
            }
        });

        setCalendar();
        DesdeText.setText(format1.format(fecha.getTime()));
        Duracion.setText(adapterDuracion.getGeneralValue(0).getValue());
        TiempoViaje.setText(adapterDuracion.getGeneralValue(0).getValue());

        List<String> stringList = new ArrayList<String>();
        for(int i = 0; i < adapterDuracion.getCount(); i ++)
            stringList.add(adapterDuracion.getGeneralValue(i).getValue());

        arrayDuracion = new String[stringList.size()];
        stringList.toArray(arrayDuracion);

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
        DesdeText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimePicker(1, fecha_hora.get(Calendar.HOUR_OF_DAY), fecha_hora.get(Calendar.MINUTE), TIME_PICKER_INTERVAL);
            }
        });

        ((LinearLayout) rootView.findViewById(R.id.crear_visita_desde_clickable)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTimePicker(1, fecha_hora.get(Calendar.HOUR_OF_DAY), fecha_hora.get(Calendar.MINUTE), TIME_PICKER_INTERVAL);
            }
        });

        if (getArguments().getString(ARG_FROM).equalsIgnoreCase("Cliente"))
            setDatosCliente(getArguments().getLong(ARG_IDAGENDA));
        else
            setDatos(getArguments().getLong(ARG_IDAGENDA));
    }

    @Override
    public void onDailogTimePickerChange(int id, int hours, int minutes) {
        fecha.set(Calendar.HOUR_OF_DAY, hours);
        fecha.set(Calendar.MINUTE, minutes);
        DesdeText.setText(format1.format(fecha.getTime()));
        super.onDailogTimePickerChange(id, hours, minutes);
    }

    private void setDatosCliente(long id) {
        if (id != 0) {
            Cliente cli = Cliente.getClienteID(getDataBase(), id, false);

            if(cli.getIdExterno() == null || cli.getIdExterno().trim().length() <= 0)
                cliente_auto.setText(cli.getNombreCompleto().trim());
            else
                cliente_auto.setText(cli.getIdExterno() + " - " + cli.getNombreCompleto().trim());

            cliente_auto.dismissDropDown();
            ArrayList<String> direcciones = new ArrayList<String>();
            int position = list_nombres.indexOf(cliente_auto.getText().toString());
            if (position != -1) {
                for (ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones()) {
                    direcciones.add(cliDir.getDireccion());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
                ((Spinner) getRootView().findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
            }
        }

    }

    private void setDatos(long id) {
        if (id != 0) {
            Agenda agd = Agenda.getAgenda(getDataBase(), id);
            if (agd.getIdContacto() != 0 || agd.get_idContacto() != 0) {
                cliente_auto.setText(agd.getContacto().getApellido() + " " + agd.getContacto().getNombre() + " - " + agd.getContacto().getCargo()
                        + " de " + Cliente.getClienteIDServer(getDataBase(), agd.getIdCliente(), false).getNombreCompleto().trim());
            } else {
                cliente_auto.setText(agd.getCliente().getNombreCompleto());
            }

            cliente_auto.dismissDropDown();
            ArrayList<String> direcciones = new ArrayList<String>();
            int position = list_nombres.indexOf(cliente_auto.getText().toString());
            if (position != -1) {
                for (ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones()) {
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
        getRootView().findViewById(R.id.crear_visita_valor_layout).setVisibility(View.GONE);
        this.list_tareas = tareas;
        String tarea_string = "";
        if (tareas.size() > 0) {
            for (Tarea tarea : tareas) {
                if(tarea.getTipoTarea().equalsIgnoreCase("P"))
                    getRootView().findViewById(R.id.crear_visita_valor_layout).setVisibility(View.VISIBLE);
                tarea_string = tarea_string + tarea.getNombreTarea() + ", ";
            }

            tarea_string = tarea_string.substring(0, tarea_string.length() - 2);
        } else
            tarea_string = getResources().getString(R.string.label_conf_tareas);

        ((Button) getRootView().findViewById(R.id.crear_visita_conf_tarea)).setText(tarea_string);
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
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        switch (id)
        {
            case DIALOG_PEDIDO_INCONCLUSO:
                SaveAgenda(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeConfirmation(int id) {
        super.onNegativeConfirmation(id);
        switch (id)
        {
            case DIALOG_PEDIDO_INCONCLUSO:
                SaveAgenda(false);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                boolean hasPedido = false;
                for(Tarea tar : list_tareas)
                {
                    if(tar.getTipoTarea().equalsIgnoreCase("P"))
                        hasPedido = true;
                }
                if(hasPedido) {
                    if (list_nombres.indexOf(cliente_auto.getText().toString()) == -1) {
                        Toast.makeText(getContext(), "Nombre de Cliente incorrecto.", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    if (((EditText) getRootView().findViewById(R.id.crear_visita_valor_venta)).length() <= 0) {
                        Toast.makeText(getContext(), "Debe ingresar un valor de venta.", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    Cliente cli = list_cliente.get(list_nombres.indexOf(cliente_auto.getText().toString()));
                    Pedido pedido = Pedido.getPedidoInconclusoCliente(getDataBase(), (int) cli.getIdCliente());
                    if(pedido == null)
                        SaveAgenda(false);
                    else
                        showDialogConfirmation(DIALOG_PEDIDO_INCONCLUSO, R.string.message_pedido_inconcluso, R.string.title_pedido_inconcluso);
                }
                else
                    SaveAgenda(false);
                break;
            case R.id.action_cancel:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SaveAgenda(boolean hasPedido)
    {
        boolean tieneTareaPedido = false;
        Agenda agenda = new Agenda();
        agenda.setDuracion(duracion);
        agenda.setTiempoViaje(tiempo);

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

        cal.set(Calendar.HOUR_OF_DAY, fecha.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, fecha.get(Calendar.MINUTE));

        if(duracion < 1440) {
            calFin.set(Calendar.HOUR_OF_DAY, fecha.get(Calendar.HOUR_OF_DAY));
            calFin.set(Calendar.MINUTE, fecha.get(Calendar.MINUTE));
            calFin.add(Calendar.MINUTE, (int) agenda.getDuracion());
        }
        else
        {
            calFin.set(Calendar.HOUR_OF_DAY, 23);
            calFin.set(Calendar.MINUTE, 59);
        }

        agenda.setFechaInicio(cal.getTime());
        agenda.setFechaFin(calFin.getTime());

        if (list_nombres.indexOf(cliente_auto.getText().toString()) == -1) {
            Toast.makeText(getContext(), "Nombre de Cliente o Contacto incorrecto.", Toast.LENGTH_LONG).show();
            return;
        }
        Cliente cli = list_cliente.get(list_nombres.indexOf(cliente_auto.getText().toString()));
        if (cli.getTipoPersona().equalsIgnoreCase("C")) {
            Contacto cont = Contacto.getContactoId(getDataBase(), cli.getID());
            agenda.setIdContacto((int) cont.getIdContacto());
            agenda.set_idContacto(cli.getID());
        }

        if (cli.getIdCliente() == 0)
            agenda.setCliente(Cliente.getClienteID(getDataBase(), cli.getID(), true));
        else
            agenda.setCliente(Cliente.getClienteIDServer(getDataBase(), cli.getIdCliente(), true));

        for(Tarea tar : list_tareas)
        {
            if(tar.getTipoTarea().equalsIgnoreCase("P"))
                tieneTareaPedido = true;
        }

        if(tieneTareaPedido && cli.getIdCliente() == PreferenceManager.getInt(Contants.KEY_CLIENTE_DEFAULT))
        {
            Toast.makeText(getContext(), "No se le puede agregar una tarea de pedido a este cliente.", Toast.LENGTH_LONG).show();
            return;
        }
        agenda.setClienteDireccion(agenda.getCliente().getClienteDirecciones().get(getSpinnerSelectedPosition(R.id.crear_visita_direccion)));
        agenda.setCiudad(agenda.getCliente().getClienteDirecciones().get(getSpinnerSelectedPosition(R.id.crear_visita_direccion)).getCiudadDescripcion());
        agenda.setDireccion(agenda.getCliente().getClienteDirecciones().get(getSpinnerSelectedPosition(R.id.crear_visita_direccion)).getDireccion());
        agenda.setEstadoAgenda(Contants.ESTADO_PENDIENTE);
        agenda.setEstadoAgendaDescripcion(Contants.DESC_PENDIENTE);
        agenda.setIdCliente((int) agenda.getCliente().getIdCliente());
        agenda.set_idCliente(agenda.getCliente().getID());
        agenda.setIdClienteDireccion(agenda.getClienteDireccion().getIdClienteDireccion());
        agenda.set_idClienteDireccion(agenda.getClienteDireccion().getID());
        agenda.setIdRuta(PreferenceManager.getInt(Contants.KEY_IDRUTA));
        agenda.setNombreCompleto(agenda.getCliente().getNombreCompleto().trim());
        Calendar fc = Calendar.getInstance();
        fc.set(Calendar.MILLISECOND, 0);
        agenda.setFechaCreacion(fc.getTime());
        //agenda.setID(0);
        agenda.setIdAgenda(0);

        //Agrego valor de venta en el caso que tenga tarea de pedido
        for (Tarea tarea : list_tareas) {
            if(tarea.getTipoTarea().equalsIgnoreCase("P"))
                agenda.setValorVenta(Double.parseDouble(((EditText) getRootView().findViewById(R.id.crear_visita_valor_venta)).getText().toString()));
        }

        if(ConnectionUtils.isNetAvailable(this.getContext()))
            agenda.setEnviado(true);
        else
            agenda.setEnviado(false);

        Agenda.insert(getDataBase(), agenda);
        int last = getDataBase().getIntLastInsertRowId();
        long last2 = getDataBase().getLongLastInsertRowId();


        for (Tarea tarea : list_tareas) {
            AgendaTarea agendaTarea = new AgendaTarea();
            agendaTarea.setIdAgenda(0);
            agendaTarea.set_idAgenda(agenda.getID());
            agendaTarea.setEstadoTarea("P");
            agendaTarea.setIdRuta(PreferenceManager.getInt(Contants.KEY_IDRUTA));
            agendaTarea.setIdTarea(tarea.getIdTarea());
            AgendaTarea.insert(getDataBase(), agendaTarea);
        }

        //Agrego pedido a agenda en el caso que lo tenga
        if(hasPedido)
        {
            Pedido pedido = Pedido.getPedidoInconclusoCliente(getDataBase(), (int) cli.getIdCliente());
            pedido = Pedido.getPedido(getDataBase(), pedido.getID(), false);
            pedido.set_idAgenda((int)agenda.getID());
            Pedido.update(getDataBase(), pedido);
        }

        //agenda.setAgendaTareaList(agendaTareas);
        //String json = AgendaToJSON(agenda);

        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_INSERTAR_AGENDA);
        bundle.putLong(ARG_AGENDA, agenda.getID());
        requestSync(bundle);

        finish();
    }

    public String AgendaToJSON(Agenda agenda) {
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(agenda.getFechaInicio()));
            jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(agenda.getFechaFin()));
            jObject.put("IdCliente", agenda.getIdCliente());
            jObject.put("IdClienteDireccion", agenda.getIdClienteDireccion());
            jObject.put("Ciudad", agenda.getCiudad());
            jObject.put("NombresCompletos", agenda.getNombreCompleto());
            jObject.put("Direccion", agenda.getDireccion());
            //jObject.put("IdClienteContacto", agenda.getIdContacto());

            JSONArray jArrayTareas = new JSONArray();
            for (AgendaTarea agt : agenda.getAgendaTareas()) {
                JSONObject jObjectTarea = new JSONObject();
                jObjectTarea.put("IdTarea", agt.getIdTarea());
                jArrayTareas.put(jObjectTarea);
            }

            jObject.put("AgendaTareas", jArrayTareas);
        } catch (Exception ex) {

        }
        return jObject.toString();
    }

    private void setCalendar() {
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
                int horas = fecha.get(Calendar.HOUR_OF_DAY);
                int minutos = fecha.get(Calendar.MINUTE);
                fecha.setTime(date);
                fecha.set(Calendar.HOUR_OF_DAY, horas);
                fecha.set(Calendar.MINUTE, minutos);
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
        caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, Calendar.getInstance().getTime());
        caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, fecha.getTime());
    }

    public void showDuracion(final int type) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                getContext());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.select_dialog_singlechoice, arrayDuracion);
        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == ID_DURACION) {
                            duracion = Integer.parseInt(adapterDuracion.getGeneralValue(which).getCode());
                            Duracion.setText(adapterDuracion.getGeneralValue(which).getValue());
                            dialog.dismiss();
                        } else if (type == ID_TIEMPO) {
                            tiempo = Integer.parseInt(adapterDuracion.getGeneralValue(which).getCode());
                            TiempoViaje.setText(adapterDuracion.getGeneralValue(which).getValue());
                            dialog.dismiss();
                        }

                    }
                });
        builderSingle.show();
    }
}