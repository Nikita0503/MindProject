package com.mindproject.mindproject.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mindproject.mindproject.R;

import java.util.ArrayList;

/**
 * Created by Nikita on 14.02.2019.
 */

public class PhotoPagerAdapter extends PagerAdapter {


    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Bitmap> mImages;
    private ArrayList<Boolean> mScaled;

    public PhotoPagerAdapter(Context context, ArrayList<Bitmap> images) {
        mContext = context;
        mImages = images;
        mScaled = new ArrayList<Boolean>();
        for(int i = 0; i < mImages.size(); i++){
            Log.d("PHOTO", mImages.get(i).toString());
            mScaled.add(false);
        }
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_slider_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //imageView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        if(mScaled.get(position)) {
        //            mScaled.set(position, false);
        //            v.animate().scaleX(3.5f).scaleY(3.5f).setDuration(500);
        //        }else{
        //            mScaled.set(position, true);
        //            v.animate().scaleX(1f).scaleY(1f).setDuration(500);
        //        }
        //    }
        //});
        imageView.setImageBitmap(mImages.get(position));
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
