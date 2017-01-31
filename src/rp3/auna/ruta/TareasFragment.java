package rp3.auna.ruta;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.Agenda;
import rp3.auna.models.AgendaTarea;
import rp3.auna.models.Tarea;

import android.os.Bundle;
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
	public static String ARG_AGENDA = "agenda";
	private LinearLayout Grupo;
	private List<Tarea> tareas;
	private ArrayList<String> respuestas;
	private long idAgenda = 0;

	public static TareasFragment newInstance(List<Tarea> tareas) {
		TareasFragment fragment = new TareasFragment();
		ArrayList<String> strings = new ArrayList<String>();
		for (Tarea tarea : tareas) {
			strings.add(tarea.getNombreTarea());
		}
		Bundle arguments = new Bundle();
		arguments.putStringArrayList(ARG_TAREAS, strings);
		fragment.setArguments(arguments);
		return fragment;
	}

	public static TareasFragment newInstance(List<AgendaTarea> tareas, long idAgenda) {
		TareasFragment fragment = new TareasFragment();
		ArrayList<String> strings = new ArrayList<String>();
		ArrayList<String> stringsRealizadas = new ArrayList<String>();
		for (AgendaTarea tarea : tareas) {
			if(tarea.getNombreTarea() != null)
				strings.add(tarea.getNombreTarea());
		}
		Bundle arguments = new Bundle();
		arguments.putStringArrayList(ARG_TAREAS, strings);
		arguments.putLong(ARG_AGENDA, idAgenda);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		respuestas = getArguments().getStringArrayList(ARG_TAREAS);

		try {
			idAgenda = getArguments().getLong(ARG_AGENDA);
		}
		catch(Exception ex)
		{}

		View view = inflater.inflate(R.layout.fragment_dialog_tareas, container);

		Grupo = (LinearLayout) view.findViewById(R.id.tareas_container);

		getDialog().setTitle("Tareas");

		tareas = Tarea.getTareasVigentes(getDataBase());
		for (Tarea tarea : tareas) {
			CheckBox setter = new CheckBox(getActivity());
			setter.setButtonDrawable(R.drawable.custom_checkbox);
			if (respuestas != null && tarea.getIdTarea() != 0)
				setter.setChecked(existeRespuesta(tarea.getNombreTarea()));
			if (idAgenda != 0)
				setter.setEnabled(!existeRespuesta(tarea.getNombreTarea()));
			setter.setText(tarea.getNombreTarea());
			setter.setPadding(30, 15, 0, 15);
			Grupo.addView(setter);
		}

		((Button) view.findViewById(R.id.actividad_aceptar)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<Tarea> respuestasTareas = new ArrayList<Tarea>();
				for (int i = 0; i < Grupo.getChildCount(); i++) {
					if (((CheckBox) Grupo.getChildAt(i)).isChecked() && !existeRespuesta(tareas.get(i).getNombreTarea())) {
						respuestasTareas.add(tareas.get(i));
					}
				}
				if(idAgenda == 0) {
					CrearVisitaFragment activity = (CrearVisitaFragment) getParentFragment();
					activity.onFinishTareasDialog(respuestasTareas);
					dismiss();
				}
				else
				{
					Agenda agd = Agenda.getAgenda(getDataBase(), idAgenda);
					for(Tarea tarea : respuestasTareas) {
						AgendaTarea agendaTarea = new AgendaTarea();
						agendaTarea.setIdAgenda(agd.getIdAgenda());
						agendaTarea.set_idAgenda(idAgenda);
						agendaTarea.setEstadoTarea("P");
						agendaTarea.setIdRuta(PreferenceManager.getInt(Contants.KEY_IDRUTA));
						agendaTarea.setIdTarea(tarea.getIdTarea());
						AgendaTarea.insert(getDataBase(), agendaTarea);
					}
					getParentFragment().onResume();
					dismiss();
				}
			}
		});

		((Button) view.findViewById(R.id.actividad_cancelar)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return view;
	}

	public boolean existeRespuesta(String resp) {
		for (String r : respuestas) {
			if (r.equalsIgnoreCase(resp)) {
				return true;
			}
		}
		return false;
	}

}
