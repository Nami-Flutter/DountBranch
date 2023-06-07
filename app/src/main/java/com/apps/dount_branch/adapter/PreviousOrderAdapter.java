

package com.apps.dount_branch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dount_branch.R;
import com.apps.dount_branch.databinding.OrderRowBinding;
import com.apps.dount_branch.databinding.PreviousOrderRowBinding;
import com.apps.dount_branch.model.OrderModel;

import java.util.List;

public class PreviousOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public PreviousOrderAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        PreviousOrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.previous_order_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        ProductAdapter productOrderAdapter = new ProductAdapter(context);
        productOrderAdapter.updateList(list.get(position).getDetails());
        myHolder.binding.recView.setLayoutManager(new LinearLayoutManager(context));
        myHolder.binding.recView.setAdapter(productOrderAdapter);

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public PreviousOrderRowBinding binding;

        public MyHolder(PreviousOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<OrderModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
