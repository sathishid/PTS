package com.ara.approvalshipment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ara.approvalshipment.R;
import com.ara.approvalshipment.models.OrderItem;
import com.ara.approvalshipment.utils.ListViewClickListener;

import java.util.List;

import static com.ara.approvalshipment.utils.Helper.toQuantity;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private final List<OrderItem> mValues;

    private final ListViewClickListener listViewClickListener;


    public OrderItemAdapter(List<OrderItem> items, ListViewClickListener onItemClickListener) {
        mValues = items;

        this.listViewClickListener = onItemClickListener;
    }

    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);
        return new OrderItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderItemAdapter.ViewHolder holder, final int position) {

        final OrderItem orderItem = mValues.get(position);
        holder.mItem = orderItem;
        holder.mGradeName.setText(orderItem.getGradeName());
        holder.mSalesQty.setText(toQuantity(orderItem.getSoldQty()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mGradeName;

        public final TextView mSalesQty;


        public OrderItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mGradeName = (TextView) view.findViewById(R.id.sales_item_grade);
            mSalesQty = (TextView) view.findViewById(R.id.sales_item_qty);

        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}