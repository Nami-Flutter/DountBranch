
package com.apps.dount_branch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.apps.dount_branch.R;
import com.apps.dount_branch.databinding.FilterLayoutBinding;
import com.apps.dount_branch.databinding.SpinnerRowBinding;

public class SpinnerFilterAdapter extends BaseAdapter {
    private Context context;
    private int selectedPos;

    public SpinnerFilterAdapter(Context context, int selectedPos) {
        this.context = context;
        this.selectedPos = selectedPos;

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") FilterLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.filter_layout, viewGroup, false);
        if (selectedPos == 1) {
            binding.rbWeek.setChecked(true);
        }

        if (selectedPos == 2) {
            binding.rbMonth.setChecked(true);
        }

        if (selectedPos == 3) {
            binding.rbYear.setChecked(true);
        }

        if (selectedPos == 4) {
            binding.rbAll.setChecked(true);
        }
        return binding.getRoot();
    }




}
