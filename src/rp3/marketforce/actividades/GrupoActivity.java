package rp3.marketforce.actividades;

import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.AgendaTareaOpciones;
import rp3.marketforce.ruta.RutasDetailFragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class GrupoActivity extends ActividadActivity {
	
	LinearLayout Container;
	AgendaTareaActividades ata;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_grupo_actividad);
	    ata = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad);
	    Container = (LinearLayout) findViewById(R.id.actividad_agrupar);
	    
	    List<AgendaTareaActividades> list_atas = AgendaTareaActividades.getActividadesGrupales(getDataBase(), ata.getIdRuta(), ata.getIdAgenda(), ata.getIdTarea());
	    for(AgendaTareaActividades actividad : list_atas)
	    {
	    	if(actividad.getTipo().equalsIgnoreCase("G"))
				agregarGrupo(actividad);
	    	for(AgendaTareaActividades actividad_hija : actividad.getActividades_hijas())
	    	{
		    	if(actividad_hija.getTipo().equalsIgnoreCase("C"))
		    		agregarCheckbox(actividad_hija);	
				if(actividad_hija.getTipo().equalsIgnoreCase("M"))
					agregarMultiple(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("S"))
					agregarSeleccion(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("T"))
					agregarTexto(actividad_hija);
	    	}
	    }
	    
	}
	
	public void agregarGrupo(AgendaTareaActividades act)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		TextView texto = new TextView(this);
		texto.setLayoutParams(params);
		texto.setText(act.getDescripcion());
		texto.setGravity(Gravity.CENTER);
		texto.setTypeface(Typeface.DEFAULT_BOLD);
		Container.addView(texto);
	}
	
	public void agregarCheckbox(final AgendaTareaActividades act)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 3);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		
		LinearLayout layout = new LinearLayout(this);
		TextView texto = new TextView(this);
		CheckBox check = new CheckBox(this);
		
		texto.setLayoutParams(params);
		check.setLayoutParams(params2);
		check.setButtonDrawable(R.drawable.custom_checkbox);
		
		texto.setText(act.getDescripcion());
		if(act.getResultado().equals("true"))
			check.setChecked(true);
		check.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				act.setResultado(isChecked + "");
				AgendaTareaActividades.update(getDataBase(), act);
			}});
		
		layout.addView(texto);
		layout.addView(check);
		Container.addView(layout);
		Container.addView(getLinea());
	}
	
	public void agregarTexto(final AgendaTareaActividades act)
	{		
		LinearLayout layout = new LinearLayout(this);
		TextView texto = new TextView(this);
		EditText textoResp = new EditText(this);
		
		texto.setText(act.getDescripcion());
		layout.setOrientation(LinearLayout.VERTICAL);
		textoResp.setFocusable(false);
		textoResp.setFocusableInTouchMode(false);
		if(!act.getResultado().equalsIgnoreCase("null"))
			textoResp.setText(act.getResultado());
		layout.setClickable(true);
		layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), TextoActivity.class);
				intent.putExtra(RutasDetailFragment.ARG_ITEM_ID, (int)act.getIdTareaActividad());
				intent.putExtra(RutasDetailFragment.ARG_AGENDA_ID, (long) act.getIdAgenda());
				intent.putExtra(RutasDetailFragment.ARG_RUTA_ID, (int) act.getIdRuta());
				intent.putExtra(ARG_PADRE_ID, (int) act.getIdTareaActividadPadre());
				intent.putExtra(ARG_TAREA, (int) act.getIdTarea());
				intent.putExtra(ARG_THEME, R.style.AppDialogTheme);
				startActivity(intent);
			}});
		
		layout.addView(texto);
		layout.addView(textoResp);
		Container.addView(layout);
		Container.addView(getLinea());
	}
	
	public void agregarSeleccion(final AgendaTareaActividades act)
	{
		
		LinearLayout layout = new LinearLayout(this);
		TextView texto = new TextView(this);
		Spinner combo = new Spinner(this);
		
		texto.setPadding(0, 0, 0, 15);

		texto.setText(act.getDescripcion());
		layout.setOrientation(LinearLayout.VERTICAL);
		List<AgendaTareaOpciones> ag_opcs = AgendaTareaOpciones.getOpciones(getDataBase(), act.getIdAgenda(), act.getIdTarea(), act.getIdTareaActividad());
		List<String> opciones = new ArrayList<String>();
		
		for(AgendaTareaOpciones opcion: ag_opcs)
		{
			opciones.add(opcion.getDescripcion());
		}
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opciones);
		combo.setAdapter(adapter);
		combo.setSelection(adapter.getPosition(act.getResultado()));
		combo.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				act.setResultado(adapter.getItem(position));
				AgendaTareaActividades.update(getDataBase(), act);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}});
		
		layout.addView(texto);
		layout.addView(combo);
		Container.addView(layout);
		Container.addView(getLinea());
	}
	
	public void agregarMultiple(final AgendaTareaActividades act)
	{
		LinearLayout layout = new LinearLayout(this);
		TextView texto = new TextView(this);
		EditText textoResp = new EditText(this);
		
		texto.setText(act.getDescripcion());
		layout.setOrientation(LinearLayout.VERTICAL);
		textoResp.setEnabled(false);
		if(!act.getResultado().equalsIgnoreCase("null"))
			textoResp.setText(act.getResultado());
		layout.setClickable(true);
		layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), MultipleActivity.class);
				intent.putExtra(RutasDetailFragment.ARG_ITEM_ID, (int)act.getIdTareaActividad());
				intent.putExtra(RutasDetailFragment.ARG_AGENDA_ID, (long) act.getIdAgenda());
				intent.putExtra(RutasDetailFragment.ARG_RUTA_ID, (int) act.getIdRuta());
				intent.putExtra(ARG_PADRE_ID, (int) act.getIdTareaActividadPadre());
				intent.putExtra(ARG_TAREA, (int) act.getIdTarea());
				intent.putExtra(ARG_THEME, R.style.AppDialogTheme);
				startActivity(intent);
			}});
		
		layout.addView(texto);
		layout.addView(textoResp);
		Container.addView(layout);
		Container.addView(getLinea());
	}

	@Override
	public void aceptarCambios(View v) {
		AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), ata.getIdAgenda(), ata.getIdRuta(), ata.getIdTarea());
		agt.setEstadoTarea("R");
		AgendaTarea.update(getDataBase(), agt);
		finish();
		
	}
	
	@SuppressLint("ResourceAsColor")
	public LinearLayout getLinea()
	{
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		params3.setMargins(0, 20, 0, 20);
		LinearLayout linea = new LinearLayout(this);
		linea.setLayoutParams(params3);
		
		linea.setBackgroundColor(R.color.bg_button_bg_main_pressed);
		return linea;
	}
	
	@Override
	protected void onResume() {
		Container.removeAllViews();
		List<AgendaTareaActividades> list_atas = AgendaTareaActividades.getActividadesGrupales(getDataBase(), ata.getIdRuta(), ata.getIdAgenda(), ata.getIdTarea());
	    for(AgendaTareaActividades actividad : list_atas)
	    {
	    	if(actividad.getTipo().equalsIgnoreCase("G"))
				agregarGrupo(actividad);
	    	for(AgendaTareaActividades actividad_hija : actividad.getActividades_hijas())
	    	{
		    	if(actividad_hija.getTipo().equalsIgnoreCase("C"))
		    		agregarCheckbox(actividad_hija);	
				if(actividad_hija.getTipo().equalsIgnoreCase("M"))
					agregarMultiple(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("S"))
					agregarSeleccion(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("T"))
					agregarTexto(actividad_hija);
	    	}
	    }
		super.onResume();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
