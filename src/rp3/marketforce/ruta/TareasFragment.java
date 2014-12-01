package rp3.marketforce.ruta;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientDetailFragment;
import rp3.marketforce.models.AgendaTareaOpciones;
import rp3.marketforce.models.Tarea;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class TareasFragment extends BaseFragment {
	 public interface EditTareasDialogListener {
	        void onFinishTareasDialog(List<Tarea> tareas);
	 }

	public static String ARG_TAREAS = "tareas";
	private LinearLayout Grupo;
	private List<Tarea> tareas;
	private ArrayList<String> respuestas;
	
	public static TareasFragment newInstance(List<Tarea> tareas) {
		TareasFragment fragment = new TareasFragment();
		ArrayList<String> strings = new ArrayList<String>();
		for(Tarea tarea : tareas)
		{
			strings.add(tarea.getNombreTarea());
		}
		Bundle arguments = new Bundle();
		arguments.putStringArrayList(ARG_TAREAS, strings);
		fragment.setArguments(arguments);
		return fragment;
    }

	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 	respuestas = getArguments().getStringArrayList(ARG_TAREAS);
		 
	        View view = inflater.inflate(R.layout.fragment_dialog_tareas, container);
	        
	        Grupo = (LinearLayout) view.findViewById(R.id.tareas_container);
	        
	        getDialog().setTitle("Tareas");
	        
	        tareas = Tarea.getTareas(getDataBase());
		    for(Tarea tarea: tareas)
			{
		    	CheckBox setter = new CheckBox(getActivity());
		    	setter.setButtonDrawable(R.drawable.custom_checkbox);
		    	if(respuestas != null)
		    		setter.setChecked(existeRespuesta(tarea.getNombreTarea()));
		    	setter.setText(tarea.getNombreTarea());
		    	setter.setPadding(30, 15, 0, 15);
				Grupo.addView(setter);
			}
		    
		    ((Button) view.findViewById(R.id.actividad_aceptar)).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					List<Tarea> respuestasTareas = new ArrayList<Tarea>();
					for(int i = 0; i < Grupo.getChildCount();i++)
					{
						if(((CheckBox)Grupo.getChildAt(i)).isChecked())
						{
							respuestasTareas.add(tareas.get(i));
						}
					}
					CrearVisitaFragment activity = (CrearVisitaFragment) getParentFragment();
		            activity.onFinishTareasDialog(respuestasTareas);
					dismiss();
				}});
			
			((Button) view.findViewById(R.id.actividad_cancelar)).setOnClickListener(new OnClickListener(){

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
