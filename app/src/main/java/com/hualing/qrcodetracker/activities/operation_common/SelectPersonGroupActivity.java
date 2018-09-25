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
import com.hualing.qrcodetracker.bean.PersonBean;
import com.hualing.qrcodetracker.bean.PersonResult;
import com.hualing.qrcodetracker.bean.UserGroupBean;
import com.hualing.qrcodetracker.bean.UserGroupResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
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

public class SelectPersonGroupActivity extends BaseActivity {

    private static final int SELECT_PERSON = 111;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.inputValue)
    EditText mInputValue;
    @BindView(R.id.dataList)
    RecyclerView mRecyclerView;

    private MyAdapter mAdapter;
    private List<UserGroupBean> mData ;
    //模糊过滤后的数据
    private List<UserGroupBean> mFilterData ;
    private MainDao mainDao;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        bundle = getIntent().getExtras();
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(SelectPersonGroupActivity.this);
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
        mAdapter = new SelectPersonGroupActivity.MyAdapter();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PERSON:
                    int personID = data.getIntExtra("personID",0);
                    String personName = data.getStringExtra("personName");
                    Intent ii = new Intent();
                    ii.putExtra("personID", personID);
                    ii.putExtra("personName", personName);
                    setResult(RESULT_OK,ii);
                    AllActivitiesHolder.removeAct(SelectPersonGroupActivity.this);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void getDataFormWeb() {
        mainDao = YoniClient.getInstance().create(MainDao.class);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<UserGroupResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<UserGroupResult>> e) throws Exception {
                ActionResult<UserGroupResult> nr = mainDao.getDepartmentData();
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<UserGroupResult>>() {
                    @Override
                    public void accept(ActionResult<UserGroupResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            UserGroupResult data = result.getResult();
                            List<UserGroupBean> userGroupBeans = data.getGroupBeanList();
                            mData.clear();
                            mData.addAll(userGroupBeans);
                            mFilterData.clear();
                            mFilterData.addAll(userGroupBeans);
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
        return R.layout.activity_select_person_group;
    }


    private class MyAdapter extends RecyclerView.Adapter<SelectPersonGroupActivity.MyAdapter.MyViewHolder> implements Filterable {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v =  LayoutInflater.from(SelectPersonGroupActivity.this).inflate(R.layout.adapter_single,parent,false);
            return new SelectPersonGroupActivity.MyAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(SelectPersonGroupActivity.MyAdapter.MyViewHolder holder, int position) {
            final UserGroupBean bean = mFilterData.get(position);
            holder.sortName.setText(bean.getGroupName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("groupID", bean.getGroupID());
                    if(bundle!=null)
                        bundle1.putString("checkQX", bundle.getString("checkQX"));
                    IntentUtil.openActivityForResult(SelectPersonGroupActivity.this, SelectPersonActivity.class, SELECT_PERSON, bundle1);
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
                        List<UserGroupBean> filteredList = new ArrayList<>();
                        for (UserGroupBean bean : mData) {
                            //这里根据需求，添加匹配规则
                            if (bean.getGroupName().contains(charString)) {
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
                    mFilterData = (ArrayList<UserGroupBean>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView sortName;

            public MyViewHolder(View itemView) {
                super(itemView);
                sortName = itemView.findViewById(R.id.sortName);
            }
        }

    }
}
