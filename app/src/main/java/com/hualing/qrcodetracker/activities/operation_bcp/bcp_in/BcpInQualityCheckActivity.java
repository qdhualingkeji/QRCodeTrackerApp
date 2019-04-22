package com.hualing.qrcodetracker.activities.operation_bcp.bcp_in;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonGroupActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_in.SmallCPInDataInputActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpInQualityCheckResult;
import com.hualing.qrcodetracker.bean.BcpInShowBean;
import com.hualing.qrcodetracker.bean.BcpInVerifyResult;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.NotificationType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.MyListView;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class BcpInQualityCheckActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.indhValue)
    TextView mIndhValue;
    @BindView(R.id.jhdwValue)
    TextView mJhdwValue;
    @BindView(R.id.shrqValue)
    TextView mShrqValue;
    @BindView(R.id.shfzrValue)
    TextView mShfzrValue;
    @BindView(R.id.jhRValue)
    TextView mJhRValue;
    @BindView(R.id.jhfzrValue)
    TextView mJhfzrValue;
    @BindView(R.id.remarkValue)
    TextView mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private BcpInQualityCheckActivity.MyAdapter mAdapter;
    private List<BcpInShowBean> mData;
    private String mDh;
    private VerifyParam param;
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
                AllActivitiesHolder.removeAct(BcpInQualityCheckActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new VerifyParam();
        if (getIntent() != null) {
            String[] checkQXArr = GlobalData.checkQXGroup.split(",");
            for (String checkQX:checkQXArr) {
                if("zjy".equals(checkQX)){
                    isZJY=true;
                    break;
                }
                else if("zjld".equals(checkQX)){
                    isZJLD=true;
                    break;
                }
            }
            //下面是根据身份判断当前是质检还是审核
            int checkQXFlag=-1;
            if(isZJY)
                checkQXFlag=VerifyParam.ZJY;
            else if(isZJLD)
                checkQXFlag=VerifyParam.ZJLD;
            param.setCheckQXFlag(checkQXFlag);

            mDh = getIntent().getStringExtra("dh");
            param.setDh(mDh);
        }

        mData = new ArrayList<>();
        mAdapter = new BcpInQualityCheckActivity.MyAdapter();
        mChildDataList.setAdapter(mAdapter);
        mChildDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BcpInQualityCheckResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BcpInQualityCheckResult>> e) throws Exception {
                ActionResult<BcpInQualityCheckResult> nr = mainDao.getBcpInQualityCheckData(param);
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
                            mIndhValue.setText(dataResult.getInDh());
                            mJhdwValue.setText(dataResult.getJhDw());
                            mShrqValue.setText(dataResult.getShRq());
                            mShfzrValue.setText(dataResult.getShFzr());
                            mJhfzrValue.setText(dataResult.getJhFzr());
                            mJhRValue.setText(dataResult.getJhR());
                            mRemarkValue.setText(TextUtils.isEmpty(dataResult.getRemark())?"无备注信息":dataResult.getRemark());

                            if (dataResult.getBeans() != null && dataResult.getBeans().size() > 0) {
                                mData.clear();
                                mData.addAll(dataResult.getBeans());
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        if(isZJY||isZJLD) {
                            JSONArray bcpInShowJA = new JSONArray();
                            JSONObject bcpInShowJO=null;
                            Log.e("mData===",""+mData.size());
                            for(BcpInShowBean bcpInShow : mData){
                                Log.e("getqRCodeID===",bcpInShow.getqRCodeID());
                                //bcpInShow.setZjy(GlobalData.realName);
                                bcpInShowJO = new JSONObject();
                                bcpInShowJO.put("qRCodeID",bcpInShow.getqRCodeID());
                                bcpInShowJO.put("zjy",GlobalData.realName);
                                bcpInShowJO.put("zjzt",bcpInShow.getZjzt());
                                bcpInShowJA.add(bcpInShowJO);
                            }
                            Log.e("bcpInShowJA===",""+bcpInShowJA.toString());
                            param.setBcpInShowJAStr(bcpInShowJA.toString());
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
                ActionResult<ActionResult> nr = mainDao.toRefuseBcpIn(param);
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
                            AllActivitiesHolder.removeAct(BcpInQualityCheckActivity.this);
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
                ActionResult<ActionResult> nr = mainDao.toAgreeBcpIn(param);
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
                            Toast.makeText(TheApplication.getContext(), "质检已通过", Toast.LENGTH_SHORT).show();
                            if(param.getCheckQXFlag()==VerifyParam.ZJY)//如果登录者是班长的话，说明还得推送给领料负责人;如果登录者是发料负责人的话，说明还得推送给质检员;
                                sendNotification(param.getCheckQXFlag());
                            else{//不是的话，说明登录者就是质检领导，最后一道审核就不必再推送了
                                setResult(RETURN_AND_REFRESH);
                                AllActivitiesHolder.removeAct(BcpInQualityCheckActivity.this);
                            }
                            return;
                        }
                    }
                });
    }

    private void sendNotification(Integer checkQXFlag) {

        final NotificationParam notificationParam = new NotificationParam();
        //根据单号去查找审核人
        notificationParam.setDh(param.getDh());
        notificationParam.setStyle(NotificationType.BCP_RKD);
        int personFlag=-1;
        String notifText=null;
        if(checkQXFlag==VerifyParam.ZJY){
            personFlag=NotificationParam.ZJLD;
            notifText="已通知质检领导质检";
        }
        notificationParam.setPersonFlag(personFlag);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        final String finalNotifText = notifText;
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
                            Toast.makeText(TheApplication.getContext(), finalNotifText, Toast.LENGTH_SHORT).show();
                        }
                        setResult(RETURN_AND_REFRESH);
                        AllActivitiesHolder.removeAct(BcpInQualityCheckActivity.this);
                    }
                });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_in_quality_check;
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
            final BcpInQualityCheckActivity.MyAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(BcpInQualityCheckActivity.this, R.layout.item_cp_quality_check, null);
                viewHolder = new BcpInQualityCheckActivity.MyAdapter.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (BcpInQualityCheckActivity.MyAdapter.ViewHolder) convertView.getTag();

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

            List<Map<String, String>> jyztList = new ArrayList<Map<String, String>>();
            Map<String, String> jyztMap = new HashMap<String, String>();
            jyztMap.put("name", "合格");
            jyztMap.put("id", "1");
            jyztList.add(jyztMap);
            jyztMap = new HashMap<String, String>();
            jyztMap.put("name", "不合格");
            jyztMap.put("id", "0");
            jyztList.add(jyztMap);
            viewHolder.mZjztAdapter = new SimpleAdapter(BcpInQualityCheckActivity.this,jyztList,R.layout.item_shen_fen,new String[]{"name","id"},new int[]{R.id.nameValue,R.id.idValue});
            viewHolder.mZjztSpinner.setAdapter(viewHolder.mZjztAdapter);
            if(isZJY) {
                viewHolder.mZjztSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int jyztPosition, long id) {
                        //mData.get(position).setZjzt(jyztPosition);
                        String bcpInShowJAStr = param.getBcpInShowJAStr();
                        JSONArray bcpInShowJA = JSONArray.parseArray(bcpInShowJAStr);
                        JSONObject bcpInShowJO = (JSONObject) bcpInShowJA.get(position);
                        Map<String, String> selectedItemMap = (Map<String, String>) viewHolder.mZjztSpinner.getSelectedItem();
                        bcpInShowJO.put("zjzt", selectedItemMap.get("id"));
                        param.setBcpInShowJAStr(bcpInShowJA.toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            else if(isZJLD){
                String bcpInShowJAStr = param.getBcpInShowJAStr();
                JSONArray bcpInShowJA = JSONArray.parseArray(bcpInShowJAStr);
                JSONObject bcpInShowJO = (JSONObject) bcpInShowJA.get(position);
                viewHolder.mZjztSpinner.setSelection(1-bcpInShowJO.getInteger("zjzt"));
                viewHolder.mZjztSpinner.setEnabled(false);
            }

            final String qRCodeID = bean.getqRCodeID();
            if("4".equals(qRCodeID.substring(8,9))){
                viewHolder.mBcpCjLayout.setVisibility(View.GONE);
                viewHolder.mBcpCjView.setVisibility(View.GONE);

                viewHolder.mBcpGxLayout.setVisibility(View.GONE);
                viewHolder.mBcpGxView.setVisibility(View.GONE);

                viewHolder.goSmallBtn.setVisibility(View.VISIBLE);
                viewHolder.goSmallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("qRCodeID",qRCodeID);
                        IntentUtil.openActivityForResult(BcpInQualityCheckActivity.this, SmallCPInQualityCheckActivity.class, -1, bundle);
                    }
                });
            }
            else{
                viewHolder.mBcpCjLayout.setVisibility(View.VISIBLE);
                viewHolder.mBcpCjView.setVisibility(View.VISIBLE);

                viewHolder.mBcpGxLayout.setVisibility(View.VISIBLE);
                viewHolder.mBcpGxView.setVisibility(View.VISIBLE);

                viewHolder.goSmallBtn.setVisibility(View.GONE);
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
            SimpleAdapter mZjztAdapter;
            @BindView(R.id.zjztSpinner)
            Spinner mZjztSpinner;
            @BindView(R.id.goSmallBtn)
            Button goSmallBtn;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
