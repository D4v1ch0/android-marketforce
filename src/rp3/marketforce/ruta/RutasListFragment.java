package rp3.marketforce.ruta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.data.MessageCollection;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.loader.RutasLoader;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import rp3.util.Convert;
import rp3.util.DateTime;
import rp3.util.Screen;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class RutasListFragment extends rp3.app.BaseFragment{
    private static final String TAG = RutasListFragment.class.getSimpleName();
		    
	public static String ARG_INICIO = "inicio";
	public static String ARG_FIN = "fin";
	
    private TransactionListFragmentListener transactionListFragmentCallback;
    private SwipeRefreshLayout pullRefresher;
    private View LoadingFooter;
    //private LinearLayout linearLayout_rootParent;
    private ListView headerlist;
    private LoaderRutas loaderRutas;
    private List<Agenda> list_agenda;
    private List<Agenda> list_agenda_in_adapter;
    private int day_week = -1;
    private int day_month = -1;
    private int month = -1;
    private Calendar calendar;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout_horizontal;
    LayoutInflater inflater;
    private int width;
    private String date;
    private SimpleDateFormat format1;
    private SimpleDateFormat format2;
    private SimpleDateFormat format3;
    private SimpleDateFormat format4;
    private RutasListAdapter adapter;
    private int cont;
    public static int SECTION = 0;
    private List<String> header_position;
    private int scrollV = 0;
    private View lastItem = null;
    private DataBase db;
    private String fromCarga;
    
    public static RutasListFragment newInstance() {
    	RutasListFragment fragment = new RutasListFragment();
		return fragment;
    }
    
    public interface TransactionListFragmentListener {
        public void onTransactionSelected(long id);

		boolean onCreateOptionsMenu(Menu menu);
		
		boolean allowSelectedItem();		
    }  
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        if(getParentFragment()!=null){        	
        	transactionListFragmentCallback = (TransactionListFragmentListener)getParentFragment();
        }else{
        	transactionListFragmentCallback = (TransactionListFragmentListener) activity;
        }
        
        super.setContentView(R.layout.layout_headerlist_ruta_list);
    }   

	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);		     

        db = this.getDataBase();
        calendar = Calendar.getInstance();
        day_month =  calendar.get(Calendar.DAY_OF_MONTH);
        day_week  =  calendar.get(Calendar.DAY_OF_WEEK);
        month = calendar.get(Calendar.MONTH);
        
        if(day_week == 0)
           day_week = 7;

		format1 = new SimpleDateFormat("EEEE dd MMMM yyyy");
		format2 = new SimpleDateFormat("EEEE");
		format3 = new SimpleDateFormat("dd");
		format4 = new SimpleDateFormat("MMMM");
		
		LoadingFooter = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_list_loading, null, false);
    }
    
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	
    	inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    	//linearLayout_rootParent = (LinearLayout) rootView.findViewById(R.id.linearLayout_headerlist_ruta_list);
    	headerlist = (ListView) rootView.findViewById(R.id.linearLayout_headerlist_ruta_list);
    	headerlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	pullRefresher = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
    	horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);
    	
    	pullRefresher.setRefreshing(false);
    	
    	pullRefresher.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
                if(ConnectionUtils.isNetAvailable(getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_AGENDAS);
                    requestSync(bundle);
                }
                else
                {
                    Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                    pullRefresher.setRefreshing(false);
                }
			}});
    	
    	linearLayout_horizontal = (LinearLayout) rootView.findViewById(R.id.linearLayout_horizontal);
    	
//    	if(headerlist!=null)
//    		paintDates();
    	DisplayMetrics metrics = new DisplayMetrics();
    	getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	
    	if(!transactionListFragmentCallback.allowSelectedItem()){
    		width = metrics.widthPixels;
    	}else{
    		width = (int) (metrics.widthPixels * 0.35);
    	}
    	
    	if(adapter != null)
    	{
    		headerlist.setAdapter(adapter);
    		setListenersList();
    		paintDates();
    	}
    	    	    	
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {    	
    	super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onStart() {    	
    	super.onStart();
        Log.d(TAG,"onStart...");
    }
    
    public void searchTransactions(String termSearch){    	
    	Bundle args = new Bundle();
		args.putString(LoaderRutas.STRING_SEARCH, termSearch);
		args.putBoolean(LoaderRutas.STRING_BOOLEAN, false);
	    executeLoader(0, args, loaderRutas);
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
        
        //pullRefresher.removeView(headerlist);
        
    }
    
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		closeDialogProgress();		
		pullRefresher.setRefreshing(false);
        String prueba = data.getString(SyncAdapter.ARG_SYNC_TYPE);
        if(data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && (data.getString(SyncAdapter.ARG_SYNC_TYPE).equalsIgnoreCase(SyncAdapter.SYNC_TYPE_UPLOAD_AGENDAS)
                || data.getString(SyncAdapter.ARG_SYNC_TYPE).equalsIgnoreCase(SyncAdapter.SYNC_TYPE_ACTUALIZAR_AGENDA))) {
            if (data.getString(SyncAdapter.ARG_SYNC_TYPE).equalsIgnoreCase(SyncAdapter.SYNC_TYPE_UPLOAD_AGENDAS)) {
                list_agenda = Agenda.getAgendaSemanal(getDataBase());
            } else {
                try {
                    if (headerlist.getFooterViewsCount() != 0)
                        headerlist.removeFooterView(LoadingFooter);
                } catch (Exception ex) {

                }
                if(fromCarga.equalsIgnoreCase(ARG_INICIO))
                    list_agenda = Agenda.getAgenda(getDataBase());
                else if(fromCarga.equalsIgnoreCase(ARG_FIN))
                    list_agenda = Agenda.getAgendaSemanal(getDataBase());
                else
                    list_agenda = Agenda.getAgendaSemanal(getDataBase());

                fromCarga = "";
            }
            orderDate();

            if (list_agenda_in_adapter != null) {
                if (adapter == null) {
                    adapter = new RutasListAdapter(getActivity(), list_agenda_in_adapter, transactionListFragmentCallback);
                    headerlist.setAdapter(adapter);
                }
                adapter.changeList(list_agenda_in_adapter);
            }
            paintDates();
        }
	}
	
	private void setListenersList()
	{
		headerlist.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
					    
					   if(header_position.contains(String.valueOf(firstVisibleItem)))
					   {
						   SECTION = header_position.indexOf(""+firstVisibleItem);
						   mPaintRiel();
					   }
					   
					   if(firstVisibleItem == 0 && visibleItemCount != 0)
					   {
						   pullRefresher.setEnabled(true);
					   }
					   else
					   {
						   pullRefresher.setEnabled(false);
					   }
				
			}
		});
    	
    	headerlist.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				
				if(adapter.isAction() && adapter.getItem(position).getNombreCompleto() != null)
				{
					
					transactionListFragmentCallback.onTransactionSelected(list_agenda_in_adapter.get(position).getID());
				}
				
				adapter.setAction(true);
				
			}
		});

    	headerlist.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(
					AdapterView<?> parent, View view,
					final int position, long id) {
				if(adapter.getItem(position).getNombreCompleto() != null && adapter.getItem(position).getCliente() != null)
				{
					adapter.setAction(false);
					PopupMenu popup = new PopupMenu(getContext(), view);
	                
	                popup.getMenuInflater()
	                    .inflate(R.menu.list_item_ruta_menu, popup.getMenu());
	                
	                if(adapter.getItem(position).getEstadoAgenda().equals(Contants.ESTADO_VISITADO) || 
	                		adapter.getItem(position).getEstadoAgenda().equals(Contants.ESTADO_GESTIONANDO))
	                	popup.getMenu().getItem(0).setEnabled(false);
	                
	                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
	                    public boolean onMenuItemClick(MenuItem item) {
	                        switch(item.getItemId())
	                        {
	                        	case R.id.item_menu_reprogramar_visita:
	                        		Intent intent = new Intent(getActivity(), ReprogramarActivity.class);
	                        		intent.putExtra(ReprogramarActivity.ARG_AGENDA, adapter.getItem(position).getID());
	                        		startActivity(intent);
	                        	break;
	                        	case R.id.item_menu_crear_visita:
	                        		Intent intent2 = new Intent(getActivity(), CrearVisitaActivity.class);
	                        		intent2.putExtra(CrearVisitaFragment.ARG_IDAGENDA, (int) adapter.getItem(position).getID());
	                        		intent2.putExtra(CrearVisitaFragment.ARG_FROM, "Agenda");
	                        		startActivity(intent2);
	                        	break;
	                        }
	                        return true;
	                    }
	                });
	                popup.show();
				}
						
				return false;
				
			}});
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
                if(getActivity() != null) {
                    list_agenda = data;

                    if (list_agenda != null)
                        if (list_agenda.size() > 0) {
                            if (headerlist == null) {
                                //headerlist = (HeaderListView) rootView.findViewById(R.id.linearLayout_headerlist_ruta_list);
                                headerlist.setDivider(null);
                                headerlist.setDividerHeight(0);
                                headerlist.setSelector(getActivity().getResources().getDrawable(R.drawable.bkg_rutas));
                                //linearLayout_rootParent.addView(headerlist);
                            }

                            orderDate();
                            adapter = new RutasListAdapter(getActivity(), list_agenda_in_adapter, transactionListFragmentCallback);
                            headerlist.setAdapter(adapter);

                            header_position = new ArrayList<String>();

                            cont = 0;
                            for (int m = 0; m < list_agenda_in_adapter.size(); m++) {
                                if (list_agenda_in_adapter.get(m).getNombreCompleto() == null) {
                                    cont++;
                                    header_position.add("" + m);
                                }

                            }
                            setListenersList();
                            paintDates();
                        }
                }
			}

			@Override
			public void onLoaderReset(Loader<List<Agenda>> arg0) {	
				
			}
		}
	 
	 @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
     void paintDates(){
		 	Calendar anterior = null;
	    	LinearLayout.LayoutParams params;
            int scrollTo;
	    	if(width > 480) {
                params = new LinearLayout.LayoutParams((int) (width * .14f) + 2, LinearLayout.LayoutParams.MATCH_PARENT);
                scrollTo = (int) (width*.14f);
            }
	    	else {
                params = new LinearLayout.LayoutParams((int) (width * .18f), LinearLayout.LayoutParams.MATCH_PARENT);
                scrollTo = (int) (width*.18f);
            }
	    	
			if(list_agenda_in_adapter != null) {
                linearLayout_horizontal.removeAllViews();

                try {

                    View view_ = inflater.inflate(R.layout.rowlist_date, null);
                    view_.setLayoutParams(params);

                    ((TextView) view_.findViewById(R.id.textView_week)).setText("");
                    ((TextView) view_.findViewById(R.id.textView_day)).setText("+");
                    ((TextView) view_.findViewById(R.id.textView_month)).setText("");

                    linearLayout_horizontal.addView(view_);

                    final View finalView_ = view_;
                    view_.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            fromCarga = ARG_INICIO;
                            finalView_.findViewById(R.id.textView_week).setVisibility(View.GONE);
                            finalView_.findViewById(R.id.textView_day).setVisibility(View.GONE);
                            finalView_.findViewById(R.id.textView_month).setVisibility(View.GONE);
                            finalView_.findViewById(R.id.date_progress).setVisibility(View.VISIBLE);

                            if (list_agenda_in_adapter == null || list_agenda_in_adapter.size() == 0) {
                                if (list_agenda.size() == 0) {
                                    long fin = Agenda.getFirstAgenda(getDataBase());
                                    Bundle bundle = new Bundle();
                                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ACTUALIZAR_AGENDA);
                                    bundle.putLong(ARG_FIN, fin);
                                    requestSync(bundle);
                                } else {
                                    pullRefresher.setRefreshing(false);
                                    try {
                                        headerlist.removeFooterView(LoadingFooter);
                                    } catch (Exception ex) {

                                    }
                                    orderDate();
                                    adapter.changeList(list_agenda_in_adapter);
                                    paintDates();
                                }
                            } else {
                                if (Convert.getTicksFromDate(list_agenda_in_adapter.get(0).getFechaInicio()) > Agenda.getFirstAgenda(getDataBase())) {
                                    pullRefresher.setRefreshing(false);
                                    try {
                                        headerlist.removeFooterView(LoadingFooter);
                                    } catch (Exception ex) {

                                    }
                                    list_agenda = Agenda.getAgenda(getDataBase());
                                    orderDate();
                                    adapter.changeList(list_agenda_in_adapter);
                                    paintDates();
                                } else {
                                    long fin = Agenda.getFirstAgenda(getDataBase());
                                    Bundle bundle = new Bundle();
                                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ACTUALIZAR_AGENDA);
                                    bundle.putLong(ARG_FIN, fin);
                                    requestSync(bundle);
                                }
                            }
                        }
                    });
                    //linearLayout_horizontal.setScrollX(scrollTo);
                    int x = 0;
                    for (Agenda fec : list_agenda_in_adapter) {
                        if (fec.getNombreCompleto() == null) {
                            try {
                                calendar.setTime(fec.getFechaInicio());
                                if(anterior != null)
                                {
                                    int dias = calendar.get(Calendar.DAY_OF_YEAR) - anterior.get(Calendar.DAY_OF_YEAR);
                                    for(int i = 0; i < dias - 1; i++)
                                    {
                                        anterior.add(Calendar.DAY_OF_YEAR, 1);
                                        view_ = inflater.inflate(R.layout.rowlist_date, null);
                                        view_.setLayoutParams(params);

                                        ((TextView) view_.findViewById(R.id.textView_week)).setText(format2.format(anterior.getTime()).substring(0, 3));
                                        ((TextView) view_.findViewById(R.id.textView_week)).setTextColor(getResources().getColor(R.color.color_unvisited));
                                        ((TextView) view_.findViewById(R.id.textView_day)).setText(format3.format(anterior.getTime()));
                                        ((TextView) view_.findViewById(R.id.textView_day)).setTextColor(getResources().getColor(R.color.color_unvisited));
                                        ((TextView) view_.findViewById(R.id.textView_month)).setText(format4.format(anterior.getTime()).substring(0, 3));
                                        ((TextView) view_.findViewById(R.id.textView_month)).setTextColor(getResources().getColor(R.color.color_unvisited));
                                        linearLayout_horizontal.addView(view_);
                                    }
                                }
                                anterior = (Calendar)calendar.clone();

                                view_ = inflater.inflate(R.layout.rowlist_date, null);
                                view_.setLayoutParams(params);

                                ((TextView) view_.findViewById(R.id.textView_week)).setText(format2.format(calendar.getTime()).substring(0, 3));
                                ((TextView) view_.findViewById(R.id.textView_day)).setText(format3.format(calendar.getTime()));
                                ((TextView) view_.findViewById(R.id.textView_month)).setText(format4.format(calendar.getTime()).substring(0, 3));

                                view_.setId(56 + x);
                                x++;
                                linearLayout_horizontal.addView(view_);

                                view_.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        cont = 0;
                                        SECTION = v.getId() - 56;

                                        if (adapter != null)
                                            adapter.notifyDataSetChanged();

                                        int x = 0;
                                        for (int m = 0; m < list_agenda_in_adapter.size(); m++)
                                            if (x <= SECTION && list_agenda_in_adapter.get(m).getNombreCompleto() == null) {
                                                cont = m;
                                                x++;
                                            }

                                        scrollV = cont;
                                        headerlist.setSelection(cont);

                                        mPaintRiel();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mPaintRiel();
                        }
                    }

                    view_ = inflater.inflate(R.layout.rowlist_date, null);
                    view_.setLayoutParams(params);

                    ((TextView) view_.findViewById(R.id.textView_week)).setText("");
                    ((TextView) view_.findViewById(R.id.textView_day)).setText("+");
                    ((TextView) view_.findViewById(R.id.textView_month)).setText("");

                    linearLayout_horizontal.addView(view_);

                    view_.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            fromCarga = ARG_FIN;
                            v.findViewById(R.id.textView_week).setVisibility(View.GONE);
                            v.findViewById(R.id.textView_day).setVisibility(View.GONE);
                            v.findViewById(R.id.textView_month).setVisibility(View.GONE);
                            v.findViewById(R.id.date_progress).setVisibility(View.VISIBLE);
                            long inicio = Agenda.getLastAgenda(getDataBase());
                            Bundle bundle = new Bundle();
                            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ACTUALIZAR_AGENDA);
                            bundle.putLong(ARG_INICIO, inicio);
                            requestSync(bundle);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPaintRiel();


            }
	 }
	 
    @SuppressLint("SimpleDateFormat")
	private void orderDate()
	 {
		 if(list_agenda != null)
			 if(list_agenda.size()> 0)
			 {
				 list_agenda_in_adapter = new ArrayList<Agenda>();
				 Calendar cal_ant = null;
				 Calendar cal = Calendar.getInstance();
				 for(Agenda agd : list_agenda)
				 {
					 cal.setTime(agd.getFechaInicio());
					 if(cal_ant == null || cal_ant.get(Calendar.DATE) != cal.get(Calendar.DATE))
					 {
						 Agenda new_agd = new Agenda();
						 new_agd.setFechaInicio(agd.getFechaInicio());
						 list_agenda_in_adapter.add(new_agd);
						 cal_ant = Calendar.getInstance();
						 cal_ant.setTime(agd.getFechaInicio());
					 }
					 list_agenda_in_adapter.add(agd);
				 }
			 }
	 }
    
    private void mPaintRiel()
    {
    	int bgColor = getActivity().getResources().getColor(R.color.bg_button_bg_main);
    	int x = 0;
    	for(int y = 0 ; y < list_agenda_in_adapter.size() ; y++)
		{
    		if(list_agenda_in_adapter.get(y).getNombreCompleto() == null)
    		{
	    		View st =  linearLayout_horizontal.findViewById((x+56));
	    		
	    		if(st != null)
	    		{
					if((st.getId()-56) == SECTION)
					{
						st.setBackgroundColor(bgColor);
						((TextView) st.findViewById(R.id.textView_week)).setTextColor(Color.WHITE);
						((TextView) st.findViewById(R.id.textView_day)).setTextColor(Color.WHITE);
						((TextView) st.findViewById(R.id.textView_month)).setTextColor(Color.WHITE);
						if(!isViewVisible(st))
							horizontalScrollView.scrollTo(st.getLeft(), 0);
					}
					else
					{
						if(st != null)
						{
							st.setBackgroundColor(Color.TRANSPARENT);
							((TextView) st.findViewById(R.id.textView_week)).setTextColor(Color.BLACK);
							((TextView) st.findViewById(R.id.textView_day)).setTextColor(Color.BLACK);
							((TextView) st.findViewById(R.id.textView_month)).setTextColor(Color.BLACK);
						}
					}
					x++;
	    		}
    		}
		}
    }
    
    private boolean isViewVisible(View view) {
    	
    	Rect scrollBounds = new Rect();
    	horizontalScrollView.getHitRect(scrollBounds);
    	if (view.getLocalVisibleRect(scrollBounds)) {
    	    // Any portion of the imageView, even a single pixel, is within the visible window
    		return true;
    	} else {
    	    // NONE of the imageView is within the visible window
    		return false;
    	}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
        Log.d(TAG,"onResume...");
        loaderRutas = new  LoaderRutas();
        Bundle args = new Bundle();
        args.putString(LoaderRutas.STRING_SEARCH, "");
        args.putBoolean(LoaderRutas.STRING_BOOLEAN, true);
        executeLoader(0, args, loaderRutas);
    }
    
    public void Refresh() {	  
    	Context ctx = this.getContext();
    	if(list_agenda_in_adapter == null || list_agenda_in_adapter.size() == 0)
    		list_agenda = Agenda.getAgenda(this.getDataBase());
		else
		{
			if(Convert.getTicksFromDate(list_agenda_in_adapter.get(0).getFechaInicio()) > Agenda.getFirstAgenda(this.getDataBase()))
				list_agenda = Agenda.getAgendaSemanal(this.getDataBase());
			else
				list_agenda = Agenda.getAgenda(this.getDataBase());
		}
		orderDate();
		adapter.changeList(list_agenda_in_adapter);
		paintDates();
    }

    /**
     *
     * Ciclo de vida
     *
     */


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
}
