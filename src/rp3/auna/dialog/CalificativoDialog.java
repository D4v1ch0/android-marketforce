package rp3.auna.dialog;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;
import rp3.auna.adapter.GeneralValueAdapter;
import rp3.auna.bean.InVentasProspecto;
import rp3.auna.bean.InVentasProspectoAfiliado;
import rp3.auna.customviews.DividerItemDecoration;

/**
 * Created by Jesus Villa on 11/12/2017.
 */

public class CalificativoDialog extends DialogFragment {
    private static final String TAG = CalificativoDialog.class.getSimpleName();
    @BindView(R.id.lyCerrar) View tvCerrar;
    //region Views Afiliado
    @BindView(R.id.tvDocumentoAfiliado)TextView tvDocumentoAfiliado;
    @BindView(R.id.tvPlanAfiliado)TextView tvPlanAfiliado;
    @BindView(R.id.tvTitularAfiliado)TextView tvTitularAfiliado;
    @BindView(R.id.tvGFAfiliado)TextView tvGFAfiliado;
    @BindView(R.id.tvRelacionOncosaludAfiliado)TextView getTvRelacionOncosaludAfiliado;
    //endregion

    //region Views Familia
    @BindView(R.id.tvSegmentoStar)TextView tvSegmentoStar;
    @BindView(R.id.tvCantidadHijos)TextView tvCantidadHijos;
    //Temporal SIN ONCOSALUD
    @BindView(R.id.tvRelacionOncosalud)TextView tvRelacionOncosalud;
    @BindView(R.id.iv1)ImageView iv1;
    @BindView(R.id.iv2)ImageView iv2;
    @BindView(R.id.iv3)ImageView iv3;
    @BindView(R.id.iv4)ImageView iv4;
    @BindView(R.id.iv5)ImageView iv5;
    @BindView(R.id.iv6)ImageView iv6;
    @BindView(R.id.tvBancarizado)TextView tvBancarizado;
    @BindView(R.id.tvsbs)TextView tvsbs;
    @BindView(R.id.tvtdc)TextView tvtdc;
    @BindView(R.id.tvcantidadtdc)TextView tvcantidadtdc;
    @BindView(R.id.tvcantidadbanco)TextView tvcantidadbanco;
    @BindView(R.id.tvdislineatdc)TextView tvdislineatdc;
    @BindView(R.id.tvdismayorlineatdc)TextView tvdismayorlineatdc;
    @BindView(R.id.tvRangoPromedioIngreso)TextView tvRangoPromedioIngreso;
    @BindView(R.id.tvDependencia)TextView tvDependencia;
    //endregion

    //region View Containers
    @BindView(R.id.lyContentEmpty)View viewEmpty;
    @BindView(R.id.lySegmentoContent)View viewSegmento;
    @BindView(R.id.viewSeparatorAwithStar)View viewAwithStar;
    @BindView(R.id.lyStarsContent)View viewStarsConten;
    @BindView(R.id.viewSeparatorStarwithHijo)View viewSeparatorStarwithHijo;
    @BindView(R.id.lyHijosContent)View viewHijosContent;
    @BindView(R.id.viewSeparatorHijowithFamiliar)View vieSeparatorHijowithFamiliar;
    @BindView(R.id.lyFamiliarContent)View viewFamiliarContent;
    @BindView(R.id.viewSeparatorFamiliarwithAfiliado)View viewSeparatorFamiliarwithAfiliado;
    @BindView(R.id.lyAfiliadoContent)View viewAfiliadoContent;
    @BindView(R.id.lyDataContent)View viewDataContent;
    @BindView(R.id.viewSeparatorFinancierawithLaboral)View viewSeparatorFinancierawithLaboral;
    //endregion

    private callbackOpcionSelected listener;
    private InVentasProspecto data;

    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_calificativo, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        init();
        configureAcceptCancelButton();

        return builder.create();
    }

    private void init(){
        if(data==null){
            Log.d(TAG,"data == null...");
            empty();
        }else if(!TextUtils.isEmpty(data.getNRO_DOCIDENTIDAD())){
            Log.d(TAG,"si hay numero de documento en la respuesta");
            if(data.getAfiliado()!=null){
                Log.d(TAG,"Si hay numero de coumento y tambien hay afiliado...");
                if(!TextUtils.isEmpty(data.getAfiliado().getIdentificacion())){
                    Log.d(TAG,"Si hay numero de coumento y tambien hay afiliado con su identificacion...");
                    setViewCompleto();
                    setFamiliar();
                    setViewAfiliado(true);
                    setDataAfiliado();
                    setStars();
                }else{
                    Log.d(TAG,"Si hay numero de documento y tampoco hay afliado con su identificacion...");
                    setViewCompleto();
                    setFamiliar();
                    setStars();
                }
            }else{
                Log.d(TAG,"Hay numero de documento pero no hay afiliado...");
                setViewCompleto();
                setFamiliar();
                setStars();
            }
        }else if(data.getAfiliado()!=null){
            Log.d(TAG,"No hay numero de documento pero si hay afiliado...");
            if(!TextUtils.isEmpty(data.getAfiliado().getIdentificacion())){
                Log.d(TAG,"No hay numero de documento pero si hay afliado con su identificacion...");
                setViewAfiliado(false);
                setDataAfiliado();
            }else{
                Log.d(TAG,"No hay numero de documento y tampoco hay afliado con su identificacion...");
                empty();
            }

        }else{
            Log.d(TAG,"No hay informacion...");
            empty();
        }
    }

    //region Completo
    private void setViewCompleto(){
        viewEmpty.setVisibility(View.GONE);
        viewDataContent.setVisibility(View.VISIBLE);
        viewSegmento.setVisibility(View.VISIBLE);
        viewAwithStar.setVisibility(View.VISIBLE);
        viewStarsConten.setVisibility(View.VISIBLE);
        viewSeparatorStarwithHijo.setVisibility(View.VISIBLE);
        viewHijosContent.setVisibility(View.VISIBLE);
        vieSeparatorHijowithFamiliar.setVisibility(View.VISIBLE);
        viewFamiliarContent.setVisibility(View.VISIBLE);
        viewSeparatorFinancierawithLaboral.setVisibility(View.VISIBLE);
        viewSeparatorFamiliarwithAfiliado.setVisibility(View.VISIBLE);
        viewAfiliadoContent.setVisibility(View.GONE);
    }

    private void setFamiliar(){
        viewAfiliadoContent.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(data.getIND_BANCARIZADO())){
            String txt = tvBancarizado.getText().toString()+" ";
            tvBancarizado.setText(txt+data.getIND_BANCARIZADO().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getIND_CALIF_SBS())){
            String txt = tvsbs.getText().toString()+" ";
            tvsbs.setText(txt+data.getIND_CALIF_SBS().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getIND_CALIF_TDC())){
            String txt = tvtdc.getText().toString()+" ";
            tvtdc.setText(txt+data.getIND_CALIF_TDC().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getCNT_TDC_REPORTADAS())){
            String txt = tvcantidadtdc.getText().toString()+" ";
            tvcantidadtdc.setText(txt+data.getCNT_TDC_REPORTADAS().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getCNT_TDC_REP_BANCO())){
            String txt = tvcantidadbanco.getText().toString()+" ";
            tvcantidadbanco.setText(txt+data.getCNT_TDC_REP_BANCO().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getIND_DISP_TDC())){
            String txt = tvdislineatdc.getText().toString()+" ";
            tvdislineatdc.setText(txt+data.getIND_DISP_TDC().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getIND_DISP_MAYOR_TDC())){
            String txt = tvdismayorlineatdc.getText().toString()+" ";
            tvdismayorlineatdc.setText(txt+data.getIND_DISP_MAYOR_TDC().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getIND_RANGO_ING())){
            String txt = tvRangoPromedioIngreso.getText().toString();
            tvRangoPromedioIngreso.setText(txt+data.getIND_RANGO_ING().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getIND_DEPENDENCIA())){
            String txt = tvDependencia.getText().toString();
            tvDependencia.setText(txt+data.getIND_DEPENDENCIA().trim());
        }else{

        }
        if(!TextUtils.isEmpty(data.getNRO_HIJOS())){
            tvCantidadHijos.setText(data.getNRO_HIJOS().trim());
        }else{

        }
        //Temporal
        /*if(data.getAfiliado()!=null){
            if(data.getAfiliado().getCodigoGrupoFamiliar()!=null){
                if(!TextUtils.isEmpty(data.getAfiliado().getCodigoGrupoFamiliar().trim())){
                    tvRelacionOncosalud.setText("GF "+data.getAfiliado().getCodigoGrupoFamiliar().trim());
                }
            }
        }/*/
        if(data.getAfiliado()!=null){
            Log.d(TAG,"data.getAfiliado!=null...");
            if(data.getAfiliado().getCodigoGrupoFamiliar()!=null){
                Log.d(TAG,"data.getAfiliado!=null...");
                if((data.getAfiliado().getCodigoGrupoFamiliar().trim().length()>0)){
                    viewAfiliadoContent.setVisibility(View.VISIBLE);
                    getTvRelacionOncosaludAfiliado.setVisibility(View.GONE);
                    tvDocumentoAfiliado.setVisibility(View.VISIBLE);
                    tvPlanAfiliado.setVisibility(View.VISIBLE);
                    tvTitularAfiliado.setVisibility(View.VISIBLE);
                    tvGFAfiliado.setVisibility(View.VISIBLE);
                }else{
                    viewAfiliadoContent.setVisibility(View.VISIBLE);
                    getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                    tvDocumentoAfiliado.setVisibility(View.GONE);
                    tvPlanAfiliado.setVisibility(View.GONE);
                    tvTitularAfiliado.setVisibility(View.GONE);
                    tvGFAfiliado.setVisibility(View.GONE);
                }
            }else{
                viewAfiliadoContent.setVisibility(View.VISIBLE);
                getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                tvDocumentoAfiliado.setVisibility(View.GONE);
                tvPlanAfiliado.setVisibility(View.GONE);
                tvTitularAfiliado.setVisibility(View.GONE);
                tvGFAfiliado.setVisibility(View.GONE);
            }
        }else{
            viewAfiliadoContent.setVisibility(View.VISIBLE);
            getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
            tvDocumentoAfiliado.setVisibility(View.GONE);
            tvPlanAfiliado.setVisibility(View.GONE);
            tvTitularAfiliado.setVisibility(View.GONE);
            tvGFAfiliado.setVisibility(View.GONE);
        }
    }

    private void setStars(){
        viewSegmento.setVisibility(View.VISIBLE);
        String segmento = data.getCOD_SEGMENTO().trim();
        if(segmento!=null){
            if(!segmento.equalsIgnoreCase("")){
                //Es aqui Ramon!
                Drawable fullStar  = getResources().getDrawable(R.drawable.iv_star_full);
                fullStar.mutate();
                Drawable halfStar = getResources().getDrawable(R.drawable.iv_star_half);
                halfStar.mutate();
                Drawable emptyStar = getResources().getDrawable(R.drawable.iv_star_empty);
                emptyStar.mutate();
                tvSegmentoStar.setText(segmento);
                switch (segmento){
                    case "AAA":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(fullStar);
                        iv4.setImageDrawable(fullStar);
                        iv5.setImageDrawable(fullStar);
                        iv6.setImageDrawable(fullStar);
                        break;
                    case "AA":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(fullStar);
                        iv4.setImageDrawable(fullStar);
                        iv5.setImageDrawable(fullStar);
                        iv6.setImageDrawable(halfStar);
                        break;
                    case "A":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(fullStar);
                        iv4.setImageDrawable(fullStar);
                        iv5.setImageDrawable(fullStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "BBB":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(fullStar);
                        iv4.setImageDrawable(fullStar);
                        iv5.setImageDrawable(halfStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "BB":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(fullStar);
                        iv4.setImageDrawable(fullStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "B":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(fullStar);
                        iv4.setImageDrawable(halfStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "CCC":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(fullStar);
                        iv4.setImageDrawable(emptyStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "CC":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(halfStar);
                        iv4.setImageDrawable(emptyStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "C":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(fullStar);
                        iv3.setImageDrawable(emptyStar);
                        iv4.setImageDrawable(emptyStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "DDD":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(halfStar);
                        iv3.setImageDrawable(emptyStar);
                        iv4.setImageDrawable(emptyStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "DD":
                        iv1.setImageDrawable(fullStar);
                        iv2.setImageDrawable(emptyStar);
                        iv3.setImageDrawable(emptyStar);
                        iv4.setImageDrawable(emptyStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    case "D":
                        iv1.setImageDrawable(halfStar);
                        iv2.setImageDrawable(emptyStar);
                        iv3.setImageDrawable(emptyStar);
                        iv4.setImageDrawable(emptyStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                    default:
                        iv1.setImageDrawable(emptyStar);
                        iv2.setImageDrawable(emptyStar);
                        iv3.setImageDrawable(emptyStar);
                        iv4.setImageDrawable(emptyStar);
                        iv5.setImageDrawable(emptyStar);
                        iv6.setImageDrawable(emptyStar);
                        break;
                }
                //
            }else{
                tvSegmentoStar.setText("Sin Segmento");
            }
        }else{
            tvSegmentoStar.setText("Sin Segmento");
        }
    }
    //endregion

    //region Afiliado
    private void setViewAfiliado(boolean flag){
        if(flag){
            viewEmpty.setVisibility(View.GONE);
            viewDataContent.setVisibility(View.VISIBLE);
            viewStarsConten.setVisibility(View.VISIBLE);
            viewSeparatorStarwithHijo.setVisibility(View.VISIBLE);
            vieSeparatorHijowithFamiliar.setVisibility(View.GONE);
            viewAfiliadoContent.setVisibility(View.VISIBLE);
        }else{
            viewEmpty.setVisibility(View.GONE);
            viewSegmento.setVisibility(View.GONE);
            viewDataContent.setVisibility(View.VISIBLE);
            viewAwithStar.setVisibility(View.GONE);
            viewStarsConten.setVisibility(View.VISIBLE);
            viewSeparatorStarwithHijo.setVisibility(View.VISIBLE);
            viewHijosContent.setVisibility(View.GONE);
            vieSeparatorHijowithFamiliar.setVisibility(View.GONE);
            viewFamiliarContent.setVisibility(View.GONE);
            viewSeparatorFamiliarwithAfiliado.setVisibility(View.GONE);
            viewAfiliadoContent.setVisibility(View.VISIBLE);
        }

    }

    private void setDataAfiliado(){
        InVentasProspectoAfiliado afiliado = data.getAfiliado();
        //Actualizacion para el atributo Dependencia
        if(data.getAfiliado()==null){
            viewAfiliadoContent.setVisibility(View.VISIBLE);
            getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
            tvDocumentoAfiliado.setVisibility(View.GONE);
            tvPlanAfiliado.setVisibility(View.GONE);
            tvTitularAfiliado.setVisibility(View.GONE);
            tvGFAfiliado.setVisibility(View.GONE);
        }else{
            if(data.getAfiliado().getCodigoGrupoFamiliar()!=null){
                if((data.getAfiliado().getCodigoGrupoFamiliar().trim().length()==0)){
                    viewAfiliadoContent.setVisibility(View.VISIBLE);
                    getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                    tvDocumentoAfiliado.setVisibility(View.GONE);
                    tvPlanAfiliado.setVisibility(View.GONE);
                    tvTitularAfiliado.setVisibility(View.GONE);
                    tvGFAfiliado.setVisibility(View.GONE);
                }else{
                    if(!TextUtils.isEmpty(afiliado.getTipoIdentificacion()) && !TextUtils.isEmpty(afiliado.getIdentificacion())){
                        tvDocumentoAfiliado.setText(afiliado.getTipoIdentificacion().trim()+" "+afiliado.getIdentificacion().trim());
                    }else{
                        tvDocumentoAfiliado.setVisibility(View.GONE);
                        getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                    }
                    if(!TextUtils.isEmpty(afiliado.getOrigenCliente())){
                        tvPlanAfiliado.setText(afiliado.getOrigenCliente().trim());
                    }else{
                        tvPlanAfiliado.setVisibility(View.GONE);
                        getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                    }
                    if(!TextUtils.isEmpty(afiliado.getCategoria())){
                        tvTitularAfiliado.setText(afiliado.getCategoria().trim());
                    }else{
                        tvTitularAfiliado.setVisibility(View.GONE);
                        getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                    }
                    if(!TextUtils.isEmpty(afiliado.getCodigoGrupoFamiliar())){
                        tvGFAfiliado.setText("GF "+afiliado.getCodigoGrupoFamiliar().trim());
                    }else {
                        tvGFAfiliado.setVisibility(View.GONE);
                        getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                if(!TextUtils.isEmpty(afiliado.getTipoIdentificacion()) && !TextUtils.isEmpty(afiliado.getIdentificacion())){
                    tvDocumentoAfiliado.setText(afiliado.getTipoIdentificacion().trim()+" "+afiliado.getIdentificacion().trim());
                }else{
                    tvDocumentoAfiliado.setVisibility(View.GONE);
                    getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(afiliado.getOrigenCliente())){
                    tvPlanAfiliado.setText(afiliado.getOrigenCliente().trim());
                }else{
                    tvPlanAfiliado.setVisibility(View.GONE);
                    getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(afiliado.getCategoria())){
                    tvTitularAfiliado.setText(afiliado.getCategoria().trim());
                }else{
                    tvTitularAfiliado.setVisibility(View.GONE);
                    getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(afiliado.getCodigoGrupoFamiliar())){
                    tvGFAfiliado.setText("GF "+afiliado.getCodigoGrupoFamiliar().trim());
                }else {
                    tvGFAfiliado.setVisibility(View.GONE);
                    getTvRelacionOncosaludAfiliado.setVisibility(View.VISIBLE);
                }
            }

        }
    }
    //endregion

    //region Empty
    private void empty(){
        viewEmpty.setVisibility(View.VISIBLE);
        viewDataContent.setVisibility(View.GONE);
    }
    //endregion

    //region newInstance
    public static CalificativoDialog newInstance(InVentasProspecto list,callbackOpcionSelected listener){
        CalificativoDialog dialog = new CalificativoDialog();
        dialog.setData(list,listener);
        return dialog;
    }

    private void setData(InVentasProspecto list,callbackOpcionSelected listener){
        this.data = list;
        this.listener = listener;
    }
    //endregion

    //region Methods

    private void configureAcceptCancelButton(){
        tvCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"tvCerrar...");
                Log.d(TAG,"Se eligio la opcion cerrar...");
                listener.onSelectedAceptar();
                dismiss();
            }
        });

    }

    public interface callbackOpcionSelected{
        void onSelectedCancelar();
        void onSelectedAceptar();
    }

    //endregion

    //region Ciclo de vida

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createCustomDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
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

    //endregion
}
