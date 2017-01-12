package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.ustcxiaoqie.learn.processoflearning.PossiableCityAdapter;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.tools.AvailableCity;
import com.ustcxiaoqie.learn.processoflearning.tools.DataTransferTool;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Xiaoqie on 2017/1/9.
 */

public class SearchActivity extends Activity {
    private static final String TAG = "SearchActivity";
    private EditText mSearchEditText;
    private List<AvailableCity> AllCitiesList;
    private List<AvailableCity> PossiableCitiesList;
    private ListView possible_city_lv;

    private SQLiteDatabase mSQLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();
        intiViews();
    }

    private void intiViews() {
        mSearchEditText = (EditText) findViewById(R.id.search_et);
        possible_city_lv = (ListView) findViewById(R.id.possiable_lv);
        final PossiableCityAdapter adapter = new PossiableCityAdapter(this,PossiableCitiesList);
        possible_city_lv.setAdapter(adapter);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                beginToSearch(String.valueOf(editable).trim());
                if(PossiableCitiesList.size() == 0 || editable.length() == 0){
                    possible_city_lv.setVisibility(GONE);
                }else{
                    possible_city_lv.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * S  必须连续存在于对应自字符中  v1.0.2
     * params s: 待搜索string
     */
    private void beginToSearch(String s) {
        if(null == AllCitiesList) return;
        LA.d(TAG,"s:"+s);
        LA.d(TAG,"s:"+s.length());
        PossiableCitiesList.clear();
        for(int i = 0 ; i< AllCitiesList.size();i++) {
            if(AllCitiesList.get(i).getName().toLowerCase().contains(s.toLowerCase())){

                PossiableCitiesList.add(AllCitiesList.get(i));
                if(PossiableCitiesList.size()>3) break;  //只保留4个待选项
            }
        }
    }


    private void initData() {
        PossiableCitiesList = new ArrayList<>();

        try {
            AllCitiesList = DataTransferTool.getAvailableCityList(this);
        } catch (IOException e) {
            e.printStackTrace();
            LA.d(TAG,"IO异常！");
            AllCitiesList = null;
            return;
        } catch (JSONException e) {
            e.printStackTrace();
            LA.d(TAG,"JSON异常！");
            AllCitiesList = null;
            return;
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LA.d(TAG,"huishou");
    }

}
