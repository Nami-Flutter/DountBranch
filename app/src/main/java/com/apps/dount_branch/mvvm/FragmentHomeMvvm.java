package com.apps.dount_branch.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.dount_branch.R;
import com.apps.dount_branch.model.OrderDataModel;
import com.apps.dount_branch.model.OrderModel;
import com.apps.dount_branch.model.StatusResponse;
import com.apps.dount_branch.model.UserModel;
import com.apps.dount_branch.remote.Api;
import com.apps.dount_branch.share.Common;
import com.apps.dount_branch.tags.Tags;

import java.io.IOException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentHomeMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentHomeMvvm";
    private MutableLiveData<List<OrderModel>> onOrderDataSuccess;
    private MutableLiveData<Boolean> isLoadingLivData;
    private MutableLiveData<Boolean> onRefused;
    private MutableLiveData<Boolean> onAccept;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentHomeMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public LiveData<List<OrderModel>> onOrderDataSuccess() {
        if (onOrderDataSuccess == null) {
            onOrderDataSuccess = new MutableLiveData<>();
        }
        return onOrderDataSuccess;
    }

    public MutableLiveData<Boolean> onRefused() {
        if (onRefused == null) {
            onRefused = new MutableLiveData<>();
        }
        return onRefused;
    }
    public MutableLiveData<Boolean> onAccept() {
        if (onAccept == null) {
            onAccept = new MutableLiveData<>();
        }
        return onAccept;
    }
    public void getOrder(UserModel userModel) {
        isLoadingLivData.setValue(true);

        Api.getService(Tags.base_url)
                .getNewOrders(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<OrderDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<OrderDataModel> response) {
                        isLoadingLivData.setValue(false);
//                        try {
////                            Log.e("lllll",user_id+" "
////                                    +response.code()+""+response.errorBody().s);
////                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    onOrderDataSuccess.setValue(response.body().getData());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLivData.setValue(false);

                        Log.d(TAG, "onError: ", e);
                    }
                });
    }

    public void refuseOrder(Context context, UserModel userModel, String order_id) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .cancelOrder(userModel.getData().getAccess_token(), order_id)
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
                                    onRefused.setValue(true);
                                }
                            }
                        }else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                        Log.d(TAG, "onError: ", e);
                    }
                });

    }
    public void acceptOrder(Context context, UserModel userModel, String order_id) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .acceptOrder(userModel.getData().getAccess_token(), order_id)
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
                                    onAccept.setValue(true);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                        Log.d(TAG, "onError: ", e);
                    }
                });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
