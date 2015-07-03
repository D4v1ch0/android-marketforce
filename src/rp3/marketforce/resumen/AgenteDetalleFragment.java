package rp3.marketforce.resumen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agente;

/**
 * Created by magno_000 on 03/07/2015.
 */
public class AgenteDetalleFragment extends BaseFragment {

    public int idAgente;

    public final static String ARG_AGENTE = "id_agente";

    public AgenteDetalleFragment newInstance(int idAgente)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_AGENTE, idAgente);
        AgenteDetalleFragment fragment = new AgenteDetalleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_agente_detalle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        Agente.getAgente(getDataBase(), idAgente);

        rootView.findViewById(R.id.agente_enviar_notificación).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enviar notificacion
            }
        });
    }
}
