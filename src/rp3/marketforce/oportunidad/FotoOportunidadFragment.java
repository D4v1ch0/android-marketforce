package rp3.marketforce.oportunidad;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.oportunidad.OportunidadContacto;
import rp3.marketforce.models.oportunidad.OportunidadFoto;
import rp3.marketforce.ruta.RutasDetailFragment;
import rp3.marketforce.utils.DrawableManager;

/**
 * Created by magno_000 on 25/05/2015.
 */
public class FotoOportunidadFragment extends BaseFragment {

    private long id;
    private int tipo;
    private DrawableManager DManager;


    public static FotoOportunidadFragment newInstance(long id, int tipo ) {
        Bundle arguments = new Bundle();
        arguments.putLong(FotoOportunidadActivity.ARG_ID, id);
        arguments.putInt(FotoOportunidadActivity.ARG_TIPO, tipo);
        FotoOportunidadFragment fragment = new FotoOportunidadFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(FotoOportunidadActivity.ARG_ID)) {
            id = getArguments().getLong(FotoOportunidadActivity.ARG_ID);
        }else if(savedInstanceState!=null){
            id = savedInstanceState.getLong(FotoOportunidadActivity.ARG_ID);
        }
        if (getArguments().containsKey(FotoOportunidadActivity.ARG_TIPO)) {
            tipo = getArguments().getInt(FotoOportunidadActivity.ARG_TIPO);
        }else if(savedInstanceState!=null){
            tipo = savedInstanceState.getInt(FotoOportunidadActivity.ARG_TIPO);
        }
        DManager = new DrawableManager();

        super.setContentView(R.layout.fragment_foto_oportunidad);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        if(id != 0)
        {
            switch (tipo)
            {
                case 1:
                    OportunidadContacto opCont = OportunidadContacto.getContactoInt(getDataBase(), id);
                    DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                                    rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opCont.getURLFoto().replace("\"",""),
                            (ImageView) this.getRootView().findViewById(R.id.image_set));
                    break;
                case 2:
                    OportunidadFoto opFoto = OportunidadFoto.getFotoInt(getDataBase(), id);
                    DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                                    rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opFoto.getURLFoto().replace("\"",""),
                            (ImageView) this.getRootView().findViewById(R.id.image_set));
                    break;
            }

        }
    }
}
