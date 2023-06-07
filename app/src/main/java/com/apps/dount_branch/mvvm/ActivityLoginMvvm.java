package com.apps.dount_branch.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.dount_branch.R;
import com.apps.dount_branch.model.LoginModel;
import com.apps.dount_branch.model.UserModel;
import com.apps.dount_branch.remote.Api;
import com.apps.dount_branch.share.Common;
import com.apps.dount_branch.tags.Tags;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityLoginMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityLoginMvvm";
    private MutableLiveData<UserModel> onLoginSuccess = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityLoginMvvm(@NonNull Application application) {
        super(application);


    }

    public LiveData<UserModel> onLoginSuccess(){
        if (onLoginSuccess==null){
            onLoginSuccess = new MutableLiveData<>();
        }
        return onLoginSuccess;
    }


    public void login(Context context, LoginModel model) {

        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).login(model.getEmail(), model.getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<UserModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull Response<UserModel> userModelResponse) {
                dialog.dismiss();

                if (userModelResponse.isSuccessful()) {
                    Log.e("status", userModelResponse.body().getStatus() + "");
                    if (userModelResponse.body().getStatus() == 200) {

                        onLoginSuccess.setValue(userModelResponse.body());
                    } else if (userModelResponse.body().getStatus() == 403) {
                        Toast.makeText(context, R.string.inv_cred, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                dialog.dismiss();

            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
