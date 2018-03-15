package rp3.auna.customviews.Tabs.TabsAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rp3.auna.bean.CotizacionVisita;
import rp3.auna.bean.RegistroPago;
import rp3.auna.bean.SolicitudMovil;
import rp3.auna.fragments.CotizacionFragment;
import rp3.auna.fragments.DocumentoFragment;
import rp3.auna.fragments.PagoFragment;
import rp3.auna.fragments.SolicitudFragment;

/**
 * Created by Jesus Villa on 09/01/2018.
 */

public class TabsDetalleAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = TabsDetalleAdapter.class.getSimpleName();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private CotizacionFragment cotizacionInicialFragment;
    private CotizacionFragment cotizacionFinalFragment;
    private SolicitudFragment solicitudFragment;
    private PagoFragment pagoFragment;
    private DocumentoFragment documentoFragment;

    public TabsDetalleAdapter(FragmentManager fragmentManager, CotizacionVisita listInicio, CotizacionVisita listFinal,
                              SolicitudMovil solicitudMovil,RegistroPago registroPago,Bundle todoPago){
        super(fragmentManager);
        Log.d(TAG,"contructor...");
        cotizacionInicialFragment = CotizacionFragment.newInstance(listInicio);
        cotizacionFinalFragment = CotizacionFragment.newInstance(listFinal);
        solicitudFragment = SolicitudFragment.newInstance(solicitudMovil);
        pagoFragment = PagoFragment.newInstance(registroPago,todoPago);
        documentoFragment = new DocumentoFragment();
        mFragmentTitleList.add("Cotización Inicial");
        mFragmentTitleList.add("Solicitud");
        mFragmentTitleList.add("Cotización Final");
        mFragmentTitleList.add("Pagos");
        mFragmentTitleList.add("Documentos");
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG,"getItem..."+position);
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d(TAG,"tabInicial getItem...");
                fragment = cotizacionInicialFragment;
                break;
            case 1:
                Log.d(TAG,"tabSolicitud getItem...");
                fragment = solicitudFragment;
                break;
            case 2:
                Log.d(TAG,"tabFinal getItem...");
                fragment = cotizacionFinalFragment;
                break;
            case 3:
                Log.d(TAG,"tabPagos getItem...");
                fragment = pagoFragment;
                break;
            case 4:
                Log.d(TAG,"tabDocumento getItem...");
                fragment = documentoFragment;
                break;
        }
        return fragment;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d(TAG,"notifyDataSetChanged...");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d(TAG,"position:"+position+" "+mFragmentTitleList.get(position));
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }
}
