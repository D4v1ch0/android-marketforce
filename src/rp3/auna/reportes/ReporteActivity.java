package rp3.auna.reportes;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.CaptacionSolicitudActivity;
import rp3.auna.R;
import rp3.auna.util.helper.Util;
import rp3.runtime.Session;

public class ReporteActivity extends AppCompatActivity {
    private static final String TAG = ReporteActivity.class.getSimpleName();
    @BindView(R.id.toolbarReporte) Toolbar toolbar;
    @BindView(R.id.statusBarReporte) FrameLayout frameLayout;
    @BindView(R.id.containerLoading)View viewLoading;
    @BindView(R.id.containerOff)View viewOff;
    @BindView(R.id.containerReporte1)WebView viewData;
    @BindView(R.id.scrollcontent)ScrollView scrollView;
    private String logonName;
    private String password;
    private String option;
    private String title;
    private boolean initPage = false;
    private String url;
    private String urlOption;
    private String resource;
    private String actionOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        Log.d(TAG,"onCreate...");
        ButterKnife.bind(this);
        setData();
        toolbarStatusBar();
        navigationBarStatusBar();
        init();
    }

    private void init(){
        if(validate()){
            initData();
        }else{
            setContainnerOff();
        }
    }

    private void initData(){
        Log.d(TAG,"initData...");

        resource = "VentaNueva/ReporteControlVenta/";
        if(option.equalsIgnoreCase("REPVTNGRA")){
            actionOption = "GraficoControlVenta";
        }else if(option.equalsIgnoreCase("REPVTNINF")){
            actionOption = "ControlVentaInforme";
        }
        else if(option.equalsIgnoreCase("REPVTNJEF")){
            actionOption = "ControlVentaJefatura";
        }
        else if(option.equalsIgnoreCase("REPVTNGER")){
            actionOption = "ControlVentaGerencia";
        }
        else if(option.equalsIgnoreCase("REPVTNFUN")){
            actionOption = "ControlVentaFunnel";
        }else{
            actionOption = "Inicio";
        }
        url = getResources().getString(R.string.urlapi)+resource+"ReporteMovilOption?"+
                "logonName="+logonName+"&password="+password+"&option="+option;
        Log.d(TAG,"Url:"+url);
        viewData.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG,"shouldOverrideUrlLoading...");
                String nuevo= getApplicationContext().getResources().getString(R.string.urlapi)+resource+actionOption;
                Log.d(TAG,"UrlChange:"+nuevo);
                /*if(url.equalsIgnoreCase(nuevo)){
                    setContainerWeb();
                }*/
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG,"2show...");
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG,"onPageStarted...");
                //if(initPage){
                  //  setContainerWeb();
                //}
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d(TAG,"onReceivedSslError...");
                setContainnerOff();
                super.onReceivedSslError(view, handler, error);
            }

            public void onPageFinished(WebView view, String url) {
                Log.d(TAG,"onPageFinished...");
                if(!initPage){
                    setContainerWeb();
                }
                //initPage = true;

            }
        });


        viewData.getSettings().setJavaScriptEnabled(true);
        viewData.getSettings().setLoadWithOverviewMode(true);
        viewData.getSettings().setUseWideViewPort(true);
        viewData.getSettings().setSupportZoom(true);
        viewData.getSettings().setBuiltInZoomControls(false);
        viewData.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        viewData.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        viewData.getSettings().setDomStorageEnabled(true);
        viewData.setHorizontalScrollBarEnabled(true);
        viewData.setVerticalScrollBarEnabled(true);
        viewData.setScrollbarFadingEnabled(false);
        viewData.setVerticalFadingEdgeEnabled(false);
        viewData.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        viewData.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            viewData.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            viewData.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //viewData.setScrollBarDefaultDelayBeforeFade(View.SCROLLBAR_POSITION_DEFAULT);
        Log.d(TAG,"URL:"+url);
        viewData.loadUrl(url);
    }

    //region Loading

    private void setContainnerOff(){
        viewLoading.setVisibility(View.GONE);
        viewData.setVisibility(View.GONE);
        viewOff.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }

    private void setContainerWeb(){
        viewLoading.setVisibility(View.GONE);
        viewData.setVisibility(View.VISIBLE);
        viewOff.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    private void setData(){
        logonName = Session.getUser().getLogonName();
        password = Session.getUser().getPassword();
        option = getIntent().getStringExtra("Option");
        title = getIntent().getStringExtra("Title");
    }

    //endregion

    //region General

    public void toolbarStatusBar() {
        setSupportActionBar(toolbar);
        if(title!=null){
            getSupportActionBar().setTitle(title);
        }else{
            getSupportActionBar().setTitle("Reportes");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void navigationBarStatusBar() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                ReporteActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                frameLayout.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                ReporteActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                frameLayout.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                ReporteActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                frameLayout.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                ReporteActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    private boolean validate(){
        if(Util.isNetworkAvailable(this)){
            return true;
        }
        return false;
    }

    //endregion

    //region Ciclo de vida

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG,"onBackPressed...");
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion
}
