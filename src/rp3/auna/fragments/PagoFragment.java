package rp3.auna.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;
import rp3.auna.bean.RegistroPago;
import rp3.auna.bean.SolicitudMovil;
import rp3.auna.util.constants.Constants;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 28/12/2017.
 */

public class PagoFragment extends Fragment {
    private static final String TAG = PagoFragment.class.getSimpleName();
    private RegistroPago registroPago;
    @BindView(R.id.viewtemp)View temp;
    @BindView(R.id.viewdata) View data;
    @BindView(R.id.tvFecha)TextView tvFecha;
    @BindView(R.id.tvMonto)TextView tvMonto;
    @BindView(R.id.tvNumeroOperacion)TextView tvNumeroOperacion;
    //Nueva Variante
    @BindView(R.id.tvFormaPago)TextView tvFormaPago;
    @BindView(R.id.lyFechaVenta)View lyFormaPago;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        View view = inflater.inflate(R.layout.fragment_pago_visita,container,false);
        ButterKnife.bind(this,view);
        try{
            if(visualize()){
                temp.setVisibility(View.GONE);
                data.setVisibility(View.VISIBLE);
                setData();
            }else{
                temp.setVisibility(View.VISIBLE);
                data.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private boolean visualize(){
        if(getArguments()==null){
            return false;
        }
        return true;
    }

    private void setData(){
        String formaPago = getArguments().getString("FormaPago");
        if(formaPago.equalsIgnoreCase("1")){
            //En Linea
            lyFormaPago.setVisibility(View.VISIBLE);
            tvFormaPago.setText("En Línea");
            if(registroPago.getFechaPago()>0){
                SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date date = Convert.getDateFromDotNetTicks(registroPago.getFechaPago());
                tvFecha.setText(dateFormat.format(date));
            }else{
                tvFecha.setText("");
            }
            if(registroPago.getMonto()>0){
                NumberFormat numberFormat;
                Locale locale = new Locale("es", "pe");
                numberFormat = NumberFormat.getInstance(locale);
                //numberFormat.setMaximumFractionDigits(4);
                numberFormat.setMinimumFractionDigits(2);
                tvMonto.setText(String.valueOf(numberFormat.format(registroPago.getMonto())));
            }else{
                tvMonto.setText("");
            }
            if(registroPago.getNumeroOperacion()!=null){
                tvNumeroOperacion.setText(registroPago.getNumeroOperacion());
            }else{
                tvNumeroOperacion.setText("");
            }
        }else if(formaPago.equalsIgnoreCase("2")){
            //Efectivo
            lyFormaPago.setVisibility(View.GONE);
            tvFormaPago.setText("Efectivo");
        }else{
            //Regular
            lyFormaPago.setVisibility(View.GONE);
            tvFormaPago.setText("Regular");
        }

    }

    //region Instance

    public static PagoFragment newInstance(RegistroPago list,Bundle todo){
        PagoFragment dialog = new PagoFragment();
        dialog.setArguments(todo);
        dialog.setPago(list);
        return dialog;
    }

    private void setPago(RegistroPago list){
        this.registroPago = list;
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
