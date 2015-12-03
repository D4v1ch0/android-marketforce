package rp3.marketforce.oportunidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClienteEditActivity;
import rp3.marketforce.cliente.ClienteEditFragment;
import rp3.marketforce.cliente.CrearClienteActivity;
import rp3.marketforce.models.Agente;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadEtapa;
import rp3.marketforce.resumen.AgenteDetalleFragment;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.DonutChart;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.MapActivity;
import rp3.marketforce.utils.Utils;
import rp3.util.CalendarUtils;
import rp3.util.ViewUtils;
import rp3.widget.ViewPager;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadDetailFragment extends BaseFragment {
    public static final String ARG_ITEM_ID = "rp3.pos.transactionid";

    public static final String STATE_CLIENT_ID = "clientId";

    private static final int IDINFO = 501;
    private static final int IDTIMELINE = 502;
    private static final int IDFOTOS= 503;

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
    public AgenteDetalleFragment agenteDetalleFragment;

    private String str_titulo;
    private final int REQUEST_CODE_DETAIL_EDIT = 3;
    private boolean flag = false;
    private DrawableManager DManager;
    private int curentPage = -1;
    SimpleDateFormat format1 = new SimpleDateFormat("dd");
    SimpleDateFormat format2 = new SimpleDateFormat("MM");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy");

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

        //linearLayoutRigth.removeAllViews();
        //linearLayoutAdress.removeAllViews();
        //linearLayoutContact.removeAllViews();

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
        if(pagerAdapter == null)
            pagerAdapter = new DetailsPageAdapter();


        if(curentPage == -1) {
            setPageConfig(PagerDetalles.getCurrentItem());
        }
        else {
            setPageConfig(curentPage);
        }

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
            if (opt.getEstado().equalsIgnoreCase("A"))
                ((ImageView) rootView.findViewById(R.id.oportunidad_estado)).setImageResource(R.drawable.red_flag);
            if (opt.getEstado().equalsIgnoreCase("S"))
                ((ImageView) rootView.findViewById(R.id.oportunidad_estado)).setImageResource(R.drawable.blue_flag);
            if (opt.getEstado().equalsIgnoreCase("C"))
                ((ImageView) rootView.findViewById(R.id.oportunidad_estado)).setImageResource(R.drawable.green_flag);
            if (opt.getEstado().equalsIgnoreCase("NC"))
                ((ImageView) rootView.findViewById(R.id.oportunidad_estado)).setImageResource(R.drawable.gray_flag);

            if (!opt.getEstado().equalsIgnoreCase("C"))
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

            View view_info = null;
            if(pagerAdapter.getCount() != 3) {
                view_info = inflater.inflate(
                        R.layout.layout_oportunidad_info, null);
                view_info.setId(IDINFO);
            }
            else
                view_info = pagerAdapter.getView(0).findViewById(IDINFO);

            List<Integer> values = new ArrayList<Integer>();
            values.add(100 - opt.getProbabilidad());
            values.add(opt.getProbabilidad());

            List<Integer> colors = new ArrayList<Integer>();
            colors.add(getResources().getColor(R.color.tab_activated));
            colors.add(getResources().getColor(R.color.tab_inactivated));

            ((DonutChart) view_info.findViewById(R.id.donutChart)).setValues(values);
            ((DonutChart) view_info.findViewById(R.id.donutChart)).setColors(colors);
            ((DonutChart) view_info.findViewById(R.id.donutChart)).invalidate();
            ((TextView) view_info.findViewById(R.id.oportunidad_probabilidad)).setText(opt.getProbabilidad() + " %");
            //((ProgressBar) view_info.findViewById(R.id.oportunidad_probabilidad_progress)).setProgress(opt.getProbabilidad());
            ((TextView) view_info.findViewById(R.id.oportunidad_importe)).setText(numberFormat.format(opt.getImporte()));
            ((TextView) view_info.findViewById(R.id.oportunidad_movil)).setText(opt.getTelefono1());
            if(((TextView) view_info.findViewById(R.id.oportunidad_movil)).length() > 0) {
                ViewUtils.setPhoneActionClickListener(view_info.findViewById(R.id.oportunidad_movil), Utils.convertToSMSNumber(opt.getTelefono1()));
                ((TextView) view_info.findViewById(R.id.oportunidad_movil)).setPaintFlags(((TextView) view_info.findViewById(R.id.oportunidad_movil)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ((TextView) view_info.findViewById(R.id.oportunidad_movil)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
            }

            ((TextView) view_info.findViewById(R.id.oportunidad_fijo)).setText(opt.getTelefono2());
            if(((TextView) view_info.findViewById(R.id.oportunidad_fijo)).length() > 0) {
                ViewUtils.setPhoneActionClickListener(view_info.findViewById(R.id.oportunidad_fijo), Utils.convertToSMSNumber(opt.getTelefono2()));
                ((TextView) view_info.findViewById(R.id.oportunidad_fijo)).setPaintFlags(((TextView) view_info.findViewById(R.id.oportunidad_fijo)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ((TextView) view_info.findViewById(R.id.oportunidad_fijo)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
            }

            ((TextView) view_info.findViewById(R.id.oportunidad_correo)).setText(opt.getCorreo());
            if(((TextView) view_info.findViewById(R.id.oportunidad_correo)).length() > 0) {
                ViewUtils.setEmailActionClickListener(view_info.findViewById(R.id.oportunidad_correo), opt.getCorreo());
                ((TextView) view_info.findViewById(R.id.oportunidad_correo)).setPaintFlags(((TextView) view_info.findViewById(R.id.oportunidad_correo)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ((TextView) view_info.findViewById(R.id.oportunidad_correo)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
            }

            ((TextView) view_info.findViewById(R.id.oportunidad_pagina_web)).setText(opt.getPaginaWeb());
            if(((TextView) view_info.findViewById(R.id.oportunidad_pagina_web)).length() > 0) {
                ViewUtils.setLinkActionClickListener(view_info.findViewById(R.id.oportunidad_pagina_web), opt.getPaginaWeb());
                ((TextView) view_info.findViewById(R.id.oportunidad_pagina_web)).setPaintFlags(((TextView) view_info.findViewById(R.id.oportunidad_pagina_web)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ((TextView) view_info.findViewById(R.id.oportunidad_pagina_web)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
            }

            ((TextView) view_info.findViewById(R.id.oportunidad_direccion)).setText(opt.getDireccion());
            ((TextView) view_info.findViewById(R.id.oportunidad_medio_referencia)).setText(opt.getReferencia());
            ((TextView) view_info.findViewById(R.id.oportunidad_referencia)).setText(opt.getDireccionReferencia());
            ((TextView) view_info.findViewById(R.id.oportunidades_comentarios)).setText(opt.getObservacion());

            view_info.findViewById(R.id.oportunidad_locacion).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    intent.putExtra(MapActivity.ARG_LATITUD, opt.getLatitud());
                    intent.putExtra(MapActivity.ARG_LONGITUD, opt.getLongitud());
                    startActivity(intent);
                }
            });

            if (opt.getOportunidadResponsables().size() > 0) {
                view_info.findViewById(R.id.oportunidad_sin_responsables).setVisibility(View.GONE);
                view_info.findViewById(R.id.oportunidad_todos_responsables).setClickable(true);
                view_info.findViewById(R.id.oportunidad_todos_responsables).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agenteDetalleFragment = AgenteDetalleFragment.newInstance((int) opt.getID(), true);
                        agenteDetalleFragment.id_oportunidad = (int)opt.getID();
                        showDialogFragment(agenteDetalleFragment, "Agente", "Todos los Responsables");
                    }
                });
                ((TextView) view_info.findViewById(R.id.oportunidad_todos_responsables)).setPaintFlags(((TextView) view_info.findViewById(R.id.oportunidad_todos_responsables)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ((TextView) view_info.findViewById(R.id.oportunidad_todos_responsables)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                ((LinearLayout) view_info.findViewById(R.id.oportunidad_responsables)).removeAllViews();
                for (int i = 0; i < opt.getOportunidadResponsables().size(); i++) {
                    View view_responsable = inflater.inflate(
                            R.layout.rowlist_responsable_detail, null);
                    final Agente agt = Agente.getAgente(getDataBase(), opt.getOportunidadResponsables().get(i).getIdAgente());
                    ((TextView) view_responsable.findViewById(R.id.responsable_number)).setText(i + 1 + "");
                    ((TextView) view_responsable.findViewById(R.id.responsable_nombre)).setText(agt.getNombre());
                    ((TextView) view_responsable.findViewById(R.id.responsable_nombre)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            agenteDetalleFragment = AgenteDetalleFragment.newInstance(agt.getIdAgente());
                            agenteDetalleFragment.id_oportunidad = (int)opt.getID();
                            showDialogFragment(agenteDetalleFragment, "Agente", "Agente");
                        }
                    });
                    ((TextView) view_responsable.findViewById(R.id.responsable_nombre)).setPaintFlags(((TextView) view_responsable.findViewById(R.id.responsable_nombre)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    ((TextView) view_responsable.findViewById(R.id.responsable_nombre)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                    ((LinearLayout) view_info.findViewById(R.id.oportunidad_responsables)).addView(view_responsable);
                }
            }

            if (opt.getOportunidadContactos().size() > 0) {
                view_info.findViewById(R.id.oportunidad_sin_contactos).setVisibility(View.GONE);
                ((LinearLayout) view_info.findViewById(R.id.oportunidad_contactos)).removeAllViews();
                for (int i = 0; i < opt.getOportunidadContactos().size(); i++) {
                    View view_contacto = inflater.inflate(
                            R.layout.rowlist_oportunidad_contacto, null);
                    ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_number)).setText(i + 1 + "");
                    ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_nombre)).setText(opt.getOportunidadContactos().get(i).getNombre());
                    ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_cargo)).setText(opt.getOportunidadContactos().get(i).getCargo());

                    if(opt.getOportunidadContactos().get(i).getEmail() != null && opt.getOportunidadContactos().get(i).getEmail().length() != 0)
                    {
                        ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_email)).setText(opt.getOportunidadContactos().get(i).getEmail());
                        ViewUtils.setEmailActionClickListener(view_contacto.findViewById(R.id.oportunidad_contacto_email), opt.getOportunidadContactos().get(i).getEmail());
                        ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_email)).setPaintFlags(((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_email)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_email)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                    }
                    else
                        ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_email)).setText(R.string.label_sin_especificar);

                    if(opt.getOportunidadContactos().get(i).getMovil() != null && opt.getOportunidadContactos().get(i).getMovil().length() != 0)
                    {
                        ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_movil)).setText(opt.getOportunidadContactos().get(i).getMovil());
                        ViewUtils.setPhoneActionClickListener(view_contacto.findViewById(R.id.oportunidad_contacto_movil), opt.getOportunidadContactos().get(i).getMovil());
                        ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_movil)).setPaintFlags(((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_movil)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_movil)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                    }
                    else
                        ((TextView) view_contacto.findViewById(R.id.oportunidad_contacto_movil)).setText(R.string.label_sin_especificar);

                    if(opt.getOportunidadContactos().get(i).getURLFoto() != null && opt.getOportunidadContactos().get(i).getURLFoto().replace("\"", "").length() > 0) {
                        DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opt.getOportunidadContactos().get(i).getURLFoto().replace("\"", ""),
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
                    }
                    else
                    {

                    }
                    ((LinearLayout) view_info.findViewById(R.id.oportunidad_contactos)).addView(view_contacto);
                }
            }

            ScrollView fl = null;
            if(pagerAdapter.getCount() != 3) {
                linearLayoutRigth.addView(view_info);

                fl = new ScrollView(getActivity());
                ((ViewGroup) linearLayoutRigth.getParent()).removeView(linearLayoutRigth);
                fl.addView(linearLayoutRigth);
                pagerAdapter.addView(fl);
            }

            //Timeline
            View view_timeline = null;
            if(pagerAdapter.getCount() != 3) {
                view_timeline = inflater.inflate(
                        R.layout.layout_oportunidad_timeline, null);
                view_timeline.setId(IDTIMELINE);
            }
            else
                view_timeline = pagerAdapter.getView(1).findViewById(IDTIMELINE);

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
            etapas_layout.removeAllViews();
            for (OportunidadEtapa etp : etapas) {
                if(etp.getEtapa().getIdEtapaPadre() == 0) {
                    View row_etapa = inflater.inflate(R.layout.rowlist_oportunidad_etapa, null);

                    int id_icon = R.drawable.x_red;
                    if (etp.getEstado().equalsIgnoreCase("R"))
                        id_icon = R.drawable.check;

                    ((TextView) row_etapa.findViewById(R.id.map_phone)).setCompoundDrawablesWithIntrinsicBounds(0, 0, id_icon, 0);
                    ((TextView) row_etapa.findViewById(R.id.detail_agenda_estado)).setText(etp.getEtapa().getDescripcion());
                    ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setText(position + 1 + "");
                    if (position == 0) {
                        ((View)view_timeline.findViewById(R.id.etapa1_fecha).getParent()).setVisibility(View.VISIBLE);
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa1));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        ((ImageView) view_timeline.findViewById(R.id.etapa1_indicator)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EtapaFechasFragment fragment = EtapaFechasFragment.newInstance(idOptEtp);
                                showDialogFragment(fragment, "Etapa Fecha", nameEtp);
                            }
                        });
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((ImageView) view_timeline.findViewById(R.id.etapa1_indicator)).setImageResource(R.drawable.timeline1);
                        }
                        if(etp.getFechaInicio().getTime() > 0)
                            ((TextView) view_timeline.findViewById(R.id.etapa1_fecha)).setText(format1.format(etp.getFechaInicio()) + "/" + format2.format(etp.getFechaInicio()) + "/" + format3.format(etp.getFechaInicio()));
                        Calendar thisDay = Calendar.getInstance();
                        if(etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());
                        long dias = CalendarUtils.DayDiffTruncate(thisDay, ant);
                        dias = etp.getEtapa().getDias() - dias;
                        if(ant.getTime().getTime() > 0) {
                            if (dias < 0) {
                                ((TextView) view_timeline.findViewById(R.id.etapa1_dias)).setTextColor(getResources().getColor(R.color.color_unvisited));
                                dias = dias * -1;
                            }
                            ((TextView) view_timeline.findViewById(R.id.etapa1_dias)).setText(dias + " Días");
                            totalDias = totalDias + CalendarUtils.DayDiffTruncate(thisDay, ant);
                        }
                    }
                    if (position == 1) {
                        ((View)view_timeline.findViewById(R.id.etapa2_fecha).getParent()).setVisibility(View.VISIBLE);
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa2));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        ((ImageView) view_timeline.findViewById(R.id.etapa2_indicator)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EtapaFechasFragment fragment = EtapaFechasFragment.newInstance(idOptEtp);
                                showDialogFragment(fragment, "Etapa Fecha", nameEtp);
                            }
                        });
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((ImageView) view_timeline.findViewById(R.id.etapa2_indicator)).setImageResource(R.drawable.timeline2);
                        }
                        if(etp.getFechaInicio().getTime() > 0)
                            ((TextView) view_timeline.findViewById(R.id.etapa2_fecha)).setText(format1.format(etp.getFechaInicio()) + "/" + format2.format(etp.getFechaInicio()) + "/" + format3.format(etp.getFechaInicio()));
                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());
                        long dias = CalendarUtils.DayDiffTruncate(thisDay, ant);
                        dias = etp.getEtapa().getDias() - dias;
                        if(ant.getTime().getTime() > 0) {
                            if (dias < 0) {
                                ((TextView) view_timeline.findViewById(R.id.etapa2_dias)).setTextColor(getResources().getColor(R.color.color_unvisited));
                                dias = dias * -1;
                            }
                            ((TextView) view_timeline.findViewById(R.id.etapa2_dias)).setText(dias + " Días");
                            totalDias = totalDias + CalendarUtils.DayDiffTruncate(thisDay, ant);
                        }
                    }
                    if (position == 2) {
                        ((View)view_timeline.findViewById(R.id.etapa3_fecha).getParent()).setVisibility(View.VISIBLE);
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa3));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        ((ImageView) view_timeline.findViewById(R.id.etapa3_indicator)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EtapaFechasFragment fragment = EtapaFechasFragment.newInstance(idOptEtp);
                                showDialogFragment(fragment, "Etapa Fecha", nameEtp);
                            }
                        });
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((ImageView) view_timeline.findViewById(R.id.etapa3_indicator)).setImageResource(R.drawable.timeline3);
                        }
                        if(etp.getFechaInicio().getTime() > 0)
                            ((TextView) view_timeline.findViewById(R.id.etapa3_fecha)).setText(format1.format(etp.getFechaInicio()) + "/" + format2.format(etp.getFechaInicio()) + "/" + format3.format(etp.getFechaInicio()));
                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());
                        long dias = CalendarUtils.DayDiffTruncate(thisDay, ant);
                        dias = etp.getEtapa().getDias() - dias;
                        if(ant.getTime().getTime() > 0) {
                            if (dias < 0) {
                                ((TextView) view_timeline.findViewById(R.id.etapa3_dias)).setTextColor(getResources().getColor(R.color.color_unvisited));
                                dias = dias * -1;
                            }
                            ((TextView) view_timeline.findViewById(R.id.etapa3_dias)).setText(dias + " Días");
                            totalDias = totalDias + CalendarUtils.DayDiffTruncate(thisDay, ant);
                        }

                    }
                    if (position == 3) {
                        ((View)view_timeline.findViewById(R.id.etapa4_fecha).getParent()).setVisibility(View.VISIBLE);
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa4));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        ((ImageView) view_timeline.findViewById(R.id.etapa4_indicator)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EtapaFechasFragment fragment = EtapaFechasFragment.newInstance(idOptEtp);
                                showDialogFragment(fragment, "Etapa Fecha", nameEtp);
                            }
                        });
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((ImageView) view_timeline.findViewById(R.id.etapa4_indicator)).setImageResource(R.drawable.timeline4);
                        }
                        if(etp.getFechaInicio().getTime() > 0)
                            ((TextView) view_timeline.findViewById(R.id.etapa4_fecha)).setText(format1.format(etp.getFechaInicio()) + "/" + format2.format(etp.getFechaInicio()) + "/" + format3.format(etp.getFechaInicio()));
                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());
                        long dias = CalendarUtils.DayDiffTruncate(thisDay, ant);
                        dias = etp.getEtapa().getDias() - dias;
                        if(ant.getTime().getTime() > 0) {
                            if (dias < 0) {
                                ((TextView) view_timeline.findViewById(R.id.etapa4_dias)).setTextColor(getResources().getColor(R.color.color_unvisited));
                                dias = dias * -1;
                            }
                            ((TextView) view_timeline.findViewById(R.id.etapa4_dias)).setText(dias + " Días");
                            totalDias = totalDias + CalendarUtils.DayDiffTruncate(thisDay, ant);
                        }
                    }
                    if (position == 4) {
                        ((View)view_timeline.findViewById(R.id.etapa5_fecha).getParent()).setVisibility(View.VISIBLE);
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa5));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        ((ImageView) view_timeline.findViewById(R.id.etapa5_indicator)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EtapaFechasFragment fragment = EtapaFechasFragment.newInstance(idOptEtp);
                                showDialogFragment(fragment, "Etapa Fecha", nameEtp);
                            }
                        });
                        if (etp.getEstado().equalsIgnoreCase("R")) {
                            ((ImageView) view_timeline.findViewById(R.id.etapa5_indicator)).setImageResource(R.drawable.timeline5);
                        }
                        if(etp.getFechaInicio().getTime() > 0)
                            ((TextView) view_timeline.findViewById(R.id.etapa5_fecha)).setText(format1.format(etp.getFechaInicio()) + "/" + format2.format(etp.getFechaInicio()) + "/" + format3.format(etp.getFechaInicio()));
                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());
                        long dias = CalendarUtils.DayDiffTruncate(thisDay, ant);
                        dias = etp.getEtapa().getDias() - dias;
                        if(ant.getTime().getTime() > 0) {
                            if (dias < 0) {
                                ((TextView) view_timeline.findViewById(R.id.etapa5_dias)).setTextColor(getResources().getColor(R.color.color_unvisited));
                                dias = dias * -1;
                            }
                            ((TextView) view_timeline.findViewById(R.id.etapa5_dias)).setText(dias + " Días");
                            totalDias = totalDias + CalendarUtils.DayDiffTruncate(thisDay, ant);
                        }

                        row_etapa.findViewById(R.id.grey_line).setVisibility(View.GONE);
                    }
                    row_etapa.setId(etp.getIdEtapa());

                    row_etapa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), EtapaActivity.class);
                            intent.putExtra(EtapaActivity.ARG_ETAPA, view.getId());
                            intent.putExtra(EtapaActivity.ARG_OPORTUNIDAD, clientId);
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


            if(pagerAdapter.getCount() != 3) {
                linearLayoutAdress.addView(view_timeline);

                fl = new ScrollView(getActivity());
                ((ViewGroup) linearLayoutAdress.getParent()).removeView(linearLayoutAdress);
                fl.addView(linearLayoutAdress);
                pagerAdapter.addView(fl);
            }


            linearLayoutContact.setVisibility(View.VISIBLE);

            View view_fotos = null;
            if(pagerAdapter.getCount() != 3) {
                view_fotos = inflater.inflate(
                        R.layout.layout_oportunidad_fotos, null);
                view_fotos.setId(IDFOTOS);
            }
            else
                view_fotos = pagerAdapter.getView(2).findViewById(IDFOTOS);

            if (opt.getOportunidadFotos().size() > 0) {
                for (int i = 0; i < opt.getOportunidadFotos().size(); i++) {
                    view_fotos.findViewById(R.id.oportunidad_no_fotos).setVisibility(View.GONE);
                    if (opt.getOportunidadFotos().get(i).getURLFoto().length() > 0) {
                        switch (i) {
                            case 0:
                                DManager.fetchDrawableThumbnailOnThreadOnline(PreferenceManager.getString("server") +
                                                rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opt.getOportunidadFotos().get(i).getURLFoto().replace("\"",""),
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
                                DManager.fetchDrawableThumbnailOnThreadOnline(PreferenceManager.getString("server") +
                                                rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opt.getOportunidadFotos().get(i).getURLFoto().replace("\"",""),
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
                                DManager.fetchDrawableThumbnailOnThreadOnline(PreferenceManager.getString("server") +
                                                rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opt.getOportunidadFotos().get(i).getURLFoto().replace("\"",""),
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

            if(pagerAdapter.getCount() != 3) {
                linearLayoutContact.addView(view_fotos);

                fl = new ScrollView(getActivity());
                ((ViewGroup) linearLayoutContact.getParent()).removeView(linearLayoutContact);
                fl.addView(linearLayoutContact);
                pagerAdapter.addView(fl);
            }

            if(PagerDetalles.getAdapter() == null) {
                PagerDetalles.setAdapter(pagerAdapter);
                PagerDetalles.setCurrentItem(curentPage);
            }



        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
        BitmapDrawable dr = (BitmapDrawable)((ImageView)linearLayoutContact.findViewById(IDFOTOS).findViewById(R.id.oportunidad_photo1)).getDrawable();
        if(dr != null) {((ImageView)linearLayoutContact.findViewById(IDFOTOS).findViewById(R.id.oportunidad_photo1)).setImageDrawable(null);}
        dr = (BitmapDrawable)((ImageView)linearLayoutContact.findViewById(IDFOTOS).findViewById(R.id.oportunidad_photo2)).getDrawable();
        if(dr != null) {((ImageView)linearLayoutContact.findViewById(IDFOTOS).findViewById(R.id.oportunidad_photo2)).setImageDrawable(null);}
        dr = (BitmapDrawable)((ImageView)linearLayoutContact.findViewById(IDFOTOS).findViewById(R.id.oportunidad_photo3)).getDrawable();
        if(dr != null) {((ImageView)linearLayoutContact.findViewById(IDFOTOS).findViewById(R.id.oportunidad_photo3)).setImageDrawable(null);}
        dr = null;
        */

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

    public class MyGraphview extends View
    {
        private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        private int[] COLORS={Color.BLUE,Color.GREEN,Color.GRAY,Color.CYAN,Color.RED};
        RectF rectf = new RectF (10, 10, 200, 200);
        int temp=0;
        public MyGraphview(Context context, float[] values) {

            super(context);
            value_degree=new float[values.length];
            for(int i=0;i<values.length;i++)
            {
                value_degree[i]=values[i];
            }
        }
        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);

            for (int i = 0; i < value_degree.length; i++) {//values2.length; i++) {
                if (i == 0) {
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                }
                else
                {
                    temp += (int) value_degree[i - 1];
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }

    }

}
