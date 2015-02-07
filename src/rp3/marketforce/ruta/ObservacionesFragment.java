package rp3.marketforce.ruta;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.utils.Utils;

public class ObservacionesFragment extends BaseFragment {
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int PHOTO_1 = 1;
	public static final int PHOTO_2 = 2;
	public static final int PHOTO_3 = 3;
	
	public static final int MAX_WIDTH = 500;
	public static final int MAX_HEIGHT = 500;
	
	private Uri fileUri;
	private long idAgenda;
	private Agenda agenda;
	public boolean closed = false;
	public View parentView;

	public static ObservacionesFragment newInstance(long idAgenda)
	{
		Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, idAgenda);
		ObservacionesFragment fragment = new ObservacionesFragment();
		fragment.setArguments(arguments);
		return fragment;
	}
	
	public interface ObservacionesFragmentListener {
        public void onResumir();
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(RutasDetailFragment.ARG_ITEM_ID)) {            
            idAgenda = getArguments().getLong(RutasDetailFragment.ARG_ITEM_ID);   
        }else if(savedInstanceState!=null){
        	idAgenda = savedInstanceState.getLong(RutasDetailFragment.STATE_IDAGENDA);
        }    
        
        if(idAgenda != 0){        	
        	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
        }
        super.setContentView(R.layout.fragment_observaciones);
	}
	
	@Override
	public void onAttach(Activity activity) {    	
	    super.onAttach(activity);
	    setRetainInstance(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.action_save:
			agenda.setObservaciones(getTextViewString(R.id.obs_text));
			Agenda.update(getDataBase(), agenda);
			closed = true;
			finish();
			break;
		case R.id.action_cancel:
			closed = true;
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	    
	@Override
	public void onResume() {
	    super.onResume();
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		parentView = rootView;
		if(agenda.getFoto1Int() != null)
			((ImageButton) rootView.findViewById(R.id.obs_foto1)).setImageBitmap(Utils.resizeBitMapImage(agenda.getFoto1Int(), MAX_WIDTH, MAX_HEIGHT));
		if(agenda.getFoto2Int() != null)
			((ImageButton) rootView.findViewById(R.id.obs_foto2)).setImageBitmap(Utils.resizeBitMapImage(agenda.getFoto2Int(), MAX_WIDTH, MAX_HEIGHT));
		if(agenda.getFoto3Int() != null)
			((ImageButton) rootView.findViewById(R.id.obs_foto3)).setImageBitmap(Utils.resizeBitMapImage(agenda.getFoto3Int(), MAX_WIDTH, MAX_HEIGHT));
		if(agenda.getObservaciones() != null)
			setTextViewText(R.id.obs_text, agenda.getObservaciones());
		
		((ImageButton) rootView.findViewById(R.id.obs_foto1)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    fileUri = Utils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE); 
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
			    getActivity().startActivityForResult(intent, PHOTO_1);
			}
		});
		
		((ImageButton) rootView.findViewById(R.id.obs_foto2)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    fileUri = Utils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE); 
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
			    getActivity().startActivityForResult(intent, PHOTO_2);
				
			}
		});
		
		((ImageButton) rootView.findViewById(R.id.obs_foto3)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			    fileUri = Utils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE); 
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
			    getActivity().startActivityForResult(intent, PHOTO_3);
				
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String path = fileUri.getPath();
	        switch(requestCode)
	        {
	        case PHOTO_1:
	        	((ImageButton) getRootView().findViewById(R.id.obs_foto1)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500));
	        	agenda.setFoto1Int(path);
	        	break;
	        case PHOTO_2:
	        	((ImageButton) getRootView().findViewById(R.id.obs_foto2)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500));
	        	agenda.setFoto2Int(path);
	        	break;
	        case PHOTO_3:
	        	((ImageButton) getRootView().findViewById(R.id.obs_foto3)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500));
	        	agenda.setFoto3Int(path);
	        	break;
	        default:
	        	break;
	        }	        
	    }
	}
}
