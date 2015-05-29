package rp3.marketforce.oportunidad;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.oportunidad.Oportunidad;

/**
 * Created by magno_000 on 28/05/2015.
 */
public class CambiarEstadoFragment extends BaseFragment {
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

        List<GeneralValue> values = GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_ESTADOS_OPORTUNIDAD);
        final List<GeneralValue> generalValues = new ArrayList<GeneralValue>();
        for(GeneralValue value : values)
        {
            if(!value.getCode().equalsIgnoreCase("C"))
                generalValues.add(value);
        }

        ((ListView)rootView.findViewById(R.id.estados_list)).setAdapter(new EstadosAdapter(this.getContext(), generalValues));
        ((ListView)rootView.findViewById(R.id.estados_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Oportunidad opt = Oportunidad.getOportunidadId(getDataBase(), id);
                opt.setEstado(generalValues.get(i).getCode());
                Oportunidad.update(getDataBase(), opt);
                finish();
            }
        });

    }
}
