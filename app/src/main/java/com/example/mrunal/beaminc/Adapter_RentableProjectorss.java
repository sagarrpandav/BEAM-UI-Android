package com.example.mrunal.beaminc;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapter_RentableProjectorss extends ArrayAdapter<String>
{
    private String[] names,ids;
    private Activity activity;

    public Adapter_RentableProjectorss(Activity activity,String[] ids,String[] names)
    {
        super(activity,R.layout.adapter_all_projectorsss,ids);
        this.activity=activity;
        this.ids=ids;
        this.names=names;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        row=layoutInflater.inflate(R.layout.adapter_all_projectorsss,null,true);

        TextView tv1,tv2;

        tv1=(TextView)row.findViewById(R.id.tv_all_id);
        tv2=(TextView)row.findViewById(R.id.tv_all_name);

        tv1.setText(ids[position]);
        tv2.setText(names[position]);
        return row;

    }
}
