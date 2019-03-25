package com.mindproject.mindproject.add_request;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mindproject.mindproject.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import com.mindproject.mindproject.add_request.AddRequestActivity;
/**
 * Created by Nikita on 13.02.2019.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    //private Picasso mPicasso;
    private ArrayList<File> mFileList;
    private AddRequestFragment mActivity;

    public PhotosAdapter(AddRequestFragment fragment) {
        mFileList = new ArrayList<File>();
        mActivity = fragment;
        //mPicasso = Picasso.with(addRequestActivity.getApplicationContext());
    }

    //public void addPhotos(Uri photo){
    //    notifyDataSetChanged();
    //}

    public void addFile(File file){
        mFileList.add(file);
        notifyDataSetChanged();
    }

    public ArrayList<File> getFileList(){
        return mFileList;
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
        Glide
                .with(mActivity.getContext())
                .load(mFileList.get(position))
                .into(holder.imageViewEvent);
        holder.imageViewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFileList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewEvent = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
        }
    }



}
