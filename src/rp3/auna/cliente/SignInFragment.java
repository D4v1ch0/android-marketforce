package rp3.auna.cliente;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rp3.accounts.ServerAuthenticate;
import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.auna.R;
import rp3.auna.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 18/04/2016.
 */
public class SignInFragment extends BaseFragment {

    private static final String TAG = SignInFragment.class.getSimpleName();
    public static String ARG_USER = "user";
    public static String ARG_PASS = "pass";
    public String type = "SignIn";

    private SignConfirmListener createFragmentListener;

    public interface SignConfirmListener{
        public void onSignSuccess(Bundle bundle);
        public void onSignError(Bundle bundle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"...");
        this.setCancelable(false);
        setContentView(R.layout.fragment_sign_in);
        if(getParentFragment()!=null){
            createFragmentListener = (SignConfirmListener)getParentFragment();
        }else{
            createFragmentListener = (SignConfirmListener) activity;
        }
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);
        Log.d(TAG,"...");
        closeDialogProgress();
        if(messages.hasErrorMessage()){
            showDialogMessage(messages);
        }else {
            if(data.getBoolean(ServerAuthenticate.KEY_SUCCESS, false)) {
                data.putString(ARG_USER, ((EditText) getRootView().findViewById(R.id.sign_in_user)).getText().toString());
                createFragmentListener.onSignSuccess(data);
                dismiss();
            }
            else
            {
                Toast.makeText(getContext(), "Usuario No Autorizado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"...");
        ((Button) rootView.findViewById(R.id.sign_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) rootView.findViewById(R.id.sign_in_user)).length() <= 0) {
                    Toast.makeText(getContext(), "Falta nombre de usuario.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (((EditText) rootView.findViewById(R.id.sign_in_pass)).length() <= 0) {
                    Toast.makeText(getContext(), "Falta password.", Toast.LENGTH_LONG).show();
                    return;
                }

                showDialogProgress("Espere un momento", "Autorizando...");
                if (ConnectionUtils.isNetAvailable(getActivity())) {
                    Bundle bundle = new Bundle();
                    if (type.equalsIgnoreCase("SignIn"))
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AUTORIZAR_CIUD_ORO);
                    if (type.equalsIgnoreCase("AuthDesc"))
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AUTORIZAR_DESC);
                    bundle.putString(ARG_USER, ((EditText) rootView.findViewById(R.id.sign_in_user)).getText().toString());
                    bundle.putString(ARG_PASS, ((EditText) rootView.findViewById(R.id.sign_in_pass)).getText().toString());
                    requestSync(bundle);
                }

            }
        });
        ((Button) rootView.findViewById(R.id.sign_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragmentListener.onSignError(new Bundle());
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