package com.apps.dount_branch.uis.activity_login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import com.apps.dount_branch.R;
import com.apps.dount_branch.databinding.ActivityLoginBinding;
import com.apps.dount_branch.model.LoginModel;
import com.apps.dount_branch.mvvm.ActivityLoginMvvm;
import com.apps.dount_branch.share.Common;
import com.apps.dount_branch.uis.activity_base.BaseActivity;
import com.apps.dount_branch.uis.activity_home.HomeActivity;
import com.apps.dount_branch.uis.activity_sign_up.SignUpActivity;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private LoginModel model;
    private ActivityLoginMvvm activityLoginMvvm;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }


    private void initView() {
        activityLoginMvvm = ViewModelProviders.of(this).get(ActivityLoginMvvm.class);
        activityLoginMvvm.onLoginSuccess().observe(this, userModel -> {
            setUserModel(userModel);
            navigateToHomActivity();
        });

        binding.tvSignUp.setPaintFlags(binding.tvSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        model = new LoginModel();
        binding.setModel(model);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    navigateToHomActivity();
                }
            }
        });
        binding.btnLogin.setOnClickListener(v -> {
            if (model.isDataValid(this)) {
                Common.CloseKeyBoard(this, binding.edtUserName);
                activityLoginMvvm.login(this, model);
            }
        });

        binding.llSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);
        });
    }

    private void navigateToHomActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


}