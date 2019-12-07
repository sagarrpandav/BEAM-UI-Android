package com.example.mrunal.beaminc;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Adapter_OwnerAllRequests extends ArrayAdapter<String>
{
    private String[] req,proj,mobile;
    private Activity activity;

    public Adapter_OwnerAllRequests(Activity activity,String[] req,String[] proj,String[] mobile)
    {
        super(activity,R.layout.adapter_all_projectorsss,req);
        this.activity=activity;
        this.req=req;
        this.proj=proj;
        this.mobile=mobile;
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

        tv1.setText(proj[position]);
        tv2.setText(mobile[position]);
        return row;

    }
}
