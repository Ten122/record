package com.example.recordapp.fragrecord;

import android.app.AlertDialog;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recordapp.R;
import com.example.recordapp.adapter.TypeBaseAdapter;
import com.example.recordapp.db.AccountBean;
import com.example.recordapp.db.DBManger;
import com.example.recordapp.db.TypeBean;
import com.example.recordapp.utils.BeiZhuDialog;
import com.example.recordapp.utils.KeyBoardUtils;
import com.example.recordapp.utils.SelectTimeDialog;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 支出
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    KeyboardView keyboardView;
    EditText editText;
    GridView gridView;
    TextView textView1,textView2,textView3;
    ImageView imageView;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;   //将需要插入到记账本中的数据保存成对象形式

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();  //创建对象
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.image5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_out, container, false);
        initView(view);
        setInitTime();
        loadDataToGV();
        setGVListener();  //点击事件
        return view;
    }

    /**
     * 获取时间，显示
     */
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        textView1.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    private void setGVListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.selecPos = i;
                adapter.notifyDataSetChanged();  //改变点击图片颜色

                TypeBean typeBean = typeList.get(i);  //更换文字
                String tyname = typeBean.getTypename();
                textView3.setText(tyname);
                accountBean.setTypename(tyname);
                int imageId = typeBean.getsImageId();  //更换图片
                imageView.setImageResource(imageId);
                accountBean.setsImageId(imageId);


            }
        });
    }

    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(),typeList);
        gridView.setAdapter(adapter);

    }


    private void initView(View view) {

        keyboardView = view.findViewById(R.id.frag_record_top_key);
        editText = view.findViewById(R.id.frag_record_top_money);
        imageView = view.findViewById(R.id.frag_record_top_src);
        gridView = view.findViewById(R.id.frag_record_top_rv);
        textView1 = view.findViewById(R.id.frag_record_top_time);
        textView2 = view.findViewById(R.id.frag_record_top_write);
        textView3 = view.findViewById(R.id.frag_record_top_tv);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);

        //显示软键盘
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, editText);
        boardUtils.showKeyboard();

        //设置接口 确定按钮被点击
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //点击按钮
                String moneyStr = editText.getText().toString();
                if(TextUtils.isEmpty(moneyStr) || moneyStr.equals(("0"))){
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //获取信息
                saveDataToDB();
                //返回上一级
                getActivity().finish();
            }
        });
    }

    /**
     * 抽象  子类一定对该方法进行重写
     */
    public abstract void saveDataToDB();

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frag_record_top_time:
                showSelectTimeDialog();
                break;
                
            case R.id.frag_record_top_write:
                showRecordDialog();
                break;
        }
    }

    public void showSelectTimeDialog(){
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();

        //设置确定按钮被点击了
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                textView1.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });

    }


    public void showRecordDialog(){
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if(!TextUtils.isEmpty(msg)){
                    textView2.setText(msg);
                    accountBean.setRecord(msg);
                }
                dialog.cancel();
            }
        });
    }
}