package com.hualing.qrcodetracker.activities.operation_common;

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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.HlProductBean;
import com.hualing.qrcodetracker.bean.HlProductParam;
import com.hualing.qrcodetracker.bean.HlProductResult;
import com.hualing.qrcodetracker.bean.HlSortBean;
import com.hualing.qrcodetracker.bean.HlSortResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.widget.MyRecycleViewDivider;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SelectHlProductActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.inputValue)
    EditText mInputValue;
    @BindView(R.id.dataList)
    RecyclerView mRecyclerView;

    private SelectHlProductActivity.MyAdapter mAdapter;
    private List<HlProductBean> mData ;
    //模糊过滤后的数据
    private List<HlProductBean> mFilterData ;
    private MainDao mainDao;
    private HlProductParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        param=new HlProductParam();
        if(getIntent()!=null)
            param.setSortID(getIntent().getIntExtra("sortID",-1));
        Log.e("SortID=======",""+param.getSortID());

        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(SelectHlProductActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        mData = new ArrayList<>();
        mFilterData = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.divide_gray_color)));
        mAdapter = new SelectHlProductActivity.MyAdapter();
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

    }

    @Override
    protected void getDataFormWeb() {

        mainDao = YoniClient.getInstance().create(MainDao.class);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<HlProductResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<HlProductResult>> e) throws Exception {
                ActionResult<HlProductResult> nr = mainDao.getHlProduct(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<HlProductResult>>() {
                    @Override
                    public void accept(ActionResult<HlProductResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            HlProductResult data = result.getResult();
                            List<HlProductBean> hlSortBeans = data.getHlProductBeans();
                            mData.clear();
                            mData.addAll(hlSortBeans);
                            mFilterData.clear();
                            mFilterData.addAll(hlSortBeans);
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
        return R.layout.activity_select_product;
    }

    private class MyAdapter extends RecyclerView.Adapter<SelectHlProductActivity.MyAdapter.MyViewHolder> implements Filterable {
        @Override
        public SelectHlProductActivity.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v =  LayoutInflater.from(SelectHlProductActivity.this).inflate(R.layout.product_adapter_single,parent,false);
            return new SelectHlProductActivity.MyAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(SelectHlProductActivity.MyAdapter.MyViewHolder holder, int position) {
            final HlProductBean bean = mFilterData.get(position);
            holder.productName.setText(bean.getProductName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ii = new Intent();
                    ii.putExtra("productName",bean.getProductName());
                    ii.putExtra("model",bean.getModel());
                    ii.putExtra("productCode",bean.getProductCode());
                    ii.putExtra("company",bean.getCompany());
                    setResult(RESULT_OK,ii);
                    AllActivitiesHolder.removeAct(SelectHlProductActivity.this);
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
                        mFilterData = mData;
                    } else {
                        List<HlProductBean> filteredList = new ArrayList<>();
                        for (HlProductBean bean : mData) {
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
                    mFilterData = (ArrayList<HlProductBean>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView productName;

            public MyViewHolder(View itemView) {
                super(itemView);
                productName = itemView.findViewById(R.id.productName);
            }
        }

    }
}
