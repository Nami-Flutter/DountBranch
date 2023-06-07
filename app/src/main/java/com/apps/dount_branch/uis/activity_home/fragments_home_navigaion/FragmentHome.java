package com.apps.dount_branch.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dount_branch.R;

import com.apps.dount_branch.adapter.CurrentOrderAdapter;
import com.apps.dount_branch.model.OrderModel;
import com.apps.dount_branch.mvvm.FragmentHomeMvvm;
import com.apps.dount_branch.uis.activity_base.BaseFragment;
import com.apps.dount_branch.databinding.FragmentHomeBinding;
import com.apps.dount_branch.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentHome extends BaseFragment {
    private static final String TAG = FragmentHome.class.getName();
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private FragmentHomeMvvm fragmentHomeMvvm;
    private CompositeDisposable disposable = new CompositeDisposable();
    private CurrentOrderAdapter adapter;
    private int adapterPos = -1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Observable.timer(130, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        initView();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initView() {

        fragmentHomeMvvm = ViewModelProviders.of(this).get(FragmentHomeMvvm.class);
        fragmentHomeMvvm.getIsLoading().observe(this, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
            if (isLoading) {
                binding.llNoData.setVisibility(View.GONE);
            }
        });

        fragmentHomeMvvm.onRefused().observe(activity, success -> {
            if (success) {
                if (adapterPos != -1) {
                    fragmentHomeMvvm.onOrderDataSuccess().getValue().remove(adapterPos);
                    adapter.notifyItemRemoved(adapterPos);
                    adapterPos = -1;
                    if(adapter.getItemCount()==0){
                        binding.llNoData.setVisibility(View.VISIBLE);

                    }
                }

            }
        });
        fragmentHomeMvvm.onAccept().observe(activity, success -> {
            if (success) {
                if (adapterPos != -1) {
                    fragmentHomeMvvm.onOrderDataSuccess().getValue().remove(adapterPos);
                    adapter.notifyItemRemoved(adapterPos);
                    adapterPos = -1;
                    if(adapter.getItemCount()==0){
                        binding.llNoData.setVisibility(View.VISIBLE);

                    }
                }

            }
        });
        fragmentHomeMvvm.onOrderDataSuccess().observe(this, dataList -> {
            if (adapter != null && dataList != null&&dataList.size()>0) {
                adapter.updateList(dataList);
                binding.llNoData.setVisibility(View.GONE);
            }
            else{
                adapter.updateList(new ArrayList<>());
                binding.llNoData.setVisibility(View.VISIBLE);
            }
        });
        adapter = new CurrentOrderAdapter(activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);

        fragmentHomeMvvm.getOrder(getUserModel());
        binding.swipeRefresh.setOnRefreshListener(() -> fragmentHomeMvvm.getOrder(getUserModel()));

        getParentFragmentManager().setFragmentResultListener("key1", this, (requestKey, result) -> {
            boolean isOrderEnded = result.getBoolean("isEnded");
            if (isOrderEnded){
                Navigation.findNavController(binding.getRoot()).navigate(R.id.fragmentPreviousReservation);
            }
        });

    }


    public void showDetails(OrderModel orderModel) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id", orderModel.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.fragmentOrderDetails, bundle);
    }

    public void cancel(OrderModel orderModel, int adapterPosition) {
        this.adapterPos = adapterPosition;
        fragmentHomeMvvm.refuseOrder(activity, getUserModel(), orderModel.getId());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }

    public void accept(OrderModel orderModel, int adapterPosition) {
        this.adapterPos = adapterPosition;
        fragmentHomeMvvm.acceptOrder(activity, getUserModel(), orderModel.getId());
    }
}