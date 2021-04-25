package com.example.zhiliao.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.zhiliao.R;

import org.w3c.dom.Text;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener{
    private TextView time;
    public TimePickerFragment(View view){
        time= (TextView) view;
    }
       // private TextView begTime;
        //private TextView endTime;
@NonNull
@Override
public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
        DateFormat.is24HourFormat(getActivity()));
        }

@Override
public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //begTime=getActivity().findViewById(R.id.begTime);
               // endTime=getActivity().findViewById(R.id.endTime);
                time.setText(hourOfDay+":"+minute);

                //endTime.setText(hourOfDay+":"+minute);
        Log.d("onTimeSet", "hourOfDay: "+hourOfDay + "Minute: "+minute);
        }
}
