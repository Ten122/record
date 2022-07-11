package com.example.recordapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.recordapp.adapter.AccountAdapter;
import com.example.recordapp.db.AccountBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.example.recordapp.db.DBManger;
import com.example.recordapp.utils.BudgetDialog;
import com.example.recordapp.utils.MoreDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ListView listView;  //展示收支情况
    ImageView imageView1,imageView2;
    Button button;
    //声明数据源
    List<AccountBean>mDatas;
    private AccountAdapter adapter;
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView textView1,textView2,textView3,textView4;
    ImageView topShow;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        //添加ListView的头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器，加载每一行数据到列表中
        adapter = new AccountAdapter(this,mDatas);
        listView.setAdapter(adapter);
    }

    /** 给ListView添加头布局的方法*/
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainiv_top, null);
        listView.addHeaderView(headerView);
        //查找头布局可用控件
        textView1 = headerView.findViewById(R.id.item_main_top_tv2);
        textView2 = headerView.findViewById(R.id.item_main_top_tv4);
        textView3 = headerView.findViewById(R.id.item_main_top_tv5);
        textView4 = headerView.findViewById(R.id.item_main_top_money);
        topShow = headerView.findViewById(R.id.item_main_top_src);

        textView3.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShow.setOnClickListener(this);

    }
    /** 设置ListView的长按事件*/
    private void setLVLongClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  //点击了头布局
                    return false;
                }
                int pos = position-1;
                AccountBean clickBean = mDatas.get(pos);  //获取正在被点击的这条信息

                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
    /* 弹出是否删除某一条记录的对话框*/
    private void showDeleteItemDialog(final  AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录么？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManger.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);   //实时刷新，移除集合当中的对象
                        adapter.notifyDataSetChanged();   //提示适配器更新数据
                        setTopTvShow();   //改变头布局TextView显示的内容
                    }
                });
        builder.create().show();   //显示对话框
    }
    /** 初始化自带的View的方法*/
    private void initView() {
        listView = findViewById(R.id.main);
        button = findViewById(R.id.main_button);
        imageView1 = findViewById(R.id.main_search);
        imageView2 = findViewById(R.id.main_list);
        button.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);

        setLVLongClickListener();
    }
    // 当activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    /* 设置头布局当中文本内容的显示*/
    private void setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        float incomeOneDay = DBManger.getSumMoneyOneDay(year, month, day, 0);
        float outcomeOneDay = DBManger.getSumMoneyOneDay(year, month, day, 1);
        String infoOneDay = "今日支出 ￥"+outcomeOneDay+"  收入 ￥"+incomeOneDay;
        textView4.setText(infoOneDay);
        //获取本月收入和支出总金额
        float incomeOneMonth = DBManger.getSumMoneyOneMonth(year, month, 0);
        float outcomeOneMonth = DBManger.getSumMoneyOneMonth(year, month, 1);
        textView2.setText("￥"+incomeOneMonth);
        textView1.setText("￥"+outcomeOneMonth);
        //设置显示运算剩余
        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            textView3.setText("￥ 0");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            textView3.setText("￥"+syMoney);
        }
    }

    /* 获取今日的具体时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    // 加载数据库数据
    private void loadDBData() {
        List<AccountBean> list = DBManger.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.main_search:
                Intent it = new Intent(this,SearchActivity.class);  //跳转
                startActivity(it);
                break;

            case R.id.main_button:
                Intent intent = new Intent(this,RecordActivity.class);  //跳转
                startActivity(intent);
                break;

            case R.id.main_list:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;

            case R.id.item_main_top_tv5:
                showBudgetDialog();
                break;

            case R.id.item_main_top_src:
                // 切换TextView明文和密文
                toggleShow();
                break;

        }
    }

    /** 显示运算设置对话框*/
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额写入到共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                //计算剩余金额
                float outcomeOneMonth = DBManger.getSumMoneyOneMonth(year, month, 1);
                float syMoney = money-outcomeOneMonth;//预算剩余 = 预算-支出
                textView3.setText("￥"+syMoney);
            }
        });
    }

    boolean isShow = true;
    /**
     *
     * */
    private void toggleShow() {
        if (isShow) {   //隐藏
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            textView1.setTransformationMethod(passwordMethod);
            textView2.setTransformationMethod(passwordMethod);
            textView3.setTransformationMethod(passwordMethod);
            topShow.setImageResource(R.mipmap.image54);
            isShow = false;   //状态改变
        }else{  //显示
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            textView1.setTransformationMethod(hideMethod);
            textView2.setTransformationMethod(hideMethod);
            textView3.setTransformationMethod(hideMethod);
            topShow.setImageResource(R.mipmap.image55);
            isShow = true;   //设置标志位为隐藏状态
        }
    }
}