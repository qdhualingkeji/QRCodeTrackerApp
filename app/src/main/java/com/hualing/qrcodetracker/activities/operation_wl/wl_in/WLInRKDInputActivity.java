package com.hualing.qrcodetracker.activities.operation_wl.wl_in;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;
import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.EmployeeMainActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.CreateWLRKDParam;
import com.hualing.qrcodetracker.bean.UserGroupBean;
import com.hualing.qrcodetracker.bean.WLRKDResult;
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

/**
 * @desc 物料入库单数据录入
 */
public class WLInRKDInputActivity extends BaseActivity {

    private static final int SELECT_PERSON1 = 111;
    private static final int SELECT_ZJLD = 112;
    private static final int SELECT_PERSON = 113;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.FhDwValue)
    EditText mFhDwValue;
    //    @BindView(R.id.ShRqValue)
    //    EditText mShRqValue;
    @BindView(R.id.ShRqValue)
    TextView mShRqValue;
    @BindView(R.id.ShSjValue)
    TextView mShSjValue;
    //    @BindView(R.id.InDhValue)
    //    EditText mInDhValue;
//    @BindView(R.id.ShrValue)
//    EditText mShrValue;
    @BindView(R.id.zjyValue)
    TextView mZjyValue;
    @BindView(R.id.zjldValue)
    TextView mZjldValue;
    @BindView(R.id.ShFzrValue)
    TextView mShFzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    private int zjyID;
    private int zjldID;
    private int fzrID;
    private MainDao mainDao;

    private CreateWLRKDParam params;

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
                AllActivitiesHolder.removeAct(WLInRKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        params = new CreateWLRKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wlin_rkdinput;
    }

    private boolean checkDataIfCompleted() {
        String fhdwValue = mFhDwValue.getText().toString();
        String shrqValue = mShRqValue.getText().toString();
        String shsjValue = mShSjValue.getText().toString();
        String zjyValue = mZjyValue.getText().toString();
        String zjldValue = mZjldValue.getText().toString();
        String shfzrValue = mShFzrValue.getText().toString();
        String remarkValue = mRemarkValue.getText().toString();
        if (TextUtils.isEmpty(fhdwValue)
                || "请选择收货日期".equals(shrqValue)
                || "请选择收货时间".equals(shsjValue)
                || "请选择质检员".equals(zjyValue)
                || "请选择质检领导".equals(zjldValue)
                || "请选择仓库负责人".equals(shfzrValue)) {
            return false;
        }
        params.setFhDw(fhdwValue);
        params.setShRq(shrqValue+" "+shsjValue);
        //收货人改为当前用户（操作者是仓库管理员）
        params.setShr(GlobalData.realName);
        params.setZjyID(zjyID);
        params.setZjy(zjyValue);
        params.setZjyStatus(0);
        params.setZjldID(zjldID);
        params.setZjld(zjldValue);
        params.setZjldStatus(0);
        params.setFzrID(fzrID);
        params.setShFzr(shfzrValue);
        params.setFzrStatus(0);
        params.setRemark(remarkValue);
        return true;
    }

    @OnClick({R.id.ShRqValue, R.id.ShSjValue,R.id.selectPerson1,R.id.selectZjld,R.id.selectPerson, R.id.commitBtn})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ShRqValue:

                View v = LayoutInflater.from(this).inflate(R.layout.date_select, null);
                final DatePicker datePicker = v.findViewById(R.id.datePicker);
                String lastDate = mShRqValue.getText().toString();
                String[] sa = null;
                if (!"请选择收货日期".equals(lastDate)) {
                    sa = lastDate.split("-");
                }
                if (sa != null) {
                    datePicker.updateDate(Integer.parseInt(sa[0]), Integer.parseInt(sa[1]) - 1, Integer.parseInt(sa[2]));
                }
                new AlertDialog.Builder(this).setView(v)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dateStr = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                                mShRqValue.setText(dateStr);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
            case R.id.ShSjValue:

                View v1 = LayoutInflater.from(this).inflate(R.layout.time_select, null);
                final TimePicker timePicker = v1.findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true);
                new AlertDialog.Builder(this).setView(v1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dateStr = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                                mShSjValue.setText(dateStr);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
            case R.id.selectPerson1:
                bundle.putString("checkQX", "zjy");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, SELECT_PERSON1, bundle);
                break;
            case R.id.selectZjld:
                bundle.putString("checkQX", "zjld");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, SELECT_ZJLD, bundle);
                break;
            case R.id.selectPerson:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, SELECT_PERSON, bundle);
                break;
            case R.id.commitBtn:
                commitDataToWeb();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            int personID = data.getIntExtra("personID",0);
            String personName = data.getStringExtra("personName");
            switch (requestCode) {
                case SELECT_PERSON1:
                    zjyID =personID;
                    mZjyValue.setText(personName);
                    break;
                case SELECT_ZJLD:
                    zjldID =personID;
                    mZjldValue.setText(personName);
                    break;
                case SELECT_PERSON:
                    fzrID =personID;
                    mShFzrValue.setText(personName);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void commitDataToWeb() {
        if (!checkDataIfCompleted()) {
            Toast.makeText(this, "数据录入不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<WLRKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WLRKDResult>> e) throws Exception {
                ActionResult<WLRKDResult> nr = mainDao.createWL_RKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WLRKDResult>>() {
                    @Override
                    public void accept(ActionResult<WLRKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "入库单创建成功~", Toast.LENGTH_SHORT).show();
                            //保存物料入库单号
                            //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                            WLRKDResult rkdResult = result.getResult();
                            SharedPreferenceUtil.setWlRKDNumber(String.valueOf(rkdResult.getInDh()));
                            //IntentUtil.openActivity(WLInRKDInputActivity.this, ScanActivity.class);
                            IntentUtil.openActivity(WLInRKDInputActivity.this, MaterialInDataInputActivity.class);
                            AllActivitiesHolder.removeAct(WLInRKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteRKD() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult> e) throws Exception {
                ActionResult nr = mainDao.createWL_RKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult>() {
                    @Override
                    public void accept(ActionResult result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), "入库单删除成功~", Toast.LENGTH_SHORT).show();
                            //保存物料入库单号
                            SharedPreferenceUtil.setWlRKDNumber(null);
                            IntentUtil.openActivity(WLInRKDInputActivity.this, EmployeeMainActivity.class);
                            AllActivitiesHolder.removeAct(WLInRKDInputActivity.this);
                        }
                    }
                });
    }
}
