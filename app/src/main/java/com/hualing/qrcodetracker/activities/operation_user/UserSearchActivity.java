package com.hualing.qrcodetracker.activities.operation_user;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.EmployeeMainActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.PersonBean;
import com.hualing.qrcodetracker.bean.PersonParam;
import com.hualing.qrcodetracker.bean.PersonResult;
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

public class UserSearchActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.inputValue)
    EditText mInputValue;
    @BindView(R.id.dataList)
    RecyclerView mRecyclerView;

    private UserSearchActivity.MyAdapter mAdapter;
    private List<PersonBean> mData ;
    //模糊过滤后的数据
    private List<PersonBean> mFilterData ;
    private MainDao mainDao;

    @Override
    protected void initLogic() {

        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(UserSearchActivity.this);
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
        mAdapter = new UserSearchActivity.MyAdapter();
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


        Observable.create(new ObservableOnSubscribe<ActionResult<PersonResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<PersonResult>> e) throws Exception {
                ActionResult<PersonResult> nr = mainDao.searchAllPerson();
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<PersonResult>>() {
                    @Override
                    public void accept(ActionResult<PersonResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            PersonResult data = result.getResult();
                            List<PersonBean> personBeans = data.getPersonBeans();
                            mData.clear();
                            mData.addAll(personBeans);
                            mFilterData.clear();
                            mFilterData.addAll(personBeans);
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
        return R.layout.activity_user_search;
    }

    private class MyAdapter extends RecyclerView.Adapter<UserSearchActivity.MyAdapter.MyViewHolder> implements Filterable {
        @Override
        public UserSearchActivity.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v =  LayoutInflater.from(UserSearchActivity.this).inflate(R.layout.item_user_info,parent,false);
            return new UserSearchActivity.MyAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(UserSearchActivity.MyAdapter.MyViewHolder holder, final int position) {
            final PersonBean bean = mFilterData.get(position);
            holder.trueName.setText(bean.getTrueName());
            holder.group.setText(bean.getGroupName());
            holder.loginName.setText(bean.getLoginName());
            holder.regTime.setText(bean.getRegTime());
            holder.lookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userID", bean.getUserId());
                    IntentUtil.openActivityForResult(UserSearchActivity.this,UserInfoActivity.class,-1,bundle);
                }
            });
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(UserSearchActivity.this)
                            .setCancelable(false)
                            .setTitle("提示")
                            .setMessage("确定要删除员工："+bean.getLoginName()+"？删除后不可恢复，请谨慎操作！")
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final Dialog progressDialog = TheApplication.createLoadingDialog(UserSearchActivity.this, "");
                                    progressDialog.show();

                                    final PersonParam personParam = new PersonParam();
                                    personParam.setUserId(bean.getUserId());
                                    Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
                                        @Override
                                        public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                                            ActionResult<ActionResult> nr = mainDao.deleteUser(personParam);
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
                                                        Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                                                        mFilterData.remove(position);
                                                        MyAdapter.this.notifyDataSetChanged();
                                                    }
                                                }
                                            });
                                }
                            })
                            .show();
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
                        List<PersonBean> filteredList = new ArrayList<>();
                        for (PersonBean bean : mData) {
                            //这里根据需求，添加匹配规则
                            if (bean.getTrueName().contains(charString)||bean.getLoginName().contains(charString)) {
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
                    mFilterData = (ArrayList<PersonBean>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView trueName;
            TextView group;
            TextView loginName;
            TextView regTime;
            Button lookBtn;
            Button deleteBtn;

            public MyViewHolder(View itemView) {
                super(itemView);
                trueName = itemView.findViewById(R.id.trueNameValue);
                group = itemView.findViewById(R.id.groupValue);
                loginName = itemView.findViewById(R.id.loginNameValue);
                regTime = itemView.findViewById(R.id.regTimeValue);
                lookBtn = itemView.findViewById(R.id.lookBtn);
                deleteBtn = itemView.findViewById(R.id.deleteBtn);
            }
        }

    }
}
