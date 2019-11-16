package com.example.mynavjava;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    public void setDetails(Context ct,String title,  String image ,String sciName){
        TextView titleView = view.findViewById(R.id.rTitleTv);
        ImageView imageTv = view.findViewById(R.id.rImageTv);
        TextView ciname = view.findViewById(R.id.sciname);
        titleView.setText(title);
        Picasso.get().load(image).into(imageTv);
        ciname.setText(sciName);


    }
    public void setGalleryDetails(Context ct,String title,  String image){
        TextView titleView = view.findViewById(R.id.gallery_title);
        ImageView imageTv = view.findViewById(R.id.gallery_image);
        titleView.setText(title);
        Picasso.get().load(image).into(imageTv);
    }
    public void setShareDetails(Context ct,String user,  String imageURL, String timestamp, String plant, String percent, Uri uri){
        TextView titleView = view.findViewById(R.id.gallery_title);
        TextView userView = view.findViewById(R.id.gallery_user);
        TextView dateView = view.findViewById(R.id.gallery_date);
        TextView percentView = view.findViewById(R.id.gallery_percent);
        ImageView imageTv = view.findViewById(R.id.gallery_image);
        ImageView profile_icon = view.findViewById(R.id.profile_icon);

        userView.setText(user);
        Picasso.get().load(imageURL).into(imageTv);
        dateView.setText(timestamp);
        titleView.setText(plant);
        percentView.setText(percent);
        if(uri != null){
            Picasso.get().load(uri).into(profile_icon);
        }
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
