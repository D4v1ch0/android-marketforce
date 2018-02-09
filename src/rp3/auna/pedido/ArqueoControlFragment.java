package rp3.auna.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import rp3.app.BaseFragment;
import rp3.auna.R;
import rp3.auna.models.pedido.ControlCaja;

/**
 * Created by magno_000 on 22/02/2016.
 */
public class ArqueoControlFragment extends BaseFragment {

    private static final String TAG = ArqueoControlFragment.class.getSimpleName();
    private ControlCajaListener createFragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach...");
        setContentView(R.layout.fragment_arqueo_lista);
        if(getParentFragment()!=null){
            createFragmentListener = (ControlCajaListener)getParentFragment();
        }else{
            createFragmentListener = (ControlCajaListener) activity;
        }

    }

    public interface ControlCajaListener{
        public void onControlCajaSelected(ControlCaja transaction);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        final List<ControlCaja> controlCajas = ControlCaja.getControlCajas(getDataBase());
        ControlCajaAdapter adapter = new ControlCajaAdapter(this.getContext(), controlCajas);

        ((ListView) rootView.findViewById(R.id.arqueo_control_list)).setAdapter(adapter);
        ((ListView) rootView.findViewById(R.id.arqueo_control_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createFragmentListener.onControlCajaSelected(controlCajas.get(position));
                dismiss();
            }
        });

    }

    /**
     *
     * Ciclo de vida
     *
     */

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
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
