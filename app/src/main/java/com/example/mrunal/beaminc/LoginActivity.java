package com.example.mrunal.beaminc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button buttonLogin;
    TextView textRegister;
    EditText ed_mobile,ed_pass;
    int total_images=0,total_videos=0,total_files=0,total_campaigns=0;
    String[] proj_id=null,proj_name=null,camp_id=null,camp_names=null;
    String user_type=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin=(Button)(findViewById(R.id.buttonLogin));
        ed_mobile=(EditText)findViewById(R.id.editLmobile);
        ed_pass=(EditText) findViewById(R.id.editLPassword);
        textRegister=(TextView)(findViewById(R.id.textRegister));

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_mobile.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Mobile", Toast.LENGTH_SHORT).show();
                }
                else if(ed_pass.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "ALL correct", Toast.LENGTH_SHORT).show();
                    Async_SendMsg asy= new Async_SendMsg();
                    Log.d("created0","async obj");
                    asy.execute(ed_mobile.getText().toString(),ed_pass.getText().toString());
                }
            }
        });



        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    class Async_SendMsg extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            Log.d("starting ...","async send");
            String response=null;                         //output from server
            HttpClient httpClient=new DefaultHttpClient(); //get,head,post,put
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/fromAndroidLogin");  //address of php rest file

            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(1);
            BasicNameValuePair mobile_= new BasicNameValuePair("mobile",strings[0]);
            BasicNameValuePair pass_= new BasicNameValuePair("password",strings[1]);
            //send key message to server with value

            list.add(pass_);
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
            try {
                response=httpClient.execute(httpPost, responseHandler); //httpClient triggers the httppost that contains
                // input and response handler returns output

            }
            catch (IOException e)
            {
                Log.d("Exception",e.toString());
            }
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("json",s);
            if(s.equals("false"))
            {
                Toast.makeText(getApplicationContext(), "Unregistered User or enter right Details ", Toast.LENGTH_LONG).show();

            }
            else
            {
                String name=null;

                boolean[] flag=null;
                int tmpflag=-1;
                try
                {
                    JSONObject main=new JSONObject(s);
                    name=main.getString("User_name");
                    Log.d("NAME ",name);

                    total_campaigns=main.getInt("total_campaigns");
                    total_images=main.getInt("total_images");
                    total_videos=main.getInt("total_videos");
                    user_type=main.getString("Type");
                    //total_files=main.getInt("total_files");

                    JSONArray jsonArray=main.getJSONArray("Proj_Data");
                    int no_proj=jsonArray.length();
                    proj_id=new String[no_proj];
                    proj_name=new String[no_proj];
                    camp_id=new String[no_proj];
                    flag=new boolean[no_proj];
                    camp_names=new String[no_proj];

                    for(int i=0;i<no_proj;i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        proj_id[i]=jsonObject.getString("Projector_ID");
                        proj_name[i]=jsonObject.getString("Projector_Name");
                        camp_id[i]=jsonObject.getString("Campaign_ID");
                        camp_names[i]=jsonObject.getString("Campaign_Name");
                        tmpflag=jsonObject.getInt("Flag");
                        if(tmpflag==1)
                        {
                            flag[i]=true;
                        }

                    }
                }
                catch (JSONException e)
                {
                    Log.d("JSON EXCEP",e.toString());
                }



                /*try
                {
                    //[{"Projector_ID": "1", "Projector_Name": "mrunalprojector1", "Campaign_ID": 0, "Flag_Connected": "666"},
                    // {"Projector_ID": "2", "Projector_Name": "mrunalproj2", "Campaign_ID": 0, "Flag_Connected": "666"}]
                    //JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray=new JSONArray(s);
                    int no_proj=jsonArray.length();

                    proj_id=new String[no_proj];
                    proj_name=new String[no_proj];
                    camp_id=new String[no_proj];
                    flag=new Boolean[no_proj];

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        proj_id[i]=jsonObject.getString("Projector_ID");
                        proj_name[i]=jsonObject.getString("Projector_Name");
                        camp_id[i]=jsonObject.getString("Campaign_ID");
                    }

                }
                catch (JSONException e)
                {
                    Log.d("JSON EXCEPTION ",e.toString());
                }*/
                Toast.makeText(getApplicationContext(), "SUCESSFULLY Logged in! ", Toast.LENGTH_SHORT).show();
                Intent i;
                if(user_type.equals("O"))
                {
                    i = new Intent(getApplicationContext(),Dashboard.class);
                }
                else
                {
                    i = new Intent(getApplicationContext(),Dashboard_client.class);
                }
                //i.putExtra(s,"json_data");
                i.putExtra("names",proj_name);
                i.putExtra("current_campaigns",camp_id);
                i.putExtra("uname",name);
                i.putExtra("user_type",user_type);
                i.putExtra("onlineflags",flag);
                i.putExtra("total_images",total_images);
                i.putExtra("total_videos",total_videos);
                //i.putExtra("total_files",total_files);
                i.putExtra("total_campaigns",total_campaigns);
                i.putExtra("camp_names",camp_names);
                i.putExtra("myprojids",proj_id);
                i.putExtra("mobile",ed_mobile.getText().toString());
                startActivity(i);
                finish();
            }

        }
    }

}