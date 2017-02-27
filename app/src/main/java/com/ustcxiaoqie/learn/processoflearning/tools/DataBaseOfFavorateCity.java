package com.ustcxiaoqie.learn.processoflearning.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ustcxiaoqie.learn.processoflearning.database.DataBaseHelper;
import com.ustcxiaoqie.learn.processoflearning.database.Table_Structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiaoqie on 2017/1/13.
 */

public class DataBaseOfFavorateCity {
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;
    public DataBaseOfFavorateCity(Context context){
        this.mContext = context;
    }
    public List<City> getCities(){
        List<City> cityList = new ArrayList<>();
        DataBaseHelper helper = new DataBaseHelper(mContext,Constant.DATABASE_VERSION);
        mSQLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = helper.queryFromFavoriteCity(mSQLiteDatabase,
                new String[]{Table_Structure.TABLE_FAVORATE_CITIES.city_name
                        ,Table_Structure.TABLE_FAVORATE_CITIES.city_id},
                null,null,null,null,null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setId(cursor.getInt(1));
            city.setName(cursor.getString(0));
            cityList.add(city);
        }
        helper.close();
        return cityList;
    }
}
