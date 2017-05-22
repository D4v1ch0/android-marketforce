package rp3.berlin.pedido;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.berlin.R;
import rp3.berlin.models.pedido.Producto;

/**
 * Created by magno_000 on 28/04/2017.
 */

public class ProductoAutoCompleteAdapter extends ArrayAdapter<Producto> {
    private final String MY_DEBUG_TAG = "GeopoliticalStructureAdapter";
    private List<Producto> items;
    private List<Producto> suggestions;
    private Producto selected;
    private int viewResourceId;
    private DataBase db;

    public ProductoAutoCompleteAdapter(Context context, DataBase db) {
        super(context, R.layout.rowlist_producto, new ArrayList<Producto>());
        this.db = db;
        this.items = new ArrayList<Producto>();
        this.suggestions = new ArrayList<Producto>();
    }

    public List<Producto> getItems() {
        return items;
    }

    public Producto getSelected(String text)
    {
        try {
            for (Producto prod : suggestions) {
                if (prod.getCodigoExterno().equalsIgnoreCase(text))
                    return prod;
            }
        }
        catch (Exception ex)
        {

        }
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final Producto prod = getItem(position);
        if(prod.getDescripcion().equalsIgnoreCase("Cargando"))
        {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(rp3.core.R.layout.base_rowlist_loading, null);
            v.setClickable(false);
            v.setFocusable(false);
            v.setEnabled(false);
            v.setOnClickListener(null);
        }
        else {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.rowlist_producto, null);
            }
            if (prod != null) {
                TextView NameLabel = (TextView) v.findViewById(R.id.producto_descripcion);
                if (NameLabel != null) {
                    NameLabel.setText(prod.getCodigoExterno() + " - " + prod.getDescripcion());
                }
                ((TextView) v.findViewById(R.id.producto_codigo)).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.producto_precio)).setVisibility(View.GONE);
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Producto)(resultValue)).getCodigoExterno();
            return str;
        }

        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                final Producto loading = new Producto();
                loading.setDescripcion("Cargando");
                suggestions.add(loading);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Activity actv = (Activity)getContext();
                        suggestions = Producto.getProductoSearchNoSerie(db, constraint.toString());
                        actv.runOnUiThread(new Runnable()
                        {

                            @Override
                            public void run() {
                                try {
                                    FilterResults filterResults = new FilterResults();
                                    filterResults.values = suggestions;
                                    filterResults.count = suggestions.size();
                                    notifyDataSetInvalidated();
                                    if(suggestions.size() == 0)
                                        publishResults(constraint, null);
                                    else
                                        publishResults(constraint, filterResults);
                                }catch (Exception ex)
                                {
                                    clear();
                                    publishResults(constraint, null);
                                }

                            }

                        });
                    }
                };
                new Thread(runnable).start();
                //suggestions = GeopoliticalStructure.getGeopoliticalStructureSearch(db, constraint.toString());
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                if (results != null && results.count > 0) {
                    ArrayList<Producto> filteredList = (ArrayList<Producto>) results.values;
                    clear();
                    for (Producto c : filteredList) {
                        add(c);
                    }
                }
                else
                {
                    clear();
                    notifyDataSetInvalidated();
                }
            }
            catch (Exception ex)
            {}
            notifyDataSetChanged();
        }
    };
}