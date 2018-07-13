package com.hualing.qrcodetracker.activities.operation_wl.wl_return;

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
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.WlInVerifyActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.bean.WLTkShowBean;
import com.hualing.qrcodetracker.bean.WlTkVerifyResult;
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

public class WlTkVerifyActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.backdhValue)
    TextView mBackdhValue;
    @BindView(R.id.thdwValue)
    TextView mThdwValue;
    @BindView(R.id.thrqValue)
    TextView mThrqValue;
    @BindView(R.id.shfzrValue)
    TextView mShfzrValue;
    @BindView(R.id.thRValue)
    TextView mThRValue;
    @BindView(R.id.thfzrValue)
    TextView mThfzrValue;
    @BindView(R.id.remarkValue)
    TextView mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<WLTkShowBean> mData;
    private String mDh;
    private VerifyParam param;

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
                AllActivitiesHolder.removeAct(WlTkVerifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new VerifyParam();
        if (getIntent() != null) {
            String[] checkQXArr = GlobalData.checkQXGroup.split(",");
            boolean isFZR=false;
            for (String checkQX:checkQXArr) {
                if("19".equals(checkQX)){
                    isFZR=true;
                    break;
                }
            }
            if(isFZR)
                param.setCheckQXFlag(VerifyParam.FZR);
            else
                param.setCheckQXFlag(VerifyParam.ZJY);

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

        Observable.create(new ObservableOnSubscribe<ActionResult<WlTkVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WlTkVerifyResult>> e) throws Exception {
                ActionResult<WlTkVerifyResult> nr = mainDao.getWlTkVerifyData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WlTkVerifyResult>>() {
                    @Override
                    public void accept(ActionResult<WlTkVerifyResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            WlTkVerifyResult dataResult = result.getResult();
                            mBackdhValue.setText(dataResult.getBackDh());
                            mThdwValue.setText(dataResult.getThDw());
                            mThrqValue.setText(dataResult.getThRq());
                            mShfzrValue.setText(dataResult.getShFzr());
                            mThfzrValue.setText(dataResult.getThFzr());
                            mThRValue.setText(dataResult.getThR());
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
                ActionResult<ActionResult> nr = mainDao.toRefuseWlTk(param);
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
                ActionResult<ActionResult> nr = mainDao.toAgreeWlTk(param);
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
                            AllActivitiesHolder.removeAct(WlTkVerifyActivity.this);
                            return;
                        }
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wl_tk_verify;
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
                convertView = View.inflate(WlTkVerifyActivity.this, R.layout.item_wlout_verify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            WLTkShowBean bean = mData.get(position);
            viewHolder.mWlbmValue.setText(bean.getwLCode());
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mLbValue.setText(bean.getSortID()+"");
            viewHolder.mGgValue.setText(bean.getgG());
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mSldwValue.setText(bean.getdW());
            viewHolder.mSlValue.setText(bean.getShl() + "");
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.wlbmValue)
            TextView mWlbmValue;
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
