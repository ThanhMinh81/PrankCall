package com.lutech.potmanprankcall.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.Interface.ItemClickListenerCall;

import com.lutech.potmanprankcall.model.User;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    ArrayList<User> users;

    Context context;

    ItemClickListenerCall itemClickListenerCall;


    public CallAdapter(ArrayList<User> users, Context context, ItemClickListenerCall itemClickListenerCall) {
        this.users = users;
        this.context = context;
        this.itemClickListenerCall = itemClickListenerCall;
    }

    @NonNull
    @Override
    public CallAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_layout_call, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.ViewHolder holder, int position) {

        User user = users.get(position);

        Log.d("r0w-er", user.getPersonAvt());
        try {
            Glide.with(context).load(user.getPersonAvt()).into(holder.imgAvtar);
        } catch (Exception e) {
            Log.d("730249204", e.toString());
        }


        holder.imgAvtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListenerCall.callPerson(user);
            }
        });

    }


    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvtar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvtar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
