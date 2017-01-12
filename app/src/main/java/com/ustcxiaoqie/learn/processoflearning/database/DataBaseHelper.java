package com.ustcxiaoqie.learn.processoflearning.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.ustcxiaoqie.learn.processoflearning.tools.LA;

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

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      /*  String sql = "";
        if(db_name.equals(TABLE_FAVORATE_CITIES)){
            sql = "create table "+TABLE_FAVORATE_CITIES+"(_id integer primary key autoincrement, city_name, city_id)";
        }else if(db_name.equals(TABLE_CITIES)){
            sql = "create table "+TABLE_FAVORATE_CITIES+"(_id integer primary key autoincrement, city_name, city_id,city_country,city_population)";
        }else{
            throw new IllegalArgumentException();
        }*/
        sqLiteDatabase.execSQL("create table "+TABLE_FAVORATE_CITIES+"(_id integer primary key autoincrement, city_name, city_id)");
    //    sqLiteDatabase.execSQL("create table "+TABLE_CITIES+ "(_id integer primary key autoincrement, city_name, city_id,city_country,city_population)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1 != i){
            LA.d(TAG,"onUpgrade");
        }
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
}
