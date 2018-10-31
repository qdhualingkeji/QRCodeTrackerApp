package com.hualing.qrcodetracker.activities.operation_user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectSXYLActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.Module2Bean;
import com.hualing.qrcodetracker.bean.Module2Result;
import com.hualing.qrcodetracker.bean.PersonResult;
import com.hualing.qrcodetracker.bean.SXYLResult;
import com.hualing.qrcodetracker.bean.TLYLBean;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.LocalShowBean;
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

public class SelectModule2Activity extends BaseActivity {

    @BindView(R.id.inputValue)
    EditText mInputValue;
    @BindView(R.id.dataList)
    RecyclerView mRecyclerView;
    @BindView(R.id.selectAll)
    CheckBox mSelectAll;

    private SelectModule2Activity.MyAdapter mAdapter;
    private List<Module2Bean> mData;
    private List<LocalShowBean> mLocalData;
    //模糊过滤后的数据
    private List<LocalShowBean> mFilterData;
    private MainDao mainDao;

    @Override
    protected void initLogic() {
        mData = new ArrayList<>();
        mLocalData = new ArrayList<>();
        mFilterData = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.divide_gray_color)));
        mAdapter = new SelectModule2Activity.MyAdapter();
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

        Bundle bundle = getIntent().getExtras();

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

        Observable.create(new ObservableOnSubscribe<ActionResult<Module2Result>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<Module2Result>> e) throws Exception {
                ActionResult<Module2Result> nr = mainDao.getXZQX();
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<Module2Result>>() {
                    @Override
                    public void accept(ActionResult<Module2Result> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Module2Result data = result.getResult();
                            List<Module2Bean> beans = data.getBeans();
                            mData.clear();
                            mData.addAll(beans);
                            mLocalData.clear();
                            for (int i = 0; i < mData.size(); i++) {
                                LocalShowBean b = new LocalShowBean();
                                b.setID(mData.get(i).getId());
                                b.setProductName(mData.get(i).getName());
                                b.setFlag(false);
                                mLocalData.add(b);
                            }
                            mFilterData.clear();
                            mFilterData.addAll(mLocalData);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_module2;
    }

    @OnClick(R.id.lastButton)
    public void onViewClicked() {
        StringBuffer nameBuffer = new StringBuffer();
        StringBuffer idBuffer = new StringBuffer();
        for (int i = 0; i < mFilterData.size(); i++) {
            if (mFilterData.get(i).getFlag()) {
                nameBuffer.append(mFilterData.get(i).getProductName() + ",");
                idBuffer.append(mFilterData.get(i).getID() + ",");
            }
        }
        //如果有选中的则去掉最后一个逗号
        if (nameBuffer.length()>0) {
            nameBuffer.deleteCharAt(nameBuffer.length()-1);
        }
        //如果有选中的则去掉最后一个逗号
        if (idBuffer.length()>0) {
            idBuffer.deleteCharAt(idBuffer.length()-1);
        }
        Intent intent = new Intent();
        intent.putExtra("allQxNameStr",nameBuffer.toString());
        intent.putExtra("allQxId",idBuffer.toString());
        Log.d("Test", "send: "+idBuffer.toString());
        setResult(RESULT_OK,intent);
        AllActivitiesHolder.removeAct(this);
    }

    private class MyAdapter extends RecyclerView.Adapter<SelectModule2Activity.MyAdapter.MyViewHolder> implements Filterable {
        @Override
        public SelectModule2Activity.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(SelectModule2Activity.this).inflate(R.layout.tlyl_adapter_single, parent, false);
            return new SelectModule2Activity.MyAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final SelectModule2Activity.MyAdapter.MyViewHolder holder, final int position) {
            final LocalShowBean bean = mFilterData.get(position);
            holder.ylName.setText(bean.getProductName());
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
                        List<LocalShowBean> filteredList = new ArrayList<>();
                        for (LocalShowBean bean : mLocalData) {
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
                    mFilterData = (ArrayList<LocalShowBean>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView ylName;
            CheckBox flag;

            public MyViewHolder(View itemView) {
                super(itemView);
                ylName = itemView.findViewById(R.id.ylName);
                flag = itemView.findViewById(R.id.checkFlag);
            }
        }

    }

    @Override
    public void onBackPressed() {
        StringBuffer nameBuffer = new StringBuffer();
        StringBuffer idBuffer = new StringBuffer();
        for (int i = 0; i < mFilterData.size(); i++) {
            if (mFilterData.get(i).getFlag()) {
                nameBuffer.append(mFilterData.get(i).getProductName() + ",");
                idBuffer.append(mFilterData.get(i).getID() + ",");
            }
        }
        //如果有选中的则去掉最后一个逗号
        if (nameBuffer.length()>0) {
            nameBuffer.deleteCharAt(nameBuffer.length()-1);
        }
        //如果有选中的则去掉最后一个逗号
        if (idBuffer.length()>0) {
            idBuffer.deleteCharAt(idBuffer.length()-1);
        }
        Intent intent = new Intent();
        intent.putExtra("allQxNameStr",nameBuffer.toString());
        intent.putExtra("allQxId",idBuffer.toString());
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}
