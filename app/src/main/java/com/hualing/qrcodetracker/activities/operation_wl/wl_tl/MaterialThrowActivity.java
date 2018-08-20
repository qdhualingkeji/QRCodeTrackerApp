package com.hualing.qrcodetracker.activities.operation_wl.wl_tl;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.EmployeeMainActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectCJActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectGXActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.WLThrowGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLThrowParam;
import com.hualing.qrcodetracker.bean.WLThrowShowDataResult;
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

public class MaterialThrowActivity extends BaseActivity {

    private static final int GET_GX_CODE = 11;
    private static final int GET_CJ_CODE = 10;
    private static final int NON_SELECT = -1;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.nameValue)
    TextView mNameValue;
    @BindView(R.id.cdValue)
    TextView mCdValue;
    //@BindView(R.id.lbValue)
    //TextView mLbValue;
    @BindView(R.id.ggValue)
    TextView mGgValue;
    @BindView(R.id.sldwValue)
    TextView mSldwValue;
    @BindView(R.id.remainShlValue)
    TextView mRemainShlValue;
    @BindView(R.id.zhlValue)
    TextView mZhlValue;
    @BindView(R.id.tlShlValue)
    EditText mTlShlValue;
    @BindView(R.id.gxValue)
    TextView mGxValue;
    @BindView(R.id.cjValue)
    TextView mCjValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;

    private MainDao mainDao;

    //二维码解析出的Id
    private String mQrcodeId;
    private WLThrowParam params;
    private WLThrowGetShowDataParam getParam;
    private Dialog progressDialog;
    //车间id
    private int mSelectedCJId = NON_SELECT;
    //车间包含的工序id
    private String mCJHasGongXuId ;
    //工序id
    private int mSelectedGxId = NON_SELECT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

        mainDao = YoniClient.getInstance().create(MainDao.class);
        progressDialog = TheApplication.createLoadingDialog(this, "");

        params = new WLThrowParam();
        getParam = new WLThrowGetShowDataParam();

        /*
        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
        }
        */
        mQrcodeId = SharedPreferenceUtil.getQrCodeId();
        getParam.setQrcodeId(mQrcodeId);

        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(MaterialThrowActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
    }

    @Override
    protected void getDataFormWeb() {
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<WLThrowShowDataResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WLThrowShowDataResult>> e) throws Exception {
                ActionResult<WLThrowShowDataResult> nr = mainDao.getWlThrowShowData(getParam);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WLThrowShowDataResult>>() {
                    @Override
                    public void accept(ActionResult<WLThrowShowDataResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(MaterialThrowActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(MaterialThrowActivity.this);
                            return;
                        } else {
                            WLThrowShowDataResult dataResult = result.getResult();
                            mNameValue.setText(dataResult.getProductName());
                            mCdValue.setText(dataResult.getChd());
                            //mLbValue.setText(dataResult.getSortName());
                            mGgValue.setText(dataResult.getGg());
                            mSldwValue.setText(dataResult.getDw());
                            mRemainShlValue.setText(dataResult.getShl() + "");
                            mZhlValue.setText(dataResult.getDwzl() + "");
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_material_throw;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_GX_CODE:
                    String s1 = data.getStringExtra("gxName");
                    mGxValue.setText(s1);
                    mSelectedGxId = data.getIntExtra("gxId", NON_SELECT);
                    break;
                case GET_CJ_CODE:
                    String s2 = data.getStringExtra("cjName");
                    mCjValue.setText(s2);
                    mSelectedCJId = data.getIntExtra("cjId", NON_SELECT);
                    mCJHasGongXuId = data.getStringExtra("cjGXIds");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.selectGX, R.id.commitBtn, R.id.selectCJ})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.selectCJ:
                IntentUtil.openActivityForResult(this, SelectCJActivity.class, GET_CJ_CODE, null);
                break;
            case R.id.selectGX:
                if (mSelectedCJId==NON_SELECT) {
                    Toast.makeText(this, "请先选择车间", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("cjGXIds",mCJHasGongXuId);
                IntentUtil.openActivityForResult(this, SelectGXActivity.class, GET_GX_CODE, bundle);
                break;
            case R.id.commitBtn:
                //数据录入是否完整
                if (checkIfInfoPerfect()) {
                    commitDataToWeb();
                }
                break;
        }
    }

    private boolean checkIfInfoPerfect() {
        String value = mTlShlValue.getText().toString();
        //        String llbm = mLlbmValue.getText().toString();
        if (TextUtils.isEmpty(value)
                || (mSelectedGxId == NON_SELECT)
                || (mSelectedCJId == NON_SELECT)
                ) {
            Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
            return false;
        }
        float remainShL = Float.parseFloat(mRemainShlValue.getText().toString());
        float ckShL = Float.parseFloat(value);
        if (ckShL > remainShL) {
            Toast.makeText(this, "投料数量不得大于剩余数量", Toast.LENGTH_SHORT).show();
            return false;
        }

        params.setQrcodeId(mQrcodeId);
        params.setTlShl(Float.parseFloat(value));
        params.setCjId(mSelectedCJId);
        params.setCj(mCjValue.getText().toString());
        params.setGxId(mSelectedGxId);
        params.setGx(mGxValue.getText().toString());
        params.setCzy(GlobalData.realName);
        params.setRemark(mRemarkValue.getText().toString());

        return true;
    }

    private void commitDataToWeb() {

        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.wlThrow(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<ActionResult>>() {
                    @Override
                    public void accept(ActionResult<ActionResult> result) throws Exception {
                        progressDialog.dismiss();
                        Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        if (result.getCode() != 0) {
                        } else {
                            new AlertDialog.Builder(MaterialThrowActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("是否继续扫码录入投料数据？")
                                    .setPositiveButton("继续扫码录入", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(MaterialThrowActivity.this, ScanActivity.class);
                                            AllActivitiesHolder.removeAct(MaterialThrowActivity.this);
                                        }
                                    })
                                    .setNegativeButton("已录入完毕", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(MaterialThrowActivity.this, EmployeeMainActivity.class);
                                            AllActivitiesHolder.removeAct(MaterialThrowActivity.this);

                                            //这里调接口发推送

                                        }
                                    })
                                    .show();
                        }
                    }
                });

    }

}
