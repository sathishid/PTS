package com.ara.approvalshipment.utils;

import com.ara.approvalshipment.models.Shipment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AppService {
    @GET("pt_android_app.php")
    Call<ResponseBody> validateUser(@Query("action") String action, @Query("user_id") String loginId,
                                    @Query("password") String password);

    @GET("pt_android_app.php")
    Call<List<Shipment>> listShipments(@Query("action") String action, @Query("godown_id") int godownId);


    @GET("pt_android_app.php")
    Call<String> submitShipment(@Query("action") String action, @Query("data") String shipmentAsJson);

}
