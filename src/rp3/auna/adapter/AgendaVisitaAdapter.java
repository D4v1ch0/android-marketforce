package rp3.auna.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.util.helper.Util;
import rp3.auna.utils.Utils;
import rp3.data.models.GeneralValue;

import static rp3.auna.Contants.GENERAL_TABLE_ESTADOS_LLAMADA;

/**
 * Created by Jesus Villa on 03/12/2017.
 */

public class AgendaVisitaAdapter extends  RecyclerView.Adapter<AgendaVisitaAdapter.AgendaVtaViewHolder> {

    private static final String TAG=AgendaLlamadaAdapter.class.getSimpleName();

    private List<VisitaVta> _list;
    private Context _context;
    private AgendaVisitaAdapter.CallbackVer callbackVer;
    private AgendaVisitaAdapter.CallbackAction callbackAction;
    public int mSelectedItem = -1;
    private List<GeneralValue> generalValueList;


    public AgendaVisitaAdapter(Context context, AgendaVisitaAdapter.CallbackVer callBack1, AgendaVisitaAdapter.CallbackAction callback2) {
        this._context = context;
        this._list = new ArrayList<>();
        this.callbackVer=callBack1;
        this.callbackAction=callback2;
        generalValueList = GeneralValue.getGeneralValues(Utils.getDataBase(context), Contants.GENERAL_TABLE_DURACION_VISITA);
        for(int i = 0;i<generalValueList.size();i++){
            if(generalValueList.get(i)!=null){
                if(generalValueList.get(i).getReference3()!=null){
                    if(generalValueList.get(i).getReference3().equalsIgnoreCase("0")){
                        generalValueList.remove(i);
                        break;
                    }
                }
            }
        }
        Log.d(TAG,"Cantidad de General Duraciones:"+generalValueList.size());
    }

    @Override
    public AgendaVisitaAdapter.AgendaVtaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AgendaVisitaAdapter.AgendaVtaViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendavta, parent, false);
        vh = new AgendaVisitaAdapter.AgendaVtaViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(AgendaVisitaAdapter.AgendaVtaViewHolder viewHolder, int position) {
        VisitaVta visitaVta = _list.get(position);
        SimpleDateFormat format = new SimpleDateFormat(Contants.DATE_TIME_FORMAT);
        SimpleDateFormat format2 = new SimpleDateFormat(Contants.DATE_FORMAT);
        SimpleDateFormat format1 = new SimpleDateFormat(Contants.DATE_TIME_FORMAT_HH_MM);
        SimpleDateFormat time = new SimpleDateFormat(Contants.DATE_TIME_FORMAT_TIME);
        Log.d(TAG,"Visita...");
        Drawable drawable = _context.getResources().getDrawable(R.drawable.ic_action_agenda);
        drawable.mutate();
        viewHolder.ivIcono.setImageDrawable(drawable);
        if(visitaVta.getVisitaValue().equalsIgnoreCase( Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE)){
            viewHolder.ivEstado.setImageDrawable(_context.getResources().getDrawable(R.drawable.timeline4));
            viewHolder.tvOrigen.setText("Pendiente");
        }
        else if(visitaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_CANCELADA)){
            viewHolder.ivEstado.setImageDrawable(_context.getResources().getDrawable(R.drawable.timeline3));
            viewHolder.tvOrigen.setText("Cancelada");
        }
        else if(visitaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_REPROGRAMADA)){
            viewHolder.ivEstado.setImageDrawable(_context.getResources().getDrawable(R.drawable.timeline2));
            viewHolder.tvOrigen.setText("Reprogramada");
        }
        else if(visitaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA)){
            viewHolder.ivEstado.setImageDrawable(_context.getResources().getDrawable(R.drawable.timeline5));
            viewHolder.tvDuracionReal.setVisibility(View.VISIBLE);
            viewHolder.tvFechaInicio.setVisibility(View.VISIBLE);
            viewHolder.tvFechaFin.setVisibility(View.VISIBLE);
            viewHolder.tvDuracionEstimada.setVisibility(View.VISIBLE);
            viewHolder.tvFechaInicio.setText("Inicio: "+time.format(visitaVta.getFechaInicio()));
            viewHolder.tvFechaFin.setText("Finalizo: "+time.format(visitaVta.getFechaFin()));
            viewHolder.tvOrigen.setText("Realizada");
            viewHolder.tvDuracionEstimada.setVisibility(View.GONE);
            //Duracion Estimada
            /*for(GeneralValue o:generalValueList){
                if(visitaVta.getDuracionCode().equalsIgnoreCase(o.getCode())){
                    viewHolder.tvDuracionEstimada.setText("Estimado: "+o.getValue());
                    break;
                }
            }*/
            //Duracion Real
                //viewHolder.tvDuracionReal.setText(Util.printDifference(visitaVta.getFechaInicio(),visitaVta.getFechaFin()));
            Calendar t1 = Calendar.getInstance();
            Calendar t2 = Calendar.getInstance();
            t1.setTime(visitaVta.getFechaFin());
            t2.setTime(visitaVta.getFechaInicio());
            long timeDiferrence = (t1.getTimeInMillis() - t2.getTimeInMillis());
            Date date = new Date(timeDiferrence);
            //viewHolder.tvDuracionReal.setText("Duracion: "+time.format(date));
            viewHolder.tvDuracionReal.setText("Duracion: "+ Util.convertMillisToHMmSsDuration(timeDiferrence));
        }
        else if(visitaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_NO_REALIZADA)){
            viewHolder.ivEstado.setImageDrawable(_context.getResources().getDrawable(R.drawable.timeline1));
            viewHolder.tvOrigen.setText("No realizada");
        }
        //Verificar si tiene un idProspecto en sqlite o de servidor
        if(visitaVta.getIdVisita()==0){
            int idProspecto = visitaVta.getIdCliente();
            List<ProspectoVtaDb> prospectoVtaDb = ProspectoVtaDb.getAllEstado(Utils.getDataBase(_context));
            for (ProspectoVtaDb obj:prospectoVtaDb) {
                if(obj.getEstado()==1){
                    if(obj.getID() == idProspecto){
                        viewHolder.tvProspecto.setText("Prospecto: "+obj.getNombre());
                        break;
                    }
                }else{
                    if(obj.getIdProspecto() == idProspecto){
                        viewHolder.tvProspecto.setText("Prospecto: "+obj.getNombre());
                        break;
                    }
                }
            }
        }
        //Si esta en servidor porque se actualizo tokio
        else{
            int idProspecto = visitaVta.getIdCliente();
            List<ProspectoVtaDb> prospectoVtaDb = ProspectoVtaDb.getAllEstado(Utils.getDataBase(_context));
            for (ProspectoVtaDb obj:prospectoVtaDb) {
                if(obj.getIdProspecto() == idProspecto){
                    viewHolder.tvProspecto.setText("Prospecto: "+obj.getNombre());
                    break;
                }
            }
        }

        viewHolder.tvHoraUbicacion.setText("Prog: "+format1.format(visitaVta.getFechaVisita()));
        viewHolder.tvFecha.setText("Direccion: "+visitaVta.getIdClienteDireccion());
    }

    public interface CallbackVer{
        void agendaSelected(VisitaVta agendaVta, int position);
    }

    public interface CallbackAction {
        void agendaActionSelected(VisitaVta agendaVta, int position,View v);
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public void addMoreItems(List<VisitaVta> moreItems) {
        clear();
        _list.addAll(moreItems);
        notifyDataSetChanged();
    }

    public void clear() {
        _list.clear();
        notifyDataSetChanged();
    }

    public class AgendaVtaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewItemTitle)
        TextView tvProspecto;
        @BindView(R.id.imageViewImage)
        ImageView ivIcono;
        @BindView(R.id.ivEstado) ImageView ivEstado;
        @BindView(R.id.tvFecha) TextView tvFecha;
        @BindView(R.id.tvDireccion) TextView tvHoraUbicacion;
        @BindView(R.id.tvFechaInicio) TextView tvFechaInicio;
        @BindView(R.id.tvFechaFin) TextView tvFechaFin;
        @BindView(R.id.tvDuracionReal) TextView tvDuracionReal;
        @BindView(R.id.tvDuracionEstimada) TextView tvDuracionEstimada;
        @BindView(R.id.tvOrigen) TextView tvOrigen;

        public AgendaVtaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"OnClick..."+_list.get(getAdapterPosition()).toString());
                    callbackVer.agendaSelected(_list.get(getAdapterPosition()),getAdapterPosition());
                }
            };
            View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG,"OnLongClick..."+_list.get(getAdapterPosition()).toString());
                    callbackAction.agendaActionSelected(_list.get(getAdapterPosition()),getAdapterPosition(),tvProspecto);
                    return false;
                }
            };
            itemView.setOnClickListener(clickListener);
            itemView.setOnLongClickListener(longClickListener);
        }
    }

    public void filterToDate(Date date,List<VisitaVta> moreItems){
        clear();
        _list.addAll(moreItems);
        notifyDataSetChanged();
    }

}