package com.ustcxiaoqie.learn.processoflearning.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.ustcxiaoqie.learn.processoflearning.tools.LA;

import java.util.Date;

/**
 * Created by Xiaoqie on 2017/1/12.
 */

public class DataBaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "DataBaseHelper";
    public final static String DB_NAME = "weather.db";
    public final static String TABLE_FAVORATE_CITIES = "CITY_FAVORITE";
    public final static String TABLE_CITIES = "CITIES";
    private final static int version = 1;

    private String db_name;

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.db_name = name;
    }
    public DataBaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DataBaseHelper(Context context, String name) {
        this(context, name, version);
    }
    public DataBaseHelper(Context context){
        this(context,DB_NAME);
    }
    public DataBaseHelper(Context context,int version){
        this(context,DB_NAME,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        LA.d(TAG,"数据库创建....");
        //Version 1
    /*    sqLiteDatabase.execSQL("create table "+TABLE_FAVORATE_CITIES+" (_id integer primary key autoincrement, city_name, city_id)");
        ContentValues values = new ContentValues();
        values.put("city_name","Hefei");
        values.put("city_id",1808722);
        insertIntoFavoriteCity(sqLiteDatabase,null,values,true);

        ContentValues values0 = new ContentValues();
        values0.put("city_name","Wuhan");
        values0.put("city_id",1791247);
        insertIntoFavoriteCity(sqLiteDatabase,null,values0,true);*/

        //Version 2
        sqLiteDatabase.execSQL("create table "+TABLE_FAVORATE_CITIES+" (_id integer primary key autoincrement, city_name, city_id,time_favorite))");
        ContentValues values = new ContentValues();
        values.put("city_name","Hefei");
        values.put("city_id",1808722);
        values.put("city_id",new Date().toString());
        insertIntoFavoriteCity(sqLiteDatabase,null,values,true);

        ContentValues values0 = new ContentValues();
        values0.put("city_name","Wuhan");
        values0.put("city_id",1791247);
        values0.put("city_id",new Date().toString());
        insertIntoFavoriteCity(sqLiteDatabase,null,values0,true);

        ContentValues values1 = new ContentValues();
        values1.put("city_name","Wuhan");
        values1.put("city_id",1791247);
        values1.put("city_id",new Date().toString());
        insertIntoFavoriteCity(sqLiteDatabase,null,values1,true);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /**
         * 考虑到oldversion对用户来说，可能是不同的
         * 所以采用数据库逐个版本升级的方法
         */
        LA.d(TAG,"数据库升级中....");
       for(int j = oldVersion ; j <newVersion ; j++){
            switch (j){
                case 1 :
                    //负责版本1升到2
                    String sql = "alter table "+TABLE_FAVORATE_CITIES +" add column time_favorite default '2017-1-14'";

                    sqLiteDatabase.execSQL(sql);
                    break;
                case 2:
                    //负责版本2升到3
                    break;
            }
       }
        LA.d(TAG,"数据库升级完毕！ "+"FROM "+oldVersion +" TO "+newVersion);
    }
    public void insertIntoFavoriteCity(SQLiteDatabase db,String nullcolomnHack,ContentValues values,@NonNull boolean isOverWrite){
        //  isOverWrite 是否覆盖之前的城市数据
        if(isOverWrite){
            Cursor cursor = queryFromFavoriteCity(db,null,"city_name=?",new String[]{values.getAsString("city_name")},null,null,null);
            if(!cursor.moveToNext()) {
                //数据库之前不存在这个城市
                db.insert(TABLE_FAVORATE_CITIES,null,values);
            }else{
                db.update(TABLE_FAVORATE_CITIES,values,"city_name=?",new String[]{values.getAsString("city_name")});
            }
            cursor.close();
        }else{
            db.insert(TABLE_FAVORATE_CITIES,null,values);
        }

    }

    public Cursor queryFromFavoriteCity(SQLiteDatabase db,String[] columns,String selection,String[] selectionArgs,String groupBy,String having,String orderBy){
        return db.query(TABLE_FAVORATE_CITIES,columns,selection,selectionArgs,groupBy,having,orderBy);
    }
    public void updateToFavoriteCity(SQLiteDatabase sb,ContentValues values,String whereClause,String[] whereArgs){
        sb.update(TABLE_FAVORATE_CITIES,values,whereClause,whereArgs);
    }

    public void clearDataBase(SQLiteDatabase db,boolean confime){
        if(confime){
        }
    }
}
