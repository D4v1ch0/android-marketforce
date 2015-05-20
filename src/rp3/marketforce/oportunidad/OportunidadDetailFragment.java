package rp3.marketforce.oportunidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClienteEditActivity;
import rp3.marketforce.cliente.ClienteEditFragment;
import rp3.marketforce.cliente.CrearClienteActivity;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.oportunidad.Etapa;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.widget.ViewPager;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadDetailFragment extends BaseFragment {
    public static final String ARG_ITEM_ID = "rp3.pos.transactionid";

    public static final String STATE_CLIENT_ID = "clientId";

    private long clientId;
    private Oportunidad opt;
    private LayoutInflater inflater;
    private LinearLayout linearLayoutRigth;
    private LinearLayout linearLayoutAdress;
    private LinearLayout linearLayoutContact;
    private ViewPager PagerDetalles;
    private DetailsPageAdapter pagerAdapter;
    private ImageButton TabInfo;
    private ImageButton TabDirecciones;
    private ImageButton TabContactos;
    private ImageView ArrowInfo, ArrowDir, ArrowCont;

    private String str_titulo;
    private final int REQUEST_CODE_DETAIL_EDIT = 3;
    private boolean flag = false;
    private DrawableManager DManager;
    private int curentPage = -1;

    public static OportunidadDetailFragment newInstance(Oportunidad op) {
        return newInstance(op.getID());
    }

    public static OportunidadDetailFragment newInstance(long id) {
        Bundle arguments = new Bundle();
        arguments.putLong(OportunidadDetailFragment.ARG_ITEM_ID, id);
        OportunidadDetailFragment fragment = new OportunidadDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/*
		 * Se instancia Drawable Manager para carga de imagenes;
		 */
        DManager = new DrawableManager();
        if (getParentFragment() == null)
            setRetainInstance(true);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            clientId = getArguments().getLong(ARG_ITEM_ID);
        } else if (savedInstanceState != null) {
            clientId = savedInstanceState.getLong(STATE_CLIENT_ID);
        }

        if (clientId != 0) {
            super.setContentView(R.layout.fragment_oportunidad_detail);
        } else {
            super.setContentView(R.layout.base_content_no_selected_item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (clientId != 0) {
            opt = Oportunidad.getOportunidadId(getDataBase(), clientId);
        }

        if (opt == null) return;
        PagerDetalles = (ViewPager) getRootView().findViewById(R.id.detail_oportunidad_pager);
        TabInfo = (ImageButton) getRootView().findViewById((R.id.detail_tab_info));
        TabDirecciones = (ImageButton) getRootView().findViewById((R.id.detail_tab_direccion));
        TabContactos = (ImageButton) getRootView().findViewById((R.id.detail_tab_contactos));
        ArrowInfo = (ImageView) getRootView().findViewById((R.id.detail_tab_info_arrow));
        ArrowDir = (ImageView) getRootView().findViewById((R.id.detail_tab_direccion_arrow));
        ArrowCont = (ImageView) getRootView().findViewById((R.id.detail_tab_contactos_arrow));

        if (linearLayoutRigth == null) {
            linearLayoutRigth = (LinearLayout) getRootView()
                    .findViewById(R.id.linearLayout_content_rigth);
            linearLayoutAdress = (LinearLayout) getRootView()
                    .findViewById(R.id.linearLayout_content_adress);
            linearLayoutContact = (LinearLayout) getRootView()
                    .findViewById(R.id.linearLayout_content_contactos);
        }

        linearLayoutRigth.removeAllViews();
        linearLayoutAdress.removeAllViews();
        linearLayoutContact.removeAllViews();

        TabInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PagerDetalles.setCurrentItem(0);
            }
        });

        TabDirecciones.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PagerDetalles.setCurrentItem(1);
            }
        });

        TabContactos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PagerDetalles.setCurrentItem(2);
            }
        });

        PagerDetalles.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int arg0) {
                setPageConfig(arg0);

            }
        });
        pagerAdapter = new DetailsPageAdapter();


        setPageConfig(PagerDetalles.getCurrentItem());
        if (opt != null) {
            renderOportunidad(getRootView());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //setRetainInstance(true);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {

    }

    private void setPageConfig(int page){
        curentPage = page;
        String title = pagerAdapter.getPageTitle(page).toString();
        if(title.equalsIgnoreCase("Info"))
        {
            TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
            TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            ArrowInfo.setVisibility(View.VISIBLE);
            ArrowDir.setVisibility(View.INVISIBLE);
            ArrowCont.setVisibility(View.INVISIBLE);
        }
        if(title.equalsIgnoreCase("Direcciones"))
        {
            TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_activated));
            TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            ArrowInfo.setVisibility(View.INVISIBLE);
            ArrowDir.setVisibility(View.VISIBLE);
            ArrowCont.setVisibility(View.INVISIBLE);
        }
        if(title.equalsIgnoreCase("Contactos"))
        {
            TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_activated));
            ArrowInfo.setVisibility(View.INVISIBLE);
            ArrowDir.setVisibility(View.INVISIBLE);
            ArrowCont.setVisibility(View.VISIBLE);
        }
    }

    private void renderOportunidad(View rootView) {
        boolean telf = false, email = false;
        hideDialogConfirmation();
        String email_str = "";

        ((TextView) rootView.findViewById(R.id.oportunidad_descripcion)).setText(opt.getDescripcion());
        ((TextView) rootView.findViewById(R.id.oportunidad_referencia)).setText(opt.getReferencia());
        ((RatingBar) rootView.findViewById(R.id.oportunidad_calificacion)).setRating(opt.getCalificacion());

        inflater = (LayoutInflater) this.getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view_info = inflater.inflate(
                R.layout.layout_oportunidad_info, null);

        ((TextView) view_info.findViewById(R.id.oportunidad_probabilidad)).setText(opt.getProbabilidad() + " %");
        ((ProgressBar) view_info.findViewById(R.id.oportunidad_probabilidad_progress)).setProgress(opt.getProbabilidad());
        ((TextView) view_info.findViewById(R.id.oportunidad_importe)).setText("$ " + opt.getImporte());
        ((TextView) view_info.findViewById(R.id.oportunidad_movil)).setText("");
        ((TextView) view_info.findViewById(R.id.oportunidad_fijo)).setText("");
        ((TextView) view_info.findViewById(R.id.oportunidad_correo)).setText("");
        ((TextView) view_info.findViewById(R.id.oportunidad_pagina_web)).setText("");
        ((TextView) view_info.findViewById(R.id.oportunidad_direccion)).setText(opt.getDireccion());
        ((TextView) view_info.findViewById(R.id.oportunidad_referencia)).setText(opt.getReferencia());
        ((TextView) view_info.findViewById(R.id.oportunidades_comentarios)).setText(opt.getObservacion());


        linearLayoutRigth.addView(view_info);

        ScrollView fl = new ScrollView(getActivity());
        ((ViewGroup) linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
        fl.addView(linearLayoutRigth);
        pagerAdapter.addView(fl);

        //Timeline

        View view_timeline = inflater.inflate(
                R.layout.layout_oportunidad_timeline, null);

        LinearLayout etapas_layout = ((LinearLayout) view_timeline.findViewById(R.id.oportunidad_etapas));
        List<Etapa> etapas = Etapa.getEtapas(getDataBase());

        if(etapas.size() > 0)
            view_timeline.findViewById(R.id.oportunidad_no_etapas).setVisibility(View.GONE);

        for(Etapa etp : etapas)
        {
            View row_etapa = inflater.inflate(R.layout.rowlist_tarea, null);

            int id_icon = R.drawable.checkbox_off;

            ((TextView) row_etapa.findViewById(R.id.map_phone)).setCompoundDrawablesWithIntrinsicBounds(0, 0, id_icon, 0);
            ((TextView) row_etapa.findViewById(R.id.detail_agenda_estado)).setText(etp.getDescripcion());
            ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setText(etp.getIdEtapa()+ "");
            row_etapa.setId(etp.getIdEtapa());

            row_etapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), EtapaTareasActivity.class);
                    intent.putExtra(EtapaTareasActivity.ARG_ETAPA, view.getId());
                    intent.putExtra(EtapaTareasActivity.ARG_OPORTUNIDAD, clientId);
                    startActivity(intent);
                }
            });

            etapas_layout.addView(row_etapa);
        }

        linearLayoutAdress.setVisibility(View.VISIBLE);
        linearLayoutAdress.addView(view_timeline);


        fl = new ScrollView(getActivity());
        ((ViewGroup) linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
        fl.addView(linearLayoutAdress);
        pagerAdapter.addView(fl);


        linearLayoutContact.setVisibility(View.VISIBLE);

        View view_fotos = inflater.inflate(
                R.layout.layout_oportunidad_fotos, null);


        linearLayoutContact.addView(view_fotos);


        fl = new ScrollView(getActivity());
        ((ViewGroup) linearLayoutContact.getParent()).removeView(linearLayoutContact);
        fl.addView(linearLayoutContact);
        pagerAdapter.addView(fl);


        PagerDetalles.setAdapter(pagerAdapter);

        TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_CLIENT_ID, clientId);
    }

    private void showDetailDialog(long transactionDetailId) {
        ClienteEditFragment fragment = ClienteEditFragment
                .newInstance(transactionDetailId);
        showDialogFragment(fragment, "detailDialog");
    }

    private void startDetailEditActivity(long transactionDetailId) {
        startActivityForResult(ClienteEditActivity.newIntent(getContext(),
                transactionDetailId), REQUEST_CODE_DETAIL_EDIT);
    }

    public void onDetailItemEdit(long transactionDetailId) {
        Intent intent = new Intent(getActivity(), CrearClienteActivity.class);
        intent.putExtra(CrearClienteActivity.ARG_IDCLIENTE, transactionDetailId);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
