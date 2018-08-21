package com.hualing.qrcodetracker.activities.operation_cp.cp_in;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BCPRKDResult;
import com.hualing.qrcodetracker.bean.CreateBCPRKDParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.CPType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author 具体操作和半成品一样，因为操作的都是rkd_bcp表
 * @date 2018-3-17
 */
public class CPRKDInputActivity extends BaseActivity {

    private static final int GET_DEPARTMENT = 10;
    private static final int REQUEST_CODE_SELECT_JHFZR = 31;
    private static final int REQUEST_CODE_SELECT_SHFZR = 32;
    private static final int REQUEST_CODE_SELECT_SHR = 33;
    private static final int REQUEST_CODE_SELECT_ZJY = 34;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.departmentValue)
    TextView mDepartmentValue;
    @BindView(R.id.ShrValue)
    TextView mShrValue;
    @BindView(R.id.ShFzrValue)
    TextView mShFzrValue;
    @BindView(R.id.JhFhrValue)
    TextView mJhFhrValue;
    @BindView(R.id.zjyValue)
    TextView mZjyValue;
    private int fzrID;
    private int zjyID;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;

    private MainDao mainDao;

    private CreateBCPRKDParam params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);


        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(CPRKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        params = new CreateBCPRKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cprkdinput;
    }

    @OnClick({R.id.selectBM, R.id.commitBtn, R.id.selectSHR, R.id.selectSHFZR, R.id.selectJHFZR, R.id.selectZJY})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.selectBM:
                IntentUtil.openActivityForResult(this, SelectDepartmentActivity.class, GET_DEPARTMENT, null);
                break;
            case R.id.selectSHR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_SHR, null);
                break;
            case R.id.selectSHFZR:
                bundle.putString("checkQX", "ld");
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_SHFZR, bundle);
                break;
            case R.id.selectJHFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_JHFZR, null);
                break;
            case R.id.selectZJY:
                bundle.putString("checkQX", "zjy");
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_ZJY, bundle);
                break;
            case R.id.commitBtn:
                commitDataToWeb();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_DEPARTMENT:
                    mDepartmentValue.setText(data.getStringExtra("groupName"));
                    break;
                case REQUEST_CODE_SELECT_JHFZR:
                    mJhFhrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_SHFZR:
                    fzrID=data.getIntExtra("personID",0);
                    mShFzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_SHR:
                    mShrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_ZJY:
                    zjyID=data.getIntExtra("personID",0);
                    mZjyValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkDataIfCompleted() {
        String fhdwValue = mDepartmentValue.getText().toString();
        String shrValue = mShrValue.getText().toString();
        String shfzrValue = mShFzrValue.getText().toString();
        String jhfzrValue = mJhFhrValue.getText().toString();
        if (TextUtils.isEmpty(fhdwValue)
                || "请选择部门".equals(fhdwValue)
                || "请选择收货人".equals(shrValue)
                || "请选择收货负责人".equals(shfzrValue)
                || "请选择交货负责人".equals(jhfzrValue)) {
            return false;
        }
        params.setJhDw(fhdwValue);
        params.setShr(shrValue);
        params.setShFzr(shfzrValue);
        params.setJhr(GlobalData.realName);
        params.setJhFzr(jhfzrValue);
        params.setFzrID(fzrID);
        params.setFzrStatus(0);
        params.setZjyID(zjyID);
        params.setZjyStatus(0);
        params.setRemark(mRemarkValue.getText().toString());
        return true;
    }

    private void commitDataToWeb() {
        if (!checkDataIfCompleted()) {
            Toast.makeText(this, "数据录入不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BCPRKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BCPRKDResult>> e) throws Exception {
                ActionResult<BCPRKDResult> nr = mainDao.createBCP_RKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BCPRKDResult>>() {
                    @Override
                    public void accept(ActionResult<BCPRKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "入库单创建成功~", Toast.LENGTH_SHORT).show();
                            //保存物料入库单号
                            //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                            BCPRKDResult rkdResult = result.getResult();
                            SharedPreferenceUtil.setBCPRKDNumber(String.valueOf(rkdResult.getIndh()));
                            //去选择大包装入库还是小包装入库
                            //IntentUtil.openActivity(CPRKDInputActivity.this, CPInProductStylePickActivity.class);
                            switch (GlobalData.currentCPInType) {
                                case CPType.BIG_CP_IN:
                                    IntentUtil.openActivity(CPRKDInputActivity.this, BigCPInDataInputActivity.class);
                                    break;
                                case CPType.SMALL_CP_IN:
                                    IntentUtil.openActivity(CPRKDInputActivity.this, SmallCPInDataInputActivity.class);
                                    break;
                            }
                            AllActivitiesHolder.removeAct(CPRKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
