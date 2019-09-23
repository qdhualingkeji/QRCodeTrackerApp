package com.hualing.qrcodetracker.activities.operation_modify;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.hualing.qrcodetracker.bean.BcpOutShowBean;
import com.hualing.qrcodetracker.bean.BcpOutVerifyResult;
import com.hualing.qrcodetracker.bean.CpOutShowBean;
import com.hualing.qrcodetracker.bean.CpOutVerifyResult;
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

public class BcpOutModifyActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_KG = 31;
    private static final int REQUEST_CODE_SELECT_FHFZR = 32;
    private static final int REQUEST_CODE_SELECT_BZ = 33;
    private static final int REQUEST_CODE_SELECT_LHFZR = 34;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.outdhValue)
    TextView mOutdhValue;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.fhfzrValue)
    TextView mFhfzrValue;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.lhfzrValue)
    TextView mLhfzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;
    @BindView(R.id.bzLayout)
    LinearLayout mBzLayout;
    @BindView(R.id.bzView)
    View mBzView;
    @BindView(R.id.lhfzrLayout)
    LinearLayout mLhfzrLayout;
    @BindView(R.id.lhfzrView)
    View mLhfzrView;

    private MainDao mainDao;
    private MyBcpAdapter mBcpAdapter;
    private List<BcpOutShowBean> mBcpData;
    private MyCpAdapter mCpAdapter;
    private List<CpOutShowBean> mCpData;
    private String mDh;
    private String mName;
    private VerifyParam param;
    private BcpOutVerifyResult updatedBcpParam;
    private CpOutVerifyResult updatedCpParam;
    private Integer kgID;
    private Integer fzrID;
    private Integer bzID;
    private Integer llfzrID;

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
                AllActivitiesHolder.removeAct(BcpOutModifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new VerifyParam();
        Intent intent = getIntent();
        if (intent != null) {
            mName = intent.getStringExtra("name");
            mDh = getIntent().getStringExtra("dh");
            param.setDh(mDh);
        }

        if("半成品出库单".equals(mName)){
            updatedBcpParam = new BcpOutVerifyResult();
            mTitle.setTitle("半成品出库数据修改");
            mBzLayout.setVisibility(View.VISIBLE);
            mBzView.setVisibility(View.VISIBLE);
            mLhfzrLayout.setVisibility(View.VISIBLE);
            mLhfzrView.setVisibility(View.VISIBLE);

            mBcpData = new ArrayList<>();
            mBcpAdapter = new BcpOutModifyActivity.MyBcpAdapter();
            mChildDataList.setAdapter(mBcpAdapter);
        }
        else{
            updatedCpParam = new CpOutVerifyResult();
            mTitle.setTitle("成品出库数据修改");
            mBzLayout.setVisibility(View.GONE);
            mBzView.setVisibility(View.GONE);
            mLhfzrLayout.setVisibility(View.GONE);
            mLhfzrView.setVisibility(View.GONE);

            mCpData = new ArrayList<>();
            mCpAdapter = new BcpOutModifyActivity.MyCpAdapter();
            mChildDataList.setAdapter(mCpAdapter);
        }
        mChildDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        if("半成品出库单".equals(mName)){
            Observable.create(new ObservableOnSubscribe<ActionResult<BcpOutVerifyResult>>() {
                @Override
                public void subscribe(ObservableEmitter<ActionResult<BcpOutVerifyResult>> e) throws Exception {
                    ActionResult<BcpOutVerifyResult> nr = mainDao.getBcpOutVerifyData(param);
                    e.onNext(nr);
                }
            }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                    .subscribe(new Consumer<ActionResult<BcpOutVerifyResult>>() {
                        @Override
                        public void accept(ActionResult<BcpOutVerifyResult> result) throws Exception {
                            progressDialog.dismiss();
                            if (result.getCode() != 0) {
                                Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                BcpOutVerifyResult dataResult = result.getResult();
                                updatedBcpParam = dataResult;
                                mOutdhValue.setText(dataResult.getOutDh());

                                kgID = dataResult.getKgID();
                                mKgValue.setText(dataResult.getKg());
                                mKgValue.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        updatedBcpParam.setKg("" + s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                fzrID = dataResult.getFzrID();
                                mFhfzrValue.setText(dataResult.getFhFzr());
                                mFhfzrValue.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        updatedBcpParam.setFhFzr("" + s);
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
                                        updatedBcpParam.setBz("" + s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                llfzrID = dataResult.getLlfzrID();
                                mLhfzrValue.setText(dataResult.getLhFzr());
                                mLhfzrValue.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        updatedBcpParam.setLhFzr("" + s);
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
                                        updatedBcpParam.setRemark("" + s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                if (dataResult.getBeans() != null && dataResult.getBeans().size() > 0) {
                                    mBcpData.clear();
                                    mBcpData.addAll(dataResult.getBeans());
                                    mBcpAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
        else {
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
                                updatedCpParam = dataResult;
                                mOutdhValue.setText(dataResult.getOutDh());

                                kgID = dataResult.getKgID();
                                mKgValue.setText(dataResult.getKg());
                                mKgValue.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        updatedCpParam.setKg("" + s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                fzrID = dataResult.getFzrID();
                                mFhfzrValue.setText(dataResult.getFhFzr());
                                mFhfzrValue.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        updatedCpParam.setFhFzr("" + s);
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
                                        updatedCpParam.setRemark("" + s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                if (dataResult.getBeans() != null && dataResult.getBeans().size() > 0) {
                                    mCpData.clear();
                                    mCpData.addAll(dataResult.getBeans());
                                    mCpAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_out_modify;
    }

    @OnClick({R.id.selectKG,R.id.selectFHFZR,R.id.selectBz,R.id.selectLHFZR,R.id.confirmBtn})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.selectKG:
                bundle.putString("checkQX", "kg");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_KG, bundle);
                break;
            case R.id.selectFHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_FHFZR, bundle);
                break;
            case R.id.selectBz:
                bundle.putString("checkQX", "bz");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_BZ, bundle);
                break;
            case R.id.selectLHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class, REQUEST_CODE_SELECT_LHFZR, bundle);
                break;
            case R.id.confirmBtn:
                toCommit();
                break;
        }

    }

    private void toCommit() {

        if("半成品出库单".equals(mName)) {
            if ("请选择仓库管理员".equals(mKgValue.getText().toString())
                    || "请选择发货负责人".equals(mFhfzrValue.getText().toString())
                    || "请选择班长".equals(mBzValue.getText().toString())
                    || "请选择领货负责人".equals(mLhfzrValue.getText().toString())
                    ) {
                Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < mBcpData.size(); i++) {
                if (mBcpData.get(i).getcKZL() > mBcpData.get(i).getsYZL() + mBcpData.get(i).getcKZL1()) {
                    Toast.makeText(this, "出库重量不能大于库存重量" + (mBcpData.get(i).getsYZL() + mBcpData.get(i).getcKZL1()) + mBcpData.get(i).getDw(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            updatedBcpParam.setBeans(mBcpData);
            updatedBcpParam.setKgID(kgID);
            updatedBcpParam.setKgStatus(0);
            updatedBcpParam.setFzrID(fzrID);
            updatedBcpParam.setFzrStatus(0);
            updatedBcpParam.setBzID(bzID);
            updatedBcpParam.setBzStatus(0);
            updatedBcpParam.setLlfzrID(llfzrID);
            updatedBcpParam.setLlfzrStatus(0);
            updatedBcpParam.setRemark(mRemarkValue.getText().toString());
        }
        else{
            if ("请选择仓库管理员".equals(mKgValue.getText().toString())
                    || "请选择发货负责人".equals(mFhfzrValue.getText().toString())
                    ) {
                Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }
            updatedCpParam.setBeans(mCpData);
            updatedCpParam.setKgID(kgID);
            updatedCpParam.setKgStatus(0);
            updatedCpParam.setFzrID(fzrID);
            updatedCpParam.setFzrStatus(0);
            updatedCpParam.setRemark(mRemarkValue.getText().toString());
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = null;
                if("半成品出库单".equals(mName)) {
                    nr = mainDao.toUpdateBcpOutData(updatedBcpParam);
                }
                else{
                    nr = mainDao.toUpdateCpOutData(updatedCpParam);
                }
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
        //String dh = SharedPreferenceUtil.getBCPCKDNumber();
        notificationParam.setDh(mDh);
        if("半成品出库单".equals(mName)) {
            notificationParam.setStyle(NotificationType.BCP_CKD);
        }
        else {
            notificationParam.setStyle(NotificationType.CP_CKD);
        }
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
                        AllActivitiesHolder.removeAct(BcpOutModifyActivity.this);
                    }
                });

    }

    class MyBcpAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBcpData.size();
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
                convertView = View.inflate(BcpOutModifyActivity.this, R.layout.item_bcpout_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final BcpOutShowBean bean = mBcpData.get(position);
            viewHolder.mNameValue.setText(bean.getProductName());
            //viewHolder.mLbValue.setText(bean.getSortID() + "");
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.shl = bean.getShl();
            viewHolder.rKZL = bean.getrKZL();
            viewHolder.mRkzlValue.setText(bean.getrKZL()+"");
            viewHolder.mDwzlValue.setText(bean.getdWZL()+"");
            viewHolder.mSyzlValue.setText(bean.getsYZL()+"");
            viewHolder.mCkzlValue.setText(bean.getcKZL()+"");
            viewHolder.mCkzlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat("" + s);
                        bean.setcKZL(num);
                        bean.setShl(num/viewHolder.rKZL);
                    } else {
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
            @BindView(R.id.nameValue)
            TextView mNameValue;
            //@BindView(R.id.lbValue)
            //TextView mLbValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.rkzlValue)
            TextView mRkzlValue;
            @BindView(R.id.dwzlValue)
            TextView mDwzlValue;
            @BindView(R.id.syzlValue)
            TextView mSyzlValue;
            @BindView(R.id.ckzlValue)
            EditText mCkzlValue;
            Float shl;
            Float rKZL;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    class MyCpAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCpData.size();
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
                convertView = View.inflate(BcpOutModifyActivity.this, R.layout.item_cpout_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final CpOutShowBean bean = mCpData.get(position);
            viewHolder.mNameValue.setText(bean.getCpName());
            //viewHolder.mLbValue.setText(bean.getSortID() + "");
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mScpcValue.setText(bean.getsCPC());
            viewHolder.mScTimeValue.setText(bean.getfHDate());

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

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_KG:
                    kgID=data.getIntExtra("personID",0);
                    mKgValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_FHFZR:
                    fzrID=data.getIntExtra("personID",0);
                    mFhfzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_BZ:
                    bzID=data.getIntExtra("personID",0);
                    mBzValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_LHFZR:
                    llfzrID=data.getIntExtra("personID",0);
                    mLhfzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
