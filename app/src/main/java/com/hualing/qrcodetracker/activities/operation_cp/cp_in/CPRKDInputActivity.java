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
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
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
    private static final int REQUEST_CODE_SELECT_BZ = 32;
    private static final int REQUEST_CODE_SELECT_JHFZR = 33;
    private static final int REQUEST_CODE_SELECT_ZJY = 34;
    private static final int REQUEST_CODE_SELECT_ZJLD = 35;
    private static final int REQUEST_CODE_SELECT_KG = 36;
    private static final int REQUEST_CODE_SELECT_SHFZR = 37;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.departmentValue)
    TextView mDepartmentValue;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.JhFhrValue)
    TextView mJhFhrValue;
    @BindView(R.id.zjyValue)
    TextView mZjyValue;
    @BindView(R.id.zjldValue)
    TextView mZjldValue;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.ShFzrValue)
    TextView mShFzrValue;
    private int bzID;
    private int flfzrID;
    private int zjyID;
    private int zjldID;
    private int kgID;
    private int llfzrID;
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

    @OnClick({R.id.selectBM, R.id.selectBZ, R.id.selectJHFZR, R.id.selectZJY, R.id.selectZJLD, R.id.selectKG, R.id.selectSHFZR, R.id.commitBtn})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.selectBM:
                IntentUtil.openActivityForResult(this, SelectDepartmentActivity.class, GET_DEPARTMENT, null);
                break;
            case R.id.selectBZ:
                bundle.putString("checkQX", "bz");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_BZ, bundle);
                break;
            case R.id.selectJHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_JHFZR, bundle);
                break;
            case R.id.selectZJY:
                bundle.putString("checkQX", "zjy");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_ZJY, bundle);
                break;
            case R.id.selectZJLD:
                bundle.putString("checkQX", "zjld");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_ZJLD, bundle);
                break;
            case R.id.selectKG:
                bundle.putString("checkQX", "kg");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_KG, bundle);
                break;
            case R.id.selectSHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_SHFZR, bundle);
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
                case REQUEST_CODE_SELECT_BZ:
                    bzID=data.getIntExtra("personID",0);
                    mBzValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_JHFZR:
                    flfzrID=data.getIntExtra("personID",0);
                    mJhFhrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_ZJY:
                    zjyID=data.getIntExtra("personID",0);
                    mZjyValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_ZJLD:
                    zjldID=data.getIntExtra("personID",0);
                    mZjldValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_KG:
                    kgID=data.getIntExtra("personID",0);
                    mKgValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_SHFZR:
                    llfzrID=data.getIntExtra("personID",0);
                    mShFzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkDataIfCompleted() {
        String fhdwValue = mDepartmentValue.getText().toString();
        String bzValue = mBzValue.getText().toString();
        String jhfzrValue = mJhFhrValue.getText().toString();
        String zjyValue = mZjyValue.getText().toString();
        String zjldValue = mZjldValue.getText().toString();
        String kgValue = mKgValue.getText().toString();
        String shfzrValue = mShFzrValue.getText().toString();
        String remarkValue = mRemarkValue.getText().toString();
        if (TextUtils.isEmpty(fhdwValue)
                || "请选择发货部门".equals(fhdwValue)
                || "请选择班长".equals(bzValue)
                || "请选择交货负责人".equals(jhfzrValue)
                || "请选择质检员".equals(zjyValue)
                || "请选择质检领导".equals(zjldValue)
                || "请选择仓库管理员".equals(kgValue)
                || "请选择收货负责人".equals(shfzrValue)) {
            return false;
        }
        params.setJhDw(fhdwValue);
        params.setJhr(GlobalData.realName);
        params.setBzID(bzID);
        params.setBz(bzValue);
        params.setBzStatus(0);
        params.setFlfzrID(flfzrID);
        params.setJhFzr(jhfzrValue);
        params.setFlfzrStatus(0);
        params.setZjyID(zjyID);
        params.setZjy(zjyValue);
        params.setZjyStatus(0);
        params.setZjldID(zjldID);
        params.setZjld(zjldValue);
        params.setZjldStatus(0);
        params.setKgID(kgID);
        params.setKg(kgValue);
        params.setKgStatus(0);
        params.setLlfzrID(llfzrID);
        params.setShFzr(shfzrValue);
        params.setLlfzrStatus(0);
        params.setRemark(remarkValue);
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
