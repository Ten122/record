package com.example.recordapp.fragrecord;


import com.example.recordapp.R;
import com.example.recordapp.db.DBManger;
import com.example.recordapp.db.TypeBean;

import java.util.List;

/**
 * 收入
 */
public class InFragment extends BaseFragment {


    //public 重写
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库中的数据源
        List<TypeBean> inlist = DBManger.getTypeList(0);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
        imageView.setImageResource(R.mipmap.image36);
        textView3.setText("其他");
    }

    @Override
    public void saveDataToDB() {
        accountBean.setKind(0);
        DBManger.insertItemToAccounttb(accountBean);
    }
}