package com.hualing.qrcodetracker.activities.operation_wl.wl_out;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.EmployeeMainActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.WLOutGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLOutParam;
import com.hualing.qrcodetracker.bean.WLOutShowDataResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.NotificationType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MaterialOutDataInputActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 10;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.nameValue)
    TextView mNameValue;
    @BindView(R.id.cdValue)
    TextView mCdValue;
    @BindView(R.id.lbValue)
    TextView mLbValue;
    @BindView(R.id.ggValue)
    TextView mGgValue;
    @BindView(R.id.zzlValue)
    TextView mZzlValue;
    @BindView(R.id.sldwValue)
    TextView mSldwValue;
    @BindView(R.id.remainShlValue)
    TextView mRemainShlValue;
    @BindView(R.id.zhlValue)
    TextView mZhlValue;
    @BindView(R.id.needZhlValue)
    EditText mNeedZhlValue;
    //    @BindView(R.id.llbmValue)
    //    TextView mLlbmValue;

    private MainDao mainDao;

    //二维码解析出的Id
    private String mQrcodeId;
    private WLOutParam params;
    private WLOutGetShowDataParam getParam;

    private ListView popupView;
    private BaseAdapter mPopAdapter;
    private PopupWindow popupWindow;
    private int mSelectedSortId;


    @Override
    protected void initLogic() {

        mainDao = YoniClient.getInstance().create(MainDao.class);

        params = new WLOutParam();
        getParam = new WLOutGetShowDataParam();

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
                AllActivitiesHolder.removeAct(MaterialOutDataInputActivity.this);
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

        Observable.create(new ObservableOnSubscribe<ActionResult<WLOutShowDataResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WLOutShowDataResult>> e) throws Exception {
                ActionResult<WLOutShowDataResult> nr = mainDao.getWlOutShowData(getParam);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WLOutShowDataResult>>() {
                    @Override
                    public void accept(ActionResult<WLOutShowDataResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(MaterialOutDataInputActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(MaterialOutDataInputActivity.this);
                            return;
                        } else {
                            WLOutShowDataResult dataResult = result.getResult();
                            mNameValue.setText(dataResult.getProductName());
                            mCdValue.setText(dataResult.getChd());
                            mLbValue.setText(dataResult.getSortName());
                            String gg = dataResult.getGg();
                            if(TextUtils.isEmpty(gg))
                                gg=getString(R.string.no_gg);
                            mGgValue.setText(gg);
                            mZzlValue.setText(dataResult.getPczl() + "");
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
        return R.layout.activity_material_out_data_input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private boolean checkIfInfoPerfect() {
        float dwzl = Float.parseFloat(mZhlValue.getText().toString());
        String value = mNeedZhlValue.getText().toString();
        //        String llbm = mLlbmValue.getText().toString();
        float remainShL = Float.parseFloat(mRemainShlValue.getText().toString());
        if (TextUtils.isEmpty(value)
            //                || "请选择领料部门".equals(llbm)
                ) {
            Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
            return false;
        }
        float ckZhl = Float.parseFloat(value);
        if(ckZhl==0){
            Toast.makeText(this, "出库重量不能为0", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (ckZhl > dwzl) {
            Toast.makeText(this, "出库重量不得大于单位重量", Toast.LENGTH_SHORT).show();
            return false;
        }

        params.setQrCodeId(mQrcodeId);
        DecimalFormat df = new DecimalFormat("0.00");
        //params.setCkShL(Float.parseFloat(df.format(remainShL - (dwzl - Float.parseFloat(value)) / dwzl)));
        params.setCkShL(Float.parseFloat(value)*remainShL/dwzl);
        params.setDwzl(Float.parseFloat(value));
        //        params.setLlbm(llbm);
        params.setOutDh(SharedPreferenceUtil.getWlCKDNumber());

        return true;
    }

    private void commitDataToWeb() {

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.wlOut(params);
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
                            new AlertDialog.Builder(MaterialOutDataInputActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("是否继续扫码录入出库数据？")
                                    .setPositiveButton("继续扫码录入", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(MaterialOutDataInputActivity.this, ScanActivity.class);
                                            AllActivitiesHolder.removeAct(MaterialOutDataInputActivity.this);
                                        }
                                    })
                                    .setNegativeButton("已录入完毕", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //调接口发推送审核
                                            sendNotification();

                                        }
                                    })
                                    .show();
                        }
                    }
                });

    }

    private void sendNotification() {

        final NotificationParam notificationParam = new NotificationParam();
        //根据单号去查找审核人
        String dh = SharedPreferenceUtil.getWlCKDNumber();
        notificationParam.setDh(dh);
        notificationParam.setStyle(NotificationType.WL_CKD);
        notificationParam.setPersonFlag(NotificationParam.KG);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.sendNotification(notificationParam);
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
                            Toast.makeText(TheApplication.getContext(), "已通知仓库管理员审核", Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(MaterialOutDataInputActivity.this, EmployeeMainActivity.class);
                            AllActivitiesHolder.removeAct(MaterialOutDataInputActivity.this);
                        }
                    }
                });

    }

    @OnClick(R.id.commitBtn)
    public void onViewClicked() {
        //数据录入是否完整
        if (checkIfInfoPerfect()) {
            commitDataToWeb();
        }
    }
}
