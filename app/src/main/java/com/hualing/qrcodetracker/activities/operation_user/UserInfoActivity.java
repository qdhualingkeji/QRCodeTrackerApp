package com.hualing.qrcodetracker.activities.operation_user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.PersonBean;
import com.hualing.qrcodetracker.bean.PersonResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.User;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.trueNameValue)
    TextView trueName;
    @BindView(R.id.loginNameValue)
    TextView loginName;
    @BindView(R.id.groupValue)
    TextView groupName;
    @BindView(R.id.regTimeValue)
    TextView regTime;
    @BindView(R.id.sfValue)
    TextView mSfValue;
    @BindView(R.id.qxValue)
    TextView qxNameGroup;
    private MainDao mainDao;
    private User param;
    private int userId;

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);
        param = new User();
        if (getIntent() != null) {
            userId = getIntent().getIntExtra("userID", 0);
            param.setUserID(userId);
        }

        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(UserInfoActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<PersonResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<PersonResult>> e) throws Exception {
                ActionResult<PersonResult> nr = mainDao.getPersonById(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<PersonResult>>() {
                    @Override
                    public void accept(ActionResult<PersonResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            PersonResult dataResult = result.getResult();
                            trueName.setText(dataResult.getTrueName());
                            loginName.setText(dataResult.getLoginName());
                            groupName.setText(dataResult.getGroupName());
                            regTime.setText(dataResult.getRegTime());
                            String checkQXGroup = dataResult.getCheckQXGroup();
                            String sfName=null;
                            if(checkQXGroup.contains("zjy"))
                                sfName="质检员";
                            else if(checkQXGroup.contains("zjld"))
                                sfName="质检领导";
                            if(checkQXGroup.contains("bz"))
                                sfName="班长";
                            if(checkQXGroup.contains("zjy"))
                                sfName="质检员";
                            mSfValue.setText(sfName);
                            qxNameGroup.setText(dataResult.getQxNameGroup());
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_info;
    }

    @OnClick({R.id.updateBtn,R.id.returnBtn})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.updateBtn:
                Bundle bundle = new Bundle();
                bundle.putInt("userID", userId);
                IntentUtil.openActivityForResult(UserInfoActivity.this,UserModifyActivity.class,-1,bundle);
                break;
            case R.id.returnBtn:
                AllActivitiesHolder.removeAct(UserInfoActivity.this);
                break;
        }
    }
}
