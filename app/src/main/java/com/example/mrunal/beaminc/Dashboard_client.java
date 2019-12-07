package com.example.mrunal.beaminc;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard_client extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tv, tv_total_campaigns,tv_total_images,tv_total_videos,tv_total_files;
    Button bplus,btn_view_campaignlist,btn_req;
    ListView lv_live_campaigns;
    Activity activity=this;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_client);

        tv=(TextView)findViewById(R.id.Main_Welcome);
        tv_total_campaigns=(TextView) findViewById(R.id.tv_total_campaigns);
        tv_total_images=(TextView) findViewById(R.id.tv_total_images);
        tv_total_videos=(TextView) findViewById(R.id.tv_total_videos);
        tv_total_files=(TextView) findViewById(R.id.tv_total_files);
        bplus=(Button) findViewById(R.id.fab);
        btn_view_campaignlist=(Button) findViewById(R.id.btn_view_campaign_list);
        btn_req=(Button)findViewById(R.id.request) ;
        lv_live_campaigns=(ListView) findViewById(R.id.lv_live_campaigns);

        GridView gridview = (GridView) findViewById(R.id.gridview);

        btn_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Request_Client.class);
                startActivity(intent);
            }
        });

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
