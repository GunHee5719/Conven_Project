package com.example.stproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 장건희 on 2017-11-30.
 */

public class DetailReviewActivity extends Activity {
    private String myName;
    private String myPhone;
    private String myID;
    private String myRecipeName;

    private TextView reviewArea;

    private String returnDest;
    private String returnType;
    private String returnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailreview);

        Intent a = getIntent();
        myName = a.getSerializableExtra("name").toString();
        myPhone = a.getSerializableExtra("phone").toString();
        myID = a.getSerializableExtra("id").toString();
        myRecipeName = a.getSerializableExtra("recipeName").toString();
        returnDest = a.getSerializableExtra("returnDest").toString();
        if(returnDest.equals("searchActivity")){
            returnType=a.getSerializableExtra("returnType").toString();
            returnData=a.getSerializableExtra("returnData").toString();
        }

        TextView loginInfor = (TextView)findViewById(R.id.loginInforAtDetailReview);
        loginInfor.setText("사용자명 : " + myName);

        TextView recipeName = (TextView)findViewById(R.id.recipeNameAtDetailReview);
        recipeName.setText(myRecipeName);

        reviewArea = (TextView)findViewById(R.id.reviewAtDetailReview);

        GetReview temp = new GetReview();
        temp.execute();
    }
    public class GetReview extends AsyncTask<String,Void,String> {

        public String doInBackground(String ...params)
        {
            try{
                String url = "http://104.199.169.193/read_fullreview.php?recipeName="+ myRecipeName;

                URL obj = new URL(url); // URL 객체로 받고,

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection(); // open connection

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();

                while((line = reader.readLine())!=null)
                {
                    sb.append(line);
                }

                reader.close();
                return sb.toString();

            }catch(Exception e){

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s!= null)
            {
                // null 이 아닐경우 추가한다.
                try {
                    JSONObject jsonObject = new JSONObject(s); // s를 json화

                    JSONArray jsonArray = jsonObject.getJSONArray("data"); // data를 기준으로 가져온다.

                    if(jsonArray.length() ==0) {
                        reviewArea.setText("작성된 리뷰가 없습니다.");
                    }
                    else
                    {
                        String fullReview="<Review List>\n";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i); // Object를 가져오고

                            String temp = item.getString("content");
                            fullReview += "\n";
                            fullReview += String.valueOf(i+1);
                            fullReview += ". ";
                            fullReview += temp;
                        }
                        reviewArea.setText(fullReview);
                    }


                }catch(JSONException e){}
            }

        }
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(DetailReviewActivity.this,DetailRecipeActivity.class);
        a.putExtra("name",myName);
        a.putExtra("phone",myPhone);
        a.putExtra("id",myID);
        a.putExtra("recipeName",myRecipeName);
        a.putExtra("returnDest",returnDest);
        a.putExtra("returnType",returnType);
        a.putExtra("returnData",returnData);
        startActivity(a);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
