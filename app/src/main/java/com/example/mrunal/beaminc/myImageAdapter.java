package com.example.mrunal.beaminc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by Mrunal on 22-03-2018.
 */

public class myImageAdapter extends ArrayAdapter<String> {

    Activity activity;
    Uri[] filepaths;
    public myImageAdapter(Activity activity,Uri[] filepaths)
    {
        super(activity,R.layout.myimageadapterrow,new String[filepaths.length]);
        this.filepaths=filepaths;
        this.activity=activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        View row=layoutInflater.inflate(R.layout.myimageadapterrow,null,true);
        ImageView imageView=row.findViewById(R.id.img_adapter_row);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filepaths[position]);
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Log.d("Image Exception ",e.toString());
        }


        return  row;

    }
}
