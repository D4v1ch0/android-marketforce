package rp3.marketforce.actividades;

import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.AgendaTareaOpciones;
import rp3.marketforce.ruta.RutasDetailFragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class GrupoActivity extends ActividadActivity {
	
	LinearLayout Container;
	int contador = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
	    	setContentView(R.layout.layout_grupo_actividad_land);
		}
		else
		{
			setContentView(R.layout.layout_grupo_actividad);
		}
	    
	    Container = (LinearLayout) findViewById(R.id.actividad_agrupar);
	    
	    List<Actividad> list_atas = Actividad.getActividadesGrupalesByTarea(getDataBase(), id_actividad);
	    for(Actividad actividad : list_atas)
	    {
	    	if(actividad.getTipo().equalsIgnoreCase("G"))
				agregarGrupo(actividad);
	    	for(Actividad actividad_hija : actividad.getActividades_hijas())
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
	    
	    Container.removeViewAt(Container.getChildCount()-1);
	    
	    if(soloVista)
		{
	    	((Button) findViewById(R.id.actividad_aceptar)).setVisibility(View.GONE);
	    	((Button) findViewById(R.id.actividad_cancelar)).setVisibility(View.GONE);
		}
	    
	}
	
	public void agregarGrupo(Actividad actividad)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		contador = 0;
		
		TextView texto = new TextView(this);
		params.setMargins(15, 0, 0, 35);
		texto.setLayoutParams(params);
		SpannableString content = new SpannableString(actividad.getDescripcion());
		content.setSpan(new UnderlineSpan(), 0, actividad.getDescripcion().length(), 0);
		texto.setText(content);
		texto.setTextColor(getResources().getColor(R.color.tab_inactivated));
		texto.setGravity(Gravity.LEFT);
		texto.setTypeface(Typeface.DEFAULT_BOLD);
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
			LinearLayout pack = new LinearLayout(this);
			LinearLayout pack2 = new LinearLayout(this);
			texto.setPadding(20, 0, 0, 0);
			texto.setLayoutParams(getParamsMitad());
			pack.setLayoutParams(getParamsMitad2());
			pack2.addView(texto);
			pack2.addView(getLineaHorizontalSinMargen());
			pack2.addView(pack);
			Container.addView(pack2);
		}
		else
		{
			Container.addView(texto);
		}
	}
	
	public void agregarCheckbox(final Actividad actividad_hija)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 4);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		params2.setMargins(15, 5, 15, 5);
		contador ++;
		
		LinearLayout layout = new LinearLayout(this);
		LinearLayout innerContainer = new LinearLayout(this);
		layout.setGravity(Gravity.CENTER);
		TextView texto = new TextView(this);
		CheckBox check = new CheckBox(this);
		check.setGravity(Gravity.LEFT);
		
		texto.setLayoutParams(params);
		check.setLayoutParams(params2);
		check.setButtonDrawable(R.drawable.custom_checkbox);
		check.setMinWidth(100);
		check.setPadding(30, 0, 0, 0);
		
		texto.setText(actividad_hija.getDescripcion());
		texto.setTypeface(Typeface.DEFAULT_BOLD);
		AgendaTareaActividades act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && act.getResultado().equalsIgnoreCase("true"))
				check.setChecked(true);
		}
		else
		{
			act = initActividad(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;
			
		check.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				resp.setResultado(isChecked + "");
				AgendaTareaActividades.update(getDataBase(), resp);
			}});
		
		//se coloca validacion para que la actividad no sea modificable
		if(soloVista)
		{
		    	check.setEnabled(false);
		}
		
		layout.addView(texto);
		innerContainer = getNumero();
		innerContainer.addView(layout);
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
			LinearLayout pack = new LinearLayout(this);
			pack.addView(innerContainer);
			innerContainer.setLayoutParams(getParamsMitad());
			check.setLayoutParams(getParamsMitad2());
			pack.addView(getLineaHorizontalSinMargen());
			pack.addView(check);
			Container.addView(pack);
			Container.addView(getLineaSinMargen());
		}
		else
		{
			layout.addView(check);
			Container.addView(innerContainer);
			Container.addView(getLinea());
		}
	}
	
	public void agregarTexto(final Actividad actividad_hija)
	{		
		LinearLayout layout = new LinearLayout(this);
		TextView texto = new TextView(this);
		TextView textoResp = new TextView(this);
		LinearLayout innerContainer = new LinearLayout(this);
		contador ++;
		
		texto.setText(actividad_hija.getDescripcion());
		texto.setTypeface(Typeface.DEFAULT_BOLD);
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
			layout.setOrientation(LinearLayout.HORIZONTAL);
			
		}
		else
		{
			layout.setOrientation(LinearLayout.VERTICAL);
		}
		textoResp.setFocusable(false);
		textoResp.setFocusableInTouchMode(false);
		textoResp.setMaxLines(1);
		final int posicion = contador;
		AgendaTareaActividades act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase("null"))
				textoResp.setText(act.getResultado());
		}
		else
		{
			act = initActividad(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;
		
		layout.setClickable(true);
		layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), TextoActivity.class);
				intent.putExtra(RutasDetailFragment.ARG_ITEM_ID, (int)actividad_hija.getIdTareaActividad());
				intent.putExtra(RutasDetailFragment.ARG_AGENDA_ID, (long) resp.getIdAgenda());
				intent.putExtra(RutasDetailFragment.ARG_RUTA_ID, (int) resp.getIdRuta());
				intent.putExtra(ARG_PADRE_ID, (int) actividad_hija.getIdTareaActividadPadre());
				intent.putExtra(ARG_TAREA, (int) actividad_hija.getIdTarea());
				intent.putExtra(ARG_THEME, R.style.AppDialogTheme);
				intent.putExtra(ARG_NUMERO, posicion);
				startActivity(intent);
			}});
		
		//se coloca validacion para que la actividad no sea modificable
		if(soloVista)
		{
			layout.setEnabled(false);
		}
		
		layout.addView(texto);
		innerContainer = getNumero();
		innerContainer.addView(layout);
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
			LinearLayout pack = new LinearLayout(this);
			pack.addView(innerContainer);
			innerContainer.setLayoutParams(getParamsMitad());
			textoResp.setLayoutParams(getParamsMitad2());
			pack.addView(getLineaHorizontalSinMargen());
			pack.addView(textoResp);
			Container.addView(pack);
			Container.addView(getLineaSinMargen());
		}
		else
		{
			layout.addView(textoResp);
			Container.addView(innerContainer);
			Container.addView(getLinea());
		}
	}
	
	public void agregarSeleccion(final Actividad actividad_hija)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout layout = new LinearLayout(this);
		TextView texto = new TextView(this);
		Spinner combo = new Spinner(this);
		LinearLayout innerContainer = new LinearLayout(this);
		contador ++;
		
		texto.setPadding(0, 0, 0, 15);
		layout.setLayoutParams(params);
		combo.setLayoutParams(params);

		texto.setText(actividad_hija.getDescripcion());
		texto.setTypeface(Typeface.DEFAULT_BOLD);
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
			layout.setOrientation(LinearLayout.HORIZONTAL);
			
		}
		else
		{
			layout.setOrientation(LinearLayout.VERTICAL);
		}
		
		List<AgendaTareaOpciones> ag_opcs = AgendaTareaOpciones.getOpciones(getDataBase(), actividad_hija.getIdTarea(), actividad_hija.getIdTareaActividad());
		List<String> opciones = new ArrayList<String>();
		
		for(AgendaTareaOpciones opcion: ag_opcs)
		{
			opciones.add(opcion.getDescripcion());
		}
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,opciones);
		combo.setAdapter(adapter);
		
		AgendaTareaActividades act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			combo.setSelection(adapter.getPosition(act.getResultado()));
		}
		else
		{
			act = initActividad(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;
		
		combo.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				resp.setResultado(adapter.getItem(position));
				AgendaTareaActividades.update(getDataBase(), resp);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}});
		
		//se coloca validacion para que la actividad no sea modificable
		if(soloVista)
		{
			combo.setEnabled(false);
		}
		
		layout.addView(texto);
		innerContainer = getNumero();
		innerContainer.addView(layout);
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
			LinearLayout pack = new LinearLayout(this);
			pack.addView(innerContainer);
			innerContainer.setLayoutParams(getParamsMitad());
			combo.setLayoutParams(getParamsMitad2());
			pack.addView(getLineaHorizontalSinMargen());
			pack.addView(combo);
			Container.addView(pack);
			Container.addView(getLineaSinMargen());
		}
		else
		{
			layout.addView(combo);
			Container.addView(innerContainer);
			Container.addView(getLinea());
		}
	}
	
	public void agregarMultiple(final Actividad actividad_hija)
	{
		LinearLayout layout = new LinearLayout(this);
		LinearLayout innerContainer = new LinearLayout(this);
		TextView texto = new TextView(this);
		TextView textoResp = new TextView(this);
		contador ++;
		
		texto.setText(actividad_hija.getDescripcion());
		texto.setTypeface(Typeface.DEFAULT_BOLD);
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
			layout.setOrientation(LinearLayout.HORIZONTAL);
			
		}
		else
		{
			layout.setOrientation(LinearLayout.VERTICAL);
		}
		textoResp.setEnabled(false);
		textoResp.setMaxLines(1);
		final int posicion = contador;
		
		AgendaTareaActividades act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase("null"))
				textoResp.setText(act.getResultado());
		}
		else
		{
			act = initActividad(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;
		
		layout.setClickable(true);
		layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), MultipleActivity.class);
				intent.putExtra(RutasDetailFragment.ARG_ITEM_ID, (int)actividad_hija.getIdTareaActividad());
				intent.putExtra(RutasDetailFragment.ARG_AGENDA_ID, (long) resp.getIdAgenda());
				intent.putExtra(RutasDetailFragment.ARG_RUTA_ID, (int) resp.getIdRuta());
				intent.putExtra(ARG_PADRE_ID, (int) actividad_hija.getIdTareaActividadPadre());
				intent.putExtra(ARG_TAREA, (int) actividad_hija.getIdTarea());
				intent.putExtra(ARG_THEME, R.style.AppDialogTheme);
				intent.putExtra(ARG_NUMERO, posicion);
				startActivity(intent);
			}});
		
		//se coloca validacion para que la actividad no sea modificable
		if(soloVista)
		{
			layout.setEnabled(false);
		}
		
		layout.addView(texto);
		innerContainer = getNumero();
		innerContainer.addView(layout);
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
			LinearLayout pack = new LinearLayout(this);
			pack.addView(innerContainer);
			innerContainer.setLayoutParams(getParamsMitad());
			textoResp.setLayoutParams(getParamsMitad2());
			pack.addView(getLineaHorizontalSinMargen());
			pack.addView(textoResp);
			Container.addView(pack);
			Container.addView(getLineaSinMargen());
		}
		else
		{
			layout.addView(textoResp);
			Container.addView(innerContainer);
			Container.addView(getLinea());
		}
	}

	@Override
	public void aceptarCambios(View v) {
		AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), id_agenda, id_ruta, id_actividad);
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
	
	@SuppressLint("ResourceAsColor")
	public LinearLayout getLineaSinMargen()
	{
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		LinearLayout linea = new LinearLayout(this);
		linea.setLayoutParams(params3);
		
		linea.setBackgroundColor(R.color.bg_button_bg_main_pressed);
		return linea;
	}
	
	@SuppressLint("ResourceAsColor")
	public LinearLayout getLineaHorizontal()
	{
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(1,LayoutParams.MATCH_PARENT);
		params3.setMargins(5, 0, 5, 0);
		LinearLayout linea = new LinearLayout(this);
		linea.setLayoutParams(params3);
		
		linea.setBackgroundColor(R.color.bg_button_bg_main_pressed);
		return linea;
	}
	
	@SuppressLint("ResourceAsColor")
	public LinearLayout getLineaHorizontalSinMargen()
	{
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(1,LayoutParams.MATCH_PARENT);
		params3.setMargins(10, 0, 10, 0);
		LinearLayout linea = new LinearLayout(this);
		linea.setLayoutParams(params3);
		linea.setPadding(0, 40, 0, 40);
		
		linea.setBackgroundColor(R.color.bg_button_bg_main_pressed);
		return linea;
	}
	
	public LinearLayout getNumero()
	{
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params3.setMargins(5, 0, 15, 0);
		LinearLayout linea = new LinearLayout(this);
		LinearLayout container = new LinearLayout(this);
		container.setLayoutParams(params);
		TextView numero = new TextView(this);
		
		linea.setLayoutParams(params3);
		container.setOrientation(LinearLayout.HORIZONTAL);
		linea.setOrientation(LinearLayout.HORIZONTAL);
		numero.setText(contador + "");
		numero.setPadding(15, 0, 15, 0);
		numero.setTextColor(getResources().getColor(R.color.tab_activated));
		numero.setTypeface(Typeface.DEFAULT_BOLD);
		
		linea.addView(numero);
		linea.addView(getLineaHorizontal());
		container.addView(linea);
		return container;
	}
	
	public LinearLayout.LayoutParams getParamsMitad2()
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		params.setMargins(0, 20, 0, 30);
		return params;
	}
	
	public LinearLayout.LayoutParams getParamsMitad()
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);
		params.setMargins(0, 20, 0, 20);
		return params;
	}
	
	@Override
	protected void onResume() {
		Container.removeAllViews();
		List<Actividad> list_atas = Actividad.getActividadesGrupalesByTarea(getDataBase(), id_actividad);
	    for(Actividad actividad : list_atas)
	    {
	    	if(actividad.getTipo().equalsIgnoreCase("G"))
				agregarGrupo(actividad);
	    	for(Actividad actividad_hija : actividad.getActividades_hijas())
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
	    if(soloVista)
		{
	    	((Button) findViewById(R.id.actividad_aceptar)).setVisibility(View.GONE);
	    	((Button) findViewById(R.id.actividad_cancelar)).setVisibility(View.GONE);
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
