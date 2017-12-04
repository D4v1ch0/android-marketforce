package rp3.marketforce.cliente;

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

import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.Producto;
import rp3.db.sqlite.DataBase;

/**
 * Created by Gustavo Meregildo on 01/09/2017.
 */

public class ClienteAutoCompleteAdapter extends ArrayAdapter<Cliente> {
    private final String MY_DEBUG_TAG = "GeopoliticalStructureAdapter";
    private List<Cliente> items;
    private List<Cliente> suggestions;
    private Cliente selected;
    private int viewResourceId;
    private DataBase db;

    public ClienteAutoCompleteAdapter(Context context, DataBase db) {
        super(context, R.layout.rowlist_producto, new ArrayList<Cliente>());
        this.db = db;
        this.items = new ArrayList<Cliente>();
        this.suggestions = new ArrayList<Cliente>();
    }

    public List<Cliente> getItems() {
        return items;
    }

    public Cliente getSelected(String text)
    {
        try {
            for (Cliente prod : suggestions) {
                if (prod.getNombreCompleto().equalsIgnoreCase(text))
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
        final Cliente prod = getItem(position);
        if(prod.getNombre1().equalsIgnoreCase("Cargando"))
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
                    NameLabel.setText(prod.getNombre1());
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
            String str = ((Cliente)(resultValue)).getNombreCompleto();
            return str;
        }

        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                final Cliente loading = new Cliente();
                loading.setNombre1("Cargando");
                suggestions.add(loading);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Activity actv = (Activity)getContext();
                        suggestions = Cliente.getClientSearch(db, constraint.toString());
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
                    ArrayList<Cliente> filteredList = (ArrayList<Cliente>) results.values;
                    clear();
                    for (Cliente c : filteredList) {
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