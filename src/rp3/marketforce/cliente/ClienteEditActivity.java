package rp3.marketforce.cliente;

import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class ClienteEditActivity extends rp3.app.BaseActivity implements ClienteEditFragment.OnClienteEditListener {

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
		finish();
	}			

}
