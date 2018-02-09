package rp3.auna.oportunidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.auna.R;
import rp3.auna.loader.OportunidadLoader;
import rp3.auna.models.oportunidad.Oportunidad;
import rp3.auna.models.oportunidad.OportunidadTipo;
import rp3.auna.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadListFragment extends BaseFragment {

    private static final String TAG = OportunidadListFragment.class.getSimpleName();
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
        public boolean allowSelectedItem();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach...");
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
        Log.d(TAG,"onResume...");
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
        Log.d(TAG,"onCreate...");
        if(savedInstanceState == null)
        {
            currentTransactionBoolean = getArguments().getBoolean(ARG_TRANSACTIONTYPEBO);
            currentTransactionSearch = getArguments().getString(ARG_TRANSACTIONTYPEID);
            loaderOportunidad = new LoaderOportunidad();



        }
    }

    public void ejecutarConsulta(){
        Log.d(TAG,"ejecutarConsulta...");
        Bundle args = new Bundle();
        executeLoader(0, args, loaderOportunidad);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        list = (ExpandableListView) rootView.findViewById(R.id.oportunidad_list);
        pullRefresher = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
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
        Log.d(TAG,"onSaveInstanceState...");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach...");
    }


    public void searchTransactions(String termSearch){
        Log.d(TAG,"searchTransactions...");
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
        Log.d(TAG,"OrderBy...");
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
        Log.d(TAG,"onSyncComplete...");
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
        Log.d(TAG,"aplicarFiltro...");
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
            listDataChild.get(opt.getOportunidadTipo().getDescripcion()).add(opt);
            if(opt.getEstado().equalsIgnoreCase("A"))
                monto = monto + opt.getImporte();
        }

        return listDataChild;
    }

    /**
     *
     * Ciclo de vida
     *
     */

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

}
