package com.hualing.qrcodetracker.activities.operation_bcp.bcp_tl;

import android.app.Dialog;
import android.os.Bundle;
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
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpTkShowBean;
import com.hualing.qrcodetracker.bean.BcpTkVerifyResult;
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

public class BcpTkVerifyActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.backdhValue)
    TextView mBackdhValue;
    @BindView(R.id.thdwValue)
    TextView mThdwValue;
    @BindView(R.id.thrqValue)
    TextView mThrqValue;
    @BindView(R.id.thRValue)
    TextView mThRValue;
    @BindView(R.id.bzLayout)
    LinearLayout mBzLayout;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.bzView)
    View mBzView;
    @BindView(R.id.kgLayout)
    LinearLayout mKgLayout;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.kgView)
    View mKgView;
    @BindView(R.id.remarkValue)
    TextView mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<BcpTkShowBean> mData;
    private String mDh;
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
                AllActivitiesHolder.removeAct(BcpTkVerifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new VerifyParam();
        if (getIntent() != null) {
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
            if(isKG) {
                param.setCheckQXFlag(VerifyParam.KG);
                mKgLayout.setVisibility(LinearLayout.VISIBLE);
                mKgView.setVisibility(View.VISIBLE);
            }
            else if(isBZ) {
                param.setCheckQXFlag(VerifyParam.BZ);
                mBzLayout.setVisibility(LinearLayout.VISIBLE);
                mBzView.setVisibility(View.VISIBLE);
            }
            else if(isFZR) {
                int personFlag = getIntent().getIntExtra("personFlag", -1);
                if(personFlag== NotificationParam.TLFZR) {
                    param.setCheckQXFlag(VerifyParam.TLFZR);
                    mBzLayout.setVisibility(LinearLayout.VISIBLE);
                    mBzView.setVisibility(View.VISIBLE);
                }
                else if(personFlag==NotificationParam.SLFZR) {
                    param.setCheckQXFlag(VerifyParam.SLFZR);
                    mKgLayout.setVisibility(LinearLayout.VISIBLE);
                    mKgView.setVisibility(View.VISIBLE);
                }
            }
            else if(isZJY)
                param.setCheckQXFlag(VerifyParam.ZJY);
            else if(isZJLD)
                param.setCheckQXFlag(VerifyParam.ZJLD);

            mDh = getIntent().getStringExtra("dh");
            param.setDh(mDh);
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

        Observable.create(new ObservableOnSubscribe<ActionResult<BcpTkVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BcpTkVerifyResult>> e) throws Exception {
                ActionResult<BcpTkVerifyResult> nr = mainDao.getBcpTkVerifyData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BcpTkVerifyResult>>() {
                    @Override
                    public void accept(ActionResult<BcpTkVerifyResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            BcpTkVerifyResult dataResult = result.getResult();
                            mBackdhValue.setText(dataResult.getBackDh());
                            mThdwValue.setText(dataResult.getThDw());
                            mThrqValue.setText(dataResult.getThRq());
                            mThRValue.setText(dataResult.getThR());
                            if(param.getCheckQXFlag()==VerifyParam.BZ||param.getCheckQXFlag()==VerifyParam.TLFZR)
                                mBzValue.setText(dataResult.getBz());
                            else if(param.getCheckQXFlag()==VerifyParam.KG||param.getCheckQXFlag()==VerifyParam.SLFZR)
                                mKgValue.setText(dataResult.getKg());
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
                ActionResult<ActionResult> nr = mainDao.toRefuseBcpTk(param);
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
                            AllActivitiesHolder.removeAct(BcpTkVerifyActivity.this);
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
                ActionResult<ActionResult> nr = mainDao.toAgreeBcpTk(param);
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
                            if(param.getCheckQXFlag()==VerifyParam.BZ||param.getCheckQXFlag()==VerifyParam.TLFZR||param.getCheckQXFlag()==VerifyParam.KG)//如果登录者是班长的话，说明还得推送给负责人
                                sendNotification(param.getCheckQXFlag());
                            else{//不是的话，说明登录者就是负责人，最后一道审核就不必再推送了
                                setResult(RETURN_AND_REFRESH);
                                AllActivitiesHolder.removeAct(BcpTkVerifyActivity.this);
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
        notificationParam.setStyle(NotificationType.BCP_TKD);
        int personFlag=-1;
        String notifText=null;
        if(checkQXFlag==VerifyParam.BZ){
            personFlag=NotificationParam.TLFZR;
            notifText="已通知交货负责人审核";
        }
        else if(checkQXFlag==VerifyParam.TLFZR){
            personFlag=NotificationParam.ZJY;
            notifText="已通知质检员质检";
        }
        else if(checkQXFlag==VerifyParam.KG){
            personFlag=NotificationParam.SLFZR;
            notifText="已通知收货负责人审核";
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
                        AllActivitiesHolder.removeAct(BcpTkVerifyActivity.this);
                    }
                });

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_tk_verify;
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
                convertView = View.inflate(BcpTkVerifyActivity.this, R.layout.item_bcp_tk_verify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            BcpTkShowBean bean = mData.get(position);
            //viewHolder.mWlbmValue.setText(bean.getBcpCode());
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mLbValue.setText(bean.getSortName());
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mRemarkValue.setText(TextUtils.isEmpty(bean.getRemark())?"无备注信息":bean.getRemark());
            viewHolder.mSlValue.setText(bean.getShl() + "");
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");
            viewHolder.mTkzlValue.setText(bean.gettKZL() + "");

            return convertView;
        }

        class ViewHolder {
            //@BindView(R.id.wlbmValue)
            //TextView mWlbmValue;
            @BindView(R.id.nameValue)
            TextView mNameValue;
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.remarkValue)
            TextView mRemarkValue;
            @BindView(R.id.slValue)
            TextView mSlValue;
            @BindView(R.id.dwzlValue)
            TextView mDwzlValue;
            @BindView(R.id.tkzlValue)
            TextView mTkzlValue;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
