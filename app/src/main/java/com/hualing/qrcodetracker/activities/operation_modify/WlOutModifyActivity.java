package com.hualing.qrcodetracker.activities.operation_modify;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.bean.WLOutShowBean;
import com.hualing.qrcodetracker.bean.WlOutVerifyResult;
import com.hualing.qrcodetracker.dao.MainDao;
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

public class WlOutModifyActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 12;
    private static final int REQUEST_CODE_SELECT_KG = 31;
    private static final int REQUEST_CODE_SELECT_FLFZR = 32;
    private static final int REQUEST_CODE_SELECT_BZ = 33;
    private static final int REQUEST_CODE_SELECT_LLFZR = 34;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.outdhValue)
    TextView mOutdhValue;
    @BindView(R.id.LldwValue)
    TextView mLldwValue;
    @BindView(R.id.selectLLBM)
    LinearLayout mSelectLLBM;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.flfzrValue)
    TextView mFlfzrValue;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.llfzrValue)
    TextView mLlfzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;
    private int kgID;
    private int flfzrID;
    private int bzID;
    private int llfzrID;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<WLOutShowBean> mData;
    private String mDh;
    private VerifyParam param;
    private WlOutVerifyResult updatedParam;

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
                AllActivitiesHolder.removeAct(WlOutModifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        updatedParam = new WlOutVerifyResult();
        param = new VerifyParam();
        if (getIntent() != null) {
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

        Observable.create(new ObservableOnSubscribe<ActionResult<WlOutVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WlOutVerifyResult>> e) throws Exception {
                ActionResult<WlOutVerifyResult> nr = mainDao.getWlOutVerifyData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WlOutVerifyResult>>() {
                    @Override
                    public void accept(ActionResult<WlOutVerifyResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            WlOutVerifyResult dataResult = result.getResult();
                            updatedParam = dataResult;
                            mOutdhValue.setText(dataResult.getOutDh());
                            mLldwValue.setText(dataResult.getLhDw());
                            mSelectLLBM.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtil.openActivityForResult(WlOutModifyActivity.this, SelectDepartmentActivity.class, REQUEST_CODE_SELECT_DEPARTMENT, null);
                                }
                            });

                            kgID = dataResult.getKgID();
                            mKgValue.setText(dataResult.getKg());
                            mKgValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setKg("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            flfzrID = dataResult.getFlfzrID();
                            mFlfzrValue.setText(dataResult.getFhFzr());
                            mFlfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setFhFzr("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            bzID = dataResult.getBzID();
                            mBzValue.setText(dataResult.getBz());
                            mBzValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setBz("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            llfzrID = dataResult.getLlfzrID();
                            mLlfzrValue.setText(dataResult.getLhFzr());
                            mLlfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setLhFzr("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mRemarkValue.setText(dataResult.getRemark());
                            mRemarkValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setRemark("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

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
    protected int getLayoutResId() {
        return R.layout.activity_wl_out_modify;
    }

    @OnClick({R.id.selectKG,R.id.selectFLFZR,R.id.selectBZ,R.id.selectLLFZR,R.id.confirmBtn})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.selectKG:
                bundle.putString("checkQX", "kg");
                IntentUtil.openActivityForResult(WlOutModifyActivity.this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_KG, bundle);
                break;
            case R.id.selectFLFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(WlOutModifyActivity.this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_FLFZR, bundle);
                break;
            case R.id.selectBZ:
                bundle.putString("checkQX", "bz");
                IntentUtil.openActivityForResult(WlOutModifyActivity.this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_BZ, bundle);
                break;
            case R.id.selectLLFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(WlOutModifyActivity.this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_LLFZR, bundle);
                break;
            case R.id.confirmBtn:
                toCommit();
                break;

        }
    }

    private void toCommit() {

        for (int i = 0; i < mData.size(); i++) {

            if (mData.get(i).getcKZL() == -1
                    || "请选择部门".equals(mLldwValue.getText().toString())
                    || "请选择仓库管理员".equals(mKgValue.getText().toString())
                    || "请选择发料负责人".equals(mFlfzrValue.getText().toString())
                    || "请选择班长".equals(mBzValue.getText().toString())
                    || "请选择领料负责人".equals(mLlfzrValue.getText().toString())
                    ) {
                Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }
            if(mData.get(i).getcKZL() > mData.get(i).getsYZL() ){
                Toast.makeText(this, "出库重量不能大于剩余重量", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        updatedParam.setBeans(mData);
        updatedParam.setKgID(kgID);
        updatedParam.setKgStatus(0);
        updatedParam.setFlfzrID(flfzrID);
        updatedParam.setFlfzrStatus(0);
        updatedParam.setBzID(bzID);
        updatedParam.setBzStatus(0);
        updatedParam.setLlfzrID(llfzrID);
        updatedParam.setLlfzrStatus(0);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.toUpdateWLOutData(updatedParam);
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
                            Toast.makeText(TheApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            sendNotification();
                            return;
                        }
                    }
                });
    }

    private void sendNotification() {

        final NotificationParam notificationParam = new NotificationParam();
        notificationParam.setDh(mDh);
        notificationParam.setStyle(NotificationType.WL_CKD);
        notificationParam.setPersonFlag(NotificationParam.KG);

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
                            Toast.makeText(TheApplication.getContext(), "已通知仓库管理员审核", Toast.LENGTH_SHORT).show();
                        }
                        setResult(RETURN_AND_REFRESH);
                        AllActivitiesHolder.removeAct(WlOutModifyActivity.this);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(WlOutModifyActivity.this, R.layout.item_wlout_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final WLOutShowBean bean = mData.get(position);
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mLbValue.setText(bean.getSortName());
            viewHolder.mGgValue.setText(bean.getgG());
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mSldwValue.setText(bean.getdW());
            viewHolder.shl = bean.getShl();
            viewHolder.pCZL = bean.getpCZL();
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");
            viewHolder.mSyzlValue.setText(bean.getsYZL() + "");
            viewHolder.mCkzlValue.setText(bean.getcKZL() + "");
            viewHolder.mCkzlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat("" + s);
                        bean.setcKZL(num);
                        bean.setShl(num/viewHolder.pCZL);
                    } else {
                        Log.e("1111","111111111");
                        bean.setcKZL(-1);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

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
            @BindView(R.id.dwzlValue)
            TextView mDwzlValue;
            @BindView(R.id.syzlValue)
            TextView mSyzlValue;
            @BindView(R.id.ckzlValue)
            EditText mCkzlValue;
            Float shl;
            Float pCZL;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_DEPARTMENT:
                    mLldwValue.setText(data.getStringExtra("groupName"));
                    updatedParam.setLhDw(data.getStringExtra("groupName"));
                    break;
                case REQUEST_CODE_SELECT_KG:
                    kgID = data.getIntExtra("personID",0);
                    mKgValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_FLFZR:
                    flfzrID = data.getIntExtra("personID",0);
                    mFlfzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_BZ:
                    bzID = data.getIntExtra("personID",0);
                    mBzValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_LLFZR:
                    llfzrID = data.getIntExtra("personID",0);
                    mLlfzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
