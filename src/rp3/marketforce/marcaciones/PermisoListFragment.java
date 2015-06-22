package rp3.marketforce.marcaciones;

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
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientListAdapter;
import rp3.marketforce.headerlistview.HeaderListView;
import rp3.marketforce.loader.ClientLoader;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.marcacion.Permiso;
import rp3.marketforce.ruta.CrearVisitaActivity;
import rp3.marketforce.ruta.CrearVisitaFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 19/06/2015.
 */
public class PermisoListFragment extends BaseFragment {

    public static final String ARG_TRANSACTIONTYPEID = "transactionType";
    public static final String ARG_TRANSACTIONTYPEBO = "transactionTypeBo";

    PermisoListFragmentListener permisoListFragmentCallback;
    private ClientListAdapter adapter;
    private boolean currentTransactionBoolean;
    private String currentTransactionSearch;
    private HeaderListView headerList;
    public static List<String> headersortList;
    private static int id_select;
    public static int itemList_click_section = -1;
    public static int itemList_click_row = -1;
    public static int itemClientID = -1;
    public LinearLayout linearLayout_rootParent;
    public SwipeRefreshLayout refreshLayout;

    private LoaderCliente loaderCliente;
    private boolean isContacts = false;

    public static PermisoListFragment newInstance(boolean flag , String transactionTypeId) {
        PermisoListFragment fragment = new PermisoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSACTIONTYPEID, transactionTypeId);
        args.putBoolean(ARG_TRANSACTIONTYPEBO, flag);
        fragment.setArguments(args);
        return fragment;
    }

    public interface PermisoListFragmentListener {
        public void onPermisoSelected(Permiso permiso);
        public void onFinalizaConsulta();
        public boolean allowSelectedItem();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(getParentFragment()!=null){
            permisoListFragmentCallback = (PermisoListFragmentListener)getParentFragment();
        }else{
            permisoListFragmentCallback = (PermisoListFragmentListener) activity;
            setRetainInstance(true);
        }

        super.setContentView(R.layout.layout_headerlist_client_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentTransactionBoolean) {
            ejecutarConsulta();
        } else {
            Bundle args = new Bundle();
            args.putString(LoaderCliente.STRING_SEARCH, currentTransactionSearch);
            args.putBoolean(LoaderCliente.STRING_BOOLEAN, false);
            if(loaderCliente == null)
                loaderCliente = new LoaderCliente();
            getLoaderManager().initLoader(0, args, loaderCliente);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState == null)
        {
            currentTransactionBoolean = getArguments().getBoolean(ARG_TRANSACTIONTYPEBO);
            currentTransactionSearch = getArguments().getString(ARG_TRANSACTIONTYPEID);



            id_select = R.id.item_order_name;

            loaderCliente = new LoaderCliente();



        }
    }

    public void ejecutarConsulta(){
        Bundle args = new Bundle();
        args.putString(LoaderCliente.STRING_SEARCH, "");
        args.putBoolean(LoaderCliente.STRING_BOOLEAN, true);
        executeLoader(0, args, loaderCliente);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        linearLayout_rootParent = (LinearLayout) rootView.findViewById(R.id.linearLayout_headerlist_client_list);
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
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_CLIENTES);
                        requestSync(bundle);
                    } else {
                        Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                        refreshLayout.setRefreshing(false);
                    }
                }
            });
            headerList.getListView().setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
            headerList.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

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
        }
    }

    @Override
    public void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);

        linearLayout_rootParent.removeView(refreshLayout);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class LoaderCliente implements LoaderManager.LoaderCallbacks<List<Cliente>> {

        public static final String STRING_SEARCH = "string_search";
        public static final String STRING_BOOLEAN = "string_boolean";
        private String Search;
        private boolean flag;

        @Override
        public Loader<List<Cliente>> onCreateLoader(int arg0,
                                                    Bundle bundle) {

            Search = bundle.getString(STRING_SEARCH);
            flag = bundle.getBoolean(STRING_BOOLEAN);

            return new ClientLoader(getActivity(), getDataBase(), flag, Search, isContacts);

        }

        @Override
        public void onLoadFinished(Loader<List<Cliente>> arg0,
                                   List<Cliente> data)
        {
            lista = data;
            permisoListFragmentCallback.onFinalizaConsulta();
            if(adapter != null)
            {
                adapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onLoaderReset(Loader<List<Cliente>> arg0) {

        }
    }

    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);

        closeDialogProgress();
        refreshLayout.setRefreshing(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectionUtils.isNetAvailable(getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_CLIENTES);
                    requestSync(bundle);
                } else {
                    Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                    refreshLayout.setRefreshing(false);
                }
            }
        });
        onResume();
    }

}
