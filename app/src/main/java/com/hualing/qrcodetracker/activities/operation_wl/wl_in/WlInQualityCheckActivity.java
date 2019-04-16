package com.hualing.qrcodetracker.activities.operation_wl.wl_in;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.EmployeeMainActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.bean.WLINShowBean;
import com.hualing.qrcodetracker.bean.WlInQualityCheckResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.NotificationType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
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

public class WlInQualityCheckActivity extends BaseActivity {

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
    @BindView(R.id.remarkValue)
    TextView mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private WlInQualityCheckActivity.MyAdapter mAdapter;
    private List<WLINShowBean> mData;
    private String mDh;
    private VerifyParam param;
    private boolean isZJY=false;
    private boolean isZJLD=false;

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);

        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(WlInQualityCheckActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new VerifyParam();
        if (getIntent() != null) {
            String[] checkQXArr = GlobalData.checkQXGroup.split(",");
            for (String checkQX:checkQXArr) {
                if("zjy".equals(checkQX)){
                    isZJY=true;
                    break;
                }
                else if("zjld".equals(checkQX)){
                    isZJLD=true;
                    break;
                }
            }
            //下面是根据身份判断当前是质检还是审核
            int checkQXFlag=-1;
            if(isZJY)
                checkQXFlag=VerifyParam.ZJY;
            else if(isZJLD)
                checkQXFlag=VerifyParam.ZJLD;
            param.setCheckQXFlag(checkQXFlag);

            mDh = getIntent().getStringExtra("dh");
            param.setDh(mDh);
        }

        mData = new ArrayList<>();
        mAdapter = new WlInQualityCheckActivity.MyAdapter();
        mChildDataList.setAdapter(mAdapter);
        mChildDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<WlInQualityCheckResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WlInQualityCheckResult>> e) throws Exception {
                ActionResult<WlInQualityCheckResult> nr = mainDao.getWlInQualityCheckData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WlInQualityCheckResult>>() {
                    @Override
                    public void accept(ActionResult<WlInQualityCheckResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            WlInQualityCheckResult dataResult = result.getResult();
                            mIndhValue.setText(dataResult.getInDh());
                            mJhdwValue.setText(dataResult.getFhDw());
                            mShrqValue.setText(dataResult.getShRq());
                            mShfzrValue.setText(dataResult.getShFzr());
//                            mJhRValue.setText(dataResult.getFhR());
//                            mJhfzrValue.setText(dataResult.getJhFzr());
                            mRemarkValue.setText(TextUtils.isEmpty(dataResult.getRemark()) ? "无备注信息" : dataResult.getRemark());

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wlin_quality_check;
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
                ActionResult<ActionResult> nr = mainDao.toRefuseWlIn(param);
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
                            AllActivitiesHolder.removeAct(WlInQualityCheckActivity.this);
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
                ActionResult<ActionResult> nr = mainDao.toAgreeWlIn(param);
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
                            Toast.makeText(TheApplication.getContext(), "质检已通过", Toast.LENGTH_SHORT).show();
                            //setResult(RETURN_AND_REFRESH);
                            //AllActivitiesHolder.removeAct(WlInQualityCheckActivity.this);
                            sendNotification();
                            return;
                        }
                    }
                });
    }


    private void sendNotification() {

        final NotificationParam notificationParam = new NotificationParam();
        //根据单号去查找审核人
        notificationParam.setDh(param.getDh());
        notificationParam.setStyle(NotificationType.WL_RKD);
        int personFlag=-1;
        //下面是判断下一个质检或审核人是谁
        if(isZJY)//如果是质检员，就推送给质检领导
            personFlag=NotificationParam.ZJLD;
        else if(isZJLD)//如果是质检领导，就推送给仓库负责人
            personFlag=NotificationParam.FZR;
        notificationParam.setPersonFlag(personFlag);

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
                            if(isZJY)//如果是质检员，就推送给质检领导
                                Toast.makeText(TheApplication.getContext(), "已通知质检领导质检", Toast.LENGTH_SHORT).show();
                            else if(isZJLD)//如果是质检领导，就推送给仓库负责人
                                Toast.makeText(TheApplication.getContext(), "已通知仓库负责人审核", Toast.LENGTH_SHORT).show();
                        }
                        setResult(RETURN_AND_REFRESH);
                        AllActivitiesHolder.removeAct(WlInQualityCheckActivity.this);
                    }
                });

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
            WlInQualityCheckActivity.MyAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(WlInQualityCheckActivity.this, R.layout.item_wlin_quality_check, null);
                viewHolder = new WlInQualityCheckActivity.MyAdapter.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (WlInQualityCheckActivity.MyAdapter.ViewHolder) convertView.getTag();

            WLINShowBean bean = mData.get(position);
            viewHolder.mLbValue.setText(bean.getSortName());
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mCdValue.setText(bean.getcHD());
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mPczlValue.setText(bean.getpCZL() + "");
            viewHolder.mLlTimeValue.setText(bean.getlLTime());
            String gg = bean.getgG();
            if(TextUtils.isEmpty(gg))
                gg=getString(R.string.no_gg);
            viewHolder.mGgValue.setText(gg);
            viewHolder.mCzyValue.setText(bean.getcZY());
            viewHolder.mSldwValue.setText(bean.getUnit());
            viewHolder.mZhlValue.setText(bean.getdWZL() + "");

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.nameValue)
            TextView mNameValue;
            @BindView(R.id.cdValue)
            TextView mCdValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.pczlValue)
            TextView mPczlValue;
            @BindView(R.id.llTimeValue)
            TextView mLlTimeValue;
            @BindView(R.id.ggValue)
            TextView mGgValue;
            @BindView(R.id.czyValue)
            TextView mCzyValue;
            @BindView(R.id.sldwValue)
            TextView mSldwValue;
            @BindView(R.id.zhlValue)
            TextView mZhlValue;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
