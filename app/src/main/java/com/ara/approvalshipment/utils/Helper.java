package com.ara.approvalshipment.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.ara.approvalshipment.BuildConfig;
import com.ara.approvalshipment.R;
import com.ara.approvalshipment.models.Grade;
import com.ara.approvalshipment.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Helper {
    public static final String REST_URL = "http://prasaadtransports.com/app/";
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    public static final int LOGIN_REQUEST = 101;
    public static final int ARRIVAL_REQUEST = 102;
    public static final int DATE_PICKER_REQUEST = 103;
    public static final int SEARCH_GRADE_REQUEST = 104;
    public static final int CHECKOUT_REQUEST = 105;

    public static final String LOGIN_ACTION = "login";
    public static final String DISPATCH_ACTION = "despatch";
    public static final String GRADE_LIST_ACTION = "grade";
    public static final String SUBMIT_SALES_ACTION = "submit_sales";
    public static final String SHIPMENT_EXTRA = "ShipmentJson";
    public static final String POSITION_EXTRA = "position";
    public static final String DATE_EXTRA = "Date";
    public static final String SUCCESS="Success";
    public static final String FAILED="Failed";
    public static final String DATA_PARAM = "data";
    public static String PREFERENCE_NAME = "PTSA_PREFERENCE";
    public static String USER_INFO = "user_infO";
    public static String SALES_ORDER_EXTRA = "SalesOrderDetails";


    private static List<Grade> availableGrades;
    public static User CurrentUser;
    private static Retrofit retrofit;
    private static AppService appService;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(REST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static String getSubmitShipmentURL() {
        return REST_URL + "pt_android_app.php?action=arrived";
    }
    public static String getSubmitSalesURL() {
        return REST_URL + "pt_android_app.php?action=submit_sales";
    }

    public static AppService getAppService() {
        if (appService == null) {
            appService = retrofit.create(AppService.class);
        }
        return appService;
    }

    public static List<Grade> getAvailableGrades(boolean force) {
        if (availableGrades == null || force) {
            availableGrades = new ArrayList<>();

            Call<List<Grade>> call = getAppService().listGrades(GRADE_LIST_ACTION, CurrentUser.getGodownId());
            call.enqueue(new Callback<List<Grade>>() {
                @Override
                public void onResponse(Call<List<Grade>> call, Response<List<Grade>> response) {
                    availableGrades = response.body();
                }

                @Override
                public void onFailure(Call<List<Grade>> call, Throwable t) {
                    availableGrades = null;
                    log("GetAvailGrades", t.getMessage());
                }
            });

        }
        return availableGrades;
    }

    public static void showSnackbar(View view, int messageId) {
        final Snackbar snackbar = Snackbar.make(view, messageId, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void showSnackbar(View view, String message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void log(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static String formatDouble(double price) {
        return String.format("%8.2f", price).trim();
    }

    public static double toDouble(String doubleValue) {
        if (doubleValue.isEmpty())
            return 0;
        return Double.parseDouble(doubleValue);
    }

    public static boolean isAnyNetworkAvailable(final Context context) {

        boolean status = false;

        final ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connManager) {
            NetworkInfo[] allNetworks = connManager.getAllNetworkInfo();
            if (null != allNetworks) {
                for (NetworkInfo info : allNetworks) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        status = true;
                        break;
                    }
                }
            }
        }
        return status;
    }

    public static String dateToString(Calendar calendar) {
        return calendar.get(Calendar.DATE) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.YEAR);
    }

}
