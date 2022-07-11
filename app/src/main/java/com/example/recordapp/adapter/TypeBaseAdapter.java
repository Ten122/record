package com.example.recordapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recordapp.R;
import com.example.recordapp.db.TypeBean;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {

    Context context;
    List<TypeBean>mDatas;
    public int selecPos = 0; //选中的位置

    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount()  {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    //不考虑复用，所有item都显示在界面,不会因为滑动消失
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_recordfrg,viewGroup,false);
        //查找布局中的控件
        ImageView iv = view.findViewById(R.id.item_record_src);
        TextView tv = view.findViewById(R.id.item_record_tv);
        //获取指定位置的数据源
        TypeBean typeBean = mDatas.get(i);
        tv.setText(typeBean.getTypename());
        //判定当前位置是否为选中位置
        if(selecPos == i)
        {
            iv.setImageResource(typeBean.getsImageId());
        }else{
            iv.setImageResource(typeBean.getImageId());
        }


        return view;
    }

}
