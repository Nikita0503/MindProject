package com.mindproject.mindproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindproject.mindproject.data.EventData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nikita on 13.02.2019.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private Picasso mPicasso;
    private ArrayList<Uri> mPhotoList;
    private AddRequestActivity mActivity;

    public PhotosAdapter(AddRequestActivity addRequestActivity) {
        mPhotoList = new ArrayList<Uri>();
        mActivity = addRequestActivity;
        mPicasso = Picasso.with(addRequestActivity.getApplicationContext());
    }

    public void addPhotos(Uri photo){
        mPhotoList.add(photo);
        notifyDataSetChanged();
    }

    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_photo_item, parent, false);
        return new PhotosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosAdapter.ViewHolder holder, final int position) {
        //mPicasso.load(getItem(position)).resizeDimen(R.dimen.image_size, R.dimen.image_size). centerInside().into(imageView);
        //mPicasso.load(mPhotoList.get(position)).resizeDimen(R.dimen.image_size, R.dimen.image_size). centerInside().into(holder.imageViewEvent);
        holder.imageViewEvent.setImageURI(mPhotoList.get(position));
        holder.imageViewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewEvent = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
        }
    }



}
