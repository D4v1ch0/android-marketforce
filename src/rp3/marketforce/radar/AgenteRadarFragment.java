package rp3.marketforce.radar;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.AgenteUbicacion;

/**
 * Created by magno_000 on 20/08/2015.
 */
public class AgenteRadarFragment extends BaseFragment {
    public interface AgenteRadarDialogListener {
        void onFinishAgenteRadarDialog(ArrayList<Integer> notShow);
    }

    public final static String ARG_LIST = "list";
    private ArrayList<Integer> ids;
    private AgenteRadarAdapter adapter;

    public static AgenteRadarFragment newInstance(ArrayList<Integer> list) {
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_LIST, list);
        AgenteRadarFragment fragment = new AgenteRadarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_LIST)) {
            ids = getArguments().getIntegerArrayList(ARG_LIST);
        }

        super.setContentView(R.layout.fragment_agente_radar);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        List<AgenteUbicacion> list_ubicaciones = AgenteUbicacion.getResumen(getDataBase());
        adapter = new AgenteRadarAdapter(this.getContext(), list_ubicaciones, ids);
        ((ListView) rootView.findViewById(R.id.agentes_list)).setAdapter(adapter);

        rootView.findViewById(R.id.agente_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadarFragment activity = (RadarFragment) getParentFragment();
                activity.onFinishAgenteRadarDialog(adapter.getListNotShowed());
                dismiss();
            }
        });

        rootView.findViewById(R.id.agente_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}

