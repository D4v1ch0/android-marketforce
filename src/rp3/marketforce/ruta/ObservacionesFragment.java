package rp3.marketforce.ruta;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.utils.Utils;
import rp3.util.StringUtils;

import static rp3.util.Screen.getOrientation;

public class ObservacionesFragment extends BaseFragment {
	private static final String TAG = ObservacionesFragment.class.getSimpleName();
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int PHOTO_1 = 1;
	public static final int PHOTO_2 = 2;
	public static final int PHOTO_3 = 3;
	
	public static final int MAX_WIDTH = 500;
	public static final int MAX_HEIGHT = 500;
    private final int REQ_CODE_SPEECH_INPUT = 100;
	
	private Uri fileUri;
	private long idAgenda;
	private Agenda agenda;
	public boolean closed = false, soloVista;
	public View parentView;

	public static ObservacionesFragment newInstance(long idAgenda, boolean soloVista)
	{
		Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, idAgenda);
        arguments.putBoolean(RutasDetailFragment.ARG_SOLO_VISTA, soloVista);
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
            if(agenda == null)
                agenda = Agenda.getAgendaClienteNull(getDataBase(), idAgenda);
        }
        soloVista = getArguments().getBoolean(RutasDetailFragment.ARG_SOLO_VISTA);
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
			agenda.setObservaciones(getTextViewString(R.id.obs_text).trim());
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
        Log.d(TAG,"onResume...");
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		parentView = rootView;
		if(agenda.getFoto1Int() != null)
			((ImageButton) rootView.findViewById(R.id.obs_foto1)).setImageBitmap(Utils.resizeBitMapImage(agenda.getFoto1Int(), MAX_WIDTH, MAX_HEIGHT, 0));
		if(agenda.getFoto2Int() != null)
			((ImageButton) rootView.findViewById(R.id.obs_foto2)).setImageBitmap(Utils.resizeBitMapImage(agenda.getFoto2Int(), MAX_WIDTH, MAX_HEIGHT, 0));
		if(agenda.getFoto3Int() != null)
			((ImageButton) rootView.findViewById(R.id.obs_foto3)).setImageBitmap(Utils.resizeBitMapImage(agenda.getFoto3Int(), MAX_WIDTH, MAX_HEIGHT, 0));
		if(agenda.getObservaciones() != null)
			setTextViewText(R.id.obs_text, agenda.getObservaciones());

        if(soloVista) {
            ((ImageButton) rootView.findViewById(R.id.obs_foto1)).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = Utils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    intent.putExtra("proof", fileUri.getPath());
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
            ((ImageView) rootView.findViewById(R.id.observaciones_voice_to_text)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    promptSpeechInput();
                }
            });
        }
        else
        {
            ((EditText) rootView.findViewById(R.id.obs_text)).setKeyListener(null);
            ((EditText) rootView.findViewById(R.id.obs_text)).setTextIsSelectable(true);
            rootView.findViewById(R.id.observaciones_voice_to_text).setVisibility(View.GONE);
        }
	}

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable Ahora");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
            Log.d(TAG,"onActivityResullt...requdcode OK");
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT:
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        setTextViewText(R.id.obs_text, StringUtils.getStringCapSentence(result.get(0)));
                    }
                    break;
                default:
                    if(fileUri != null) {
                        String path = fileUri.getPath();
                        int rotation = 0;
                        try {
                            ExifInterface exif = new ExifInterface(path);
                            rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        }
                        catch (Exception ex)
                        {}
                        switch (requestCode) {
                            case PHOTO_1:
                                ((ImageButton) getRootView().findViewById(R.id.obs_foto1)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500, rotation));
                                agenda.setFoto1Int(path);
                                agenda.setFoto1Ext(null);
                                break;
                            case PHOTO_2:
                                ((ImageButton) getRootView().findViewById(R.id.obs_foto2)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500, rotation));
                                agenda.setFoto2Int(path);
                                agenda.setFoto2Ext(null);
                                break;
                            case PHOTO_3:
                                ((ImageButton) getRootView().findViewById(R.id.obs_foto3)).setImageBitmap(Utils.resizeBitMapImage(path, 500, 500, rotation));
                                agenda.setFoto3Int(path);
                                agenda.setFoto3Ext(null);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
            }
	    }
	}

    /**
     *
     * Ciclo de vida
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }



    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }
}
