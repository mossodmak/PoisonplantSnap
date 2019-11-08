package com.example.mynavjava;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    View view;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemlongClick(view, getAdapterPosition());

                return true;
            }
        });

    }
    public void setDetails(Context ct,String title,  String image){
        TextView titleView = view.findViewById(R.id.rTitleTv);
        ImageView imageTv = view.findViewById(R.id.rImageTv);
        titleView.setText(title);
        Picasso.get().load(image).into(imageTv);


    }
    public void setGalleryDetails(Context ct,String title,  String image){
        TextView titleView = view.findViewById(R.id.gallery_title);
        ImageView imageTv = view.findViewById(R.id.gallery_image);
        titleView.setText(title);
        Picasso.get().load(image).into(imageTv);


    }
    private ViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemlongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;

    }
}
