package rp3.auna.ruta;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.cliente.ClientDetailActivity;
import rp3.auna.cliente.CrearClienteFragment;
import rp3.auna.models.Agenda;
import rp3.auna.models.Cliente;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.utils.DrawableManager;
import rp3.auna.utils.Utils;

/**
 * Created by magno_000 on 14/04/2015.
 */
public class FotoFragment extends BaseFragment {

    private static final String TAG = FotoFragment.class.getSimpleName();
    private long idAgenda;
    private Agenda agenda;
    private Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
    private DrawableManager DManager;
    private Cliente cli;

    public static FotoFragment newInstance(long idAgenda)
    {
        Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, idAgenda);
        FotoFragment fragment = new FotoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        if (getArguments().containsKey(RutasDetailFragment.ARG_ITEM_ID)) {
            idAgenda = getArguments().getLong(RutasDetailFragment.ARG_ITEM_ID);
        }else if(savedInstanceState!=null){
            idAgenda = savedInstanceState.getLong(RutasDetailFragment.STATE_IDAGENDA);
        }
        DManager = new DrawableManager();
        if(idAgenda != 0){
            agenda = Agenda.getAgenda(getDataBase(), idAgenda);
            if (agenda == null) {
                Toast.makeText(this.getContext(), "Cliente esta eliminado de la ruta", Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                cli = Cliente.getClienteID(getDataBase(), agenda.getCliente().getID(), false);
            }
        }
        super.setContentView(R.layout.fragment_foto_agenda);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        if(cli != null)
        {
            ((ImageView) this.getRootView().findViewById(R.id.foto_agenda)).setImageDrawable(getResources().getDrawable(R.drawable.user));
            DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agenda.getCliente().getURLFoto(),
                    (ImageView) this.getRootView().findViewById(R.id.foto_agenda));
            setButtonClickListener(R.id.foto_agenda_button, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePicture(1);
                }
            });
        }
    }

    protected void takePicture(final int idView) {
        Log.d(TAG,"takePicture...");
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this.getActivity());
        myAlertDialog.setTitle("Fotografía");
        myAlertDialog.setMessage("Obtener de");

        myAlertDialog.setPositiveButton("Galería",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Log.d(TAG,"Galeria...");
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.putExtra("return-data", true);
                        galleryIntent.putExtra("crop", "true");
                        galleryIntent.putExtra("aspectX", 1);
                        galleryIntent.putExtra("aspectY", 1);
                        getActivity().startActivityForResult(galleryIntent, idView);
                    }
                });

        myAlertDialog.setNegativeButton("Cámara",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Log.d(TAG,"Camara...");
                        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
                        captureIntent.putExtra("crop", "true");
                        captureIntent.putExtra("aspectX", 1);
                        captureIntent.putExtra("aspectY", 1);
                        getActivity().startActivityForResult(captureIntent, idView);

                    }
                });
        myAlertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        if (resultCode == RESULT_OK) {
            Log.d(TAG,"RESULT_OK...");
            Bitmap pree = null;
            if(data != null) {
                Log.d(TAG,"data != null...");
                if (data.getData() != null) {
                    Log.d(TAG,"data.getData() != null...");
                    try {
                        pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG,e.getLocalizedMessage());
                    }
                } else if (data.getExtras().containsKey("data"))
                {Log.d(TAG,"data.getExtras().containsKey(data)...");
                    pree = (Bitmap) data.getExtras().get("data");}
                else
                    try {Log.d(TAG,"else...data.getAction()...");
                        photo = Uri.parse(data.getAction());
                        pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                cli.setURLFoto(Utils.SaveBitmap(pree, cli.getNombre1().trim()));
            }
            else
            {
                Log.d(TAG,"data==null...");
                performCrop();
                return;
            }
            Cliente.update(getDataBase(), cli);
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE_FULL);
            bundle.putLong(CrearClienteFragment.ARG_CLIENTE, cli.getID());
            requestSync(bundle);
            finish();
        }
    }

    private void performCrop() {
        try {
            Log.d(TAG,"performCrop...");
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(photo, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, 3);
        }
        catch (ActivityNotFoundException anfe) {
            Log.d(TAG,"ActivityNotFoundException..."+anfe.getMessage());
            Toast toast = Toast
                    .makeText(this.getContext(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_editar_cliente:
                Log.d(TAG,"action_editar_cliente...");
                Intent intent2 = new Intent(getActivity(), ClientDetailActivity.class);
                intent2.putExtra(ClientDetailActivity.ARG_ITEM_ID, cli.getIdCliente());
                startActivity(intent2);
                break;
            case R.id.action_cancel:
                Log.d(TAG,"action_cancel...");
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

}
