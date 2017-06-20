package rp3.marketforce.information;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;

/**
 * Created by magno_000 on 11/05/2015.
 */
public class InformationFragment extends BaseFragment {
    private static final String TAG = InformationFragment.class.getSimpleName();
    public static InformationFragment newInstance()
    {
        return new InformationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_information);
        Log.d(TAG,"onCreate...");
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

        Spanned sp = Html.fromHtml(getString(R.string.message_information1));
        ((TextView) rootView.findViewById(R.id.label_link)).setText(sp);
        ((TextView) rootView.findViewById(R.id.information_app)).setText(getString(R.string.message_info) + " " + version);
        rootView.findViewById(R.id.information_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PreferenceManager.getString(Contants.KEY_SERVER) + rp3.configuration.Configuration.getAppConfiguration().getHelpUrl()));
                startActivity(browserIntent);
            }
        });

        rootView.findViewById(R.id.information_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",rp3.configuration.Configuration.getAppConfiguration().getMail(), null));
                startActivity(Intent.createChooser(intent, "Send Email"));
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
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
