package rp3.marketforce.oportunidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientListAdapter;
import rp3.marketforce.headerlistview.HeaderListView;
import rp3.marketforce.loader.ClientLoader;
import rp3.marketforce.loader.OportunidadLoader;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.ruta.CrearVisitaActivity;
import rp3.marketforce.ruta.CrearVisitaFragment;
import rp3.marketforce.sync.SyncAdapter;
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
    private ListView list;
    private List<Oportunidad> lista;
    private OportunidadListAdapter adapter;
    private LoaderOportunidad loaderOportunidad;
    private SwipeRefreshLayout pullRefresher;
    public boolean filtro = false;
    private NumberFormat numberFormat;

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
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        super.setContentView(R.layout.fragment_oportunidad_list);
    }

    @Override
    public void onResume() {
        super.onResume();
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

        list = (ListView) rootView.findViewById(R.id.oportunidad_list);
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

        if (lista == null) {
            list.setVisibility(View.GONE);
            getRootView().findViewById(R.id.oportunidad_empty).setVisibility(View.VISIBLE);
            return;
        }

        if (lista.size() == 0) {
            list.setVisibility(View.GONE);
            getRootView().findViewById(R.id.oportunidad_empty).setVisibility(View.VISIBLE);
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
        ((TextView) getRootView().findViewById(R.id.oportunidad_numero)).setText("Oportunidades: " + lista.size());
        double monto = 0;
        for(Oportunidad op : lista)
        {
            monto = monto + op.getImporte();
        }
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

        adapter = new OportunidadListAdapter(this.getActivity(), lista, oportunidadListFragmentCallback);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {

                oportunidadListFragmentCallback.onOportunidadSelected(lista.get(position));
            }
        });
        adapter.notifyDataSetChanged();
        if (oportunidadListFragmentCallback.allowSelectedItem() && lista.size() != 0)
            oportunidadListFragmentCallback.onOportunidadSelected(lista.get(0));

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
            lista = data;
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
        lista = Oportunidad.getOportunidadesFiltro(getDataBase(), intent);
        OrderBy();
    }
}
