package com.ara.approvalshipment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.ara.approvalshipment.adapters.GradeAdapter;
import com.ara.approvalshipment.models.Grade;
import com.ara.approvalshipment.models.OrderItem;
import com.ara.approvalshipment.models.SalesOrder;
import com.ara.approvalshipment.utils.ListViewClickListener;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ara.approvalshipment.utils.Helper.CHECKOUT_REQUEST;
import static com.ara.approvalshipment.utils.Helper.SALES_ORDER_EXTRA;
import static com.ara.approvalshipment.utils.Helper.dateToString;
import static com.ara.approvalshipment.utils.Helper.getAvailableGrades;

public class SearchActivity extends AppCompatActivity implements ListViewClickListener {


    SearchView searchView;
    SalesOrder salesOrder;

    @BindView(R.id.search_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_sales_order_count_tv)
    TextView mSalesOrderCountView;

    @BindView(R.id.search_sales_order_fab)
    FloatingActionButton mSalesOrderCounterFAB;


    private GradeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        salesOrder = new SalesOrder();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);
        searchView = findViewById(R.id.action_search);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        List<Grade> gradeList = getAvailableGrades(false);

        mAdapter = new GradeAdapter(gradeList, this);
        recyclerView.setAdapter(mAdapter);

    }

    private void animateFloatingButton() {
//        Animation anim = android.view.animation.AnimationUtils.loadAnimation(mSalesOrderCounterFAB.getContext(), R.anim.shake);
//        anim.setDuration(200L);
//        mSalesOrderCounterFAB.startAnimation(anim);


    }

    @Override
    public void onItemClick(Object selectedObject, int position) {
        Grade grade = (Grade) selectedObject;
        OrderItem orderItem = new OrderItem();
        orderItem.setGradeId(grade.getGradeId());
        orderItem.setGradeCode(grade.getGradeCode());
        orderItem.setGradeName(grade.getGradeName());
        orderItem.setSoldQty(grade.getSoldQuantity());
        salesOrder.addOrderItem(orderItem);
        salesOrder.setDate(dateToString(Calendar.getInstance()));
        mSalesOrderCountView.setText(salesOrder.getOrderItems().size() + "");
        animateFloatingButton();
    }

    @OnClick(R.id.search_sales_order_fab)
    public void onCheckOut(View view) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(SALES_ORDER_EXTRA, salesOrder.toJson());
        startActivityForResult(intent, CHECKOUT_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == CHECKOUT_REQUEST) {
            salesOrder = new SalesOrder();

            mSalesOrderCountView.setText("0");
        }
    }
}
