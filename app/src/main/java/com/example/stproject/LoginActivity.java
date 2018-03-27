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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 장건희 on 2017-11-25.
 */

public class LoginActivity extends Activity {
    private boolean hasID;
    private String idFromServer;
    private String pwFromServer;
    private String nameFromServer;
    private String phoneFromServer;
    private EditText inputPWText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView loginButton = (ImageView) findViewById(R.id.loginButton);
        ImageView signinButton = (ImageView) findViewById(R.id.signinButton);

        final EditText inputLoginText = (EditText)findViewById(R.id.inputLoginID);
        inputPWText = (EditText)findViewById(R.id.inputLoginPW);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAccount temp = new GetAccount();
                temp.execute(inputLoginText.getText().toString());
            }
        });
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(LoginActivity.this,SigninActivity.class);
                startActivity(a);
                finish();
            }
        });
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
    public class GetAccount extends AsyncTask<String,Void,String> {

        public String doInBackground(String ...params)
        {
            try{
                String input_id = params[0];
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
                        hasID = false; // Login fail.
                        Toast.makeText(getApplicationContext(), "존재하지 않는 아이디입니다", Toast.LENGTH_SHORT).show(); // makeText를 한다.
                    }
                    else
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i); // Object를 가져오고

                            idFromServer = item.getString("id");
                            pwFromServer = item.getString("pwd");
                            nameFromServer = item.getString("name");
                            phoneFromServer = item.getString("phone");

                            hasID = true; // Success in Login.

                            if(hasID == true) // 존재하고
                            {
                                String s_pwd = inputPWText.getText().toString();
                                if(pwFromServer.equals(s_pwd)) // 비밀번호가 일치하다면
                                {
                                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다", Toast.LENGTH_SHORT).show(); // makeText를 한다.
                                    Intent a = new Intent(LoginActivity.this,MainActivity.class);
                                    a.putExtra("name",nameFromServer);
                                    a.putExtra("phone",phoneFromServer);
                                    a.putExtra("id",idFromServer);
                                    startActivity(a);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }


                }catch(JSONException e){}
            }

        }
    }
}
