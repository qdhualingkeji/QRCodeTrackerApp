package com.hualing.qrcodetracker.activities.operation_user;

import android.app.Activity;
import android.os.Bundle;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;

public class UserRegisterActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;

    @Override
    protected void initLogic() {

        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(UserRegisterActivity.this);
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
        return R.layout.activity_user_register;
    }
}
