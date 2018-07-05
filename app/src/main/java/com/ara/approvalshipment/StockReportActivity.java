package com.ara.approvalshipment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ara.approvalshipment.adapters.StockReportAdapter;
import com.ara.approvalshipment.models.Stock;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ara.approvalshipment.utils.Helper.CurrentUser;
import static com.ara.approvalshipment.utils.Helper.DATE_EXTRA;
import static com.ara.approvalshipment.utils.Helper.DATE_PICKER_FROM_REQUEST;
import static com.ara.approvalshipment.utils.Helper.DATE_PICKER_TO_REQUEST;
import static com.ara.approvalshipment.utils.Helper.STOCK_REPORT_ACTION;
import static com.ara.approvalshipment.utils.Helper.getAppService;
import static com.ara.approvalshipment.utils.Helper.log;
import static com.ara.approvalshipment.utils.Helper.showSnackbar;

public class StockReportActivity extends AppCompatActivity {

    private StockReportAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.stock_report_pg)
    ProgressBar progressBar;

    @BindView(R.id.stock_report_rv)
    RecyclerView recyclerView;

    @BindView(R.id.stock_report_to_date)
    TextView toDate;

    @BindView(R.id.stock_report_from_date)
    TextView fromDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_report);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);


    }

    @OnClick({R.id.stock_report_from_date, R.id.stock_report_to_date})
    public void onArrivalDatePicked(View view) {
        Intent intent = new Intent(this, DatePickerActivity.class);
        int id = view.getId();
        if (id == R.id.stock_report_to_date)
            startActivityForResult(intent, DATE_PICKER_TO_REQUEST);
        else
            startActivityForResult(intent, DATE_PICKER_FROM_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case DATE_PICKER_FROM_REQUEST:
                fromDate.setText(data.getStringExtra(DATE_EXTRA));
                break;
            case DATE_PICKER_TO_REQUEST:
                toDate.setText(data.getStringExtra(DATE_EXTRA));
                String strFromDate = fromDate.getText().toString().replace('/', '-');
                String strToDate = fromDate.getText().toString().replace('/', '-');

                Call<List<Stock>> stocks = getAppService().listStocks(STOCK_REPORT_ACTION,
                        strFromDate, strToDate, CurrentUser.getGodownId());
                showProgress(true);
                stocks.enqueue(new Callback<List<Stock>>() {
                    @Override
                    public void onResponse(Call<List<Stock>> call, Response<List<Stock>> response) {
                        showProgress(false);

                        List<Stock> stockList = response.body();
                        if (stockList != null) {
                            mAdapter = new StockReportAdapter(stockList, null);
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            showSnackbar(recyclerView, R.string.no_data_found);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Stock>> call, Throwable t) {
                        showProgress(false);
                        log("Stock Report", t.getMessage());
                        showSnackbar(recyclerView, R.string.something_went_wrong);
                    }
                });


//        mAdapter = new GradeAdapter(gradeList, this);
//        recyclerView.setAdapter(mAdapter);
                break;
        }

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

            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            recyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
