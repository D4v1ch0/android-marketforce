package rp3.marketforce.resumen;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgenteResumen;
import rp3.marketforce.utils.DrawableManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResumenAdapter extends BaseAdapter {

	List<AgenteResumen> list_resumen;
	LayoutInflater inflater;
	Context ctx;
	
	public ResumenAdapter(Context c, List<AgenteResumen> list_resumen){
		this.inflater = LayoutInflater.from(c);
		this.ctx = c;
		this.list_resumen = list_resumen;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_resumen.size() + 1;
	}

	@Override
	public AgenteResumen getItem(int position) {
		// TODO Auto-generated method stub
		return list_resumen.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

    public void Sort(String estado, final boolean asc)
    {
        if(estado.equalsIgnoreCase(Contants.ESTADO_PENDIENTE))
        {
            Collections.sort(list_resumen, new Comparator<AgenteResumen>() {
                @Override
                public int compare(AgenteResumen agenteResumen, AgenteResumen agenteResumen2) {
                    if(agenteResumen.getPendientes() == agenteResumen2.getPendientes())
                        return 0;
                    else
                    {
                        if(asc)
                            return agenteResumen.getPendientes() < agenteResumen2.getPendientes() ? -1 : 1;
                        else
                            return agenteResumen.getPendientes() > agenteResumen2.getPendientes() ? -1 : 1;
                    }
                }
            });
        }
        if(estado.equalsIgnoreCase(Contants.ESTADO_VISITADO))
        {
            Collections.sort(list_resumen, new Comparator<AgenteResumen>() {
                @Override
                public int compare(AgenteResumen agenteResumen, AgenteResumen agenteResumen2) {
                    if(agenteResumen.getGestionados() == agenteResumen2.getGestionados())
                        return 0;
                    else
                    {
                        if(asc)
                            return agenteResumen.getGestionados() < agenteResumen2.getGestionados() ? -1 : 1;
                        else
                            return agenteResumen.getGestionados() > agenteResumen2.getGestionados() ? -1 : 1;
                    }
                }
            });
        }
        if(estado.equalsIgnoreCase(Contants.ESTADO_NO_VISITADO))
        {
            Collections.sort(list_resumen, new Comparator<AgenteResumen>() {
                @Override
                public int compare(AgenteResumen agenteResumen, AgenteResumen agenteResumen2) {
                    if(agenteResumen.getNoGestionados() == agenteResumen2.getNoGestionados())
                        return 0;
                    else
                    {
                        if(asc)
                            return agenteResumen.getNoGestionados() < agenteResumen2.getNoGestionados() ? -1 : 1;
                        else
                            return agenteResumen.getNoGestionados() > agenteResumen2.getNoGestionados() ? -1 : 1;
                    }
                }
            });
        }
        notifyDataSetChanged();

    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = (View) inflater.inflate(this.ctx.getApplicationContext().getResources().getLayout(R.layout.rowlist_grupo_agente), null);
		
		if(position + 1 > list_resumen.size())
		{
			int visitados = 0;
		 	int no_visitado = 0;
		 	int pendientes = 0;
		 	for(int i = 0; i < list_resumen.size(); i ++)
		 	{
		 		visitados = visitados + list_resumen.get(i).getGestionados();
		 		no_visitado = no_visitado + list_resumen.get(i).getNoGestionados();
		 		pendientes = pendientes + list_resumen.get(i).getPendientes();
		 	}
		 	
		 	((TextView) convertView.findViewById(R.id.grupo_agente_nombre)).setText("Total");
			((TextView) convertView.findViewById(R.id.grupo_agente_visitados)).setText(visitados + "");
			((TextView) convertView.findViewById(R.id.grupo_agente_no_visitados)).setText(no_visitado + "");
			((TextView) convertView.findViewById(R.id.grupo_agente_pendientes)).setText(pendientes + "");
		 	
		}
		else
		{
			AgenteResumen agd = list_resumen.get(position);
			
			((TextView) convertView.findViewById(R.id.grupo_agente_nombre)).setText(agd.getNombres());
			((TextView) convertView.findViewById(R.id.grupo_agente_visitados)).setText(agd.getGestionados() + "");
			((TextView) convertView.findViewById(R.id.grupo_agente_no_visitados)).setText(agd.getNoGestionados() + "");
			((TextView) convertView.findViewById(R.id.grupo_agente_pendientes)).setText(agd.getPendientes() + "");
		}
		
		return convertView;
	}

}
