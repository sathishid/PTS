package com.ara.approvalshipment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ara.approvalshipment.R;
import com.ara.approvalshipment.models.Stock;
import com.ara.approvalshipment.utils.ListViewClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.ara.approvalshipment.utils.Helper.toQuantity;

public class StockReportAdapter extends RecyclerView.Adapter<StockReportAdapter.ViewHolder>
        implements Filterable {

    private final List<Stock> mValues;
    private List<Stock> listFiltered;
    private final ListViewClickListener listViewClickListener;


    public StockReportAdapter(List<Stock> items, ListViewClickListener onItemClickListener) {
        mValues = items;
        listFiltered = items;
        this.listViewClickListener = onItemClickListener;
    }

    @Override
    public StockReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_report_item, parent, false);
        return new StockReportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StockReportAdapter.ViewHolder holder, final int position) {

        final Stock stockItem = listFiltered.get(position);
        holder.mItem = stockItem;
        holder.mGradeName.setText(stockItem.getGradeName());
        holder.mStockQty.setText(toQuantity(stockItem.getStockQuantity()));
        holder.mClottedQty.setText(toQuantity(stockItem.getClottedQty()));
        holder.mDamagedQty.setText(toQuantity(stockItem.getDamagedQty()));
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = mValues;
                } else {
                    List<Stock> filteredList = new ArrayList<>();
                    for (Stock row : mValues) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getGradeName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<Stock>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mGradeName;
        public final TextView mDamagedQty;
        public final TextView mClottedQty;
        public final TextView mStockQty;


        public Stock mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mGradeName = (TextView) view.findViewById(R.id.stock_report_grade_name);
            mStockQty = (TextView) view.findViewById(R.id.stock_report_stock_qty);
            mDamagedQty = (TextView) view.findViewById(R.id.stock_report_damaged_qty);
            mClottedQty = (TextView) view.findViewById(R.id.stock_report_clotted_qty);

        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}