package rp3.marketforce.actividades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.AgendaTareaOpciones;
import rp3.marketforce.ruta.RutasDetailFragment;
import rp3.marketforce.utils.NothingSelectedSpinnerAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

public class GrupoActivity extends ActividadActivity {
	private static final String TAG = GrupoActivity.class.getSimpleName();
	LinearLayout Container;
	int contador = 0;
    List<Spinner> combos;
    List<TextView> multiples;
    boolean sinGrupos;
	AgendaTareaActividades actFecha;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
		if(getResources().getString(R.string.is_tablet).equalsIgnoreCase("true"))
		{
            if(soloVista)
	    	    setContentView(R.layout.layout_grupo_actividad_land);
            else
                setContentView(R.layout.layout_grupo_actividad_land, R.menu.fragment_crear_cliente);
		}
		else
		{
            if(soloVista)
			    setContentView(R.layout.layout_grupo_actividad);
            else
                setContentView(R.layout.layout_grupo_actividad, R.menu.fragment_crear_cliente);
		}
	    
	    Container = (LinearLayout) findViewById(R.id.actividad_agrupar);
        combos = new ArrayList<Spinner>();
        multiples = new ArrayList<TextView>();
        sinGrupos = true;

        List<Actividad> list_atas = Actividad.getActividadesNoGrupalesByTarea(getDataBase(), id_actividad);
        for(Actividad actividad : list_atas)
        {
            if(actividad.getTipo().equalsIgnoreCase("C") || actividad.getTipo().equalsIgnoreCase("V"))
                agregarCheckbox(actividad);
            if(actividad.getTipo().equalsIgnoreCase("M"))
                agregarMultiple(actividad);
            if(actividad.getTipo().equalsIgnoreCase("S"))
                agregarSeleccion(actividad);
            if(actividad.getTipo().equalsIgnoreCase("T"))
                agregarTexto(actividad);
			if(actividad.getTipo().equalsIgnoreCase("F"))
				agregarFecha(actividad);
			if(actividad.getTipo().equalsIgnoreCase("N"))
				agregarNumerico(actividad);
        }

        sinGrupos = false;
	    list_atas = Actividad.getActividadesGrupalesByTarea(getDataBase(), id_actividad);
	    for(Actividad actividad : list_atas)
	    {
	    	if(actividad.getTipo().equalsIgnoreCase("G"))
				agregarGrupo(actividad);
	    	for(Actividad actividad_hija : actividad.getActividades_hijas())
	    	{
		    	if(actividad_hija.getTipo().equalsIgnoreCase("C") || actividad.getTipo().equalsIgnoreCase("V"))
		    		agregarCheckbox(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("M"))
					agregarMultiple(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("S"))
					agregarSeleccion(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("T"))
					agregarTexto(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("F"))
					agregarFecha(actividad);
				if(actividad_hija.getTipo().equalsIgnoreCase("N"))
					agregarNumerico(actividad);
	    	}
	    }

		if(Container != null)
	    	Container.removeViewAt(Container.getChildCount()-1);

	    if(soloVista)
		{
	    	((Button) findViewById(R.id.actividad_aceptar)).setVisibility(View.GONE);
	    	((Button) findViewById(R.id.actividad_cancelar)).setVisibility(View.GONE);
		}

	}
	
	public void agregarGrupo(Actividad actividad)
	{
		Log.d(TAG,"agregarGrupo...");
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
		Log.d(TAG,"agregarCheckBox...");
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
		AgendaTareaActividades act = null;
		if(id_agenda != 0)
			act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		else
			act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), id_agenda_int, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && act.getResultado().equalsIgnoreCase("true"))
				check.setChecked(true);
		}
		else
		{
			act = initActividadInsert(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;
			
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				resp.setResultado(isChecked + "");
				if (isChecked)
					resp.setIdsResultado("1");
				else
					resp.setIdsResultado("0");
				AgendaTareaActividades.update(getDataBase(), resp);
			}
		});
		
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
		Log.d(TAG,"agregarTexto...");
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
        textoResp.setHint("Escriba su respuesta");
		final int posicion = contador;
		AgendaTareaActividades act = null;
		if(id_agenda != 0)
			act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		else
			act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), id_agenda_int, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase("null"))
				textoResp.setText(act.getResultado());
		}
		else
		{
			act = initActividadInsert(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;
		
		layout.setClickable(true);
        final boolean actSinGrupos = sinGrupos;
        textoResp.setClickable(true);
        textoResp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), TextoActivity.class);
				intent.putExtra(RutasDetailFragment.ARG_ITEM_ID, (int) actividad_hija.getIdTareaActividad());
				intent.putExtra(RutasDetailFragment.ARG_AGENDA_ID, (long) resp.getIdAgenda());
				intent.putExtra(RutasDetailFragment.ARG_RUTA_ID, (int) resp.getIdRuta());
				intent.putExtra(ARG_AGENDA_INT, id_agenda_int);
				intent.putExtra(ARG_PADRE_ID, (int) actividad_hija.getIdTareaActividadPadre());
				intent.putExtra(ARG_TAREA, (int) actividad_hija.getIdTarea());
				intent.putExtra(ARG_THEME, R.style.AppDialogTheme);
				intent.putExtra(ARG_NUMERO, posicion);
				intent.putExtra(ARG_SIN_GRUPO, actSinGrupos);
				startActivity(intent);
			}
		});
		
		//se coloca validacion para que la actividad no sea modificable
		if(soloVista)
		{
            textoResp.setEnabled(false);
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

	public void agregarFecha(final Actividad actividad_hija)
	{
		Log.d(TAG,"agregarFecha...");
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
		textoResp.setHint("Ingrese una fecha");
		final int posicion = contador;
		AgendaTareaActividades act = null;
		if(id_agenda != 0)
			act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		else
			act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), id_agenda_int, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase("null"))
				textoResp.setText(act.getResultado());
		}
		else
		{
			act = initActividadInsert(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;

		layout.setClickable(true);
		final boolean actSinGrupos = sinGrupos;
		textoResp.setClickable(true);
		textoResp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				if(resp.getIdsResultado() != null && resp.getIdsResultado().length() > 0)
				{
					long time = Long.parseLong(resp.getIdsResultado());
					cal.setTimeInMillis(time);
				}
				actFecha = resp;
				showDialogDatePicker((int) resp.getID(), cal);

			}});

		//se coloca validacion para que la actividad no sea modificable
		if(soloVista)
		{
			textoResp.setEnabled(false);
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
	public void onDailogDatePickerChange(int id, Calendar c) {
		Log.d(TAG,"onDialogDatePickerChange...");
		super.onDailogDatePickerChange(id, c);
		SimpleDateFormat format2 = new SimpleDateFormat("dd");
		SimpleDateFormat format3 = new SimpleDateFormat("MMMM");
		SimpleDateFormat format5 = new SimpleDateFormat("yyyy");
		String mes = format3.format(c.getTime());
		mes = mes.substring(0,1).toUpperCase() + mes.substring(1);
		actFecha.setResultado(format2.format(c.getTime()) + " de " + mes + " de " + format5.format(c.getTime()));
		actFecha.setIdsResultado("" + c.getTimeInMillis());
		AgendaTareaActividades.update(getDataBase(), actFecha);
		onResume();
	}

	public void agregarNumerico(final Actividad actividad_hija)
	{
		Log.d(TAG,"agregarNumerico...");
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
		textoResp.setHint("Escriba su respuesta");
		final int posicion = contador;
		AgendaTareaActividades act = null;
		if(id_agenda != 0)
			act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		else
			act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), id_agenda_int, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase("null"))
				textoResp.setText(act.getResultado());
		}
		else
		{
			act = initActividadInsert(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;

		layout.setClickable(true);
		final boolean actSinGrupos = sinGrupos;
		textoResp.setClickable(true);
		textoResp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), NumericoActivity.class);
				intent.putExtra(RutasDetailFragment.ARG_ITEM_ID, (int)actividad_hija.getIdTareaActividad());
				intent.putExtra(RutasDetailFragment.ARG_AGENDA_ID, (long) resp.getIdAgenda());
				intent.putExtra(RutasDetailFragment.ARG_RUTA_ID, (int) resp.getIdRuta());
				intent.putExtra(ARG_PADRE_ID, (int) actividad_hija.getIdTareaActividadPadre());
				intent.putExtra(ARG_TAREA, (int) actividad_hija.getIdTarea());
				intent.putExtra(ARG_THEME, R.style.AppDialogTheme);
				intent.putExtra(ARG_NUMERO, posicion);
				intent.putExtra(ARG_SIN_GRUPO, actSinGrupos);
				startActivity(intent);
			}});

		//se coloca validacion para que la actividad no sea modificable
		if(soloVista)
		{
			textoResp.setEnabled(false);
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
		Log.d(TAG,"agregarSeleccion...");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout layout = new LinearLayout(this);
		TextView texto = new TextView(this);
		final Spinner combo = new Spinner(this);
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
		
		final List<AgendaTareaOpciones> ag_opcs = AgendaTareaOpciones.getOpciones(getDataBase(), actividad_hija.getIdTarea(), actividad_hija.getIdTareaActividad());
		List<String> opciones = new ArrayList<String>();

		for(AgendaTareaOpciones opcion: ag_opcs)
		{
			opciones.add(opcion.getDescripcion());
		}
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,opciones);
		combo.setAdapter(adapter);
        combo.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter,
                R.layout.spinner_empty_selected,
                this));
        combo.setPrompt("Seleccione una opción");
        combos.add(combo);

		AgendaTareaActividades act = null;
		if(id_agenda != 0)
			act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		else
			act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), id_agenda_int, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
            if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase(""))
			    combo.setSelection(adapter.getPosition(act.getResultado())+1);
		}
		else
		{
			act = initActividadInsert(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;
        final int pos = combos.size();
		
		combo.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
                if(position > 0)
                    resp.setResultado(adapter.getItem(position-1));
                if(resp.getResultado() != null && !resp.getResultado().equalsIgnoreCase("")) {
                    for (AgendaTareaOpciones opc : ag_opcs) {
                        if (resp.getResultado().equalsIgnoreCase(opc.getDescripcion()))
                            resp.setIdsResultado(opc.getOrden() + "");
                    }
                }
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
		Log.d(TAG,"agregarMultiple...");
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
		//textoResp.setEnabled(false);
		textoResp.setMaxLines(1);
        textoResp.setHint("Seleccione las opciones...");
        multiples.add(textoResp);
		final int posicion = contador;

		AgendaTareaActividades act = null;
		if(id_agenda != 0)
			act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad, actividad_hija.getIdTareaActividad());
		else
			act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), id_agenda_int, id_actividad, actividad_hija.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase("null"))
				textoResp.setText(act.getResultado());
		}
		else
		{
			act = initActividadInsert(actividad_hija.getIdTareaActividad());
		}
		final AgendaTareaActividades resp = act;

        //textoResp.setFocusable(false);
        //textoResp.setFocusableInTouchMode(false);
		layout.setClickable(true);
        final boolean actSinGrupos = sinGrupos;
        textoResp.setClickable(true);
        textoResp.setOnClickListener(new OnClickListener(){

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
                intent.putExtra(ARG_SIN_GRUPO, actSinGrupos);
                startActivity(intent);
            }});
		
		//se coloca validacion para que la actividad no sea modificable
		if(soloVista)
		{
            textoResp.setEnabled(false);
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
		Log.d(TAG,"aceptarCambios...");
        for(Spinner combo : combos)
        {
            if(combo.getSelectedItemPosition() == 0)
            {
                Toast.makeText(this, "Faltan preguntas de selección sin responder.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        for(TextView multiple : multiples)
        {
            if(multiple.getText().length() <= 0)
            {
                Toast.makeText(this, "Faltan preguntas de selección múltiple sin responder.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
		AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), id_agenda, id_ruta, id_actividad);
		agt.setEstadoTarea("R");
		AgendaTarea.update(getDataBase(), agt);
		finish();
		
	}
	
	@SuppressLint("ResourceAsColor")
	public LinearLayout getLinea()
	{
		Log.d(TAG,"getLinea...");
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
		Log.d(TAG,"getLineaSinMargen...");
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		LinearLayout linea = new LinearLayout(this);
		linea.setLayoutParams(params3);
		
		linea.setBackgroundColor(R.color.bg_button_bg_main_pressed);
		return linea;
	}
	
	@SuppressLint("ResourceAsColor")
	public LinearLayout getLineaHorizontal()
	{
		Log.d(TAG,"getLineaHorizontal...");
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
		Log.d(TAG,"getLineaHorizontalSinMargen...");
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
	public void onResume() {
		Log.d(TAG,"onResume...");
		Container.removeAllViews();
        combos = new ArrayList<Spinner>();
        multiples = new ArrayList<TextView>();
        contador = 0;
        sinGrupos = true;
        List<Actividad> list_atas = Actividad.getActividadesNoGrupalesByTarea(getDataBase(), id_actividad);
        for(Actividad actividad : list_atas)
        {
            if(actividad.getTipo().equalsIgnoreCase("C") || actividad.getTipo().equalsIgnoreCase("V"))
                agregarCheckbox(actividad);
            if(actividad.getTipo().equalsIgnoreCase("M"))
                agregarMultiple(actividad);
            if(actividad.getTipo().equalsIgnoreCase("S"))
                agregarSeleccion(actividad);
            if(actividad.getTipo().equalsIgnoreCase("T"))
                agregarTexto(actividad);
			if(actividad.getTipo().equalsIgnoreCase("F"))
				agregarFecha(actividad);
			if(actividad.getTipo().equalsIgnoreCase("N"))
				agregarNumerico(actividad);
        }

        sinGrupos = false;
		list_atas = Actividad.getActividadesGrupalesByTarea(getDataBase(), id_actividad);
	    for(Actividad actividad : list_atas)
	    {
	    	if(actividad.getTipo().equalsIgnoreCase("G"))
				agregarGrupo(actividad);
	    	for(Actividad actividad_hija : actividad.getActividades_hijas())
	    	{
		    	if(actividad_hija.getTipo().equalsIgnoreCase("C") || actividad.getTipo().equalsIgnoreCase("V"))
		    		agregarCheckbox(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("M"))
					agregarMultiple(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("S"))
					agregarSeleccion(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("T"))
					agregarTexto(actividad_hija);
				if(actividad_hija.getTipo().equalsIgnoreCase("F"))
					agregarFecha(actividad);
				if(actividad_hija.getTipo().equalsIgnoreCase("N"))
					agregarNumerico(actividad);
	    	}
	    }
        ((Button) findViewById(R.id.actividad_aceptar)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.actividad_cancelar)).setVisibility(View.GONE);
		super.onResume();
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
