package com.apps.dount_branch.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dount_branch.R;
import com.apps.dount_branch.model.OrderModel;
import com.apps.dount_branch.model.SingleOrderDataModel;
import com.apps.dount_branch.model.StatusResponse;
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

public class FragmentOrderDetailsMvvm extends AndroidViewModel {
    private static final String TAG = "FragOrderDeMvvm";
    private Context context;

    private MutableLiveData<OrderModel> onOrderDataSuccess;
    private MutableLiveData<Boolean> onAccept;
    private MutableLiveData<Boolean> onWay;

    private MutableLiveData<Boolean> onRefused;
    private MutableLiveData<Boolean> onEnded;

    private MutableLiveData<Boolean> isLoadingLivData;

    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentOrderDetailsMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<OrderModel> onOrderDataSuccess() {
        if (onOrderDataSuccess == null) {
            onOrderDataSuccess = new MutableLiveData<>();
        }
        return onOrderDataSuccess;
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public MutableLiveData<Boolean> onAccept() {
        if (onAccept == null) {
            onAccept = new MutableLiveData<>();
        }
        return onAccept;
    }
    public MutableLiveData<Boolean> onWay() {
        if (onWay == null) {
            onWay = new MutableLiveData<>();
        }
        return onWay;
    }
    public MutableLiveData<Boolean> onRefused() {
        if (onRefused == null) {
            onRefused = new MutableLiveData<>();
        }
        return onRefused;
    }

    public MutableLiveData<Boolean> onEnded() {
        if (onEnded == null) {
            onEnded = new MutableLiveData<>();
        }
        return onEnded;
    }

    //_________________________hitting api_________________________________

    public void getOrderDetails(UserModel userModel, String order_id) {
        isLoadingLivData.setValue(true);

        Api.getService(Tags.base_url)
                .getOrderDetails(userModel.getData().getAccess_token(), order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleOrderDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleOrderDataModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    isLoadingLivData.setValue(false);
                                    onOrderDataSuccess.setValue(response.body().getData());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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
    public void onWayOrder(Context context, UserModel userModel, String order_id) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .orderOnWay(userModel.getData().getAccess_token(), order_id)
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
                                    onWay.setValue(true);
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
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                        Log.d(TAG, "onError: ", e);
                    }
                });

    }

    public void endOrder(Context context, UserModel userModel, String order_id) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .endOrder(userModel.getData().getAccess_token(), order_id)
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
                                    onEnded.setValue(true);
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
