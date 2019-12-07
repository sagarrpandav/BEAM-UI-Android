package com.example.mrunal.beaminc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MyCampaigns extends AppCompatActivity {

    Button btn_select_projectors,btn_submit;
    TextView tv_show_projectors;
    ListView listView;
    String[] names;
    String[] campids;
    String[] finalCampids;
    boolean[] checkedItems;
    boolean[] checkedCamps;
    String[] checkedIds;
    int[] durations;
    int count_campaigns=0;
    String mobile=null;
    String[] mycamps,myprojnames,myprojids;
    boolean[] onlineflags;
    ArrayList<Integer> muserItems = new ArrayList<>();

    Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_campaigns);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        myprojnames=bundle.getStringArray("myprojnames");
        myprojids=bundle.getStringArray("myprojids");
        onlineflags=bundle.getBooleanArray("onlineflags");
        mobile=bundle.getString("mobile");
        listView=(ListView)findViewById(R.id.lv_my_campaigns);
        btn_select_projectors=(Button) findViewById(R.id.btn_select_projectors);
        btn_submit=(Button)findViewById(R.id.btn_submit_campaign);
        tv_show_projectors=(TextView) findViewById(R.id.show_projectors);

        new Async_FetchAllProjs().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //Toast.makeText(activity, "Clicked "+i, Toast.LENGTH_SHORT).show();
                if(checkedCamps[i])
                {
                    checkedCamps[i]=false;
                    count_campaigns--;
                }
                else {
                    checkedCamps[i] = true;
                    count_campaigns++;
                }
                listView.setAdapter(new AdapterForAllCamps(activity,mycamps,campids,checkedCamps));
            }
        });



        int onno=0;
        for(int u=0;u<myprojnames.length;u++)
        {
            if(onlineflags[u]) {
                //names[onno] = names_all[u];
                onno++;
            }
        }
        names= new String[onno];
        checkedIds=new String[onno];

        onno=0;
        for(int u=0;u<myprojnames.length;u++)
        {
            if(onlineflags[u]) {
                names[onno] = myprojnames[u];
                checkedIds[onno]=myprojids[u];
                onno++;
            }
        }


        for(int l=0;l<names.length;l++)
        {
            Log.d("names "+l,names[l]);
        }

        checkedItems=new boolean[names.length];


        btn_select_projectors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyCampaigns.this);
                mBuilder.setTitle("Projectors Available");
                mBuilder.setMultiChoiceItems(names, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            muserItems.add(Integer.parseInt(checkedIds[i]));
                            Log.d("Projector ID",checkedIds[i]);
                        }else{
                            muserItems.remove((Integer.valueOf(i)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item = "";
                        for(int j=0;j<muserItems.size();j++)
                        {
                            item = item + names[j];
                            if(j!=muserItems.size() -1)
                            {
                                item = item + ", ";
                            }
                        }
                        tv_show_projectors.setText(item);
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j=0;j<checkedItems.length;j++)
                        {
                            checkedItems[j]=false;
                            muserItems.clear();

                            tv_show_projectors.setText("");
                        }
                    }
                });

                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCampids=new String[count_campaigns];
                int j=0;
                for(int i=0;i<checkedCamps.length;i++)
                {
                    if(checkedCamps[i])
                    {
                        finalCampids[j]=campids[i];
                        j++;
                    }
                }
                if(count_campaigns>0 && muserItems.size()>0)
                {
                    for(int i=0;i<count_campaigns;i++)
                    {
                        new Async_SendImages().execute(i);
                    }
                }
                Toast.makeText(activity, "Submitted!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class Async_SendImages extends  AsyncTask<Integer,Void,String>
    {
        @Override
        protected String doInBackground(Integer... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/sendCampaigns");

            try
            {
                Log.d("Inside Send Message","Camlps");


                List<NameValuePair> list=new ArrayList<NameValuePair>(1);

                list.add(new BasicNameValuePair("count_projectors",""+muserItems.size()));
                list.add(new BasicNameValuePair("count_campaigns",""+count_campaigns));
                list.add(new BasicNameValuePair("campaign"+strings[0],finalCampids[strings[0]]));
                list.add(new BasicNameValuePair("duration",""+30));
                for(int m=0;m<muserItems.size();m++)
                {
                    Log.d("Adding -ProjectorID ",""+muserItems.get(m));
                    list.add(new BasicNameValuePair("projector"+m,""+muserItems.get(m)));
                }
                list.add(new BasicNameValuePair("mobile",mobile));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                ResponseHandler<String> responseHandler=new BasicResponseHandler();

                response=httpClient.execute(httpPost,responseHandler);
            }
            catch (Exception e)
            {
                Log.d("Exception In Async 2",e.toString());
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    class Async_FetchAll extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/fetchAllCamps");

            try
            {
                Log.d("Inside Fetch","Camlps");


                List<NameValuePair> list=new ArrayList<NameValuePair>(1);

                list.add(new BasicNameValuePair("mobile",mobile));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                ResponseHandler<String> responseHandler=new BasicResponseHandler();

                response=httpClient.execute(httpPost,responseHandler);
            }
            catch (Exception e)
            {
                Log.d("Exception In Async 2",e.toString());
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {

                Log.d("JSON ",s);
                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("Campaigns");
                mycamps=new String[jsonArray.length()];
                campids=new String[jsonArray.length()];
                checkedCamps=new boolean[jsonArray.length()];
                durations=new int[jsonArray.length()];
                for(int i=0;i<jsonArray.length();i++)
                {
                    campids[i]=jsonArray.getJSONObject(i).getString("Campaign_ID");
                    mycamps[i]=jsonArray.getJSONObject(i).getString("Campaign_Name");
                    Log.d("mycaps[i]",mycamps[i]);
                }
                listView.setAdapter(new AdapterForAllCamps(activity,mycamps,campids,checkedCamps));
                Log.d("Set Adapter ","Complete");
            }
            catch (Exception e)
            {
                Log.d("Excep in FetchALL",e.toString());
            }
        }
    }

    class Async_FetchAllProjs extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/getAvailableProjectors");

            try
            {
                Log.d("Inside Fetch","Camlps");

                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
                BasicNameValuePair mobile_= new BasicNameValuePair("user_mobile",mobile);
                //send key message to server with value
                list.add(mobile_);
                //list is ready

                try {


                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list); //checked exception therefore try catch block
                    httpPost.setEntity(urlEncodedFormEntity);
                }

                catch (UnsupportedEncodingException e)
                {
                    Log.d("Exception",e.toString());
                }


                ResponseHandler<String> responseHandler=new BasicResponseHandler();

                response=httpClient.execute(httpPost,responseHandler);
            }
            catch (Exception e)
            {
                Log.d("Exception In Async 2",e.toString());
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                Log.d("Response",s);
                JSONArray jsonArray=new JSONArray(s);
                int len=jsonArray.length();

                myprojids=new String[len];
                myprojnames=new String[len];
                //climobiles=new String[len];

                for (int i=0;i<len;i++)
                {
                    myprojids[i]=jsonArray.getJSONObject(i).getString("id");
                    myprojnames[i]=jsonArray.getJSONObject(i).getString("name");
                    //climobiles[i]=jsonArray.getJSONObject(i).getString("client_mobile");
                }

                //Adapter_OwnerAllRequests adapter_ownerAllRequests=new Adapter_OwnerAllRequests(activity,reqids,projids,climobiles);
                //all.setAdapter(adapter_ownerAllRequests);
            }
            catch (Exception e)
            {
                Log.d("Excep in FetchALL",e.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Async_FetchAll().execute();
    }
}
