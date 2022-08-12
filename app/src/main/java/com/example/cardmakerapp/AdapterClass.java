package com.example.cardmakerapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.DataViewHolder> {

    private ArrayList<ModelClass> imagelist;
    private Context mcontext;

    public AdapterClass(ArrayList<ModelClass> imagelist, Context mcontext) {
        this.imagelist = imagelist;
        this.mcontext = mcontext;
    }



    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.custome_card_layout,parent,false);
        return new DataViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        ModelClass modelclass = imagelist.get(position);
        holder.imageview.setImageResource(modelclass.getImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,FullImageView.class);
                intent.putExtra("image",modelclass.getImg());
                Log.e("image","iamfe=="+modelclass.getImg());
                mcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            imageview = itemView.findViewById(R.id.imageview);
        }
    }
}
