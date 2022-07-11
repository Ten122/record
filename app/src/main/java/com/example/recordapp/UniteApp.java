package com.example.recordapp;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.recordapp.db.DBManger;
import com.example.recordapp.db.TypeBean;

import java.util.ArrayList;
import java.util.List;

public class UniteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DBManger.initDB(getApplicationContext());
    }


}
