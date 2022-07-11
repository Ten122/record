package com.example.recordapp.fragrecord;

import com.example.recordapp.R;
import com.example.recordapp.db.DBManger;
import com.example.recordapp.db.TypeBean;

import java.util.List;

public class OutFragment extends  BaseFragment{
    //public 重写
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库中的数据源
        List<TypeBean> outlist = DBManger.getTypeList(1);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        imageView.setImageResource(R.mipmap.image6);
        textView3.setText("其他");
    }

    @Override
    public void saveDataToDB() {
        accountBean.setKind(1);
        DBManger.insertItemToAccounttb(accountBean);
    }
}
