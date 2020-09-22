package com.example.alexbacus_termscheduler.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alexbacus_termscheduler.Entities.TermEntity;
import com.example.alexbacus_termscheduler.R;
import com.example.alexbacus_termscheduler.TermDetail;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

        class TermViewHolder extends RecyclerView.ViewHolder {
            private final TextView termItemView;

            private TermViewHolder(View itemView) {
                super(itemView);
                termItemView = itemView.findViewById(R.id.textView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        final TermEntity current = mTerms.get(position);
                        Intent intent = new Intent(context, TermDetail.class);
                        intent.putExtra("termName", current.getTermName());
                        intent.putExtra("startDate", current.getStartDate());
                        intent.putExtra("endDate", current.getEndDate());
                        intent.putExtra("position",position);
                        intent.putExtra("termID",current.getTermID());
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent);
                    }
                });
            }
        }

        private final LayoutInflater mInflater;
        private List<TermEntity> mTerms;
        private final Context context;

        public TermAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public TermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.part_list_item, parent, false);
            return new TermViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TermViewHolder holder, int position) {
            if (mTerms != null) {
                TermEntity current = mTerms.get(position);
                holder.termItemView.setText(current.getTermName());
            }
            else {
                holder.termItemView.setText("No word");
            }
        }

        public void setTerms(List<TermEntity> terms) {
            mTerms = terms;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (mTerms != null)
                return mTerms.size();
            else return 0;
        }
    }
