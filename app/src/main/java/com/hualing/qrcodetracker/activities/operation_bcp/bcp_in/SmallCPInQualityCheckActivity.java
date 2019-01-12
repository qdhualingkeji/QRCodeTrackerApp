package com.hualing.qrcodetracker.activities.operation_bcp.bcp_in;

import android.app.Dialog;
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
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpInQualityCheckResult;
import com.hualing.qrcodetracker.bean.BcpInShowBean;
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

public class SmallCPInQualityCheckActivity extends BaseActivity {

    private MainDao mainDao;
    private SmallCPInQualityCheckActivity.MyAdapter mAdapter;
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
        mAdapter = new SmallCPInQualityCheckActivity.MyAdapter();
        mDataList.setAdapter(mAdapter);
        mDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BcpInQualityCheckResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BcpInQualityCheckResult>> e) throws Exception {
                ActionResult<BcpInQualityCheckResult> nr = mainDao.getSmallCPInQualityCheckData(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BcpInQualityCheckResult>>() {
                    @Override
                    public void accept(ActionResult<BcpInQualityCheckResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            BcpInQualityCheckResult dataResult = result.getResult();

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
                AllActivitiesHolder.removeAct(SmallCPInQualityCheckActivity.this);
                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_small_cpin_quality_check;
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
            SmallCPInQualityCheckActivity.MyAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(SmallCPInQualityCheckActivity.this, R.layout.item_small_cp_quality_check, null);
                viewHolder = new SmallCPInQualityCheckActivity.MyAdapter.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (SmallCPInQualityCheckActivity.MyAdapter.ViewHolder) convertView.getTag();

            BcpInShowBean bean = mData.get(position);
            viewHolder.mBcpLbValue.setText(bean.getSortName());
            viewHolder.mBcpNameValue.setText(bean.getProductName());
            //viewHolder.mBcpBmValue.setText(bean.getwLCode());
            viewHolder.mBcpYlpcValue.setText(bean.getyLPC());
            viewHolder.mBcpScpcValue.setText(bean.getsCPC());
            viewHolder.mBcpScTimeValue.setText(bean.getTime());
            String gg = bean.getgG();
            if("".equals(gg))
                gg=getString(R.string.no_gg);
            viewHolder.mBcpGgValue.setText(gg);
            viewHolder.mBcpCjValue.setText(bean.getCheJian());
            viewHolder.mBcpGxValue.setText(bean.getGx());
            viewHolder.mBcpCzyValue.setText(bean.getCzy());
            viewHolder.mBcpSldwValue.setText(bean.getdW());
            viewHolder.mBcpZhlValue.setText(bean.getdWZL() + "");
            if("4".equals(qRCodeID.substring(8,9))){
                viewHolder.mBcpCjLayout.setVisibility(View.GONE);
                viewHolder.mBcpCjView.setVisibility(View.GONE);

                viewHolder.mBcpGxLayout.setVisibility(View.GONE);
                viewHolder.mBcpGxView.setVisibility(View.GONE);
            }
            else{
                viewHolder.mBcpCjLayout.setVisibility(View.VISIBLE);
                viewHolder.mBcpCjView.setVisibility(View.VISIBLE);

                viewHolder.mBcpGxLayout.setVisibility(View.VISIBLE);
                viewHolder.mBcpGxView.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.bcpLbValue)
            TextView mBcpLbValue;
            @BindView(R.id.bcpNameValue)
            TextView mBcpNameValue;
            //@BindView(R.id.bcpBmValue)
            //TextView mBcpBmValue;
            @BindView(R.id.bcpYlpcValue)
            TextView mBcpYlpcValue;
            @BindView(R.id.bcpScpcValue)
            TextView mBcpScpcValue;
            @BindView(R.id.bcpScTimeValue)
            TextView mBcpScTimeValue;
            @BindView(R.id.bcpGgValue)
            TextView mBcpGgValue;
            @BindView(R.id.bcpCjLayout)
            LinearLayout mBcpCjLayout;
            @BindView(R.id.bcpCjValue)
            TextView mBcpCjValue;
            @BindView(R.id.bcpCjView)
            View mBcpCjView;
            @BindView(R.id.bcpGxLayout)
            LinearLayout mBcpGxLayout;
            @BindView(R.id.bcpGxValue)
            TextView mBcpGxValue;
            @BindView(R.id.bcpGxView)
            View mBcpGxView;
            @BindView(R.id.bcpCzyValue)
            TextView mBcpCzyValue;
            @BindView(R.id.bcpSldwValue)
            TextView mBcpSldwValue;
            @BindView(R.id.bcpZhlValue)
            TextView mBcpZhlValue;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
