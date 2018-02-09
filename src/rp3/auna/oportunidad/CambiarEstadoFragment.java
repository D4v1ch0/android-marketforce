package rp3.auna.oportunidad;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.models.GeneralValue;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.oportunidad.Oportunidad;
import rp3.auna.models.oportunidad.OportunidadBitacora;

/**
 * Created by magno_000 on 28/05/2015.
 */
public class CambiarEstadoFragment extends BaseFragment {

    private static final String TAG = CambiarEstadoFragment.class.getSimpleName();
    private long id;
    public static CambiarEstadoFragment newInstance(long id) {
        Bundle arguments = new Bundle();
        arguments.putLong(CambiarEstadoActivity.ARG_ID, id);
        CambiarEstadoFragment fragment = new CambiarEstadoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        if (getArguments().containsKey(CambiarEstadoActivity.ARG_ID)) {
            id = getArguments().getLong(CambiarEstadoActivity.ARG_ID);
        }else if(savedInstanceState!=null){
            id = savedInstanceState.getLong(CambiarEstadoActivity.ARG_ID);
        }

        super.setContentView(R.layout.fragment_cambiar_estado);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        final Oportunidad opt = Oportunidad.getOportunidadId(getDataBase(), id);
        List<GeneralValue> values = GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_ESTADOS_OPORTUNIDAD);
        final List<GeneralValue> generalValues = new ArrayList<GeneralValue>();
        for(GeneralValue value : values)
        {
            if(!value.getCode().equalsIgnoreCase("ELIM") && !value.getCode().equalsIgnoreCase("C") && !opt.getEstado().equalsIgnoreCase(value.getCode()))
                generalValues.add(value);
        }

        ((ListView)rootView.findViewById(R.id.estados_list)).setAdapter(new EstadosAdapter(this.getContext(), generalValues));
        ((ListView)rootView.findViewById(R.id.estados_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                opt.setEstado(generalValues.get(i).getCode());
                opt.setPendiente(true);
                OportunidadBitacora bitacora = new OportunidadBitacora();
                bitacora.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));
                bitacora.setFecha(Calendar.getInstance().getTime());
                bitacora.setIdOportunidad(opt.getIdOportunidad());
                bitacora.set_idOportunidad((int) opt.getID());
                bitacora.setDetalle("Se cambió estado a " + generalValues.get(i).getValue());
                OportunidadBitacora.insert(getDataBase(), bitacora);
                Oportunidad.update(getDataBase(), opt);
                finish();
            }
        });

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
