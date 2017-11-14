package rp3.marketforce.oportunidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientListAdapter;
import rp3.marketforce.headerlistview.HeaderListView;
import rp3.marketforce.loader.ClientLoader;
import rp3.marketforce.loader.OportunidadLoader;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.oportunidad.AgendaOportunidad;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadTipo;
import rp3.marketforce.ruta.CrearVisitaActivity;
import rp3.marketforce.ruta.CrearVisitaFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.CalendarUtils;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadListFragment extends BaseFragment {

    public static final String ARG_TRANSACTIONTYPEID = "transactionType";
    public static final String ARG_TRANSACTIONTYPEBO = "transactionTypeBo";
    private OportunidadListFragmentListener oportunidadListFragmentCallback;
    private boolean currentTransactionBoolean;
    private String currentTransactionSearch;
    private ExpandableListView list;
    private List<String> tipos;
    private HashMap<String, List<Oportunidad>> lista;
    private OportunidadListAdapter adapter;
    private LoaderOportunidad loaderOportunidad;
    private SwipeRefreshLayout pullRefresher;
    public boolean filtro = false;
    private NumberFormat numberFormat;
    private double monto = 0;

    public static OportunidadListFragment newInstance(boolean flag , String transactionTypeId) {
        OportunidadListFragment fragment = new OportunidadListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSACTIONTYPEID, transactionTypeId);
        args.putBoolean(ARG_TRANSACTIONTYPEBO, flag);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OportunidadListFragmentListener {
        public void onOportunidadSelected(Oportunidad oportunidad);
        public void onFinalizaConsulta();
        public void onFinalizaGestion();
        public boolean allowSelectedItem();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(getParentFragment()!=null){
            oportunidadListFragmentCallback = (OportunidadListFragmentListener)getParentFragment();
        }else{
            oportunidadListFragmentCallback = (OportunidadListFragmentListener) activity;
            setRetainInstance(true);
        }

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setMinimumFractionDigits(0);

        super.setContentView(R.layout.fragment_oportunidad_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy");
        if(!filtro) {
            if (currentTransactionBoolean) {
                ejecutarConsulta();
            } else {
                Bundle args = new Bundle();
                if (loaderOportunidad == null)
                    loaderOportunidad = new LoaderOportunidad();
                getLoaderManager().initLoader(0, args, loaderOportunidad);
            }
        }
        //muestro si existe un prospecto gestionando
        AgendaOportunidad agd = AgendaOportunidad.getAgendaOportunidadGestionado(getDataBase());
        if(agd.getID() != 0) {
            LinearLayout containerGestionando = (LinearLayout) getRootView().findViewById(R.id.oportunidad_gestionando);
            final Oportunidad opt = Oportunidad.getOportunidadId(getDataBase(), agd.get_idOportunidad());
            LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View convertView = (View) inflater.inflate(this.getContext().getResources().getLayout(R.layout.rowlist_oportunidad), null);

            ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_nombre)).setText(opt.getDescripcion());
            ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_probabilidad)).setText("Probabilidad: " + opt.getProbabilidad() + "%");
            if(opt.getAgente() != null)
                ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_agente)).setText(opt.getAgente().getNombre());
            else
                ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_agente)).setText("");
            ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_number)).setText("G");
            ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_importe)).setText("$" + numberFormat.format(opt.getImporte()));
            ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_contactado)).setText("Contactado: " + format1.format(opt.getFechaCreacion()));

            if (opt.getEstado().equalsIgnoreCase("S"))
                ((ImageView) convertView.findViewById(R.id.rowlist_oportunidad_prioridad)).setImageResource(R.drawable.blue_flag);
            if (opt.getEstado().equalsIgnoreCase("C"))
                ((ImageView) convertView.findViewById(R.id.rowlist_oportunidad_prioridad)).setImageResource(R.drawable.green_flag);
            if (opt.getEstado().equalsIgnoreCase("NC"))
                ((ImageView) convertView.findViewById(R.id.rowlist_oportunidad_prioridad)).setImageResource(R.drawable.gray_flag);
            Calendar cal = Calendar.getInstance();
            Calendar calFecha = Calendar.getInstance();
            calFecha.setTime(opt.getFechaCreacion());
            long dias = CalendarUtils.DayDiff(cal, calFecha);
            if (dias == 0 && cal.get(Calendar.DAY_OF_YEAR) != calFecha.get(Calendar.DAY_OF_YEAR))
                dias = 1;
            ((TextView) convertView.findViewById(R.id.rowlist_oportunidad_dias)).setText("Días transcurridos: " + dias);
            ((RatingBar) convertView.findViewById(R.id.rowlist_oportunidad_calificacion)).setRating(opt.getCalificacion());

            if (opt.getEtapa().getOrden() > 1)
                convertView.findViewById(R.id.rowlist_oportunidad_etapa1).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa1));
            if (opt.getEtapa().getOrden() > 2)
                convertView.findViewById(R.id.rowlist_oportunidad_etapa2).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa2));
            if (opt.getEtapa().getOrden() > 3)
                convertView.findViewById(R.id.rowlist_oportunidad_etapa3).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa3));
            if (opt.getEtapa().getOrden() > 4)
                convertView.findViewById(R.id.rowlist_oportunidad_etapa4).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa4));
            if (opt.getEstado().equalsIgnoreCase("C")) {
                convertView.findViewById(R.id.rowlist_oportunidad_etapa1).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa1));
                convertView.findViewById(R.id.rowlist_oportunidad_etapa2).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa2));
                convertView.findViewById(R.id.rowlist_oportunidad_etapa3).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa3));
                convertView.findViewById(R.id.rowlist_oportunidad_etapa4).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa4));
                convertView.findViewById(R.id.rowlist_oportunidad_etapa5).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa5));
            }

            if (opt.getMaxEtapas() < 5)
                convertView.findViewById(R.id.rowlist_oportunidad_etapa5).setVisibility(View.INVISIBLE);
            if (opt.getMaxEtapas() < 4)
                convertView.findViewById(R.id.rowlist_oportunidad_etapa4).setVisibility(View.INVISIBLE);
            if (opt.getMaxEtapas() < 3)
                convertView.findViewById(R.id.rowlist_oportunidad_etapa3).setVisibility(View.INVISIBLE);
            if (opt.getMaxEtapas() < 2)
                convertView.findViewById(R.id.rowlist_oportunidad_etapa2).setVisibility(View.INVISIBLE);

            containerGestionando.removeAllViews();
            containerGestionando.addView(convertView);
            getRootView().findViewById(R.id.oportunidad_gestionando_content).setVisibility(View.VISIBLE);
            containerGestionando.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oportunidadListFragmentCallback.onOportunidadSelected(opt);
                }
            });

        }
        else
            getRootView().findViewById(R.id.oportunidad_gestionando_content).setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState == null)
        {
            currentTransactionBoolean = getArguments().getBoolean(ARG_TRANSACTIONTYPEBO);
            currentTransactionSearch = getArguments().getString(ARG_TRANSACTIONTYPEID);
            loaderOportunidad = new LoaderOportunidad();



        }
    }

    public void ejecutarConsulta(){
        Bundle args = new Bundle();
        executeLoader(0, args, loaderOportunidad);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        list = (ExpandableListView) rootView.findViewById(R.id.oportunidad_list);
        pullRefresher = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

    }


    @Override
    public void onStart() {
        super.onStart();
        if(list!=null && list.getParent() == null){
            pullRefresher.setRefreshing(false);
            pullRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (ConnectionUtils.isNetAvailable(getContext())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_OPORTUNIDADES);
                        requestSync(bundle);
                    } else {
                        Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                        pullRefresher.setRefreshing(false);
                    }
                }
            });
            list.setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
            list.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                    if (firstVisibleItem == 0 && visibleItemCount != 0) {
                        pullRefresher.setEnabled(true);
                    } else {
                        pullRefresher.setEnabled(false);
                    }

                }
            });
            list.setAdapter(adapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void searchTransactions(String termSearch){
        Bundle args = new Bundle();
        args.putString(LoaderOportunidad.STRING_SEARCH, termSearch);
        getLoaderManager().restartLoader(0, args, loaderOportunidad);
    	//executeLoader(0, args, this);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SimpleDateFormat")
    private void OrderBy() {

        try {


            if (lista == null) {
                list.setVisibility(View.GONE);
                getRootView().findViewById(R.id.oportunidad_empty).setVisibility(View.VISIBLE);
                ((TextView) getRootView().findViewById(R.id.oportunidad_meta)).setText("Meta: $ 0");
                ((TextView) getRootView().findViewById(R.id.oportunidad_numero)).setText("Oportunidades: 0");
                return;
            }

            if (lista.size() == 0) {
                list.setVisibility(View.GONE);
                getRootView().findViewById(R.id.oportunidad_empty).setVisibility(View.VISIBLE);
                ((TextView) getRootView().findViewById(R.id.oportunidad_meta)).setText("Meta: $ 0");
                ((TextView) getRootView().findViewById(R.id.oportunidad_numero)).setText("Oportunidades: 0");
                return;
            }

            list.setVisibility(View.VISIBLE);
            getRootView().findViewById(R.id.oportunidad_empty).setVisibility(View.GONE);

            pullRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (ConnectionUtils.isNetAvailable(getContext())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_OPORTUNIDADES);
                        requestSync(bundle);
                    } else {
                        Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                        pullRefresher.setRefreshing(false);
                    }
                }
            });
            int optSize = 0;
            for(List<Oportunidad> optList : lista.values())
                optSize = optSize + optList.size();

            ((TextView) getRootView().findViewById(R.id.oportunidad_numero)).setText("Oportunidades: " + optSize);

            ((TextView) getRootView().findViewById(R.id.oportunidad_meta)).setText("Meta: $ " + numberFormat.format(monto));
            list.setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
            list.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                    if (firstVisibleItem == 0 && visibleItemCount != 0) {
                        pullRefresher.setEnabled(true);
                    } else {
                        pullRefresher.setEnabled(false);
                    }

                }
            });

            if(adapter == null) {
                adapter = new OportunidadListAdapter(this.getActivity(), lista, tipos, oportunidadListFragmentCallback);
                list.setAdapter(adapter);
            }
            else {
                if(list.getAdapter() == null) {
                    list.setAdapter(adapter);
                }
                adapter.setList(lista);
                adapter.notifyDataSetChanged();
            }

            list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    oportunidadListFragmentCallback.onOportunidadSelected(lista.get(tipos.get(groupPosition)).get(childPosition));
                    return false;
                }
            });

            if (oportunidadListFragmentCallback.allowSelectedItem() && lista.size() != 0)
                oportunidadListFragmentCallback.onOportunidadSelected(lista.get(0).get(0));
        }
        catch (Exception ex)
        {

        }

    }


    public class LoaderOportunidad implements LoaderManager.LoaderCallbacks<List<Oportunidad>> {

        public static final String STRING_SEARCH = "string_search";
        public static final String STRING_BOOLEAN = "string_boolean";
        private String Search;
        private boolean flag;

        @Override
        public Loader<List<Oportunidad>> onCreateLoader(int arg0,
                                                    Bundle bundle) {

            Search = bundle.getString(STRING_SEARCH);
            //flag = bundle.getBoolean(STRING_BOOLEAN);

            return new OportunidadLoader(getActivity(), getDataBase(), flag, Search);

        }

        @Override
        public void onLoadFinished(Loader<List<Oportunidad>> arg0,
                                   List<Oportunidad> data)
        {
            lista = toExpandableList(data);
            oportunidadListFragmentCallback.onFinalizaConsulta();
            OrderBy();
            if(adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Oportunidad>> arg0) {

        }
    }

    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);

        closeDialogProgress();
        pullRefresher.setRefreshing(false);
        pullRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectionUtils.isNetAvailable(getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_OPORTUNIDADES);
                    requestSync(bundle);
                } else {
                    Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                    pullRefresher.setRefreshing(false);
                }
            }
        });
        onResume();
    }

    public void aplicarFiltro(Intent intent)
    {
        filtro = true;
        lista = toExpandableList(Oportunidad.getOportunidadesFiltro(getDataBase(), intent));
        OrderBy();
    }

    public HashMap<String, List<Oportunidad>> toExpandableList(List<Oportunidad> lista)
    {
        tipos = new ArrayList<>();
        HashMap<String, List<Oportunidad>> listDataChild = new HashMap<String, List<Oportunidad>>();
        List<OportunidadTipo> oportunidadTipos = OportunidadTipo.getOportunidadTipoAll(getDataBase());

        for(OportunidadTipo optTipo : oportunidadTipos)
        {
            tipos.add(optTipo.getDescripcion());
            listDataChild.put(optTipo.getDescripcion(), new ArrayList<Oportunidad>());
        }

        monto = 0;
        for(Oportunidad opt : lista)
        {
            if(opt.getOportunidadTipo().getDescripcion() != null) {
                listDataChild.get(opt.getOportunidadTipo().getDescripcion()).add(opt);
                if (opt.getEstado().equalsIgnoreCase("A"))
                    monto = monto + opt.getImporte();
            }
            else
            {
                Log.e("Sin Oportunidad Tipo", opt.getDescripcion());
            }
        }

        return listDataChild;
    }
}
