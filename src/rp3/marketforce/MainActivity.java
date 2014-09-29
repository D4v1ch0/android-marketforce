package rp3.marketforce;

import java.util.List;

import rp3.app.NavActivity;
import rp3.app.nav.NavItem;
import rp3.data.MessageCollection;
import rp3.marketforce.cliente.ClientFragment;
import rp3.marketforce.ruta.RutasFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.runtime.Session;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends rp3.app.NavActivity{
	
	public static final int NAV_RUTAS  			= 1;
	public static final int NAV_CLIENTES 		= 2;
	public static final int NAV_PEDIDO 			= 3;
	public static final int NAV_REUNIONES 		= 4;
	public static final int NAV_RECORDATORIOS 	= 5;
	public static final int NAV_SINCRONIZAR 	= 6;
	public static final int NAV_AJUSTES 		= 7;
	public static final int NAV_CERRAR_SESION 	= 8;
	
	public static Intent newIntent(Context c){
		Intent i = new Intent(c, MainActivity.class);
		return i;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		
//		Session.Start(this);
		
		if(savedInstanceState == null){
			int startNav = NAV_CLIENTES;			
			setNavigationSelection(startNav);  									
		}
	}
	
	
	@Override
	public void navConfig(List<NavItem> navItems, NavActivity currentActivity) {		
		super.navConfig(navItems, currentActivity);
		
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
		settingsGroup.addChildItem(ajustes);
		settingsGroup.addChildItem(cerrarsesion);
		
		navItems.add(rutas);
		navItems.add(clientes);
		navItems.add(pedido);
		navItems.add(reuniones);
		navItems.add(recordatorios);
		navItems.add(settingsGroup);
		
	}
	
	@Override
	public void onNavItemSelected(NavItem item) {
		super.onNavItemSelected(item);

		switch (item.getId()) {
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

}
