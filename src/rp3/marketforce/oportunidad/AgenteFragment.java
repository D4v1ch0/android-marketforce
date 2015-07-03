package rp3.marketforce.oportunidad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agente;

/**
 * Created by magno_000 on 28/05/2015.
 */
public class AgenteFragment extends BaseFragment {
    private ArrayList<Integer> respuestas;
    private ListView Grupo;
    private List<Agente> agentes;

    public interface EditAgentesListener {
        void onFinishAgentesDialog(Agente agente);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        respuestas = getArguments().getIntegerArrayList(ARG_AGENTES);

        View view = inflater.inflate(R.layout.fragment_agentes, container);

        Grupo = (ListView) view.findViewById(R.id.list_agentes);

        getDialog().setTitle("Agentes");

        final List<String> agentesString = new ArrayList<String>();
        agentes = Agente.getAgentes(getDataBase());
        List<Agente> realAgentes = new ArrayList<Agente>();
        for(Agente agente: agentes)
        {
            if(respuestas.indexOf(agente.getIdAgente()) <= -1)
            {
                realAgentes.add(agente);
                agentesString.add(agente.getNombre());
            }
        }

        agentes = realAgentes;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, agentesString.toArray(new String[agentesString.size()]));
        Grupo.setAdapter(adapter);
        Grupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CrearOportunidadFragment activity = (CrearOportunidadFragment) getParentFragment();
                activity.onFinishAgentesDialog(agentes.get(i));
                dismiss();
            }
        });

        return view;
    }
}
