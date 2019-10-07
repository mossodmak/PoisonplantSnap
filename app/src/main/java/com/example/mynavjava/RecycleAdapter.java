package com.example.mynavjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleAdapter extends RecyclerView.Adapter <RecycleAdapter.RecycleViewHolder> {

    private String[] sss;

    public RecycleAdapter(String[] s) {
        sss = s;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle, parent, false);
        return new RecycleViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        holder.text.setText(sss[position]);
    }

    @Override
    public int getItemCount() {
        return sss.length;
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textView_recycle);
        }
    }
}
