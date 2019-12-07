package com.example.mrunal.beaminc;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mrunal on 22-03-2018.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private String[] names;
    boolean[] onlineflags;
    //private projector[] projectors;

    public ImageAdapter(Context c , String[] names,boolean[] onlineflags) {
        mContext = c;
        this.names=names;
        this.onlineflags=onlineflags;
        layoutInflater=LayoutInflater.from(mContext);
    }

    public int getCount() {
        try
        {
            return names.length;
        }
        catch (Exception e)
        {
            Log.d("Exce",e.toString());
        }
        return 0;

    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        /*if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else {
            imageView = (ImageView) convertView;
        }*/

        convertView=layoutInflater.inflate(R.layout.imgadapter,null,true);
        imageView=(ImageView)convertView.findViewById(R.id.icon);
        TextView tv=(TextView)convertView.findViewById(R.id.adap_tv);
        TextView tv_online=(TextView)convertView.findViewById(R.id.adap_online);
        imageView.setImageResource(mThumbIds[position]);
        if(onlineflags[position])
        {
            tv_online.setBackgroundColor(Color.GREEN);
        }

        tv.setText(names[position]);

        return convertView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.grids, R.drawable.grids,
            R.drawable.grids, R.drawable.grids,
            R.drawable.grids
    };
}
