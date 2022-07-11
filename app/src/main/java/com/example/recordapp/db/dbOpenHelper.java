package com.example.recordapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.recordapp.R;

public class dbOpenHelper extends SQLiteOpenHelper {

    public dbOpenHelper(@Nullable Context context) {
        super(context, "tally.db", null, 1);
    }

    //创建数据库方法，第一次使用调用
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table typetb(id integer primary key autoincrement, typename varchar(10), imageId integer, sImageId integr, kind integer)";
        sqLiteDatabase.execSQL(sql);
        insertType(sqLiteDatabase);

        sql = "create table accounttb(id integer primary key autoincrement, typename vachar(10), sImageId integr, record vachar(100), " +
                "money float, time varchar(60), year integer, month integer, day integer, kind integer)";
        sqLiteDatabase.execSQL(sql);
    }

    private void insertType(SQLiteDatabase sqLiteDatabase) {
        //插入元素
        String sql = "insert into typetb (typename,imageId,sImageId,kind) values (?,?,?,?)";
        sqLiteDatabase.execSQL(sql,new Object[]{"其他", R.mipmap.image6,R.mipmap.image7,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"餐饮", R.mipmap.image8,R.mipmap.image9,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"交通", R.mipmap.image10,R.mipmap.image11,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"购物", R.mipmap.image12,R.mipmap.image13,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"服饰", R.mipmap.image14,R.mipmap.image15,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"日用品", R.mipmap.image16,R.mipmap.image17,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"娱乐", R.mipmap.image18,R.mipmap.image19,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"零食", R.mipmap.image20,R.mipmap.image21,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"烟酒茶", R.mipmap.image22,R.mipmap.image23,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"学习", R.mipmap.image24,R.mipmap.image25,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"医疗", R.mipmap.image26,R.mipmap.image27,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"住宅", R.mipmap.image28,R.mipmap.image29,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"水电煤", R.mipmap.image30,R.mipmap.image31,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"通讯", R.mipmap.image32,R.mipmap.image33,1});
        sqLiteDatabase.execSQL(sql,new Object[]{"人情往来", R.mipmap.image34,R.mipmap.image35,1});

        sqLiteDatabase.execSQL(sql,new Object[]{"其他", R.mipmap.image36,R.mipmap.image37,0});
        sqLiteDatabase.execSQL(sql,new Object[]{"薪资", R.mipmap.image38,R.mipmap.image39,0});
        sqLiteDatabase.execSQL(sql,new Object[]{"奖金", R.mipmap.image40,R.mipmap.image41,0});
        sqLiteDatabase.execSQL(sql,new Object[]{"借入", R.mipmap.image42,R.mipmap.image43,0});
        sqLiteDatabase.execSQL(sql,new Object[]{"收债", R.mipmap.image44,R.mipmap.image45,0});
        sqLiteDatabase.execSQL(sql,new Object[]{"利息收入", R.mipmap.image46,R.mipmap.image47,0});
        sqLiteDatabase.execSQL(sql,new Object[]{"投资回报", R.mipmap.image48,R.mipmap.image49,0});
        sqLiteDatabase.execSQL(sql,new Object[]{"二手交易", R.mipmap.image50,R.mipmap.image51,0});
        sqLiteDatabase.execSQL(sql,new Object[]{"意外所得", R.mipmap.image52,R.mipmap.image53,0});

    }

    //数据库版本更新时发生改变，调用方法
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
