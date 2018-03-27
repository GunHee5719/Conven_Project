package com.example.stproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 장건희 on 2017-11-27.
 */

public class SearchActivity extends AppCompatActivity {
    private String myName;
    private String myPhone;
    private String myID;

    private String selectedSearchType;
    private String myRealSearchType;
    private String myRealSearchData;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<RecycleItem> myList = new ArrayList<RecycleItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent a = getIntent();
        myName = a.getSerializableExtra("name").toString();
        myPhone = a.getSerializableExtra("phone").toString();
        myID = a.getSerializableExtra("id").toString();
        String access = a.getSerializableExtra("access").toString();
        String returnType="";
        String returnData="";
        if(access.equals("detail")){
            returnType=a.getSerializableExtra("returnType").toString();
            returnData=a.getSerializableExtra("returnData").toString();
        }

        TextView loginInfor = (TextView)findViewById(R.id.loginInforAtSearch);
        loginInfor.setText("사용자명 : " + myName);

        ImageView returnBack = (ImageView)findViewById(R.id.goToMainAtSearchButton);
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(SearchActivity.this,MainActivity.class);
                a.putExtra("name",myName);
                a.putExtra("phone",myPhone);
                a.putExtra("id",myID);
                startActivity(a);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        Spinner serachSpinner = (Spinner)findViewById(R.id.searchSpinner);
        final ArrayAdapter sAdapter = ArrayAdapter.createFromResource(this, R.array.search, android.R.layout.simple_spinner_dropdown_item);
        serachSpinner.setAdapter(sAdapter);
        serachSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSearchType=String.valueOf(sAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.myRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(myList,this);
        recyclerView.setAdapter(adapter);

        ImageView searchButton = (ImageView)findViewById(R.id.searchButton);
        final EditText inputSearch = (EditText)findViewById(R.id.inputSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSearchType.equals("선택하세요")){
                    Toast.makeText(SearchActivity.this, "검색 조건을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(selectedSearchType.equals("이름으로 검색") && inputSearch.getText().toString().equals("")){
                        Toast.makeText(SearchActivity.this, "검색할 레시피 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if(selectedSearchType.equals("등록자로 검색") && inputSearch.getText().toString().equals("")){
                        Toast.makeText(SearchActivity.this, "검색할 작성자 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        myRealSearchType=selectedSearchType;
                        myRealSearchData=inputSearch.getText().toString();
                        GetRecipe temp = new GetRecipe();
                        temp.execute(inputSearch.getText().toString());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
                    }
                }
            }
        });

        ImageView searchAll = (ImageView)findViewById(R.id.searchAllButton);
        searchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSearchType="모두 검색";
                myRealSearchType=selectedSearchType;
                myRealSearchData="ghOwnType%^@";
                GetRecipe temp = new GetRecipe();
                temp.execute("ghOwnType%^@");
            }
        });

        if(access.equals("detail")){
            if(returnType.equals("모두 검색")){
                selectedSearchType="모두 검색";
                myRealSearchType=selectedSearchType;
                myRealSearchData="ghOwnType%^@";
                GetRecipe temp = new GetRecipe();
                temp.execute("ghOwnType%^@");
            }
            else{
                selectedSearchType=returnType;
                myRealSearchType=returnType;
                myRealSearchData=returnData;

                GetRecipe temp = new GetRecipe();
                temp.execute(returnData);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
            }
        }
    }

    public class GetRecipe extends AsyncTask<String,Void,String> {
        public String doInBackground(String ...params)
        {
            try{
                String input_data = params[0];
                String url;
                if(input_data.equals("ghOwnType%^@")){
                    url = "http://104.199.169.193/search_all.php";
                }
                else if(selectedSearchType.equals("이름으로 검색")){
                    url = "http://104.199.169.193/search_recipebyname.php?recipeName="+ input_data;
                }
                else{
                    url = "http://104.199.169.193/search_recipebyuser.php?userName="+ input_data;
                }

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
                        if(selectedSearchType.equals("이름으로 검색")){
                            Toast.makeText(getApplicationContext(), "해당 이름의 음식 레시피는 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if(selectedSearchType.equals("모두 검색")){
                            Toast.makeText(getApplicationContext(), "등록된 레시피가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "해당 이름의 유저가 등록한 레시피는 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        myList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i); // Object를 가져오고

                            String myRecipeName = item.getString("recipeName");
                            String myConvinName = item.getString("convinName");
                            String myOwnerName = item.getString("userName");

                            String temp = "※ 음식 이름 : " + myRecipeName + "\n※ 작성자 : " + myOwnerName + "\n※ 편의점 : " + myConvinName;

                            myList.add(new RecycleItem(String.valueOf(i+1),temp,myRecipeName));
                        }
                        adapter.notifyDataSetChanged();
                    }


                }catch(JSONException e){}
            }

        }
    }

    class MyAdapter extends RecyclerView.Adapter{
        private Context context;
        private ArrayList<RecycleItem> mItems;

        private int lastPosition=-1;

        public MyAdapter(ArrayList items, Context mContext){
            mItems=items;
            context=mContext;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,parent,false);
            ViewHolder holder = new ViewHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder)holder).searchNo.setText(mItems.get(position).getNo());
            ((ViewHolder)holder).searchText.setText(mItems.get(position).getText());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView searchNo;
            public TextView searchText;

            public ViewHolder(View itemView) {
                super(itemView);
                searchNo = (TextView)itemView.findViewById(R.id.searchNo);
                searchText = (TextView)itemView.findViewById(R.id.searchText);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getPosition();
                        Intent a = new Intent(SearchActivity.this,DetailRecipeActivity.class);
                        a.putExtra("name",myName);
                        a.putExtra("phone",myPhone);
                        a.putExtra("id",myID);
                        a.putExtra("recipeName",mItems.get(position).getRecipeName());
                        a.putExtra("returnDest","searchActivity");
                        a.putExtra("returnType",myRealSearchType);
                        a.putExtra("returnData",myRealSearchData);
                        startActivity(a);
                        finish();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(SearchActivity.this,MainActivity.class);
        a.putExtra("name",myName);
        a.putExtra("phone",myPhone);
        a.putExtra("id",myID);
        startActivity(a);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
