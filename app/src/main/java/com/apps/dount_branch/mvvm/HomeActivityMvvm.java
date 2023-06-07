package com.apps.dount_branch.mvvm;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.dount_branch.R;
import com.apps.dount_branch.model.StatusResponse;
import com.apps.dount_branch.model.UserModel;
import com.apps.dount_branch.remote.Api;
import com.apps.dount_branch.share.Common;
import com.apps.dount_branch.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HomeActivityMvvm extends AndroidViewModel {
    private Context context;

    public MutableLiveData<String> firebase = new MutableLiveData<>();
    private MutableLiveData<Boolean> onLogoutSuccess;
    private CompositeDisposable disposable = new CompositeDisposable();

    public HomeActivityMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();


    }

    public LiveData<Boolean> onLogoutSuccess() {
        if (onLogoutSuccess == null) {
            onLogoutSuccess = new MutableLiveData<>();
        }

        return onLogoutSuccess;
    }

    public void logout(Context context, UserModel userModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).logout(userModel.getData().getAccess_token(), userModel.getData().getFirebase_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> statusResponseResponse) {
                        dialog.dismiss();
                        if (statusResponseResponse.isSuccessful()) {
                            Log.e("code", statusResponseResponse.body().getStatus()+"");

                            if (statusResponseResponse.body().getStatus() == 200) {
                                onLogoutSuccess.setValue(true);
                            }
                        }else {
                            try {
                                Log.e("response",statusResponseResponse.code()+statusResponseResponse.errorBody().string()+"");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }
                });
    }

    public void updateFirebase(Context context, UserModel userModel) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener((Activity) context, task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();
                Log.e(";ll",token);
                Api.getService(Tags.base_url).updateFirebaseToken(userModel.getData().getAccess_token(), userModel.getData().getUser().getId() ,token, "android")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> statusResponseResponse) {
                        Log.e("uuuu",statusResponseResponse.code()+""+statusResponseResponse.body().getStatus());
                        if (statusResponseResponse.isSuccessful()) {
                            if (statusResponseResponse.body().getStatus() == 200) {
                                firebase.setValue(token);
                                Log.e("token", "updated successfully");
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {

                    }
                });
            }
        });


    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
