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
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Request_Client extends AppCompatActivity {

    ListView listView;
    String[] names,ids,mobiles;
    Activity activity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__client);

        listView=(ListView)findViewById(R.id.lv_request_client);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String tmpid=ids[i];
                String tmpmobi=mobiles[i];

                new Async_AddReq().execute(tmpmobi,tmpid);



            }
        });

    }

    class Async_AddReq extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/requestForProjector");

            try
            {
                Log.d("Inside Fetch","Camlps");

                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
                BasicNameValuePair mobile_= new BasicNameValuePair("client_mobile",strings[0]);
                //send key message to server with value
                list.add(mobile_);
                BasicNameValuePair mobile_1= new BasicNameValuePair("projector_id",strings[1]);
                //send key message to server with value
                list.add(mobile_1);
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

                Log.d("JSON ",s);
                Toast.makeText(activity, ""+s, Toast.LENGTH_SHORT).show();

                Log.d("Set Adapter ","Complete");
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





    class Async_FetchAll extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/getRentableProjectors");

            try
            {
                Log.d("Inside Fetch","Camlps");




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

                JSONArray jsonArray=new JSONArray(s);
                int len=jsonArray.length();
                names=new String[jsonArray.length()];
                ids=new String[jsonArray.length()];
                mobiles=new String[jsonArray.length()];
                for(int i=0;i<jsonArray.length();i++)
                {
                    names[i]=jsonArray.getJSONObject(i).getString("name");
                    ids[i]=jsonArray.getJSONObject(i).getString("id");
                    mobiles[i]=jsonArray.getJSONObject(i).getString("owner_mobile");
                }
                listView.setAdapter(new RequestAdapter_client(activity,names,ids,mobiles));
                Log.d("Set Adapter ","Complete");
            }
            catch (Exception e)
            {
                Log.d("Excep in FetchALL",e.toString());
            }
        }
    }

}


