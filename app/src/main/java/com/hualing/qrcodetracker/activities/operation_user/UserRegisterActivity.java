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
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectHlProductActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.PersonBean;
import com.hualing.qrcodetracker.bean.PersonParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
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

public class UserRegisterActivity extends BaseActivity {

    private static final int SELECT_BU_MEN=10;
    private static final int SELECT_QUAN_XIAN=11;
    private static final int SELECT_SHEN_FEN=12;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.bmValue)
    TextView mBmValue;
    @BindView(R.id.trueNameValue)
    EditText mTrueNameValue;
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
    private MainDao mainDao;
    private PersonParam params;
    private int mSelectBMId;
    private String allQxId;
    private String sfId;

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);
        params=new PersonParam();

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

    private boolean checkIfInfoPerfect(){
        String mbValue = mBmValue.getText().toString();
        String trueNameValue = mTrueNameValue.getText().toString();
        String loginNameValue = mLoginNameValue.getText().toString();
        String passwordValue = mPasswordValue.getText().toString();
        String password2Value = mPassword2Value.getText().toString();
        String sfValue = mSfValue.getText().toString();
        String qxValue = mQxValue.getText().toString();
        if("请选择部门".equals(mbValue)
                ||TextUtils.isEmpty(trueNameValue)
                ||TextUtils.isEmpty(loginNameValue)
                ||TextUtils.isEmpty(passwordValue)
                ||TextUtils.isEmpty(password2Value)
                ||"请选择身份".equals(sfValue)
                ||"请选择权限".equals(qxValue)
                ) {
            Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!passwordValue.equals(password2Value)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }

        params.setGroupID(mSelectBMId);
        params.setTrueName(trueNameValue);
        params.setLoginName(loginNameValue);
        params.setPassword(passwordValue);
        params.setCheckQXGroup(allQxId);
        return true;
    }

    private void commitDataToWeb() {

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.commitUserRegisteredData(params);
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
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            AllActivitiesHolder.removeAct(UserRegisterActivity.this);
                        }
                    }
                });

    }

    @OnClick({R.id.selectBM,R.id.selectSF,R.id.selectQX,R.id.commitBtn})
    public void onViewClicked(View view){

        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.selectBM:
                bundle.putInt("flag",1);
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, SELECT_BU_MEN, bundle);
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
                    IntentUtil.openActivityForResult(this, SelectModule2Activity.class, SELECT_QUAN_XIAN, bundle);
                }
                break;
            case R.id.commitBtn:
                //数据录入是否完整
                if (checkIfInfoPerfect()) {
                    commitDataToWeb();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_BU_MEN:
                    mBmValue.setText(data.getStringExtra("groupName"));
                    mSelectBMId=data.getIntExtra("groupID",0);
                    break;
                case SELECT_SHEN_FEN:
                    mSfValue.setText(data.getStringExtra("sfName"));
                    sfId=data.getStringExtra("sfId");
                    break;
                case SELECT_QUAN_XIAN:
                    mQxValue.setText(data.getStringExtra("allQxNameStr"));
                    allQxId = data.getStringExtra("allQxId");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
