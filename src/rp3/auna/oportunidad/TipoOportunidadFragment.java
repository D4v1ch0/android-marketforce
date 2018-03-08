package rp3.auna.oportunidad;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.auna.R;
import rp3.auna.models.oportunidad.OportunidadTipo;

/**
 * Created by magno_000 on 18/11/2015.
 */
public class TipoOportunidadFragment extends BaseFragment {
    public interface EditTiposDialogListener {
        void onFinishTiposDialog(List<OportunidadTipo> tipos);
    }

    private static final String TAG = TipoOportunidadFragment.class.getSimpleName();
    public static String ARG_TIPOS = "tipos";
    private LinearLayout Grupo;
    private List<OportunidadTipo> tipos;
    private ArrayList<String> respuestas;

    public static TipoOportunidadFragment newInstance(List<OportunidadTipo> tipos) {
        TipoOportunidadFragment fragment = new TipoOportunidadFragment();
        ArrayList<String> strings = new ArrayList<String>();
        for(OportunidadTipo tipo : tipos)
        {
            strings.add(tipo.getDescripcion());
        }
        Bundle arguments = new Bundle();
        arguments.putStringArrayList(ARG_TIPOS, strings);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        respuestas = getArguments().getStringArrayList(ARG_TIPOS);

        View view = inflater.inflate(R.layout.fragment_dialog_tareas, container);

        Grupo = (LinearLayout) view.findViewById(R.id.tareas_container);

        getDialog().setTitle("Tipos de Oportunidad");

        tipos = OportunidadTipo.getOportunidadTipoAll(getDataBase());
        for(OportunidadTipo tipo: tipos)
        {
            CheckBox setter = new CheckBox(getActivity());
            setter.setButtonDrawable(R.drawable.custom_checkbox);
            if(respuestas != null)
                setter.setChecked(existeRespuesta(tipo.getDescripcion()));
            setter.setText(tipo.getDescripcion());
            setter.setPadding(30, 15, 0, 15);
            Grupo.addView(setter);
        }

        ((Button) view.findViewById(R.id.actividad_aceptar)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                List<OportunidadTipo> respuestasTareas = new ArrayList<OportunidadTipo>();
                for(int i = 0; i < Grupo.getChildCount();i++)
                {
                    if(((CheckBox)Grupo.getChildAt(i)).isChecked())
                    {
                        respuestasTareas.add(tipos.get(i));
                    }
                }
                FiltroOportunidadFragment activity = (FiltroOportunidadFragment) getParentFragment();
                activity.onFinishTiposDialog(respuestasTareas);
                dismiss();
            }});

        ((Button) view.findViewById(R.id.actividad_cancelar)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }});

        return view;
    }

    public boolean existeRespuesta(String resp)
    {
        for(String r : respuestas)
        {
            if(r.equalsIgnoreCase(resp))
            {
                return true;
            }
        }
        return false;
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