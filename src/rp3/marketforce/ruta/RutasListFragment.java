package rp3.marketforce.ruta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.data.MessageCollection;
import rp3.marketforce.R;
import rp3.marketforce.headerlistview.HeaderListView;
import rp3.marketforce.loader.RutasLoader;
import rp3.marketforce.models.Agenda;
import rp3.util.DateTime;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class RutasListFragment extends rp3.app.BaseFragment {
		    
    private TransactionListFragmentListener transactionListFragmentCallback;
    private LinearLayout linearLayout_rootParent;
    private HeaderListView headerlist;
    private LoaderRutas loaderRutas;
    private List<Agenda> list_agenda;
    private ArrayList<ArrayList<Agenda>> arrayAgenda;
    private ArrayList<String> header;
    private int day_week = -1;
    private int day_month = -1;
    private int month = -1;
    private Calendar calendar;
    private HorizontalScrollView horizontalListView;
    private LinearLayout linearLayout_horizontal;
    LayoutInflater inflater;
    private int width;
    
    public static RutasListFragment newInstance() {
    	RutasListFragment fragment = new RutasListFragment();
		return fragment;
    }
    
    public interface TransactionListFragmentListener {
        public void onTransactionSelected(long id);

		boolean onCreateOptionsMenu(Menu menu);
    }

    public RutasListFragment() {
    }
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        if(getParentFragment()!=null){        	
        	transactionListFragmentCallback = (TransactionListFragmentListener)getParentFragment();
        }else{
        	transactionListFragmentCallback = (TransactionListFragmentListener) activity;
        }
    }   

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        
        if(savedInstanceState == null)
		     super.setContentView(R.layout.layout_headerlist_ruta_list);
        
        calendar = Calendar.getInstance();
        day_month =  calendar.get(Calendar.DAY_OF_MONTH);
        day_week  =  calendar.get(Calendar.DAY_OF_WEEK) - 1;
        month = calendar.get(Calendar.MONTH);
        
        if(day_week == 0)
           day_week = 7;
        
        loaderRutas = new  LoaderRutas();
        Bundle args = new Bundle();
        args.putString(LoaderRutas.STRING_SEARCH, "");
        args.putBoolean(LoaderRutas.STRING_BOOLEAN, true);
        getLoaderManager().initLoader(0, args, loaderRutas);
    }
    
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	
    	inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    	linearLayout_rootParent = (LinearLayout) rootView.findViewById(R.id.linearLayout_headerlist_ruta_list);
    	horizontalListView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);
    	linearLayout_horizontal = (LinearLayout) rootView.findViewById(R.id.linearLayout_horizontal);
    	
    	DisplayMetrics metrics = new DisplayMetrics();
    	getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	width = metrics.widthPixels;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {    	
    	super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onStart() {    	
    	super.onStart();    	    	
    }
    
    public void searchTransactions(String termSearch)
    {
    	
    	Bundle args = new Bundle();
		args.putString(LoaderRutas.STRING_SEARCH, termSearch);
		args.putBoolean(LoaderRutas.STRING_BOOLEAN, false);
	    getLoaderManager().restartLoader(0, args, loaderRutas);
    }      
    
    public void loadTransactions(int transactionType)
    {
    }
    
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
    }
    
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		closeDialogProgress();
		if(messages.hasErrorMessage()){
			showDialogMessage(messages);
		}
	}
	
	
	 public class LoaderRutas implements LoaderCallbacks<List<Agenda>>{
	    	
		    public static final String STRING_SEARCH = "string_search";
	    	public static final String STRING_BOOLEAN = "string_boolean";
	    	private String Search;
	    	private boolean flag;
		 
	    	@Override
			public Loader<List<Agenda>> onCreateLoader(int arg0,
					Bundle bundle) {	
	    		
	    		Search = bundle.getString(STRING_SEARCH);
	    		flag = bundle.getBoolean(STRING_BOOLEAN);

				return new RutasLoader(getActivity(), getDataBase(),flag,Search);
			}

			@Override
			public void onLoadFinished(Loader<List<Agenda>> arg0,
					List<Agenda> data)
			{			
				list_agenda = data;
				
				if(list_agenda!= null)
					if(list_agenda.size() > 0)
					{
						if(headerlist == null)
						{
							headerlist = new HeaderListView(getActivity());
							headerlist.getListView().setDivider(null);
							headerlist.getListView().setDividerHeight(0);
							headerlist.getListView().setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
							linearLayout_rootParent.addView(headerlist);
						}
						
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width*.14f) , LinearLayout.LayoutParams.WRAP_CONTENT);
						for(int x = 0 ; x < 10; x++)
						{
						  View view_ = inflater.inflate(R.layout.rowlist_date, null);
						  view_.setLayoutParams(params);
						  linearLayout_horizontal.addView(view_);
						}
						
						orderDate();
				    	headerlist.setAdapter(new RutasListAdapter(getActivity(),arrayAgenda,transactionListFragmentCallback,header));
					}
			}

			@Override
			public void onLoaderReset(Loader<List<Agenda>> arg0) {	
				
			}
		}
	 
    @SuppressLint("SimpleDateFormat")
	private void orderDate()
	 {
		 if(list_agenda != null)
			 if(list_agenda.size()> 0)
			 {
				 header = new ArrayList<String>();
				 arrayAgenda = new ArrayList<ArrayList<Agenda>>();
				 Calendar cal = Calendar.getInstance();
				 boolean flag = false;
				 
				 ArrayList<Agenda> anteriores = null; 
				 ArrayList<Agenda> hoy = null; 
				 ArrayList<Agenda> mañana = null; 
				 ArrayList<Agenda> estaSemana = null;
				 ArrayList<Agenda> proxima_semana = null;
				 ArrayList<Agenda> proximo = null;
				 
				 for(Agenda agd :list_agenda)
				 {
					long diff = DateTime.getDaysDiff(calendar.getTime(), agd.getFechaInicio());
					
					if(diff >= 0)
					{
						cal.setTime(agd.getFechaInicio());
						int day_w = cal.get(Calendar.DAY_OF_WEEK)-1;
						int day_m = cal.get(Calendar.DAY_OF_MONTH);
						int current_month = cal.get(Calendar.MONTH);
						
					if(day_w == 0)
					   day_w = 7;
					
					if(day_w == day_week && day_m == day_month && current_month == month)
					{
						if(hoy == null)
							 hoy = new ArrayList<Agenda>();
						
						hoy.add(agd);
					}else
					{
						 cal = Calendar.getInstance();
						 cal.add(Calendar.DAY_OF_YEAR, 1);
						 int day_week_aux = cal.get(Calendar.DAY_OF_WEEK)-1;
						 int day_mont_aux = cal.get(Calendar.DAY_OF_MONTH);
						 int current_month_aux = cal.get(Calendar.MONTH);
						 
						 if(day_week_aux == 0)
							 day_week_aux = 7;
						
						 if( day_w > day_week && day_mont_aux == day_m && current_month_aux == current_month)
						{
							if(mañana == null)
								mañana = new ArrayList<Agenda>();
							
							mañana.add(agd);
						}else
						{
							if(estaSemana == null)
								estaSemana = new ArrayList<Agenda>();
							
							estaSemana.add(agd);
							
//							flag = true;
//							
//							int lapso = 7 - (day_week +1);
//							int per = 2;
//							
//							for(int y = 0 ; y < lapso ; y++)
//							{
//							    cal = Calendar.getInstance();
//							    cal.add(Calendar.DAY_OF_YEAR, per++);
//								 day_week_aux = cal.get(Calendar.DAY_OF_WEEK)-1;
//								 day_mont_aux = cal.get(Calendar.DAY_OF_MONTH);
//								 current_month_aux = cal.get(Calendar.MONTH);
//								 
//								 if(day_week_aux == 0)
//									 day_week_aux = 7;
//								 
//								 if(day_w == day_week_aux && day_m == day_mont_aux && current_month == current_month_aux)
//									{
//										if(estaSemana == null)
//											estaSemana = new ArrayList<Agenda>();
//										
//										estaSemana.add(agd);
//										
//										flag = false;
//										break;
//									}
//							   }
//							
//							if(flag)
//							{
//							
//									flag = true;
//									
//									lapso =  (7 - day_week)+1;
//									
//									for(int y = 0 ; y < 7 ; y++)
//									{
//										
//									    cal = Calendar.getInstance();
//									    cal.add(Calendar.DAY_OF_YEAR, lapso++);
//										 day_week_aux = cal.get(Calendar.DAY_OF_WEEK)-1;
//										 day_mont_aux = cal.get(Calendar.DAY_OF_MONTH);
//										 current_month_aux = cal.get(Calendar.MONTH);
//										 
//										 if(day_week_aux == 0)
//											 day_week_aux = 7;
//										 
//										 if(day_w == day_week_aux && day_m == day_mont_aux && current_month == current_month_aux)
//											{
//												if(proxima_semana == null)
//													proxima_semana = new ArrayList<Agenda>();
//												
//												proxima_semana.add(agd);
//												
//												flag = false;
//												break;
//											}
//									}
//									
//									if(flag)
//									{
//										if(proximo == null)
//											proximo = new ArrayList<Agenda>();
//										
//										proximo.add(agd);
//									}
//							
//							}
						}
					}
					}else
					{
						if(anteriores == null)
							anteriores = new ArrayList<Agenda>();
						
						anteriores.add(agd);
					}
				 }
				 
//				 if(anteriores != null)
//				 {
//					 arrayAgenda.add(anteriores);
//					 header.add("Anteriores");
//				 }
				 
				 String date;
				 SimpleDateFormat format1 = new SimpleDateFormat("EEEE dd MMMM");
				 
				 if(hoy != null)
				 {
					 arrayAgenda.add(hoy);
					 
					 cal = Calendar.getInstance();
					 date = format1.format(cal.getTime());
					 header.add("Hoy, "+date);
				 }
				 
				 if(mañana != null)
				 {
					 arrayAgenda.add(mañana);
					 
					 cal = Calendar.getInstance();
					 cal.add(Calendar.DAY_OF_YEAR, 1);
					 date = format1.format(cal.getTime());
					 header.add("Mañana, "+date);
				 }
				 
				 if(estaSemana != null)
				 {
					arrayAgenda.add(estaSemana);
					header.add("");
				 }
				 
//				 if(proxima_semana != null)
//				 {
//					 arrayAgenda.add(proxima_semana);
//					 header.add("Próxima Semana");
//				 }
//				 
//				 if(proximo != null)
//				 {
//					 arrayAgenda.add(proximo);
//					 header.add("Próximo");
//				 }
				 
			 }
	 }
	}
