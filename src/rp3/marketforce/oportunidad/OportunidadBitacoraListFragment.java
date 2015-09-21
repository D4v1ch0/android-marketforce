package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agente;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadBitacora;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.Utils;
import rp3.util.ConnectionUtils;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 07/09/2015.
 */
public class OportunidadBitacoraListFragment extends BaseFragment implements OportunidadBitacoraDetailFragment.OportunidadBitacoraListListener {

    public final static String ARG_OPORTUNIDAD = "id_oportunidad";

    private int idOportunidad;
    private Oportunidad oportunidad;
    private OportunidadBitacoraDetailFragment subFragment;
    private List<OportunidadBitacora> list;

    public static OportunidadBitacoraListFragment newInstance(int idOportunidad)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_OPORTUNIDAD, idOportunidad);
        OportunidadBitacoraListFragment fragment = new OportunidadBitacoraListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_oportunidad_bitacora_list);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idOportunidad = getArguments().getInt(ARG_OPORTUNIDAD);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        oportunidad = Oportunidad.getOportunidadId(getDataBase(), idOportunidad);

        list = OportunidadBitacora.getBitacoraOportunidad(getDataBase(), oportunidad.getIdOportunidad());
        if(list.size() == 0)
            list = OportunidadBitacora.getBitacoraOportunidadInt(getDataBase(), oportunidad.getID());

        OportunidadBitacoraAdapter adapter = new OportunidadBitacoraAdapter(this.getContext(), list);

        ((ListView) getRootView().findViewById(R.id.bitacora_list)).setAdapter(adapter);
        getRootView().findViewById(R.id.bitacora_agregar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subFragment = OportunidadBitacoraDetailFragment.newInstance(idOportunidad);
                showDialogFragment(subFragment, "Bitácora", "Agregar Bitácora");
            }
        });

        ((ListView) getRootView().findViewById(R.id.bitacora_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subFragment = OportunidadBitacoraDetailFragment.newInstance(idOportunidad, (int) list.get(position).getID());
                showDialogFragment(subFragment, "Bitácora", "Ver Bitácora");
            }
        });
    }

    @Override
    public void Refresh() {
        onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK && null != data) {
                if(subFragment != null)
                    subFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
