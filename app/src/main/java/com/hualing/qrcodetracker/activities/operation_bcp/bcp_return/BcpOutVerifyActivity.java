package com.hualing.qrcodetracker.activities.operation_bcp.bcp_return;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.WlInVerifyActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpOutShowBean;
import com.hualing.qrcodetracker.bean.BcpOutVerifyResult;
import com.hualing.qrcodetracker.bean.CpOutShowBean;
import com.hualing.qrcodetracker.bean.CpOutVerifyResult;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.NotificationType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.widget.MyListView;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.hualing.qrcodetracker.activities.main.NonHandleMsgActivity.RETURN_AND_REFRESH;

public class BcpOutVerifyActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.outdhValue)
    TextView mOutdhValue;
    @BindView(R.id.lhrqValue)
    TextView mLhrqValue;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.remarkValue)
    TextView mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;
    @BindView(R.id.bzLayout)
    LinearLayout mBzLayout;
    @BindView(R.id.bzView)
    View mBzView;

    private MainDao mainDao;
    private MyBcpAdapter mBcpAdapter;
    private List<BcpOutShowBean> mBcpData;
    private MyCpAdapter mCpAdapter;
    private List<CpOutShowBean> mCpData;
    private String mDh;
    private String mName;
    private VerifyParam param;
    private boolean isKG=false;
    private boolean isBZ=false;
    private boolean isFZR=false;

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
                AllActivitiesHolder.removeAct(BcpOutVerifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new VerifyParam();
        Intent intent = getIntent();
        if (intent != null) {
            Log.e("checkQX==========",""+GlobalData.checkQXGroup);
            String[] checkQXArr = GlobalData.checkQXGroup.split(",");
            for (String checkQX:checkQXArr) {
                if("kg".equals(checkQX)){
                    isKG=true;
                    break;
                }
                else if("fzr".equals(checkQX)){
                    isFZR=true;
                    break;
                }
            }
            mName = intent.getStringExtra("name");
            if(isKG) {
                param.setCheckQXFlag(VerifyParam.KG);
            }
            else if(isBZ) {
                if("半成品出库单".equals(mName)){
                    param.setCheckQXFlag(VerifyParam.BCPBZ);
                }
                else{
                    param.setCheckQXFlag(VerifyParam.CPBZ);
                }
            }
            else if(isFZR) {
                param.setCheckQXFlag(VerifyParam.FZR);
            }

            mDh = getIntent().getStringExtra("dh");
            param.setDh(mDh);
            if("半成品出库单".equals(mName)){
                mTitle.setTitle("半成品出库审核");
                if(param.getCheckQXFlag()==VerifyParam.KG||param.getCheckQXFlag()==VerifyParam.BCPBZ||param.getCheckQXFlag()==VerifyParam.FZR){
                    mBzLayout.setVisibility(LinearLayout.VISIBLE);
                    mBzView.setVisibility(View.VISIBLE);
                }
            }
            param.setName(mName);
        }

        if("半成品出库单".equals(mName)) {
            mBcpData = new ArrayList<>();
            mBcpAdapter = new MyBcpAdapter();
            mChildDataList.setAdapter(mBcpAdapter);
        }
        else{
            mCpData = new ArrayList<>();
            mCpAdapter = new MyCpAdapter();
            mChildDataList.setAdapter(mCpAdapter);
        }
        mChildDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        if("半成品出库单".equals(mName)){
            Observable.create(new ObservableOnSubscribe<ActionResult<BcpOutVerifyResult>>() {
                @Override
                public void subscribe(ObservableEmitter<ActionResult<BcpOutVerifyResult>> e) throws Exception {
                    ActionResult<BcpOutVerifyResult> nr = mainDao.getBcpOutVerifyData(param);
                    e.onNext(nr);
                }
            }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                    .subscribe(new Consumer<ActionResult<BcpOutVerifyResult>>() {
                        @Override
                        public void accept(ActionResult<BcpOutVerifyResult> result) throws Exception {
                            progressDialog.dismiss();
                            if (result.getCode() != 0) {
                                Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                BcpOutVerifyResult dataResult = result.getResult();
                                mOutdhValue.setText(dataResult.getOutDh());
                                mLhrqValue.setText(dataResult.getLhRq());
                                mKgValue.setText(dataResult.getKg());
                                mBzValue.setText(dataResult.getBz());
                                mRemarkValue.setText(TextUtils.isEmpty(dataResult.getRemark()) ? "无备注信息" : dataResult.getRemark());

                                if (dataResult.getBeans() != null && dataResult.getBeans().size() > 0) {
                                    mBcpData.clear();
                                    mBcpData.addAll(dataResult.getBeans());
                                    mBcpAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
        else {
            Observable.create(new ObservableOnSubscribe<ActionResult<CpOutVerifyResult>>() {
                @Override
                public void subscribe(ObservableEmitter<ActionResult<CpOutVerifyResult>> e) throws Exception {
                    ActionResult<CpOutVerifyResult> nr = mainDao.getCpOutVerifyData(param);
                    e.onNext(nr);
                }
            }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                    .subscribe(new Consumer<ActionResult<CpOutVerifyResult>>() {
                        @Override
                        public void accept(ActionResult<CpOutVerifyResult> result) throws Exception {
                            progressDialog.dismiss();
                            if (result.getCode() != 0) {
                                Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                CpOutVerifyResult dataResult = result.getResult();
                                mOutdhValue.setText(dataResult.getOutDh());
                                mLhrqValue.setText(dataResult.getLhRq());
                                mKgValue.setText(dataResult.getKg());
                                mRemarkValue.setText(TextUtils.isEmpty(dataResult.getRemark()) ? "无备注信息" : dataResult.getRemark());

                                if (dataResult.getBeans() != null && dataResult.getBeans().size() > 0) {
                                    mCpData.clear();
                                    mCpData.addAll(dataResult.getBeans());
                                    mCpAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    protected void debugShow() {

    }

    @OnClick({R.id.agreeBtn, R.id.refuseBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.agreeBtn:
                toAgree();
                break;
            case R.id.refuseBtn:
                toRefuse();
                break;
        }
    }

    private void toRefuse() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = null;
                if("半成品出库单".equals(mName)) {
                    nr = mainDao.toRefuseBcpOut(param);
                }
                else{
                    nr = mainDao.toRefuseCpOut(param);
                }
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
                            Toast.makeText(TheApplication.getContext(), "核退成功", Toast.LENGTH_SHORT).show();
                            setResult(RETURN_AND_REFRESH);
                            AllActivitiesHolder.removeAct(BcpOutVerifyActivity.this);
                            return;
                        }
                    }
                });
    }

    private void toAgree() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = null;
                if("半成品出库单".equals(mName)) {
                    nr = mainDao.toAgreeBcpOut(param);
                }
                else{
                    nr = mainDao.toAgreeCpOut(param);
                }
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
                            Toast.makeText(TheApplication.getContext(), "审核已通过", Toast.LENGTH_SHORT).show();
                            if("半成品出库单".equals(mName)){
                                if(param.getCheckQXFlag() == VerifyParam.KG||param.getCheckQXFlag() == VerifyParam.FLFZR||param.getCheckQXFlag() == VerifyParam.BCPBZ) {
                                    sendNotification(param.getCheckQXFlag());
                                }
                                else {
                                    setResult(RETURN_AND_REFRESH);
                                    AllActivitiesHolder.removeAct(BcpOutVerifyActivity.this);
                                }
                            }
                            else{
                                if(param.getCheckQXFlag() == VerifyParam.KG) {
                                    sendNotification(param.getCheckQXFlag());
                                }
                                else {
                                    setResult(RETURN_AND_REFRESH);
                                    AllActivitiesHolder.removeAct(BcpOutVerifyActivity.this);
                                }
                            }
                            return;
                        }
                    }
                });
    }


    private void sendNotification(Integer checkQXFlag) {

        final NotificationParam notificationParam = new NotificationParam();
        //根据单号去查找审核人
        notificationParam.setDh(param.getDh());
        int personFlag=-1;
        String notifText=null;
        if("半成品出库单".equals(mName)) {
            notificationParam.setStyle(NotificationType.BCP_CKD);
            if (checkQXFlag == VerifyParam.KG) {
                personFlag = NotificationParam.FLFZR;
                notifText = "已通知发料负责人审核";
            }
            else if (checkQXFlag == VerifyParam.FLFZR) {
                personFlag = NotificationParam.BZ;
                notifText = "已通知班长审核";
            }
        }
        else{
            notificationParam.setStyle(NotificationType.CP_CKD);
            if (checkQXFlag == VerifyParam.KG) {
                personFlag = NotificationParam.FZR;
                notifText = "已通知交货负责人审核";
            }
        }
        notificationParam.setPersonFlag(personFlag);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        final String finalNotifText = notifText;
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
                            Toast.makeText(TheApplication.getContext(), finalNotifText, Toast.LENGTH_SHORT).show();
                        }
                        setResult(RETURN_AND_REFRESH);
                        AllActivitiesHolder.removeAct(BcpOutVerifyActivity.this);
                    }
                });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_out_verify;
    }

    class MyBcpAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBcpData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(BcpOutVerifyActivity.this, R.layout.item_bcpout_verify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            BcpOutShowBean bean = mBcpData.get(position);
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mLbValue.setText(bean.getSortName());
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mRkzlValue.setText(bean.getrKZL() + "");
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.nameValue)
            TextView mNameValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.rkzlValue)
            TextView mRkzlValue;
            @BindView(R.id.dwzlValue)
            TextView mDwzlValue;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    class MyCpAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCpData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(BcpOutVerifyActivity.this, R.layout.item_cpout_verify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            CpOutShowBean bean = mCpData.get(position);
            //viewHolder.mWlbmValue.setText(bean.getCpCode());
            viewHolder.mNameValue.setText(bean.getCpName());
            viewHolder.mLbValue.setText(bean.getSortName());
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mScpcValue.setText(bean.getsCPC());
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");

            return convertView;
        }

        class ViewHolder {
            //@BindView(R.id.wlbmValue)
            //TextView mWlbmValue;
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.nameValue)
            TextView mNameValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.scpcValue)
            TextView mScpcValue;
            @BindView(R.id.dwzlValue)
            TextView mDwzlValue;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
