package rp3.marketforce.resumen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.ValueDependentColor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import rp3.app.BaseFragment;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgenteResumen;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.util.Convert;

public class DashboardGrupoFragment extends BaseFragment {

	public static int NUM_VERTICAL_LABELS = 6;
	
	private List<String> titles;
	private List<Integer> visitas;
	private ViewPager PagerDetalles;
	private DetailsPageAdapter pagerAdapter;
	private PagerTabStrip tabStrip;
    private AgenteDetalleFragment agenteDetalleFragment;
    private boolean asc_pending = true, asc_unvisited = true, asc_visited = true;
	
	public static DashboardGrupoFragment newInstance(int i) {
		DashboardGrupoFragment fragment = new DashboardGrupoFragment();
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		getActivity().setTitle("Equipo");
		
		//setRetainInstance(true);				
		setContentView(R.layout.fragment_dashboard_grupo);
	}
	
	@Override
	public void onStart() {		
		super.onStart();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	titles = new ArrayList<String>();
    	visitas = new ArrayList<Integer>();
    	SimpleDateFormat format1 = new SimpleDateFormat("MMMM");
    	
    	PagerDetalles = (ViewPager) getRootView().findViewById(R.id.dashboard_grupo_pager);
    	tabStrip = (PagerTabStrip) getRootView().findViewById(R.id.pager_header);
    	tabStrip.setTabIndicatorColor(getResources().getColor(R.color.color_text_sky_blue));
    	pagerAdapter = new DetailsPageAdapter();
    	
    	Calendar inicio = Calendar.getInstance();
	 	Calendar fin = Calendar.getInstance();
	 	
	 	inicio.set(Calendar.HOUR_OF_DAY, 23);
	 	inicio.set(Calendar.MINUTE, 59);
	 	inicio.set(Calendar.SECOND, 59);
	 	inicio.add(Calendar.DATE, -1);
	 	
	 	fin.set(Calendar.HOUR_OF_DAY, 23);
	 	fin.set(Calendar.MINUTE, 59);
	 	fin.set(Calendar.SECOND, 59);

        String prueba = inicio.getTime().toString();
        String prueba2 = fin.getTime().toString();
	 	
	 	pagerAdapter.addView(createGraphics("Hoy", inicio.getTimeInMillis(), fin.getTimeInMillis()));
	 	
	 	inicio = Calendar.getInstance();

        inicio.set(Calendar.HOUR_OF_DAY, 23);
        inicio.set(Calendar.MINUTE, 59);
        inicio.set(Calendar.SECOND, 59);
	 	inicio.add(Calendar.DATE, - 7);
	 	
	 	pagerAdapter.addView(createGraphics("Esta semana", inicio.getTimeInMillis(), fin.getTimeInMillis()));
	 	
	 	inicio = Calendar.getInstance();

        inicio.set(Calendar.HOUR_OF_DAY, 23);
        inicio.set(Calendar.MINUTE, 59);
        inicio.set(Calendar.SECOND, 59);
	 	inicio.set(Calendar.DATE, 1);
        inicio.add(Calendar.DATE, -1);

        prueba = inicio.getTime().toString();
        prueba2 = fin.getTime().toString();
	 	
	 	pagerAdapter.addView(createGraphics("Este mes", inicio.getTimeInMillis(), fin.getTimeInMillis()));
	 	
	 	inicio = Calendar.getInstance();

        inicio.set(Calendar.HOUR_OF_DAY, 23);
        inicio.set(Calendar.MINUTE, 59);
        inicio.set(Calendar.SECOND, 59);
        inicio.set(Calendar.DATE, 1);
        inicio.add(Calendar.DATE, -1);
	 	inicio.add(Calendar.MONTH, -1);
	 	
	 	fin = Calendar.getInstance();
	 	
	 	fin.set(Calendar.HOUR_OF_DAY, 23);
	 	fin.set(Calendar.MINUTE, 59);
	 	fin.set(Calendar.SECOND, 59);
        fin.add(Calendar.MONTH, -1);
	 	fin.set(Calendar.DAY_OF_MONTH, fin.getActualMaximum(Calendar.DAY_OF_MONTH));

	 	
	 	String mes = format1.format(fin.getTime());
	 	pagerAdapter.addView(createGraphics(mes.substring(0, 1).toUpperCase() + mes.substring(1), inicio.getTimeInMillis(), fin.getTimeInMillis()));
	 	
	 	inicio = Calendar.getInstance();

        inicio.set(Calendar.HOUR_OF_DAY, 23);
        inicio.set(Calendar.MINUTE, 59);
        inicio.set(Calendar.SECOND, 59);
        inicio.set(Calendar.DATE, 1);
        inicio.add(Calendar.DATE, -1);
	 	inicio.add(Calendar.MONTH, -2);
	 	
	 	fin = Calendar.getInstance();
	 	
	 	fin.set(Calendar.HOUR_OF_DAY, 23);
	 	fin.set(Calendar.MINUTE, 59);
	 	fin.set(Calendar.SECOND, 59);
        fin.add(Calendar.MONTH, -2);
        fin.set(Calendar.DAY_OF_MONTH, fin.getActualMaximum(Calendar.DAY_OF_MONTH));

	 	
	 	mes = format1.format(fin.getTime());
	 	pagerAdapter.addView(createGraphics(mes.substring(0, 1).toUpperCase() + mes.substring(1), inicio.getTimeInMillis(), fin.getTimeInMillis()));
	 	
    	pagerAdapter.setTitles(titles.toArray(new String[]{}));
    	PagerDetalles.setAdapter(pagerAdapter);
    	PagerDetalles.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {	
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				((TextView)getRootView().findViewById(R.id.grupo_total_visitas)).setText(visitas.get(arg0) + " Visitas");
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {	
			}
		});    	
    	PagerDetalles.setCurrentItem(0);
	}
	
	private FrameLayout createGraphics(String title, long time_inicio, long time_fin)
	 {
		int mayor = 0;
	 	int tope = 0;
	 	List<AgenteResumen> list_resumen = AgenteResumen.getResumen(getDataBase(), time_inicio, time_fin);
	 	FrameLayout parent = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_grupo_tabla_grafico, null);
	 	
	 	GraphViewSeriesStyle seriesStyle = new GraphViewSeriesStyle();
	 	seriesStyle.setValueDependentColor(new ValueDependentColor() {
	 	  @Override
	 	  public int get(GraphViewDataInterface data) {
	 		  switch((int)data.getX())
	 		  {
		    		  case 1:  return getResources().getColor(R.color.color_visited);
		    		  case 2:  return getResources().getColor(R.color.color_unvisited);
		    		  case 3:  return getResources().getColor(R.color.color_pending);
	 		  }
	 		  return getResources().getColor(R.color.bg_button_bg_main);
	 	  }
	 	  
	 	});
	 	
	 	seriesStyle.thickness = 1;
	 	
	 	int visitados = 0;
	 	int no_visitado = 0;
	 	int pendientes = 0;
	 	for(int i = 0; i < list_resumen.size(); i ++)
	 	{
	 		visitados = visitados + list_resumen.get(i).getGestionados();
	 		no_visitado = no_visitado + list_resumen.get(i).getNoGestionados();
	 		pendientes = pendientes + list_resumen.get(i).getPendientes();
	 	}
	 	visitas.add(visitados + no_visitado + pendientes);

	 	GraphViewSeries seriesHoy = new GraphViewSeries("Today", seriesStyle, new GraphViewData[] {
	 	    new GraphViewData(1, visitados)
	 	    , new GraphViewData(2, no_visitado)
	 	    , new GraphViewData(3, pendientes)
	 	});

	 	mayor = Math.max(visitados, no_visitado);
		mayor = Math.max(pendientes, mayor);
		tope = getTope(mayor);
		
	 	BarGraphView interim = new BarGraphView(getActivity(), "");
	 	titles.add(title);
	 	interim.setHorizontalLabels(new String[]{ getString(R.string.abreviatura_visitado), getString(R.string.abreviatura_no_visitado), getString(R.string.abreviatura_pendiente)});
	 	interim.setManualYAxisBounds(tope, 0);
	 	interim.setVerticalLabels(getVerticalLabels(tope));
	 	interim.getGraphViewStyle().setVerticalLabelsAlign(Align.RIGHT);
	 	interim.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.text_small_size));
	 	interim.addSeries(seriesHoy);
	 	interim.setDrawValuesOnTop(true);
	 	interim.setValuesOnTopColor(getContext().getResources().getColor(R.color.color_text_sky_blue));
	 	((LinearLayout)parent.findViewById(R.id.dashboard_grupo_grafico)).addView(interim);
	 	
	 	final ResumenAdapter adapter = new ResumenAdapter(getContext(), list_resumen);
	 	((ListView)parent.findViewById(R.id.grupo_list_view)).setAdapter(adapter);
        parent.findViewById(R.id.header_pending).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 adapter.Sort(Contants.ESTADO_PENDIENTE, asc_pending);
                 asc_pending = !asc_pending;
             }
         });
         parent.findViewById(R.id.header_unvisited).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 adapter.Sort(Contants.ESTADO_NO_VISITADO, asc_unvisited);
                 asc_unvisited = !asc_unvisited;
             }
         });
         parent.findViewById(R.id.header_visited).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 adapter.Sort(Contants.ESTADO_VISITADO, asc_visited);
                 asc_visited = !asc_visited;
             }
         });
         parent.findViewById(R.id.grupo_agente_pendientes).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 agenteDetalleFragment = AgenteDetalleFragment.newInstance(0);
                 showDialogFragment(agenteDetalleFragment, "Agente", "Todos los Agentes");
             }
         });

         ((ListView)parent.findViewById(R.id.grupo_list_view)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 agenteDetalleFragment = AgenteDetalleFragment.newInstance(adapter.getItem(position).getIdAgente());
                 showDialogFragment(agenteDetalleFragment, "Agente", "Agente");
             }
         });
	 	return parent;
	 }

	private int getTope(int mayor) {
		if(mayor < NUM_VERTICAL_LABELS)
		{
			int res = mayor % (NUM_VERTICAL_LABELS - 1);
			if(res == 0)
				return mayor;
			else
				return mayor + ((NUM_VERTICAL_LABELS - 1) - res);
		}
		else
		{
			while(mayor % (NUM_VERTICAL_LABELS - 1) != 0)
				mayor++;
			return mayor;
		}
	 }

	 private String[] getVerticalLabels(int tope) {
		List<String> labels = new ArrayList<String>();
		
		if(tope < NUM_VERTICAL_LABELS)
		{
			for(int i = tope; i >= 0; i--)
				labels.add(""+i);
		}
		else
		{
			while(tope % (NUM_VERTICAL_LABELS - 1) != 0)
				tope++;
			int coeficiente = tope / (NUM_VERTICAL_LABELS - 1);
			for(int i = NUM_VERTICAL_LABELS - 1; i >= 0; i--)
			{
				labels.add(""+(i*coeficiente));
			}
		}
		return labels.toArray(new String[]{});
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(agenteDetalleFragment != null)
            agenteDetalleFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
