package rp3.auna.pedido;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import rp3.app.BaseFragment;
import rp3.auna.R;
import rp3.auna.models.pedido.Categoria;
import rp3.auna.models.pedido.SubCategoria;

/**
 * Created by magno_000 on 04/11/2015.
 */
public class CategoriaFragment extends BaseFragment {

    private static final String TAG = CategoriaFragment.class.getSimpleName();
    public static String ARG_IDCATEGORIA = "idcategoria";

    public int idCategoria;
    private CategoriaAdapter adapter = null;

    public static CategoriaFragment newInstance(int idCategoria) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_IDCATEGORIA, idCategoria);
        CategoriaFragment fragment = new CategoriaFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach...");
        tryEnableGooglePlayServices(true);
        setContentView(R.layout.fragment_categorias);
        setRetainInstance(true);
    }


    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        idCategoria = getArguments().getInt(ARG_IDCATEGORIA, 0);

        if(idCategoria == 0)
        {
            List<Categoria> categorias = Categoria.getCategorias(getDataBase());
            Categoria sinCategoria = new Categoria();
            sinCategoria.setDescripcion("Sin Categorías");
            categorias.add(sinCategoria);
            adapter = new CategoriaAdapter(getContext(), categorias, true);
        }
        else
        {
            List<SubCategoria> subCategorias = SubCategoria.getSubCategorias(getDataBase(), idCategoria);
            adapter = new CategoriaAdapter(getContext(), subCategorias);
        }

        ((ListView) rootView.findViewById(R.id.list_categorias)).setAdapter(adapter);
        ((ListView) rootView.findViewById(R.id.list_categorias)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (idCategoria == 0) {
                    if(adapter.getItem(position).getIdCategoria() == 0)
                    {
                        Intent intent = new Intent(getContext(), ProductoListActivity.class);
                        intent.putExtra(ARG_IDCATEGORIA, adapter.getItem(position).getIdCategoria());
                        startActivityForResult(intent, CrearPedidoFragment.REQUEST_BUSQUEDA);
                    }
                    else {
                        Intent intent = new Intent(getContext(), CategoriaActivity.class);
                        intent.putExtra(ARG_IDCATEGORIA, adapter.getItem(position).getIdCategoria());
                        startActivityForResult(intent, CrearPedidoFragment.REQUEST_BUSQUEDA);
                    }
                } else {
                    Intent intent = new Intent(getContext(), ProductoListActivity.class);
                    intent.putExtra(ARG_IDCATEGORIA, adapter.getItemSub(position).getIdSubCategoria());
                    startActivityForResult(intent, CrearPedidoFragment.REQUEST_BUSQUEDA);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        if(resultCode == RESULT_OK) {
            ((CategoriaActivity) getActivity()).finishOnResult(data.getExtras().getString(ProductoListFragment.ARG_PRODUCTO));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG,"onConfigurationChanged...");
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
