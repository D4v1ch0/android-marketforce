package rp3.marketforce;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rp3.marketforce.edit.TransactionEditItemFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
 
public class DateDisplayPicker extends TextView implements DatePickerDialog.OnDateSetListener{
 
    private TransactionEditItemFragment _context;
    private Date date = null;
 
    public DateDisplayPicker(TransactionEditItemFragment context, AttributeSet attrs, int defStyle) {
        super(context.getActivity(), attrs, defStyle);
        _context = context;
    }
 
    public DateDisplayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes();
    }
 
    public DateDisplayPicker(Context context) {
        super(context);
        setAttributes();
    }
 
    private void setAttributes() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }
    
    public void setParent(TransactionEditItemFragment context)
    {
    	_context = context;
    }
 
    private void showDateDialog() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(_context.getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }
 
 
    @SuppressLint("SimpleDateFormat")
	@Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	String dateInString = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
    	
    	try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	if(date != null)
    		_context.setTextViewDateText(getId(), date);
//        setText(String.format("%s/%s/%s", monthOfYear, dayOfMonth, year));
    	
    }
    
    public Date getDate() {
    	return this.date;
    }
    
}