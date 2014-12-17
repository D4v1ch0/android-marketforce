package rp3.marketforce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.app.NavActivity;
import rp3.app.nav.NavItem;
import rp3.data.MessageCollection;
import rp3.marketforce.cliente.ClientFragment;
import rp3.marketforce.dashboard.DashboardFragment;
import rp3.marketforce.ruta.RutasFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.runtime.Session;
import rp3.widget.SlidingPaneLayout;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;

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
		
		this.setNavHeaderTitle(Session.getUser().getLogonName());
		this.setNavHeaderSubtitle("RP3 Retail");
		showNavHeader(true);
		setNavHeaderIcon(getResources().getDrawable(R.drawable.ic_action_person_light));
		if(savedInstanceState == null){
			int startNav = NAV_DASHBOARD;			
			setNavigationSelection(startNav);  									
		}
	}
	
	
	@Override
	public void navConfig(List<NavItem> navItems, NavActivity currentActivity) {		
		super.navConfig(navItems, currentActivity);
		
		NavItem dashboard = new NavItem(NAV_DASHBOARD, R.string.title_option_setinicio ,R.drawable.ic_action_select_all);
		NavItem rutas = new NavItem(NAV_RUTAS, R.string.title_option_setrutas ,R.drawable.ic_rutas);
		NavItem clientes = new NavItem(NAV_CLIENTES, R.string.title_option_setclientes, R.drawable.ic_clientes);
		NavItem pedido = new NavItem(NAV_PEDIDO, R.string.title_option_setpedido, R.drawable.ic_pedido);
		NavItem reuniones = new NavItem(NAV_REUNIONES, R.string.title_option_setreuniones, R.drawable.ic_reuniones);
		NavItem recordatorios = new NavItem(NAV_RECORDATORIOS, R.string.title_option_setrecordatorios, R.drawable.ic_recordatorios);
		
		NavItem settingsGroup  = new NavItem(0, R.string.title_option_setconfiguracion, 0,NavItem.TYPE_CATEGORY);
		
		NavItem sincronizar = new NavItem(NAV_SINCRONIZAR, R.string.title_option_setsincronizar , R.drawable.ic_sincronizar, NavItem.TYPE_ACTION);
		NavItem ajustes = new NavItem(NAV_AJUSTES, R.string.title_option_setajustes, R.drawable.ic_ajustes);
		NavItem cerrarsesion = new NavItem(NAV_CERRAR_SESION, R.string.title_option_setcerrar_sesion, R.drawable.ic_cerrar_sesion);
		
		settingsGroup.addChildItem(sincronizar);
		//settingsGroup.addChildItem(ajustes);
		settingsGroup.addChildItem(cerrarsesion);
		
		navItems.add(dashboard);
		navItems.add(rutas);
		navItems.add(clientes);
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
			break;
		case NAV_RUTAS:
			setNavFragment(RutasFragment.newInstance(0),
				    item.getTitle());
			break;
		case NAV_CLIENTES:
			setNavFragment(ClientFragment.newInstance(item.getId()),
		    item.getTitle());
			break;
		case NAV_PEDIDO:	
			setNavFragment(DefaultFragment.newInstance(0),
				    item.getTitle());
			break;
		case NAV_REUNIONES:	
			setNavFragment(DefaultFragment.newInstance(0),
				    item.getTitle());
			break;
		case NAV_RECORDATORIOS:	
			setNavFragment(DefaultFragment.newInstance(0),
				    item.getTitle());
			break;
		case NAV_SINCRONIZAR:	
			
			showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
			
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
			requestSync(bundle);
			
			break;
		case NAV_AJUSTES:	
			setNavFragment(DefaultFragment.newInstance(0),
				    item.getTitle());
			break;
		case NAV_CERRAR_SESION:	
			Session.logOut();
			startActivity( new Intent(this, StartActivity.class));
			finish();
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
		
		if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GENERAL)){
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
      
      @SuppressLint("NewApi")
	public void extractDatabase()
      {
    	  File file = this.getDatabasePath("Rp3MarketForce.db");
  		file.setExecutable(true);
  		file.setReadable(true);
  		file.setWritable(true);
  		
  		File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"prueba.db");
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
