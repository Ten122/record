package com.example.recordapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recordapp.R;

import java.util.ConcurrentModificationException;
import java.util.List;

public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    EditText editText1,editText2;
    DatePicker datePicker;
    Button button1,button2;
    public interface OnEnsureListener{
        public void onEnsure(String time,int year,int month,int day);
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeDialog(@NonNull Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        editText1 = findViewById(R.id.dialog_time_et1);
        editText2 = findViewById(R.id.dialog_time_et2);
        button1 = findViewById(R.id.dialog_time_bu1);
        button2 = findViewById(R.id.dialog_time_bu2);
        datePicker = findViewById(R.id.dialog_time_dp);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        hideDatePickerHeader();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.dialog_time_bu1:

                break;

            case R.id.dialog_time_bu2:
                int year = datePicker.getYear();  //选择年份
                int month = datePicker.getMonth()+1;
                int dayOfMonth = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);
                if (month<10){
                    monthStr = "0"+month;
                }
                String dayStr = String.valueOf(dayOfMonth);
                if (dayOfMonth<10){
                    dayStr="0"+dayOfMonth;
                }
//              获取输入的小时和分钟
                String hourStr = editText1.getText().toString();
                String minuteStr = editText2.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour=hour%24;
                }
                int minute = 0;
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr);
                    minute=minute%60;
                }

                hourStr=String.valueOf(hour);
                minuteStr=String.valueOf(minute);
                if (hour<10){
                    hourStr="0"+hour;
                }
                if (minute<10){
                    minuteStr="0"+minute;
                }
                String timeFormat = year+"年"+monthStr+"月"+dayStr+"日 "+hourStr+":"+minuteStr;
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(timeFormat,year,month,dayOfMonth);
                }
                cancel();
                break;
        }
    }
    //隐藏DatePicker头布局
    private void hideDatePickerHeader() {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }
        int headerId = getContext().getResources().getIdentifier("date_picker_header", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }

}
