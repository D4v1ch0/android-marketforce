package rp3.marketforce.radar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private static final String TAG = AgenteRadarFragment.class.getSimpleName();
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
        Log.d(TAG,"onCreate...");
        if (getArguments().containsKey(ARG_LIST)) {
            ids = getArguments().getIntegerArrayList(ARG_LIST);
        }

        super.setContentView(R.layout.fragment_agente_radar);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        List<AgenteUbicacion> list_ubicaciones = AgenteUbicacion.getResumen(getDataBase());
        adapter = new AgenteRadarAdapter(this.getContext(), list_ubicaciones, ids);
        ((ListView) rootView.findViewById(R.id.agentes_list)).setAdapter(adapter);
        ((CheckBox) rootView.findViewById(R.id.import_seleccionar_todos)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.SelectAll(isChecked);
            }
        });
        if(ids.size() == list_ubicaciones.size())
            ((CheckBox) rootView.findViewById(R.id.import_seleccionar_todos)).setChecked(false);

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
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }


}

