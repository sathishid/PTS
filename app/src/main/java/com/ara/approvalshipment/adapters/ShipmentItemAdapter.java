package com.ara.approvalshipment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ara.approvalshipment.R;
import com.ara.approvalshipment.models.Shipment;
import com.ara.approvalshipment.utils.ListViewClickListener;

import java.util.List;

public class ShipmentItemAdapter extends RecyclerView.Adapter<ShipmentItemAdapter.ViewHolder> {

    private final List<Shipment> mValues;
    private final ListViewClickListener onItemClickListner;


    public ShipmentItemAdapter(List<Shipment> items, ListViewClickListener onItemClickListener) {
        mValues = items;
        this.onItemClickListner = onItemClickListener;
    }

    @Override
    public ShipmentItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shipment_list_item, parent, false);
        return new ShipmentItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShipmentItemAdapter.ViewHolder holder, final int position) {

        final Shipment shipment = mValues.get(position);
        holder.mItem = shipment;
        holder.mProductName.setText(shipment.getProductName());
        holder.mDispatchNo.setText(shipment.getDcNo());
        holder.mTruckNo.setText(shipment.getTruckNo());
        holder.mDate.setText(shipment.getDate());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListner.onItemClick(mValues.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTruckNo;
        public final TextView mDispatchNo;
        public final TextView mDate;
        public final TextView mProductName;
        ;


        public Shipment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTruckNo = (TextView) view.findViewById(R.id.ship_li_truck_no);
            mDispatchNo = (TextView) view.findViewById(R.id.ship_li_dc_no);
            mDate = (TextView) view.findViewById(R.id.ship_li_date);
            mProductName = (TextView) view.findViewById(R.id.ship_li_product_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}