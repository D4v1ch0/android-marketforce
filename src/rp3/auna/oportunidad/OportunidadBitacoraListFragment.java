package rp3.auna.oportunidad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import rp3.app.BaseFragment;
import rp3.auna.R;
import rp3.auna.models.oportunidad.Oportunidad;
import rp3.auna.models.oportunidad.OportunidadBitacora;

/**
 * Created by magno_000 on 07/09/2015.
 */
public class OportunidadBitacoraListFragment extends BaseFragment implements OportunidadBitacoraDetailFragment.OportunidadBitacoraListListener {

    private static final String TAG = OportunidadBitacoraListFragment.class.getSimpleName();
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
        Log.d(TAG,"onAttach...");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idOportunidad = getArguments().getInt(ARG_OPORTUNIDAD);
        Log.d(TAG,"onCreate...");
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
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
                showDialogFragment(subFragment, "Bitácora", oportunidad.getDescripcion());
            }
        });

        ((ListView) getRootView().findViewById(R.id.bitacora_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subFragment = OportunidadBitacoraDetailFragment.newInstance(idOportunidad, (int) list.get(position).getID());
                showDialogFragment(subFragment, "Bitácora", oportunidad.getDescripcion());
            }
        });
    }

    @Override
    public void Refresh() {
        onResume();
        Log.d(TAG,"Refresh...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        if (resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK && null != data) {
                if(subFragment != null)
                    subFragment.onActivityResult(requestCode, resultCode, data);
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

}
