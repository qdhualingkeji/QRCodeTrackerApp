package com.hualing.qrcodetracker.activities.operation_bcp.bcp_in;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.WlInVerifyActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpInShowBean;
import com.hualing.qrcodetracker.bean.BcpInVerifyResult;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.NotificationType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
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

public class BcpInVerifyActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.indhValue)
    TextView mIndhValue;
    @BindView(R.id.jhdwValue)
    TextView mJhdwValue;
    @BindView(R.id.shrqValue)
    TextView mShrqValue;
    @BindView(R.id.shfzrValue)
    TextView mShfzrValue;
    @BindView(R.id.jhRValue)
    TextView mJhRValue;
    @BindView(R.id.jhfzrValue)
    TextView mJhfzrValue;
    @BindView(R.id.remarkValue)
    TextView mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;
    @BindView(R.id.shfzrLayout)
    LinearLayout mShfzrLayout;
    @BindView(R.id.shfzrView)
    View mShfzrView;
    @BindView(R.id.jhfzrLayout)
    LinearLayout mJhfzrLayout;
    @BindView(R.id.jhfzrView)
    View mJhfzrView;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<BcpInShowBean> mData;
    private String mDh;
    private String mName;
    private VerifyParam param;
    private boolean isKG=false;
    private boolean isBZ=false;
    private boolean isFZR=false;
    private boolean isZJY=false;
    private boolean isZJLD=false;

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
                AllActivitiesHolder.removeAct(BcpInVerifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new VerifyParam();
        Intent intent = getIntent();
        if (intent != null) {
            String[] checkQXArr = GlobalData.checkQXGroup.split(",");
            for (String checkQX:checkQXArr) {
                if("kg".equals(checkQX)){
                    isKG=true;
                    break;
                }
                else if("bz".equals(checkQX)){
                    isBZ=true;
                    break;
                }
                else if("fzr".equals(checkQX)){
                    isFZR=true;
                    break;
                }
                else if("zjy".equals(checkQX)){
                    isZJY=true;
                    break;
                }
                else if("zjld".equals(checkQX)){
                    isZJLD=true;
                    break;
                }
            }
            mName = intent.getStringExtra("name");
            if(isKG) {
                param.setCheckQXFlag(VerifyParam.KG);
            }
            else if(isBZ) {
                if("半成品录入单".equals(mName)){
                    param.setCheckQXFlag(VerifyParam.BCPBZ);
                }
                else{
                    param.setCheckQXFlag(VerifyParam.CPBZ);
                }
            }
            else if(isFZR) {
                int personFlag = getIntent().getIntExtra("personFlag", -1);
                if(personFlag==NotificationParam.FLFZR)
                    param.setCheckQXFlag(VerifyParam.FLFZR);
                else if(personFlag==NotificationParam.LLFZR)
                    param.setCheckQXFlag(VerifyParam.LLFZR);
            }
            else if(isZJY)
                param.setCheckQXFlag(VerifyParam.ZJY);
            else if(isZJLD)
                param.setCheckQXFlag(VerifyParam.ZJLD);

            mDh = intent.getStringExtra("dh");
            param.setDh(mDh);
            if("半成品录入单".equals(mName)){
                mTitle.setTitle("半成品录入审核");
                mShfzrLayout.setVisibility(LinearLayout.GONE);
                mShfzrView.setVisibility(View.GONE);
                mJhfzrLayout.setVisibility(LinearLayout.GONE);
                mJhfzrView.setVisibility(View.GONE);
            }
            else{
                mTitle.setTitle("成品入库审核");
                mShfzrLayout.setVisibility(LinearLayout.VISIBLE);
                mShfzrView.setVisibility(View.VISIBLE);
                mJhfzrLayout.setVisibility(LinearLayout.VISIBLE);
                mJhfzrView.setVisibility(View.VISIBLE);
            }
            param.setName(mName);
        }

        mData = new ArrayList<>();
        mAdapter = new MyAdapter();
        mChildDataList.setAdapter(mAdapter);
        mChildDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BcpInVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BcpInVerifyResult>> e) throws Exception {
                ActionResult<BcpInVerifyResult> nr = mainDao.getBcpInVerifyData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BcpInVerifyResult>>() {
                    @Override
                    public void accept(ActionResult<BcpInVerifyResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            BcpInVerifyResult dataResult = result.getResult();
                            mIndhValue.setText(dataResult.getInDh());
                            mJhdwValue.setText(dataResult.getJhDw());
                            mShrqValue.setText(dataResult.getShRq());
                            mShfzrValue.setText(dataResult.getShFzr());
                            mJhfzrValue.setText(dataResult.getJhFzr());
                            mJhRValue.setText(dataResult.getJhR());
                            mRemarkValue.setText(TextUtils.isEmpty(dataResult.getRemark())?"无备注信息":dataResult.getRemark());

                            if (dataResult.getBeans() != null && dataResult.getBeans().size() > 0) {
                                mData.clear();
                                mData.addAll(dataResult.getBeans());
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
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
                ActionResult<ActionResult> nr = mainDao.toRefuseBcpIn(param);
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
                            AllActivitiesHolder.removeAct(BcpInVerifyActivity.this);
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
                ActionResult<ActionResult> nr = mainDao.toAgreeBcpIn(param);
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
                            if("半成品录入单".equals(mName)) {//如果是半成品录入单，需要给以下这几种身份的人推送通知
                                if (param.getCheckQXFlag() == VerifyParam.BCPBZ || param.getCheckQXFlag() == VerifyParam.FZR)//如果登录者是班长的话，说明还得推送给领料负责人;如果登录者是发料负责人的话，说明还得推送给质检员;
                                    sendNotification(param.getCheckQXFlag());
                                else {//不是的话，说明登录者就是质检领导，最后一道审核就不必再推送了
                                    setResult(RETURN_AND_REFRESH);
                                    AllActivitiesHolder.removeAct(BcpInVerifyActivity.this);
                                }
                            }
                            else{//如果是成品入库单，需要给以下这几种身份的人推送通知
                                if(param.getCheckQXFlag() == VerifyParam.CPBZ || param.getCheckQXFlag() == VerifyParam.FLFZR|| param.getCheckQXFlag() == VerifyParam.KG)
                                    sendNotification(param.getCheckQXFlag());
                                else {//不是的话，说明登录者就是质检领导，最后一道审核就不必再推送了
                                    setResult(RETURN_AND_REFRESH);
                                    AllActivitiesHolder.removeAct(BcpInVerifyActivity.this);
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
        if("半成品录入单".equals(mName)) {//如果是半成品录入单，需要给以下这几种身份的人推送通知
            notificationParam.setStyle(NotificationType.BCP_RKD);
            if (checkQXFlag == VerifyParam.BCPBZ) {
                personFlag = NotificationParam.FZR;
                notifText = "已通知车间领导审核";
            }
            else if (checkQXFlag == VerifyParam.FZR) {
                personFlag = NotificationParam.ZJY;
                notifText = "已通知质检员质检";
            }
        }
        else{//如果是成品入库单，需要给以下这几种身份的人推送通知
            notificationParam.setStyle(NotificationType.CP_RKD);
            if (checkQXFlag == VerifyParam.CPBZ) {
                personFlag = NotificationParam.FLFZR;
                notifText = "已通知交货负责人审核";
            }
            else if (checkQXFlag == VerifyParam.FLFZR) {
                personFlag = NotificationParam.ZJY;
                notifText = "已通知质检员质检";
            }
            else if (checkQXFlag == VerifyParam.KG) {
                personFlag = NotificationParam.LLFZR;
                notifText = "已通知收货负责人审核";
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
                        AllActivitiesHolder.removeAct(BcpInVerifyActivity.this);
                    }
                });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_in_verify;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
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
                convertView = View.inflate(BcpInVerifyActivity.this, R.layout.item_cp_verify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            BcpInShowBean bean = mData.get(position);
            //viewHolder.mWlbmValue.setText(bean.getwLCode());
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mLbValue.setText(bean.getSortName());
            String gg = bean.getgG();
            if(TextUtils.isEmpty(gg))
                gg=getString(R.string.no_gg);
            viewHolder.mGgValue.setText(gg);
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mSldwValue.setText(bean.getdW());
            if (bean.getShl()==-1) {
                viewHolder.mSlValue.setVisibility(View.GONE);
            }else {
                viewHolder.mSlValue.setVisibility(View.VISIBLE);
                viewHolder.mSlValue.setText(bean.getShl() + "");
            }
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");
            final String qRCodeID = bean.getqRCodeID();
            if("4".equals(qRCodeID.substring(8,9))){
                viewHolder.goSmallBtn.setVisibility(View.VISIBLE);
                viewHolder.goSmallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("qRCodeID",qRCodeID);
                        IntentUtil.openActivityForResult(BcpInVerifyActivity.this, SmallCPInQualityCheckActivity.class, -1, bundle);
                    }
                });

                viewHolder.mBcpGSBView.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.goSmallBtn.setVisibility(View.GONE);
                viewHolder.mBcpGSBView.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ViewHolder {
            //@BindView(R.id.wlbmValue)
            //TextView mWlbmValue;
            @BindView(R.id.nameValue)
            TextView mNameValue;
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.ggValue)
            TextView mGgValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.sldwValue)
            TextView mSldwValue;
            @BindView(R.id.slValue)
            TextView mSlValue;
            @BindView(R.id.dwzlValue)
            TextView mDwzlValue;
            @BindView(R.id.goSmallBtn)
            Button goSmallBtn;
            @BindView(R.id.bcpGSBView)
            View mBcpGSBView;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
