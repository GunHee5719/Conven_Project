package com.example.stproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class DetailRecipeActivity extends Activity {
    private String myName;
    private String myPhone;
    private String myID;
    private String myRecipeName;
    private String myOwnerID;
    private String myOwnerName;

    private TextView recipeNameData;
    private TextView convinNameData;
    private TextView userNameData;
    private TextView difficultyData;
    private TextView quantityData;
    private TextView timeData;
    private TextView materialData;
    private TextView recipeData;
    private TextView goToReviewButtonText;

    private String returnDest;
    private String returnType;
    private String returnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailrecipe);

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

        recipeNameData = (TextView)findViewById(R.id.recipeNameAtDetail);
        convinNameData = (TextView)findViewById(R.id.convinNameAtDetail);
        userNameData = (TextView)findViewById(R.id.userNameAtDetail);
        difficultyData = (TextView)findViewById(R.id.difficultyAtDetail);
        quantityData = (TextView)findViewById(R.id.quantityAtDetail);
        timeData = (TextView)findViewById(R.id.timeAtDetail);
        materialData = (TextView)findViewById(R.id.materialAtDetail);
        recipeData = (TextView)findViewById(R.id.recipedetailAtDetail);
        goToReviewButtonText = (TextView)findViewById(R.id.goToReviewButtonText);

        TextView loginInfor = (TextView)findViewById(R.id.loginInforAtDetail);
        loginInfor.setText("사용자명 : " + myName);

        GetRecipe temp = new GetRecipe();
        temp.execute();

        ImageView goToReviewButton = (ImageView)findViewById(R.id.goToReviewButton);
        goToReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myOwnerID.equals(myID)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailRecipeActivity.this);

                    builder.setTitle("글쓴이 옵션");
                    builder.setMessage("레시피를 삭제하려면 삭제 버튼을 클릭하세요.\n후기 버튼을 클릭할 시 후기 내역을 볼 수 있습니다.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(DetailRecipeActivity.this);

                            builder3.setTitle("레시피 삭제");
                            builder3.setMessage("정말로 레시피를 삭제하시겠어요?");
                            builder3.setCancelable(true);
                            builder3.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeleteReview temp3 = new DeleteReview();
                                    temp3.execute();
                                    DeleteRecipe temp2 = new DeleteRecipe();
                                    temp2.execute();
                                }
                            });
                            builder3.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog dialog3 = builder3.create();
                            dialog3.show();
                        }
                    });
                    builder.setNegativeButton("후기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent a = new Intent(DetailRecipeActivity.this,DetailReviewActivity.class);
                            a.putExtra("name",myName);
                            a.putExtra("phone",myPhone);
                            a.putExtra("id",myID);
                            a.putExtra("recipeName",myRecipeName);
                            a.putExtra("returnDest",returnDest);
                            a.putExtra("returnType",returnType);
                            a.putExtra("returnData",returnData);
                            startActivity(a);
                            finish();
                        }
                    });
                    builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    Intent a = new Intent(DetailRecipeActivity.this,ReviewActivity.class);
                    a.putExtra("name",myName);
                    a.putExtra("phone",myPhone);
                    a.putExtra("id",myID);
                    a.putExtra("recipeName",myRecipeName);
                    a.putExtra("ownerID",myOwnerID);
                    a.putExtra("ownerName",myOwnerName);
                    a.putExtra("returnDest",returnDest);
                    a.putExtra("returnType",returnType);
                    a.putExtra("returnData",returnData);
                    startActivity(a);
                    finish();
                }
            }
        });
    }

    public class GetRecipe extends AsyncTask<String,Void,String> {
        public String doInBackground(String ...params)
        {
            try{
                String url = "http://104.199.169.193/read_recipe.php?recipeName="+myRecipeName;

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
                        Toast.makeText(getApplicationContext(), "Error : loading data", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i); // Object를 가져오고

                            String myRecipeNameData = item.getString("recipeName");
                            String myConvinNameData = item.getString("convinName");
                            String myUserNameData = item.getString("userName");
                            String myDifficultyData = item.getString("difficulty") + "단계";
                            String myQuantityData = item.getString("quantity") + "人";
                            String myTimeData = item.getString("time") + "분";
                            String myMaterialData =  item.getString("material");
                            String myRecipeData = item.getString("content");

                            myOwnerID = item.getString("userID");
                            myOwnerName = item.getString("userName");

                            if(myOwnerID.equals(myID)){
                                goToReviewButtonText.setText("글쓴이 옵션");
                            }

                            recipeNameData.setText(myRecipeNameData);
                            convinNameData.setText(myConvinNameData);
                            userNameData.setText(myUserNameData);
                            difficultyData.setText(myDifficultyData);
                            quantityData.setText(myQuantityData);
                            timeData.setText(myTimeData);
                            materialData.setText(myMaterialData);
                            recipeData.setText(myRecipeData);
                        }
                    }


                }catch(JSONException e){}
            }

        }
    }

    public class DeleteRecipe extends AsyncTask<String,Void,String> {

        public String doInBackground(String ...params)
        {
            try{
                String url = "http://104.199.169.193/delete_recipe.php?userID="+ myID + "&recipeName="+myRecipeName;

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

            Intent a = new Intent(DetailRecipeActivity.this,MainActivity.class);
            a.putExtra("name",myName);
            a.putExtra("phone",myPhone);
            a.putExtra("id",myID);
            startActivity(a);
            Toast.makeText(DetailRecipeActivity.this, "레시피가 삭제되었습니다. 메인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }

    public class DeleteReview extends AsyncTask<String,Void,String> {

        public String doInBackground(String ...params)
        {
            try{
                String url = "http://104.199.169.193/delete_review.php?recipeName="+ myRecipeName + "&recipeOwnerID="+myID;

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
        }
    }

    @Override
    public void onBackPressed(){
        if(returnDest.equals("searchActivity")){
            Intent a = new Intent(DetailRecipeActivity.this,SearchActivity.class);
            a.putExtra("name",myName);
            a.putExtra("phone",myPhone);
            a.putExtra("id",myID);
            a.putExtra("access","detail");
            a.putExtra("returnType",returnType);
            a.putExtra("returnData",returnData);
            startActivity(a);
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
        else{
            Intent a = new Intent(DetailRecipeActivity.this,MainActivity.class);
            a.putExtra("name",myName);
            a.putExtra("phone",myPhone);
            a.putExtra("id",myID);
            startActivity(a);
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }
}
