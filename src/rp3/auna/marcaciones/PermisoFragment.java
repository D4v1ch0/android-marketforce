package rp3.auna.marcaciones;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import rp3.app.BaseFragment;
import rp3.auna.R;
import rp3.auna.models.marcacion.Justificacion;
import rp3.auna.models.marcacion.Permiso;
import rp3.widget.SlidingPaneLayout;

/**
 * Created by magno_000 on 19/06/2015.
 */
public class PermisoFragment extends BaseFragment implements PermisoListFragment.PermisoListFragmentListener, PermisoDetailFragment.PermisoDetailFragmentListener {

    private static final String TAG = PermisoFragment.class.getSimpleName();
    private static final int PARALLAX_SIZE = 0;

    private PermisoListFragment transactionListFragment;
    private PermisoDetailFragment transactionDetailFragment;
    private SlidingPaneLayout slidingPane;

    private Menu menu;
    public boolean mTwoPane = false;
    public boolean isActiveListFragment = true;
    private long selectedClientId;

    public static PermisoFragment newInstance() {
        PermisoFragment fragment = new PermisoFragment();
        return fragment;
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
        Log.d(TAG,"onFragmentResult...");
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG,"onAttach...");
        super.onAttach(activity);
        setContentView(R.layout.fragment_client);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        setRetainInstance(true);
        transactionListFragment = PermisoListFragment.newInstance(true, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @SuppressLint("NewApi")
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        slidingPane = (SlidingPaneLayout) rootView.findViewById(R.id.sliding_pane_clientes);
        slidingPane.setParallaxDistance(PARALLAX_SIZE);
        slidingPane.setShadowResource(R.drawable.sliding_pane_shadow);
        slidingPane.setSlidingEnabled(false);
        slidingPane.openPane();

        if (slidingPane.isOpen() &&
                rootView.findViewById(R.id.content_transaction_list).getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT)
            mTwoPane = true;
        else
            mTwoPane = false;

        if (!hasFragment(R.id.content_transaction_list))
            setFragment(R.id.content_transaction_list, transactionListFragment);

        slidingPane.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPanelOpened(View panel) {
                isActiveListFragment = true;
                //getActivity().getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
            }

            @Override
            public void onPanelClosed(View panel) {
                isActiveListFragment = false;
                // if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2) {
                // getActivity().getActionBar().setHomeButtonEnabled(true);
                //}
            }
        });

//		if(getChildFragmentManager().findFragmentById(R.id.transaction_detail) == null){
//			if(rootView.findViewById(R.id.content_transaction_list)!=null){
//
//			}
//		}
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
         transactionDetailFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.d(TAG,"onStart...");
        if (selectedClientId != 0) {
            if (!mTwoPane)
                slidingPane.closePane();
        }
    }

    @Override
    public boolean allowSelectedItem() {
        return mTwoPane;
    }

    @Override
    public void onPermisoSelected(Justificacion permiso) {
        selectedClientId = permiso.getID();

        if(Justificacion.getPermisoById(getDataBase(), permiso.getID()) != null) {

            if (!mTwoPane) {
                slidingPane.closePane();
                isActiveListFragment = false;
            }

            transactionDetailFragment = PermisoDetailFragment.newInstance(permiso);
            setFragment(R.id.content_transaction_detail, transactionDetailFragment);
        }
        else
        {
            Toast.makeText(getContext(), "Permiso seleccionado ya fue aprobado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFinalizaConsulta() {
    }

    @Override
    public void onPermisoChanged(Permiso permiso) {
        Log.d(TAG,"onPermisoChanged...");
        if (!mTwoPane)
            slidingPane.openPane();
        transactionListFragment.onResume();

    }

    /**
     *
     * Ciclo de vida
     *
     */


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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
