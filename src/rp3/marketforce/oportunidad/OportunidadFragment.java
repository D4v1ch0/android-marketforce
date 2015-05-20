package rp3.marketforce.oportunidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientDetailFragment;
import rp3.marketforce.cliente.ClientListFragment;
import rp3.marketforce.cliente.CrearClienteActivity;
import rp3.marketforce.cliente.ImportChooseFragment;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.oportunidad.Agente;
import rp3.marketforce.models.oportunidad.Etapa;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadContacto;
import rp3.marketforce.models.oportunidad.OportunidadResponsable;
import rp3.marketforce.models.oportunidad.OportunidadTarea;
import rp3.marketforce.ruta.MapaActivity;
import rp3.util.ConnectionUtils;
import rp3.widget.SlidingPaneLayout;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadFragment extends BaseFragment implements OportunidadListFragment.OportunidadListFragmentListener {

    private static final int PARALLAX_SIZE = 0;

    private OportunidadListFragment transactionListFragment;
    private OportunidadDetailFragment transactionDetailFragment;
    private SlidingPaneLayout slidingPane;

    private Menu menu;
    public boolean mTwoPane = false;
    public boolean isActiveListFragment = true;
    private long selectedOportunidadId;

    public static OportunidadFragment newInstance() {
        OportunidadFragment fragment = new OportunidadFragment();
        return fragment;
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_oportunidad,R.menu.fragment_oportunidad_menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        transactionListFragment = OportunidadListFragment.newInstance(true, null);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        slidingPane = (SlidingPaneLayout) rootView.findViewById(R.id.sliding_pane_clientes);
        slidingPane.setParallaxDistance(PARALLAX_SIZE);
        slidingPane.setShadowResource(R.drawable.sliding_pane_shadow);
        slidingPane.setSlidingEnabled(false);
        slidingPane.openPane();

        if(slidingPane.isOpen() &&
                rootView.findViewById(R.id.content_transaction_list).getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT)
            mTwoPane = true;
        else
            mTwoPane = false;

        if(!hasFragment(R.id.content_transaction_list))
            setFragment(R.id.content_transaction_list, transactionListFragment );

        slidingPane.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener(){

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPanelOpened(View panel) {
                isActiveListFragment = true;
                //getActivity().getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
                RefreshMenu();
                if(transactionListFragment != null)
                    transactionListFragment.onResume();
            }

            @Override
            public void onPanelClosed(View panel) {
                isActiveListFragment = false;
                RefreshMenu();

                // if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2) {
                // getActivity().getActionBar().setHomeButtonEnabled(true);
                //}
            }});

//		if(getChildFragmentManager().findFragmentById(R.id.transaction_detail) == null){
//			if(rootView.findViewById(R.id.content_transaction_list)!=null){
//
//			}
//		}
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        //PRUEBA
        /*
        Oportunidad op = new Oportunidad();
        op.setProbabilidad(80);
        op.setReferencia("Sauces");
        op.setPendiente(false);
        op.setObservacion("Prueba 2");
        op.setCalificacion(5);
        op.setDescripcion("GM Tecnologix");
        op.setDireccion("Garzota 2 Mz 141 Villa 10");
        op.setEstado("A");
        op.setFechaCreacion(Calendar.getInstance().getTime());
        op.setFechaUltimaGestion(Calendar.getInstance().getTime());
        op.setIdAgente(1);
        op.setIdEtapa(5);
        op.setIdOportunidad(1);
        op.setImporte(12000);
        Oportunidad.insert(getDataBase(), op);

        OportunidadContacto opc = new OportunidadContacto();
        opc.setIdOportunidad(op.getIdOportunidad());
        opc.setNombre("Gustavo Meregildo");
        opc.setFijo("243010");
        opc.setEsPrincipal(true);
        opc.setEmail("gmeregildo@rp3.com.ec");
        opc.setCargo("Agente");
        opc.setMovil("0915937924");
        OportunidadContacto.insert(getDataBase(), opc);

        OportunidadResponsable resp = new OportunidadResponsable();
        resp.setIdOportunidad(op.getIdOportunidad());
        resp.setIdAgente(1);
        OportunidadResponsable.insert(getDataBase(), resp);

        OportunidadTarea tarea = new OportunidadTarea();
        tarea.setIdEtapa(1);
        tarea.setIdOportunidad(op.getIdOportunidad());
        tarea.setIdTarea(121);
        tarea.setEstado("P");
        tarea.setObservacion("Prueba");
        tarea.setOrden(1);
        OportunidadTarea.insert(getDataBase(), tarea);

        tarea = new OportunidadTarea();
        tarea.setIdEtapa(1);
        tarea.setIdOportunidad(op.getIdOportunidad());
        tarea.setIdTarea(125);
        tarea.setEstado("P");
        tarea.setObservacion("Prueba");
        tarea.setOrden(2);
        OportunidadTarea.insert(getDataBase(), tarea);

        tarea = new OportunidadTarea();
        tarea.setIdEtapa(2);
        tarea.setIdOportunidad(op.getIdOportunidad());
        tarea.setIdTarea(124);
        tarea.setEstado("P");
        tarea.setObservacion("Prueba");
        tarea.setOrden(1);
        OportunidadTarea.insert(getDataBase(), tarea);

        tarea = new OportunidadTarea();
        tarea.setIdEtapa(3);
        tarea.setIdOportunidad(op.getIdOportunidad());
        tarea.setIdTarea(130);
        tarea.setEstado("P");
        tarea.setObservacion("Prueba");
        tarea.setOrden(1);
        OportunidadTarea.insert(getDataBase(), tarea);

        tarea = new OportunidadTarea();
        tarea.setIdEtapa(4);
        tarea.setIdOportunidad(op.getIdOportunidad());
        tarea.setIdTarea(131);
        tarea.setEstado("P");
        tarea.setObservacion("Prueba");
        tarea.setOrden(1);
        OportunidadTarea.insert(getDataBase(), tarea);

        tarea = new OportunidadTarea();
        tarea.setIdEtapa(5);
        tarea.setIdOportunidad(op.getIdOportunidad());
        tarea.setIdTarea(132);
        tarea.setEstado("P");
        tarea.setObservacion("Prueba");
        tarea.setOrden(1);
        OportunidadTarea.insert(getDataBase(), tarea);
        */

        if(selectedOportunidadId != 0){
            if(!mTwoPane)
                slidingPane.closePane();
        }
    }

    @Override
    public void onAfterCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));


        int searchicon = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView searchIcon = (ImageView)searchView.findViewById(searchicon);
        searchIcon.setImageResource(R.drawable.ic_action_search);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
        searchPlate.setHintTextColor(getResources().getColor(R.color.color_hint));
        searchPlate.setTextColor(getResources().getColor(R.color.apptheme_color));
        searchPlate.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);

        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        RefreshMenu();
    }

    private void RefreshMenu() {
        if (!mTwoPane) {
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_edit).setVisible(!isActiveListFragment);
            menu.findItem(R.id.action_crear_oportunidad).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_filtro).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_cambiar_etapa).setVisible(!isActiveListFragment);
        } else {
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_crear_oportunidad).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_filtro).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_edit).setVisible(selectedOportunidadId != 0);
            menu.findItem(R.id.action_cambiar_etapa).setVisible(!isActiveListFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_crear_oportunidad:
                Intent intent2 = new Intent(getContext(), CrearOportunidadActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_filtro:
                Intent intent = new Intent(getContext(), FiltroOportunidadActivity.class);
                startActivity(intent);
                break;
            case R.id.action_cambiar_etapa:
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Seleccione una etapa");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.select_dialog_singlechoice);
                final List<Etapa> etapas = Etapa.getEtapas(getDataBase());
                for(Etapa etp : etapas)
                    arrayAdapter.add(etp.getDescripcion());

                builderSingle.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(arrayAdapter,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Etapa choose = etapas.get(which);
                                Oportunidad opt = Oportunidad.getOportunidadId(getDataBase(), selectedOportunidadId);
                                opt.setIdEtapa(choose.getIdEtapa());
                                opt.setPendiente(true);
                                Oportunidad.update(getDataBase(), opt);

                            }
                        });
                builderSingle.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOportunidadSelected(Oportunidad oportunidad) {
        if(!mTwoPane) {
            //isMainFragment = false;
            slidingPane.closePane();
        }

        RefreshMenu();

        selectedOportunidadId = oportunidad.getID();
        transactionDetailFragment = OportunidadDetailFragment.newInstance(selectedOportunidadId);
        setFragment(R.id.content_transaction_detail, transactionDetailFragment);

    }

    @Override
    public void onFinalizaConsulta() {

    }

    @Override
    public boolean allowSelectedItem() {
        return false;
    }
}
