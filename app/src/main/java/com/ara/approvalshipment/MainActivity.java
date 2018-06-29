package com.ara.approvalshipment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.ara.approvalshipment.adapters.ShipmentItemAdapter;
import com.ara.approvalshipment.models.Shipment;
import com.ara.approvalshipment.models.User;
import com.ara.approvalshipment.utils.AppService;
import com.ara.approvalshipment.utils.ListViewClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ara.approvalshipment.utils.Helper.ARRIVAL_REQUEST;
import static com.ara.approvalshipment.utils.Helper.CurrentUser;
import static com.ara.approvalshipment.utils.Helper.DISPATCH_ACTION;
import static com.ara.approvalshipment.utils.Helper.LOGIN_REQUEST;
import static com.ara.approvalshipment.utils.Helper.POSITION_EXTRA;
import static com.ara.approvalshipment.utils.Helper.PREFERENCE_NAME;
import static com.ara.approvalshipment.utils.Helper.SHIPMENT_EXTRA;
import static com.ara.approvalshipment.utils.Helper.USER_INFO;
import static com.ara.approvalshipment.utils.Helper.getAppService;
import static com.ara.approvalshipment.utils.Helper.log;
import static com.ara.approvalshipment.utils.Helper.showSnackbar;

public class MainActivity extends AppCompatActivity implements ListViewClickListener {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.shipment_progress)
    ProgressBar progressBar;
    @BindView(R.id.ships_recyclerView)
    RecyclerView recyclerView;
    List<Shipment> shipmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        //If list is empty then recycler view will throw exception. So need to hide if empty.
        recyclerView.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        if (sharedPreferences.contains(USER_INFO)) {
            CurrentUser = User.fromGson(sharedPreferences.getString(USER_INFO, null));
            loadShipments();

        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        }


    }

    private void loadShipments() {
        AppService appService = getAppService();
        Call<List<Shipment>> shipmentsService = appService.listShipments(DISPATCH_ACTION, CurrentUser.getGodownId());
        showProgress(true);
        shipmentsService.enqueue(new Callback<List<Shipment>>() {
            @Override
            public void onResponse(Call<List<Shipment>> call, Response<List<Shipment>> response) {
                shipmentList = response.body();
                ShipmentItemAdapter shipmentItemAdapter = new ShipmentItemAdapter(shipmentList, MainActivity.this);
                mAdapter = shipmentItemAdapter;
                recyclerView.setAdapter(shipmentItemAdapter);
                showProgress(false);
            }

            @Override
            public void onFailure(Call<List<Shipment>> call, Throwable t) {
                log("Shipment List", t.getMessage());
                showProgress(false);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case LOGIN_REQUEST:
                loadShipments();
                updateSharedPreference();
                break;
            case ARRIVAL_REQUEST:
                int position = data.getIntExtra(POSITION_EXTRA, -1);

                shipmentList.remove(position);
                if (shipmentList.size() == 0) {
                    mAdapter.notifyItemRemoved(position);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    mAdapter.notifyItemRemoved(position);
                }
                showSnackbar(recyclerView, R.string.shipment_submitted);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
            return true;
        } else
            return super.onOptionsItemSelected(item);
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


    @Override
    public void onItemClick(Object selectedObject, int position) {
        Intent intent = new Intent(this, ShipmentArrivalActivity.class);
        Shipment shipment = (Shipment) selectedObject;
        intent.putExtra(SHIPMENT_EXTRA, shipment.toJson());
        intent.putExtra(POSITION_EXTRA, position);
        startActivityForResult(intent, ARRIVAL_REQUEST);
    }

    void updateSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(USER_INFO, CurrentUser.toJson());
        edit.commit();
    }

}
