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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.util.helper.Util;
import rp3.auna.utils.Utils;
import rp3.data.models.GeneralValue;

import static rp3.auna.Contants.GENERAL_TABLE_ESTADOS_LLAMADA;

/**
 * Created by Jesus Villa on 17/10/2017.
 */

public class AgendaLlamadaAdapter extends  RecyclerView.Adapter<AgendaLlamadaAdapter.AgendaVtaViewHolder> {

private static final String TAG=AgendaLlamadaAdapter.class.getSimpleName();

private List<LlamadaVta> _list;
private Context _context;
private CallbackVer callbackVer;
private CallbackAction callbackAction;
public int mSelectedItem = -1;
    private List<GeneralValue> generalValueList;


public AgendaLlamadaAdapter(Context context, CallbackVer callBack1, CallbackAction callback2) {
        this._context = context;
        this._list = new ArrayList<>();
        this.callbackVer=callBack1;
        this.callbackAction=callback2;
    generalValueList = GeneralValue.getGeneralValues(Utils.getDataBase(context),Contants.GENERAL_TABLE_DURACION_VISITA);
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
    public AgendaLlamadaAdapter.AgendaVtaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AgendaLlamadaAdapter.AgendaVtaViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendavta, parent, false);
            vh = new AgendaLlamadaAdapter.AgendaVtaViewHolder(v);
            return vh;
            }



    @Override
    public void onBindViewHolder(AgendaLlamadaAdapter.AgendaVtaViewHolder viewHolder, int position) {
        LlamadaVta item = _list.get(position);
        if(item!=null){
            SimpleDateFormat format = new SimpleDateFormat(Contants.DATE_TIME_FORMAT);
            SimpleDateFormat format2 = new SimpleDateFormat(Contants.DATE_FORMAT);
            SimpleDateFormat format1 = new SimpleDateFormat(Contants.DATE_TIME_FORMAT_HH_MM);
            SimpleDateFormat time = new SimpleDateFormat(Contants.DATE_TIME_FORMAT_TIME);
            Log.d(TAG,"Llamada...");
            Drawable drawable = _context.getResources().getDrawable(R.drawable.ic_phone);
            drawable.mutate();
            viewHolder.ivIcono.setImageDrawable(drawable);
            Log.d(TAG,"LlamadaTabla:"+GENERAL_TABLE_ESTADOS_LLAMADA+ " "+item.getLlamadaTabla());
            Log.d(TAG,"LlamadaValue:"+item.getLlamadoValue());
            if(item.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE)){
                Drawable drawable1 = _context.getResources().getDrawable(R.drawable.timeline4);
                drawable.mutate();
                viewHolder.ivEstado.setImageDrawable(drawable1);
                viewHolder.tvOrigen.setText("Pendiente");
            }
            else if(item.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_REPROGRAMADA)){
                Drawable drawable2 = _context.getResources().getDrawable(R.drawable.timeline2);
                drawable.mutate();
                viewHolder.ivEstado.setImageDrawable(drawable2);
                viewHolder.tvOrigen.setText("Reprogramada");
            }
            else if(item.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_CANCELADA)){
                Drawable drawable3 = _context.getResources().getDrawable(R.drawable.timeline3);
                drawable.mutate();
                viewHolder.ivEstado.setImageDrawable(drawable3);
                viewHolder.tvOrigen.setText("Cancelada");
            }
            else if(item.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA)){
                Drawable drawable4 = _context.getResources().getDrawable(R.drawable.timeline5);
                drawable.mutate();
                viewHolder.ivEstado.setImageDrawable(drawable4);
                viewHolder.tvDuracionReal.setVisibility(View.VISIBLE);
                viewHolder.tvFechaInicio.setVisibility(View.VISIBLE);
                viewHolder.tvFechaFin.setVisibility(View.VISIBLE);
                viewHolder.tvOrigen.setText("Realizada");
                int seconds = item.getDuracion();
                String hms = Util.convertSecondToHMmSsDuration(seconds);
                System.out.println(hms);
                Log.d(TAG,"VerDuracion:"+hms);

                Log.d(TAG,"Duracion de llamda:"+seconds);
                /*int minutes = (seconds % 3600) / 60;
                seconds = seconds % 60;
                String minutos;
                String segundos;
                if(minutes<10){
                    minutos = "0"+minutes;
                }else{
                    minutos = String.valueOf(minutes);
                }
                if(seconds<10){
                    segundos = "0"+seconds;
                }else{
                    segundos = String.valueOf(seconds);
                }*/
                //viewHolder.tvDuracionReal.setText("Duracion: "+minutos+":"+segundos+"min");
                viewHolder.tvDuracionReal.setText("Duración llamada: "+hms);
                viewHolder.tvFechaInicio.setText("Ini Gestión: "+time.format(item.getFechaInicioLlamada()));
                viewHolder.tvFechaFin.setText("Fin Gestión: "+time.format(item.getFechaFinLlamada()));
            }
            else if(item.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA)){
                Drawable drawable5 = _context.getResources().getDrawable(R.drawable.timeline1);
                drawable.mutate();
                viewHolder.ivEstado.setImageDrawable(drawable5);
                viewHolder.tvOrigen.setText("No realizada");
            }

            //Si esta no esta en servidor es probable que pueda ser de un prospecto en sqlite o servidor
            if(item.getInsertado()==2){
                int idProspecto = item.getIdCliente();
                List<ProspectoVtaDb> prospectoVtaDb = ProspectoVtaDb.getProspectosActualizados(Utils.getDataBase(_context));
                for (ProspectoVtaDb obj:prospectoVtaDb) {
                    if(obj.getIdProspecto() == idProspecto){
                        viewHolder.tvProspecto.setText(obj.getNombre());
                        break;
                    }
                }
            }
            //Si este esta en servidor todos los prospectos tambien lo estan...
            else{
                int idProspecto = item.getIdCliente();
                List<ProspectoVtaDb> prospectoVtaDb = ProspectoVtaDb.getAllEstado(Utils.getDataBase(_context));
                for (ProspectoVtaDb obj:prospectoVtaDb) {
                    if(obj.getEstado()==1){
                        if(obj.getID() == idProspecto){
                            viewHolder.tvProspecto.setText(obj.getNombre());
                            break;
                        }
                    }else{
                        if(obj.getIdProspecto() == idProspecto){
                            viewHolder.tvProspecto.setText(obj.getNombre());
                            break;
                        }
                    }
                }
            }
            viewHolder.tvFecha.setText("Prog: "+format1.format(item.getFechaLlamada()));
            viewHolder.tvHoraUbicacion.setVisibility(View.GONE);
            //viewHolder.tvHoraUbicacion.setText("Hora: "+format1.format(item.getFechaLlamada()));
        }
    }


    public interface CallbackVer{
        void agendaSelected(LlamadaVta agendaVta, int position);
    }

    public interface CallbackAction {
        void agendaActionSelected(LlamadaVta agendaVta, int position,View v);
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public void addMoreItems(List<LlamadaVta> moreItems) {
        clear();
        _list.addAll(moreItems);
        notifyDataSetChanged();
    }

    public void clear() {
        _list.clear();
        notifyDataSetChanged();
    }

    public class AgendaVtaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewItemTitle) TextView tvProspecto;
        @BindView(R.id.imageViewImage) ImageView ivIcono;
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

    public void filterToDate(Date date,List<LlamadaVta> moreItems){
        clear();
        _list.addAll(moreItems);
        notifyDataSetChanged();
    }

}