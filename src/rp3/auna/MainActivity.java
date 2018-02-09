package rp3.auna;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.app.NavActivity;
import rp3.app.nav.NavItem;
import rp3.auna.cliente.ClientDetailFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.Constants;
import rp3.data.MessageCollection;
import rp3.auna.cliente.ClientFragment;
import rp3.auna.content.EnviarUbicacionReceiver;
import rp3.auna.dashboard.DashboardFragment;
import rp3.auna.db.DbOpenHelper;
import rp3.auna.information.InformationFragment;
import rp3.auna.marcaciones.PermisoFragment;
import rp3.auna.models.Agenda;
import rp3.auna.oportunidad.OportunidadFragment;
import rp3.auna.models.pedido.ControlCaja;
import rp3.auna.pedido.ControlCajaFragment;
import rp3.auna.pedido.PedidoFragment;
import rp3.auna.radar.RadarFragment;
import rp3.auna.recorrido.RecorridoFragment;
import rp3.auna.resumen.DashboardGrupoFragment;
import rp3.auna.ruta.RutasFragment;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.utils.DrawableManager;
import rp3.auna.utils.Utils;
import rp3.runtime.Session;
import rp3.sync.SyncAudit;
import rp3.util.ConnectionUtils;
import rp3.util.LocationUtils;
import rp3.widget.SlidingPaneLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends rp3.app.NavActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	public static final int NAV_DASHBOARD = 1;
	public static final int NAV_RUTAS = 2;
	public static final int NAV_CLIENTES = 3;
	public static final int NAV_PEDIDO = 4;
	public static final int NAV_REUNIONES = 5;
	public static final int NAV_RECORDATORIOS = 6;
	public static final int NAV_SINCRONIZAR = 7;
	public static final int NAV_AJUSTES = 8;
	public static final int NAV_CERRAR_SESION = 9;
	public static final int NAV_RESUMEN = 10;
	public static final int NAV_RECORRIDO = 11;
	public static final int NAV_RADAR = 12;
	public static final int NAV_INFORMATION = 13;
	public static final int NAV_OPORTUNIDAD = 14;
	public static final int NAV_JUSTIFICACIONES = 15;

	public static final int CERRAR_SESION_DIALOG = 12;
	public static final int CERRAR_CAJA_DIALOG = 13;

	public static final String TO_AGENDAS = "toAgendas";
	public String lastTitle;
	public int selectedItem = 0;
	TextToSpeech t1;
	SimpleDateFormat format4 = new SimpleDateFormat("dd/MM/yyy HH:mm");
	DrawableManager DManager;
	String toSpeak = "";
	//public ClientDetailFragment clientDetailFragment;

	public static Intent newIntent(Context c) {
		Intent i = new Intent(c, MainActivity.class);
		return i;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"onCreate...");
		setNavMode(NavActivity.NAV_MODE_SLIDING_PANE);
		super.onCreate(savedInstanceState);
		Session.Start(this);
		rp3.configuration.Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);

		/*t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status != TextToSpeech.ERROR) {
					try {
						t1.setLanguage(new Locale("es", "ES"));
						t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
					} catch (Exception ex) {
					}
				}
			}
		});*/
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		int numAgendas = Agenda.getRutaDiaDashboard(getDataBase(), cal).size();
		//int numAgendas = 1;
		/*if (numAgendas != 0) {
			if (numAgendas > 1)
				toSpeak = "Usted tiene " + numAgendas + " visitas pendientes";
			else
				toSpeak = "Usted tiene una visita pendiente";
		}*/

		//extractDatabase();

		DManager = new DrawableManager();

		setNavHeaderIcon(getResources().getDrawable(R.drawable.ic_user_new));
		if (!PreferenceManager.getString(Contants.KEY_FOTO, "").equalsIgnoreCase("")) {
			Log.d(TAG,"!PreferenceManager.getString(Contants.KEY_FOTO, \"\").equalsIgnoreCase(\"\")...");
			DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") +
							Utils.getImageDPISufix(this, PreferenceManager.getString(Contants.KEY_FOTO)).replace("~", "").replace("\\", "/"),
					(ImageView) this.getRootView().findViewById(R.id.nav_header_icon));
		}
		this.setNavHeaderTitle(Session.getUser().getFullName());
		this.setNavHeaderSubtitle(PreferenceManager.getString(Contants.KEY_CARGO));
		showNavHeader(true);

		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(TO_AGENDAS)) {
			Log.d(TAG,"getIntent().getExtras() != null && getIntent().getExtras().containsKey(TO_AGENDAS)...");
			int startNav = NAV_RUTAS;
			setNavigationSelection(startNav);
			selectedItem = startNav;
			lastTitle = getText(R.string.title_option_setrutas).toString();
		} else {
			Log.d(TAG,"getIntent().getExtras() == null && !getIntent().getExtras().containsKey(TO_AGENDAS)...");
			if (savedInstanceState == null) {
				Log.d(TAG,"savedInstanceState == null...");
				int startNav = NAV_RUTAS;
				setNavigationSelection(startNav);
				selectedItem = startNav;
				lastTitle = getText(R.string.title_option_setinicio).toString();
			} else {
				Log.d(TAG,"savedInstanceState != null...");
				selectedItem = savedInstanceState.getInt("Selected");
				lastTitle = savedInstanceState.getString("Title");
			}
		}
		setAlarm();
		Bundle bundle = new Bundle();
		bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GEOPOLITICAL);
		requestSync(bundle);
		if (PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
			Log.d(TAG,"PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)...");
			Bundle bundle2 = new Bundle();
			bundle2.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SOLO_RESUMEN);
			requestSync(bundle2);
		}
		try {
			LocationUtils.getLocation(this);
		} catch (Exception ex) {
			Log.d(TAG,ex.getMessage());
		}

		ControlCaja controlCaja = ControlCaja.getControlCajaActiva(getDataBase());
		if (controlCaja != null && controlCaja.getIdAgente() != PreferenceManager.getInt(Contants.KEY_IDAGENTE)) {
			Log.d(TAG,"ontrolCaja != null && controlCaja.getIdAgente() != PreferenceManager.getInt(Contants.KEY_IDAGENTE)...");
			showDialogConfirmation(CERRAR_CAJA_DIALOG, R.string.message_cerrar_caja_activa, R.string.title_cerrar_caja_activa);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG,"onSaveIinstanceState...");
		outState.putString("Title", lastTitle);
		outState.putInt("Selected", selectedItem);
		super.onSaveInstanceState(outState);
	}



	@Override
	public void navConfig(List<NavItem> navItems, NavActivity currentActivity) {
		super.navConfig(navItems, currentActivity);
		Log.d(TAG,"navConfig...");
		//NavItem dashboard = new NavItem(NAV_DASHBOARD, R.string.title_option_setinicio, R.drawable.ic_action_select_all);
		NavItem rutas = new NavItem(NAV_RUTAS, R.string.title_option_setrutas, R.drawable.ic_rutas);
		NavItem clientes = new NavItem(NAV_CLIENTES, R.string.title_option_setclientes, R.drawable.ic_clientes);
		NavItem grupo = new NavItem(NAV_RESUMEN, R.string.title_option_resumen, R.
				drawable.ic_action_sort_by_size);
		NavItem pedido = new NavItem(NAV_PEDIDO, R.string.title_option_setpedido, R.drawable.ic_pedido);
		NavItem reuniones = new NavItem(NAV_REUNIONES, R.string.title_option_setreuniones, R.drawable.ic_reuniones);
		NavItem recordatorios = new NavItem(NAV_RECORDATORIOS, R.string.title_option_setrecordatorios, R.drawable.ic_recordatorios);
		NavItem recorrido = new NavItem(NAV_RECORRIDO, R.string.title_option_recorrido, R.drawable.ic_action_place_dark);
		NavItem radar = new NavItem(NAV_RADAR, R.string.title_option_radar, R.drawable.ic_action_data_usage);
		NavItem information = new NavItem(NAV_INFORMATION, R.string.title_option_informacion, R.drawable.ic_action_about);
		NavItem oportunidad = new NavItem(NAV_OPORTUNIDAD, R.string.title_option_oportunidad, R.drawable.oportunidades);

		NavItem settingsGroup = new NavItem(0, R.string.title_option_setconfiguracion, 0, NavItem.TYPE_CATEGORY);

		Date ultimo = SyncAudit.getLastSyncDate();
		NavItem sincronizar = new NavItem(NAV_SINCRONIZAR, R.string.title_option_setsincronizar, R.drawable.ic_sincronizar, NavItem.TYPE_ACTION, "Ult. Conexión: " + format4.format(ultimo));
		NavItem justificaciones = new NavItem(NAV_JUSTIFICACIONES, R.string.title_option_justificaciones, R.drawable.solicitar_permiso);
		NavItem ajustes = new NavItem(NAV_AJUSTES, R.string.title_option_setajustes, R.drawable.ic_ajustes);
		NavItem cerrarsesion = new NavItem(NAV_CERRAR_SESION, R.string.title_option_setcerrar_sesion, R.drawable.ic_cerrar_sesion);

		settingsGroup.addChildItem(sincronizar);
		settingsGroup.addChildItem(information);
		settingsGroup.addChildItem(cerrarsesion);

		//navItems.add(rutas);
		/*if (PreferenceManager.getBoolean(Contants.KEY_MODULO_OPORTUNIDADES, true))
			navItems.add(oportunidad);*/
		int ruta = PreferenceManager.getInt(Contants.KEY_IDRUTA);
		if (PreferenceManager.getInt(Contants.KEY_IDRUTA) != 0) {
			Log.d(TAG,"PreferenceManager.getInt(Contants.KEY_IDRUTA) != 0...");
			navItems.add(rutas);
			navItems.add(clientes);
		}
		navItems.add(recorrido);
		if (PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
			//navItems.add(grupo);
			//navItems.add(radar);
			//justificaciones.setBadge(Justificacion.getPermisosPendientesAprobarCount(getDataBase()));
			//if (PreferenceManager.getBoolean(Contants.KEY_MODULO_MARCACIONES, true))
			//	navItems.add(justificaciones);
		}
		/*if (PreferenceManager.getBoolean(Contants.KEY_MODULO_POS, true))
			navItems.add(pedido);*/
		//navItems.add(reuniones);
		//navItems.add(recordatorios);
		navItems.add(settingsGroup);
	}

	@Override
	public void onNavItemSelected(NavItem item) {
		super.onNavItemSelected(item);
		selectedItem = item.getId();
		switch (item.getId()) {
			case NAV_DASHBOARD:
				Log.d(TAG,"NAV_DASHBOARD...");
				setNavFragment(DashboardFragment.newInstance(0), item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_RUTAS:
				Log.d(TAG,"NAV_RUTAS...");
				setNavFragment(RutasFragment.newInstance(0),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_CLIENTES:
				Log.d(TAG,"NAV_CLIENTES...");
				setNavFragment(ClientFragment.newInstance(item.getId()),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_RESUMEN:
				Log.d(TAG,"NAV_RESUMEN...");
				setNavFragment(DashboardGrupoFragment.newInstance(item.getId()),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_RADAR:
				Log.d(TAG,"NAV_RADAR...");
				setNavFragment(RadarFragment.newInstance(),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_RECORRIDO:
				Log.d(TAG,"NAV_RECORRIDO...");
				if (!ConnectionUtils.isNetAvailable(this)) {
					Log.d(TAG,"!ConnectionUtils.isNetAvailable(this)...");
					Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
				} else {
					Log.d(TAG,"ConnectionUtils.isNetAvailable(this)...");
					setNavFragment(RecorridoFragment.newInstance(),
							item.getTitle());
					lastTitle = item.getTitle();
				}
				break;
			case NAV_PEDIDO:
				Log.d(TAG,"NAV_PEDIDO...");
				setNavFragment(PedidoFragment.newInstance(0),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_REUNIONES:
				Log.d(TAG,"NAV_REUNIONES...");
				setNavFragment(DefaultFragment.newInstance(0),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_RECORDATORIOS:
				Log.d(TAG,"NAV_RECORDATORIOS...");
				setNavFragment(DefaultFragment.newInstance(0),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_SINCRONIZAR:
				Log.d(TAG,"NAV_SINCRONIZAR...");
				if (!ConnectionUtils.isNetAvailable(this)) {
					Log.d(TAG,"!ConnectionUtils.isNetAvailable(this)...");
					Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
				} else {
					Log.d(TAG,"ConnectionUtils.isNetAvailable(this)...");
					showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);

					Bundle bundle = new Bundle();
					bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_TODO);
					requestSync(bundle);
				}

				break;
			case NAV_AJUSTES:
				Log.d(TAG,"NAV_AJUSTES...");
				setNavFragment(DefaultFragment.newInstance(0),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_INFORMATION:
				Log.d(TAG,"NAV_INFORMATION...");
				setNavFragment(InformationFragment.newInstance(),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_CERRAR_SESION:
				Log.d(TAG,"...");
				if (ControlCaja.getControlCajaActiva(getDataBase()) == null) {
					Log.d(TAG, "ControlCaja.getControlCajaActiva(getDataBase()) == null...");
					showDialogConfirmation(CERRAR_SESION_DIALOG, R.string.message_cerrar_sesion, R.string.title_option_setcerrar_sesion);
				}else {
					Log.d(TAG,"ControlCaja.getControlCajaActiva(getDataBase()) != null...");
					try {
						Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
						r.play();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Toast.makeText(this, "Para cerrar sesión, es necesario que cierre caja.", Toast.LENGTH_LONG).show();
				}
				break;
			case NAV_OPORTUNIDAD:
				Log.d(TAG,"NAV_OPORTUNIDAD...");
				setNavFragment(OportunidadFragment.newInstance(),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			case NAV_JUSTIFICACIONES:
				Log.d(TAG,"NAV_JUSTIFICACIONES...");
				setNavFragment(PermisoFragment.newInstance(),
						item.getTitle());
				lastTitle = item.getTitle();
				break;
			default:
				break;
		}
		setTitle(lastTitle);
		Log.d(TAG,"onNavItemSelected..."+lastTitle);
	}

	@Override
	public void onPositiveConfirmation(int id) {
		super.onPositiveConfirmation(id);
		switch (id) {
			case CERRAR_SESION_DIALOG:
				Log.d(TAG,"CERRAR_SESION_DIALOG...");
				PreferenceManager.setValue(Constants.KEY_LAST_LOGIN, Session.getUser().getLogonName());
				PreferenceManager.setValue(Constants.KEY_LAST_PASS, Session.getUser().getPassword());
				PreferenceManager.setValue(Constants.KEY_LAST_TOKEN, "temp");
				//SyncAudit.insert(SyncAdapter.SYNC_TYPE_GEOPOLITICAL,SyncAdapter.SYNC_EVENT_SUCCESS);
				AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

				Intent updateServiceIntent = new Intent(context, EnviarUbicacionReceiver.class);
				PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0);
				alarmManager.cancel(pendingUpdateIntent);
				startActivity(new Intent(this, StartActivity.class));
				finish();
				Session.logOut();
				break;
			case CERRAR_CAJA_DIALOG:
				Log.d(TAG,"CERRAR_CAJA_DIALOG...");
				ControlCaja controlCaja = ControlCaja.getControlCajaActiva(getDataBase());
				Bundle bundle2 = new Bundle();
				bundle2.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CERRAR_CAJA);
				bundle2.putLong(ControlCajaFragment.ARG_CONTROL, controlCaja.getID());
				requestSync(bundle2);

				break;
		}
	}

	@Override
	public void onNegativeConfirmation(int id) {
		Log.d(TAG,"onNegativeConfirmation...");
		super.onNegativeConfirmation(id);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Log.d(TAG,"onMenuItemSelected...");
		if (this.findViewById(R.id.sliding_pane_clientes) != null) {
			Log.d(TAG,"this.findViewById(R.id.sliding_pane_clientes) != null...");
			SlidingPaneLayout slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane_clientes);
			switch (item.getItemId()) {
				case android.R.id.home:
					Log.d(TAG,"android.R.id.home:...");
					if (!slidingPane.isOpen()) {
						Log.d(TAG,"!slidingPane.isOpen()...");
						slidingPane.openPane();
						return true;
					}
			}
		}
		return super.onMenuItemSelected(featureId, item);

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,"onBackPressed...");
		if (this.findViewById(R.id.sliding_pane_clientes) != null) {
			Log.d(TAG,"this.findViewById(R.id.sliding_pane_clientes) != null...");
			SlidingPaneLayout slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane_clientes);
			if (selectedItem == NAV_RUTAS) {
				finish();
				//extractDatabase();
			}
			if (!slidingPane.isOpen()) {
				slidingPane.openPane();
			} else {
				int startNav = NAV_RUTAS;
				setNavigationSelection(startNav);
				setTitle(getString(R.string.title_option_setinicio));
				lastTitle = getString(R.string.title_option_setinicio);
			}
		} else {
			Log.d(TAG,"this.findViewById(R.id.sliding_pane_clientes) == null...");
			if (selectedItem == NAV_RUTAS) {
				finish();
				//extractDatabase();
			} else {
				int startNav = NAV_RUTAS;
				setNavigationSelection(startNav);
				setTitle(getString(R.string.title_option_setinicio));
				lastTitle = getString(R.string.title_option_setinicio);
			}
		}

		/*if(clientDetailFragment!=null){

				clientDetailFragment.onDetach();
				clientDetailFragment.onDestroyView();
				clientDetailFragment =null;

		}*/
	}

	public void onSyncComplete(Bundle data, final MessageCollection messages) {
		Log.d(TAG,"onSyncComplete...");
		if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_TODO)) {
			closeDialogProgress();
			if (messages.hasErrorMessage()) {
				showDialogMessage(messages);
			} else {
				reset();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG,"onActivityResult...");
		List<android.support.v4.app.Fragment> frags = getSupportFragmentManager().getFragments();
		for (android.support.v4.app.Fragment fr : frags) {
			fr.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setAlarm() {
		Log.d(TAG,"setAlarm...");
		final Context ctx = this;
		new Thread(new Runnable() {
			public void run() {
				try {
					List<Agenda> agendas = Agenda.getAgendaMinutes(getDataBase(), 900000, 840000);
					for (Agenda agd : agendas)
					{
						pushNotification(ctx, agd, "Faltan 15 Minutos para reunion");
					}

					agendas = Agenda.getAgendaMinutes(getDataBase(), 1800000, 1740000);
					for (Agenda agd : agendas)
					{
						pushNotification(ctx, agd, "Faltan 30 Minutos para reunion");
					}

					agendas = Agenda.getAgendaMinutes(getDataBase(), 60000, 0);
					for (Agenda agd : agendas)
					{
						pushNotification(ctx, agd, "Hora de Reunion");
					}

					Thread.sleep(60000);
					setAlarm();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d(TAG,e.getMessage()+"...");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG,"onConfigurationChanged...");
		invalidateOptionsMenu();
	}

	private void pushNotification(Context ctx, Agenda agd, String message) {
		Log.d(TAG,"pushNotification...");
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(ctx)
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle(agd.getCliente().getNombreCompleto())
						.setContentText(message);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(ctx, StartActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
				);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(agd.getIdAgenda(), mBuilder.build());
	}

	@SuppressLint("NewApi")
	public void extractDatabase() {
		File file = this.getDatabasePath("Rp3MarketForce.db");
		file.setExecutable(true);
		file.setReadable(true);
		file.setWritable(true);

		File file2 = new File(Environment.getExternalStorageDirectory() + "/prueba.db");
		file2.setExecutable(true);
		file2.setReadable(true);
		file2.setWritable(true);

		try
		{
			file2.createNewFile();
			InputStream in = new FileInputStream(file);
			OutputStream out = new FileOutputStream(file2);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
		catch(Exception e)
		{

		}
		/*File file = this.getDatabasePath("Rp3MarketForce.db");
		file.setExecutable(true);
		file.setReadable(true);
		file.setWritable(true);

		File file2 = new File(Environment.getExternalStorageDirectory() + "/testM.db");
		file2.setExecutable(true);
		file2.setReadable(true);
		file2.setWritable(true);

		try {
			file2.createNewFile();
			InputStream in = new FileInputStream(file);
			OutputStream out = new FileOutputStream(file2);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {

		}

		File file3 = new File(Environment.getExternalStorageDirectory() + "/testK.db");
		file3.setExecutable(true);
		file3.setReadable(true);
		file3.setWritable(true);

		try {
			file3.createNewFile();
			OutputStream out = new FileOutputStream(file3);
			String key = Session.getUserMovil().getLogonName() + ";" + Session.getUserMovil().getPassword();
			out.write(key.getBytes(StandardCharsets.UTF_8));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}

	//region Ciclo de vida

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume...");
		/*if(clientDetailFragment!=null){
			if(clientDetailFragment.isResumed()){
				clientDetailFragment.onDetach();
				clientDetailFragment.onDestroyView();
				clientDetailFragment = null;
			}
		}*/
		/*for(Cliente cliente:Cliente.getCliente(getDataBase())){
			Log.d(TAG,cliente.toString());
		}*/

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart...");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPpause...");
	}

	//endregion
}
