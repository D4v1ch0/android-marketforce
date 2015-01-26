package rp3.marketforce.ruta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import rp3.app.BaseActivity;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.R.layout;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.Convert;
import rp3.util.Screen;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class ReprogramarActivity extends BaseActivity {
	
	private static final int TIME_PICKER_INTERVAL = 5;
	public static String ARG_AGENDA = "idagenda";
	private long idAgenda;
	private DatePicker calendar;
	private TimePicker desdePicker, hastaPicker;
	private Agenda agenda;
	private NumberPicker minutePicker;
	private ArrayList<String> displayedValues;

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
	    
	    agenda = Agenda.getAgenda(getDataBase(), idAgenda);
	    
	    calendar = (DatePicker) findViewById(R.id.reprogramar_ruta_calendario);
	    desdePicker = (TimePicker) findViewById(R.id.reprogramar_visita_desde);
	    hastaPicker = (TimePicker) findViewById(R.id.reprogramar_visita_hasta);
	    
	    setTextViewText(R.id.reprogramar_visita_cliente, agenda.getCliente().getNombreCompleto());
	    
	    Calendar cal = Calendar.getInstance();
	    
	    calendar.init( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
	    
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.setMinDate(cal.getTimeInMillis());
	    try
	    {
	    	cal.setTime(agenda.getFechaInicio());
	    	calendar.updateDate( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
	    }
	    catch(Exception ex)
	    {
	    	
	    }
	    
	    setTimePickerInterval(desdePicker);
	    setTimePickerInterval(hastaPicker);
	    
	    desdePicker.setCurrentMinute(agenda.getFechaInicio().getMinutes() / 5);
	    hastaPicker.setCurrentMinute(agenda.getFechaFin().getMinutes() / 5);
	    
	    desdePicker.setCurrentHour(agenda.getFechaInicio().getHours());	    
	    hastaPicker.setCurrentHour(agenda.getFechaFin().getHours());
	    
	}
	
	public void aceptarCambios(View v)
	{
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

}
