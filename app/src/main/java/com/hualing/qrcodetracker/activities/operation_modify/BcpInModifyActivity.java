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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.SmallCPInVerifyActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectHlProductActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectParentHlSortActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpInShowBean;
import com.hualing.qrcodetracker.bean.BcpInVerifyResult;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.NotificationType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
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

public class BcpInModifyActivity extends BaseActivity {

    //private static final int GET_WLSORT_CODE = 30;
    private static final int SELECT_LEI_BIE = 11;
    private static final int SELECT_PRODUCT_NAME = 12;
    private static final int REQUEST_CODE_SELECT_SHR = 31;
    private static final int REQUEST_CODE_SELECT_BZ = 32;
    private static final int REQUEST_CODE_SELECT_FZR = 33;
    private static final int REQUEST_CODE_SELECT_JHFZR = 34;
    private static final int REQUEST_CODE_SELECT_ZJY = 35;
    private static final int REQUEST_CODE_SELECT_ZJLD = 36;
    private static final int REQUEST_CODE_SELECT_KG = 37;
    private static final int REQUEST_CODE_SELECT_SHFZR = 38;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.indhValue)
    TextView mIndhValue;
    @BindView(R.id.jhdwValue)
    TextView mJhdwValue;
    @BindView(R.id.selectBM)
    LinearLayout mSelectBM;
    @BindView(R.id.ShrValue)
    TextView mShrValue;
    @BindView(R.id.bzValue)
    TextView mBzValue;
    @BindView(R.id.fzrValue)
    TextView mFzrValue;
    @BindView(R.id.JhFhrValue)
    TextView mJhFhrValue;
    @BindView(R.id.zjyValue)
    TextView mZjyValue;
    @BindView(R.id.zjldValue)
    TextView mZjldValue;
    @BindView(R.id.kgValue)
    TextView mKgValue;
    @BindView(R.id.ShFzrValue)
    TextView mShFzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;
    @BindView(R.id.shrLayout)
    LinearLayout mShrLayout;
    @BindView(R.id.shrView)
    View mShrView;
    @BindView(R.id.fzrLayout)
    LinearLayout mFzrLayout;
    @BindView(R.id.fzrView)
    View mFzrView;
    @BindView(R.id.jhfzrLayout)
    LinearLayout mJhfzrLayout;
    @BindView(R.id.jhfzrView)
    View mJhfzrView;
    @BindView(R.id.kgLayout)
    LinearLayout mKgLayout;
    @BindView(R.id.kgView)
    View mKgView;
    @BindView(R.id.shfzrLayout)
    LinearLayout mShfzrLayout;
    @BindView(R.id.shfzrView)
    View mShfzrView;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<BcpInShowBean> mData;
    private String mDh;
    private String mName;
    private VerifyParam param;
    private BcpInVerifyResult updatedParam;
    //记录选择物料编码或者类别的数据位置
    private int mCurrentPosition = -1;
    private int bzID;
    private int fzrID;
    private int flfzrID;
    private int zjyID;
    private int zjldID;
    private int kgID;
    private int llfzrID;

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
                AllActivitiesHolder.removeAct(BcpInModifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        updatedParam = new BcpInVerifyResult();
        param = new VerifyParam();
        Intent intent = getIntent();
        if (intent != null) {
            mName = intent.getStringExtra("name");
            mDh = intent.getStringExtra("dh");
            param.setDh(mDh);
        }

        if("半成品录入单".equals(mName)){
            mTitle.setTitle("半成品录入数据修改");
            mShrLayout.setVisibility(LinearLayout.VISIBLE);
            mShrView.setVisibility(LinearLayout.VISIBLE);
            mFzrLayout.setVisibility(LinearLayout.VISIBLE);
            mFzrView.setVisibility(View.VISIBLE);
            mJhfzrLayout.setVisibility(LinearLayout.GONE);
            mJhfzrView.setVisibility(View.GONE);
            mKgLayout.setVisibility(LinearLayout.GONE);
            mKgView.setVisibility(View.GONE);
            mShfzrLayout.setVisibility(LinearLayout.GONE);
            mShfzrView.setVisibility(View.GONE);
        }
        else{
            mTitle.setTitle("成品入库数据修改");
            mShrLayout.setVisibility(LinearLayout.GONE);
            mShrView.setVisibility(LinearLayout.GONE);
            mFzrLayout.setVisibility(LinearLayout.GONE);
            mFzrView.setVisibility(View.GONE);
            mJhfzrLayout.setVisibility(LinearLayout.VISIBLE);
            mJhfzrView.setVisibility(View.VISIBLE);
            mKgLayout.setVisibility(LinearLayout.VISIBLE);
            mKgView.setVisibility(View.VISIBLE);
            mShfzrLayout.setVisibility(LinearLayout.VISIBLE);
            mShfzrView.setVisibility(View.VISIBLE);
        }
        param.setName(mName);

        mData = new ArrayList<>();
        mAdapter = new MyAdapter();
        mChildDataList.setAdapter(mAdapter);
        mChildDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BcpInVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BcpInVerifyResult>> e) throws Exception {
                ActionResult<BcpInVerifyResult> nr = mainDao.getBcpInVerifyData(param);
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
                            updatedParam = dataResult;
                            mIndhValue.setText(dataResult.getInDh());
                            mJhdwValue.setText(dataResult.getJhDw());
                            mJhdwValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setJhDw("" + s);
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

                            if("半成品录入单".equals(mName)) {
                                mShrValue.setText(dataResult.getShR());
                                mShrValue.addTextChangedListener(new TextWatcher() {
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

                                fzrID = dataResult.getFzrID();
                                mFzrValue.setText(dataResult.getFzr());
                                mFzrValue.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        updatedParam.setFzr("" + s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }
                            else{
                                flfzrID=dataResult.getFlfzrID();
                                mJhFhrValue.setText(dataResult.getJhFzr());
                                mJhFhrValue.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        updatedParam.setJhFzr("" + s);
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

                                llfzrID=dataResult.getLlfzrID();
                                mShFzrValue.setText(dataResult.getShFzr());
                                mShFzrValue.addTextChangedListener(new TextWatcher() {
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

                            }

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

    private void toCommit() {
        if("半成品录入单".equals(mName)) {
            for (int i = 0; i < mData.size(); i++) {

                if (TextUtils.isEmpty(mData.get(i).getProductName())
                        || TextUtils.isEmpty(mData.get(i).getdW())
                        //|| TextUtils.isEmpty(mData.get(i).getgG())
                        //|| "请选择编码".equals(mData.get(i).getwLCode())
                        || TextUtils.isEmpty(mData.get(i).getyLPC())
                        || mData.get(i).getSortID() < 0
                        || mData.get(i).getdWZL() == -1
                        //|| mData.get(i).getShl() == -1
                        || "请选择收货人".equals(mShrValue.getText().toString())
                        || "请选择班长".equals(mBzValue.getText().toString())
                        || "请选择车间领导".equals(mFzrValue.getText().toString())
                        || "请选择质检员".equals(mZjyValue.getText().toString())
                        || "请选择质检领导".equals(mZjldValue.getText().toString())
                        ) {
                    Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            updatedParam.setBeans(mData);
            updatedParam.setBzID(bzID);
            updatedParam.setBzStatus(0);
            updatedParam.setFzrID(fzrID);
            updatedParam.setFzrStatus(0);
            updatedParam.setZjyID(zjyID);
            updatedParam.setZjyStatus(0);
            updatedParam.setZjldID(zjldID);
            updatedParam.setZjldStatus(0);
        }
        else{
            for (int i = 0; i < mData.size(); i++) {

                if (TextUtils.isEmpty(mData.get(i).getProductName())
                        || TextUtils.isEmpty(mData.get(i).getdW())
                        //|| TextUtils.isEmpty(mData.get(i).getgG())
                        //|| "请选择编码".equals(mData.get(i).getwLCode())
                        || TextUtils.isEmpty(mData.get(i).getyLPC())
                        || mData.get(i).getSortID() < 0
                        || mData.get(i).getdWZL() == -1
                        //|| mData.get(i).getShl() == -1
                        || "请选择班长".equals(mBzValue.getText().toString())
                        || "请选择入库负责人".equals(mJhFhrValue.getText().toString())
                        || "请选择质检员".equals(mZjyValue.getText().toString())
                        || "请选择质检领导".equals(mZjldValue.getText().toString())
                        || "请选择仓库管理员".equals(mKgValue.getText().toString())
                        || "请选择收货负责人".equals(mShFzrValue.getText().toString())
                        ) {
                    Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            updatedParam.setBeans(mData);
            updatedParam.setBzID(bzID);
            updatedParam.setBzStatus(0);
            updatedParam.setFlfzrID(flfzrID);
            updatedParam.setFlfzrStatus(0);
            updatedParam.setZjyID(zjyID);
            updatedParam.setZjyStatus(0);
            updatedParam.setZjldID(zjldID);
            updatedParam.setZjldStatus(0);
            updatedParam.setKgID(kgID);
            updatedParam.setKgStatus(0);
            updatedParam.setLlfzrID(llfzrID);
            updatedParam.setLlfzrStatus(0);
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.toUpdateBcpInData(updatedParam);
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
        //String dh = SharedPreferenceUtil.getBCPRKDNumber();
        notificationParam.setDh(mDh);
        if("半成品录入单".equals(mName))
            notificationParam.setStyle(NotificationType.BCP_RKD);
        else
            notificationParam.setStyle(NotificationType.CP_RKD);
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
                        AllActivitiesHolder.removeAct(BcpInModifyActivity.this);
                    }
                });

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_in_modify;
    }

    @OnClick({R.id.selectSHR,R.id.selectBZ,R.id.selectFZR,R.id.selectJHFZR,R.id.selectZJY,R.id.selectZJLD,R.id.selectKG,R.id.selectSHFZR,R.id.confirmBtn})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.selectSHR:
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class,REQUEST_CODE_SELECT_SHR,null);
                break;
            case R.id.selectBZ:
                bundle.putString("checkQX", "bz");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class,REQUEST_CODE_SELECT_BZ,bundle);
                break;
            case R.id.selectFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class,REQUEST_CODE_SELECT_FZR,bundle);
                break;
            case R.id.selectJHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class,REQUEST_CODE_SELECT_JHFZR,bundle);
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
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class,REQUEST_CODE_SELECT_KG,bundle);
                break;
            case R.id.selectSHFZR:
                bundle.putString("checkQX", "fzr");
                IntentUtil.openActivityForResult(this, SelectPersonGroupActivity.class,REQUEST_CODE_SELECT_SHFZR,bundle);
                break;
            case R.id.confirmBtn:
                toCommit();
                break;
        }
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
                convertView = View.inflate(BcpInModifyActivity.this, R.layout.item_bcpin_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final BcpInShowBean bean = mData.get(position);
            /*
            if (!TextUtils.isEmpty(bean.getwLCode())) {
                viewHolder.mBcpCodeValue.setText(bean.getwLCode());
            }
            */
            viewHolder.mProductNameValue.setText(bean.getProductName());
            /*
            viewHolder.mProductNameValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setProductName("" + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            */
            viewHolder.mSelectedLeiBieId=bean.getSortID();
            viewHolder.mLbValue.setText(bean.getSortName());
            viewHolder.mGgValue.setText(bean.getgG());
            /*
            viewHolder.mGgValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setgG("" + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            */
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mYlpcValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setyLPC("" + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mDwValue.setText(bean.getdW());
            viewHolder.mDwValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setdW("" + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            /*
            viewHolder.mShlValue.setText(bean.getShl() + "");
            viewHolder.mShlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat("" + s);
                        bean.setShl(num);
                    } else {
                        bean.setShl(-1);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            */
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");
            viewHolder.mDwzlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat("" + s);
                        bean.setdWZL(num);
                        bean.setrKZL(num);
                        bean.setsYZL(num);
                        bean.setShl(1);
                    } else {
                        bean.setShl(-1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            /*
            viewHolder.mSelectBCPCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.openActivityForResult(BcpInModifyActivity.this, SelectParentHlSortActivity.class, GET_WLSORT_CODE, null);
                    mCurrentPosition = position;
                }
            });
            */

            viewHolder.mSelectLB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.openActivityForResult(BcpInModifyActivity.this, SelectParentHlSortActivity.class, SELECT_LEI_BIE, null);
                    mCurrentPosition = position;
                }
            });

            viewHolder.mSelectProductName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("sortID",viewHolder.mSelectedLeiBieId);
                    IntentUtil.openActivityForResult(BcpInModifyActivity.this, SelectHlProductActivity.class, SELECT_PRODUCT_NAME, bundle);
                    mCurrentPosition = position;
                }
            });

            final String qRCodeID = bean.getqRCodeID();
            if("4".equals(qRCodeID.substring(8,9))){
                viewHolder.goSmallLayout.setVisibility(View.VISIBLE);
                viewHolder.goSmallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("qRCodeID",qRCodeID);
                        IntentUtil.openActivityForResult(BcpInModifyActivity.this, SmallCPInVerifyActivity.class, -1, bundle);
                    }
                });
            }
            else{
                viewHolder.goSmallLayout.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ViewHolder {
            //@BindView(R.id.bcpCodeValue)
            //TextView mBcpCodeValue;
            //@BindView(R.id.selectBCPCode)
            //LinearLayout mSelectBCPCode;
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.selectLB)
            LinearLayout mSelectLB;
            @BindView(R.id.productNameValue)
            TextView mProductNameValue;
            @BindView(R.id.selectProductName)
            LinearLayout mSelectProductName;
            @BindView(R.id.ylpcValue)
            EditText mYlpcValue;
            @BindView(R.id.ggValue)
            EditText mGgValue;
            //@BindView(R.id.shlValue)
            //EditText mShlValue;
            Float shl;
            @BindView(R.id.dwzlValue)
            EditText mDwzlValue;
            @BindView(R.id.dwValue)
            EditText mDwValue;
            @BindView(R.id.goSmallLayout)
            LinearLayout goSmallLayout;
            @BindView(R.id.goSmallBtn)
            Button goSmallBtn;
            @BindView(R.id.goSmallView)
            View goSmallView;
            int mSelectedLeiBieId = -1;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                /*
                case GET_WLSORT_CODE:
                    String sortName = data.getStringExtra("sortName");
                    String sortCode = data.getStringExtra("sortCode");
                    if (mCurrentPosition != -1) {
                        mData.get(mCurrentPosition).setwLCode(sortCode);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                    */
                case SELECT_LEI_BIE:
                    if (mCurrentPosition != -1) {
                        int sortID = data.getIntExtra("sortID", -1);
                        String sortName = data.getStringExtra("sortName");
                        BcpInShowBean item = mData.get(mCurrentPosition);
                        item.setSortID(sortID);
                        item.setSortName(sortName);
                        mData.remove(mCurrentPosition);
                        mData.add(mCurrentPosition,item);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case SELECT_PRODUCT_NAME:
                    if (mCurrentPosition != -1) {
                        String productName = data.getStringExtra("productName");
                        String model = data.getStringExtra("model");
                        BcpInShowBean item = mData.get(mCurrentPosition);
                        item.setProductName(productName);
                        item.setgG(model);
                        mData.remove(mCurrentPosition);
                        mData.add(mCurrentPosition, item);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case REQUEST_CODE_SELECT_SHR:
                    mShrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_BZ:
                    bzID=data.getIntExtra("personID",0);
                    mBzValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_FZR:
                    fzrID=data.getIntExtra("personID",0);
                    mFzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_JHFZR:
                    flfzrID=data.getIntExtra("personID",0);
                    mJhFhrValue.setText(data.getStringExtra("personName"));
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
                case REQUEST_CODE_SELECT_SHFZR:
                    llfzrID=data.getIntExtra("personID",0);
                    mShFzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
