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

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClienteEditActivity;
import rp3.marketforce.cliente.ClienteEditFragment;
import rp3.marketforce.cliente.CrearClienteActivity;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.oportunidad.Agente;
import rp3.marketforce.models.oportunidad.Etapa;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadEtapa;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.util.CalendarUtils;
import rp3.util.Convert;
import rp3.util.Format;
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
    SimpleDateFormat format1 = new SimpleDateFormat("dd");
    SimpleDateFormat format2 = new SimpleDateFormat("MMMM yyyy");

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


        if(curentPage == -1)
            setPageConfig(PagerDetalles.getCurrentItem());
        else
            setPageConfig(curentPage);
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
        hideDialogConfirmation();
        try {
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            numberFormat.setMaximumFractionDigits(0);
            numberFormat.setMinimumFractionDigits(0);

            long totalDias = 0;

            ((TextView) rootView.findViewById(R.id.oportunidad_descripcion)).setText(opt.getDescripcion());
            ((TextView) rootView.findViewById(R.id.oportunidad_referencia)).setText(opt.getTipoEmpresa());
            ((RatingBar) rootView.findViewById(R.id.oportunidad_calificacion)).setRating(opt.getCalificacion());
            if (opt.getEstado().equalsIgnoreCase("S"))
                ((ImageView) rootView.findViewById(R.id.oportunidad_estado)).setImageResource(R.drawable.blue_flag);
            if (opt.getEstado().equalsIgnoreCase("C"))
                ((ImageView) rootView.findViewById(R.id.oportunidad_estado)).setImageResource(R.drawable.green_flag);
            if (opt.getEstado().equalsIgnoreCase("NC"))
                ((ImageView) rootView.findViewById(R.id.oportunidad_estado)).setImageResource(R.drawable.gray_flag);

            rootView.findViewById(R.id.oportunidad_estado).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), CambiarEstadoActivity.class);
                    intent.putExtra(CambiarEstadoActivity.ARG_ID, clientId);
                    startActivity(intent);
                }
            });

            inflater = (LayoutInflater) this.getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View view_info = inflater.inflate(
                    R.layout.layout_oportunidad_info, null);

            ((TextView) view_info.findViewById(R.id.oportunidad_probabilidad)).setText(opt.getProbabilidad() + " %");
            ((ProgressBar) view_info.findViewById(R.id.oportunidad_probabilidad_progress)).setProgress(opt.getProbabilidad());
            ((TextView) view_info.findViewById(R.id.oportunidad_importe)).setText(numberFormat.format(opt.getImporte()));
            ((TextView) view_info.findViewById(R.id.oportunidad_movil)).setText(opt.getTelefono1());
            ((TextView) view_info.findViewById(R.id.oportunidad_fijo)).setText(opt.getTelefono2());
            ((TextView) view_info.findViewById(R.id.oportunidad_correo)).setText(opt.getCorreo());
            ((TextView) view_info.findViewById(R.id.oportunidad_pagina_web)).setText(opt.getPaginaWeb());
            ((TextView) view_info.findViewById(R.id.oportunidad_direccion)).setText(opt.getDireccion());
            ((TextView) view_info.findViewById(R.id.oportunidad_medio_referencia)).setText(opt.getReferencia());
            ((TextView) view_info.findViewById(R.id.oportunidad_referencia)).setText(opt.getDireccionReferencia());
            ((TextView) view_info.findViewById(R.id.oportunidades_comentarios)).setText(opt.getObservacion());

            if (opt.getOportunidadResponsables().size() > 0) {
                view_info.findViewById(R.id.oportunidad_sin_responsables).setVisibility(View.GONE);
                for (int i = 0; i < opt.getOportunidadResponsables().size(); i++) {
                    View view_responsable = inflater.inflate(
                            R.layout.rowlist_responsable_detail, null);
                    ((TextView) view_responsable.findViewById(R.id.responsable_number)).setText(i + 1 + "");
                    ((TextView) view_responsable.findViewById(R.id.responsable_nombre)).setText(Agente.getAgente(getDataBase(), opt.getOportunidadResponsables().get(i).getIdAgente()).getNombre());
                    ((LinearLayout) view_info.findViewById(R.id.oportunidad_responsables)).addView(view_responsable);
                }
            }

            if (opt.getOportunidadContactos().size() > 0) {
                view_info.findViewById(R.id.oportunidad_sin_contactos).setVisibility(View.GONE);
                for (int i = 0; i < opt.getOportunidadContactos().size(); i++) {
                    View view_contacto = inflater.inflate(
                            R.layout.rowlist_oportunidad_contacto, null);
                    ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_number)).setText(i + 1 + "");
                    ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_nombre)).setText(opt.getOportunidadContactos().get(i).getNombre());
                    ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_cargo)).setText(opt.getOportunidadContactos().get(i).getCargo());
                    DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                                    rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + opt.getOportunidadContactos().get(i).getURLFoto(),
                            (ImageView) view_contacto.getRootView().findViewById(R.id.oportunidad_contacto_foto));
                    final int finalI = i;
                    view_contacto.findViewById(R.id.oportunidad_contacto_foto).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), FotoOportunidadActivity.class);
                            intent.putExtra(FotoOportunidadActivity.ARG_ID, opt.getOportunidadContactos().get(finalI).getID());
                            intent.putExtra(FotoOportunidadActivity.ARG_TIPO, 1);
                            startActivity(intent);
                        }
                    });
                    view_contacto.findViewById(R.id.contacto_foto).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), FotoOportunidadActivity.class);
                            intent.putExtra(FotoOportunidadActivity.ARG_ID, opt.getOportunidadContactos().get(finalI).getID());
                            intent.putExtra(FotoOportunidadActivity.ARG_TIPO, 1);
                            startActivity(intent);
                        }
                    });
                    ((LinearLayout) view_info.findViewById(R.id.oportunidad_contactos)).addView(view_contacto);
                }
            }


            linearLayoutRigth.addView(view_info);

            ScrollView fl = new ScrollView(getActivity());
            ((ViewGroup) linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
            fl.addView(linearLayoutRigth);
            pagerAdapter.addView(fl);

            //Timeline

            View view_timeline = inflater.inflate(
                    R.layout.layout_oportunidad_timeline, null);

            LinearLayout etapas_layout = ((LinearLayout) view_timeline.findViewById(R.id.oportunidad_etapas));
            List<OportunidadEtapa> etapas = null;
            if (opt.getIdOportunidad() == 0)
                etapas = OportunidadEtapa.getEtapasOportunidadInt(getDataBase(), opt.getID());
            else
                etapas = OportunidadEtapa.getEtapasOportunidad(getDataBase(), opt.getIdOportunidad());

            if (etapas.size() > 0)
                view_timeline.findViewById(R.id.oportunidad_no_etapas).setVisibility(View.GONE);

            int position = 0;
            Calendar ant = Calendar.getInstance();
            ant.setTime(opt.getFechaCreacion());
            for (OportunidadEtapa etp : etapas) {
                if(etp.getEtapa().getIdEtapaPadre() == 0) {
                    View row_etapa = inflater.inflate(R.layout.rowlist_oportunidad_etapa, null);

                    int id_icon = R.drawable.x_red;
                    if (etp.getEstado().equalsIgnoreCase("R"))
                        id_icon = R.drawable.check;

                    ((TextView) row_etapa.findViewById(R.id.map_phone)).setCompoundDrawablesWithIntrinsicBounds(0, 0, id_icon, 0);
                    ((TextView) row_etapa.findViewById(R.id.detail_agenda_estado)).setText(etp.getEtapa().getDescripcion());
                    ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setText(etp.getIdEtapa() + "");
                    if (position == 0) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa1));
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((TextView) view_timeline.findViewById(R.id.etapa1_fecha)).setText(format1.format(etp.getFechaFin()) + " de " + format2.format(etp.getFechaFin()));
                            Calendar thisDay = Calendar.getInstance();
                            thisDay.setTime(etp.getFechaFin());
                            ((TextView) view_timeline.findViewById(R.id.etapa1_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                            ((ImageView) view_timeline.findViewById(R.id.etapa1_indicator)).setImageResource(R.drawable.timeline1);
                            totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                        }
                    }
                    if (position == 1) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa2));
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((TextView) view_timeline.findViewById(R.id.etapa2_fecha)).setText(format1.format(etp.getFechaFin()) + " de " + format2.format(etp.getFechaFin()));
                            Calendar thisDay = Calendar.getInstance();
                            thisDay.setTime(etp.getFechaFin());
                            ((TextView) view_timeline.findViewById(R.id.etapa2_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                            ((ImageView) view_timeline.findViewById(R.id.etapa2_indicator)).setImageResource(R.drawable.timeline2);
                            totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                        }
                    }
                    if (position == 2) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa3));
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((TextView) view_timeline.findViewById(R.id.etapa3_fecha)).setText(format1.format(etp.getFechaFin()) + " de " + format2.format(etp.getFechaFin()));
                            Calendar thisDay = Calendar.getInstance();
                            thisDay.setTime(etp.getFechaFin());
                            ((TextView) view_timeline.findViewById(R.id.etapa3_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                            ((ImageView) view_timeline.findViewById(R.id.etapa3_indicator)).setImageResource(R.drawable.timeline3);
                            totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                        }
                    }
                    if (position == 3) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa4));
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((TextView) view_timeline.findViewById(R.id.etapa4_fecha)).setText(format1.format(etp.getFechaFin()) + " de " + format2.format(etp.getFechaFin()));
                            Calendar thisDay = Calendar.getInstance();
                            thisDay.setTime(etp.getFechaFin());
                            ((TextView) view_timeline.findViewById(R.id.etapa4_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                            ((ImageView) view_timeline.findViewById(R.id.etapa4_indicator)).setImageResource(R.drawable.timeline4);
                            totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                        }
                    }
                    if (position == 4) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa5));
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((TextView) view_timeline.findViewById(R.id.etapa5_fecha)).setText(format1.format(etp.getFechaFin()) + " de " + format2.format(etp.getFechaFin()));
                            Calendar thisDay = Calendar.getInstance();
                            thisDay.setTime(etp.getFechaFin());
                            ((TextView) view_timeline.findViewById(R.id.etapa5_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                            ((ImageView) view_timeline.findViewById(R.id.etapa5_indicator)).setImageResource(R.drawable.timeline5);
                            totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                        }
                        row_etapa.findViewById(R.id.grey_line).setVisibility(View.GONE);
                    }
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
                    position++;
                    ant.setTime(etp.getFechaFin());
                }
            }

            ((TextView) view_timeline.findViewById(R.id.timeline_total_dias)).setText("Total " + totalDias + " Días");

            linearLayoutAdress.setVisibility(View.VISIBLE);
            linearLayoutAdress.addView(view_timeline);


            fl = new ScrollView(getActivity());
            ((ViewGroup) linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
            fl.addView(linearLayoutAdress);
            pagerAdapter.addView(fl);


            linearLayoutContact.setVisibility(View.VISIBLE);

            View view_fotos = inflater.inflate(
                    R.layout.layout_oportunidad_fotos, null);

            if (opt.getOportunidadFotos().size() > 0) {
                for (int i = 0; i < opt.getOportunidadFotos().size(); i++) {
                    view_fotos.findViewById(R.id.oportunidad_no_fotos).setVisibility(View.GONE);
                    if (opt.getOportunidadFotos().get(i).getURLFoto().length() > 0) {
                        switch (i) {
                            case 0:
                                DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                                                rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + opt.getOportunidadFotos().get(i).getURLFoto(),
                                        ((ImageView) view_fotos.findViewById(R.id.oportunidad_photo1)));
                                final int finalI = i;
                                ((ImageButton) view_fotos.findViewById(R.id.oportunidad_photo1_click)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getContext(), FotoOportunidadActivity.class);
                                        intent.putExtra(FotoOportunidadActivity.ARG_ID, opt.getOportunidadFotos().get(finalI).getID());
                                        intent.putExtra(FotoOportunidadActivity.ARG_TIPO, 2);
                                        startActivity(intent);
                                    }
                                });
                                ((TextView) view_fotos.findViewById(R.id.oportunidad_photo1_fecha)).setText("");
                                view_fotos.findViewById(R.id.photo1_layout).setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                                                rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + opt.getOportunidadFotos().get(i).getURLFoto(),
                                        ((ImageView) view_fotos.findViewById(R.id.oportunidad_photo2)));
                                final int finalI2 = i;
                                ((ImageButton) view_fotos.findViewById(R.id.oportunidad_photo2_click)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getContext(), FotoOportunidadActivity.class);
                                        intent.putExtra(FotoOportunidadActivity.ARG_ID, opt.getOportunidadFotos().get(finalI2).getID());
                                        intent.putExtra(FotoOportunidadActivity.ARG_TIPO, 2);
                                        startActivity(intent);
                                    }
                                });
                                ((TextView) view_fotos.findViewById(R.id.oportunidad_photo2_fecha)).setText("");
                                view_fotos.findViewById(R.id.photo2_layout).setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                                                rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + opt.getOportunidadFotos().get(i).getURLFoto(),
                                        ((ImageView) view_fotos.findViewById(R.id.oportunidad_photo3)));
                                final int finalI3 = i;
                                ((ImageButton) view_fotos.findViewById(R.id.oportunidad_photo3_click)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getContext(), FotoOportunidadActivity.class);
                                        intent.putExtra(FotoOportunidadActivity.ARG_ID, opt.getOportunidadFotos().get(finalI3).getID());
                                        intent.putExtra(FotoOportunidadActivity.ARG_TIPO, 2);
                                        startActivity(intent);
                                    }
                                });
                                ((TextView) view_fotos.findViewById(R.id.oportunidad_photo3_fecha)).setText("");
                                view_fotos.findViewById(R.id.photo3_layout).setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
            }


            linearLayoutContact.addView(view_fotos);


            fl = new ScrollView(getActivity());
            ((ViewGroup) linearLayoutContact.getParent()).removeView(linearLayoutContact);
            fl.addView(linearLayoutContact);
            pagerAdapter.addView(fl);

            PagerDetalles.setAdapter(pagerAdapter);
            PagerDetalles.setCurrentItem(curentPage);
        } catch (Exception ex) {

        }

        //TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
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
