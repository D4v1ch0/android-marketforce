package rp3.auna.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;

import org.json.JSONException;
import org.spongycastle.asn1.cmp.CAKeyUpdAnnContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.CrearLlamadaActivity;
import rp3.auna.Main2Activity;
import rp3.auna.ProspectoActivity;
import rp3.auna.R;
import rp3.auna.adapter.ProspectoAdapter;
import rp3.auna.customviews.Popup.WindowPopup;
import rp3.auna.dialog.GeneralValueDialog;
import rp3.auna.loader.ventanueva.ProspectoVtaLoader;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.ruta.CrearVisitaActivity;
import rp3.auna.util.helper.Util;
import rp3.auna.util.recyclerview.DividerItemDecoration;
import rp3.auna.util.recyclerview.ScrollManagerToolbar;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.configuration.PreferenceManager;

import static rp3.auna.util.helper.Util.isNumber;

/**
 * Created by Jesus Villa on 09/10/2017.
 */

public class ProspectoFragment extends Fragment implements SearchView.OnQueryTextListener{

    private static final String TAG = ProspectoFragment.class.getSimpleName();
    private static final int REQUEST_PROSPECTO_NUEVO = 3;
    private static final int RESULT_PROSPECTO_NUEVO = 3;
    private static final int REQUEST_LLAMADA_NUEVO = 4;
    private static final int RESULT_LLAMADA_NUEVO = 4;
    private static final int REQUEST_VISITA_NUEVO = 5;
    private static final int RESULT_VISITA_NUEVO = 5;
    private static final int REQUEST_PROSPECTO_EDIT = 17;
    private static final int RESULT_PROSPECTO_EDIT = 17;
    private List<ProspectoVtaDb> list;
    private ProspectoAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    TabLayout tabLayout;
    @BindView(R.id.recyclerViewProspecto) RecyclerView recyclerView;
    @BindView(R.id.progressBarProspecto) ProgressBar progressBar;
    @BindView(R.id.containerProspecto) RelativeLayout container;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private FrameLayout statusBar;
    private SharedPreferences sharedPreferences;
    private Boolean error = false;
    int recyclerViewPaddingTop;
    private TypedValue typedValueToolbarHeight = new TypedValue();
    private WindowPopup windowPopup;
    private boolean filterState = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prospectovta, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        try {
            sharedPreferences = getActivity().getSharedPreferences("Rp3MarketForceAuna", Context.MODE_PRIVATE);
            //swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_containerProspecto);
            appBarLayout = (AppBarLayout)getActivity().findViewById(R.id.appBarMain);
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            statusBar = (FrameLayout) getActivity().findViewById(R.id.statusBar);
            tabLayout = ((Main2Activity) getActivity()).tabLayout;
            tabLayout.setVisibility(View.GONE);
            ((Main2Activity) getActivity()).getSupportActionBar().setTitle("Prospectos");
            toolbarHideShow();
            recyclerViewDevelop();
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    //region Init

    public void toolbarHideShow() {
        final Activity activity = getActivity();
        /*appBarLayout.post(new Runnable() {
            @Override
            public void run() {
                ScrollManagerToolbar manager = new ScrollManagerToolbar(activity);
                manager.attach(recyclerView);
                manager.addView(appBarLayout, ScrollManagerToolbar.Direction.UP);
                manager.addView(statusBar, ScrollManagerToolbar.Direction.UP);
                manager.setInitialOffset(appBarLayout.getHeight());
            }
        });*/
        try{
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            adapter = new ProspectoAdapter(getActivity(), new ProspectoAdapter.CallbackVerProspecto() {
                @Override
                public void prospectoSelected(ProspectoVtaDb prospectoVtaDb, int position,View view) {
                    Log.d(TAG,"prospectoClickSelected...");
                    try {
                        int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE,0);
                        if(idAgente==0){
                            Toast.makeText(getActivity(), R.string.generic_show_message_service, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getActivity(),ProspectoActivity.class);
                        Bundle todo = new Bundle();
                        todo.putInt("Opcion",2);
                        intent.putExtras(todo);
                        Log.d(TAG,prospectoVtaDb.toString());
                        SessionManager.getInstance(getActivity()).createProspectoEdit(prospectoVtaDb);
                        getActivity().startActivityForResult(intent,REQUEST_PROSPECTO_EDIT);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }, new ProspectoAdapter.CallbackActionProspecto() {
                @Override
                public void prospectoSelected(final ProspectoVtaDb prospectoVtaDb, int position, View v) {
                    Log.d(TAG,"prospectoLongSelected...");
                    Log.d(TAG,prospectoVtaDb.toString());
                    try{
                        if(prospectoVtaDb.getDireccion1()!=null || prospectoVtaDb.getDireccion2()!=null){
                            if(prospectoVtaDb.getCelular() !=null || prospectoVtaDb.getTelefono()!=null){
                                if(prospectoVtaDb.getDireccion1().length()>0 || prospectoVtaDb.getDireccion2().length()>0){
                                    if(prospectoVtaDb.getCelular().length()>0 || prospectoVtaDb.getTelefono().length()>0){
                                        Log.d(TAG,"Tiene ambos...");
                                        showWindowPopup(v,0,prospectoVtaDb);
                                    }else{
                                        Log.d(TAG,"Tiene direcciones...");
                                        showWindowPopup(v,2,prospectoVtaDb);
                                    }
                                }else{
                                    Log.d(TAG,"Tiene telefonos...");
                                    showWindowPopup(v,1,prospectoVtaDb);
                                }
                            }
                        }
                        else if(prospectoVtaDb.getDireccion1()!=null || prospectoVtaDb.getDireccion2()!=null){
                            Log.d(TAG,"Tiene direcciones...");
                            showWindowPopup(v,2,prospectoVtaDb);
                        }else if(prospectoVtaDb.getCelular()!=null || prospectoVtaDb.getTelefono()!=null){
                            Log.d(TAG,"Tiene telefonos...");
                            showWindowPopup(v,1,prospectoVtaDb);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            // Create the recyclerViewAdapter
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void recyclerViewDevelop() {
        try{
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValueToolbarHeight, true);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.d(TAG,"Orientation_Portait...");
                if (Build.VERSION.SDK_INT >= 19) {
                    Log.d(TAG,"SDK_INT >= 19...");
                    recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
                }else{
                    Log.d(TAG,"SDK_INT < 19...");
                    recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
                }
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.d(TAG,"Orientation_LandScape...");
                if (Build.VERSION.SDK_INT >= 21) {
                    Log.d(TAG,"SDK_INT >= 21...");
                    recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
                }
                if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21){
                    Log.d(TAG,"Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21...");
                    recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
                }
                if (Build.VERSION.SDK_INT < 19) {
                    Log.d(TAG,"Build.VERSION.SDK_INT < 19...");
                    recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
                }
            }
            recyclerView.setPadding(0, recyclerViewPaddingTop, 0, 0);
            LoaderProspecto loader = new LoaderProspecto();
            Bundle args = new Bundle();
            getLoaderManager().initLoader(0,args,loader);
            //swipeRefresh();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //endregion

    private void showWindowPopup(View view, int type, final ProspectoVtaDb prospectoVtaDb){
        windowPopup = new WindowPopup(getActivity(), new WindowPopup.callbackWindow() {
            @Override
            public void onSelected(int position) {
                Log.d(TAG,"onSelected option:"+position);
                try {
                    switch (position){
                        case 1:
                            initSelectedCall(prospectoVtaDb);
                            break;
                        case 2:
                            initSelectedDirection(prospectoVtaDb);
                            break;
                        case 3:
                            initShared(prospectoVtaDb);
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        windowPopup.init(type);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        windowPopup.setAnimation(R.style.PopupAnimation);
        if(!windowPopup.isTooltipShown()) {
            windowPopup.showToolTip(view);
        }else{
            windowPopup.dismissTooltip();
        }
    }

    private void initShared(ProspectoVtaDb prospectoVtaDb){
        ArrayList<String> list = new ArrayList<>();
        if(prospectoVtaDb.getCelular()!=null || prospectoVtaDb.getTelefono()!=null && prospectoVtaDb.getEmail()!=null){
            int type = 0;
            if(!prospectoVtaDb.getEmail().trim().equalsIgnoreCase("") &&
                    prospectoVtaDb.getEmail().trim().length()>0){
                list.add(prospectoVtaDb.getEmail());
                type = 1 ;
            }
            if(prospectoVtaDb.getCelular().toString().trim().length()>0||
                    prospectoVtaDb.getTelefono().toString().trim().length()>0 ){
                Log.d(TAG,"Tiene telefonos y email...");
                if(prospectoVtaDb.getCelular()!=null){
                    if(prospectoVtaDb.getCelular().toString().trim().length()>0){
                        list.add(prospectoVtaDb.getCelular());
                    }
                }
                if(prospectoVtaDb.getTelefono()!=null){
                    if(prospectoVtaDb.getTelefono().toString().trim().length()>0){
                        list.add(prospectoVtaDb.getTelefono());
                    }
                }
                if(type==1){
                    showDialogShared(prospectoVtaDb,list,3);
                }else{
                    showDialogShared(prospectoVtaDb,list,1);
                }

            }
        }
        else if(prospectoVtaDb.getCelular()!=null || prospectoVtaDb.getTelefono()!=null){
            if(prospectoVtaDb.getCelular().toString().trim().length()>0 ||
                    prospectoVtaDb.getTelefono().toString().trim().length()>0){
                Log.d(TAG,"Tiene telefonos...");
                if(prospectoVtaDb.getCelular()!=null){
                    if(prospectoVtaDb.getCelular().toString().trim().length()>0){
                        list.add(prospectoVtaDb.getCelular());
                    }
                }
                if(prospectoVtaDb.getTelefono()!=null){
                    if(prospectoVtaDb.getTelefono().toString().trim().length()>0){
                        list.add(prospectoVtaDb.getTelefono());
                    }
                }
                showDialogShared(prospectoVtaDb,list,1);
            }
        }

        else if(prospectoVtaDb.getEmail()!=null){
            if(prospectoVtaDb.getEmail().toString().trim().length()>0){
                list.add(prospectoVtaDb.getEmail());
                showDialogShared(prospectoVtaDb,list,2);
            }
        }
    }

    private void showDialogShared(final ProspectoVtaDb prospectoVtaDb, ArrayList<String> list, final int tipo){
        Log.d(TAG,"tipo:"+tipo);
        GeneralValueDialog sharedDialog = GeneralValueDialog.newInstance(new GeneralValueDialog.callbackGeneralSelected() {
            @Override
            public void onSelected(String selected, int position) {
                Log.d(TAG,"selected:"+selected+" position:"+position);
                if(tipo==1){
                    Util.onSharedNumber(getActivity(),selected);
                }else if(tipo==2){
                    Util.onSharedEmail(getActivity(),selected,prospectoVtaDb.getNombre());
                }else{
                    if(position==0){
                        Util.onSharedEmail(getActivity(),selected,prospectoVtaDb.getNombre());
                    }else{
                        Util.onSharedNumber(getActivity(),selected);
                    }
                }
            }
        });
        Bundle todo = new Bundle();
        todo.putStringArrayList("list",list);
        sharedDialog.setCancelable(true);
        sharedDialog.setArguments(todo);
        sharedDialog.show(getFragmentManager(),"");
    }

    private void initSelectedCall(ProspectoVtaDb prospectoVtaDb){
        Bundle bundle =new Bundle();
        bundle.putString("Prospecto",prospectoVtaDb.getNombre());
        bundle.putInt("Estado",prospectoVtaDb.getEstado());
        bundle.putInt("IdProspecto",prospectoVtaDb.getIdProspecto());
        bundle.putLong("Id",prospectoVtaDb.getID());
        Location location = ((Main2Activity)getActivity()).location;
        if(location!=null){
            bundle.putDouble("Latitud",location.getLatitude());
            bundle.putDouble("Longitud",location.getLongitude());
        }
        int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE,0);
        if(idAgente==0){
            Toast.makeText(getActivity(), R.string.generic_show_message_service, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), CrearLlamadaActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent,REQUEST_LLAMADA_NUEVO);
    }

    private void initSelectedDirection(final ProspectoVtaDb prospectoVtaDb){
        try {
            ArrayList<String> direcciones = new ArrayList<String>();
            if(prospectoVtaDb.getDireccion1()!=null){
                if(prospectoVtaDb.getDireccion1().trim().length()>0){
                    direcciones.add(prospectoVtaDb.getDireccion1());
                }
            }
            if(prospectoVtaDb.getDireccion2()!=null){
                if(prospectoVtaDb.getDireccion2().trim().length()>0){
                    direcciones.add(prospectoVtaDb.getDireccion2());
                }
            }
            final String [] listDir = new String[direcciones.size()];
            for (int i=0;i<direcciones.size();i++){
                listDir[i] = direcciones.get(i);
            }
            AlertDialog dialog = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle)
                    .setItems(listDir, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /**
                             * Llamar a la actividad de crear Visita
                             */
                            Bundle bundle =new Bundle();
                            bundle.putString("Prospecto",prospectoVtaDb.getNombre());
                            bundle.putInt("Estado",prospectoVtaDb.getEstado());
                            bundle.putInt("IdProspecto",prospectoVtaDb.getIdProspecto());
                            bundle.putLong("Id",prospectoVtaDb.getID());
                            bundle.putString("Direccion",listDir[which]);
                            Location location = ((Main2Activity)getActivity()).location;
                            if(location!=null){
                                bundle.putDouble("Latitud",location.getLatitude());
                                bundle.putDouble("Longitud",location.getLongitude());
                            }
                            int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE,0);
                            if(idAgente==0){
                                Toast.makeText(getActivity(), R.string.generic_show_message_service, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent(getActivity(), rp3.auna.CrearVisitaActivity.class);
                            intent.putExtras(bundle);
                            getActivity().startActivityForResult(intent,REQUEST_VISITA_NUEVO);
                        }
                    })
                    .create();
            dialog.setTitle("Seleccionar Dirección");
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //region Search and Order Prospecto

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG,"onQueryTextSubmit...");
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG,"onCreateOptionsMenu...");
        inflater.inflate(R.menu.menu_prospecto, menu);
        MenuItem item=menu.findItem(R.id.action_search_prospecto);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
                ((Main2Activity)getActivity()).openDrawer();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.agregar_prospecto) {
            Log.d(TAG,"agregar prospecto item clicked...");
            int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE,0);
            if(idAgente==0){
                Toast.makeText(getActivity(), R.string.generic_show_message_service, Toast.LENGTH_SHORT).show();
                return true;
            }
            getActivity().startActivityForResult(new Intent(getActivity(),ProspectoActivity.class),REQUEST_PROSPECTO_NUEVO);
            return true;
        }
        if(id == R.id.order_nombre){
           orderbyName();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG,"onQueryTextChange...");
        newText=newText.toLowerCase();
        ArrayList<ProspectoVtaDb> newList=new ArrayList<>();
        Log.d(TAG,"newText es = "+newText);
        try {
            if(newText!=null){
                Log.d(TAG,"newText!=null...");
                if(newText.trim().length()>0){
                    filterState = true;
                    Log.d(TAG,"newText>0");
                    if(isNumber(newText)){
                        for(ProspectoVtaDb obj:list){
                            if(obj.getDocumento()!=null){
                                if(obj.getDocumento().trim().length()>0 && obj.getTipoDocumento()==1){
                                    String documento=String.valueOf(obj.getDocumento());
                                    if(documento.matches("(?i).*" + newText + ".*")){
                                        if(newText.trim().length()>0){
                                            newList.add(obj);
                                        }
                                    }
                                }
                            }

                        }
                        Log.d(TAG,"cantidad de PDV Document search:"+newList.size());
                        adapter.addMoreItems(newList);
                    }else if(!isNumber(newText)){
                        for(ProspectoVtaDb obj:list){
                            String name=String.valueOf(obj.getNombre());
                            if(name.matches("(?i).*" + newText + ".*")){
                                newList.add(obj);
                            }
                        }
                        Log.d(TAG,"cantidad de PDV nombre search:"+newList.size());
                        adapter.addMoreItems(newList);
                    }
                }else{
                    filterState = false;
                    Log.d(TAG,"newText==0");
                    adapter.addMoreItems(list);
                }
            }else{
                Log.d(TAG,"newText==null");
                filterState = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void filtrarProspecto(String newText){
        newText=newText.toLowerCase();
        ArrayList<ProspectoVtaDb> newList=new ArrayList<>();
        for(ProspectoVtaDb obj:list){
            String name=String.valueOf(obj.getNombre());
            if(name.matches("(?i).*" + newText + ".*")){
                newList.add(obj);
            }
        }
        Log.d(TAG,"cantidad de PDV search:"+newList.size());
        adapter.addMoreItems(newList);
    }

    public void orderbyName(){
        try {
            Collections.sort(list, new Comparator<ProspectoVtaDb>() {
                @Override
                public int compare(ProspectoVtaDb o1, ProspectoVtaDb o2) {

                    return o1.getNombre().compareToIgnoreCase(o2.getNombre());
                }
            });
            adapter.addMoreItems(list);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //endregion

    //region LoaderRefresh

    public class LoaderProspecto implements LoaderManager.LoaderCallbacks<List<ProspectoVtaDb>> {
        @Override
        public Loader<List<ProspectoVtaDb>> onCreateLoader(int arg0, Bundle bundle) {
            Log.d(TAG,"onCreateLoader...");
            container.setVisibility(View.GONE);
            return new ProspectoVtaLoader(getActivity(), Utils.getDataBase(getActivity()));
        }
        @Override
        public void onLoadFinished(Loader<List<ProspectoVtaDb>> arg0, List<ProspectoVtaDb> data) {
            Log.d(TAG,"onLoadFinished...");
            try {
                if(getActivity() != null) {
                    list = data;
                    if (list != null){
                        if (list.size() > 0) {
                            Log.d(TAG,"cantidad de prospectos:"+list.size());
                            toolbar.setTranslationY(0);
                            //swipeRefreshLayout.setRefreshing(false);
                            //ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.GONE);
                            adapter.addMoreItems(list);
                            recyclerView.setVisibility(View.VISIBLE);
                            //swipeRefreshLayout.setVisibility(View.VISIBLE);
                            container.setVisibility(View.GONE);
                        }else{
                            Log.d(TAG,"list.prospecto == 0...");
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            //swipeRefreshLayout.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Log.d(TAG,"list == null...");
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        container.setVisibility(View.VISIBLE);
                        //swipeRefreshLayout.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No hay Prospectos sincronizados ni registrados...", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            //swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<List<ProspectoVtaDb>> arg0) {
            Log.d(TAG,"onLoaderReset...");
        }
    }

    /*private void swipeRefresh(){
        //swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh...");
                refresh();
                //getLoaderManager().restartLoader(0,args,loader);
            }
        });
    }*/

    private void refresh(){
        getLoaderManager().destroyLoader(0);
        LoaderProspecto loader = new LoaderProspecto();
        Bundle args = new Bundle();
        Log.d(TAG,"getLoaderManager...");
        getLoaderManager().initLoader(0,args,loader);
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    //endregion

    //region Ciclo de Vida

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
        try {
            if(!filterState){
                refresh();
            }
            if(windowPopup!=null)windowPopup.dismissTooltip();
        } catch (Exception e){
            e.printStackTrace();
        }
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
        onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult..."+requestCode+" "+resultCode);
    }

    //endregion
}
