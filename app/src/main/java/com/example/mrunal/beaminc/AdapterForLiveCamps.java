package com.example.mrunal.beaminc;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Mrunal on 22-03-2018.
 */

public class AdapterForLiveCamps extends ArrayAdapter<String>
{
    private String[] projnames,campname;
    private Activity activity;
    public  AdapterForLiveCamps(Activity activity,String[] projnames,String[] campname)
    {
        super(activity,R.layout.rowforlivecamps,projnames);
        this.activity=activity;
        this.projnames=projnames;
        this.campname=campname;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=null;
        LayoutInflater layoutInflater=activity.getLayoutInflater();

            row = layoutInflater.inflate(R.layout.rowforlivecamps, null, true);

            TextView tv1, tv2;
            tv1 = (TextView) row.findViewById(R.id.tv_adap_camp);
            tv2 = (TextView) row.findViewById(R.id.tv_adap_proj);


            tv1.setText("Campaign ID : " + campname[position]);
            tv2.setText(projnames[position]);

            return row;

    }
}