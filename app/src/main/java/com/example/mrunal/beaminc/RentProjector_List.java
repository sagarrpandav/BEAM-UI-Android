package com.example.mrunal.beaminc;

import android.app.Activity;
import android.content.Intent;
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
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RentProjector_List extends AppCompatActivity {

    ListView all_list,rent_list;
    String[] all_id,all_name,rent_id,rent_name;
    String owner_mobile;
    Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_projector__list);
        all_list=(ListView)findViewById(R.id.lv_all_list_projectors);
        rent_list=(ListView) findViewById(R.id.lv_rented_list_projectors);


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        all_id=bundle.getStringArray("myprojids");
        all_name=bundle.getStringArray("myprojnames");
        owner_mobile=bundle.getString("mobile");

        adapter_all_projectorsss adapter_all_projectorsss=new adapter_all_projectorsss(activity,all_id,all_name);
        all_list.setAdapter(adapter_all_projectorsss);



        all_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String tmpID=all_id[i];
                Toast.makeText(activity, ""+tmpID, Toast.LENGTH_SHORT).show();
                new Async_AddToRents().execute(tmpID);
            }
        });

        rent_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tmpID=rent_id[i];
                Toast.makeText(activity, ""+tmpID, Toast.LENGTH_SHORT).show();
                new Async_RemoveFromRents().execute(tmpID);
            }
        });



    }

    class Async_RemoveFromRents extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/removeFromRentableProjectors");

            try
            {
                Log.d("Inside Fetch","Camlps");

                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
                BasicNameValuePair mobile= new BasicNameValuePair("projector_id",strings[0]);
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
                Log.d("RETURNs ",s);
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


    class Async_AddToRents extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/addToRentableProjectors");

            try
            {
                Log.d("Inside Fetch","Camlps");

                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
                BasicNameValuePair mobile= new BasicNameValuePair("projector_id",strings[0]);
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
                Log.d("RETURNs ",s);
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





    class Async_FetchAllRents extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/getRentableProjectorsPerOwner");

            try
            {
                Log.d("Inside Fetch","Camlps");

                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
                BasicNameValuePair mobile_= new BasicNameValuePair("user_mobile",owner_mobile);
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

                rent_id=new String[len];
                rent_name=new String[len];

                for (int i=0;i<len;i++)
                {
                    rent_id[i]=jsonArray.getJSONObject(i).getString("id");
                    rent_name[i]=jsonArray.getJSONObject(i).getString("name");
                }

                Adapter_RentableProjectorss adapter_rentableProjectorss=new Adapter_RentableProjectorss(activity,rent_id,rent_name);
                rent_list.setAdapter(adapter_rentableProjectorss);

            }
            catch (Exception e)
            {
                Log.d("Excep in FetchALL",e.toString());
            }
        }
    }
}
