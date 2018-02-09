package rp3.auna.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.Main2Activity;
import rp3.auna.R;
import rp3.configuration.PreferenceManager;

/**
 * Created by Jesus Villa on 28/11/2017.
 */

public class InformationFragment extends Fragment {
    private String TAG = InformationFragment.class.getSimpleName();
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private FrameLayout statusBar;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    int recyclerViewPaddingTop;
    private TypedValue typedValueToolbarHeight = new TypedValue();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        View view = inflater.inflate(R.layout.fragment_information,container,false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        appBarLayout = (AppBarLayout)getActivity().findViewById(R.id.appBarMain);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        statusBar = (FrameLayout) getActivity().findViewById(R.id.statusBar);
        tabLayout = ((Main2Activity) getActivity()).tabLayout;
        tabLayout.setVisibility(View.GONE);
        ((Main2Activity) getActivity()).getSupportActionBar().setTitle("Información");
        setData(view);
        setTool();
        return view;
    }

    private void setData(View v){
        linearLayout = (LinearLayout) v.findViewById(R.id.linearInformation);
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

        Spanned sp = Html.fromHtml(getString(R.string.message_information1));
        ((TextView) v.findViewById(R.id.label_link)).setText(sp);
        ((TextView) v.findViewById(R.id.information_app)).setText(getString(R.string.message_info) + " " + version);
        v.findViewById(R.id.information_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PreferenceManager.getString(Contants.KEY_SERVER) + rp3.configuration.Configuration.getAppConfiguration().getHelpUrl()));
                startActivity(browserIntent);
            }
        });

        v.findViewById(R.id.information_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", rp3.configuration.Configuration.getAppConfiguration().getMail(), null));
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        ((TextView) v.findViewById(R.id.info_android_id)).setText("Android ID: " + PreferenceManager.getString(Contants.KEY_ANDROID_ID, ""));
        ((TextView) v.findViewById(R.id.info_punto_operacion)).setVisibility(View.GONE);
                //setText("Punto de Operacion : " + PreferenceManager.getString(Contants.KEY_NOMBRE_PUNTO_OPERACION,""));
        ((TextView) v.findViewById(R.id.info_caja)).setVisibility(View.GONE);
                //setText("Caja: " + PreferenceManager.getString(Contants.KEY_CAJA, "Sin Caja"));
        ((TextView) v.findViewById(R.id.infor_caja_id)).setVisibility(View.GONE);
                //setText("Id Caja: " + PreferenceManager.getInt(Contants.KEY_ID_CAJA, 0));
        scrollView = (ScrollView) v.findViewById(R.id.scrollView);
    }

    private void setTool(){
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValueToolbarHeight, true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG,"Orientation_Portait...");
            if (Build.VERSION.SDK_INT >= 19) {
                Log.d(TAG,"SDK_INT >= 19...");
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }else{
                Log.d(TAG,"SDK_INT < 19...");
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG,"Orientation_LandScape...");
            if (Build.VERSION.SDK_INT >= 21) {
                Log.d(TAG,"SDK_INT >= 21...");
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21){
                Log.d(TAG,"Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21...");
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }
            if (Build.VERSION.SDK_INT < 19) {
                Log.d(TAG,"Build.VERSION.SDK_INT < 19...");
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        linearLayout.setPadding(0, recyclerViewPaddingTop, 0, 0);
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    //region Ciclo de vida

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            ((Main2Activity)getActivity()).openDrawer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
        Log.d(TAG,"onDestroy...");
        onDestroyView();
    }

    //endregion
}
