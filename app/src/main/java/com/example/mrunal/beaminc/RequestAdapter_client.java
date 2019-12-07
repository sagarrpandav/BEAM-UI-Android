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

public class RequestAdapter_client extends ArrayAdapter<String>
{
    private String[] projnames,ids,mobiles;
    private Activity activity;

    public RequestAdapter_client(Activity activity,String[] projnames,String[] ids,String[] mobiles)
    {
        super(activity,R.layout.adapter_allrentable,ids);
        this.activity=activity;
        this.projnames=projnames;
        this.ids=ids;
        this.mobiles=mobiles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        row=layoutInflater.inflate(R.layout.adapter_allrentable,null,true);

        TextView tv1,tv2,tv3;

        tv1=(TextView)row.findViewById(R.id.tv_rent_projname);

        tv2=(TextView)row.findViewById(R.id.tv_rent_projid);

        tv3=(TextView)row.findViewById(R.id.tv_rent_owner);
        tv1.setText(projnames[position]);

        tv2.setText(ids[position]);

        tv3.setText(mobiles[position]);
        return row;

    }
}

