package com.example.stproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private String myName;
    private String myPhone;
    private String myID;
    private int recipeLength;

    private String[] recipeName;
    private String[] recipeUser;
    private double[] recipeEval;

    private LinearLayout no1;
    private LinearLayout no2;
    private LinearLayout no3;
    private LinearLayout no4;
    private LinearLayout no5;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent a = getIntent();
        myName = a.getSerializableExtra("name").toString();
        myPhone = a.getSerializableExtra("phone").toString();
        myID = a.getSerializableExtra("id").toString();

        TextView loginInfor = (TextView)findViewById(R.id.loginInfor);
        ImageView goToSearch = (ImageView)findViewById(R.id.goToSearchButton);
        ImageView register = (ImageView)findViewById(R.id.registerRecipeButton);

        loginInfor.setText("사용자명 : " + myName);

        goToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(MainActivity.this,SearchActivity.class);
                b.putExtra("name",myName);
                b.putExtra("phone",myPhone);
                b.putExtra("id",myID);
                b.putExtra("access","main");
                startActivity(b);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(MainActivity.this,RegisterActivity.class);
                c.putExtra("name",myName);
                c.putExtra("phone",myPhone);
                c.putExtra("id",myID);
                startActivity(c);
                finish();
            }
        });

        no1 = (LinearLayout)findViewById(R.id.popularLayoutNo1);
        no2 = (LinearLayout)findViewById(R.id.popularLayoutNo2);
        no3 = (LinearLayout)findViewById(R.id.popularLayoutNo3);
        no4 = (LinearLayout)findViewById(R.id.popularLayoutNo4);
        no5 = (LinearLayout)findViewById(R.id.popularLayoutNo5);

        text1 = (TextView)findViewById(R.id.popularTextNo1);
        text2 = (TextView)findViewById(R.id.popularTextNo2);
        text3 = (TextView)findViewById(R.id.popularTextNo3);
        text4 = (TextView)findViewById(R.id.popularTextNo4);
        text5 = (TextView)findViewById(R.id.popularTextNo5);

        GetPopularRecipe temp = new GetPopularRecipe();
        temp.execute();
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("App 종료");
        builder.setMessage("정말로 앱을 종료하시겠어요??");
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    public class GetPopularRecipe extends AsyncTask<String,Void,String> {

        public String doInBackground(String ...params)
        {
            try{
                String url = "http://104.199.169.193/popular_recipe.php";

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

                    recipeLength=jsonArray.length();
                    if(recipeLength !=0) {
                        recipeName = new String[recipeLength];
                        recipeUser = new String[recipeLength];
                        recipeEval = new double[recipeLength];

                        for (int i = 0; i < recipeLength; i++) {
                            JSONObject item = jsonArray.getJSONObject(i); // Object를 가져오고

                            recipeName[i] = item.getString("recipeName");
                            recipeUser[i] = item.getString("recipeOwnerName");
                            recipeEval[i] = item.getDouble("avg(evaluate)");
                        }
                    }


                }catch(JSONException e){}
            }
            no1.setVisibility(View.INVISIBLE);
            no2.setVisibility(View.INVISIBLE);
            no3.setVisibility(View.INVISIBLE);
            no4.setVisibility(View.INVISIBLE);
            no5.setVisibility(View.INVISIBLE);

            if(recipeLength>0){
                text1.setText("※ 음식 이름 : " + recipeName[0] + "\n※ 작성자 : " + recipeUser[0] + "\n※ 평점 : " + String.valueOf(recipeEval[0]));
                no1.setVisibility(View.VISIBLE);
                no1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(MainActivity.this,DetailRecipeActivity.class);
                        a.putExtra("name",myName);
                        a.putExtra("phone",myPhone);
                        a.putExtra("id",myID);
                        a.putExtra("recipeName",recipeName[0]);
                        a.putExtra("returnDest","mainActivity");
                        startActivity(a);
                        finish();
                    }
                });
            }
            if(recipeLength>1){
                text2.setText("※ 음식 이름 : " + recipeName[1] + "\n※ 작성자 : " + recipeUser[1] + "\n※ 평점 : " + String.valueOf(recipeEval[1]));
                no2.setVisibility(View.VISIBLE);
                no2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(MainActivity.this,DetailRecipeActivity.class);
                        a.putExtra("name",myName);
                        a.putExtra("phone",myPhone);
                        a.putExtra("id",myID);
                        a.putExtra("recipeName",recipeName[1]);
                        a.putExtra("returnDest","mainActivity");
                        startActivity(a);
                        finish();
                    }
                });
            }
            if(recipeLength>2){
                text3.setText("※ 음식 이름 : " + recipeName[2] + "\n※ 작성자 : " + recipeUser[2] + "\n※ 평점 : " + String.valueOf(recipeEval[2]));
                no3.setVisibility(View.VISIBLE);
                no3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(MainActivity.this,DetailRecipeActivity.class);
                        a.putExtra("name",myName);
                        a.putExtra("phone",myPhone);
                        a.putExtra("id",myID);
                        a.putExtra("recipeName",recipeName[2]);
                        a.putExtra("returnDest","mainActivity");
                        startActivity(a);
                        finish();
                    }
                });
            }
            if(recipeLength>3){
                text4.setText("※ 음식 이름 : " + recipeName[3] + "\n※ 작성자 : " + recipeUser[3] + "\n※ 평점 : " + String.valueOf(recipeEval[3]));
                no4.setVisibility(View.VISIBLE);
                no4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(MainActivity.this,DetailRecipeActivity.class);
                        a.putExtra("name",myName);
                        a.putExtra("phone",myPhone);
                        a.putExtra("id",myID);
                        a.putExtra("recipeName",recipeName[3]);
                        a.putExtra("returnDest","mainActivity");
                        startActivity(a);
                        finish();
                    }
                });
            }
            if(recipeLength>4){
                text5.setText("※ 음식 이름 : " + recipeName[4] + "\n※ 작성자 : " + recipeUser[4] + "\n※ 평점 : " + String.valueOf(recipeEval[4]));
                no5.setVisibility(View.VISIBLE);
                no5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(MainActivity.this,DetailRecipeActivity.class);
                        a.putExtra("name",myName);
                        a.putExtra("phone",myPhone);
                        a.putExtra("id",myID);
                        a.putExtra("recipeName",recipeName[4]);
                        a.putExtra("returnDest","mainActivity");
                        startActivity(a);
                        finish();
                    }
                });
            }
        }
    }
}
