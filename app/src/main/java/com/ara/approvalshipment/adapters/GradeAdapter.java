package com.ara.approvalshipment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ara.approvalshipment.R;
import com.ara.approvalshipment.models.Grade;
import com.ara.approvalshipment.utils.ListViewClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.ara.approvalshipment.utils.Helper.formatDouble;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> implements Filterable {

    private final List<Grade> mValues;
    private List<Grade> gradeListFiltered;
    private final ListViewClickListener listViewClickListener;


    public GradeAdapter(List<Grade> items, ListViewClickListener onItemClickListener) {
        mValues = items;
        gradeListFiltered = items;
        this.listViewClickListener = onItemClickListener;
    }

    @Override
    public GradeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);
        return new GradeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GradeAdapter.ViewHolder holder, final int position) {

        final Grade grade = gradeListFiltered.get(position);
        holder.mItem = grade;
        holder.mGradeName.setText(grade.getGradeName());
        holder.mAvailableQty.setText(formatDouble(grade.getQuantity()));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewClickListener.onItemClick(mValues.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gradeListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    gradeListFiltered = mValues;
                } else {
                    List<Grade> filteredList = new ArrayList<>();
                    for (Grade row : mValues) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getGradeName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    gradeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = gradeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                gradeListFiltered = (ArrayList<Grade>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mGradeName;
        public final TextView mAvailableQty;


        public Grade mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mGradeName = (TextView) view.findViewById(R.id.list_grade_name);
            mAvailableQty = (TextView) view.findViewById(R.id.list_grade_avail_qty);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}