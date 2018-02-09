package rp3.auna.customviews.Tabs.TabsViews;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;
import rp3.auna.models.ventanueva.ComisionesVta;
import rp3.auna.util.recyclerview.DividerItemDecoration;
import rp3.auna.utils.Utils;

/**
 * Created by Jesus Villa on 01/12/2017.
 */

public class TabComisiones extends Fragment {
    private static final String TAG = TabComisiones.class.getSimpleName();
    private Toolbar toolbar;
    private TypedValue typedValueToolbarHeight = new TypedValue();
    private int recyclerViewPaddingTop;
    private AppBarLayout appBarLayout;
    @BindView(R.id.tvMensaje) TextView tvMensaje;
    @BindView(R.id.containerAgenda) RelativeLayout containerSinInformacion;
    @BindView(R.id.containerComision) RelativeLayout containerInformacion;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tvVentas)TextView tvVentas;
    @BindView(R.id.tvIncentivos)TextView tvIncentivos;
    @BindView(R.id.tvComisional)TextView tvComisional;
    private ComisionesVta comisionesVta ;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_comisiones, container, false);
        ButterKnife.bind(this,view);
        Log.d(TAG,"onCreateView...");
        tvMensaje.setText("En construccion...");
        view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        containerSinInformacion.setVisibility(View.GONE);
        containerInformacion.setVisibility(View.GONE);
        try {
            init();
            data();
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void data(){
        comisionesVta = ComisionesVta.getAll(Utils.getDataBase(getActivity()));
        if(comisionesVta!=null){
            progressBar.setVisibility(View.GONE);
            containerSinInformacion.setVisibility(View.GONE);
            containerInformacion.setVisibility(View.VISIBLE);
            tvVentas.setText(comisionesVta.getVentas());
            tvComisional.setText(comisionesVta.getComision());
            tvIncentivos.setText(comisionesVta.getIncentivo());
        }else{
            progressBar.setVisibility(View.GONE);
            containerSinInformacion.setVisibility(View.VISIBLE);
            containerInformacion.setVisibility(View.GONE);
        }
    }

    private void init() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValueToolbarHeight, true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }else{
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 21) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
                Log.d(TAG,">=21 : "+recyclerViewPaddingTop);
            }
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21){
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }
            if (Build.VERSION.SDK_INT < 19) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        Log.d(TAG,"recyclerPaddingTop:"+recyclerViewPaddingTop);
        containerInformacion.setPadding(0, (recyclerViewPaddingTop-15)*2, 0, 0 );
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    //region Ciclo de Vida

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

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
    public void onDestroy() {
        super.onDestroy();
        onDestroyView();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        Log.d(TAG,"requestCode:"+requestCode+" resultCode:"+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //endregion
}
