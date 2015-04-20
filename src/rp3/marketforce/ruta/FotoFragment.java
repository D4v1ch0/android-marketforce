package rp3.marketforce.ruta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.CrearClienteFragment;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.BitmapUtils;

import static rp3.util.Screen.getOrientation;

/**
 * Created by magno_000 on 14/04/2015.
 */
public class FotoFragment extends BaseFragment {

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
        if(cli != null)
        {
            ((ImageView) this.getRootView().findViewById(R.id.foto_agenda)).setImageDrawable(getResources().getDrawable(R.drawable.user));
            DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agenda.getCliente().getURLFoto(),
                    (ImageView) this.getRootView().findViewById(R.id.foto_agenda));
        }
    }

    protected void takePicture(final int idView) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this.getActivity());
        myAlertDialog.setTitle("Fotografía");
        myAlertDialog.setMessage("Obtener de");

        myAlertDialog.setPositiveButton("Galería",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
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
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap pree = null;
            if(data.getData() != null) {
                try {
                    pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                if(data.getExtras().containsKey("data"))
                    pree = (Bitmap)data.getExtras().get("data");
                else
                    try {
                        photo = Uri.parse(data.getAction());
                        pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            cli.setURLFoto(Utils.SaveBitmap(pree, cli.getNombre1().trim()));
            Cliente.update(getDataBase(), cli);
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE_FULL);
            bundle.putLong(CrearClienteFragment.ARG_CLIENTE, cli.getID());
            requestSync(bundle);
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_editar_cliente:
                takePicture(1);
                break;
            case R.id.action_cancel:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
