package rp3.auna.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.utils.Utils;

/**
 * Created by Jesus Villa on 09/10/2017.
 */

public class ProspectoAdapter extends RecyclerView.Adapter<ProspectoAdapter.ProspectoVtaViewHolder> {

    private static final String TAG=ProspectoAdapter.class.getSimpleName();

    private List<ProspectoVtaDb> _list;
    private Context _context;
    private CallbackVerProspecto callbackVer;
    private CallbackActionProspecto callbackAction;
    public int mSelectedItem = -1;


    public ProspectoAdapter(Context context, CallbackVerProspecto callBack1,CallbackActionProspecto callback2) {
        this._context = context;
        this._list = new ArrayList<>();
        this.callbackVer=callBack1;
        this.callbackAction=callback2;
    }

    @Override
    public ProspectoAdapter.ProspectoVtaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProspectoAdapter.ProspectoVtaViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prospectovta, parent, false);
        vh = new ProspectoAdapter.ProspectoVtaViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(ProspectoAdapter.ProspectoVtaViewHolder viewHolder, int position) {
        ProspectoVtaDb item = _list.get(position);
        Log.d(TAG,item.toString());
        viewHolder.tvNombre.setText(item.getNombre());
        Drawable drawable;
        viewHolder.tvReferido.setVisibility(View.GONE);
        if(item!=null){
            if(item.getOrigenCode()!=null){
                switch (item.getOrigenCode()){
                    case Contants.GENERAL_VALUE_CODE_ORIGEN_P100:
                        drawable = _context.getResources().getDrawable(R.drawable.p100);
                        drawable.mutate();
                        viewHolder.ivProspecto.setImageDrawable(drawable);
                        viewHolder.tvOrigen.setText("Origen: P100");
                        break;
                    case Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF:
                        drawable = _context.getResources().getDrawable(R.drawable.referido);
                        drawable.mutate();
                        viewHolder.ivProspecto.setImageDrawable(drawable);
                        viewHolder.tvOrigen.setText("Origen: Referido");
                        //
                        if(item.getReferente()!=null){
                            if(item.getReferente().length()>0){
                                viewHolder.tvReferido.setVisibility(View.VISIBLE);
                                viewHolder.tvReferido.setText("Referido por: "+item.getReferente());
                            }
                        }

                        //
                        break;
                    case Contants.GENERAL_VALUE_CODE_ORIGEN_MKF_MOVIL:
                        drawable = _context.getResources().getDrawable(R.drawable.referido);
                        drawable.mutate();
                        viewHolder.ivProspecto.setImageDrawable(drawable);
                        viewHolder.tvOrigen.setText("Origen: Propio");
                        break;
                    case Contants.GENERAL_VALUE_CODE_ORIGEN_CAMPAÑAS:
                        drawable = _context.getResources().getDrawable(R.drawable.campanas);
                        drawable.mutate();
                        viewHolder.ivProspecto.setImageDrawable(drawable);
                        viewHolder.tvOrigen.setText("Origen: Campañas");
                        break;
                    case Contants.GENERAL_VALUE_CODE_ORIGEN_MKF_WEB:
                        drawable = _context.getResources().getDrawable(R.drawable.mkf);
                        drawable.mutate();
                        viewHolder.ivProspecto.setImageDrawable(drawable);
                        viewHolder.tvOrigen.setText("Origen: Propio");
                        break;
                    default:
                        drawable = _context.getResources().getDrawable(R.drawable.mkf);
                        drawable.mutate();
                        viewHolder.ivProspecto.setImageDrawable(drawable);
                        viewHolder.tvOrigen.setText("Origen: Propio");
                }

                if(item.getTipoPersonaCode().equalsIgnoreCase("N")){
                    viewHolder.tvEmpresa.setVisibility(View.GONE);
                    //Persona Natural
                    //Telefonos
                    if(TextUtils.isEmpty(item.getCelular()) && TextUtils.isEmpty(item.getTelefono()) ||
                            item.getCelular()==null && item.getTelefono() == null){
                        viewHolder.containerTelefono.setVisibility(View.GONE);
                    }else{
                        viewHolder.containerTelefono.setVisibility(View.VISIBLE);
                        if(!TextUtils.isEmpty(item.getCelular()) && !TextUtils.isEmpty(item.getTelefono())
                                || item.getCelular()!=null && item.getTelefono() != null){
                            viewHolder.tvCelular.setText(item.getCelular());
                            viewHolder.tvTelefono.setText(item.getTelefono());
                        }else{
                            if(!TextUtils.isEmpty(item.getCelular()) || item.getCelular()!=null){
                                viewHolder.tvCelular.setText(item.getCelular());
                                viewHolder.tvTelefono.setVisibility(View.GONE);
                            }
                            else if(!TextUtils.isEmpty(item.getTelefono()) || item.getTelefono()!=null){
                                viewHolder.tvTelefono.setText(item.getTelefono());
                                viewHolder.tvCelular.setVisibility(View.GONE);
                            }
                        }
                    }
                    //Direcciones
                    if(TextUtils.isEmpty(item.getDireccion1()) && TextUtils.isEmpty(item.getDireccion2()) ||
                            item.getDireccion1()==null && item.getDireccion2() == null){
                        viewHolder.containerDirecciones.setVisibility(View.GONE);
                    }else{
                        viewHolder.containerDirecciones.setVisibility(View.VISIBLE);
                        if(!TextUtils.isEmpty(item.getDireccion1()) && !TextUtils.isEmpty(item.getDireccion2())
                                || item.getDireccion1() !=null && item.getDireccion2() !=null){
                            viewHolder.tvDireccion1.setText(item.getDireccion1());
                            viewHolder.tvDireccion2.setText(item.getDireccion2());
                        }else{
                            if(!TextUtils.isEmpty(item.getDireccion1()) || item.getDireccion1()!=null){
                                viewHolder.tvDireccion1.setText(item.getDireccion1());
                                viewHolder.tvDireccion2.setVisibility(View.GONE);
                            }else if(!TextUtils.isEmpty(item.getDireccion2()) || item.getDireccion2()!=null){
                                viewHolder.tvDireccion2.setText(item.getDireccion2());
                                viewHolder.tvDireccion1.setVisibility(View.GONE);
                            }
                        }
                    }

                }else{
                    //Persona Juridica
                    viewHolder.tvEmpresa.setVisibility(View.VISIBLE);
                    viewHolder.tvEmpresa.setText(item.getRazonSocial());
                    //Telefonos
                    if(TextUtils.isEmpty(item.getCelular()) && TextUtils.isEmpty(item.getTelefono()) ||
                            item.getCelular()==null && item.getTelefono() == null){
                        viewHolder.containerTelefono.setVisibility(View.GONE);
                    }else{
                        viewHolder.containerTelefono.setVisibility(View.VISIBLE);
                        if(!TextUtils.isEmpty(item.getCelular()) && !TextUtils.isEmpty(item.getTelefono())
                                || item.getCelular()!=null && item.getTelefono() != null){
                            viewHolder.tvCelular.setText(item.getCelular());
                            viewHolder.tvTelefono.setText(item.getTelefono());
                        }else{
                            if(!TextUtils.isEmpty(item.getCelular()) || item.getCelular()!=null){
                                viewHolder.tvCelular.setText(item.getCelular());
                                viewHolder.tvTelefono.setVisibility(View.GONE);
                            }
                            if(!TextUtils.isEmpty(item.getTelefono()) || item.getTelefono()!=null){
                                viewHolder.tvTelefono.setText(item.getTelefono());
                                viewHolder.tvCelular.setVisibility(View.GONE);
                            }
                        }
                    }
                    //Direcciones
                    if(TextUtils.isEmpty(item.getDireccion1()) && TextUtils.isEmpty(item.getDireccion2()) ||
                            item.getDireccion1()==null && item.getDireccion2() == null){
                        viewHolder.containerDirecciones.setVisibility(View.GONE);
                    }else{
                        viewHolder.containerDirecciones.setVisibility(View.VISIBLE);
                        if(!TextUtils.isEmpty(item.getDireccion1()) && !TextUtils.isEmpty(item.getDireccion2())
                                || item.getDireccion1() !=null && item.getDireccion2() !=null){
                            viewHolder.tvDireccion1.setText(item.getDireccion1());
                            viewHolder.tvDireccion2.setText(item.getDireccion2());
                        }else{
                            if(!TextUtils.isEmpty(item.getDireccion1()) || item.getDireccion1()!=null){
                                viewHolder.tvDireccion1.setText(item.getDireccion1());
                                viewHolder.tvDireccion2.setVisibility(View.GONE);
                            }if(!TextUtils.isEmpty(item.getDireccion2()) || item.getDireccion2()!=null){
                                viewHolder.tvDireccion2.setText(item.getDireccion2());
                                viewHolder.tvDireccion1.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        }
    }


    public interface CallbackVerProspecto {
         void prospectoSelected(ProspectoVtaDb prospectoVtaDb,int position,View view);
    }

    public interface CallbackActionProspecto {
        void prospectoSelected(ProspectoVtaDb prospectoVtaDb,int position,View view);
    }



    @Override
    public int getItemCount() {
        return _list.size();
    }

    public void addMoreItems(List<ProspectoVtaDb> moreItems) {
        clear();
        _list.addAll(moreItems);
        notifyDataSetChanged();
    }

    public void clear() {
        _list.clear();
        notifyDataSetChanged();
    }

    public class ProspectoVtaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewItemTitle) TextView tvNombre;
        @BindView(R.id.linearTelefonosProspecto) LinearLayout containerTelefono;
        @BindView(R.id.linearDireccionesProspecto) LinearLayout containerDirecciones;
        @BindView(R.id.tvCelular) TextView tvCelular;
        @BindView(R.id.tvTelefono) TextView tvTelefono;
        @BindView(R.id.tvDireccion1) TextView tvDireccion1;
        @BindView(R.id.tvDireccion2) TextView tvDireccion2;
        @BindView(R.id.tvOrigen) TextView tvOrigen;
        @BindView(R.id.tvEmpresa) TextView tvEmpresa;
        @BindView(R.id.imageViewImage) ImageView ivProspecto;
        @BindView(R.id.tvProspectoReferido) TextView tvReferido;

        public ProspectoVtaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"OnClick..."+_list.get(getAdapterPosition()).toString());
                    callbackVer.prospectoSelected(_list.get(getAdapterPosition()),getAdapterPosition(),v);
                }
            };
            View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG,"OnLongClick..."+_list.get(getAdapterPosition()).toString());
                    callbackAction.prospectoSelected(_list.get(getAdapterPosition()),getAdapterPosition(),v);
                    return false;
                }
            };
            itemView.setOnClickListener(clickListener);
            itemView.setOnLongClickListener(longClickListener);
        }

    }


    /**
     * Searching PDV
     */
    public void filter(ArrayList<ProspectoVtaDb> newList){
        _list=new ArrayList<>();
        _list.addAll(newList);
        notifyDataSetChanged();
    }

}