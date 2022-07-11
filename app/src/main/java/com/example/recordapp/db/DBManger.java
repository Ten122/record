package com.example.recordapp.db;

import android.content.ContentValues;
import  android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责管理数据库
 */
public class DBManger {

    private static SQLiteDatabase sqLiteOpenHelper;
    /*初始化*/
    public static void initDB(Context context)
    {
        dbOpenHelper helper = new dbOpenHelper(context); //帮助类对象
        sqLiteOpenHelper = helper.getWritableDatabase(); //得到对象
    }

    /**
     * 读取数据库中的数据
     */

    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean>list = new ArrayList<>();
        //读取type表中数据
        String sql = "select * from typetb where kind =" + kind;
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql, null);

        //循环读取游标内容，储存在数据对象中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;
    }

    /**
     * 向记账表中插入一条元素
     */
    public static  void insertItemToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("record",bean.getRecord());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        sqLiteOpenHelper.insert("accounttb",null,values);
    }

    /**
     * 获取记账表中某天所有支出或收入情况
     */
    public static List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String record = cursor.getString(cursor.getColumnIndexOrThrow("record"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, record, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /*
     * 获取记账表当中某一月的所有支出或者收入情况
     * */
    public static List<AccountBean>getAccountListOneMonthFromAccounttb(int year,int month){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String bz = cursor.getString(cursor.getColumnIndexOrThrow("record"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /**
     * 获取某一天的支出或者收入的总金额   kind：支出==1    收入===0
     * */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }
    /**
     * 获取搜索到的支出和收入总和
     */
    public static float getSumMoneyFromAccounttb(String record, int kind)
    {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where like '%"+record+"%' and kind=?";
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql,new String[]{kind + ""});
        if (cursor.moveToFirst())
        {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }

    /**
     * 获取某一月的支出或者收入的总金额   kind：支出==1    收入===0
     * */
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }
    /**
     * 统计某月份支出或者收入情况有多少条  收入-1   支出-0
     * */
    public static int getCountItemOneMonth(int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count(money)"));
            total = count;
        }
        return total;
    }
    /**
     * 获取某一年的支出或者收入的总金额   kind：支出==1    收入===0
     * */
    public static float getSumMoneyOneYear(int year,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql, new String[]{year + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }
    /*
     * 根据传入的id，删除accounttb表当中的一条数据
     * */
    public static int deleteItemFromAccounttbById(int id){
        int i = sqLiteOpenHelper.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }
    /**
     * 根据备注搜索收入或者支出的情况列表
     * */
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String record){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where record like '%"+record+"%'";  //模糊查询
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String bz = cursor.getString(cursor.getColumnIndexOrThrow("record"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /**
     * 查询记账的表当中有几个年份信息
     * */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = sqLiteOpenHelper.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            list.add(year);
        }
        return list;
    }

    /*
     * 删除accounttb表格当中的所有数据
     * */
    public static void deleteAllAccount(){
        String sql = "delete from accounttb";
        sqLiteOpenHelper.execSQL(sql);
    }



}
