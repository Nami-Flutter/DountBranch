package com.apps.dount_branch.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.apps.dount_branch.BR;
import com.apps.dount_branch.R;


public class SignUpModel extends BaseObservable {
    private String username;
    private String phone;
    private String phone_code;
    private String vehicle_id;
    private String identification;
    private String password;
    private String photo_uri;


    public ObservableField<String> error_username = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_identification = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!username.isEmpty() &&
                !phone.isEmpty() &&
                !vehicle_id.isEmpty() &&
                identification.length() == 15 &&
                password.length() >= 6


        ) {
            error_username.set(null);
            error_phone.set(null);
            error_identification.set(null);
            error_password.set(null);


            return true;
        } else {

            Log.e("len",identification.length()+"");
            if (username.isEmpty()) {
                error_username.set(context.getString(R.string.field_required));

            } else {
                error_username.set(null);

            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));

            } else {
                error_phone.set(null);

            }

            if (vehicle_id.isEmpty()) {
                Toast.makeText(context, R.string.ch_vehicle, Toast.LENGTH_SHORT).show();
            }

            if (identification.isEmpty()) {
                error_identification.set(context.getString(R.string.field_required));

            } else if (identification.length() != 15) {
                error_identification.set(context.getString(R.string.iden_should_be));

            } else {
                error_identification.set(null);

            }

            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_required));

            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.pass_short));

            } else {
                error_password.set(null);

            }


            return false;
        }
    }

    public SignUpModel() {
        username = "";
        phone_code = "20";
        phone = "";
        identification = "";
        vehicle_id = "";
        photo_uri = "";
        password = "";

    }


    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    @Bindable
    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
        notifyPropertyChanged(BR.identification);

    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }
}