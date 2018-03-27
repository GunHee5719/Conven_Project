package com.example.stproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by 장건희 on 2017-11-26.
 */

public class SigninActivity extends Activity {
    private int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        check=0;

        ImageView signinButton = (ImageView) findViewById(R.id.newSigninButton);
        ImageView returnBackButton = (ImageView) findViewById(R.id.returnBackButton);

        final EditText inputName = (EditText)findViewById(R.id.inputSignName);
        final EditText inputID = (EditText)findViewById(R.id.inputSignID);
        final EditText inputPW = (EditText)findViewById(R.id.inputSignPW);
        final EditText inputPhone = (EditText)findViewById(R.id.inputSignPhone);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern ps = Pattern.compile("^[ㄱ-ㅣ가-힣]*$");
                if(inputName.getText().toString().equals("")){
                    Toast.makeText(SigninActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(inputID.getText().toString().equals("")){
                    Toast.makeText(SigninActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(inputPW.getText().toString().equals("")){
                    Toast.makeText(SigninActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(inputPhone.getText().toString().equals("")){
                    Toast.makeText(SigninActivity.this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (ps.matcher(inputID.getText().toString()).matches()) {
                    Toast.makeText(SigninActivity.this, "아이디는 한글을 제외하고 영문과 숫자로만 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (ps.matcher(inputPW.getText().toString()).matches()) {
                    Toast.makeText(SigninActivity.this, "비밀번호는 한글을 제외하고 영문과 숫자로만 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    SignAccount temp = new SignAccount();
                    temp.execute(inputName.getText().toString(),inputID.getText().toString(),inputPW.getText().toString(),inputPhone.getText().toString());
                }
            }
        });

        returnBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(SigninActivity.this,LoginActivity.class);
                startActivity(a);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent a = new Intent(SigninActivity.this,LoginActivity.class);
        startActivity(a);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public class SignAccount extends AsyncTask<String,Void,String> {

        public String doInBackground(String ...params)
        {
            try{
                String input_name = params[0];
                String input_id = params[1];
                String input_pw = params[2];
                String input_phone = params[3];

                String url = "http://104.199.169.193/read_account.php?id="+ input_id;
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
                            Toast.makeText(SigninActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    String url2 = "http://104.199.169.193/write_account.php?id="+ input_id+"&name="+input_name+"&pwd="+input_pw+"&phone="+input_phone;
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
                Toast.makeText(SigninActivity.this, "계정이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(SigninActivity.this,LoginActivity.class);
                startActivity(a);
                finish();
            }
        }
    }
}
