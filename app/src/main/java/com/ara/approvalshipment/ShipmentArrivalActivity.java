package com.ara.approvalshipment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ara.approvalshipment.models.Shipment;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.ara.approvalshipment.utils.Helper.CurrentUser;
import static com.ara.approvalshipment.utils.Helper.DATE_EXTRA;
import static com.ara.approvalshipment.utils.Helper.DATE_PICKER_REQUEST;
import static com.ara.approvalshipment.utils.Helper.JSON_MEDIA_TYPE;
import static com.ara.approvalshipment.utils.Helper.POSITION_EXTRA;
import static com.ara.approvalshipment.utils.Helper.SHIPMENT_EXTRA;
import static com.ara.approvalshipment.utils.Helper.dateToString;
import static com.ara.approvalshipment.utils.Helper.formatDouble;
import static com.ara.approvalshipment.utils.Helper.getSubmitShipmentURL;
import static com.ara.approvalshipment.utils.Helper.log;
import static com.ara.approvalshipment.utils.Helper.showSnackbar;
import static com.ara.approvalshipment.utils.Helper.toDouble;

public class ShipmentArrivalActivity extends AppCompatActivity {

    @BindView(R.id.arr_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.arr_linear_layout)
    LinearLayout linearLayoutView;
    @BindView(R.id.arr_dispatch_no)
    TextView mDispatchNoView;
    @BindView(R.id.arr_truck_no)
    TextView mTruckNoView;
    @BindView(R.id.arr_dispatch_date)
    TextView mDateView;

    @BindView(R.id.arr_customer_name)
    TextView mCustomerNameView;

    @BindView(R.id.arr_grade_name)
    TextView mGradeNameView;

    @BindView(R.id.arr_quantity)
    TextView mQuantityView;

    @BindView(R.id.arr_rate)
    TextView mRateView;

    @BindView(R.id.arr_total)
    TextView mTotalView;

    @BindView(R.id.arr_edit_clotted_qty)
    TextInputEditText mClottedQtyView;

    @BindView(R.id.arr_edit_clotted_reason)
    TextInputEditText mClottedReasonView;

    @BindView(R.id.arr_edit_damaged_qty)
    TextInputEditText mDamagedQtyView;

    @BindView(R.id.arr_edit_damaged_reason)
    TextInputEditText mDamagedReasonView;

    @BindView(R.id.arr_good_qty)
    TextView mGoodQty;

    @BindView(R.id.arr_date)
    TextView mArrivalDateView;

    @BindView(R.id.arr_div_com_qty)
    TextInputEditText mComDiversionQty;

    @BindView(R.id.arr_div_own_qty)
    TextInputEditText mOwnDiversionQty;


    Shipment shipment;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_arrival);
        ButterKnife.bind(this);

        Intent data = getIntent();
        String shipmentAsJson = data.getStringExtra(SHIPMENT_EXTRA);
        position = data.getIntExtra(POSITION_EXTRA, -1);
        shipment = Shipment.fromJson(shipmentAsJson);
        mDispatchNoView.setText(shipment.getDcNo());

        mTruckNoView.setText(shipment.getTruckNo());
        mDateView.setText(shipment.getDate());
        mCustomerNameView.setText(shipment.getCustomerName());
        mGradeNameView.setText(shipment.getGradeName());
        mQuantityView.setText(formatDouble(shipment.getQuantity()));
        mRateView.setText(formatDouble(shipment.getRate()));
        mTotalView.setText(formatDouble(shipment.getTotal()));
        mGoodQty.setText(formatDouble(shipment.getQuantity()));
        shipment.setUser(CurrentUser);

        String todayDate = dateToString(Calendar.getInstance());
        mArrivalDateView.setText(todayDate);
        shipment.setArrivedDate(todayDate);
        updateLabel();
    }

    private void updateLabel() {
        ActionBar actionBar = getSupportActionBar();
        String title = actionBar.getTitle().toString();
        title += " - " + CurrentUser.getUserName();
        setTitle(title);
    }

    @OnFocusChange({R.id.arr_edit_damaged_qty, R.id.arr_edit_clotted_qty,
            R.id.arr_div_com_qty, R.id.arr_div_own_qty})
    public void updateGoodQty(View view, boolean hasFocus) {
        if (hasFocus)
            return;
        int id = view.getId();

        if (id != R.id.arr_edit_damaged_qty && id != R.id.arr_edit_clotted_qty)
            return;

        String clottedQty = mClottedQtyView.getText().toString();
        String damagedQty = mDamagedQtyView.getText().toString();
        String companyQty = mComDiversionQty.getText().toString();
        String ownQty = mOwnDiversionQty.getText().toString();

        shipment.setClottedQty(toDouble(clottedQty));
        shipment.setDamagedQty(toDouble(damagedQty));
        shipment.setOwnDiversionQty(toDouble(ownQty));
        shipment.setCompanyDiversionQty(toDouble(companyQty));

        double totalDamaged = shipment.getClottedQty() + shipment.getDamagedQty() +
                shipment.getOwnDiversionQty() + shipment.getCompanyDiversionQty();
        double goodQty = shipment.getQuantity() - totalDamaged;
        shipment.setGoodQty(goodQty);
        mGoodQty.setText(formatDouble(shipment.getGoodQty()));

        if (goodQty <= 0) {
            mGoodQty.setTextColor(Color.RED);
        }


    }


    @OnClick(R.id.arrival_submit_btn)
    public void onSubmit(View view) {
        if (!validate()) {
            return;
        }
        mClottedReasonView.setError(null);
        mDamagedReasonView.setError(null);

        String clottedQty = mClottedQtyView.getText().toString();
        String clottedReason = mClottedReasonView.getText().toString();
        String damagedQty = mDamagedQtyView.getText().toString();
        String damagedReason = mDamagedReasonView.getText().toString();
        String companyQty = mComDiversionQty.getText().toString();
        String ownQty = mOwnDiversionQty.getText().toString();


        double dblClottedQty = toDouble(clottedQty);
        double dblDamagedQty = toDouble(damagedQty);
        shipment.setClottedQty(dblClottedQty);
        shipment.setDamagedQty(dblDamagedQty);
        shipment.setDamagedReason(damagedReason);
        shipment.setClottedReason(clottedReason);
        shipment.setOwnDiversionQty(toDouble(ownQty));
        shipment.setCompanyDiversionQty(toDouble(companyQty));

        new SubmitShipment().execute();
    }

    @OnClick(R.id.arr_date)
    public void onArrivalDatePicked(View view) {
        Intent intent = new Intent(this, DatePickerActivity.class);
        startActivityForResult(intent, DATE_PICKER_REQUEST);
    }

    private boolean validate() {
        String clottedQty = mClottedQtyView.getText().toString();
        String clottedReason = mClottedReasonView.getText().toString();
        String damagedQty = mDamagedQtyView.getText().toString();
        String damagedReason = mDamagedReasonView.getText().toString();

        if (!clottedQty.isEmpty() && clottedReason.isEmpty()) {
            String error = getResources().getString(R.string.clotted_reason_error);
            mClottedReasonView.setError(error);
            return false;
        }
        if (!damagedQty.isEmpty() && damagedReason.isEmpty()) {
            String error = getResources().getString(R.string.damaged_reason_error);
            mDamagedReasonView.setError(error);
            return false;
        }
        if (shipment.getGoodQty() < 0) {
            showSnackbar(mDateView, R.string.good_qty_neg);
            return false;
        }

        return true;

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            linearLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
            linearLayoutView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    linearLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            linearLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    void onSubmitComplete() {
        Intent data = new Intent();
        data.putExtra(POSITION_EXTRA, position);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == DATE_PICKER_REQUEST) {
            String arrivedDate = data.getStringExtra(DATE_EXTRA);
            mArrivalDateView.setText(arrivedDate);
            shipment.setArrivedDate(arrivedDate);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    class SubmitShipment extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... strings) {
            OkHttpClient client = new OkHttpClient();
            try {

                RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, shipment.toJson());
                Request request = new Request.Builder()
                        .url(getSubmitShipmentURL())
                        .post(requestBody)
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();


            } catch (Exception e) {

                log("Http URL", e.toString());

            }
            return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
            showProgress(false);
            onSubmitComplete();
        }
    }
}
