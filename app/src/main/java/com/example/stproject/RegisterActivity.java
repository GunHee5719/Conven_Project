package com.example.stproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 장건희 on 2017-11-28.
 */

public class RegisterActivity extends AppCompatActivity {
    private String myName;
    private String myPhone;
    private String myID;
    private GoogleApiClient client;

    private String selectedDifficulty;
    private String selectedQuantity;
    private String selectedTime;
    private int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        check=0;

        Intent a = getIntent();
        myName = a.getSerializableExtra("name").toString();
        myPhone = a.getSerializableExtra("phone").toString();
        myID = a.getSerializableExtra("id").toString();

        ImageView register = (ImageView)findViewById(R.id.registerButton);
        Spinner quantitySpinner = (Spinner) findViewById(R.id.quantitySpinner);
        Spinner difficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);
        Spinner timeSpinner = (Spinner)findViewById(R.id.timeSpinner);
        final ArrayAdapter sAdapter = ArrayAdapter.createFromResource(this, R.array.quantity, android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter sAdapter2 = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_dropdown_item);

        quantitySpinner.setAdapter(sAdapter);
        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQuantity=String.valueOf(sAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        difficultySpinner.setAdapter(sAdapter);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDifficulty=String.valueOf(sAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        timeSpinner.setAdapter(sAdapter2);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime=String.valueOf(sAdapter2.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        final EditText recipeName = (EditText)findViewById(R.id.inputRecipeName);
        final EditText convinName = (EditText)findViewById(R.id.inputRecipeConvinName);
        final EditText material = (EditText)findViewById(R.id.inputRecipeMaterial);
        final EditText detailRecipe = (EditText)findViewById(R.id.inputRecipeDetail);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeName.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "레시피 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(convinName.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "편의점 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(selectedDifficulty.equals("선택하세요")){
                    Toast.makeText(RegisterActivity.this, "난이도를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(selectedQuantity.equals("선택하세요")){
                    Toast.makeText(RegisterActivity.this, "인분 수를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(selectedTime.equals("선택하세요")){
                    Toast.makeText(RegisterActivity.this, "조리 시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(material.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "필요 재료를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(detailRecipe.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "레시피 설명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    WriteRecipe temp = new WriteRecipe();
                    temp.execute(recipeName.getText().toString(),convinName.getText().toString(),selectedDifficulty,selectedQuantity,
                            selectedTime,material.getText().toString(),detailRecipe.getText().toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("메인 화면으로 돌아가기");
        builder.setMessage("취소 시 작성중이던 내용은 저장되지 않습니다. 계속하시겠어요?");
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(RegisterActivity.this, MainActivity.class);
                a.putExtra("name", myName);
                a.putExtra("phone", myPhone);
                a.putExtra("id", myID);
                startActivity(a);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class WriteRecipe extends AsyncTask<String,Void,String> {

        public String doInBackground(String ...params)
        {
            try{
                String input_recipe_name = params[0];
                String input_convin_name = params[1];
                String input_difficulty = params[2];
                String input_quantity = params[3];
                String input_time = params[4];
                String input_material = params[5];
                String input_content = params[6];

                String url = "http://104.199.169.193/read_recipe.php?recipeName="+ input_recipe_name;
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
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 레시피 이름입니다. 새로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    String content = input_content.replace(System.getProperty("line.separator"), "\\n");

                    String url2 = "http://104.199.169.193/write_recipe.php?userID="+ myID+"&userName="+myName+"&recipeName="+input_recipe_name+"&convinName="+input_convin_name+
                            "&difficulty="+input_difficulty+"&quantity="+input_quantity+"&time="+input_time+"&material="+input_material+"&content="+content;
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
                Toast.makeText(RegisterActivity.this, "레시피가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(RegisterActivity.this,MainActivity.class);
                a.putExtra("name", myName);
                a.putExtra("phone", myPhone);
                a.putExtra("id", myID);
                startActivity(a);
                finish();
            }
        }
    }
}
