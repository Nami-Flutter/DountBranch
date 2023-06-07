package com.apps.dount_branch.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.dount_branch.R;
import com.apps.dount_branch.model.SignUpModel;
import com.apps.dount_branch.model.UserModel;
import com.apps.dount_branch.model.VehicleDataModel;
import com.apps.dount_branch.model.VehicleModel;
import com.apps.dount_branch.remote.Api;
import com.apps.dount_branch.share.Common;
import com.apps.dount_branch.tags.Tags;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ActivitySignupMvvm extends AndroidViewModel {
    private MutableLiveData<UserModel> onSignUpSuccess = new MutableLiveData<>();
    private MutableLiveData<UserModel> onUpdateSuccess = new MutableLiveData<>();

    private MutableLiveData<List<VehicleModel>> onVehicleDataSuccess = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivitySignupMvvm(@NonNull Application application) {
        super(application);


    }

    public LiveData<UserModel> onSignUpSuccess() {
        if (onSignUpSuccess == null) {
            onSignUpSuccess = new MutableLiveData<>();
        }
        return onSignUpSuccess;
    }
    public LiveData<UserModel> onUpdateSuccess() {
        if (onUpdateSuccess == null) {
            onUpdateSuccess = new MutableLiveData<>();
        }
        return onUpdateSuccess;
    }

    public LiveData<List<VehicleModel>> onVehicleDataSuccess() {
        if (onVehicleDataSuccess == null) {
            onVehicleDataSuccess = new MutableLiveData<>();
        }
        return onVehicleDataSuccess;
    }

    public void signUp(Context context, SignUpModel model) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(model.getUsername());
        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody phone_code_part = Common.getRequestBodyText(model.getPhone_code());
        RequestBody password_part = Common.getRequestBodyText(model.getPassword());
        RequestBody identification_part = Common.getRequestBodyText(model.getIdentification());
        RequestBody vehicle_part = Common.getRequestBodyText(model.getVehicle_id());


        MultipartBody.Part image = null;
        if (!model.getPhoto_uri().isEmpty()) {
            image = Common.getMultiPart(context, Uri.parse(model.getPhoto_uri()), "photo");
        }


        Api.getService(Tags.base_url).signUp(name_part, phone_code_part, phone_part, password_part, vehicle_part, identification_part, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                Log.e("code", response.body().getStatus() + "");
                                if (response.body().getStatus() == 200) {
                                    onSignUpSuccess.setValue(response.body());

                                } else if (response.body().getStatus() == 400) {
                                    Toast.makeText(context, R.string.data_exist, Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(context, response.body().getMessage() + "", Toast.LENGTH_LONG).show();

                                }
                            }
                        } else {
                            try {
                                Log.e("response", response.code() + response.errorBody().string() + "");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    public void updateProfile(Context context, SignUpModel model, UserModel userModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(model.getUsername());
        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody phone_code_part = Common.getRequestBodyText(model.getPhone_code());
        RequestBody password_part = Common.getRequestBodyText(model.getPassword());
        RequestBody identification_part = Common.getRequestBodyText(model.getIdentification());
        RequestBody vehicle_part = Common.getRequestBodyText(model.getVehicle_id());


        MultipartBody.Part image = null;
        if (!model.getPhoto_uri().isEmpty()) {
            image = Common.getMultiPart(context, Uri.parse(model.getPhoto_uri()), "photo");
        }


        Api.getService(Tags.base_url).updateProfile(userModel.getData().getAccess_token(), name_part, vehicle_part, identification_part, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                Log.e("code", response.body().getStatus() + "");
                                if (response.body().getStatus() == 200) {
                                    onUpdateSuccess.setValue(response.body());

                                } else if (response.body().getStatus() == 400) {
                                    Toast.makeText(context, R.string.data_exist, Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(context, response.body().getMessage() + "", Toast.LENGTH_LONG).show();

                                }
                            }
                        } else {
                            try {
                                Log.e("response", response.code() + response.errorBody().string() + "");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    public void getVehicles(String lang) {
        Api.getService(Tags.base_url)
                .getVehicles(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<VehicleDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<VehicleDataModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    onVehicleDataSuccess.setValue(response.body().getData());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("SignUpMVVM", "onError: ", e);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }


}
