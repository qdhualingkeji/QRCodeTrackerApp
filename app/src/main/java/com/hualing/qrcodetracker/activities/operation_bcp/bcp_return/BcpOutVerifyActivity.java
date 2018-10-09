package com.hualing.qrcodetracker.activities.operation_bcp.bcp_return;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.WlInVerifyActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.CpOutShowBean;
import com.hualing.qrcodetracker.bean.CpOutVerifyResult;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
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
    @BindView(R.id.lhdwValue)
    TextView mLhdwValue;
    @BindView(R.id.lhrqValue)
    TextView mLhrqValue;
    @BindView(R.id.jhfzrValue)
    TextView mJhfzrValue;
    @BindView(R.id.lhRValue)
    TextView mLhRValue;
    @BindView(R.id.lhfzrValue)
    TextView mLhfzrValue;
    @BindView(R.id.remarkValue)
    TextView mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<CpOutShowBean> mData;
    private String mDh;
    private VerifyParam param;
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
                AllActivitiesHolder.removeAct(BcpOutVerifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new VerifyParam();
        if (getIntent() != null) {
            Log.e("checkQX==========",""+GlobalData.checkQXGroup);
            String[] checkQXArr = GlobalData.checkQXGroup.split(",");
            for (String checkQX:checkQXArr) {
                if("bz".equals(checkQX)){
                    isBZ=true;
                    break;
                }
                else if("ld".equals(checkQX)){
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
            if(isBZ)
                param.setCheckQXFlag(VerifyParam.BZ);
            else if(isFZR)
                param.setCheckQXFlag(VerifyParam.FZR);
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
                            mLhdwValue.setText(dataResult.getLhDw());
                            mLhrqValue.setText(dataResult.getLhRq());
                            mLhRValue.setText(dataResult.getLhR());
                            mLhfzrValue.setText(dataResult.getLhFzr());
                            mJhfzrValue.setText(dataResult.getFhFzr());
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
                ActionResult<ActionResult> nr = mainDao.toRefuseBcpOut(param);
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
                ActionResult<ActionResult> nr = mainDao.toAgreeBcpOut(param);
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
                            setResult(RETURN_AND_REFRESH);
                            AllActivitiesHolder.removeAct(BcpOutVerifyActivity.this);
                            return;
                        }
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_out_verify;
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
                convertView = View.inflate(BcpOutVerifyActivity.this, R.layout.item_cpout_verify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            CpOutShowBean bean = mData.get(position);
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
