package rp3.marketforce.marcaciones;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.marcacion.Permiso;
import rp3.widget.SlidingPaneLayout;

/**
 * Created by magno_000 on 19/06/2015.
 */
public class PermisoFragment extends BaseFragment implements PermisoListFragment.PermisoListFragmentListener, PermisoDetailFragment.PermisoDetailFragmentListener {

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
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_client);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        transactionListFragment = PermisoListFragment.newInstance(true, null);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

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

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

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
    public void onPermisoSelected(Permiso permiso) {
        selectedClientId = permiso.getID();

        if(!mTwoPane) {
            slidingPane.closePane();
            isActiveListFragment = false;
        }

        transactionDetailFragment = PermisoDetailFragment.newInstance(permiso);
        setFragment(R.id.content_transaction_detail, transactionDetailFragment);
    }

    @Override
    public void onFinalizaConsulta() {
    }

    @Override
    public void onPermisoChanged(Permiso permiso) {

    }
}
