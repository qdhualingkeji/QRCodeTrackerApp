package com.hualing.qrcodetracker.activities.operation_bcp.bcp_return;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BCPTKDResult;
import com.hualing.qrcodetracker.bean.CreateBCPTKDParam;
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

public class BcpTKDInputActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 30;
    //private static final int REQUEST_CODE_SELECT_SLR = 31;
    private static final int REQUEST_CODE_SELECT_BZ = 32;
    private static final int REQUEST_CODE_SELECT_TLFZR = 33;
    private static final int REQUEST_CODE_SELECT_ZJY = 34;
    private static final int REQUEST_CODE_SELECT_ZJLD = 35;
    private static final int REQUEST_CODE_SELECT_KG = 36;
    private static final int REQUEST_CODE_SELECT_SLFZR = 37;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.thdwValue)
    TextView mThdwValue;
    //@BindView(R.id.shrValue)
    //TextView mShrValue;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.thfzrValue)
    TextView mThfzrValue;
    @BindView(R.id.zjyValue)
    TextView mZjyValue;
    @BindView(R.id.zjldValue)
    TextView mZjldValue;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.shfzrValue)
    TextView mShfzrValue;
    private int bzID;
    private int tlfzrID;
    private int zjyID;
    private int zjldID;
    private int kgID;
    private int slfzrID;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;

    private CreateBCPTKDParam params;
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
                AllActivitiesHolder.removeAct(BcpTKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
        params = new CreateBCPTKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_tkddata_input;
    }

    @OnClick({R.id.selectLLBM, R.id.selectBZ, R.id.selectTHFZR, R.id.selectZJY, R.id.selectZJLD, R.id.selectKG, R.id.selectSHFZR, R.id.commitBtn})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.selectLLBM:
                IntentUtil.openActivityForResult(this, SelectDepartmentActivity.class, REQUEST_CODE_SELECT_DEPARTMENT, null);
                break;
                /*
            case R.id.selectSHR:
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_SLR, null);
                break;
                */
            case R.id.selectBZ:
                bundle.putString("checkQX", "bz");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_BZ, bundle);
                break;
            case R.id.selectTHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_TLFZR, bundle);
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
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_SLFZR, bundle);
                break;
            case R.id.commitBtn:
                commitDataToWeb();
                break;
        }
    }

    private void commitDataToWeb() {
        if (!checkDataIfCompleted()) {
            Toast.makeText(this, "数据录入不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BCPTKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BCPTKDResult>> e) throws Exception {
                ActionResult<BCPTKDResult> nr = mainDao.createBCP_TKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BCPTKDResult>>() {
                    @Override
                    public void accept(ActionResult<BCPTKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "半成品入库（退库）单创建成功~", Toast.LENGTH_SHORT).show();
                            BCPTKDResult tkdResult = result.getResult();
                            //保存物料退库单号
                            SharedPreferenceUtil.setBCPTKDNumber(String.valueOf(tkdResult.getBackDh()));
                            //IntentUtil.openActivity(BcpTKDInputActivity.this, ScanActivity.class);
                            IntentUtil.openActivity(BcpTKDInputActivity.this, BcpTKDataInputActivity.class);
                            AllActivitiesHolder.removeAct(BcpTKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkDataIfCompleted() {
        String thdwValue = mThdwValue.getText().toString();
        //String shrValue = mShrValue.getText().toString();
        String bzValue = mBzValue.getText().toString();
        String thfzrValue = mThfzrValue.getText().toString();
        String zjyValue = mZjyValue.getText().toString();
        String zjldValue = mZjldValue.getText().toString();
        String kgValue = mKgValue.getText().toString();
        String shfzrValue = mShfzrValue.getText().toString();
        String remarkValue = mRemarkValue.getText().toString();
        if ("请选择部门".equals(thdwValue)
                //|| "请选择收货人".equals(shrValue)
                || "请选择班长".equals(bzValue)
                || "请选择交货负责人".equals(thfzrValue)
                || "请选择质检员".equals(zjyValue)
                || "请选择质检领导".equals(zjldValue)
                || "请选择库管".equals(kgValue)
                || "请选择收货负责人".equals(shfzrValue)
            //                || TextUtils.isEmpty(remarkValue)
                ) {
            return false;
        }
        params.setThDw(thdwValue);
        //params.setShrr(shrValue);
        params.setThr(GlobalData.realName);
        params.setBzID(bzID);
        params.setBz(bzValue);
        params.setBzStatus(0);
        params.setTlfzrID(tlfzrID);
        params.setThFzr(thfzrValue);
        params.setTlfzrStatus(0);
        params.setZjyID(zjyID);
        params.setZjy(zjyValue);
        params.setZjyStatus(0);
        params.setZjldID(zjldID);
        params.setZjld(zjldValue);
        params.setZjldStatus(0);
        params.setKgID(kgID);
        params.setKg(kgValue);
        params.setKgStatus(0);
        params.setSlfzrID(slfzrID);
        params.setShFzr(shfzrValue);
        params.setSlfzrStatus(0);
        params.setRemark(remarkValue);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_DEPARTMENT:
                    mThdwValue.setText(data.getStringExtra("groupName"));
                    break;
                    /*
                case REQUEST_CODE_SELECT_SLR:
                    mShrValue.setText(data.getStringExtra("personName"));
                    break;
                    */
                case REQUEST_CODE_SELECT_BZ:
                    bzID=data.getIntExtra("personID",0);
                    mBzValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_TLFZR:
                    tlfzrID=data.getIntExtra("personID",0);
                    mThfzrValue.setText(data.getStringExtra("personName"));
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
                case REQUEST_CODE_SELECT_SLFZR:
                    slfzrID=data.getIntExtra("personID",0);
                    mShfzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
