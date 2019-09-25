package com.hualing.qrcodetracker.activities.operation_common;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.GetSXYLParam;
import com.hualing.qrcodetracker.bean.SXYLResult;
import com.hualing.qrcodetracker.bean.TLYLBean;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.TrackType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.widget.MyRecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SelectSXYLActivity extends BaseActivity {

    @BindView(R.id.inputValue)
    EditText mInputValue;
    @BindView(R.id.jiSuanTLZL)
    Button mJiSuanTLZL;
    @BindView(R.id.dataList)
    RecyclerView mRecyclerView;
    @BindView(R.id.selectAll)
    CheckBox mSelectAll;

    private MyAdapter mAdapter;
    private List<TLYLBean> mData;
    private List<TLYLBean> mLocalData;
    //模糊过滤后的数据
    private List<TLYLBean> mFilterData;
    private MainDao mainDao;

    //车间包含的工序id
    private int mSelectedGxId;
    private String mTrackType;
    private String mAction;
    private String mAllYlQrCode;
    private String mAllYlTlzl;
    private float dwzl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mData = new ArrayList<>();
        mLocalData = new ArrayList<>();
        mFilterData = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.divide_gray_color)));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mInputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mJiSuanTLZL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mFilterData.size();
                int selectedCount=0;
                for(int i=0;i<size;i++){
                    TLYLBean tlylBean = mFilterData.get(i);
                    if(tlylBean.getFlag())
                        selectedCount++;
                }

                if(selectedCount==0){
                    Toast.makeText(SelectSXYLActivity.this, "请选择所需原料", Toast.LENGTH_SHORT).show();
                }
                else {
                    float tlzl = dwzl / selectedCount;
                    for(int i=0;i<size;i++){
                        TLYLBean tlylBean = mFilterData.get(i);
                        if(tlylBean.getFlag()) {
                            tlylBean.setTlzl(tlzl);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        mSelectedGxId = bundle.getInt("selectedGxId");
        mTrackType = bundle.getString("trackType");
        dwzl = bundle.getFloat("dwzl",0);
        mAction = bundle.getString("action");
        mAllYlQrCode = bundle.getString("allYlQrCode");
        mAllYlTlzl = bundle.getString("allYlTlzl");

        mSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < mFilterData.size(); i++) {
                        mFilterData.get(i).setFlag(true);
                    }
                } else {
                    for (int i = 0; i < mFilterData.size(); i++) {
                        mFilterData.get(i).setFlag(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void getDataFormWeb() {
        mainDao = YoniClient.getInstance().create(MainDao.class);
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        final GetSXYLParam param = new GetSXYLParam();
        param.setGxId(mSelectedGxId);
        param.setTrackType(mTrackType);

        Observable.create(new ObservableOnSubscribe<ActionResult<SXYLResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<SXYLResult>> e) throws Exception {
                ActionResult<SXYLResult> nr = mainDao.getSXYL(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<SXYLResult>>() {
                    @Override
                    public void accept(ActionResult<SXYLResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            SXYLResult data = result.getResult();
                            List<TLYLBean> beans = data.getTlylList();
                            mData.clear();
                            mData.addAll(beans);
                            mLocalData.clear();
                            for (int i = 0; i < mData.size(); i++) {
                                TLYLBean b = new TLYLBean();
                                b.setID(mData.get(i).getID());
                                b.setProductName(mData.get(i).getProductName());
                                b.setQrcodeID(mData.get(i).getQrcodeID());
                                b.setDw(mData.get(i).getDw());
                                b.setSyzl(mData.get(i).getSyzl());
                                b.setTlzl(mData.get(i).getTlzl());
                                b.setFlag(false);
                                mLocalData.add(b);
                            }
                            mFilterData.clear();
                            mFilterData.addAll(mLocalData);

                            if("edit".equals(mAction)){
                                initSelectedSXYL();
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initSelectedSXYL() {
        String[] ylQrCodeArr = mAllYlQrCode.split(",");
        String[] ylTlzlArr = mAllYlTlzl.split(",");
        for(int i=0;i<ylQrCodeArr.length;i++){
            String ylQrCode = ylQrCodeArr[i];
            for(int j=0;j<mFilterData.size();j++) {
                TLYLBean tlylBean = mFilterData.get(j);
                if (ylQrCode.equals(tlylBean.getQrcodeID())) {
                    tlylBean.setTlzl(Float.valueOf(ylTlzlArr[i]));
                    tlylBean.setFlag(true);
                }
            }
        }
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_sxyl;
    }

    @OnClick(R.id.lastButton)
    public void onViewClicked() {
        if (!checkIfInfoPerfect()) {
            return;
        }
        StringBuffer nameBuffer = new StringBuffer();
        StringBuffer qrcodeBuffer = new StringBuffer();
        StringBuffer tlzlBuffer = new StringBuffer();
        for (int i = 0; i < mFilterData.size(); i++) {
            if (mFilterData.get(i).getFlag()) {
                nameBuffer.append(mFilterData.get(i).getProductName() + ",");
                qrcodeBuffer.append(mFilterData.get(i).getQrcodeID() + ",");
                tlzlBuffer.append(mFilterData.get(i).getTlzl() + ",");
            }
        }
        //如果有选中的则去掉最后一个逗号
        if (nameBuffer.length()>0) {
            nameBuffer.deleteCharAt(nameBuffer.length()-1);
        }
        //如果有选中的则去掉最后一个逗号
        if (qrcodeBuffer.length()>0) {
            qrcodeBuffer.deleteCharAt(qrcodeBuffer.length()-1);
        }
        //如果有选中的则去掉最后一个逗号
        if (tlzlBuffer.length()>0) {
            tlzlBuffer.deleteCharAt(tlzlBuffer.length()-1);
        }
        Intent intent = new Intent();
        intent.putExtra("allYlStr",nameBuffer.toString());
        intent.putExtra("allYlQrCode",qrcodeBuffer.toString());
        intent.putExtra("allYlTlzl",tlzlBuffer.toString());
        Log.e("allYlTlzl===",""+tlzlBuffer.toString());
        Log.d("Test", "send: "+qrcodeBuffer.toString());
        setResult(RESULT_OK,intent);
        AllActivitiesHolder.removeAct(this);
    }

    private boolean checkIfInfoPerfect() {
        float tlzlSum=(float) 0.0;
        for (int i = 0; i < mFilterData.size(); i++) {
            if (mFilterData.get(i).getFlag()) {
                Float syzl = mFilterData.get(i).getSyzl();
                Float tlzl = mFilterData.get(i).getTlzl();
                tlzlSum+=tlzl;
                if(tlzl>syzl){
                    Toast.makeText(this, "投料重量不能大于剩余重量", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        if(tlzlSum>0&&tlzlSum<dwzl){
            Toast.makeText(this, "投料重量不能小于单位重量", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(SelectSXYLActivity.this).inflate(R.layout.sxyl_adapter_single, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final TLYLBean bean = mFilterData.get(position);
            holder.ylName.setText(bean.getProductName());
            holder.qrcodeID.setText(bean.getQrcodeID());
            holder.syzlValue.setText(String.valueOf(bean.getSyzl()));
            holder.tlZhlValue.setText(String.valueOf(bean.getTlzl()));
            holder.tlZhlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!TextUtils.isEmpty(s.toString()))
                        bean.setTlzl(Float.parseFloat(s.toString()));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            holder.syzlDwValue.setText(bean.getDw());
            holder.tlZhlDwValue.setText(bean.getDw());
            if (bean.getFlag()) {
                holder.flag.setChecked(true);
            } else {
                holder.flag.setChecked(false);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getFlag()) {
                        holder.flag.setChecked(false);
                        bean.setFlag(false);
                    } else {
                        holder.flag.setChecked(true);
                        bean.setFlag(true);
                    }
                }
            });
            holder.flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        bean.setFlag(true);
                    } else {
                        bean.setFlag(false);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFilterData.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                //执行过滤操作
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        //没有过滤的内容，则使用源数据
                        mFilterData = mLocalData;
                    } else {
                        List<TLYLBean> filteredList = new ArrayList<>();
                        for (TLYLBean bean : mLocalData) {
                            //这里根据需求，添加匹配规则
                            if (bean.getProductName().contains(charString)) {
                                filteredList.add(bean);
                            }
                        }

                        mFilterData = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilterData;
                    return filterResults;
                }

                //把过滤后的值返回出来
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilterData = (ArrayList<TLYLBean>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView ylName;
            TextView qrcodeID;
            TextView syzlValue;
            EditText tlZhlValue;
            TextView syzlDwValue;
            TextView tlZhlDwValue;
            CheckBox flag;

            public MyViewHolder(View itemView) {
                super(itemView);
                ylName = itemView.findViewById(R.id.ylName);
                qrcodeID = itemView.findViewById(R.id.qrcodeID);
                syzlValue = itemView.findViewById(R.id.syzlValue);
                tlZhlValue = itemView.findViewById(R.id.tlZhlValue);
                syzlDwValue = itemView.findViewById(R.id.syzlDwValue);
                tlZhlDwValue = itemView.findViewById(R.id.tlZhlDwValue);
                flag = itemView.findViewById(R.id.checkFlag);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (!checkIfInfoPerfect()) {
            return;
        }
        StringBuffer nameBuffer = new StringBuffer();
        StringBuffer qrcodeBuffer = new StringBuffer();
        StringBuffer tlzlBuffer = new StringBuffer();
        for (int i = 0; i < mFilterData.size(); i++) {
            if (mFilterData.get(i).getFlag()) {
                nameBuffer.append(mFilterData.get(i).getProductName() + ",");
                qrcodeBuffer.append(mFilterData.get(i).getQrcodeID() + ",");
                tlzlBuffer.append(mFilterData.get(i).getTlzl() + ",");
            }
        }
        //如果有选中的则去掉最后一个逗号
        if (nameBuffer.length()>0) {
            nameBuffer.deleteCharAt(nameBuffer.length()-1);
        }
        //如果有选中的则去掉最后一个逗号
        if (qrcodeBuffer.length()>0) {
            qrcodeBuffer.deleteCharAt(qrcodeBuffer.length()-1);
        }
        //如果有选中的则去掉最后一个逗号
        if (tlzlBuffer.length()>0) {
            tlzlBuffer.deleteCharAt(tlzlBuffer.length()-1);
        }
        Intent intent = new Intent();
        intent.putExtra("allYlStr",nameBuffer.toString());
        intent.putExtra("allYlQrCode",qrcodeBuffer.toString());
        intent.putExtra("allYlTlzl",tlzlBuffer.toString());
        Log.e("allYlTlzl===",""+tlzlBuffer.toString());
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}
