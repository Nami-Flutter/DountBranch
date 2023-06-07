package com.apps.dount_branch.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.dount_branch.R;
import com.apps.dount_branch.model.ContactUsModel;
import com.apps.dount_branch.model.StatusResponse;
import com.apps.dount_branch.remote.Api;
import com.apps.dount_branch.share.Common;
import com.apps.dount_branch.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentContactusMvvm extends AndroidViewModel {

    private MutableLiveData<Boolean> onSendSuccess = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentContactusMvvm(@NonNull Application application) {
        super(application);


    }

    public LiveData<Boolean> onSendSuccess() {
        if (onSendSuccess == null) {
            onSendSuccess = new MutableLiveData<>();
        }
        return onSendSuccess;
    }

    public void contactus(Context context, ContactUsModel contactUsModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).contactUs(contactUsModel.getName(), contactUsModel.getEmail(), contactUsModel.getSubject(), contactUsModel.getMessage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    onSendSuccess.setValue(true);
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
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
