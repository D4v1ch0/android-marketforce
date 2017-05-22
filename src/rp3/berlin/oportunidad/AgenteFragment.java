package rp3.berlin.oportunidad;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.berlin.R;
import rp3.berlin.models.Agente;

/**
 * Created by magno_000 on 28/05/2015.
 */
public class AgenteFragment extends BaseFragment {
    private ArrayList<Integer> respuestas;
    private ListView Grupo;
    private List<Agente> agentes;
    private AgenteOportunidadAdapter adapter;

    public interface EditAgentesListener {
        void onFinishAgentesDialog(ArrayList<Integer> agentes);
    }

    public static String ARG_AGENTES = "agentes";

    public static AgenteFragment newInstance(List<Integer> agentes) {
        AgenteFragment fragment = new AgenteFragment();
        ArrayList<Integer> strings = new ArrayList<Integer>();
        strings.addAll(agentes);
        Bundle arguments = new Bundle();
        arguments.putIntegerArrayList(ARG_AGENTES, strings);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.fragment_agente_radar);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        respuestas = getArguments().getIntegerArrayList(ARG_AGENTES);

        List<Agente> agentes = Agente.getAgentes(getDataBase());

        adapter = new AgenteOportunidadAdapter(this.getContext(), agentes, respuestas);

        ((ListView) getRootView().findViewById(R.id.agentes_list)).setAdapter(adapter);
        ((View)getRootView().findViewById(R.id.import_seleccionar_todos).getParent()).setVisibility(View.GONE);

        getRootView().findViewById(R.id.agente_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearOportunidadFragment activity = (CrearOportunidadFragment) getParentFragment();
                activity.onFinishAgentesDialog(adapter.getListNotShowed());
                dismiss();
            }
        });

        getRootView().findViewById(R.id.agente_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
