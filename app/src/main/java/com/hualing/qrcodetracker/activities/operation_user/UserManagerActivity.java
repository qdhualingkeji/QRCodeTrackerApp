package com.hualing.qrcodetracker.activities.operation_user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

public class UserManagerActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(UserManagerActivity.this);
            }

            @Override
            public void clickRightButton() {

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
        return R.layout.activity_user_manager;
    }

    @OnClick({R.id.userRegister, R.id.userSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userRegister:
                IntentUtil.openActivity(UserManagerActivity.this, UserRegisterActivity.class);
                break;
            case R.id.userSearch:
                //IntentUtil.openActivity(UserManagerActivity.this, UserSearchActivity.class);
                break;
        }
    }
}
