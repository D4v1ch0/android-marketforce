package rp3.marketforce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.app.NavActivity;
import rp3.app.nav.NavItem;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.marketforce.cliente.ClientFragment;
import rp3.marketforce.content.EnviarUbicacionReceiver;
import rp3.marketforce.dashboard.DashboardFragment;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.Tarea;
import rp3.marketforce.models.Ubicacion;
import rp3.marketforce.recorrido.RecorridoFragment;
import rp3.marketforce.resumen.DashboardGrupoFragment;
import rp3.marketforce.ruta.RutasFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.runtime.Session;
import rp3.sync.SyncAudit;
import rp3.util.Screen;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends rp3.app.NavActivity{
	
	public static final int NAV_DASHBOARD		= 1;
	public static final int NAV_RUTAS  			= 2;
	public static final int NAV_CLIENTES 		= 3;
	public static final int NAV_PEDIDO 			= 4;
	public static final int NAV_REUNIONES 		= 5;
	public static final int NAV_RECORDATORIOS 	= 6;
	public static final int NAV_SINCRONIZAR 	= 7;
	public static final int NAV_AJUSTES 		= 8;
	public static final int NAV_CERRAR_SESION 	= 9;
	public static final int NAV_RESUMEN		 	= 10;
	public static final int NAV_RECORRIDO	 	= 11;
	public String lastTitle = "";
	
	public static Intent newIntent(Context c){
		Intent i = new Intent(c, MainActivity.class);
		return i;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		
//		Session.Start(this);
		
		//extractDatabase();
		
		this.setNavHeaderTitle(Session.getUser().getFullName());
		this.setNavHeaderSubtitle(PreferenceManager.getString(Contants.KEY_CARGO));
		showNavHeader(true);
		setNavHeaderIcon(getResources().getDrawable(R.drawable.ic_user_new));
		if(savedInstanceState == null){
			int startNav = NAV_DASHBOARD;			
			setNavigationSelection(startNav);  									
		}
		setAlarm();	
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Screen.isMinLargeLayoutSize(getApplicationContext())){
			SlidingPaneLayout sp = (SlidingPaneLayout) findViewById(rp3.core.R.id.drawer_layout);
			sp.setPanelSlideListener(new PanelSlideListener(){

				@Override
				public void onPanelSlide(View panel, float slideOffset) {
				}

				@Override
				public void onPanelOpened(View panel) {
					lastTitle = getTitle().toString();
					setTitle("RP3 Market Force");
				}

				@Override
				public void onPanelClosed(View panel) {
					setTitle(lastTitle);	
				}});
		}
		else
		{
			DrawerLayout drawerLayout = (DrawerLayout) findViewById(rp3.core.R.id.drawer_layout);
			ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
					rp3.core.R.drawable.ic_drawer, // nav menu toggle icon
					R.string.app_name, // nav drawer open - description for
										// accessibility
					R.string.app_name // nav drawer close - description for
										// accessibility
			) {
				public void onDrawerClosed(View view) {
					getActionBar().setTitle(lastTitle);
					// calling onPrepareOptionsMenu() to show action bar icons
					invalidateOptionsMenu();
				}
	
				public void onDrawerOpened(View drawerView) {
					lastTitle = getActionBar().getTitle().toString();
					getActionBar().setTitle("RP3 Market Force");
					// calling onPrepareOptionsMenu() to hide action bar icons
					invalidateOptionsMenu();
				}
			};
			drawerLayout.setDrawerListener(actionBarDrawerToggle);
		}
	}
	
	@Override
	public void navConfig(List<NavItem> navItems, NavActivity currentActivity) {		
		super.navConfig(navItems, currentActivity);
		
		NavItem dashboard = new NavItem(NAV_DASHBOARD, R.string.title_option_setinicio ,R.drawable.ic_action_select_all);
		NavItem rutas = new NavItem(NAV_RUTAS, R.string.title_option_setrutas ,R.drawable.ic_rutas);
		NavItem clientes = new NavItem(NAV_CLIENTES, R.string.title_option_setclientes, R.drawable.ic_clientes);
		NavItem grupo = new NavItem(NAV_RESUMEN, R.string.title_option_resumen, R.drawable.ic_action_sort_by_size);
		NavItem pedido = new NavItem(NAV_PEDIDO, R.string.title_option_setpedido, R.drawable.ic_pedido);
		NavItem reuniones = new NavItem(NAV_REUNIONES, R.string.title_option_setreuniones, R.drawable.ic_reuniones);
		NavItem recordatorios = new NavItem(NAV_RECORDATORIOS, R.string.title_option_setrecordatorios, R.drawable.ic_recordatorios);
		NavItem recorrido = new NavItem(NAV_RECORRIDO, R.string.title_option_recorrido, R.drawable.ic_action_place_dark);
		
		NavItem settingsGroup  = new NavItem(0, R.string.title_option_setconfiguracion, 0,NavItem.TYPE_CATEGORY);
		
		NavItem sincronizar = new NavItem(NAV_SINCRONIZAR, R.string.title_option_setsincronizar , R.drawable.ic_sincronizar, NavItem.TYPE_ACTION);
		NavItem ajustes = new NavItem(NAV_AJUSTES, R.string.title_option_setajustes, R.drawable.ic_ajustes);
		NavItem cerrarsesion = new NavItem(NAV_CERRAR_SESION, R.string.title_option_setcerrar_sesion, R.drawable.ic_cerrar_sesion);
		
		settingsGroup.addChildItem(sincronizar);
		//settingsGroup.addChildItem(ajustes);
		settingsGroup.addChildItem(cerrarsesion);
		
		navItems.add(dashboard);
		if(PreferenceManager.getInt(Contants.KEY_IDRUTA) != 0)
			navItems.add(rutas);
		navItems.add(clientes);
		navItems.add(recorrido);
		if(PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR))
			navItems.add(grupo);
		//navItems.add(pedido);
		//navItems.add(reuniones);
		//navItems.add(recordatorios);
		navItems.add(settingsGroup);
	}
	
	@Override
	public void onNavItemSelected(NavItem item) {
		super.onNavItemSelected(item);
		switch (item.getId()) {
		case NAV_DASHBOARD:
			setNavFragment(DashboardFragment.newInstance(0), item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_RUTAS:
			setNavFragment(RutasFragment.newInstance(0),
				    item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_CLIENTES:
			setNavFragment(ClientFragment.newInstance(item.getId()),
		    item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_RESUMEN:
			setNavFragment(DashboardGrupoFragment.newInstance(item.getId()),
		    item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_RECORRIDO:	
			setNavFragment(RecorridoFragment.newInstance(),
				    item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_PEDIDO:	
			setNavFragment(DefaultFragment.newInstance(0),
				    item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_REUNIONES:	
			setNavFragment(DefaultFragment.newInstance(0),
				    item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_RECORDATORIOS:	
			setNavFragment(DefaultFragment.newInstance(0),
				    item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_SINCRONIZAR:	
			
			showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
			
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_TODO);
			requestSync(bundle);
			
			break;
		case NAV_AJUSTES:	
			setNavFragment(DefaultFragment.newInstance(0),
				    item.getTitle());
			lastTitle = item.getTitle();
			break;
		case NAV_CERRAR_SESION:	
			Agenda.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
			Tarea.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
			Cliente.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
			ClienteDireccion.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
			Contacto.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
			Actividad.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
			AgendaTarea.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
			AgendaTareaActividades.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
			SyncAudit.clearAudit();
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		    Intent updateServiceIntent = new Intent(context, EnviarUbicacionReceiver.class);
		    PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0);
		    alarmManager.cancel(pendingUpdateIntent);
		    startActivity(new Intent(this, StartActivity.class));
		    finish();
		    Session.logOut();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(this.findViewById(R.id.sliding_pane_clientes) != null)
		{
			SlidingPaneLayout slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane_clientes);
			switch(item.getItemId())
			{
			case android.R.id.home:
				if(!slidingPane.isOpen())
				{
					slidingPane.openPane();
					return true;
				}
			}
		}
		return super.onMenuItemSelected(featureId, item);
		
	}

	@Override
	public void onBackPressed() {
		if(this.findViewById(R.id.sliding_pane_clientes) != null)
		{
			SlidingPaneLayout slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane_clientes);
			if(!slidingPane.isOpen())
			{
				slidingPane.openPane();
			}
			else
			{
				finish();
			}
		}
		else
		{
			finish();
		}
	}
	
      public void onSyncComplete(Bundle data, final MessageCollection messages) {		

		if(data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_TODO)){
			closeDialogProgress();
			if(messages.hasErrorMessage()){
				showDialogMessage(messages);
			}else{
				reset();
			}
		}
		
	}
      
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	List<android.support.v4.app.Fragment> frags = getSupportFragmentManager().getFragments();
    	for(android.support.v4.app.Fragment fr: frags){
            fr.onActivityResult(requestCode, resultCode, data);
        }
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void setAlarm()
    {
    	final Context ctx = this;
    	new Thread(new Runnable() {
            public void run() {
                try {
                	List<Agenda> agendas = Agenda.getAgendaMinutes(getDataBase(), 900000, 840000);
                	for(Agenda agd : agendas)
                		pushNotification(ctx, agd, "Faltan 15 Minutos para reunion");
                	
                	agendas = Agenda.getAgendaMinutes(getDataBase(), 1800000, 1740000);
                	for(Agenda agd : agendas)
                		pushNotification(ctx, agd, "Faltan 30 Minutos para reunion");
                	
                	agendas = Agenda.getAgendaMinutes(getDataBase(), 60000, 0);
                	for(Agenda agd : agendas)
                		pushNotification(ctx, agd, "Hora de Reunion");
                	
					Thread.sleep(60000);
					setAlarm();
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }).start();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	getActionBar().setTitle(lastTitle);
    }
    
    private void pushNotification(Context ctx, Agenda agd, String message)
    {
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(ctx)
    	        .setSmallIcon(R.drawable.ic_launcher)
    	        .setContentTitle(agd.getCliente().getNombreCompleto())
    	        .setContentText(message);
    	// Creates an explicit intent for an Activity in your app
    	Intent resultIntent = new Intent(ctx, MainActivity.class);

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
	public void extractDatabase()
      {
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
      }

}
