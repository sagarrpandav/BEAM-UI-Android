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

/**
 * Created by Mrunal on 22-03-2018.
 */

public class AdapterForAllCamps extends ArrayAdapter<String>
{
    private String[] campname;
    private boolean[] checkedCamps;
    private int[] campaign_duration;
    private Activity activity;

    public AdapterForAllCamps(Activity activity,String[] campname,String[] campids,boolean[] checkedCamps)
    {
        super(activity,R.layout.rowforallcamps,campids);
        this.activity=activity;
        this.campname=campname;
        this.checkedCamps=checkedCamps;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        row=layoutInflater.inflate(R.layout.rowforallcamps,null,true);

        TextView tv1;
        EditText ed1;
        CheckBox checkBox;
        tv1=(TextView)row.findViewById(R.id.tv_mycampaign_names);
        if(checkedCamps[position]) {
            checkBox = (CheckBox) row.findViewById(R.id.campaigns_checkbox);
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
        }
        tv1.setText(campname[position]);
        return row;

    }
}
