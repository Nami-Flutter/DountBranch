package com.apps.dount_branch.services;


import com.apps.dount_branch.model.NotificationDataModel;
import com.apps.dount_branch.model.OrderDataModel;
import com.apps.dount_branch.model.PlaceGeocodeData;
import com.apps.dount_branch.model.SingleOrderDataModel;
import com.apps.dount_branch.model.StatusResponse;
import com.apps.dount_branch.model.UserModel;
import com.apps.dount_branch.model.VehicleDataModel;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("geocode/json")
    Single<Response<PlaceGeocodeData>> getGeoData(@Query(value = "latlng") String latlng,
                                                  @Query(value = "language") String language,
                                                  @Query(value = "key") String key);


    @FormUrlEncoded
    @POST("branch/auth/login")
    Single<Response<UserModel>> login(@Field("email") String email,
                                      @Field("password") String password);

    @Multipart
    @POST("branch/representative/register")
    Observable<Response<UserModel>> signUp(@Part("name") RequestBody name,
                                           @Part("phone_code") RequestBody phone_code,
                                           @Part("phone") RequestBody phone,
                                           @Part("password") RequestBody password,
                                           @Part("vehicle_id") RequestBody vehicle_id,
                                           @Part("identification") RequestBody identification,
                                           @Part MultipartBody.Part logo


    );

    @Multipart
    @POST("branch/representative/edit_profile")
    Observable<Response<UserModel>> updateProfile(@Header("Authorization") String token,
                                                  @Part("name") RequestBody name,
                                                  @Part("vehicle_id") RequestBody vehicle_id,
                                                  @Part("identification") RequestBody identification,
                                                  @Part MultipartBody.Part logo


    );


    @FormUrlEncoded
    @POST("branch/auth/logout")
    Single<Response<StatusResponse>> logout(@Header("Authorization") String token,
                                            @Field("phone_token") String phone_token


    );

    @FormUrlEncoded
    @POST("branch/auth/insert_token")
    Single<Response<StatusResponse>> updateFirebaseToken(@Header("Authorization") String token,
                                                         @Field("user_id") String user_id,
                                                         @Field("phone_token") String phone_token,
                                                         @Field("type") String software_type


    );

    @FormUrlEncoded
    @POST("branch/contact_us")
    Single<Response<StatusResponse>> contactUs(@Field("name") String name,
                                               @Field("email") String email,
                                               @Field("subject") String subject,
                                               @Field("message") String message);


    @GET("branch/representative/vehicles")
    Single<Response<VehicleDataModel>> getVehicles(@Header("lang") String lang);

    @GET("branch/notifications")
    Single<Response<NotificationDataModel>> getNotifications(@Header("AUTHORIZATION") String token,
                                                             @Query(value = "user_id") String user_id
    );

    @GET("branch/order/newOrders")
    Single<Response<OrderDataModel>> getNewOrders(@Header("AUTHORIZATION") String token);

    @GET("branch/order/currentOrders")
    Single<Response<OrderDataModel>> getCurrentOrders(@Header("AUTHORIZATION") String token);

    @GET("branch/order/endedOrders")
    Single<Response<OrderDataModel>> getPreviousOrders(@Header("Authorization") String auth_token
    );

    @GET("branch/orders/order_details")
    Single<Response<SingleOrderDataModel>> getOrderDetails(@Header("Authorization") String auth_token,
                                                           @Query(value = "order_id") String order_id
    );

    @FormUrlEncoded
    @POST("branch/order/acceptOrder")
    Single<Response<StatusResponse>> acceptOrder(@Header("Authorization") String auth_token,
                                                 @Field("order_id") String order_id
    );
    @FormUrlEncoded
    @POST("branch/orders/on_way_order")
    Single<Response<StatusResponse>> orderOnWay(@Header("Authorization") String auth_token,
                                                 @Field("order_id") String order_id
    );
    @FormUrlEncoded
    @POST("branch/order/cancelOrder")
    Single<Response<StatusResponse>> cancelOrder(@Header("Authorization") String auth_token,
                                                 @Field("order_id") String order_id
    );

    @FormUrlEncoded
    @POST("branch/order/endOrder")
    Single<Response<StatusResponse>> endOrder(@Header("Authorization") String auth_token,
                                              @Field("order_id") String order_id
    );


}