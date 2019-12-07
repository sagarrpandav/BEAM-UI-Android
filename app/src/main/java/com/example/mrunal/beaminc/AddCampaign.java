package com.example.mrunal.beaminc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddCampaign extends AppCompatActivity {

    Button btn_selectimg,btn_selectvid,btn_selectproj,btn_submit;
    ListView lv_img,lv_vid;
    TextView tv_showproj;
    Activity activity=this;
    EditText ed_delay,ed_name;
    boolean[] checkedItems;
    Uri[] filepaths = new Uri[15];
    String[] names;
    ArrayList<Integer> muserItems = new ArrayList<>();
    int count=0;
    int seq=0;
    String campaignId=null;
    String campaign_name=null;
    String mobile;
    int delay=0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filepaths[count]=data.getData();
            count++;
            lv_img.setAdapter(new myImageAdapter(activity,filepaths));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_campaign);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mobile=bundle.getString("mobile");

        btn_selectimg=(Button) findViewById(R.id.bt_select_photo);
        btn_submit=(Button) findViewById(R.id.btn_submit);

        lv_img=(ListView) findViewById(R.id.lv_image);
        ed_delay=(EditText)findViewById(R.id.ed_delay);
        ed_name=(EditText)findViewById(R.id.ed_campaign_name);

        btn_selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                campaign_name=ed_name.getText().toString();
                delay=Integer.parseInt(ed_delay.getText().toString());

                if(count>0 && campaign_name!=null && delay>0)
                {
                    new Async_SendFirst().execute();
                }
                Toast.makeText(activity, "Submitted!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class Async_SendFirst extends AsyncTask<Integer,Void,String>
    {
        @Override
        protected String doInBackground(Integer... integers) {

            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/addNewCampaign");

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filepaths[0]);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                Log.d("Encoded String ", encodedImage);

                List<NameValuePair> list=new ArrayList<NameValuePair>(1);
                list.add(new BasicNameValuePair("name",campaign_name));
                list.add(new BasicNameValuePair("delay",""+delay));
                list.add(new BasicNameValuePair("mobile",mobile));
                list.add(new BasicNameValuePair("image",encodedImage));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                ResponseHandler<String> responseHandler=new BasicResponseHandler();

                response=httpClient.execute(httpPost,responseHandler);
            }
            catch (Exception e)
            {
                Log.d("Exception ",e.toString());
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
                campaignId=s;
                Log.d("Campaign Id :",s);
                seq++;
                if(seq<count)
                {
                    for(;seq<count;seq++)
                    {
                        new Async_SendRemaining().execute(seq);
                    }


                }
            }
            catch (Exception e)
            {
                Log.d("Exception in Async1 ",e.toString());
            }
        }
    }

    class Async_SendRemaining extends AsyncTask<Integer,Void,String>
    {
        @Override
        protected String doInBackground(Integer... integers)
        {
            String response=null;
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(getResources().getString(R.string.ip_address)+"/addImagesToCampaign");

            try
            {
                Log.d("Inside Send Remaining ",""+integers[0]);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filepaths[integers[0]]);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                Log.d("Encoded String ", encodedImage);

                List<NameValuePair> list=new ArrayList<NameValuePair>(1);

                list.add(new BasicNameValuePair("mobile",mobile));
                list.add(new BasicNameValuePair("delay",""+delay));
                list.add(new BasicNameValuePair("image",encodedImage));


                list.add(new BasicNameValuePair("campaign_id",campaignId));
                list.add(new BasicNameValuePair("sequence",""+seq));


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
                Log.d("Response for Sequence "+seq,s);

            }
            catch (Exception e)
            {
                Log.d("onPostAsync2 Error",e.toString());
            }
        }
    }

}
