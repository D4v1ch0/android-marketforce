package rp3.auna.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;
import rp3.auna.adapter.GeneralAdapter;
import rp3.auna.adapter.LlamadaAdapter;
import rp3.auna.customviews.DividerItemDecoration;

/**
 * Created by Jesus Villa on 03/11/2017.
 */

public class GeneralValueDialog extends DialogFragment {
    private static final String TAG = GeneralValueDialog.class.getSimpleName();
    @BindView(R.id.tvLlamada)TextView tvTitulo;
    @BindView(R.id.rvListGeneral)RecyclerView recyclerView;
    private callbackGeneralSelected callBack;
    private RecyclerView.LayoutManager llmanager;
    private GeneralAdapter adapter;
    private int selected = -1;

    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_general_value, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        llmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llmanager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        List<String> list = getArguments().getStringArrayList("list");
        adapter=new GeneralAdapter(getActivity(), new GeneralAdapter.OnSelectedListener() {
            @Override
            public void onSelected(String selected, int position) {
                callBack.onSelected(selected,position);
                dismiss();
            }
        });
        adapter.addMoreItems(list);
        recyclerView.setAdapter(adapter);
        return builder.create();
    }

    public static GeneralValueDialog newInstance(callbackGeneralSelected callBack){
        GeneralValueDialog dialog = new GeneralValueDialog();
        dialog.setListener(callBack);
        return dialog;
    }

    public void setListener(callbackGeneralSelected callbackLlamada) {
        this.callBack = callbackLlamada;
    }


    public interface callbackGeneralSelected{
        void onSelected(String selected,int position);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createCustomDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
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
}
