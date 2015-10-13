package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.marketforce.R;
import rp3.marketforce.loader.PedidoLoader;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoListFragment extends BaseFragment {

    public static final String ARG_TRANSACTIONTYPEID = "transactionType";
    public static final String ARG_TRANSACTIONTYPEBO = "transactionTypeBo";

    PedidoListFragmentListener permisoListFragmentCallback;
    private PedidoAdapter adapter;
    private boolean currentTransactionBoolean;
    private ListView headerList;
    public LinearLayout linearLayout_rootParent;
    public SwipeRefreshLayout refreshLayout;
    private String currentTransactionSearch;

    private LoaderPedidos loaderPedidos;
    private boolean isContacts = false;

    public static PedidoListFragment newInstance() {
        PedidoListFragment fragment = new PedidoListFragment();
        return fragment;
    }

    public interface PedidoListFragmentListener {
        public void onPermisoSelected(Pedido pedido);
        public void onFinalizaConsulta();
        public boolean allowSelectedItem();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(getParentFragment()!=null){
            permisoListFragmentCallback = (PedidoListFragmentListener)getParentFragment();
        }else{
            permisoListFragmentCallback = (PedidoListFragmentListener) activity;
            setRetainInstance(true);
        }

        super.setContentView(R.layout.fragment_pedido_list);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState == null)
        {
            Bundle args = new Bundle();
            args.putString(LoaderPedidos.STRING_SEARCH, currentTransactionSearch);
            loaderPedidos = new LoaderPedidos();
        }
    }

    public void ejecutarConsulta(){
        Bundle args = new Bundle();
        executeLoader(0, args, loaderPedidos);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        headerList = (ListView) rootView.findViewById(R.id.linearLayout_headerlist_ruta_list);
        headerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        refreshLayout.setRefreshing(false);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                if(ConnectionUtils.isNetAvailable(getContext())) {
                    //Bundle bundle = new Bundle();
                    //bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_JUSTIFICACIONES);
                    //requestSync(bundle);
                }
                else
                {
                    Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                    refreshLayout.setRefreshing(false);
                }
            }});

        if(adapter != null)
        {
            headerList.setAdapter(adapter);
            setListeners();
        }

    }

    public void setListeners()
    {
        headerList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0 && visibleItemCount != 0) {
                    refreshLayout.setEnabled(true);
                } else {
                    refreshLayout.setEnabled(false);
                }

            }
        });
        headerList.setAdapter(adapter);
        headerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {

                permisoListFragmentCallback.onPermisoSelected(adapter.getItem(position));

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if(headerList!=null && headerList.getParent() == null){
            if(refreshLayout == null)
                refreshLayout = new SwipeRefreshLayout(this.getContext());
            refreshLayout.setRefreshing(false);
            refreshLayout.addView(headerList);
            linearLayout_rootParent.removeView(refreshLayout);
            linearLayout_rootParent.addView(refreshLayout);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (ConnectionUtils.isNetAvailable(getContext())) {
                        //Bundle bundle = new Bundle();
                        //bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_JUSTIFICACIONES);
                        //requestSync(bundle);
                    } else {
                        Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                        refreshLayout.setRefreshing(false);
                    }
                }
            });
            headerList.setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
            setListeners();
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

    public class LoaderPedidos implements LoaderManager.LoaderCallbacks<List<Pedido>> {

        public static final String STRING_SEARCH = "string_search";
        private String Search;

        @Override
        public Loader<List<Pedido>> onCreateLoader(int arg0,
                                                          Bundle bundle) {

            Search = bundle.getString(STRING_SEARCH);
            return new PedidoLoader(getActivity(), getDataBase(), Search);

        }

        @Override
        public void onLoadFinished(Loader<List<Pedido>> arg0,
                                   List<Pedido> data) {

            headerList.setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
            headerList.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                    if (firstVisibleItem == 0 && visibleItemCount != 0) {
                        refreshLayout.setEnabled(true);
                    } else {
                        refreshLayout.setEnabled(false);
                    }

                }
            });


            adapter = new PedidoAdapter(getContext(), data);
            headerList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (permisoListFragmentCallback.allowSelectedItem())
                permisoListFragmentCallback.onPermisoSelected(data.get(0));
            permisoListFragmentCallback.onFinalizaConsulta();
            headerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @SuppressLint("ResourceAsColor")
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {

                    permisoListFragmentCallback.onPermisoSelected(adapter.getItem(position));

                }
            });
            if(data.size() == 0)
                getRootView().findViewById(R.id.list_pedidos_ninguno).setVisibility(View.VISIBLE);
            else
                getRootView().findViewById(R.id.list_pedidos_ninguno).setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<List<Pedido>> arg0) {

        }
    }

    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);

    }

    public void setList()
    {
        if (currentTransactionBoolean) {
            ejecutarConsulta();
        } else {
            Bundle args = new Bundle();
            if(loaderPedidos == null)
                loaderPedidos = new LoaderPedidos();
            getLoaderManager().initLoader(0, args, loaderPedidos);
        }
    }

}