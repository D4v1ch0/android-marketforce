package rp3.marketforce.ruta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RutasMapaAdapter extends BaseAdapter {
	
	private List<Agenda> list;
	private LayoutInflater inflater;
	private Context ctx;
	private String hour_inicio="";
	private String str_range;
	private SimpleDateFormat format4;
	private Date date;
	
	public RutasMapaAdapter(Context c, List<Agenda> list)
	{
		this.list = list;
		ctx = c;
		inflater = LayoutInflater.from(c);
		format4= new SimpleDateFormat("HH:mm");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = (View) inflater.inflate(this.ctx.getApplicationContext().getResources().getLayout(R.layout.rowlist_rutas_mapa), null);
		
		Agenda agd = list.get(position);
		date = agd.getFechaInicio();
		 hour_inicio = format4.format(date);
		 str_range =hour_inicio;
		
		((TextView) convertView.findViewById(R.id.textView_horas)).setText(str_range);
		 
		((TextView) convertView.findViewById(R.id.textView_nombre)).setText(""+agd.getNombreCompleto());

		((ImageView) convertView.findViewById(R.id.itemlist_rutas_estado)).setImageBitmap(writeTextOnDrawable(R.drawable.map_position, position + 1 + ""));
		
		
		if(agd.getClienteDireccion() != null)
			((TextView) convertView.findViewById(R.id.textView_address)).setText(""+agd.getDireccion());
		
		return convertView;
	}
	
	private Bitmap writeTextOnDrawable(int drawableId, String text) {

	    Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(), drawableId)
	            .copy(Bitmap.Config.ARGB_8888, true);

	    //Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

	    Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    paint.setColor(Color.WHITE);
	    //paint.setTypeface(tf);
	    paint.setTextAlign(Align.CENTER);
	    paint.setTextSize(ctx.getResources().getDimension(R.dimen.text_small_size));

	    Rect textRect = new Rect();
	    paint.getTextBounds(text, 0, text.length(), textRect);

	    Canvas canvas = new Canvas(bm);

	    //If the text is bigger than the canvas , reduce the font size
	    if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
	        paint.setTextSize(ctx.getResources().getDimension(R.dimen.text_small_size));        //Scaling needs to be used for different dpi's

	    //Calculate the positions
	    int xPos = (canvas.getWidth() / 2);     //-2 is for regulating the x position offset

	    //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
	    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;  

	    canvas.drawText(text, xPos, yPos, paint);

	    return  bm;
	}

}
