package com.hualing.qrcodetracker.activities.operation_user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectIdentityActivity extends BaseActivity {

    @BindView(R.id.dataList)
    ListView mListView;
    SimpleAdapter mAdapter;
    List<Map<String, String>> list;

    @Override
    protected void initLogic() {

        list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "普通员工");
        map.put("id", "ptyg");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("name", "质检员");
        map.put("id", "zjy");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("name", "质检领导");
        map.put("id", "zjld");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("name", "班长");
        map.put("id", "bz");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("name", "库管");
        map.put("id", "kg");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("name", "(车间/仓库)负责人");
        map.put("id", "fzr");
        list.add(map);
        mAdapter=new SimpleAdapter(this,list,R.layout.item_shen_fen,new String[]{"name","id"},new int[]{R.id.nameValue,R.id.idValue});
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> sfMap = list.get(position);
                Intent ii = new Intent();
                ii.putExtra("sfId",sfMap.get("id"));
                ii.putExtra("sfName",sfMap.get("name"));
                setResult(RESULT_OK,ii);
                AllActivitiesHolder.removeAct(SelectIdentityActivity.this);
            }
        });
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_identity;
    }

    @OnClick(R.id.lastButton)
    public void onViewClicked() {
        AllActivitiesHolder.removeAct(this);
    }
}
