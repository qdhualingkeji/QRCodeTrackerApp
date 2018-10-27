package com.hualing.qrcodetracker.activities.operation_bcp.bcp_in;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpInShowBean;
import com.hualing.qrcodetracker.bean.BcpInVerifyResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.widget.MyListView;

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

public class SmallCPInVerifyActivity extends BaseActivity {

    private MainDao mainDao;
    private SmallCPInVerifyActivity.MyAdapter mAdapter;
    private List<BcpInShowBean> mData;
    private String qRCodeID;
    private BcpInShowBean params;
    @BindView(R.id.dataList)
    MyListView mDataList;

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);

        params = new BcpInShowBean();
        if (getIntent() != null){
            qRCodeID = getIntent().getStringExtra("qRCodeID");
            params.setqRCodeID(qRCodeID);
        }

        mData = new ArrayList<>();
        mAdapter = new SmallCPInVerifyActivity.MyAdapter();
        mDataList.setAdapter(mAdapter);
        mDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BcpInVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BcpInVerifyResult>> e) throws Exception {
                ActionResult<BcpInVerifyResult> nr = mainDao.getSmallCPInVerifyData(params);
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

    @OnClick({R.id.returnBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.returnBtn:
                AllActivitiesHolder.removeAct(SmallCPInVerifyActivity.this);
                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_small_cpin_verify;
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
            SmallCPInVerifyActivity.MyAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(SmallCPInVerifyActivity.this, R.layout.item_small_cp_verify, null);
                viewHolder = new SmallCPInVerifyActivity.MyAdapter.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (SmallCPInVerifyActivity.MyAdapter.ViewHolder) convertView.getTag();

            BcpInShowBean bean = mData.get(position);
            //viewHolder.mWlbmValue.setText(bean.getwLCode());
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mLbValue.setText(bean.getSortName());
            viewHolder.mGgValue.setText(bean.getgG());
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mSldwValue.setText(bean.getdW());
            if (bean.getShl()==-1) {
                viewHolder.mSlValue.setVisibility(View.GONE);
            }else {
                viewHolder.mSlValue.setVisibility(View.VISIBLE);
                viewHolder.mSlValue.setText(bean.getShl() + "");
            }
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");

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

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
