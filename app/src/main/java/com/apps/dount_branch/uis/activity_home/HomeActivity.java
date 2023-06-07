package com.apps.dount_branch.uis.activity_home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.apps.dount_branch.interfaces.Listeners;
import com.apps.dount_branch.model.UserModel;
import com.apps.dount_branch.mvvm.HomeActivityMvvm;
import com.apps.dount_branch.uis.activity_base.BaseActivity;

import com.apps.dount_branch.R;

import com.apps.dount_branch.databinding.ActivityHomeBinding;
import com.apps.dount_branch.language.Language;
import com.apps.dount_branch.uis.activity_login.LoginActivity;
import com.apps.dount_branch.uis.activity_sign_up.SignUpActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity implements Listeners.VerificationListener {
    private ActivityHomeBinding binding;
    private NavController navController;
    private HomeActivityMvvm homeActivityMvvm;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }


    private void initView() {

        homeActivityMvvm = ViewModelProviders.of(this).get(HomeActivityMvvm.class);
        homeActivityMvvm.onLogoutSuccess().observe(this, isLogout -> {
            if (isLogout) {
                clearUserModel();
                navigateToLoginActivity();
            }
        });
        binding.setModel(getUserModel());
        binding.setLang(getLang());
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navController = Navigation.findNavController(this, R.id.navHostFragment);

        NavigationUI.setupWithNavController(binding.navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout);


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (binding.toolBar.getNavigationIcon() != null) {
                binding.toolBar.getNavigationIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.gray3), PorterDuff.Mode.SRC_ATOP);

            }

        });

        homeActivityMvvm.firebase.observe(this, token -> {
            if (getUserModel() != null) {
                UserModel userModel = getUserModel();
                userModel.getData().setFirebase_token(token);
                setUserModel(userModel);
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req==1&&result.getResultCode()==RESULT_OK){
                binding.setModel(getUserModel());
            }
        });

        binding.llHome.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);

            int currentFragmentId = navController.getCurrentDestination().getId();
            if (currentFragmentId != R.id.fragmentHome) {
                navController.navigate(R.id.fragmentHome);


            }

        });

        binding.llEditProfile.setOnClickListener(v -> {
            req =1;
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);

        });
        binding.llChangeLanguage.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);

            navController.navigate(R.id.fragmentLanguage);
        });
        binding.llContactUs.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);

            navController.navigate(R.id.fragmentContactUs);
        });

        binding.llPreviousOrder.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);

            navController.navigate(R.id.fragmentPreviousReservation);
        });
        binding.llCurrentOrder.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);

            navController.navigate(R.id.fragmentcurrent);
        });
        binding.llLogout.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);

            homeActivityMvvm.logout(this, getUserModel());
        });

       /* binding.imgNotification.setOnClickListener(v -> {
            if (getUserModel() != null) {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        });*/
        if (getUserModel() != null) {
            homeActivityMvvm.updateFirebase(this, getUserModel());
        }

        String time = new SimpleDateFormat("aa", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        if (time.equals("am")) {
            binding.setGreeting(getString(R.string.good_morning));
        } else {
            binding.setGreeting(getString(R.string.good_evening));

        }

        homeActivityMvvm.updateFirebase(this, getUserModel());
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, binding.drawerLayout) | super.onSupportNavigateUp();

    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int currentFragmentId = navController.getCurrentDestination().getId();
            if (currentFragmentId == R.id.fragmentHome) {
                finish();

            } else {
                navController.popBackStack();
            }
        }


    }

    @Override
    public void onVerificationSuccess() {

    }


    public void updateFirebase() {
        if (getUserModel() != null) {
            homeActivityMvvm.updateFirebase(this, getUserModel());
        }
    }


}
