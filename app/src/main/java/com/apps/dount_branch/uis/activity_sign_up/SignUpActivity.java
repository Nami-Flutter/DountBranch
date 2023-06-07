package com.apps.dount_branch.uis.activity_sign_up;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.apps.dount_branch.R;
import com.apps.dount_branch.adapter.SpinnerVehicleAdapter;
import com.apps.dount_branch.databinding.ActivitySignUpBinding;
import com.apps.dount_branch.model.SignUpModel;
import com.apps.dount_branch.mvvm.ActivitySignupMvvm;
import com.apps.dount_branch.share.Common;
import com.apps.dount_branch.uis.activity_base.BaseActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;
    private SignUpModel model;
    private ActivitySignupMvvm activitySignupMvvm;
    private ActivityResultLauncher<Intent> launcher;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private int selectedReq = 0;
    private Uri uri = null;
    private SpinnerVehicleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }


    private void initView() {
        activitySignupMvvm = ViewModelProviders.of(this).get(ActivitySignupMvvm.class);
        model = new SignUpModel();
        binding.setModel(model);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (selectedReq == READ_REQ) {

                    uri = result.getData().getData();
                    model.setPhoto_uri(uri.toString());
                    File file = new File(Common.getImagePath(this, uri));
                    Picasso.get().load(file).fit().into(binding.image);

                } else if (selectedReq == CAMERA_REQ) {
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    uri = getUriFromBitmap(bitmap);
                    if (uri != null) {
                        model.setPhoto_uri(uri.toString());

                        String path = Common.getImagePath(this, uri);

                        if (path != null) {
                            Picasso.get().load(new File(path)).fit().into(binding.image);

                        } else {
                            Picasso.get().load(uri).fit().into(binding.image);

                        }
                    }
                }
            }
        });

        activitySignupMvvm.onVehicleDataSuccess().observe(this, data -> {
            if (adapter != null) {
                adapter.updateData(data);
                if (getUserModel() != null) {
                   // int pos = getVehiclePos(getUserModel().getData().getUser().getVehicle_id());
                   // binding.spinner.setSelection(pos);
                }

            }
        });
        activitySignupMvvm.onSignUpSuccess().observe(this, userModel -> {
            setUserModel(userModel);
            setResult(RESULT_OK);
            finish();
        });

        activitySignupMvvm.onUpdateSuccess().observe(this, userModel -> {
//            String api_token = getUserModel().getData().getApi_token();
//            userModel.getData().setApi_token(api_token);
//            setUserModel(userModel);
//            setResult(RESULT_OK);
//            finish();
        });

        binding.flImage.setOnClickListener(view -> openSheet());
        binding.flGallery.setOnClickListener(view -> {
            closeSheet();
            checkReadPermission();
        });

        binding.flCamera.setOnClickListener(view -> {
            closeSheet();
            checkCameraPermission();
        });

        adapter = new SpinnerVehicleAdapter(this);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setVehicle_id(activitySignupMvvm.onVehicleDataSuccess().getValue().get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnCancel.setOnClickListener(view -> closeSheet());

        binding.btnSignup.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                if (model.isDataValid(this)) {
                    if (getUserModel() == null) {
                        activitySignupMvvm.signUp(this, model);

                    } else {
                        activitySignupMvvm.updateProfile(this, model, getUserModel());

                    }
                }
            }
        });

        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0")) {
                    binding.edtPhone.setText("");
                }
            }
        });


//        if (getUserModel() != null) {
//            UserModel userModel = getUserModel();
//            model.setUsername(userModel.getData().getName());
//            model.setIdentification(userModel.getData().getIdentification());
//            model.setPassword("123456");
//            model.setPhone(userModel.getData().getPhone());
//            model.setVehicle_id(userModel.getData().getVehicle_id());
//            if (userModel.getData().getPhoto() != null && !userModel.getData().getPhoto().isEmpty()) {
//                Picasso.get().load(Uri.parse(Tags.base_url + userModel.getData().getPhoto())).into(binding.image);
//            }
//            binding.tvPassword.setVisibility(View.GONE);
//            binding.tiPassword.setVisibility(View.GONE);
//            binding.tvPhone.setVisibility(View.GONE);
//            binding.llPhone.setVisibility(View.GONE);
//            binding.btnSignup.setText(getString(R.string.update));
//        }

        activitySignupMvvm.getVehicles(getLang());


    }

    private int getVehiclePos(String vehicleId) {
        int pos = 0;
        for (int index = 0; index < activitySignupMvvm.onVehicleDataSuccess().getValue().size(); index++) {
            String id = activitySignupMvvm.onVehicleDataSuccess().getValue().get(index).getId();
            if (id.equals(vehicleId)) {
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    public void openSheet() {
        binding.expandLayout.setExpanded(true, true);
    }

    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }

    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {
        selectedReq = req;
        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");

            launcher.launch(intent);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(intent);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


}