package com.example.cardmakerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class EditCardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ModelClass> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        recyclerView = findViewById(R.id.recyclerview);
        datalist = new ArrayList<>();

        datalist.add(new ModelClass(R.drawable.birthdaycard));
        datalist.add(new ModelClass(R.drawable.bday_1));
        datalist.add(new ModelClass(R.drawable.bday_3));
        datalist.add(new ModelClass(R.drawable.bday_4));
        datalist.add(new ModelClass(R.drawable.bday_5));
        datalist.add(new ModelClass(R.drawable.bday_6));
        datalist.add(new ModelClass(R.drawable.bday_7));
        datalist.add(new ModelClass(R.drawable.bday_8));


        AdapterClass adapter = new AdapterClass(datalist,this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}