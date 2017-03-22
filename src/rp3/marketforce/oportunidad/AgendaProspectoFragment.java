package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;
import rp3.maps.utils.SphericalUtil;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.actividades.ActividadActivity;
import rp3.marketforce.actividades.ActualizacionActivity;
import rp3.marketforce.actividades.CheckboxActivity;
import rp3.marketforce.actividades.GrupoActivity;
import rp3.marketforce.actividades.MultipleActivity;
import rp3.marketforce.actividades.SeleccionActivity;
import rp3.marketforce.actividades.TextoActivity;
import rp3.marketforce.content.EnviarUbicacionReceiver;
import rp3.marketforce.marcaciones.JustificacionFragment;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.DiaLaboral;
import rp3.marketforce.models.marcacion.Marcacion;
import rp3.marketforce.models.marcacion.Permiso;
import rp3.marketforce.models.oportunidad.AgendaOportunidad;
import rp3.marketforce.models.oportunidad.OportunidadEtapa;
import rp3.marketforce.ruta.FotoActivity;
import rp3.marketforce.ruta.ListaTareasAdapter;
import rp3.marketforce.ruta.ObservacionesActivity;
import rp3.marketforce.ruta.ObservacionesFragment;
import rp3.marketforce.ruta.TareasFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.BitmapUtils;
import rp3.util.CalendarUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;

/**
 * Created by magno_000 on 14/03/2017.
 */
public class AgendaProspectoFragment extends rp3.app.BaseFragment{

    public static final String ARG_ITEM_ID = "idagenda";
    public static final String ARG_AGENDA_ID = "agenda";
    public static final String ARG_RUTA_ID = "ruta";

    public static final String ARG_LONGITUD = "longitud";
    public static final String ARG_LATITUD = "latitud";
    public static final String ARG_SOLO_VISTA = "solovista";

    public static final String PARENT_SOURCE_LIST = "LIST";
    public static final String PARENT_SOURCE_SEARCH = "SEARCH";

    public static final String STATE_IDAGENDA = "state_idagenda";

    public static final int DIALOG_INICIO_JORNADA = 1;
    public static final int DIALOG_FIN_JORNADA = 2;

    private long idAgenda;
    private AgendaOportunidad agenda;
    private boolean soloVista = true, clienteNull = false;
    private SimpleDateFormat format1;
    private SimpleDateFormat format2;
    public boolean reDoMenu = true;
    Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
    private Menu menuRutas;
    DateFormat format;
    private LinearLayout etapas_layout;
    private ListView bitacoraListView;
    private LocationUtils locationUtils;
    private LayoutInflater inflater;
    private OportunidadBitacoraDetailFragment subFragment;

    public static AgendaProspectoFragment newInstance(long idAgenda){
        Bundle arguments = new Bundle();
        arguments.putLong(AgendaProspectoFragment.ARG_ITEM_ID, idAgenda);
        AgendaProspectoFragment fragment = new AgendaProspectoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        format1 = new SimpleDateFormat("EEEE dd MMMM yyyy, HH:mm");
        format2 = new SimpleDateFormat("HH:mm");
        format = new SimpleDateFormat("HH:mm");

        locationUtils = new LocationUtils();

        if(getParentFragment()==null)
            setRetainInstance(true);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            idAgenda = getArguments().getLong(ARG_ITEM_ID);
        }else if(savedInstanceState!=null){
            idAgenda = savedInstanceState.getLong(STATE_IDAGENDA);
        }

        if(idAgenda != 0){
            super.setContentView(R.layout.fragment_agenda_prospecto);
        }
        else{
            super.setContentView(R.layout.base_content_no_selected_item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAfterCreateOptionsMenu(Menu menu) {
        if(reDoMenu)
        {
            menuRutas = menu;
            RefreshMenu();
        }

        super.onAfterCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        agenda = AgendaOportunidad.getAgendaById(getDataBase(), idAgenda);

        setTextViewText(R.id.prospecto_descripcion, agenda.getDescripcion());
        setTextViewText(R.id.prospecto_canal, agenda.getOportunidad().getCanal());
        setTextViewText(R.id.prospecto_tipo_cliente, agenda.getOportunidad().getTipoPersona().equalsIgnoreCase("N") ? "Natural" : "Jurídica");


        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
        }
        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_NO_VISITADO)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_unvisited);
        }
        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_PENDIENTE)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_pending);
        }
        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_reprogramed);
        }
        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_VISITADO)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
        }
        setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgenda());

    }



    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {

        inflater = (LayoutInflater) this.getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        //Seteo layouts
        etapas_layout = (LinearLayout) getRootView().findViewById(R.id.prospecto_etapas_content);
        bitacoraListView = (ListView) getRootView().findViewById(R.id.prospecto_bitacora);


        agenda = AgendaOportunidad.getAgendaById(getDataBase(), idAgenda);

        setTextViewText(R.id.prospecto_descripcion, agenda.getDescripcion());
        setTextViewText(R.id.prospecto_canal, agenda.getOportunidad().getCanal());
        setTextViewText(R.id.prospecto_tipo_cliente, agenda.getOportunidad().getTipoPersona().equalsIgnoreCase("N") ? "Natural" : "Jurídica");


        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
        }
        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_NO_VISITADO)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_unvisited);
        }
        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_PENDIENTE)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_pending);
        }
        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_reprogramed);
        }
        if (agenda.getEstado().equalsIgnoreCase(Contants.ESTADO_VISITADO)) {
            ((ImageView) getRootView().findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
        }

        if(agenda != null){

                if(agenda.getEmail() != null && agenda.getEmail().length() > 0) {
                    setTextViewText(R.id.textView_mail, agenda.getEmail());
                    ((TextView) rootView.findViewById(R.id.textView_mail)).setClickable(true);
                    ((TextView) rootView.findViewById(R.id.textView_mail)).setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto",agenda.getEmail(), null));
                            startActivity(Intent.createChooser(intent, "Send Email"));
                        }});
                    ((TextView) rootView.findViewById(R.id.textView_mail)).setPaintFlags(((TextView) rootView.findViewById(R.id.textView_mail)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    ((TextView) rootView.findViewById(R.id.textView_mail)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                }
                else {
                    setTextViewText(R.id.textView_mail, getResources().getString(R.string.label_sin_especificar));
                    ((TextView) rootView.findViewById(R.id.textView_mail)).setClickable(false);
                }

                if(agenda.getDireccion() != null && agenda.getDireccion().length() > 0) {
                    setTextViewText(R.id.textView_movil, agenda.getOportunidad().getTelefono1());
                    ((TextView) rootView.findViewById(R.id.textView_movil)).setClickable(true);
                    ((TextView) rootView.findViewById(R.id.textView_movil)).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            String uri = "tel:" + agenda.getOportunidad().getTelefono1();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(uri));
                            Uri mUri = Uri.parse("smsto:" + Utils.convertToSMSNumber(agenda.getOportunidad().getTelefono1()));
                            Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                            mIntent.putExtra("chat",true);
                            Intent chooserIntent = Intent.createChooser(mIntent, "Seleccionar Acción");
                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent });
                            startActivity(chooserIntent);
                        }});
                    ((TextView) rootView.findViewById(R.id.textView_movil)).setPaintFlags(((TextView) rootView.findViewById(R.id.textView_movil)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    ((TextView) rootView.findViewById(R.id.textView_movil)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                }
                else {
                    setTextViewText(R.id.textView_movil, getResources().getString(R.string.label_sin_especificar));
                    ((TextView) rootView.findViewById(R.id.textView_movil)).setClickable(false);
                }
                setTextViewText(R.id.textView_address, agenda.getDireccion().equalsIgnoreCase("") ? "(Sin Descripción)" : agenda.getDireccion());

            setTextViewText(R.id.textView_fecha, format1.format(agenda.getFechaInicio()) + " - " + format2.format(agenda.getFechaFin()));

            setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgenda());

            //Agregar Etapas
            etapas_layout.removeAllViews();
            for (OportunidadEtapa etp : agenda.getOportunidadEtapas()) {
                if (etp.getEtapa().getIdEtapaPadre() == 0) {
                    View row_etapa = inflater.inflate(R.layout.rowlist_oportunidad_etapa, null);
                    getRootView().findViewById(R.id.detail_agenda_empty_tareas).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.prospecto_etapas_scroll).setVisibility(View.VISIBLE);

                    int id_icon = R.drawable.x_red;
                    if (etp.getEstado().equalsIgnoreCase("R"))
                        id_icon = R.drawable.check;

                    ((TextView) row_etapa.findViewById(R.id.map_phone)).setCompoundDrawablesWithIntrinsicBounds(0, 0, id_icon, 0);
                    ((TextView) row_etapa.findViewById(R.id.detail_agenda_estado)).setText(etp.getEtapa().getDescripcion());
                    ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setText(etp.getEtapa().getOrden() + 1 + "");
                    if (etp.getEtapa().getOrden() == 0) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa1));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();

                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());
                    }
                    if (etp.getEtapa().getOrden() == 1) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa2));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());
                    }
                    if (etp.getEtapa().getOrden() == 2) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa3));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());

                    }
                    if (etp.getEtapa().getOrden() == 3) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa4));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());
                    }
                    if (etp.getEtapa().getOrden() == 4) {
                        ((TextView) row_etapa.findViewById(R.id.detail_tarea_num)).setBackgroundColor(getContext().getResources().getColor(R.color.color_etapa5));
                        final long idOptEtp = etp.getID();
                        final String nameEtp = etp.getEtapa().getDescripcion();
                        Calendar thisDay = Calendar.getInstance();
                        if (etp.getFechaFin().getTime() > 0)
                            thisDay.setTime(etp.getFechaFin());

                        row_etapa.findViewById(R.id.grey_line).setVisibility(View.GONE);
                    }
                    row_etapa.setId(etp.getIdEtapa());

                    row_etapa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), EtapaActivity.class);
                            intent.putExtra(EtapaActivity.ARG_ETAPA, view.getId());
                            intent.putExtra(EtapaActivity.ARG_OPORTUNIDAD, (long) agenda.get_idOportunidad());
                            intent.putExtra(EtapaActivity.ARG_ID_AGENDA, 0);
                            startActivity(intent);
                        }
                    });

                    etapas_layout.addView(row_etapa);
                }
            }

            //Agregar Bitacora
            OportunidadBitacoraAdapter adapter = new OportunidadBitacoraAdapter(this.getContext(), agenda.getOportunidadBitacoras());

            bitacoraListView.setAdapter(adapter);

            bitacoraListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    subFragment = OportunidadBitacoraDetailFragment.newInstance(agenda.get_idOportunidad(), (int) agenda.getOportunidadBitacoras().get(position).getID());
                    showDialogFragment(subFragment, "Bitácora", agenda.getOportunidad().getDescripcion());
                }
            });

            if(agenda.getOportunidadBitacoras().size() > 0)
            {
                getRootView().findViewById(R.id.prospecto_sin_bitacora).setVisibility(View.GONE);
                getRootView().findViewById(R.id.prospecto_bitacora).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        switch (id)
        {
            default:
                break;
        }
    }

    protected boolean ValidarAgendas() {
        if(Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_GESTIONANDO, 0, Agenda.getLastAgenda(getDataBase())) > 0 && !agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
        {
            Toast.makeText(getContext(), "No puede gestionar otra agenda, si existe otra con estado Gestionando.", Toast.LENGTH_LONG).show();
            return false;
        }

        Calendar cal = Calendar.getInstance();
        Calendar cal_agenda = Calendar.getInstance();
        cal_agenda.setTime(agenda.getFechaInicio());
        if(cal.get(Calendar.DAY_OF_MONTH) != cal_agenda.get(Calendar.DAY_OF_MONTH) ||
                cal.get(Calendar.MONTH) != cal_agenda.get(Calendar.MONTH) ||
                cal.get(Calendar.YEAR) != cal_agenda.get(Calendar.YEAR))
        {
            return false;
        }
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_IDAGENDA, idAgenda);
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

    protected void takePicture(final int idView) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this.getActivity());
        myAlertDialog.setTitle("Fotografía");
        myAlertDialog.setMessage("Obtener de");

        myAlertDialog.setPositiveButton("Galería",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.putExtra("return-data", true);
                        getActivity().startActivityForResult(galleryIntent, idView);
                    }
                });

        myAlertDialog.setNegativeButton("Cámara",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
                        getActivity().startActivityForResult(captureIntent, idView);

                    }
                });
        myAlertDialog.show();
    }

    public void showTareaTexto(Actividad ata, AgendaTarea setter)
    {
        Intent intent = new Intent(getContext(), TextoActivity.class);
        intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
        intent.putExtra(ARG_AGENDA_ID, setter.getIdAgenda());
        intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
        intent.putExtra(ActividadActivity.ARG_AGENDA_INT, setter.get_idAgenda());
        intent.putExtra(ActividadActivity.ARG_TAREA, setter.getIdTarea());
        intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
        intent.putExtra(ActividadActivity.ARG_TITULO, setter.getNombreTarea());
        startActivity(intent);
    }

    public void showTareaSeleccion(Actividad ata, AgendaTarea setter)
    {
        Intent intent = new Intent(getContext(), SeleccionActivity.class);
        intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
        intent.putExtra(ARG_AGENDA_ID, setter.getIdAgenda());
        intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
        intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
        intent.putExtra(ActividadActivity.ARG_TITULO, setter.getNombreTarea());
        startActivity(intent);
    }
    public void showTareaMultiSeleccion(Actividad ata, AgendaTarea setter)
    {
        Intent intent = new Intent(getContext(), MultipleActivity.class);
        intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
        intent.putExtra(ARG_AGENDA_ID, setter.getIdAgenda());
        intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
        intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
        intent.putExtra(ActividadActivity.ARG_TITULO, setter.getNombreTarea());
        startActivity(intent);
    }

    public void showTareaCheckbox(Actividad ata, AgendaTarea setter)
    {
        Intent intent = new Intent(getContext(), CheckboxActivity.class);
        intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
        intent.putExtra(ARG_AGENDA_ID, setter.getIdAgenda());
        intent.putExtra(ARG_RUTA_ID, setter.getIdRuta());
        intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
        intent.putExtra(ActividadActivity.ARG_TITULO, setter.getNombreTarea());
        startActivity(intent);
    }

    public void showTareaGrupo(AgendaTarea agt)
    {
        Intent intent = new Intent(getContext(), GrupoActivity.class);
        intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
        intent.putExtra(ARG_AGENDA_ID, agt.getIdAgenda());
        intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
        intent.putExtra(ActividadActivity.ARG_AGENDA_INT, agt.get_idAgenda());
        intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
        intent.putExtra(ActividadActivity.ARG_TITULO, agt.getNombreTarea());
        startActivity(intent);
    }

    public void showTareaActualizacion(AgendaTarea agt)
    {
        if(!clienteNull) {
            Intent intent = new Intent(getContext(), ActualizacionActivity.class);
            intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
            intent.putExtra(ARG_AGENDA_ID, agt.getIdAgenda());
            intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
            intent.putExtra(ActividadActivity.ARG_AGENDA_INT, agenda.getID());
            intent.putExtra(ActividadActivity.ARG_VISTA, soloVista);
            intent.putExtra(ActividadActivity.ARG_TITULO, agt.getNombreTarea());
            startActivity(intent);
        }
        else
            Toast.makeText(this.getContext(), "Cliente esta eliminado de la ruta. No se puede actualizar.", Toast.LENGTH_LONG).show();
    }

    public void RefreshMenu()
    {
        menuRutas.findItem(R.id.action_search_ruta).setVisible(false);
        menuRutas.findItem(R.id.action_crear_visita).setVisible(false);
        Agenda agendaNoClient = Agenda.getAgenda(getDataBase(), idAgenda);
        if(idAgenda != 0) {
            for (int i = 0; i < menuRutas.size(); i++) {
                if (menuRutas.getItem(i).getItemId() == R.id.submenu_agenda) {
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(agendaNoClient != null);
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(agendaNoClient != null);
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_suspender_agenda).setVisible(true);
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(true);
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_crear_agenda).setVisible(true);
                    //if(agenda.getPedido().getID() != 0)
                    //    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setTitle("Editar Pedido");
                }
            }
        }
        if(idAgenda != 0)
        {
            String estado = Agenda.getAgendaEstado(getDataBase(), idAgenda);
            if (estado.equalsIgnoreCase(Contants.ESTADO_NO_VISITADO) || estado.equalsIgnoreCase(Contants.ESTADO_VISITADO)) {
                for (int i = 0; i < menuRutas.size(); i++) {
                    if (menuRutas.getItem(i).getItemId() == R.id.submenu_agenda) {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(false);
                        menuRutas.findItem(R.id.submenu_agenda).setVisible(false);
                        //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                    }
                }
            } else
                menuRutas.findItem(R.id.submenu_agenda).setVisible(true);
            if (!estado.equalsIgnoreCase(Contants.ESTADO_PENDIENTE) && !estado.equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO)) {
                for (int i = 0; i < menuRutas.size(); i++) {
                    if (menuRutas.getItem(i).getItemId() == R.id.submenu_agenda) {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_suspender_agenda).setVisible(false);
                        //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                    }
                }
            }
            if(estado.equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
            {
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                    {
                        //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(true);
                    }
                }
            }
        }
    }
}