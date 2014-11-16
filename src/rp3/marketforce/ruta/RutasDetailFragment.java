package rp3.marketforce.ruta;

import java.util.Calendar;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.actividades.CheckboxActivity;
import rp3.marketforce.actividades.GrupoActivity;
import rp3.marketforce.actividades.MultipleActivity;
import rp3.marketforce.actividades.SeleccionActivity;
import rp3.marketforce.actividades.TextoActivity;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.sync.SyncAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class RutasDetailFragment extends rp3.app.BaseFragment {
    
    public static final String ARG_ITEM_ID = "idagenda";
    public static final String ARG_AGENDA_ID = "agenda";
    public static final String ARG_RUTA_ID = "ruta";

    public static final String PARENT_SOURCE_LIST = "LIST";
    public static final String PARENT_SOURCE_SEARCH = "SEARCH";
    
    public static final String STATE_IDAGENDA = "state_idagenda";
    
    private long idAgenda;        
    private Agenda agenda;
    private ListaTareasAdapter adapter;
    private ListView lista_tarea;

    public interface TransactionDetailListener{
    	public void onDeleteSuccess(Cliente transaction);
    }
    
    public static RutasDetailFragment newInstance(long idAgenda){
    	Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, idAgenda);
        RutasDetailFragment fragment = new RutasDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
      

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                

        if(getParentFragment()==null)
        	setRetainInstance(true);
        
        if (getArguments().containsKey(ARG_ITEM_ID)) {            
            idAgenda = getArguments().getLong(ARG_ITEM_ID);   
        }else if(savedInstanceState!=null){
        	idAgenda = savedInstanceState.getLong(STATE_IDAGENDA);
        }    
        
        if(idAgenda != 0){        	
        	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
        }
        
        if(agenda != null){
        	super.setContentView(R.layout.fragment_rutas_detalle);
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
    public void onResume() {
    	super.onResume();
    	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
    	adapter = new ListaTareasAdapter(getActivity(), agenda.getAgendaTareas());
    	lista_tarea.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    }
      
    
	@Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {    	
    	 
		if(agenda != null){			
		   setImageViewBitmapFromInternalStorageAsync(R.id.imageView1, agenda.getCliente().getFotoFileName());
		   setTextViewText(R.id.textView_name, agenda.getNombreCompleto());
		   setTextViewText(R.id.textView_movil, agenda.getClienteDireccion().getTelefono1());
		   setTextViewText(R.id.textView_mail, agenda.getCliente().getCorreoElectronico());
		   setTextViewText(R.id.textView_address, agenda.getClienteDireccion().getDireccion());
		   setTextViewDateText(R.id.textView_fecha, agenda.getFechaInicio());		
		   setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
		   
		   setButtonClickListener(R.id.detail_agenda_button_iniciar, new OnClickListener(){

			@Override
			public void onClick(View v) {
				setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_fin, View.VISIBLE);
				setViewVisibility(R.id.detail_agenda_button_cancelar, View.VISIBLE);
				agenda.setEstadoAgenda(Contants.ESTADO_GESTIONANDO);
				agenda.setEstadoAgendaDescripcion(Contants.DESC_GESTIONANDO);
				agenda.setFechaInicioReal(Calendar.getInstance().getTime());
				Agenda.update(getDataBase(), agenda);
				((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
				 setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
				
			}});
		   
		   setButtonClickListener(R.id.detail_agenda_button_cancelar, new OnClickListener(){

				@Override
				public void onClick(View v) {
					setViewVisibility(R.id.detail_agenda_button_iniciar, View.VISIBLE);
					setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
					setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
					agenda.setEstadoAgenda(Contants.ESTADO_PENDIENTE);
					agenda.setEstadoAgendaDescripcion(Contants.DESC_PENDIENTE);
					Agenda.update(getDataBase(), agenda);
					((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_pending);
					 setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());
				}});
		   
		   setButtonClickListener(R.id.detail_agenda_button_fin, new OnClickListener(){

				@Override
				public void onClick(View v) {
					setViewVisibility(R.id.detail_agenda_button_iniciar, View.VISIBLE);
					setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
					setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
					agenda.setEstadoAgenda(Contants.ESTADO_VISITADO);
					agenda.setEstadoAgendaDescripcion(Contants.DESC_VISITADO);
					agenda.setFechaFinReal(Calendar.getInstance().getTime());
					//agenda.setEnviado(true);
					Agenda.update(getDataBase(), agenda);
					((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
					setTextViewText(R.id.detail_agenda_estado, agenda.getEstadoAgendaDescripcion());	
					Bundle bundle = new Bundle();
					bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ENVIAR_AGENDA);
					bundle.putInt(ARG_AGENDA_ID, (int) idAgenda);
					requestSync(bundle);
				}});
		   
		   if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
		   {
				((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_in_process);
				setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_fin, View.VISIBLE);
				setViewVisibility(R.id.detail_agenda_button_cancelar, View.VISIBLE);
		   }
			if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_NO_VISITADO))
			{
				((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_unvisited);
				setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
			}
			if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_PENDIENTE))
				((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_pending);
			if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
				((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_reprogramed);
			if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_VISITADO))
			{
				((ImageView) rootView.findViewById(R.id.detail_agenda_image_status)).setImageResource(R.drawable.circle_visited);
				setViewVisibility(R.id.detail_agenda_button_iniciar, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_fin, View.GONE);
				setViewVisibility(R.id.detail_agenda_button_cancelar, View.GONE);
			}
		   
		   if(agenda.getAgendaTareas() != null){
			   adapter = new ListaTareasAdapter(getActivity(), agenda.getAgendaTareas());
			   lista_tarea = (ListView) rootView.findViewById(R.id.listView_tareas);
			   lista_tarea.setAdapter(adapter);
			   lista_tarea.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(agenda.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
					{
						AgendaTarea setter = adapter.getItem(position);
						if(setter.getTipoTarea().equalsIgnoreCase("A") || setter.getTipoTarea().equalsIgnoreCase("R"))
						{
							AgendaTareaActividades ata = AgendaTareaActividades.getActividadSimple(getDataBase(), setter.getIdRuta(), setter.getIdAgenda(), setter.getIdTarea());
							if(ata.getTipo() != null)
							{
								if(ata.getTipo().equalsIgnoreCase("C"))
									showTareaCheckbox(ata);	
								if(ata.getTipo().equalsIgnoreCase("M"))
									showTareaMultiSeleccion(ata);
								if(ata.getTipo().equalsIgnoreCase("S"))
									showTareaSeleccion(ata);
								if(ata.getTipo().equalsIgnoreCase("T"))
									showTareaTexto(ata);
							}
						}
						if(setter.getTipoTarea().equalsIgnoreCase("E"))
							showTareaGrupo(setter);	
					}
				}});
		   }
		   
		}
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
	
	public void showTareaTexto(AgendaTareaActividades agt)
	{
		Intent intent = new Intent(getContext(), TextoActivity.class);
		intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID,(long) agt.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
		startActivity(intent);
	}
	
	public void showTareaSeleccion(AgendaTareaActividades agt)
	{
		Intent intent = new Intent(getContext(), SeleccionActivity.class);
		intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, agt.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
		startActivity(intent);
	}
	public void showTareaMultiSeleccion(AgendaTareaActividades agt)
	{
		Intent intent = new Intent(getContext(), MultipleActivity.class);
		intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, agt.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
		startActivity(intent);
	}
    
	public void showTareaCheckbox(AgendaTareaActividades agt)
	{
		Intent intent = new Intent(getContext(), CheckboxActivity.class);
		intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, agt.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
		startActivity(intent);
	}
	
	public void showTareaGrupo(AgendaTarea agt)
	{
		Intent intent = new Intent(getContext(), GrupoActivity.class);
		intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
		intent.putExtra(ARG_AGENDA_ID, agt.getIdAgenda());
		intent.putExtra(ARG_RUTA_ID, agt.getIdRuta());
		startActivity(intent);
	}
}
