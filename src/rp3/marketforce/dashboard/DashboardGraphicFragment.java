package rp3.marketforce.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.ValueDependentColor;

import rp3.app.BaseFragment;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.util.Convert;

public class DashboardGraphicFragment extends BaseFragment {
	
	public static int NUM_VERTICAL_LABELS = 6;

	public static DashboardGraphicFragment newInstance() {
		DashboardGraphicFragment fragment = new DashboardGraphicFragment();
		return fragment;
	}

	private ViewPager PagerDetalles;
	private DetailsPageAdapter pagerAdapter;
	private PagerTabStrip tabStrip;
	private List<String> titles;
	private List<Integer> visitas;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
//		setContentView(R.layout.fragment_client,R.menu.fragment_client);
		setContentView(R.layout.fragment_dashboard_graphic);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
	}
	
	@Override
	public void onResume() {
		super.onResume();
		titles = new ArrayList<String>();
    	visitas = new ArrayList<Integer>();
    	PagerDetalles = (ViewPager) getRootView().findViewById(R.id.dashboard_graphics_pager);
    	pagerAdapter = new DetailsPageAdapter();
    	pagerAdapter.addView(createGraphics(false));
    	pagerAdapter.addView(createGraphics(true));
    	pagerAdapter.setTitles(titles.toArray(new String[]{}));
    	PagerDetalles.setAdapter(pagerAdapter);	    	   	
    	PagerDetalles.setCurrentItem(0);
	}
	
	@Override
	public void onStart() {		
		super.onStart();
			
	}
	
	 public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
	    	super.onFragmentCreateView(rootView, savedInstanceState);
	    	
	 }
	 
	 @SuppressLint("SimpleDateFormat")
	private FrameLayout createGraphics(boolean resumen)
	 {
		int mayor = 0;
	 	int tope = 0;
	 	long time_inicio = 0;
	 	long time_fin = 0;
	 	FrameLayout parent = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_dashboard_graphic_page, null);
	 	
	 	GraphViewSeries.GraphViewSeriesStyle seriesStyle = new GraphViewSeries.GraphViewSeriesStyle();
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
	 	
	 	if(resumen)
	 	{
	 		time_inicio = Agenda.getFirstAgenda(getDataBase());
	 		time_fin = Agenda.getLastAgenda(getDataBase());
	 	}
	 	else
	 	{
		 	Calendar inicio = Calendar.getInstance();
		 	Calendar fin = Calendar.getInstance();
		 	
		 	inicio.set(Calendar.HOUR_OF_DAY, 0);
		 	inicio.set(Calendar.MINUTE, 0);
		 	inicio.set(Calendar.SECOND, 0);
		 	
		 	fin.set(Calendar.HOUR_OF_DAY, 23);
		 	fin.set(Calendar.MINUTE, 59);
		 	fin.set(Calendar.SECOND, 59);
		 	
		 	time_inicio = inicio.getTimeInMillis();
		 	time_fin = fin.getTimeInMillis();
	 	}
	 	
	 	
	 	int visitados = Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_VISITADO, time_inicio, time_fin);
	 	int no_visitado = Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_NO_VISITADO, time_inicio, time_fin);
	 	int pendientes = Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_PENDIENTE, time_inicio, time_fin);
	 	int gestionados = Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_GESTIONANDO, time_inicio, time_fin);
	 	int reprogramados = Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_REPROGRAMADO, time_inicio, time_fin);
	 	pendientes = gestionados + reprogramados + pendientes;
	 	
	 	GraphViewSeries seriesHoy = new GraphViewSeries("Today", seriesStyle, new GraphView.GraphViewData[] {
	 	    new GraphView.GraphViewData(1, visitados)
	 	    , new GraphView.GraphViewData(2, no_visitado)
	 	    , new GraphView.GraphViewData(3, pendientes)
	 	});
	 	((TextView) parent.findViewById(R.id.grupo_total_visitas)).setText((visitados + no_visitado + pendientes) + " Visitas");
	 	
			mayor = Math.max(visitados, no_visitado);
			mayor = Math.max(pendientes, mayor);
			tope = getTope(mayor);
			int efectividad = 0;
			
			if(visitados != 0)
			{
				double total = (visitados + pendientes + no_visitado);
				double coef = visitados / total;
				efectividad = (int) (coef * 100);
			}
			((TextView) parent.findViewById(R.id.dashboard_porcentaje_hoy)).setText("" + (efectividad) + "%");
			SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
			((TextView) parent.findViewById(R.id.dashboard_hora_hoy)).setText(format2.format(Calendar.getInstance().getTime()));
			
	 	BarGraphView interim = new BarGraphView(getActivity(), "");
	 	if(resumen)
	 	{
	 		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM");
	 		((TextView) parent.findViewById(R.id.title_pager)).setText((String.format(getString(R.string.label_desde_graph), format1.format(Convert.getDateFromTicks(time_inicio)))));
	 	}
	 	else
	 	{
	 		((TextView) parent.findViewById(R.id.title_pager)).setText(R.string.label_hoy);
	 	}
	 	interim.setHorizontalLabels(new String[]{ getString(R.string.abreviatura_visitado), getString(R.string.abreviatura_no_visitado), getString(R.string.abreviatura_pendiente)});
	 	interim.setManualYAxisBounds(tope, 0);
	 	interim.setVerticalLabels(getVerticalLabels(tope));
	 	interim.getGraphViewStyle().setVerticalLabelsAlign(Align.RIGHT);
	 	interim.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.text_small_size));
	 	interim.addSeries(seriesHoy);
	 	interim.setDrawValuesOnTop(true);
	 	interim.setValuesOnTopColor(getContext().getResources().getColor(R.color.color_text_sky_blue));
	 	((LinearLayout)parent.findViewById(R.id.dashboard_graphic_hoy)).addView(interim);
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
	 
	 public class PagerTabStripBugfix extends PagerTabStrip {

		    public PagerTabStripBugfix(Context context) {
				super(context);
				// TODO Auto-generated constructor stub
			}

			@Override
		    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		        super.onMeasure(
		                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), 
		                heightMeasureSpec);
		    }    


		}
}


