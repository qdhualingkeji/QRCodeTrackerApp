package com.hualing.qrcodetracker.activities.operation_user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
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

public class UserModifyActivity extends BaseActivity {

    private static final int SELECT_BU_MEN = 11;
    private static final int SELECT_QUAN_XIAN = 12;
    private static final int SELECT_SHEN_FEN=13;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.trueNameValue)
    EditText mTrueName;
    @BindView(R.id.bmValue)
    TextView mBmValue;
    @BindView(R.id.loginNameValue)
    EditText mLoginNameValue;
    @BindView(R.id.passwordValue)
    EditText mPasswordValue;
    @BindView(R.id.password2Value)
    EditText mPassword2Value;
    @BindView(R.id.sfValue)
    TextView mSfValue;
    @BindView(R.id.qxValue)
    TextView mQxValue;
    private User param;
    private PersonResult updatedParam;
    private MainDao mainDao;
    private int userId;
    private int groupID;
    private String checkQXGroup;
    private String sfId;

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);
        param = new User();
        if (getIntent() != null) {
            userId = getIntent().getIntExtra("userID", 0);
            param.setUserID(userId);
        }
        updatedParam = new PersonResult();

        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(UserModifyActivity.this);
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
                            mTrueName.setText(dataResult.getTrueName());
                            groupID = dataResult.getGroupID();
                            mBmValue.setText(dataResult.getGroupName());
                            mLoginNameValue.setText(dataResult.getLoginName());
                            checkQXGroup = dataResult.getCheckQXGroup();
                            String sfName=null;
                            if(checkQXGroup.contains("zjy")) {
                                sfId="zjy";
                                sfName = "质检员";
                            }
                            else if(checkQXGroup.contains("zjld")) {
                                sfId="zjld";
                                sfName = "质检领导";
                            }
                            else if(checkQXGroup.contains("bz")) {
                                sfId="bz";
                                sfName = "班长";
                            }
                            else if(checkQXGroup.contains("kg")) {
                                sfId="kg";
                                sfName = "库管";
                            }
                            else if(checkQXGroup.contains("fzr")) {
                                sfId="fzr";
                                sfName = "负责人";
                            }
                            else{
                                sfId="ptyg";
                                sfName = "普通员工";
                            }
                            mSfValue.setText(sfName);
                            mQxValue.setText(dataResult.getQxNameGroup());
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_modify;
    }

    @OnClick({R.id.selectBM,R.id.selectSF,R.id.selectQX,R.id.submitBtn,R.id.returnBtn})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.selectBM:
                bundle.putInt("flag", 1);
                IntentUtil.openActivityForResult(UserModifyActivity.this, SelectPersonGroupActivity.class, SELECT_BU_MEN, bundle);
                break;
            case R.id.selectSF:
                IntentUtil.openActivityForResult(this, SelectIdentityActivity.class, SELECT_SHEN_FEN, null);
                break;
            case R.id.selectQX:
                String sfValue = mSfValue.getText().toString();
                if("请选择身份".equals(sfValue)){
                    Toast.makeText(this, "请先选择身份", Toast.LENGTH_SHORT).show();
                }
                else {
                    bundle.putString("sfId", sfId);
                    IntentUtil.openActivityForResult(UserModifyActivity.this, SelectModule2Activity.class, SELECT_QUAN_XIAN, bundle);
                }
                break;
            case R.id.submitBtn:
                toCommit();
                break;
            case R.id.returnBtn:
                AllActivitiesHolder.removeAct(UserModifyActivity.this);
                break;
        }
    }

    private void toCommit() {
        String trueName = mTrueName.getText().toString();
        String bmValue = mBmValue.getText().toString();
        String loginNameValue = mLoginNameValue.getText().toString();
        String sfValue = mSfValue.getText().toString();
        String qxValue = mQxValue.getText().toString();
        String passwordValue = mPasswordValue.getText().toString();
        String password2Value = mPassword2Value.getText().toString();

        if(TextUtils.isEmpty(trueName)
                ||groupID==0
                ||TextUtils.isEmpty(bmValue)
                ||TextUtils.isEmpty(loginNameValue)
                ||TextUtils.isEmpty(checkQXGroup)
                ||TextUtils.isEmpty(sfValue)
                ||TextUtils.isEmpty(qxValue)
                ){
            Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!passwordValue.equals(password2Value)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        updatedParam.setUserId(userId);
        updatedParam.setTrueName(trueName);
        updatedParam.setGroupID(groupID);
        updatedParam.setLoginName(loginNameValue);
        if(!TextUtils.isEmpty(passwordValue))
            updatedParam.setPassword(passwordValue);
        updatedParam.setShenFen(sfId);
        updatedParam.setCheckQXGroup(checkQXGroup);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.updateUserData(updatedParam);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<ActionResult>>() {
                    @Override
                    public void accept(ActionResult<ActionResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(UserModifyActivity.this, UserSearchActivity.class);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_BU_MEN:
                    groupID = data.getIntExtra("groupID", 0);
                    String groupName = data.getStringExtra("groupName");
                    mBmValue.setText(groupName);
                    break;
                case SELECT_SHEN_FEN:
                    mSfValue.setText(data.getStringExtra("sfName"));
                    sfId=data.getStringExtra("sfId");
                    checkQXGroup="";
                    mQxValue.setText("");
                    break;
                case SELECT_QUAN_XIAN:
                    String allQxId = data.getStringExtra("allQxId");
                    if(!TextUtils.isEmpty(allQxId))
                        checkQXGroup = allQxId;
                    String allQxName = data.getStringExtra("allQxNameStr");
                    if(!TextUtils.isEmpty(allQxName))
                        mQxValue.setText(allQxName);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
