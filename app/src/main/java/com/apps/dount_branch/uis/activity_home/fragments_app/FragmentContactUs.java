package com.apps.dount_branch.uis.activity_home.fragments_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.apps.dount_branch.R;
import com.apps.dount_branch.databinding.FragmentContactUsBinding;
import com.apps.dount_branch.databinding.FragmentLanguageBinding;
import com.apps.dount_branch.model.ContactUsModel;
import com.apps.dount_branch.mvvm.FragmentContactusMvvm;
import com.apps.dount_branch.share.Common;
import com.apps.dount_branch.uis.activity_base.BaseFragment;
import com.apps.dount_branch.uis.activity_home.HomeActivity;


public class FragmentContactUs extends BaseFragment {
    private static final String TAG = FragmentContactUs.class.getName();
    private HomeActivity activity;
    private FragmentContactUsBinding binding;
    private ContactUsModel model;
    private FragmentContactusMvvm fragmentContactusMvvm;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        fragmentContactusMvvm = ViewModelProviders.of(this).get(FragmentContactusMvvm.class);
        fragmentContactusMvvm.onSendSuccess().observe(activity, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).popBackStack();

            }
        });
        model = new ContactUsModel();
        binding.setContactModel(model);

        binding.btnSend.setOnClickListener(v -> {
            if (model.isDataValid(activity)) {
                Common.CloseKeyBoard(activity, binding.edtName);
                fragmentContactusMvvm.contactus(activity, model);
            }
        });


    }


}