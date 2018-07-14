package com.ara.approvalshipment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ara.approvalshipment.adapters.OrderItemAdapter;
import com.ara.approvalshipment.models.SalesOrder;
import com.ara.approvalshipment.utils.ListViewClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import static com.ara.approvalshipment.utils.Helper.CurrentUser;
import static com.ara.approvalshipment.utils.Helper.DATA_PARAM;
import static com.ara.approvalshipment.utils.Helper.DATE_EXTRA;
import static com.ara.approvalshipment.utils.Helper.DATE_PICKER_REQUEST;
import static com.ara.approvalshipment.utils.Helper.FAILED;
import static com.ara.approvalshipment.utils.Helper.SALES_ORDER_EXTRA;
import static com.ara.approvalshipment.utils.Helper.SUCCESS;
import static com.ara.approvalshipment.utils.Helper.getSubmitSalesURL;
import static com.ara.approvalshipment.utils.Helper.log;
import static com.ara.approvalshipment.utils.Helper.showSnackbar;
import static com.ara.approvalshipment.utils.Helper.toQuantity;

public class CheckoutActivity extends AppCompatActivity implements ListViewClickListener {
    SalesOrder salesOrder;

    @BindView(R.id.checkout_container_layout)
    RelativeLayout mContainerLayout;

    @BindView(R.id.checkout_progress)
    ProgressBar progressBar;

    @BindView(R.id.checkout_date)
    TextView mCheckoutDateView;

    @BindView(R.id.checkout_total_items)
    TextView mTotalItemsView;

    @BindView(R.id.checkout_recycler_view)
    RecyclerView recyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        String json = intent.getStringExtra(SALES_ORDER_EXTRA);
        ;
        salesOrder = SalesOrder.fromJson(json);
        mTotalItemsView.setText(toQuantity(salesOrder.getTotalQuantity()));

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OrderItemAdapter(salesOrder.getOrderItems(), this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(Object selectedObject, int position) {

    }

    @OnClick(R.id.checkout_date)
    public void chooseDate(View view) {
        Intent intent = new Intent(this, DatePickerActivity.class);
        startActivityForResult(intent, DATE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == DATE_PICKER_REQUEST) {
            String arrivedDate = data.getStringExtra(DATE_EXTRA);
            mCheckoutDateView.setText(arrivedDate);
            salesOrder.setDate(arrivedDate);
        }
    }

    @OnClick(R.id.checkout_confirm_btn)
    public void onConfirmCheckout(View view) {
        new SubmitSales().execute();
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

            mContainerLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            mContainerLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContainerLayout.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mContainerLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    class SubmitSales extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... strings) {
            OkHttpClient client = new OkHttpClient();
            try {
                salesOrder.setUser(CurrentUser);
                FormBody.Builder builder = new FormBody.Builder();
                builder.add(DATA_PARAM, salesOrder.toJson());


                Request request = new Request.Builder()
                        .url(getSubmitSalesURL())
                        .post(builder.build())
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();


            } catch (Exception e) {

                log("Http URL", e.toString());
                return FAILED;

            }
            return SUCCESS;
        }

        @Override
        protected void onPostExecute(String result) {
            showProgress(false);
            if (result.equalsIgnoreCase(SUCCESS)) {

                setResult(RESULT_OK);
                finish();
            } else {
                showSnackbar(mContainerLayout, R.string.something_went_wrong);
            }
        }
    }

}
