package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.oportunidad.Etapa;
import rp3.marketforce.models.oportunidad.OportunidadEtapa;

/**
 * Created by magno_000 on 30/11/2015.
 */
public class EtapasDefinicionFragment extends BaseFragment {

    public static final String ID_ETAPA = "id_etapa";

    private int idTipoOportunidad;
    private List<OportunidadEtapa> optEtapas;
    private EtapaDefinicionAdapter adapter;
    public static EtapasDefinicionFragment newInstance(int idEtapa)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(ID_ETAPA, idEtapa);
        EtapasDefinicionFragment fragment = new EtapasDefinicionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optEtapas = new ArrayList<OportunidadEtapa>();

        idTipoOportunidad = getArguments().getInt(ID_ETAPA);

        List<Etapa> etapas = Etapa.getEtapasAll(getDataBase(), idTipoOportunidad);


        int etapaPadre = -1;
        for (Etapa etapa : etapas) {
            OportunidadEtapa oportunidadEtapa = new OportunidadEtapa();
            oportunidadEtapa.setEstado("P");
            oportunidadEtapa.setIdEtapa(etapa.getIdEtapa());
            if (etapa.getOrden() == 1 && (etapa.getIdEtapaPadre() == 0 || etapa.getIdEtapaPadre() == etapaPadre)) {
                oportunidadEtapa.setFechaInicio(Calendar.getInstance().getTime());
                if (etapa.getIdEtapaPadre() == 0)
                    etapaPadre = etapa.getIdEtapa();
            }
            oportunidadEtapa.setIdEtapaPadre(etapa.getIdEtapaPadre());
            oportunidadEtapa.setObservacion("");
            oportunidadEtapa.setEtapa(etapa);
            optEtapas.add(oportunidadEtapa);
        }
    }

    public interface EtapasDefinicionListener
    {
        void onEtapasFinish(List<OportunidadEtapa> etapas);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_etapas_definicion);

    }

    @Override
    public void onDailogDatePickerChange(int id, Calendar c) {
        super.onDailogDatePickerChange(id, c);
        optEtapas.get(id).setFechaFinPlan(c.getTime());
        adapter.changeList(optEtapas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        adapter = new EtapaDefinicionAdapter(getContext(),optEtapas);
        ((ListView) rootView.findViewById(R.id.etapas_list)).setAdapter(adapter);
        ((ListView) rootView.findViewById(R.id.etapas_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(optEtapas.get(position).getEtapa().isEsVariable())
                    showDialogDatePicker(position, Calendar.getInstance());
            }
        });

        rootView.findViewById(R.id.button_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearOportunidadFragment activity = (CrearOportunidadFragment) getParentFragment();
                activity.onEtapasFinish(optEtapas);
                dismiss();
            }
        });

        rootView.findViewById(R.id.button_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
