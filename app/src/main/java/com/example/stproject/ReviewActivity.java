package com.example.stproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by 장건희 on 2017-11-29.
 */

public class ReviewActivity extends Activity {
    private String myName;
    private String myPhone;
    private String myID;
    private String myRecipeName;
    private String myOwnerID;
    private String myOwnerName;

    private String returnDest;
    private String returnType;
    private String returnData;

    private int score;
    private int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        score=0;
        check=0;

        Intent a = getIntent();
        myName = a.getSerializableExtra("name").toString();
        myPhone = a.getSerializableExtra("phone").toString();
        myID = a.getSerializableExtra("id").toString();
        myRecipeName = a.getSerializableExtra("recipeName").toString();
        myOwnerID=a.getSerializableExtra("ownerID").toString();
        myOwnerName=a.getSerializableExtra("ownerName").toString();
        returnDest = a.getSerializableExtra("returnDest").toString();
        if(returnDest.equals("searchActivity")){
            returnType=a.getSerializableExtra("returnType").toString();
            returnData=a.getSerializableExtra("returnData").toString();
        }


        TextView loginInfor = (TextView)findViewById(R.id.loginInforAtReview);
        loginInfor.setText("사용자명 : " + myName);

        TextView recipeName = (TextView)findViewById(R.id.recipeNameAtReview);
        recipeName.setText(myRecipeName);

        final ImageView star1_O = (ImageView)findViewById(R.id.Star1_O);
        final ImageView star2_O = (ImageView)findViewById(R.id.Star2_O);
        final ImageView star3_O = (ImageView)findViewById(R.id.Star3_O);
        final ImageView star4_O = (ImageView)findViewById(R.id.Star4_O);
        final ImageView star5_O = (ImageView)findViewById(R.id.Star5_O);
        final ImageView star1_X = (ImageView)findViewById(R.id.Star1_X);
        final ImageView star2_X = (ImageView)findViewById(R.id.Star2_X);
        final ImageView star3_X = (ImageView)findViewById(R.id.Star3_X);
        final ImageView star4_X = (ImageView)findViewById(R.id.Star4_X);
        final ImageView star5_X = (ImageView)findViewById(R.id.Star5_X);

        star1_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<1){
                    score=1;
                    star1_X.setVisibility(View.INVISIBLE);
                    star1_O.setVisibility(View.VISIBLE);
                }
            }
        });
        star1_O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score>1){
                    score=1;
                    star2_X.setVisibility(View.VISIBLE);
                    star3_X.setVisibility(View.VISIBLE);
                    star4_X.setVisibility(View.VISIBLE);
                    star5_X.setVisibility(View.VISIBLE);
                    star2_O.setVisibility(View.INVISIBLE);
                    star3_O.setVisibility(View.INVISIBLE);
                    star4_O.setVisibility(View.INVISIBLE);
                    star5_O.setVisibility(View.INVISIBLE);
                }
            }
        });
        star2_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<2){
                    score=2;
                    star1_X.setVisibility(View.INVISIBLE);
                    star2_X.setVisibility(View.INVISIBLE);
                    star1_O.setVisibility(View.VISIBLE);
                    star2_O.setVisibility(View.VISIBLE);
                }
            }
        });
        star2_O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score>2){
                    score=2;
                    star3_X.setVisibility(View.VISIBLE);
                    star4_X.setVisibility(View.VISIBLE);
                    star5_X.setVisibility(View.VISIBLE);
                    star3_O.setVisibility(View.INVISIBLE);
                    star4_O.setVisibility(View.INVISIBLE);
                    star5_O.setVisibility(View.INVISIBLE);
                }
            }
        });
        star3_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<3){
                    score=3;
                    star1_X.setVisibility(View.INVISIBLE);
                    star2_X.setVisibility(View.INVISIBLE);
                    star3_X.setVisibility(View.INVISIBLE);
                    star1_O.setVisibility(View.VISIBLE);
                    star2_O.setVisibility(View.VISIBLE);
                    star3_O.setVisibility(View.VISIBLE);
                }
            }
        });
        star3_O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score>3){
                    score=3;
                    star4_X.setVisibility(View.VISIBLE);
                    star5_X.setVisibility(View.VISIBLE);
                    star4_O.setVisibility(View.INVISIBLE);
                    star5_O.setVisibility(View.INVISIBLE);
                }
            }
        });
        star4_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<4){
                    score=4;
                    star1_X.setVisibility(View.INVISIBLE);
                    star2_X.setVisibility(View.INVISIBLE);
                    star3_X.setVisibility(View.INVISIBLE);
                    star4_X.setVisibility(View.INVISIBLE);
                    star1_O.setVisibility(View.VISIBLE);
                    star2_O.setVisibility(View.VISIBLE);
                    star3_O.setVisibility(View.VISIBLE);
                    star4_O.setVisibility(View.VISIBLE);
                }
            }
        });
        star4_O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score>4){
                    score=4;
                    star5_X.setVisibility(View.VISIBLE);
                    star5_O.setVisibility(View.INVISIBLE);
                }
            }
        });
        star5_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<5){
                    score=5;
                    star1_X.setVisibility(View.INVISIBLE);
                    star2_X.setVisibility(View.INVISIBLE);
                    star3_X.setVisibility(View.INVISIBLE);
                    star4_X.setVisibility(View.INVISIBLE);
                    star5_X.setVisibility(View.INVISIBLE);
                    star1_O.setVisibility(View.VISIBLE);
                    star2_O.setVisibility(View.VISIBLE);
                    star3_O.setVisibility(View.VISIBLE);
                    star4_O.setVisibility(View.VISIBLE);
                    star5_O.setVisibility(View.VISIBLE);
                }
            }
        });

        final EditText content = (EditText)findViewById(R.id.editReview);
        ImageView enterReviewButton = (ImageView)findViewById(R.id.enterReviewButton);

        enterReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score==0){
                    Toast.makeText(ReviewActivity.this, "평점을 별 개수로 나타내 주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    RegisterReview temp = new RegisterReview();
                    temp.execute(content.getText().toString());
                }
            }
        });
    }

    public class RegisterReview extends AsyncTask<String,Void,String> {

        public String doInBackground(String ...params)
        {
            try{
                String inputdata = params[0];

                String url = "http://104.199.169.193/read_review.php?evalUserID="+ myID+"&recipeName="+myRecipeName;
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
                JSONObject jsonObject = new JSONObject(sb.toString()); // s를 json화
                JSONArray jsonArray = jsonObject.getJSONArray("data"); // data를 기준으로 가져온다.

                if(jsonArray.length() !=0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ReviewActivity.this, "이미 후기를 등록하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    String content = inputdata.replace(System.getProperty("line.separator"), "\\n");

                    String url2 = "http://104.199.169.193/write_review.php?recipeName="+ myRecipeName+"&recipeOwnerName="+myOwnerName+"&recipeOwnerID="+myOwnerID+
                            "&evalUserID="+myID+"&evaluate="+score+ "&content="+content;
                    URL obj2 = new URL(url2); // URL 객체로 받고,

                    HttpURLConnection conn2 = (HttpURLConnection) obj2.openConnection(); // open connection

                    conn2.setReadTimeout(10000);
                    conn2.setConnectTimeout(15000);
                    conn2.setRequestMethod("GET");
                    conn2.setDoInput(true);
                    conn2.setDoOutput(true);

                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream(),"UTF-8"));

                    String line2;
                    StringBuilder sb2 = new StringBuilder();

                    while((line2 = reader2.readLine())!=null)
                    {
                        sb2.append(line2);
                    }

                    reader2.close();

                    check=1;
                    return sb2.toString();
                }

            }catch(Exception e){

                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(check==1){
                Toast.makeText(ReviewActivity.this, "후기 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(ReviewActivity.this,DetailRecipeActivity.class);
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
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("이전 화면으로 돌아가기");
        builder.setMessage("취소 시 작성중이던 내용은 저장되지 않습니다. 계속하시겠어요?");
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(ReviewActivity.this,DetailRecipeActivity.class);
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
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
