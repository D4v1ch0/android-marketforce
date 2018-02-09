package rp3.auna.fragments;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;


import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.Main2Activity;
import rp3.auna.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rp3.auna.customviews.Tabs.TabsAdapter.TabsDesignViewPagerAdapter;
import rp3.auna.dialog.DateSelectPickerDialog;
import rp3.auna.models.ApplicationParameter;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.util.constants.Constants;
import rp3.auna.utils.Utils;

/**
 * Created by Jesus Villa on 10/10/2017.
 */

public class AgendaFragment extends Fragment{

    private static final String TAG = AgendaFragment.class.getSimpleName();
    public static final String FECHA = "Fecha";
    TabLayout tabLayout;
    @BindView(R.id.pager) ViewPager pager;
    private TabsDesignViewPagerAdapter tabsDesignViewPagerAdapter;
    private Toolbar toolbar;
    private FrameLayout statusBar;
    private AppBarLayout appBarLayout;

    private CharSequence titles[] = {"Llamadas", "Visitas","Campañas","Comisiones"};
    private int tabNumber = titles.length;
    private int tabsPaddingTop;
    private  TypedValue typedValueToolbarHeight = new TypedValue();
    //Date seleccionado para estabilizar al pasar de tab
    public Date dateSelected = null;
    public ApplicationParameter parameterDateMaxLlamada = null;
    public ApplicationParameter parameterDateMinLlamada = null;
    public Calendar dateMaxLlamada = null;
    public Calendar dateMinLlamada = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        View view = inflater.inflate(R.layout.fragment_agenda,container,false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        appBarLayout = (AppBarLayout)getActivity().findViewById(R.id.appBarMain);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        statusBar = (FrameLayout) getActivity().findViewById(R.id.statusBar);
        tabLayout = ((Main2Activity) getActivity()).tabLayout;
        tabLayout.setVisibility(View.VISIBLE);
        ((Main2Activity) getActivity()).getSupportActionBar().setTitle("Agenda");
        setupTabs();
        try {
            setParametersDate();
        }catch (Exception e){
            e.printStackTrace();
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatTile = new SimpleDateFormat(Contants.DATE_FORMAT_AGENDA_TITLE);
        String title = formatTile.format(calendar.getTime());
        ((Main2Activity) getActivity()).getSupportActionBar().setTitle("Agenda "+title);
        return view;
    }

    //Control de cambios setear la seleccion de visitas
    public void setTabSelection(int index){
        Log.d(TAG,"Seleccionar el index tab: "+index);
        tabsDesignViewPagerAdapter.onResfreshCita();
    }

    //region General

    private void setupViewPager() {
        //pager.setPadding(convertToPx(48), tabsPaddingTop, convertToPx(16), 0);
        tabsDesignViewPagerAdapter = new TabsDesignViewPagerAdapter(getFragmentManager(), titles, tabNumber);
        tabsDesignViewPagerAdapter.setDate(Calendar.getInstance().getTime());
        pager.setAdapter(tabsDesignViewPagerAdapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();
        //tabLayout.setPadding(convertToPx(48), tabsPaddingTop, convertToPx(16), 0);
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_action_phone,
                R.drawable.ic_action_cita,
                R.drawable.ic_action_campana,
                R.drawable.ic_action_comision};
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    public void setupTabs() {
        //tabsDesignViewPagerAdapter = new TabsDesignViewPagerAdapter(getFragmentManager(), titles, tabNumber);
        //pager.setAdapter(tabsDesignViewPagerAdapter);
        //tabs.setPadding(convertToPx(48), tabsPaddingTop, convertToPx(16), 0);
        //tabs.setDistributeEvenly(false);
        // Tab indicator color
        /*tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                //TypedValue typedValue = new TypedValue();
                //getActivity().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
                //final int color = typedValue.data;
                return getResources().getColor(R.color.md_white_1000);
            }
        });*/

        // Setup tabs top padding
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValueToolbarHeight, true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }else{
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 21) {
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21){
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }
            if (Build.VERSION.SDK_INT < 19) {
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        //tabLayout.setPadding(convertToPx(48), tabsPaddingTop, convertToPx(16), 0);
        setupViewPager();
        //tabs.setPadding(convertToPx(48), tabsPaddingTop, convertToPx(16), 0);
        //tabs.setViewPager(pager);
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG,"onCreateOptionsMenu...");
        inflater.inflate(R.menu.menu_agenda_fecha,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //endregion

    //region Others
    private void setParametersDate(){
        parameterDateMaxLlamada = ApplicationParameter.getParameter(Utils.getDataBase(getActivity()), Constants.PARAMETERID_ANO_MAX_FILTER_LLAMADA,Constants.LABEL_LLAMADAS);
        parameterDateMinLlamada = ApplicationParameter.getParameter(Utils.getDataBase(getActivity()), Constants.PARAMETERID_ANO_MIN_FILTER_LLAMADA,Constants.LABEL_LLAMADAS);

        dateMaxLlamada = Calendar.getInstance();
        dateMinLlamada = Calendar.getInstance();

        Calendar hoy = Calendar.getInstance();
        Calendar anterior = Calendar.getInstance();
        int añoActual = hoy.get(Calendar.YEAR);
        int añoAnterior = (añoActual-(Integer.parseInt(parameterDateMinLlamada.getValue())));

        //dateMaxLlamada.set(Calendar.YEAR,Integer.parseInt(parameterDateMaxLlamada.getValue()));
        dateMaxLlamada.set(Calendar.YEAR,añoActual);
        dateMaxLlamada.set(Calendar.DAY_OF_YEAR,365);

        //dateMinLlamada.set(Calendar.YEAR,Integer.parseInt(parameterDateMinLlamada.getValue()));
        dateMinLlamada.set(Calendar.YEAR,añoAnterior);
        dateMinLlamada.set(Calendar.DAY_OF_YEAR,1);

        System.out.println("Año maximo:"+dateMaxLlamada.getTime());
        System.out.println("Año minimo:"+dateMinLlamada.getTime());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            ((Main2Activity)getActivity()).openDrawer();
            return true;
        }
        else if(id == R.id.agenda_action_filtrar){
            showDatePickerDialog();
            return true;
        }
        else if(id==R.id.agenda_refresh){
            refreshAgenda();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshAgenda(){
        ((Main2Activity) getActivity()).invokeSync("Agenda","Actualizando...");
    }
    //endregion

    private void showDatePickerDialog() {
        DateSelectPickerDialog newFragment = DateSelectPickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String selectedDate = dayOfMonth + " / " + (month+1) + " / " + year;
                Log.d(TAG,"selectedDate:"+selectedDate);
                SimpleDateFormat format=new SimpleDateFormat(Contants.DATE_FORMAT);
                SimpleDateFormat formatTile = new SimpleDateFormat(Contants.DATE_FORMAT_AGENDA_TITLE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,(month),dayOfMonth);
                final String date = format.format(calendar.getTime());
                final String title = formatTile.format(calendar.getTime());
                Log.d(TAG,"dateselected:"+date);
                ((Main2Activity) getActivity()).getSupportActionBar().setTitle("Agenda "+title);
                dateSelected = calendar.getTime();
                //tabsDesignViewPagerAdapter.setDate(dateSelected);
                setDate(calendar.getTime());
            }
        },dateMaxLlamada,dateMinLlamada);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void setDate(Date date){
        tabsDesignViewPagerAdapter.setDate(date);
    }



    //region Ciclo de vida

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
