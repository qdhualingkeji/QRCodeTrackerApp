package com.hualing.qrcodetracker.activities.operation_bcp.bcp_out;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BCPCKDResult;
import com.hualing.qrcodetracker.bean.CreateBCPCKDParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
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

public class BCPCKDInputActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 30;
    private static final int REQUEST_CODE_SELECT_KG = 31;
    private static final int REQUEST_CODE_SELECT_FLFZR = 32;
    private static final int REQUEST_CODE_SELECT_BZ = 33;
    private static final int REQUEST_CODE_SELECT_LLFZR = 34;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.LldwValue)
    TextView mLldwValue;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.FlfzrValue)
    TextView mFlfzrValue;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.LlfzrValue)
    TextView mLlfzrValue;
    private int kgID;
    private int flfzrID;
    private int bzID;
    private int llfzrID;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    private CreateBCPCKDParam params;
    private MainDao mainDao;

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
                AllActivitiesHolder.removeAct(BCPCKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
        params = new CreateBCPCKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_ckd_input;
    }

    private void commitDataToWeb() {
        if (!checkDataIfCompleted()) {
            Toast.makeText(this, "数据录入不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BCPCKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BCPCKDResult>> e) throws Exception {
                ActionResult<BCPCKDResult> nr = mainDao.createBCP_CKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BCPCKDResult>>() {
                    @Override
                    public void accept(ActionResult<BCPCKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "出库单创建成功~", Toast.LENGTH_SHORT).show();
                            //保存物料出库单号
                            //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                            BCPCKDResult ckdResult = result.getResult();
                            SharedPreferenceUtil.setBCPCKDNumber(String.valueOf(ckdResult.getOutDh()));
                            //IntentUtil.openActivity(WLCKDInputActivity.this, ScanActivity.class);
                            IntentUtil.openActivity(BCPCKDInputActivity.this, BCPOutDataInputActivity.class);
                            AllActivitiesHolder.removeAct(BCPCKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkDataIfCompleted() {
        String lldwValue = mLldwValue.getText().toString();
        String kgValue = mKgValue.getText().toString();
        String flfzrValue = mFlfzrValue.getText().toString();
        String bzValue = mBzValue.getText().toString();
        String llfzrValue = mLlfzrValue.getText().toString();
        String remarkValue = mRemarkValue.getText().toString();
        if ("请选择领料部门".equals(lldwValue)
                || "请选择库管".equals(kgValue)
                || "请选择发料负责人".equals(flfzrValue)
                || "请选择班长".equals(bzValue)
                || "请选择领料负责人".equals(llfzrValue)
            //                || TextUtils.isEmpty(remarkValue)
                ) {
            return false;
        }
        params.setLhDw(lldwValue);
        params.setFhr(GlobalData.realName);
        params.setLhr(GlobalData.realName);
        params.setKgID(kgID);
        params.setKg(kgValue);
        params.setKgStatus(0);
        params.setFzrID(flfzrID);
        params.setFhFzr(flfzrValue);
        params.setFzrStatus(0);
        params.setBzID(bzID);
        params.setBz(bzValue);
        params.setBzStatus(0);
        params.setLlfzrID(llfzrID);
        params.setLhFzr(llfzrValue);
        params.setLlfzrStatus(0);
        params.setRemark(remarkValue);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_DEPARTMENT:
                    mLldwValue.setText(data.getStringExtra("groupName"));
                    break;
                case REQUEST_CODE_SELECT_KG:
                    kgID=data.getIntExtra("personID",0);
                    mKgValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_FLFZR:
                    flfzrID=data.getIntExtra("personID",0);
                    mFlfzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_BZ:
                    bzID=data.getIntExtra("personID",0);
                    mBzValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_LLFZR:
                    llfzrID=data.getIntExtra("personID",0);
                    mLlfzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.selectLLBM, R.id.commitBtn, R.id.selectKG, R.id.selectFLFZR, R.id.selectBZ, R.id.selectLLFZR})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.selectLLBM:
                IntentUtil.openActivityForResult(this, SelectDepartmentActivity.class, REQUEST_CODE_SELECT_DEPARTMENT, null);
                break;
            case R.id.selectKG:
                bundle.putString("checkQX", "kg");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_KG, bundle);
                break;
            case R.id.selectFLFZR:
                bundle.putString("checkQX", "fzr");//这个是发料负责人标识
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_FLFZR, bundle);
                break;
            case R.id.selectBZ:
                bundle.putString("checkQX", "bz");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_BZ, bundle);
                break;
            case R.id.selectLLFZR:
                bundle.putString("checkQX", "fzr");//这个是领料负责人标识
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_LLFZR, bundle);
                break;
            case R.id.commitBtn:
                commitDataToWeb();
                break;
        }
    }
}
