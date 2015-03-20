package rp3.marketforce.ruta;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import rp3.app.BaseActivity;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.R.layout;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.NothingSelectedSpinnerAdapter;
import rp3.util.Convert;
import rp3.util.Screen;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;

public class ReprogramarActivity extends BaseActivity {
	
	private static final int TIME_PICKER_INTERVAL = 5;
    public static final int DURACION_15_MINUTOS = 0;
    public static final int DURACION_30_MINUTOS = 1;
    public static final int DURACION_45_MINUTOS = 2;
    public static final int DURACION_60_MINUTOS = 3;
    public static final int DURACION_90_MINUTOS = 4;
    public static final int DURACION_120_MINUTOS = 5;
    public static final int DURACION_180_MINUTOS = 6;

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
    private String[] arrayDuracion;
    private int duracion = 0, tiempo = 0;
    private CaldroidFragment caldroidFragment;
    SimpleDateFormat format1 = new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm");

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
	    setContentView(R.layout.dialog_reprogramar_agenda);
	    if(Screen.getOrientation() == Screen.ORIENTATION_LANDSCAPE && Screen.isMinLargeLayoutSize(this))
	    {
	    	DisplayMetrics metrics = new DisplayMetrics();
	    	getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    	WindowManager.LayoutParams params = getWindow().getAttributes();    
	    	params.height = metrics.heightPixels - 100;  
	    	params.width = metrics.widthPixels - 100;   

	    	this.getWindow().setAttributes(params); 
	    }
	    
	    idAgenda = getIntent().getExtras().getLong(ARG_AGENDA);
        arrayDuracion = this.getResources()
                .getStringArray(R.array.arrayDuracion);
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
                if (findViewById(R.id.reprogramar_visita_desde).getVisibility() == View.VISIBLE)
                    findViewById(R.id.reprogramar_visita_desde).setVisibility(View.GONE);
                else
                    findViewById(R.id.reprogramar_visita_desde).setVisibility(View.VISIBLE);
            }
        });
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, fecha.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, fecha.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        switch((int)agenda.getDuracion())
        {
            case 15: Duracion.setText(arrayDuracion[DURACION_15_MINUTOS]); break;
            case 30: Duracion.setText(arrayDuracion[DURACION_30_MINUTOS]); break;
            case 45: Duracion.setText(arrayDuracion[DURACION_45_MINUTOS]); break;
            case 60: Duracion.setText(arrayDuracion[DURACION_60_MINUTOS]); break;
            case 90: Duracion.setText(arrayDuracion[DURACION_90_MINUTOS]); break;
            case 120: Duracion.setText(arrayDuracion[DURACION_120_MINUTOS]); break;
            case 180: Duracion.setText(arrayDuracion[DURACION_180_MINUTOS]); break;
            default:  Duracion.setText(arrayDuracion[DURACION_15_MINUTOS]); break;
        }

        switch((int)agenda.getTiempoViaje())
        {
            case 15: TiempoViaje.setText(arrayDuracion[DURACION_15_MINUTOS]); break;
            case 30: TiempoViaje.setText(arrayDuracion[DURACION_30_MINUTOS]); break;
            case 45: TiempoViaje.setText(arrayDuracion[DURACION_45_MINUTOS]); break;
            case 60: TiempoViaje.setText(arrayDuracion[DURACION_60_MINUTOS]); break;
            case 90: TiempoViaje.setText(arrayDuracion[DURACION_90_MINUTOS]); break;
            case 120: TiempoViaje.setText(arrayDuracion[DURACION_120_MINUTOS]); break;
            case 180: TiempoViaje.setText(arrayDuracion[DURACION_180_MINUTOS]); break;
            default: TiempoViaje.setText(arrayDuracion[DURACION_15_MINUTOS]); break;
        }
        DesdeText.setText(format1.format(fecha.getTime()));
	}

	public void aceptarCambios(View v)
	{
		Calendar cal = Calendar.getInstance();
		Calendar calFin = Calendar.getInstance();

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

        cal.set(Calendar.HOUR_OF_DAY, desdePicker.getCurrentHour());
        cal.set(Calendar.MINUTE, desdePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);

        calFin.set(Calendar.HOUR_OF_DAY, desdePicker.getCurrentHour());
        calFin.set(Calendar.MINUTE, desdePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
        calFin.add(Calendar.MINUTE, (int) agenda.getDuracion());

        agenda.setFechaInicio(cal.getTime());
        agenda.setFechaFin(calFin.getTime());

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
	
	public void cancelarCambios(View v)
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
        caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, Calendar.getInstance().getTime());
        caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, fecha.getTime());
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
