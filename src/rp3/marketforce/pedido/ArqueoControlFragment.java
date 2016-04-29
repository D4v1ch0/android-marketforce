package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 22/02/2016.
 */
public class ArqueoControlFragment extends BaseFragment {

    private ControlCajaListener createFragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

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
}
