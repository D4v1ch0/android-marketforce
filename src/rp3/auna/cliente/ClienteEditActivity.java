package rp3.auna.cliente;

import rp3.auna.R;
import rp3.auna.models.Cliente;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class ClienteEditActivity extends rp3.app.BaseActivity implements ClienteEditFragment.OnClienteEditListener {

	private static final String TAG = ClienteEditActivity.class.getSimpleName();
	public static final String EXTRA_CLIENTE_ID = "clienteId";
	private long clientId = 0;
	
	ClienteEditFragment clienteEditFragment;
	
	public static Intent newIntent(Context c, long transactionDetailId){
		Intent intent = new Intent(c,ClienteEditActivity.class);
		intent.putExtra(ClienteEditActivity.EXTRA_CLIENTE_ID, transactionDetailId);
		return intent;
	}
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
		setContentView(R.layout.activity_transaction_edit_item);
		setHomeAsUpEnabled(true, true);		
		
		Intent intent = getIntent();
		if(intent!=null && intent.hasExtra(EXTRA_CLIENTE_ID))
			clientId = intent.getLongExtra(EXTRA_CLIENTE_ID, 0);
				
		if(!this.hasFragment(R.id.content_transaction_edit_item)){
			clienteEditFragment = ClienteEditFragment.newInstance(clientId);			
			setFragment(R.id.content_transaction_edit_item, clienteEditFragment);
		}
	}


	@Override
	public void onClienteUpdate(Cliente cliente) {
		Log.d(TAG,"onClienteUpdate...");
		finish();
	}

	/**
	 *
	 * Ciclo de vida
	 *
	 */

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart...");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume...");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}
}
