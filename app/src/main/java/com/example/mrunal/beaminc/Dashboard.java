package com.example.mrunal.beaminc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv, tv_total_campaigns,tv_total_images,tv_total_videos,tv_total_files;
    Button bplus,btn_view_campaignlist,btn_rent_projectors,btn_approve;
    ListView lv_live_campaigns;
    Activity activity=this;
    String mobile;

    String[] myprojnames,myprojids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tv=(TextView)findViewById(R.id.Main_Welcome);
        tv_total_campaigns=(TextView) findViewById(R.id.tv_total_campaigns);
        tv_total_images=(TextView) findViewById(R.id.tv_total_images);
        tv_total_videos=(TextView) findViewById(R.id.tv_total_videos);
        tv_total_files=(TextView) findViewById(R.id.tv_total_files);
        bplus=(Button) findViewById(R.id.fab);
        btn_view_campaignlist=(Button) findViewById(R.id.btn_view_campaign_list);
        lv_live_campaigns=(ListView) findViewById(R.id.lv_live_campaigns);
        btn_rent_projectors=(Button)findViewById(R.id.btn_make_project_rentable);
        btn_approve=(Button)findViewById(R.id.btn_approve_requests);

        GridView gridview = (GridView) findViewById(R.id.gridview);

        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        final String[] names=bundle.getStringArray("names");
        String uname=bundle.getString("uname");
        final boolean[] onlineflags=bundle.getBooleanArray("onlineflags");
        String[] live_campaigns=bundle.getStringArray("current_campaigns");
        int tvideos,timages,tfiles,tcampaigns;
        tvideos=bundle.getInt("total_videos");
        timages=bundle.getInt("total_images");
        //tfiles=bundle.getInt("total_files");
        myprojnames=bundle.getStringArray("myprojnames");
        myprojids=bundle.getStringArray("myprojids");
        mobile=bundle.getString("mobile");
        tcampaigns=bundle.getInt("total_campaigns");
        String[] camp_names=bundle.getStringArray("camp_names");
        final String[] myprojids=bundle.getStringArray("myprojids");

        mobile=bundle.getString("mobile");
        tv_total_campaigns.setText(""+tcampaigns);
        tv_total_images.setText(""+timages);
        tv_total_videos.setText(""+tvideos);
        tv_total_files.setText(""+(timages+tvideos));

        tv.setText(uname);

        Toast.makeText(getApplicationContext(),"Logged in Successfully",Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);                        //getSupportActionBar().setIcon(R.mipmap.home);
        getSupportActionBar().setTitle("Home");

        gridview.setAdapter(new ImageAdapter(this,names,onlineflags));

        lv_live_campaigns.setAdapter(new AdapterForLiveCamps(activity,names,camp_names));

        bplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),AddCampaign.class);
                i.putExtra("mobile",mobile);
                startActivity(i);
            }
        });

        btn_view_campaignlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MyCampaigns.class);
                intent.putExtra("mobile",mobile);
                intent.putExtra("myprojnames",names);
                intent.putExtra("myprojids",myprojids);
                intent.putExtra("onlineflags",onlineflags);
                startActivity(intent);
            }
        });



        btn_rent_projectors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),RentProjector_List.class);
                i.putExtra("mobile",mobile);
                i.putExtra("myprojnames",names);
                i.putExtra("myprojids",myprojids);
                startActivity(i);

            }
        });

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RequestOwnerActivity.class);
                intent.putExtra("owner",mobile);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
