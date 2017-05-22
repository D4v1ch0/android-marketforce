package rp3.berlin.ruta;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.app.BaseActivity;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.Agenda;
import rp3.berlin.sync.SyncAdapter;
import rp3.berlin.utils.NothingSelectedSpinnerAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class ReprogramarActivity extends BaseActivity {
	
	private static final int TIME_PICKER_INTERVAL = 5;

    public static final int ID_DURACION = 0;
    public static final int ID_TIEMPO = 1;

	public static String ARG_AGENDA = "idagenda";
	private long idAgenda;
    private Calendar fecha;
    private TextView Duracion, TiempoViaje, DesdeText;
	private TimePicker desdePicker, hastaPicker;
	private Agenda agenda;
	private NumberPicker minutePicker;
	private ArrayList<String> displayedValues;
    private SimpleGeneralValueAdapter adapterDuracion;
    private String[] arrayDuracion;
    private int duracion = 0, tiempo = 0;
    private CaldroidFragment caldroidFragment;
    SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("Reprogramar Agenda");
	    
	    setContentView(R.layout.dialog_reprogramar_agenda, R.menu.fragment_crear_cliente);
	    
	    idAgenda = getIntent().getExtras().getLong(ARG_AGENDA);
	    fecha = Calendar.getInstance();
	    agenda = Agenda.getAgenda(getDataBase(), idAgenda);

	    desdePicker = (TimePicker) findViewById(R.id.reprogramar_desde);
        Duracion = ((TextView) findViewById(R.id.reprogramar_duracion));
        TiempoViaje = ((TextView) findViewById(R.id.reprogramacion_tiempo));
        DesdeText = ((TextView) findViewById(R.id.reprogramar_desde_text));
	    
	    setTextViewText(R.id.reprogramar_visita_cliente, agenda.getCliente().getNombreCompleto());
	    
	    setTimePickerInterval(desdePicker);
	    
	    desdePicker.setCurrentMinute(agenda.getFechaInicio().getMinutes() / 5);
	    desdePicker.setCurrentHour(agenda.getFechaInicio().getHours());
        fecha.setTime(agenda.getFechaInicio());
        adapterDuracion = new SimpleGeneralValueAdapter(this, getDataBase(), Contants.GENERAL_TABLE_DURACION_VISITA);
        SimpleGeneralValueAdapter motivosAdapter = new SimpleGeneralValueAdapter(this, getDataBase(), Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION);
        ((Spinner) findViewById(R.id.reprogramar_motivo)).setAdapter(new NothingSelectedSpinnerAdapter(
                motivosAdapter,
                R.layout.spinner_empty_selected,
                this));

        setCalendar();

        Duracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDuracion(ID_DURACION);
            }
        });
        TiempoViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDuracion(ID_TIEMPO);
            }
        });

        ((LinearLayout) findViewById(R.id.reprogramar_desde_clickable)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTimePicker(1, fecha.get(Calendar.HOUR_OF_DAY), fecha.get(Calendar.MINUTE), TIME_PICKER_INTERVAL);
            }
        });
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, fecha.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, fecha.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        if(agenda.getDuracion() == 0)
            agenda.setDuracion(15);
        if(agenda.getTiempoViaje() == 0)
            agenda.setTiempoViaje(15);
        Duracion.setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_DURACION_VISITA, agenda.getDuracion() + "").getValue());
        TiempoViaje.setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_DURACION_VISITA, agenda.getTiempoViaje() + "").getValue());
        DesdeText.setText(format1.format(fecha.getTime()));

        duracion = (int) agenda.getDuracion();
        tiempo = (int) agenda.getTiempoViaje();

        List<String> stringList = new ArrayList<String>();
        for(int i = 0; i < adapterDuracion.getCount(); i ++)
            stringList.add(adapterDuracion.getGeneralValue(i).getValue());

        arrayDuracion = new String[stringList.size()];
        stringList.toArray(arrayDuracion);
	}

    @Override
    public void onDailogTimePickerChange(int id, int hours, int minutes) {
        fecha.set(Calendar.HOUR_OF_DAY, hours);
        fecha.set(Calendar.MINUTE, minutes);
        DesdeText.setText(format1.format(fecha.getTime()));
        super.onDailogTimePickerChange(id, hours, minutes);
    }

	public void aceptarCambios()
	{
        agenda = Agenda.getAgenda(getDataBase(), idAgenda);
		Calendar cal = Calendar.getInstance();
		Calendar calFin = Calendar.getInstance();

        agenda.setDuracion(duracion);
        agenda.setTiempoViaje(tiempo);

        Calendar cal_hoy = Calendar.getInstance();
        //if((fecha.get(Calendar.YEAR) < cal_hoy.get(Calendar.YEAR)) || (fecha.get(Calendar.MONTH) < cal_hoy.get(Calendar.MONTH))
        //        || (fecha.get(Calendar.DATE) < cal_hoy.get(Calendar.DATE)) )
        //{
        //    Toast.makeText(this, "Fecha no puede ser anterior a hoy.", Toast.LENGTH_LONG).show();
        //   return;
        //}

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

        if(((Spinner) findViewById(R.id.reprogramar_motivo)).getSelectedItemPosition() == 0)
        {
            Toast.makeText(this, "Debe de escoger un motivo de reprogramación.", Toast.LENGTH_LONG).show();
            return;
        }
        agenda.setIdMotivoReprogramacion(((GeneralValue) ((Spinner) findViewById(R.id.reprogramar_motivo)).getSelectedItem()).getCode());
        agenda.setFechaInicio(cal.getTime());
		agenda.setFechaFin(calFin.getTime());
		agenda.setEstadoAgenda(Contants.ESTADO_REPROGRAMADO);
		agenda.setEstadoAgendaDescripcion(Contants.DESC_REPROGRAMADO);
		agenda.setEnviado(false);
		
		Bundle bundle = new Bundle();
		bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_REPROGRAMAR_AGENDA);
		bundle.putInt(RutasDetailFragment.ARG_AGENDA_ID,(int) agenda.getID());
		requestSync(bundle);
		
		Agenda.update(getDataBase(), agenda);
		
		finish();
	}
	
	public void cancelarCambios()
	{
		finish();
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

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.reprogramar_visita_calendar, caldroidFragment);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:
                aceptarCambios();
                break;
            case R.id.action_cancel:
                cancelarCambios();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDuracion(final int type)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice, arrayDuracion);
        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type == ID_DURACION)
                        {
                            duracion = Integer.parseInt(adapterDuracion.getGeneralValue(which).getCode());
                            Duracion.setText(adapterDuracion.getGeneralValue(which).getValue());
                            dialog.dismiss();
                        }
                        else if(type == ID_TIEMPO)
                        {
                            tiempo = Integer.parseInt(adapterDuracion.getGeneralValue(which).getCode());
                            TiempoViaje.setText(adapterDuracion.getGeneralValue(which).getValue());
                            dialog.dismiss();
                        }

                    }
                });
        builderSingle.show();
    }

}
