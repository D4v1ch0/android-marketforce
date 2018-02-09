package rp3.auna.customviews.Tabs.TabsViews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Main2Activity;
import rp3.auna.R;

/**
 * Created by Jesus Villa on 01/12/2017.
 */

public class TabCampaña extends Fragment {
    private static final String TAG = TabCampaña.class.getSimpleName();
    @BindView(R.id.tvMensaje) TextView tvMensaje;
    @BindView(R.id.containerAgenda) RelativeLayout view;
    @BindView(R.id.recyclerViewAgenda) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_agenda, container, false);
        ButterKnife.bind(this,view);
        Log.d(TAG,"onCreateView...");
        tvMensaje.setText("En construccion...");
        view.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        return view;
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
