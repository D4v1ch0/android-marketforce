package rp3.auna;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStates;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rp3.auna.adapter.ReporteAdapter;
import rp3.auna.adapter.ReporteriaAdapter;
import rp3.auna.bean.models.TitleChild;
import rp3.auna.bean.models.TitleCreator;
import rp3.auna.bean.models.TitleParent;
import rp3.auna.content.EnviarUbicacionReceiver;
import rp3.auna.events.EventBus;
import rp3.auna.events.Events;
import rp3.auna.fragments.AgendaFragment;
import rp3.auna.fragments.InformationFragment;
import rp3.auna.fragments.ProspectoFragment;
import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.AlarmJvs;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.reportes.ReporteActivity;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.util.adapter.DrawerAdapter;
import rp3.auna.util.helper.Alarm;
import rp3.auna.util.location.LocationProvider;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.data.Constants;
import rp3.data.Message;
import rp3.data.MessageCollection;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import rp3.sync.SyncUtils;
import rp3.util.ConnectionUtils;

/**Nuevo Rama para finalizar virtual**/

public class Main2Activity extends AppCompatActivity implements rp3.auna.util.location.LocationProvider.LocationCallback{

    private static final String TAG = Main2Activity.class.getSimpleName();
    private int REQUEST_STATE = 0;
    private int RESULT_STATE = 0;

    private static final int REQUEST_VISITA_REPROGRAMADA_FINALIZADA = 12;
    private static final int RESULT_VISITA_REPROGRAMADA_FINALIZADA = 12;
    private static final int RESULT_VISITA_CANCELADA_FINALIZADA = 13;
    private static final int MY_PERMISSIONS=100;
    private static final int REQUEST_CHECK_SETTINGS=1000;
    private static final int REQUEST_PROSPECTO_NUEVO = 3;
    private static final int RESULT_PROSPECTO_NUEVO = 3;
    private static final int REQUEST_PROSPECTO_EDIT = 17;
    private static final int RESULT_PROSPECTO_EDIT = 17;
    private static final int REQUEST_LLAMADA_NUEVO = 4;
    private static final int RESULT_LLAMADA_NUEVO = 4;
    private static final int REQUEST_LLAMADA_SI_DESEA_PROGRAMA = 33;
    private static final int RESULT_LLAMADA_SI_DESEA_PROGRAMA = 33;
    private static final int REQUEST_LLAMADA_REPROGRAMADA = 18;
    private static final int RESULT_LLAMADA_REPROGRAMADA = 18;
    private static final int REQUEST_VISITA_REPROGRAMADA = 19;
    private static final int RESULT_VISITA_REPROGRAMADA = 12;
    private static final int REQUEST_VISITA_NUEVO = 5;
    private static final int RESULT_VISITA_NUEVO = 5;
    //Peticion para una cotizacion Nueva(desde cero)
    private static final int REQUEST_VISITA_COTIZACION_NUEVO = 6;
    //Result Referidos de la cotizacion
    private static final int RESULT_VISITA_ONLINE_REFERIDO = 21;
    private static final int RESULT_VISITA_EFECTIVO_REFERIDO = 22;
    private static final int RESULT_VISITA_REGULAR_REFERIDO = 23;
    //
    private static final int REQUEST_REFERIDOS_FROM_VISITA = 30;
    private static final int RESULT_REFERIDOS_FROM_VISITA = 30;
    //
    private static final int REQUEST_VISITA_NUEVA_PAGO_REGULAR = 6;
    private static final int RESULT_VISITA_NUEVA_PAGO_FISICO = 11;
    private static final int REQUEST_VISITA_PAGO_FISICO_DOCUMENTOS= 7;
    private static final int RESULT_VISITA_PAGO_FISICO_DOCUMENTOS = 12;
    //Control de cambio, se solicito que se posicione en el Tab de Visita al reprogamar ó cancelar la visita
    private static final int RESULT_VISITA_REPROGRAMM_CANCEL = 34;
    private List<String> permissionsAll;
    public @BindView(R.id.appBarMain) AppBarLayout appBarLayout;
    public @BindView(R.id.tabsLayout) TabLayout tabLayout;
    @BindView(R.id.statusBar) FrameLayout statusBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @BindView(R.id.textViewUsername) TextView tvPerfil;
    @BindView(R.id.textViewName) TextView tvName;
    @BindView(R.id.imageViewToggle)ImageView imageViewToogle;
    @BindView(R.id.imageViewCover)ImageView imageViewCover;
    @BindView(R.id.imageViewPicture)CircleImageView imageViewPicture;
    @BindView(R.id.toggleButtonDrawer) ToggleButton toggleButtonDrawer;
    @BindView(R.id.linearLayoutMain) LinearLayout linearLayoutMain;
    @BindView(R.id.linearLayoutSecond) LinearLayout linearLayoutSecond;
    @BindView(R.id.rlISincronizacion) RelativeLayout rlSincronizacion;
    @BindView(R.id.rlInformation) RelativeLayout rlInformation;
    @BindView(R.id.relativeLayoutSettings) RelativeLayout rlCerrarSesion;
    public ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView recyclerViewDrawer;
    private DrawerAdapter adapterDrawer;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    public Location location;
    private LocationProvider locationProvider;
    private Status status;
    private boolean GPS = false;
    private AlertDialog dialogGps;
    private int positionSelected = 0;
    private int positiSelectedLast = 0;
    //
    private int collapse = 0;
    ReporteAdapter reporteAdapter;
    ReporteriaAdapter adapter;

    boolean busFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("Rp3MarketForceAuna", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        toolbarStatusBar();
        setFragment(sharedPreferences.getInt("FRAGMENT", 0));
        configureClickItemDefault();
        navigationDrawer();
        toogleButtonDrawer();
        initLocation();
        validatePermissionApp();
    }

    private void setDefaultItemSelected(){
        final TypedValue typedValue = new TypedValue();
        Main2Activity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.data;
        if(positionSelected ==0 || positionSelected == 1 || positionSelected ==4 || positionSelected == 2){
            if (positionSelected == 2) {
                Log.d(TAG,"click position 2 pintarlo...");
                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.imageViewDrawerIconMain);
                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.textViewDrawerItemTitleMain);
                imageViewDrawerIcon.setColorFilter(color);
                if (Build.VERSION.SDK_INT > 15) {
                    imageViewDrawerIcon.setImageAlpha(255);
                } else {
                    imageViewDrawerIcon.setAlpha(255);
                }
                textViewDrawerTitle.setTextColor(color);
                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(2).findViewById(R.id.relativeLayoutDrawerItem);
                TypedValue typedValueDrawerSelected = new TypedValue();
                getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                int colorDrawerItemSelected = typedValueDrawerSelected.data;
                colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

            }
            else {
                Log.d(TAG,"click no es position 0 despintar la posicion 0 ...");
                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.imageViewDrawerIconMain);
                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.textViewDrawerItemTitleMain);
                imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                if (Build.VERSION.SDK_INT > 15) {
                    imageViewDrawerIcon.setImageAlpha(138);
                } else {
                    imageViewDrawerIcon.setAlpha(138);
                }
                textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(2).findViewById(R.id.relativeLayoutDrawerItem);
                relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
            }
            if (positionSelected == 0) {
                Log.d(TAG,"click position 0 pintarlo...");
                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.imageViewDrawerIconMain);
                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.textViewDrawerItemTitleMain);
                imageViewDrawerIcon.setColorFilter(color);
                if (Build.VERSION.SDK_INT > 15) {
                    imageViewDrawerIcon.setImageAlpha(255);
                } else {
                    imageViewDrawerIcon.setAlpha(255);
                }
                textViewDrawerTitle.setTextColor(color);
                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(0).findViewById(R.id.relativeLayoutDrawerItem);
                TypedValue typedValueDrawerSelected = new TypedValue();
                getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                int colorDrawerItemSelected = typedValueDrawerSelected.data;
                colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

            }
            else {
                Log.d(TAG,"click no es position 0 despintar la posicion 0 ...");
                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.imageViewDrawerIconMain);
                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.textViewDrawerItemTitleMain);
                imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                if (Build.VERSION.SDK_INT > 15) {
                    imageViewDrawerIcon.setImageAlpha(138);
                } else {
                    imageViewDrawerIcon.setAlpha(138);
                }
                textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(0).findViewById(R.id.relativeLayoutDrawerItem);
                relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
            }
            if (positionSelected == 1) {
                Log.d(TAG,"click position 1 pintarlo...");
                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.imageViewDrawerIconMain);
                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.textViewDrawerItemTitleMain);
                imageViewDrawerIcon.setColorFilter(color);
                if (Build.VERSION.SDK_INT > 15) {
                    imageViewDrawerIcon.setImageAlpha(255);
                } else {
                    imageViewDrawerIcon.setAlpha(255);
                }
                textViewDrawerTitle.setTextColor(color);
                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(1).findViewById(R.id.relativeLayoutDrawerItem);
                TypedValue typedValueDrawerSelected = new TypedValue();
                getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                int colorDrawerItemSelected = typedValueDrawerSelected.data;
                colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

            }
            else {
                Log.d(TAG,"click position 1 despintar position 1...");
                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.imageViewDrawerIconMain);
                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.textViewDrawerItemTitleMain);
                imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                if (Build.VERSION.SDK_INT > 15) {
                    imageViewDrawerIcon.setImageAlpha(138);
                } else {
                    imageViewDrawerIcon.setAlpha(138);
                }
                textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(1).findViewById(R.id.relativeLayoutDrawerItem);
                relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
            }
            if (positionSelected == 4) {
                Log.d(TAG,"click position 4 pintarlo...");
                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.imageViewDrawerIconMain);
                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.textViewDrawerItemTitleMain);
                imageViewDrawerIcon.setColorFilter(color);
                if (Build.VERSION.SDK_INT > 15) {
                    imageViewDrawerIcon.setImageAlpha(255);
                } else {
                    imageViewDrawerIcon.setAlpha(255);
                }
                textViewDrawerTitle.setTextColor(color);
                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(4).findViewById(R.id.relativeLayoutDrawerItem);
                TypedValue typedValueDrawerSelected = new TypedValue();
                getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                int colorDrawerItemSelected = typedValueDrawerSelected.data;
                colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

            }
            else {
                Log.d(TAG,"click position no es 3 despintarlo...");
                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.imageViewDrawerIconMain);
                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.textViewDrawerItemTitleMain);
                imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                if (Build.VERSION.SDK_INT > 15) {
                    imageViewDrawerIcon.setImageAlpha(138);
                } else {
                    imageViewDrawerIcon.setAlpha(138);
                }
                textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(4).findViewById(R.id.relativeLayoutDrawerItem);
                relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
            }
        }
    }

    public void navigationDrawer() {
        //Setup  User data
        this.tvName.setText(Session.getUser().getFullName());
        this.tvPerfil.setText(PreferenceManager.getString(Contants.KEY_CARGO));

        // Fix right margin to 56dp (portrait)
        View drawer = findViewById(R.id.scrimInsetsFrameLayout);
        ViewGroup.LayoutParams layoutParams = drawer.getLayoutParams();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams.width = displayMetrics.widthPixels - (56 * Math.round(displayMetrics.density));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = displayMetrics.widthPixels + (20 * Math.round(displayMetrics.density)) - displayMetrics.widthPixels / 2;
        }

        // Setup Drawer Icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        // statusBar color behind navigation drawer
        TypedValue typedValueStatusBarColor = new TypedValue();
        Main2Activity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueStatusBarColor, true);
        final int colorStatusBar = typedValueStatusBarColor.data;
        mDrawerLayout.setStatusBarBackgroundColor(colorStatusBar);

        // Setup RecyclerView inside drawer
        /*recyclerViewDrawer = (RecyclerView) findViewById(R.id.recyclerViewDrawerMain);
        recyclerViewDrawer.setHasFixedSize(true);
        recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(Main2Activity.this));

        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        final String[] drawerTitles = getResources().getStringArray(R.array.drawer);
        final TypedArray drawerIcons = getResources().obtainTypedArray(R.array. drawerIcons);
        for (int i = 0; i < drawerTitles.length; i++) {
            drawerItems.add(new DrawerItem(drawerTitles[i], drawerIcons.getDrawable(i)));
        }
        drawerIcons.recycle();
        adapterDrawer = new DrawerAdapter(drawerItems);
        recyclerViewDrawer.setAdapter(adapterDrawer);
        */
        final TypedValue typedValue = new TypedValue();
        Main2Activity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.data;
        /*if(recyclerViewDrawer!=null){
            Log.d(TAG,"Si hay recyclerViewDrawer...");
            Log.d(TAG,"Canitdad de item drawer"+drawerItems.size());
        }*/
        //TODO try to get status bar translucent in landscape mode (lollipop)
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(android.R.color.transparent));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setDrawersMain();
        recyclerViewDrawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG,"rv.GlobalLayoutLister...");
                Log.d(TAG,"positionSelected:"+positionSelected+"...");
                if (positionSelected == 0|| positionSelected==1 || positionSelected == 4 ) {
                    Log.d(TAG,"i==0 || i==1 || i==4|| i==2 en drawertitles...");
                    //
                    /*if (positionSelected == 2) {
                        Log.d(TAG,"i==0 pintar Agenda en drawertitles...");
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.imageViewDrawerIconMain);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.textViewDrawerItemTitleMain);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(2).findViewById(R.id.relativeLayoutDrawerItem);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);
                    }else{
                        Log.d(TAG,"i!=0 despintar  Agenda  en drawertitles...");
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.imageViewDrawerIconMain);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.textViewDrawerItemTitleMain);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(2).findViewById(R.id.relativeLayoutDrawerItem);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                    }*/
                    //
                    if (positionSelected == 0) {
                        Log.d(TAG,"i==0 pintar Agenda en drawertitles...");
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.imageViewDrawerIconMain11);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.textViewDrawerItemTitleMain11);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(0).findViewById(R.id.relativeLayoutDrawerItem11);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);
                    }else{
                        Log.d(TAG,"i!=0 despintar  Agenda  en drawertitles...");
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.imageViewDrawerIconMain11);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.textViewDrawerItemTitleMain11);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(0).findViewById(R.id.relativeLayoutDrawerItem11);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                    }
                    if (positionSelected == 1) {
                        Log.d(TAG,"i==1 pintar Prospecto en drawertitles...");
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.imageViewDrawerIconMain11);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.textViewDrawerItemTitleMain11);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(1).findViewById(R.id.relativeLayoutDrawerItem11);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);
                    }else{
                        Log.d(TAG,"i!=1 despintar Prospecto en drawertitles...");
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.imageViewDrawerIconMain11);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.textViewDrawerItemTitleMain11);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(1).findViewById(R.id.relativeLayoutDrawerItem11);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                    }

                    if (positionSelected == 4) {
                        Log.d(TAG,"i==1 pintar Informacion en drawertitles...");
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.imageViewDrawerIconMain11);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.textViewDrawerItemTitleMain11);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(4).findViewById(R.id.relativeLayoutDrawerItem11);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);
                    }else{
                        Log.d(TAG,"i!=1 despintar Prospecto en drawertitles...");
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.imageViewDrawerIconMain11);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.textViewDrawerItemTitleMain11);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(4).findViewById(R.id.relativeLayoutDrawerItem11);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                    }

                }

                else {
                    Log.d(TAG,"despintar las demas en drawertitles...");
                    ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(positionSelected).findViewById(R.id.imageViewDrawerIconMain11);
                    TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(positionSelected).findViewById(R.id.textViewDrawerItemTitleMain11);
                    imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                    if (Build.VERSION.SDK_INT > 15) {
                        imageViewDrawerIcon.setImageAlpha(138);
                    } else {
                        imageViewDrawerIcon.setAlpha(138);
                    }
                    textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                    RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(positionSelected).findViewById(R.id.relativeLayoutDrawerItem11);
                    relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                }
                // unregister listener (this is important)
                recyclerViewDrawer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    public void openDrawer(){
        if(mDrawerLayout!=null){
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    //region Methods
    private void toolbarStatusBar() {
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
    }

    public void toogleButtonDrawer() {
        //imageViewToogle = (ImageView) findViewById(R.id.imageViewToggle);
        toggleButtonDrawer = (ToggleButton) findViewById(R.id.toggleButtonDrawer);
        linearLayoutMain = (LinearLayout) findViewById(R.id.linearLayoutMain);
        //linearLayoutSecond = (LinearLayout) findViewById(R.id.linearLayoutSecond);
        toggleButtonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleButtonDrawer.isChecked()) {
                    toggleButtonDrawer.setChecked(false);
                    //imageViewToogle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_drop_down));
                    //linearLayoutMain.setVisibility(View.VISIBLE);
                    //linearLayoutSecond.setVisibility(View.GONE);
                }
                if (toggleButtonDrawer.isChecked()) {
                    toggleButtonDrawer.setChecked(true);
                    //imageViewToogle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_drop_up));
                    //linearLayoutMain.setVisibility(View.GONE);
                    //linearLayoutSecond.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void configureClickItemDefault(){
        /*rlInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Information Clicked...");
                invokeInformation();
            }
        });*/

        /*rlSincronizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Sincronizacion clicked...");
                invokeSync("Sincronizando","Espere porfavor...");
            }
        });*/
        /*rlCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cerrar Sesion clicked...");
                invokeCerrarSesion();
            }
        });*/
    }
    //endregion

    public void invokeSync(String titulo,String mensaje){
        if (!ConnectionUtils.isNetAvailable(this)) {
            Log.d(TAG,"!ConnectionUtils.isNetAvailable(this)...");
            Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG,"ConnectionUtils.isNetAvailable(this)...");
            progressDialog.setTitle(titulo);
            progressDialog.setMessage(mensaje);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_VENTA_NUEVA);
            requestSyncMain(bundle);
        }
    }

    private  void invokeCerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.title_option_setcerrar_sesion));
        builder.setMessage(getResources().getString(R.string.message_cerrar_sesion))
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Log.d(TAG, "Cerrando Sesion...");
                        dialog.dismiss();
                        logOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logOut(){
        PreferenceManager.setValue(Constants.KEY_LAST_LOGIN, Session.getUser().getLogonName());
        PreferenceManager.setValue(Constants.KEY_LAST_PASS, Session.getUser().getPassword());
        PreferenceManager.setValue(Constants.KEY_LAST_TOKEN, "temp");
        //SyncAudit.insert(SyncAdapter.SYNC_TYPE_GEOPOLITICAL,SyncAdapter.SYNC_EVENT_SUCCESS);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent updateServiceIntent = new Intent(this, EnviarUbicacionReceiver.class);
        PendingIntent pendingUpdateIntent = PendingIntent.getService(this, 0, updateServiceIntent, 0);
        alarmManager.cancel(pendingUpdateIntent);
        startActivity(new Intent(this, StartActivity.class));
        this.finish();
        Session.logOut();
    }

    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 1:
                Log.d(TAG,"item selected:prospecto");
                //sharedPreferences.edit().putInt("FRAGMENT", 0).apply();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ProspectoFragment prospectoFragment = new ProspectoFragment();
                prospectoFragment.setHasOptionsMenu(true);
                fragmentTransaction.replace(R.id.fragment, prospectoFragment);
                fragmentTransaction.commit();
                break;
            case 0:
                Log.d(TAG,"item selected:agenda");
                //sharedPreferences.edit().putInt("FRAGMENT", 1).apply();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                AgendaFragment agendaFragment = new AgendaFragment();
                agendaFragment.setHasOptionsMenu(true);
                fragmentTransaction.replace(R.id.fragment, agendaFragment);
                fragmentTransaction.commit();
                break;
            /*case 2:
                Intent intent = new Intent(Main2Activity.this,ReporteActivity.class);
                intent.putExtra("Option","REPVTNGER");
                startActivity(intent);
                break;*/
            case 3:
                Log.d(TAG,"item selected:sincronizacion");
                invokeSync("Sincronizando","Espere porfavor...");
                break;
            case 4:
                Log.d(TAG,"item selected:informacion");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                InformationFragment informationFragment = new InformationFragment();
                informationFragment.setHasOptionsMenu(true);
                fragmentTransaction.replace(R.id.fragment, informationFragment);
                fragmentTransaction.commit();
                break;
            case 5:
                Log.d(TAG,"item selected:cerrarsion");
                invokeCerrarSesion();
                break;

            //Case by expanded:
            case 8:
                Log.d(TAG,"item selected:sincronizacion");
                invokeSync("Sincronizando","Espere porfavor...");
                break;
            case 9:
                Log.d(TAG,"item selected:informacion");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                InformationFragment informationFragment1 = new InformationFragment();
                informationFragment1.setHasOptionsMenu(true);
                fragmentTransaction.replace(R.id.fragment, informationFragment1);
                fragmentTransaction.commit();
                break;
            case 10:
                Log.d(TAG,"item selected:cerrarsion");
                invokeCerrarSesion();
                break;

        }
    }

    public void initLocation(){
        locationProvider=new LocationProvider(this,this);
    }

    private void showDialogGps(){
        if(dialogGps!=null){dialogGps.dismiss();}
        dialogGps = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle)
                .setTitle(getResources().getString(R.string.appname_marketforce))
                .setMessage("Debe Mantener el GPS encendido porfavor.")
                .setCancelable(false)
                .setNeutralButton(R.string.gpsdialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"Activar GPS onclickdialog...");
                        locationProvider.request();
                    }
                }).create();
        dialogGps.show();
    }

    private BroadcastReceiver syncFinishedReceiverr = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //resetRotation();
            Log.d(TAG,"syncFinishedReceiver...");
            MessageCollection messages = (MessageCollection) intent.getExtras()
                    .getParcelable(rp3.content.SyncAdapter.NOTIFY_EXTRA_MESSAGES);
            Bundle bundle = intent.getExtras().getBundle(
                    rp3.content.SyncAdapter.NOTIFY_EXTRA_DATA);
            onSyncComplete(bundle,messages);
        }
    };

    public  void requestSyncMain(Bundle settingsBundle) {
        if (ConnectionUtils.isNetAvailable(this)) {
            PreferenceManager.close();
            SyncUtils.requestSync(settingsBundle);
            //lockRotation();
        } else {
            MessageCollection mc = new MessageCollection();
            mc.addErrorMessage(this.getResources().getString(
                    rp3.core.R.string.message_error_sync_no_net_available).toString());
            onSyncComplete(new Bundle(),mc);
        }
    }

    public void invokeSyncLlamada(String titulo,String mensaje,Bundle todo){
        if (!ConnectionUtils.isNetAvailable(this)) {
            Log.d(TAG,"!ConnectionUtils.isNetAvailable(this)...");
            Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG,"ConnectionUtils.isNetAvailable(this)...");
            progressDialog.setTitle(titulo);
            progressDialog.setMessage(mensaje);
            progressDialog.setCancelable(false);
            progressDialog.show();
            requestSyncMain(todo);
        }
    }

    public void onSyncComplete(Bundle bundle,MessageCollection messageCollection){
        Log.d(TAG,"onSyncComplete...");
        if (bundle.containsKey(SyncAdapter.ARG_SYNC_TYPE) && bundle.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_UPDATE_PROSPECTO)) {
            Log.d(TAG,"onsyncfinish Actualizar Prospectos...");
            if(progressDialog!=null){
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    positionSelected=positiSelectedLast;
                    try{
                        //setDefaultItemSelected();
                        //setFragment(1);
                        try{
                            //setFragment(positiSelectedLast);
                            android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                            if (fragment instanceof ProspectoFragment ) {
                                Log.v(TAG, "find the current fragment");
                                ProspectoFragment agendaFragment = (ProspectoFragment) fragment;
                                agendaFragment.onResume();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            if (messageCollection.hasErrorMessage()) {
                Log.d(TAG,"hasErrorSync..");
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                showDialogMessage(messageCollection,null);
            }
        }
        else if (bundle.containsKey(SyncAdapter.ARG_SYNC_TYPE) && bundle.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION)) {
            Log.d(TAG,"onsyncfinish SendNotification...");
            if(progressDialog!=null){
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    try{
                        setFragment(1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            if (messageCollection.hasErrorMessage()) {
                Log.d(TAG,"hasErrorSync..");
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                showDialogMessage(messageCollection,null);
            }
        }
        else if (bundle.containsKey(SyncAdapter.ARG_SYNC_TYPE) && bundle.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_VENTA_NUEVA)) {
            Log.d(TAG,"onsyncfinish venta nueva...");
            if(progressDialog!=null){
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                            try{
                                setFragment(positiSelectedLast);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                }
            }
            if (messageCollection.hasErrorMessage()) {
                Log.d(TAG,"hasErrorSync..");
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                showDialogMessage(messageCollection,null);
            }
        }
        else if (bundle.containsKey(SyncAdapter.ARG_SYNC_TYPE) && bundle.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA)) {
            Log.d(TAG,"onsyncfinish actualizar llamada...");
            if(progressDialog!=null){
                if(progressDialog.isShowing()){

                    try{
                        if(SessionManager.getInstance(this).getLlamada()!=null){
                            if(SessionManager.getInstance(this).getLlamada()!=null){
                                if(SessionManager.getInstance(this).getLlamada().equalsIgnoreCase("LlamadaPrograma")){
                                    Log.d(TAG,"Desea programa....agendarle una visita...");
                                    Intent intent = new Intent(this, rp3.auna.CrearVisitaActivity.class);
                                    Bundle bundle1 = new Bundle();
                                    if(location!=null){
                                        if(location.getLatitude()>0 && location.getLongitude()>0){
                                            bundle1.putDouble("Latitud",location.getLatitude());
                                            bundle1.putDouble("Longitud",location.getLongitude());
                                        }else{
                                            bundle1.putDouble("Latitud",0.0);
                                            bundle1.putDouble("Longitud",0.0);
                                        }
                                    }else{
                                        bundle1.putDouble("Latitud",0.0);
                                        bundle1.putDouble("Longitud",0.0);
                                    }

                                    bundle1.putInt("Service",2);
                                    ProspectoVtaDb prospecto = SessionManager.getInstance(this).getProspectoLlamada();
                                    bundle1.putLong("Id",prospecto.getID());
                                    bundle1.putInt("IdProspecto",prospecto.getIdProspecto());
                                    bundle1.putString("Prospecto",prospecto.getNombre());
                                    if(prospecto.getDireccion1()!=null){
                                        bundle1.putString("Direccion",prospecto.getDireccion1());
                                    }else if(prospecto.getDireccion2()!=null){
                                        bundle1.putString("Direccion",prospecto.getDireccion2());
                                    }else{
                                        bundle1.putString("Direccion","");
                                    }
                                    bundle1.putInt("Estado",0);
                                    bundle1.putInt("VisitaId",0);
                                    int idMaxLlamada = LlamadaVta.getMaxIdLlamadaVta(Utils.getDataBase(this));
                                    bundle1.putInt("LlamadaId",idMaxLlamada);
                                    intent.putExtras(bundle1);
                                    SessionManager.getInstance(this).removeProspectoLlamada();
                                    startActivityForResult(intent,REQUEST_LLAMADA_SI_DESEA_PROGRAMA);
                                }else{
                                    Log.d(TAG,"Reprogrmar...");
                                    final Intent intent = new Intent(this, rp3.auna.CrearLlamadaActivity.class);
                                    final Bundle bundle1 = new Bundle();
                                    if(location!=null){
                                        if(location.getLatitude()>0 && location.getLongitude()>0){
                                            bundle1.putDouble("Latitud",location.getLatitude());
                                            bundle1.putDouble("Longitud",location.getLongitude());

                                        }else{
                                            bundle1.putDouble("Latitud",0.0);
                                            bundle1.putDouble("Longitud",0.0);
                                        }
                                    }else{
                                        //LocationUtils.getLocation(this, new LocationUtils.OnLocationResultListener() {
                                            //@Override
                                            //public void getLocationResult(Location location) {
                                                bundle1.putDouble("Latitud",0.0);
                                                bundle1.putDouble("Longitud",0.0);

                                        //    }
                                        //});
                                    }
                                    intent.putExtras(bundle1);
                                    startActivityForResult(intent,REQUEST_LLAMADA_REPROGRAMADA);
                                }
                            }else{
                                Log.d(TAG,"Cancelar...");
                                SessionManager.getInstance(this).removeLlamada();
                                Intent intent = new Intent(this,StartActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Log.d(TAG,"Llamada actualizada desde agenda por gestion...");
                            setFragment(0);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }
            if (messageCollection.hasErrorMessage()) {
                Log.d(TAG,"hasErrorSync..");
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                showDialogMessage(messageCollection,null);
            }
        }

        else if (bundle.containsKey(SyncAdapter.ARG_SYNC_TYPE) && bundle.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_UPDATE_VISITA)) {
            Log.d(TAG,"onsyncfinish actualizar visita...");
            if(progressDialog!=null){
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    try{
                        if(SessionManager.getInstance(this).getVisitaReprogramada()!=null){
                            Log.d(TAG,"Reprogrmar...");
                            Intent intent = new Intent(this, rp3.auna.CrearVisitaActivity.class);
                            Bundle bundle1 = new Bundle();
                            if(location!=null){
                                if(location.getLatitude()>0 && location.getLongitude()>0){
                                    bundle1.putDouble("Latitud",location.getLatitude());
                                    bundle1.putDouble("Longitud",location.getLongitude());
                                }else{
                                    bundle1.putDouble("Latitud",0.0);
                                    bundle1.putDouble("Longitud",0.0);
                                }
                            }else{
                                bundle1.putDouble("Latitud",0.0);
                                bundle1.putDouble("Longitud",0.0);
                            }
                            ProspectoVtaDb prospecto = null;
                            Log.d(TAG,"Id Prospecto en visita reprogramar d tabcita:"+SessionManager.getInstance(this).getVisitaReprogramada().toString());
                            List<ProspectoVtaDb> list = ProspectoVtaDb.getAll(Utils.getDataBase(getApplicationContext()));
                            if(list!=null){
                                Log.d(TAG,"list!=null: Prospectos Sincronizados:"+list.size());
                                if(SessionManager.getInstance(this).getVisitaReprogramada().getInsertado()==1){
                                    for(ProspectoVtaDb obj:list){
                                        if(SessionManager.getInstance(this).getVisitaReprogramada().getIdCliente()==obj.getID()){
                                            prospecto = obj;
                                            Log.d(TAG,"break:"+prospecto.toString());
                                            break;
                                        }
                                    }
                                }else{
                                    for(ProspectoVtaDb obj:list){
                                        if(SessionManager.getInstance(this).getVisitaReprogramada().getIdCliente()==obj.getIdProspecto()){
                                            prospecto = obj;
                                            Log.d(TAG,"break:"+prospecto.toString());
                                            break;
                                        }
                                    }
                                }

                                bundle1.putInt("Service",5);
                                bundle1.putLong("Id",prospecto.getID());
                                bundle1.putInt("IdProspecto",prospecto.getIdProspecto());
                                bundle1.putString("Prospecto",prospecto.getNombre());
                                bundle1.putString("Direccion",SessionManager.getInstance(this).getVisitaReprogramada().getIdClienteDireccion());
                                bundle1.putInt("Estado",0);
                                bundle1.putInt("VisitaId",SessionManager.getInstance(this).getVisitaReprogramada().getIdVisita());
                                SessionManager.getInstance(this).removeVisitaReprogramada();
                                intent.putExtras(bundle1);
                                startActivityForResult(intent,REQUEST_VISITA_REPROGRAMADA);
                            }else{
                                Log.d(TAG,"Cancelar...");
                                SessionManager.getInstance(this).removeVisitaReprogramada();
                                Intent intent1 = new Intent(this,Main2Activity.class);
                                startActivity(intent1);
                                finish();
                            }
                        }else{
                            Log.d(TAG,"Llamada actualizada desde agenda por gestion...");
                            setFragment(0);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

            }
            if (messageCollection.hasErrorMessage()) {
                Log.d(TAG,"hasErrorSync..");
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                showDialogMessage(messageCollection,null);
            }
        }
        if(progressDialog!=null){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
        }

        //Refresh en Llamadas
        else if(bundle.containsKey(SyncAdapter.ARG_SYNC_TYPE) && bundle.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_REFRESH_LLAMADA)){
            Log.d(TAG,"onsyncfinish refresh agenda...");
            if(progressDialog!=null){
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    //Execute...
                    Bundle msj = new Bundle();
                    msj.putString("Llamada","Refresh");
                    EventBus.getBus().post(new Events.Message(msj));
                }
            }
            if (messageCollection.hasErrorMessage()) {
                Log.d(TAG,"hasErrorSync..");
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                showDialogMessage(messageCollection,null);
            }
        }
        //Refresh en Visitas
        else if(bundle.containsKey(SyncAdapter.ARG_SYNC_TYPE) && bundle.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_REFRESH_VISITA)){
            Log.d(TAG,"onsyncfinish refresh visita...");
            if(progressDialog!=null){
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    //Execute...
                    Bundle msj = new Bundle();
                    msj.putString("Llamada","Refresh");
                    EventBus.getBus().post(new Events.Message(msj));
                }
            }
            if (messageCollection.hasErrorMessage()) {
                Log.d(TAG,"hasErrorSync..");
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                showDialogMessage(messageCollection,null);
            }
        }
        else if(bundle.containsKey(SyncAdapter.ARG_SYNC_TYPE) && bundle.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_REFRESH_REPROGRAM_CANCELED_VISITA)){
            Log.d(TAG,"onsyncfinish SYNC_TYPE_REFRESH_REPROGRAM_CANCELED_VISITA...");
            if(progressDialog!=null){
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    //Execute...
                    try{
                        //setFragment(positiSelectedLast);
                        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                        if (fragment instanceof AgendaFragment ) {
                            Log.v(TAG, "find the current fragment");
                            AgendaFragment agendaFragment = (AgendaFragment) fragment;
                            agendaFragment.setTabSelection(1);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            if (messageCollection.hasErrorMessage()) {
                Log.d(TAG,"hasErrorSync..");
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                showDialogMessage(messageCollection,null);
            }
        }

        //Alarmar
        //pruebaAlarm(Utils.getDataBase()getApplicationContext());
    }

    public void showDialogMessage(MessageCollection messages,final SimpleCallback callback) {
        final CharSequence[] items = new CharSequence[messages.getCuount()];
        int i = 0;
        for (Message m : messages.getMessages()) {
            items[i++] = m.getText();
        }
        Log.d(TAG,"showdialogmessageerror...");
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(Main2Activity.this,R.style.AppCompatAlertDialogStyle)
                        .setTitle(rp3.core.R.string.app_name)
                        .setItems(items, null)
                        .setPositiveButton(rp3.core.R.string.action_accept,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if (callback != null)
                                            callback.onExecute();
                                    }
                                }).setCancelable(true);
                dialog.show();
            }
        });

    }

    private void validatePermissionApp() {
        int fineCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int fineStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int fineContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        int fineTelephonyPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        permissionsAll = new ArrayList<>();
        permissionsAll.add(Manifest.permission.CAMERA);
        permissionsAll.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsAll.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsAll.add(Manifest.permission.GET_ACCOUNTS);
        permissionsAll.add(Manifest.permission.CALL_PHONE);


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d(TAG,"version >Mr1 permissions runtime required...");
            if (fineStoragePermission != PackageManager.PERMISSION_GRANTED || fineCameraPermission != PackageManager.PERMISSION_GRANTED
                    || fineLocationPermission != PackageManager.PERMISSION_GRANTED ||fineContactPermission != PackageManager.PERMISSION_GRANTED
                   || fineTelephonyPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissionsAll.toArray(new String[permissionsAll.size()]), MY_PERMISSIONS);
                Log.d(TAG,"Algun permiso falto ser aprobado...");
                return;
            }
            else{
                Log.d(TAG,"Todos los permisos han sido aceptados por el usuario...");
            }
        }
        else {
            Log.d(TAG,"Todos los permisos han sido aceptados porque es una version < Lollipop_Mr1...");
        }
    }

    private void validateVisitaSession(){
        Log.d(TAG,"validateVisitaSession...");
        //Iniciar la Cotizacion Inicial
        VisitaVta visitaVta = SessionManager.getInstance(this).getVisitaSession();
        if(visitaVta!=null){
            if(visitaVta.getEstado()==1){
                Intent intent = new Intent(this, CotizacionActivity.class);
                intent.putExtra("Estado",1);
                startActivityForResult(intent,REQUEST_VISITA_COTIZACION_NUEVO);
            }else if(visitaVta.getEstado()==3){
                //Iniciar la visita fisica (Subir Documentos)
                /*Intent intent = new Intent(this, VisitaMediaActivity.class);
                intent.putExtra("Estado",1);
                intent.putExtra("VisitaId",visitaVta.getVisitaId());
                startActivityForResult(intent,REQUEST_VISITA_PAGO_FISICO_DOCUMENTOS);*/
            }else if(visitaVta.getEstado()==6){
                Intent intent = new Intent(this, CotizacionActivity.class);
                intent.putExtra("Estado",1);
                startActivityForResult(intent,REQUEST_VISITA_COTIZACION_NUEVO);
            }
        }else{
            SessionManager.getInstance(this).removeVisitaSession();
        }

    }

    //region Ciclo de vida

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
        if(busFlag){
            EventBus.getBus().unregister(this);
            busFlag=false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();


        locationProvider.disconnect();
        Log.d(TAG,"onStop...");
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.d(TAG,"onUserLeaveHint...");
    }

    public void sendNotification(String mensaje){
        Bundle todo = new Bundle();
        todo.putInt("id_agente",PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR));
        todo.putString("titulo","RP3 Market Force");
        todo.putString("mensaje",mensaje);
        todo.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION);
        requestSyncMain(todo);
        //invokeSyncLlamada("Prueba","Notificando...",todo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
        if(!busFlag){
            EventBus.getBus().register(this);
            busFlag = true;
        }
        //alarmTest();
        SessionManager.getInstance(this).removeLlamadaGestion();
        locationProvider.connect();
        //sendNotification();
        //Util.setAlarmAgenda(Main2Activity.this);
        try {
            registerReceiver(syncFinishedReceiverr, new IntentFilter(
                    rp3.content.SyncAdapter.SYNC_FINISHED));
        } catch (IllegalArgumentException e) {
            Log.d(TAG,"receiver:"+e.getMessage());
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG,"onPostResume...");
        validateVisitaSession();
        if(REQUEST_STATE == REQUEST_VISITA_COTIZACION_NUEVO){
            if(RESULT_STATE == RESULT_VISITA_REPROGRAMADA_FINALIZADA ){

            }else if(RESULT_STATE == RESULT_VISITA_CANCELADA_FINALIZADA){

            }
        }else{

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
        //locationProvider.disconnect();

        try {
            unregisterReceiver(syncFinishedReceiverr);
        } catch (IllegalArgumentException e) {
            Log.d(TAG,"Receiver:"+e.getMessage());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
        Log.d(TAG,"onBackPressed...");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS) {
            if (grantResults.length > 0) {
                Log.d(TAG,"grantResults > 0");
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED &&
                        grantResults[2]==PackageManager.PERMISSION_GRANTED && grantResults[3]==PackageManager.PERMISSION_GRANTED
                        && grantResults[4]==PackageManager.PERMISSION_GRANTED ){
                    Log.d(TAG,"All grant result Aceptados...");
                    validatePermissionApp();
                }
                else {
                    Log.d(TAG,"Algun grant result no ha sido aceptado...");
                    validatePermissionApp();
                }
            }
            else {
                Log.d(TAG,"no hay grant results...");
                validatePermissionApp();
            }
        }
    }

    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        REQUEST_STATE = requestCode;
        RESULT_STATE = resultCode;
        Log.d(TAG,"requestCode:"+requestCode+" resultCode:"+resultCode);
        if(requestCode == REQUEST_PROSPECTO_NUEVO && resultCode == RESULT_PROSPECTO_NUEVO){
            Log.d(TAG,"REQUEST_PROSPECTO_NUEVO...RESULT_PROSPECTO_NUEVO...");
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_INSERTAR_PROSPECTOVTA);
            //requestSyncMain(bundle);
            invokeSync("Prospecto","Actualizando prospectos...");
        }
        else if(requestCode == REQUEST_LLAMADA_NUEVO && resultCode == RESULT_LLAMADA_NUEVO){
            Log.d(TAG,"REQUEST_LLAMADA_NUEVO...RESULT_LLAMADA_NUEVO...");
            List<rp3.auna.models.ventanueva.LlamadaVta> llamadaVtas = rp3.auna.models.ventanueva.LlamadaVta.getLlamadasInsert(Utils.getDataBase(this));
            Log.d(TAG,"Cantidad de llamadas insertadas:"+llamadaVtas.size());
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
            //requestSyncMain(bundle);
            invokeSync("Agenda","Actualizando llamadas...");
        }
        else if(requestCode == REQUEST_LLAMADA_REPROGRAMADA && resultCode == RESULT_LLAMADA_REPROGRAMADA){
            Log.d(TAG,"REQUEST_LLAMADA_REPROGRAMADA...RESULT_LLAMADA_REPROGRAMADA...");
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
            //requestSyncMain(bundle);
            invokeSync("Agenda","Actualizando llamadas...");
        }else if(requestCode == REQUEST_LLAMADA_SI_DESEA_PROGRAMA && resultCode == RESULT_LLAMADA_SI_DESEA_PROGRAMA){
            Log.d(TAG,"VISITA: REQUEST_LLAMADA_SI_DESEA_PROGRAMA...RESULT_LLAMADA_SI_DESEA_PROGRAMA...");
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_VISITA);
            //requestSyncMain(bundle);
            invokeSync("Agenda","Actualizando llamadas...");
        }
        else if(requestCode == REQUEST_VISITA_NUEVO && resultCode == RESULT_VISITA_NUEVO){
            Log.d(TAG,"REQUEST_VISITA_NUEVO...RESULT_VISITA_NUEVO...");
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_VISITA);
            //requestSyncMain(bundle);
            invokeSync("Agenda","Actualizando Visitas...");
        }
        /*if(requestCode == REQUEST_VISITA_COTIZACION_NUEVO){
            Log.d(TAG,"REQUEST_VISITA_COTIZACION_NUEVO...Verificar su RESULT...");
            if(resultCode == RESULT_VISITA_NUEVA_PAGO_FISICO){
                Log.d(TAG,"RESULT == RESULT_VISITA_NUEVA_PAGO_FISICO...");
                Log.d(TAG,"Se creo una visita por y el tipo de pago fue fisico se necesita registrar las visitas en agenda...");
                invokeSync("Agenda","Actualizando Visitas...");
            }
        }*/
        else if(requestCode == REQUEST_VISITA_COTIZACION_NUEVO){
            Log.d(TAG,"REQUEST_VISITA_COTIZACION_NUEVO...Verificar su RESULT...");
            if(resultCode == RESULT_VISITA_NUEVA_PAGO_FISICO){
                Log.d(TAG,"RESULT == RESULT_VISITA_NUEVA_PAGO_FISICO...");
                Log.d(TAG,"Se creo una visita por y el tipo de pago fue fisico se necesita registrar las visitas en agenda...");
                invokeSync("Agenda","Actualizando Visitas...");
            }
            /*else if(resultCode == RESULT_VISITA_REPROGRAMM_CANCEL){
                Log.d(TAG,"RESULT == RESULT_VISITA_REPROGRAMADA_FINALIZADA...");
                Log.d(TAG,"Se creo una visita y se reprogramo..");
                SessionManager.getInstance(getApplicationContext()).removeVisitaSession();
                invokeSync("Agenda","Actualizando...");
            }*/
            /*else if(resultCode == RESULT_VISITA_REPROGRAMM_CANCEL){
                Log.d(TAG,"RESULT == RESULT_VISITA_CANCELADA_FINALIZADA...");
                Log.d(TAG,"Se creo una visita y se Cancelo..");
                SessionManager.getInstance(getApplicationContext()).removeVisitaSession();
                Bundle todo = new Bundle();
                todo.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_REFRESH_REPROGRAM_CANCELED_VISITA);
                invokeSyncLlamada("Agenda","Actualizando...",todo);
            }*/
            /*else if(resultCode == RESULT_VISITA_EFECTIVO_REFERIDO){
                Log.d(TAG,"RESULT == RESULT_VISITA_EFECTIVO_REFERIDO...");
                Log.d(TAG,"Se creo una visita y se reprogramo..");
                //invokeSync("Agenda","Actualizando Visitas...");
            }*/
            //Verificar el resultado con respecto a los referidos
            else if(resultCode == RESULT_VISITA_ONLINE_REFERIDO){
                Log.d(TAG,"RESULT == RESULT_VISITA_ONLINE_REFERIDO...");
                Log.d(TAG,"Se creo una visita se finalizo en forma de pago online...ahora solicitar referidos..");
                Intent intent = new Intent(this,ProspectoActivity.class);
                Bundle todo = new Bundle();
                todo.putInt("Opcion",3);
                intent.putExtras(todo);
                startActivityForResult(intent,REQUEST_REFERIDOS_FROM_VISITA);
            }
            else if(resultCode == RESULT_VISITA_EFECTIVO_REFERIDO){
                Log.d(TAG,"RESULT == RESULT_VISITA_EFECTIVO_REFERIDO...");
                Log.d(TAG,"Se creo una visita se finalizo en forma de pago efectivo...ahora solicitar referidos");
                Intent intent = new Intent(this,ProspectoActivity.class);
                Bundle todo = new Bundle();
                todo.putInt("Opcion",3);
                intent.putExtras(todo);
                startActivityForResult(intent,REQUEST_REFERIDOS_FROM_VISITA);
            }
            else if(resultCode == RESULT_VISITA_REGULAR_REFERIDO){
                Log.d(TAG,"RESULT == RESULT_VISITA_REGULAR_REFERIDO...");
                Log.d(TAG,"Se creo una visita, se finalizo en forma de pago regular...ahora solicitar referidos");
                Intent intent = new Intent(this,ProspectoActivity.class);
                Bundle todo = new Bundle();
                todo.putInt("Opcion",3);
                intent.putExtras(todo);
                startActivityForResult(intent,REQUEST_REFERIDOS_FROM_VISITA);
            }

            //Control de cambios al reprogramar o cancelar visita
            else if(resultCode == RESULT_VISITA_REPROGRAMM_CANCEL){
                Log.d(TAG,"resultCode == RESULT_VISITA_REPROGRAMM_CANCEL)...");
                Bundle todo = new Bundle();
                SessionManager.getInstance(this).removeVisitaSession();
                todo.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_REFRESH_REPROGRAM_CANCELED_VISITA);
                invokeSyncLlamada("Agenda","Actualizando...",todo);
            }
        }
        else if(requestCode == REQUEST_PROSPECTO_EDIT && resultCode == RESULT_PROSPECTO_EDIT){
            Log.d(TAG,"REQUEST_PROSPECTO_EDIT...RESULT_PROSPECTO_EDIT...");
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_PROSPECTO);
            //requestSyncMain(bundle);
            invokeSyncLlamada("RP3 Market Force","Actualizando Prospectos...",bundle);
            //invokeSync("RP3 Market Force","Actualizando Prospectos...");
        }
        else if(requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK){
            Log.d(TAG,"REQUEST_CHECK_SETTINGS...RESULT_OK...");
            Log.d(TAG,"RESULT_OK FOR GOOGLEAPICLIENT LOCATION");
            GPS = true;
            //invokeSync();
            //status=null;
        }
        else if(requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_CANCELED){
            Log.d(TAG,"REQUEST_CHECK_SETTINGS...RESULT_CANCELED...");
            Log.d(TAG,"RESULT_CANCELED FOR GOOGLEAPICLIENT LOCATION");
            GPS =false;
            showDialogGps();
            if(status!=null){
                Log.d(TAG,"status!=null...");
                try {
                    status.startResolutionForResult(this,REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    Log.d(TAG,"IntentSender.SendIntentException e...");
                    Log.d(TAG,e.getLocalizedMessage());
                }
            }
        }
        else if (requestCode == REQUEST_VISITA_REPROGRAMADA && resultCode == RESULT_VISITA_REPROGRAMADA){
            Log.d(TAG,"requestCode == REQUEST_VISITA_REPROGRAMADA && resultCode == RESULT_VISITA_REPROGRAMADA");
            Log.d(TAG,"Se creo una visita de la reprogramacion en TabCita..");
            invokeSync("Agenda","Actualizando Visitas...");
        }



        //Request and result de prospextos
        else if(requestCode == REQUEST_REFERIDOS_FROM_VISITA && resultCode == RESULT_REFERIDOS_FROM_VISITA){
            Log.d(TAG,"requestCode == REQUEST_REFERIDOS_FROM_VISITA && resultCode == RESULT_REFERIDOS_FROM_VISITA");
            invokeSync("Agenda","Actualizando...");
        }
    }

    // region Events

    @Subscribe
    public void recievedMessage(Events.Message msj){
        Log.d(TAG,"recievedMessage...");
        Bundle todo = msj.getMessage();
        if(todo!=null){
            if(todo.getString("GpsReceiver")!=null){
                if(todo.getString("GpsReceiver").equalsIgnoreCase("Apagado")){
                    Log.d(TAG,"GPS apagado...");
                    if(dialogGps!=null){
                        if(dialogGps.isShowing()){
                            dialogGps.dismiss();
                        }
                    }
                    showDialogGps();
                    GPS = false;
                    if(status!=null){
                        Log.d(TAG,"status!=null...");
                        Log.d(TAG,"status!=null...");
                        try {
                            status.startResolutionForResult(this,REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            Log.d(TAG,"IntentSender.SendIntentException e...");
                            Log.d(TAG,e.getLocalizedMessage());
                        }
                    }else{
                        Log.d(TAG,"status==null...");
                        locationProvider.request();
                    }
                }else{
                    GPS = true;
                    if(dialogGps!=null){
                        if(dialogGps.isShowing()){
                            dialogGps.dismiss();
                        }
                    }
                    Log.d(TAG,"GPS encendido...");
                }
            }
        }

    }

    @Override
    public void handleNewLocation(Location location) {
        //Log.d(TAG,"handleNewLocation...");
        if(this.location==null){
            //Log.d(TAG,"location==null...");
            if(location!=null){
                //Log.d(TAG,"lastLocation!=null...");
                this.location = location;
            }else{
                // Log.d(TAG,"lastLocation==null...");
            }
        }else{
            //Log.d(TAG,"location!=null...");
            if(location!=null){
                // Log.d(TAG,"lastLocation!=null...");
                this.location = location;
            }else{
                // Log.d(TAG,"lastLocation==null...");
            }
        }
    }

    @Override
    public void handleConnectionFailed(String failed) {
        Log.d(TAG,"handleConnectionFailed...");
        Log.d(TAG,"handleConnectionFailed:"+failed);
    }

    @Override
    public void handleConnectionSuccess(String success) {
        Log.d(TAG,"handleConnectionSuccess...");
        Log.d(TAG,"handleConnectionSuccess:"+success);
    }

    @Override
    public void handleSettingsSuccess(String success) {
        Log.d(TAG,"handleSettingsSuccess...");
        Log.d(TAG,"handleSettingsSuccess:"+success);
    }

    @Override
    public void handleSettingsResolutionRequired(String resolutionRequired, Status status, LocationSettingsStates state) {
        Log.d(TAG,"handleSettingsResolutionRequired...");
        Log.d(TAG,"handleSettingsResolutionRequired:"+resolutionRequired);
        try {
            this.status=status;
            GPS = false;
            status.startResolutionForResult(this,REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            Log.d(TAG,"IntentSender.SendIntentException:"+e.getLocalizedMessage());
        }
    }

    @Override
    public void handleSettingsChangeUnavailable(String changeUnavailable) {
        Log.d(TAG,"handleSettingsChangeUnavailable...");
        Log.d(TAG,"changeUnavailable:"+changeUnavailable);
    }

    @Override
    public void handleLastLocation(Location lastLocation) {
        //Log.d(TAG,"handleLastLocation...");
        if(location==null){
            // Log.d(TAG,"location==null...");
            if(lastLocation!=null){
                // Log.d(TAG,"lastLocation!=null...");
                location = lastLocation;
            }else{
                // Log.d(TAG,"lastLocation==null...");
            }
        }else{
            //  Log.d(TAG,"location!=null...");
        }
    }

    //endregion

    public static void pruebaAlarm(DataBase db ,Context context){
        Log.d(TAG,"pruebaAlarm...");
        try{
            final Calendar hoy = Calendar.getInstance();
            final long ONE_MINUTE_IN_MILLIS=60000;



            //region Alarma de Llamadas
            //Llamadas
            ApplicationParameter tiempoNotificar = ApplicationParameter.getParameter(db, rp3.auna.util.constants.Constants.PARAMETERID_ALERTALLAMADA, rp3.auna.util.constants.Constants.LABEL_LLAMADAS);
            final int value = Integer.parseInt(tiempoNotificar.getValue());
            Log.d(TAG,"Tiempo para notificar llamada:"+value+" minutos");
            List<LlamadaVta> llamadas = LlamadaVta.getLlamadasPendienteAll(db,value);
            if(llamadas.size()>0){
                Log.d(TAG,"Existen "+llamadas.size()+" llamadas pendientes, eliminar las alertas.");
                List<AlarmJvs> list = AlarmJvs.getLlamadasAll(db);
                Log.d(TAG,"Cantidad de alarmas de llamada a eliminar:"+list.size());
                for (AlarmJvs jvs:list){
                    jvs.cancelAlarm(context);
                    AlarmJvs.delete(db,jvs);
                }

                for (LlamadaVta a:llamadas){
                    Calendar fechaLlamada = Calendar.getInstance();
                    fechaLlamada.setTime(a.getFechaLlamada());
                    boolean sameDayLlamada = hoy.get(Calendar.YEAR) == fechaLlamada.get(Calendar.YEAR) &&
                            hoy.get(Calendar.DAY_OF_YEAR) == fechaLlamada.get(Calendar.DAY_OF_YEAR);
                    if(sameDayLlamada){
                        Log.d(TAG,"Llamada de pendiente el dia de hoy:"+a.toString());
                        final long t= fechaLlamada.getTimeInMillis();
                        //Alertar Llamada
                        Date fechaAlertarLlamada = new Date(t-(value*ONE_MINUTE_IN_MILLIS));
                        Calendar calendarLlamadaAlertar = Calendar.getInstance();
                        calendarLlamadaAlertar.setTime(fechaAlertarLlamada);
                        calendarLlamadaAlertar.set(Calendar.SECOND,0);
                        Log.d(TAG,"Fecha Alertar la llamada:"+calendarLlamadaAlertar.getTime().toString());
                        int hora = calendarLlamadaAlertar.get(Calendar.HOUR_OF_DAY);
                        int minutos = calendarLlamadaAlertar.get(Calendar.MINUTE);
                        Log.d(TAG,"Llamada Alertar:Hora del dia:"+hora+", minutos:"+minutos);

                        ProspectoVtaDb prospectoVtaDb = ProspectoVtaDb.getProspectoIdProspecto(db,a.getIdCliente());
                        if(prospectoVtaDb!=null){
                            String mensaje = "Tienes una llamada por realizar a "+prospectoVtaDb.getNombre();
                            Alarm.setAlarmLlamada(fechaAlertarLlamada,db,context,hora,minutos,(a.getIdLlamada()+200),mensaje);
                        }else{
                            String mensaje = "Tienes una llamada por realizar en "+value+" minutos.";
                            Alarm.setAlarmLlamada(fechaAlertarLlamada,db,context,hora,minutos,(a.getIdLlamada()+200),mensaje);
                        }
                    }
                }
            }
            //endregion

            //region Alerta de Llamada No realizada a Supervisor
            ApplicationParameter tiempoNotificarSupervisor = ApplicationParameter.getParameter(db, rp3.auna.util.constants.Constants.PARAMETERID_ALERTASUPERVISOR, rp3.auna.util.constants.Constants.LABEL_LLAMADAS);
            final int valueSupervisor = Integer.parseInt(tiempoNotificarSupervisor.getValue());
            Log.d(TAG,"Tiempo para notificar llamada supervisor:"+valueSupervisor);
            List<LlamadaVta> llamadasSupervisor = LlamadaVta.getLlamadasPendienteSupervisorAll(db,valueSupervisor);
            if(llamadasSupervisor.size()>0){
                Log.d(TAG,"Existen "+llamadas.size()+" llamadas pendientes, eliminar las alertas.");
                List<AlarmJvs> list1 = AlarmJvs.getLlamadasSupervisorAll(db);
                Log.d(TAG,"Cantidad de alarmas de llamada a notificar supervisor a eliminar:"+list1.size());
                for (AlarmJvs jvs:list1){
                    jvs.cancelAlarm(context);
                    AlarmJvs.delete(db,jvs);
                }
                for (LlamadaVta llamadaVta:llamadasSupervisor){
                    Calendar fechaLlamada = Calendar.getInstance();
                    fechaLlamada.setTime(llamadaVta.getFechaLlamada());
                    boolean sameDayLlamada = hoy.get(Calendar.YEAR) == fechaLlamada.get(Calendar.YEAR) &&
                            hoy.get(Calendar.DAY_OF_YEAR) == fechaLlamada.get(Calendar.DAY_OF_YEAR);
                    if(sameDayLlamada){
                        Log.d(TAG,"Llamada de pendiente el dia de hoy:"+llamadaVta.toString());
                        //Alerta para Notificar al Supervisor
                        final long k = fechaLlamada.getTimeInMillis();
                        Date fechaNotificarLlamada = new Date(k+(valueSupervisor*ONE_MINUTE_IN_MILLIS));
                        Calendar calendarLlamadaSupervisor = Calendar.getInstance();
                        calendarLlamadaSupervisor.setTime(fechaNotificarLlamada);
                        calendarLlamadaSupervisor.set(Calendar.SECOND,0);
                        Log.d(TAG,"Fecha Llamada Notificar Supervisor:"+calendarLlamadaSupervisor.getTime().toString());
                        int horaNotificar = calendarLlamadaSupervisor.get(Calendar.HOUR_OF_DAY);
                        int minutoNotificar = calendarLlamadaSupervisor.get(Calendar.MINUTE);
                        Log.d(TAG,"Llamada Notificar Supervisor:Hora del dia:"+horaNotificar+", minutos:"+minutoNotificar);
                        Alarm.setAlarmLlamadaSupervisor(fechaNotificarLlamada,db,context,horaNotificar,minutoNotificar,(llamadaVta.getIdLlamada()+2000));
                    }
                }
            }
            //endregion

            //region Alarma de Visitas
            ApplicationParameter tiempoNotificar1 = ApplicationParameter.getParameter(db, rp3.auna.util.constants.Constants.PARAMETERID_ALERTAVISITA, rp3.auna.util.constants.Constants.LABEL_VISITAS);
            final int value1 = Integer.parseInt(tiempoNotificar1.getValue());
            Log.d(TAG,"Valor de notificar visita:"+value1);
            List<VisitaVta> visitas = VisitaVta.getPendienteAll(db,value1);
            if(visitas.size()>0){
                Log.d(TAG,"Existen "+visitas.size()+" visitas pendientes, eliminar las alertas.");
                List<AlarmJvs> list = AlarmJvs.getVisitasAll(db);
                Log.d(TAG,"Cantidad de alertas de visita a eliminar:"+list.size());
                if(list.size()>0){
                    for (AlarmJvs jvs:list){
                        jvs.cancelAlarm(context);
                        AlarmJvs.delete(db,jvs);
                    }
                }
                for (VisitaVta b:visitas){
                    Calendar fechaVisita = Calendar.getInstance();
                    fechaVisita.setTime(b.getFechaVisita());
                    boolean sameDayVisita = hoy.get(Calendar.YEAR) == fechaVisita.get(Calendar.YEAR) &&
                            hoy.get(Calendar.DAY_OF_YEAR) == fechaVisita.get(Calendar.DAY_OF_YEAR);
                    if(sameDayVisita){
                        Log.d(TAG,"Visita pendiente el dia de hoy:"+b.toString());
                        final long q= fechaVisita.getTimeInMillis();
                        //Alertar de Visita
                        Date fechaAlertarVisita = new Date(q-(value1*ONE_MINUTE_IN_MILLIS));
                        Calendar calendarVisita = Calendar.getInstance();
                        calendarVisita.setTime(fechaAlertarVisita);
                        calendarVisita.set(Calendar.SECOND,0);
                        Log.d(TAG,"Fecha para alertar visita:"+calendarVisita.getTime().toString());
                        int hora = calendarVisita.get(Calendar.HOUR_OF_DAY);
                        int minutos = calendarVisita.get(Calendar.MINUTE);
                        Log.d(TAG,"Visita Alertar:Hora del dia:"+hora+", minutos:"+minutos);
                        ProspectoVtaDb prospectoVtaDb = ProspectoVtaDb.getProspectoIdProspecto(db,b.getIdCliente());
                        if(prospectoVtaDb!=null){
                            String mensaje = "Tienes una visita por realizar a "+prospectoVtaDb.getNombre();
                            Alarm.setAlarmVisita(fechaAlertarVisita,db,context,hora,minutos,(b.getIdVisita()+100),mensaje);
                        }else{
                            String mensaje = "Tienes una visita por realizar en "+value1+" minutos.";
                            Alarm.setAlarmVisita(fechaAlertarVisita,db,context,hora,minutos,(b.getIdVisita()+100),mensaje);
                        }

                    }
                }
            }
            //endregion

            //region Alerta de Visita No realizada a Supervisor
            ApplicationParameter tiempoNotificarSupervisor1 = ApplicationParameter.getParameter(db, rp3.auna.util.constants.Constants.PARAMETERID_ALERTASUPERVISORVISITA, rp3.auna.util.constants.Constants.LABEL_VISITAS);
            final int valueSupervisor1 = Integer.parseInt(tiempoNotificarSupervisor1.getValue());
            Log.d(TAG,"Valor de supervisor visita alertar:"+valueSupervisor1);
            List<VisitaVta> visitasSupervisor = VisitaVta.getPendienteSupervisorAll(db,valueSupervisor1);
            if(visitasSupervisor.size()>0){
                Log.d(TAG,"Existen "+visitasSupervisor.size()+" visitas no realizadas pendientes, eliminar las alertas a supervisor.");
                List<AlarmJvs> list1 = AlarmJvs.getVisitasSupervisorAll(db);
                Log.d(TAG,"Cantidad de alertas a supervisor de visita a eliminar:"+list1.size());
                for (AlarmJvs jvs:list1){
                    jvs.cancelAlarm(context);
                    AlarmJvs.delete(db,jvs);
                }
                for(VisitaVta visitaVta:visitasSupervisor){
                    Calendar fechaVisita = Calendar.getInstance();
                    fechaVisita.setTime(visitaVta.getFechaVisita());
                    boolean sameDayVisita = hoy.get(Calendar.YEAR) == fechaVisita.get(Calendar.YEAR) &&
                            hoy.get(Calendar.DAY_OF_YEAR) == fechaVisita.get(Calendar.DAY_OF_YEAR);
                    if(sameDayVisita){
                        Log.d(TAG,"Visita pendiente el dia de hoy:"+visitaVta.toString());
                        //Notificar al Supervisor Visita
                        final long x = fechaVisita.getTimeInMillis();
                        Date fechaNotificar = new Date(x+(valueSupervisor1*ONE_MINUTE_IN_MILLIS));
                        Calendar calendarNotificarVisita = Calendar.getInstance();
                        calendarNotificarVisita.setTime(fechaNotificar);
                        calendarNotificarVisita.set(Calendar.SECOND,0);
                        Log.d(TAG,"Fecha alertar supervisor visita:"+calendarNotificarVisita.getTime().toString());
                        int horaNotificar = calendarNotificarVisita.get(Calendar.HOUR_OF_DAY);
                        int minutoNotificar = calendarNotificarVisita.get(Calendar.MINUTE);
                        Log.d(TAG,"Visita Notificar Supervisor:Hora del dia:"+horaNotificar+", minutos:"+minutoNotificar);
                        Alarm.setAlarmVisitaSupervisor(fechaNotificar,db,context,horaNotificar,minutoNotificar,(visitaVta.getIdVisita()+1000));
                    }
                }
            }
            //endregion

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void alarmTest(){
        Log.d(TAG,"alarmTest...");
        try{
            Alarm.setAlarmTest(Utils.getDataBase(this),this,1,20,0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //region Drawer Items

    private void setDrawersMain(){
        final TypedValue typedValue = new TypedValue();
        Main2Activity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.data;
        recyclerViewDrawer = (RecyclerView) findViewById(R.id.recyclerViewDrawerMain);
        recyclerViewDrawer.setHasFixedSize(true);
        recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReporteriaAdapter(this, initData(), new ReporteriaAdapter.ListenerItems() {
            @Override
            public void setParent(final int position) {
                Log.d(TAG,"positionParent:"+position);
                //region Pintar
                positionSelected = position;
                /**
                 * Configurar clicks en cada item del recycler
                 * **/
                try{
                    //region Clicked Position
                    if(position==0 || position ==1 || position ==4 || position ==2){
                        positiSelectedLast = position;
                        Log.d(TAG,"click position 0 || 1 || 4 || 2...es="+position);
                        //
                        int countDrawer = recyclerViewDrawer.getChildCount();
                        Log.d(TAG,"Count Recycler child: "+countDrawer);
                        for(int i=0;i<countDrawer;i++){
                            if(i==position){
                                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIconMain11);
                                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitleMain11);
                                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem11);
                                if(imageViewDrawerIcon!=null && textViewDrawerTitle !=null && relativeLayoutDrawerItem!=null){
                                    imageViewDrawerIcon.setColorFilter(color);
                                    if (Build.VERSION.SDK_INT > 15) {
                                        imageViewDrawerIcon.setImageAlpha(255);
                                    } else {
                                        imageViewDrawerIcon.setAlpha(255);
                                    }
                                    textViewDrawerTitle.setTextColor(color);
                                    TypedValue typedValueDrawerSelected = new TypedValue();
                                    getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                                    int colorDrawerItemSelected = typedValueDrawerSelected.data;
                                    colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                                    relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);
                                }
                            }else{
                                ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIconMain11);
                                TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitleMain11);
                                RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem11);

                                if(imageViewDrawerIcon!=null && textViewDrawerTitle !=null && relativeLayoutDrawerItem!=null){
                                    imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                                    if (Build.VERSION.SDK_INT > 15) {
                                        imageViewDrawerIcon.setImageAlpha(138);
                                    } else {
                                        imageViewDrawerIcon.setAlpha(138);
                                    }
                                    textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                                    relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                }
                            }
                        }

                        //region reemplazo
                        /*
                        if (position == 2) {
                            Log.d(TAG,"click position 0 pintar...");
                            ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.imageViewDrawerIconMain11);
                            TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.textViewDrawerItemTitleMain11);
                            imageViewDrawerIcon.setColorFilter(color);
                            if (Build.VERSION.SDK_INT > 15) {
                                imageViewDrawerIcon.setImageAlpha(255);
                            } else {
                                imageViewDrawerIcon.setAlpha(255);
                            }
                            textViewDrawerTitle.setTextColor(color);
                            RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(2).findViewById(R.id.relativeLayoutDrawerItem11);
                            TypedValue typedValueDrawerSelected = new TypedValue();
                            getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                            int colorDrawerItemSelected = typedValueDrawerSelected.data;
                            colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                            relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

                        }
                        else {
                            Log.d(TAG,"click position 0 despintar...");
                            ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.imageViewDrawerIconMain11);
                            TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(2).findViewById(R.id.textViewDrawerItemTitleMain11);
                            imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                            if (Build.VERSION.SDK_INT > 15) {
                                imageViewDrawerIcon.setImageAlpha(138);
                            } else {
                                imageViewDrawerIcon.setAlpha(138);
                            }
                            textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                            RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(2).findViewById(R.id.relativeLayoutDrawerItem11);
                            relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                        }
                        //
                        if (position == 0) {
                            Log.d(TAG,"click position 0 pintar...");
                            ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.imageViewDrawerIconMain11);
                            TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.textViewDrawerItemTitleMain11);
                            imageViewDrawerIcon.setColorFilter(color);
                            if (Build.VERSION.SDK_INT > 15) {
                                imageViewDrawerIcon.setImageAlpha(255);
                            } else {
                                imageViewDrawerIcon.setAlpha(255);
                            }
                            textViewDrawerTitle.setTextColor(color);
                            RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(0).findViewById(R.id.relativeLayoutDrawerItem11);
                            TypedValue typedValueDrawerSelected = new TypedValue();
                            getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                            int colorDrawerItemSelected = typedValueDrawerSelected.data;
                            colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                            relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

                        }
                        else {
                            Log.d(TAG,"click position 0 despintar...");
                            ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.imageViewDrawerIconMain11);
                            TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(0).findViewById(R.id.textViewDrawerItemTitleMain11);
                            imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                            if (Build.VERSION.SDK_INT > 15) {
                                imageViewDrawerIcon.setImageAlpha(138);
                            } else {
                                imageViewDrawerIcon.setAlpha(138);
                            }
                            textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                            RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(0).findViewById(R.id.relativeLayoutDrawerItem11);
                            relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                        }
                        if (position == 1) {
                            Log.d(TAG,"click position 1 pintar...");
                            ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.imageViewDrawerIconMain11);
                            TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.textViewDrawerItemTitleMain11);
                            imageViewDrawerIcon.setColorFilter(color);
                            if (Build.VERSION.SDK_INT > 15) {
                                imageViewDrawerIcon.setImageAlpha(255);
                            } else {
                                imageViewDrawerIcon.setAlpha(255);
                            }
                            textViewDrawerTitle.setTextColor(color);
                            RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(1).findViewById(R.id.relativeLayoutDrawerItem11);
                            TypedValue typedValueDrawerSelected = new TypedValue();
                            getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                            int colorDrawerItemSelected = typedValueDrawerSelected.data;
                            colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                            relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

                        }
                        else {
                            Log.d(TAG,"click position 1 despintar...");
                            ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.imageViewDrawerIconMain11);
                            TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(1).findViewById(R.id.textViewDrawerItemTitleMain11);
                            imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                            if (Build.VERSION.SDK_INT > 15) {
                                imageViewDrawerIcon.setImageAlpha(138);
                            } else {
                                imageViewDrawerIcon.setAlpha(138);
                            }
                            textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                            RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(1).findViewById(R.id.relativeLayoutDrawerItem11);
                            relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                        }
                        if (position == 4) {
                            Log.d(TAG,"click position 4 pintar...");
                            ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.imageViewDrawerIconMain11);
                            TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.textViewDrawerItemTitleMain11);
                            imageViewDrawerIcon.setColorFilter(color);
                            if (Build.VERSION.SDK_INT > 15) {
                                imageViewDrawerIcon.setImageAlpha(255);
                            } else {
                                imageViewDrawerIcon.setAlpha(255);
                            }
                            textViewDrawerTitle.setTextColor(color);
                            RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(4).findViewById(R.id.relativeLayoutDrawerItem11);
                            TypedValue typedValueDrawerSelected = new TypedValue();
                            getTheme().resolveAttribute(R.attr.colorAccent, typedValueDrawerSelected, true);
                            int colorDrawerItemSelected = typedValueDrawerSelected.data;
                            colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                            relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

                        }
                        else {
                            Log.d(TAG,"click position 1 despintar...");
                            ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.imageViewDrawerIconMain11);
                            TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(4).findViewById(R.id.textViewDrawerItemTitleMain11);
                            imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                            if (Build.VERSION.SDK_INT > 15) {
                                imageViewDrawerIcon.setImageAlpha(138);
                            } else {
                                imageViewDrawerIcon.setAlpha(138);
                            }
                            textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                            RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(4).findViewById(R.id.relativeLayoutDrawerItem11);
                            relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                        }
                    }else{
                        Log.d(TAG,"!!!click position 0 || 1..."+position);
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(position).findViewById(R.id.imageViewDrawerIconMain11);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(position).findViewById(R.id.textViewDrawerItemTitleMain11);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(position).findViewById(R.id.relativeLayoutDrawerItem11);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                    }
                    */
                    //endregion

                    //endregion
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after some time
                            setFragment(position);
                        }
                    }, 250);
                    if(position!=2){
                        mDrawerLayout.closeDrawers();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void setChild(final int i,TitleChild position) {
                Log.d(TAG,"posiiton:"+i+" setChild:"+position.toString());
                Intent intent = new Intent(Main2Activity.this,ReporteActivity.class);
                intent.putExtra("Option",position.getOption());
                intent.putExtra("Title","Reporte "+position.getSubtitle());
                startActivity(intent);
            }
        });
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        recyclerViewDrawer.setAdapter(adapter);
        //region inusual
        /*ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        List<TitleMenu> titulos = new ArrayList<>();
        final String[] drawerTitles = getResources().getStringArray(R.array.drawer);
        final TypedArray drawerIcons = getResources().obtainTypedArray(R.array. drawerIcons);
        for (int i = 0; i < drawerTitles.length; i++) {
            drawerItems.add(new DrawerItem(drawerTitles[i], drawerIcons.getDrawable(i)));*/
            //Titulos
            //Es reporte
            /*if(i==2){
                Log.d(TAG,"i==2 es reporte ponerle sus subtitles...");
                String titulo = drawerTitles[i];
                Drawable drawable = drawerIcons.getDrawable(i);
                List<ItemSubTitle> itemSubTitleList = new ArrayList<>();
                ItemSubTitle subTitle1 = new ItemSubTitle();
                subTitle1.setOption("REPVTNGER");
                subTitle1.setSubTitle("Reporte 1");
                ItemSubTitle subTitle2 = new ItemSubTitle();
                subTitle2.setOption("REPVTNGRA");
                subTitle2.setSubTitle("Reporte 2");
                ItemSubTitle subTitle3 = new ItemSubTitle();
                subTitle3.setOption("REPVTNINF");
                subTitle3.setSubTitle("Reporte 3");
                ItemSubTitle subTitle4 = new ItemSubTitle();
                subTitle4.setOption("REPVTNJEF");
                subTitle4.setSubTitle("Reporte 4");
                itemSubTitleList.add(subTitle1);
                itemSubTitleList.add(subTitle2);
                itemSubTitleList.add(subTitle3);
                itemSubTitleList.add(subTitle4);
                Log.d(TAG,"itemSubTitleList:"+itemSubTitleList.size());
                TitleMenu menu = new TitleMenu(titulo,itemSubTitleList,drawable);
                titulos.add(menu);
            }else{
                String titulo = drawerTitles[i];
                Drawable drawable = drawerIcons.getDrawable(i);
                TitleMenu menu = new TitleMenu(titulo,null,drawable);
                titulos.add(menu);
            }

        }
        drawerIcons.recycle();*/
        //adapterDrawer = new DrawerAdapter(drawerItems);
        //endregion

    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title:titles)
        {
            parentObject.add(title);
        }
        return parentObject;

    }

    //endregion
}
