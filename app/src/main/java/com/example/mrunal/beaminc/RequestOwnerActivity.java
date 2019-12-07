package com.example.mrunal.beaminc;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RequestOwnerActivity extends AppCompatActivity {

    ListView all,accepted;
    String[] reqids,projids,climobiles;
    String owner;
    Activity activity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_owner);

        owner=getIntent().getExtras().getString("owner");

        all=(ListView)findViewById(R.id.listview_owners_allreq);
        accepted=(ListView)findViewById(R.id.listview_owners_acceptedreq);

        all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String tmpreqid=reqids[i];
                new Async_AddToGrants().execute(tmpreqid,projids[i],climobiles[i]);
            }
        });

        accepted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
    class Async_FetchAllRents extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/viewAllRequestsOwner");

            try
            {
                Log.d("Inside Fetch","Camlps");

                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
                BasicNameValuePair mobile_= new BasicNameValuePair("owner_mobile",owner);
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

                reqids=new String[len];
                projids=new String[len];
                climobiles=new String[len];

                for (int i=0;i<len;i++)
                {
                    reqids[i]=jsonArray.getJSONObject(i).getString("request_id");
                    projids[i]=jsonArray.getJSONObject(i).getString("projector_id");
                    climobiles[i]=jsonArray.getJSONObject(i).getString("client_mobile");
                }

                Adapter_OwnerAllRequests adapter_ownerAllRequests=new Adapter_OwnerAllRequests(activity,reqids,projids,climobiles);
                all.setAdapter(adapter_ownerAllRequests);
            }
            catch (Exception e)
            {
                Log.d("Excep in FetchALL",e.toString());
            }
        }
    }

    class Async_AddToGrants extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/grantRequest");

            try
            {
                Log.d("Inside Fetch","Camlps");

                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
                BasicNameValuePair mobile= new BasicNameValuePair("request_id",strings[0]);

                BasicNameValuePair mobile1= new BasicNameValuePair("client_mobile",strings[1]);

                BasicNameValuePair mobile2= new BasicNameValuePair("projector_id",strings[2]);
                //send key message to server with value
                list.add(mobile);
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
                if(s.equals("true")) {
                    Toast.makeText(activity, "Projector added to Rentables", Toast.LENGTH_SHORT).show();
                    new Async_FetchAllRents().execute();
                }
                else
                {

                }
            }
            catch (Exception e)
            {
                Log.d("Excep in FetchALL",e.toString());
            }
        }
    }


    class Async_FetchAllaCCEPTEDrEQ extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/viewAllRequestsOwner");

            try
            {
                Log.d("Inside Fetch","Camlps");

                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
                BasicNameValuePair mobile_= new BasicNameValuePair("owner_mobile",owner);
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

                reqids=new String[len];
                projids=new String[len];
                climobiles=new String[len];

                for (int i=0;i<len;i++)
                {
                    reqids[i]=jsonArray.getJSONObject(i).getString("request_id");
                    projids[i]=jsonArray.getJSONObject(i).getString("projector_id");
                    climobiles[i]=jsonArray.getJSONObject(i).getString("client_mobile");
                }

                Adapter_OwnerAllRequests adapter_ownerAllRequests=new Adapter_OwnerAllRequests(activity,reqids,projids,climobiles);
                all.setAdapter(adapter_ownerAllRequests);
            }
            catch (Exception e)
            {
                Log.d("Excep in FetchALL",e.toString());
            }
        }
    }


}
