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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.BcpInQualityCheckActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.SmallCPInVerifyActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectHlProductActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectHlSortActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectLBActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpInShowBean;
import com.hualing.qrcodetracker.bean.BcpInVerifyResult;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.bean.WLINShowBean;
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

public class BcpInModifyActivity extends BaseActivity {

    //private static final int GET_WLSORT_CODE = 30;
    private static final int SELECT_LEI_BIE = 11;
    private static final int SELECT_PRODUCT_NAME = 12;
    private static final int REQUEST_CODE_SELECT_SHFZR = 31;
    private static final int REQUEST_CODE_SELECT_JHFZR = 32;
    private static final int REQUEST_CODE_SELECT_SHR = 33;
    private static final int REQUEST_CODE_SELECT_ZJY = 34;
    private static final int REQUEST_CODE_SELECT_BZ = 35;
    private static final int REQUEST_CODE_SELECT_ZJLD = 36;
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
    @BindView(R.id.ShFzrValue)
    TextView mShFzrValue;
    @BindView(R.id.JhFhrValue)
    TextView mJhFhrValue;
    @BindView(R.id.zjyValue)
    TextView mZjyValue;
    @BindView(R.id.zjldValue)
    TextView mZjldValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<BcpInShowBean> mData;
    private String mDh;
    private VerifyParam param;
    private BcpInVerifyResult updatedParam;
    //记录选择物料编码或者类别的数据位置
    private int mCurrentPosition = -1;
    private int bzID;
    private int fzrID;
    private int zjyID;
    private int zjldID;

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
                            mBzValue.setText(dataResult.getBzName());

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

                            fzrID=dataResult.getFzrID();
                            zjyID=dataResult.getZjyID();
                            mZjyValue.setText(dataResult.getZjyName());

                            zjldID=dataResult.getZjldID();
                            mZjldValue.setText(dataResult.getZjldName());

                            mShrValue.setText(dataResult.getJhR());
                            mShrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setJhR("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
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

        for (int i = 0; i < mData.size(); i++) {

            if (TextUtils.isEmpty(mData.get(i).getProductName())
                    || TextUtils.isEmpty(mData.get(i).getdW())
                    //|| TextUtils.isEmpty(mData.get(i).getgG())
                    //|| "请选择编码".equals(mData.get(i).getwLCode())
                    || TextUtils.isEmpty(mData.get(i).getyLPC())
                    || mData.get(i).getSortID() < 0
                    || mData.get(i).getdWZL() == -1
                    || mData.get(i).getShl() == -1
                    || "请选择收货人".equals(mShrValue.getText().toString())
                    || "请选择收货负责人".equals(mShFzrValue.getText().toString())
                    || "请选择入库负责人".equals(mJhFhrValue.getText().toString())
                    ) {
                Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        updatedParam.setBeans(mData);
        updatedParam.setBzStatus(0);
        updatedParam.setFzrStatus(0);
        updatedParam.setZjyStatus(0);
        updatedParam.setZjldStatus(0);

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
        String dh = SharedPreferenceUtil.getBCPRKDNumber();
        notificationParam.setDh(dh);
        notificationParam.setStyle(NotificationType.BCP_RKD);
        notificationParam.setPersonFlag(NotificationParam.ZJY);

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

    @OnClick({R.id.confirmBtn,R.id.selectSHR,R.id.selectBZ,R.id.selectSHFZR,R.id.selectJHFZR,R.id.selectZJY,R.id.selectZJLD})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.selectSHR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class,REQUEST_CODE_SELECT_SHR,null);
                break;
            case R.id.selectBZ:
                bundle.putString("checkQX", "bz");
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class,REQUEST_CODE_SELECT_BZ,bundle);
                break;
            case R.id.selectSHFZR:
                bundle.putString("checkQX", "ld");
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class,REQUEST_CODE_SELECT_SHFZR,bundle);
                break;
            case R.id.selectJHFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class,REQUEST_CODE_SELECT_JHFZR,null);
                break;
            case R.id.selectZJY:
                bundle.putString("checkQX", "zjy");
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_ZJY, bundle);
                break;
            case R.id.selectZJLD:
                bundle.putString("checkQX", "zjld");
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_ZJLD, bundle);
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
                    IntentUtil.openActivityForResult(BcpInModifyActivity.this, SelectHlSortActivity.class, GET_WLSORT_CODE, null);
                    mCurrentPosition = position;
                }
            });
            */

            viewHolder.mSelectLB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.openActivityForResult(BcpInModifyActivity.this, SelectHlSortActivity.class, SELECT_LEI_BIE, null);
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
                viewHolder.goSmallBtn.setVisibility(View.VISIBLE);
                viewHolder.goSmallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("qRCodeID",qRCodeID);
                        IntentUtil.openActivityForResult(BcpInModifyActivity.this, SmallCPInVerifyActivity.class, -1, bundle);
                    }
                });
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
            @BindView(R.id.shlValue)
            EditText mShlValue;
            @BindView(R.id.dwzlValue)
            EditText mDwzlValue;
            @BindView(R.id.dwValue)
            EditText mDwValue;
            @BindView(R.id.goSmallBtn)
            Button goSmallBtn;
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
                case REQUEST_CODE_SELECT_SHFZR:
                    fzrID=data.getIntExtra("personID",0);
                    mShFzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_JHFZR:
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
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
