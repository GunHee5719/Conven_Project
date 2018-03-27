package com.example.stproject;

/**
 * Created by 장건희 on 2017-11-29.
 */

public class RecycleItem {
    String no;
    String text;
    String recipeName;

    public String getNo(){
        return no;
    }

    public String getText(){
        return text;
    }

    public String getRecipeName(){
        return recipeName;
    }


    public RecycleItem(String myNo, String myText,String myRecipeName){
        this.no = myNo;
        this.text = myText;
        this.recipeName=myRecipeName;
    }
}
