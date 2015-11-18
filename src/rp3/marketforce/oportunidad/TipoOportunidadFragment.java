package rp3.marketforce.oportunidad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Tarea;
import rp3.marketforce.models.oportunidad.OportunidadBitacora;
import rp3.marketforce.models.oportunidad.OportunidadTipo;
import rp3.marketforce.ruta.CrearVisitaFragment;

/**
 * Created by magno_000 on 18/11/2015.
 */
public class TipoOportunidadFragment extends BaseFragment {
    public interface EditTiposDialogListener {
        void onFinishTiposDialog(List<OportunidadTipo> tipos);
    }

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

}
