package com.hualing.qrcodetracker.activities.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.BcpInQualityCheckActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.BcpInVerifyActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_return.BcpOutVerifyActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_tl.BcpTkQualityCheckActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_tl.BcpTkVerifyActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.WlInQualityCheckActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.WlInVerifyActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_out.WlOutVerifyActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_return.WlTkQualityCheckActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_return.WlTkVerifyActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.MainParams;
import com.hualing.qrcodetracker.bean.NonCheckBean;
import com.hualing.qrcodetracker.bean.NonCheckResult;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.widget.TitleBar;
import com.hualing.qrcodetracker.widget.pull2refresh.PullToRefreshLayout;
import com.hualing.qrcodetracker.widget.pull2refresh.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NonHandleMsgActivity extends BaseActivity {

    private static final int REQUEST_CODE = 111;
    public static final int RETURN_AND_REFRESH = 112;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.listView)
    PullableListView mListView;
    @BindView(R.id.refresher)
    PullToRefreshLayout mRefresher;
    @BindView(R.id.noDataTip)
    RelativeLayout mNoDataTip;


    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<NonCheckBean> mData;
    private boolean isKG;
    private boolean isBZ;
    private boolean isFZR;
    private boolean isZJY;
    private boolean isZJLD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        //判断登录角色的身份（领导、质检员）
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

        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(NonHandleMsgActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
        mRefresher.setOnRefreshListener(new MyListener());
        mData = new ArrayList<>();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void getDataFormWeb() {
        mRefresher.autoRefresh();
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_non_handle_msg;
    }

    private void getData() {
        mainDao = YoniClient.getInstance().create(MainDao.class);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        final MainParams params = new MainParams();
        params.setUserId(GlobalData.userId);
        params.setRealName(GlobalData.realName);
        if(isKG)
            params.setCheckQXFlag(MainParams.KG);
        else if(isBZ)
            params.setCheckQXFlag(MainParams.BZ);
        else if(isFZR)
            params.setCheckQXFlag(MainParams.FZR);
        else if(isZJY)
            params.setCheckQXFlag(MainParams.ZJY);
        else if(isZJLD)
            params.setCheckQXFlag(MainParams.ZJLD);

        Observable.create(new ObservableOnSubscribe<ActionResult<NonCheckResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<NonCheckResult>> e) throws Exception {
                ActionResult<NonCheckResult> nr = mainDao.getNonCheckData(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<NonCheckResult>>() {
                    @Override
                    public void accept(ActionResult<NonCheckResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            NonCheckResult mainResult = result.getResult();
                            if (mainResult.getBeans() != null && mainResult.getBeans().size() > 0) {
                                mNoDataTip.setVisibility(View.GONE);
                                mRefresher.setVisibility(View.VISIBLE);
                                mData.clear();
                                mData.addAll(mainResult.getBeans());
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mNoDataTip.setVisibility(View.VISIBLE);
                                mRefresher.setVisibility(View.GONE);
                            }
                            mRefresher.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_non_check, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            final NonCheckBean info = mData.get(position);
            final String name = info.getName();
            holder.mName.setText(name);
            final String dh = info.getDh();
            holder.mDh.setText(dh);
            String time = info.getTime();
            holder.mDate.setText(time);

            convertView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =null;
                            switch (name){
                                case "物料入库单":
                                    if(isKG||isFZR)
                                        intent = new Intent(NonHandleMsgActivity.this,WlInVerifyActivity.class);
                                    else if(isZJY||isZJLD)
                                        intent = new Intent(NonHandleMsgActivity.this,WlInQualityCheckActivity.class);
                                    break;
                                case "物料出库单":
                                    intent = new Intent(NonHandleMsgActivity.this,WlOutVerifyActivity.class);
                                    if(GlobalData.userId.equals(info.getFlfzrID()+""))
                                        intent.putExtra("personFlag", NotificationParam.FLFZR);
                                    else if(GlobalData.userId.equals(info.getLlfzrID()+""))
                                        intent.putExtra("personFlag", NotificationParam.LLFZR);
                                    break;
                                case "物料退库单":
                                    if(isBZ||isKG||isFZR) {
                                        intent = new Intent(NonHandleMsgActivity.this, WlTkVerifyActivity.class);
                                        if(GlobalData.userId.equals(info.getTlfzrID()+""))
                                            intent.putExtra("personFlag", NotificationParam.TLFZR);
                                        else if(GlobalData.userId.equals(info.getSlfzrID()+""))
                                            intent.putExtra("personFlag", NotificationParam.SLFZR);
                                    }
                                    else if(isZJY||isZJLD)//物料退库单一般都是由班长、库管、负责人（退料负责人、收料负责人）等审核，没有质检员或质检领导，这里是之前做的，后来改了，至于这个类先留着不删除
                                        intent = new Intent(NonHandleMsgActivity.this,WlTkQualityCheckActivity.class);
                                    break;
                                case "半成品录入单":
                                case "成品入库单":
                                    if(isBZ||isKG||isFZR) {
                                        intent = new Intent(NonHandleMsgActivity.this, BcpInVerifyActivity.class);
                                        if(GlobalData.userId.equals(info.getFzrID()+""))
                                            intent.putExtra("personFlag", NotificationParam.FZR);
                                        else if(GlobalData.userId.equals(info.getFlfzrID()+""))
                                            intent.putExtra("personFlag", NotificationParam.FLFZR);
                                        else if(GlobalData.userId.equals(info.getLlfzrID()+""))
                                            intent.putExtra("personFlag", NotificationParam.LLFZR);
                                        intent.putExtra("name",name);
                                    }
                                    else if(isZJY||isZJLD)
                                        intent = new Intent(NonHandleMsgActivity.this,BcpInQualityCheckActivity.class);
                                    intent.putExtra("name",name);
                                    break;
                                case "成品出库单":
                                    intent = new Intent(NonHandleMsgActivity.this,BcpOutVerifyActivity.class);
                                    break;
                                case "半成品入库（退库）单":
                                    if(isBZ||isKG||isFZR) {
                                        intent = new Intent(NonHandleMsgActivity.this, BcpTkVerifyActivity.class);
                                        if(GlobalData.userId.equals(info.getTlfzrID()+""))
                                            intent.putExtra("personFlag", NotificationParam.TLFZR);
                                        else if(GlobalData.userId.equals(info.getSlfzrID()+""))
                                            intent.putExtra("personFlag", NotificationParam.SLFZR);
                                    }
                                    else if(isZJY||isZJLD)
                                        intent = new Intent(NonHandleMsgActivity.this,BcpTkQualityCheckActivity.class);
                                    break;
                            }
                            if (intent!=null) {
                                intent.putExtra("dh", dh);
                                startActivityForResult(intent,REQUEST_CODE);
                            }
                        }
                    });

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.dh)
            TextView mDh;
            @BindView(R.id.date)
            TextView mDate;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE){
            if (resultCode == RETURN_AND_REFRESH) {
                mRefresher.autoRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 下拉刷新监听器
     */
    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            getData();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            //不要上拉加载
            mRefresher.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

}
