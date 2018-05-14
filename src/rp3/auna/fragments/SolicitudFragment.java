package rp3.auna.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.bean.CotizacionVisita;
import rp3.auna.bean.SolicitudAfiliadoMovil;
import rp3.auna.bean.SolicitudMovil;
import rp3.auna.utils.Utils;
import rp3.data.models.GeneralValue;

/**
 * Created by Jesus Villa on 28/12/2017.
 */

public class SolicitudFragment extends Fragment {
    private static final String TAG = SolicitudFragment.class.getSimpleName();
    private SolicitudMovil solicitudMovil;
    private SolicitudAfiliadoMovil solicitudAfiliadoMovil;
    @BindView(R.id.viewdata)View data;
    @BindView(R.id.viewtemp)View temp;
    //region Views
    @BindView(R.id.tvTipoVentaSolicitud)TextView tvTipoVenta;
    @BindView(R.id.tvPrograma)TextView tvPrograma;
    @BindView(R.id.Solicitud)TextView tvSolicitud;
    @BindView(R.id.tvDocumento)TextView tvDocumento;
    @BindView(R.id.tvContratante)TextView tvContratante;
    @BindView(R.id.tvTelefono)TextView tvTelefono;
    @BindView(R.id.tvCelular)TextView tvCelular;
    @BindView(R.id.tvCorreo)TextView tvCorreo;
    @BindView(R.id.tvFormaPago)TextView tvFormaPago;
    //endregion

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        View view = inflater.inflate(R.layout.fragment_solicitud_visita,container,false);
        ButterKnife.bind(this,view);
        try{
            if(visualize()){
                data.setVisibility(View.VISIBLE);
                temp.setVisibility(View.GONE);
                setData();
            }else{
                data.setVisibility(View.GONE);
                temp.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private boolean visualize(){
        if(solicitudMovil!=null){
            return true;
        }
        return false;
    }

    private void setData(){
        if(solicitudMovil.getAfiliados()!=null){
            if(solicitudMovil.getAfiliados().size()>0){
                for (SolicitudAfiliadoMovil solicitudAfiliadoMovil:solicitudMovil.getAfiliados()){
                    if(solicitudAfiliadoMovil.getIdTitular().equalsIgnoreCase(Contants.GENERAL_VALUE_SOLICITUD_VALIDAR_TITULAR_SI)){
                        this.solicitudAfiliadoMovil = solicitudAfiliadoMovil;
                        break;
                    }
                }
            }
        }
        if(solicitudAfiliadoMovil!=null){
            if(solicitudAfiliadoMovil.getNombre()!=null && solicitudAfiliadoMovil.getApellidoPaterno()!=null){
                tvContratante.setText(solicitudAfiliadoMovil.getNombre()+" "+solicitudAfiliadoMovil.getApellidoPaterno());
            }else{
                tvContratante.setText("-----");
            }
            if(solicitudAfiliadoMovil.getTelefono()!=null){
                tvTelefono.setText(solicitudAfiliadoMovil.getTelefono());
            }else{
                tvTelefono.setText("-----");
            }
            if(solicitudAfiliadoMovil.getCelular()!=null){
                tvCelular.setText(solicitudAfiliadoMovil.getCelular());
            }else{
                tvCelular.setText("-----");
            }
            if (solicitudAfiliadoMovil.getCorreo()!=null){
                tvCorreo.setText(solicitudAfiliadoMovil.getCorreo());
            }else{
                tvCorreo.setText("-----");
            }
            if (solicitudMovil.getModoTarifa()!=null){
                if (solicitudMovil.getModoTarifa().equalsIgnoreCase("NR"))
                {
                    tvFormaPago.setText("NO RECURRENTE");
                }
                else if (solicitudMovil.getModoTarifa().equalsIgnoreCase("RE"))
                {
                    tvFormaPago.setText("RECURRENTE");
                }
                else
                {
                    tvFormaPago.setText("----");
                }
            }else{
                tvFormaPago.setText("-----");
            }
        }else{
            tvContratante.setText("-----");
            tvTelefono.setText("-----");
            tvCelular.setText("-----");
            tvCorreo.setText("-----");
            tvFormaPago.setText("-----");
        }
        if(solicitudMovil.getIdTipoSolicitud().equalsIgnoreCase("N")){
            tvTipoVenta.setText("INDIVIDUAL");
        }else if(solicitudMovil.getIdTipoSolicitud().equalsIgnoreCase("J")){
            tvTipoVenta.setText("JURIDICO");
        }else {
            tvTipoVenta.setText("-----");
        }
        if(solicitudMovil.getIdPrograma()!=null){
            if(solicitudMovil.getIdPrograma()>0){
                List<GeneralValue> list = GeneralValue.getGeneralValues(Utils.getDataBase(getActivity()),Contants.GENERAL_TABLE_PROGRAMAS);
                List<GeneralValue> generalValueList = new ArrayList<>();
                for(GeneralValue obj:list){
                    if(obj.getReference1()!=null){
                        if(obj.getReference1().equalsIgnoreCase("A")){
                            generalValueList.add(obj);
                        }
                    }
                }

                for (GeneralValue obj:generalValueList){
                    if(obj.getCode().equalsIgnoreCase(String.valueOf(solicitudMovil.getIdPrograma()))){
                        tvPrograma.setText(obj.getValue());
                        break;
                    }
                }
            }
            else {
                tvPrograma.setText("-----");
            }
        }else {
            tvPrograma.setText("-----");
        }

        if(solicitudMovil.getNumeroSolicitud()!=null){
            tvSolicitud.setText(solicitudMovil.getNumeroSolicitud());
        }else {
            tvSolicitud.setText("-----");
        }
        /*if(solicitudMovil.){
            tvDocumento
        }else {
            tvDocumento.setText("-----");
        }*/
    }

    //region Instance

    public static SolicitudFragment newInstance(SolicitudMovil list){
        SolicitudFragment dialog = new SolicitudFragment();
        dialog.setSolicitud(list);
        return dialog;
    }

    private void setSolicitud(SolicitudMovil list){
        this.solicitudMovil = list;
    }

    //endregion

    //region Ciclo de vida

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    //endregion
}
