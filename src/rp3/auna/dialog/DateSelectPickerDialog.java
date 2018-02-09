package rp3.auna.dialog;

import android.annotation.SuppressLint;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import rp3.auna.R;

/**
 * Created by Jesus Villa on 18/10/2017.
 */

public class DateSelectPickerDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private Calendar max;
    private Calendar min;

    public static DateSelectPickerDialog newInstance(DatePickerDialog.OnDateSetListener listener,Calendar max,Calendar min) {
        DateSelectPickerDialog fragment = new DateSelectPickerDialog();
        fragment.setListener(listener,max,min);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener,Calendar max,Calendar min) {
        this.listener = listener;
        this.max = max;
        this.min = min;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePicker, listener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(max.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(min.getTimeInMillis());
        return datePickerDialog;
    }
}