package rp3.auna.customviews.Tabs.TabsAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.auna.Main2Activity;
import rp3.auna.customviews.Tabs.TabsViews.TabCampaña;
import rp3.auna.customviews.Tabs.TabsViews.TabCita;
import rp3.auna.customviews.Tabs.TabsViews.TabComisiones;
import rp3.auna.customviews.Tabs.TabsViews.TabLlamada;
import rp3.auna.sync.SyncAdapter;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 10/10/2017.
 */

public class TabsDesignViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = TabsDesignViewPagerAdapter.class.getSimpleName();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    CharSequence titles[];
    int tabNumber;
    public TabCita tabCita = new TabCita();
    public TabLlamada tabLlamada = new TabLlamada();
    public TabComisiones tabComisiones = new TabComisiones();
    public TabCampaña tabCampaña = new TabCampaña();
    //Date selected
    public Date dateSelected = null;

    public TabsDesignViewPagerAdapter (FragmentManager fragmentManager, CharSequence titles[], int tabNumber) {
        super(fragmentManager);
        Log.d(TAG,"contructor...");
        this.titles = titles;
        this.tabNumber = tabNumber;
        mFragmentTitleList.add("Llamadas");
        mFragmentTitleList.add("Visitas");
        mFragmentTitleList.add("Campañas");
        mFragmentTitleList.add("Comisiones");
    }

    public void onResfreshCita(){
        tabCita.refresh();
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG,"getItem..."+position);
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d(TAG,"tabLlamada getItem...");
                tabLlamada.setDate(dateSelected);
                fragment = tabLlamada;
                break;
            case 1:
                Log.d(TAG,"tabCita getItem...");
                tabCita.setDate(dateSelected);
                fragment = tabCita;
                break;
            case 2:
                Log.d(TAG,"tabCampaña getItem...");
                fragment = tabCampaña;
                break;
            case 3:
                Log.d(TAG,"tabComision getItem...");
                fragment = tabComisiones;
                break;
        }
        return fragment;
    }

    public void setDate(Date date){
        try {
            dateSelected = date;
            Log.d(TAG,"dateSelected:"+dateSelected.toString());
            if(tabCita.isResumed()){
                Log.d(TAG,"tabCita.isResumed()...");
                if(tabCita.isVisible()){
                    Log.d(TAG,"tabCita.isVisible()...");
                    tabCita.obtenerDate(Convert.getDotNetTicksFromDate(dateSelected));
                }
            }
            if(tabLlamada.isResumed()){
                Log.d(TAG,"tabLlamada.isResumed()...");
                if(tabLlamada.isVisible()){
                    Log.d(TAG,"tabLlamada.isVisible()...");
                    tabLlamada.obtenerDate(Convert.getDotNetTicksFromDate(dateSelected));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d(TAG,"notifyDataSetChanged...");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // return null to display only the icon
        //return null;
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
        return tabNumber;
    }
}