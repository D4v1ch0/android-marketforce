package rp3.marketforce.ruta;

import java.util.Calendar;

import rp3.app.BaseActivity;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.R.layout;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.Convert;
import rp3.util.Screen;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class ReprogramarActivity extends BaseActivity {
	
	public static String ARG_AGENDA = "idagenda";
	private long idAgenda;
	private DatePicker calendar;
	private TimePicker desdePicker, hastaPicker;
	private Agenda agenda;

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
	    desdePicker.setCurrentHour(agenda.getFechaInicio().getHours());
	    desdePicker.setCurrentMinute(agenda.getFechaInicio().getMinutes());
	    
	    hastaPicker.setCurrentHour(agenda.getFechaFin().getHours());
	    hastaPicker.setCurrentMinute(agenda.getFechaFin().getMinutes());
	    
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
		cal.set(Calendar.MINUTE, desdePicker.getCurrentMinute());
		
		calFin.set(Calendar.HOUR_OF_DAY, hastaPicker.getCurrentHour());
		calFin.set(Calendar.MINUTE, hastaPicker.getCurrentMinute());
		
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

}
