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
import com.hualing.qrcodetracker.bean.BcpTkShowBean;
import com.hualing.qrcodetracker.bean.BcpTkVerifyResult;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
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

public class BcpTkModifyActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 12;
    private static final int REQUEST_CODE_SELECT_SLR = 31;
    private static final int REQUEST_CODE_SELECT_BZ = 32;
    private static final int REQUEST_CODE_SELECT_TLFZR = 33;
    private static final int REQUEST_CODE_SELECT_ZJY = 34;
    private static final int REQUEST_CODE_SELECT_ZJLD = 35;
    private static final int REQUEST_CODE_SELECT_KG = 36;
    private static final int REQUEST_CODE_SELECT_SLFZR = 37;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.backdhValue)
    TextView mBackdhValue;
    @BindView(R.id.tkdwValue)
    TextView mTkdwValue;
    @BindView(R.id.selectLLBM)
    LinearLayout mSelectLLBM;
    @BindView(R.id.shRValue)
    TextView mShRValue;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.tkfzrValue)
    TextView mTkfzrValue;
    @BindView(R.id.zjyValue)
    TextView mZjyValue;
    @BindView(R.id.zjldValue)
    TextView mZjldValue;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.shfzrValue)
    TextView mShfzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<BcpTkShowBean> mData;
    private String mDh;
    private VerifyParam param;
    private BcpTkVerifyResult updatedParam;
    private Integer bzID;
    private Integer tlfzrID;
    private Integer zjyID;
    private Integer zjldID;
    private Integer kgID;
    private Integer slfzrID;

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
                AllActivitiesHolder.removeAct(BcpTkModifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        updatedParam = new BcpTkVerifyResult();
        param = new VerifyParam();
        if (getIntent() != null) {
            mDh = getIntent().getStringExtra("dh");
            param.setDh(mDh);
        }

        mData = new ArrayList<>();
        mAdapter = new BcpTkModifyActivity.MyAdapter();
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
                            updatedParam = dataResult;
                            mBackdhValue.setText(dataResult.getBackDh());
                            mTkdwValue.setText(dataResult.getThDw());
                            mSelectLLBM.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtil.openActivityForResult(BcpTkModifyActivity.this, SelectDepartmentActivity.class, REQUEST_CODE_SELECT_DEPARTMENT, null);
                                }
                            });

                            mShRValue.setText(dataResult.getShR());
                            mShRValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setShR("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            bzID=dataResult.getBzID();
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

                            tlfzrID=dataResult.getTlfzrID();
                            mTkfzrValue.setText(dataResult.getThFzr());
                            mTkfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setThFzr("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            zjyID=dataResult.getZjyID();
                            mZjyValue.setText(dataResult.getZjy());
                            mZjyValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setZjy("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            zjldID=dataResult.getZjldID();
                            mZjldValue.setText(dataResult.getZjld());
                            mZjldValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setZjld("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            kgID=dataResult.getKgID();
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

                            slfzrID=dataResult.getSlfzrID();
                            mShfzrValue.setText(dataResult.getShFzr());
                            mShfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setShFzr("" + s);
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
        return R.layout.activity_bcp_tk_modify;
    }

    @OnClick({R.id.selectSHR,R.id.selectBZ,R.id.selectTHFZR,R.id.selectZJY,R.id.selectZJLD,R.id.selectKG,R.id.selectSHFZR,R.id.confirmBtn})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.selectSHR:
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_SLR, null);
                break;
            case R.id.selectBZ:
                bundle.putString("checkQX", "bz");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_BZ, bundle);
                break;
            case R.id.selectTHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_TLFZR, bundle);
                break;
            case R.id.selectZJY:
                bundle.putString("checkQX", "zjy");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_ZJY, bundle);
                break;
            case R.id.selectZJLD:
                bundle.putString("checkQX", "zjld");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_ZJLD, bundle);
                break;
            case R.id.selectKG:
                bundle.putString("checkQX", "kg");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_KG, bundle);
                break;
            case R.id.selectSHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_SLFZR, bundle);
                break;
            case R.id.confirmBtn:
                toCommit();
                break;

        }

    }

    private void toCommit() {

        for (int i = 0; i < mData.size(); i++) {

            if ("请选择部门".equals(mTkdwValue.getText().toString())
                    || "请选择收货人".equals(mShRValue.getText().toString())
                    || "请选择班长".equals(mBzValue.getText().toString())
                    || "请选择退库负责人".equals(mTkfzrValue.getText().toString())
                    || "请选择质检员".equals(mZjyValue.getText().toString())
                    || "请选择质检领导".equals(mZjldValue.getText().toString())
                    || "请选择仓库管理员".equals(mKgValue.getText().toString())
                    || "请选择收货负责人".equals(mShfzrValue.getText().toString())
                    || mData.get(i).gettKZL()==-1
                    ) {
                Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }
            if(mData.get(i).gettKZL() > mData.get(i).getsYZL()+mData.get(i).gettKZL1() ){
                Toast.makeText(this, "退库重量不能大于车间的半成品重量"+(mData.get(i).getsYZL()+mData.get(i).gettKZL1())+mData.get(i).getdW(), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        updatedParam.setBeans(mData);
        updatedParam.setBzID(bzID);
        updatedParam.setBzStatus(0);
        updatedParam.setTlfzrID(tlfzrID);
        updatedParam.setTlfzrStatus(0);
        updatedParam.setZjyID(zjyID);
        updatedParam.setZjyStatus(0);
        updatedParam.setZjldID(zjldID);
        updatedParam.setZjldStatus(0);
        updatedParam.setKgID(kgID);
        updatedParam.setKgStatus(0);
        updatedParam.setSlfzrID(slfzrID);
        updatedParam.setSlfzrStatus(0);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.toUpdateBcpTkData(updatedParam);
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
        //根据单号去查找审核人
        String dh = SharedPreferenceUtil.getBCPTKDNumber();
        notificationParam.setDh(dh);
        notificationParam.setStyle(NotificationType.BCP_TKD);
        notificationParam.setPersonFlag(NotificationParam.BZ);

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
                            Toast.makeText(TheApplication.getContext(), "已通知班长审核", Toast.LENGTH_SHORT).show();
                        }
                        setResult(RETURN_AND_REFRESH);
                        AllActivitiesHolder.removeAct(BcpTkModifyActivity.this);
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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(BcpTkModifyActivity.this, R.layout.item_bcptk_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final BcpTkShowBean bean = mData.get(position);
            viewHolder.mNameValue.setText(bean.getProductName());
            //viewHolder.mLbValue.setText(bean.getSortID() + "");
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mScpcValue.setText(bean.getsCPC());
            viewHolder.mScTimeValue.setText(bean.getScTime());
            viewHolder.mShl=bean.getShl();
            viewHolder.mDwZhlValue.setText(bean.getdWZL() + "");
            viewHolder.mSyzlValue.setText(bean.getsYZL() + "");
            viewHolder.mTkzlValue.setText(bean.gettKZL() + "");
            viewHolder.mTkzlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat("" + s);
                        bean.settKZL(num);
                        bean.setShl(num/bean.getpCZL());//单位重量变了，必须计算出相对数量
                    } else {
                        bean.settKZL(-1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mRemarkValue.setText(bean.getRemark());
            viewHolder.mRemarkValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setRemark("" + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.nameValue)
            TextView mNameValue;
            //@BindView(R.id.lbValue)
            //TextView mLbValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.scpcValue)
            TextView mScpcValue;
            @BindView(R.id.scTimeValue)
            TextView mScTimeValue;
            @BindView(R.id.dwZhlValue)
            TextView mDwZhlValue;
            @BindView(R.id.syzlValue)
            TextView mSyzlValue;
            @BindView(R.id.tkzlValue)
            EditText mTkzlValue;
            //@BindView(R.id.tkShlValue)
            //EditText mTkShlValue;
            Float mShl;
            @BindView(R.id.remarkValue)
            EditText mRemarkValue;

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
                    mTkdwValue.setText(data.getStringExtra("groupName"));
                    updatedParam.setThDw(data.getStringExtra("groupName"));
                    break;
                case REQUEST_CODE_SELECT_SLR:
                    mShRValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_BZ:
                    bzID=data.getIntExtra("personID",0);
                    mBzValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_TLFZR:
                    tlfzrID=data.getIntExtra("personID",0);
                    mTkfzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_ZJY:
                    zjyID=data.getIntExtra("personID",0);
                    mZjyValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_ZJLD:
                    zjldID=data.getIntExtra("personID",0);
                    mZjldValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_KG:
                    kgID=data.getIntExtra("personID",0);
                    mKgValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_SLFZR:
                    slfzrID=data.getIntExtra("personID",0);
                    mShfzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
