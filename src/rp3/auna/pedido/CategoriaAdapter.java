package rp3.auna.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import rp3.auna.R;
import rp3.auna.models.pedido.Categoria;
import rp3.auna.models.pedido.SubCategoria;

/**
 * Created by magno_000 on 04/11/2015.
 */
public class CategoriaAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Categoria> categorias;
    private List<SubCategoria> subCategorias;
    private boolean cateOrSub = false;

    public CategoriaAdapter(Context context, List<Categoria> categorias, boolean t)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.categorias = categorias;
        cateOrSub = t;
    }

    public CategoriaAdapter(Context context, List<SubCategoria> subCategorias)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.subCategorias = subCategorias;
    }

    @Override
    public int getCount() {
        if(cateOrSub)
            return categorias.size();
        else
            return subCategorias.size();
    }

    @Override
    public Categoria getItem(int position) {
            return categorias.get(position);

    }

    public SubCategoria getItemSub(int position)
    {
        return subCategorias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_categoria), null);

        if(cateOrSub)
        {
            ((TextView) convertView.findViewById(R.id.categoria_descripcion)).setText(categorias.get(position).getDescripcion());
        }
        else
        {
            ((TextView) convertView.findViewById(R.id.categoria_descripcion)).setText(subCategorias.get(position).getDescripcion());
        }

        return convertView;
    }
}
