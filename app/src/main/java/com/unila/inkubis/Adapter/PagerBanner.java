package com.unila.inkubis.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.unila.inkubis.R;

public class PagerBanner extends PagerAdapter {
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public PagerBanner(Context context, ArrayList<String> images){
        this.context = context;
        this.images = images;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_banner, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.iv_banner);
        Picasso.get()
                .load("http://inkubator.sikubis.com/uploads/file/"+images.get(position))
                .into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
