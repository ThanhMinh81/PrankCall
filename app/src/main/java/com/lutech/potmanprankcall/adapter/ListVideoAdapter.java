package com.lutech.potmanprankcall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.Interface.ItemClickListenerVideo;
import com.lutech.potmanprankcall.model.VideoCall;


import java.util.ArrayList;

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.ViewHolder> {


    ArrayList<VideoCall> videoCalls;

    Context context;

    ItemClickListenerVideo iClickCall;


    public ListVideoAdapter(ArrayList<VideoCall> videoCalls, Context context, ItemClickListenerVideo iClickCall) {
        this.videoCalls = videoCalls;
        this.context = context;
        this.iClickCall = iClickCall;
    }

    @NonNull
    @Override
    public ListVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_layout_call, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ListVideoAdapter.ViewHolder holder, int position) {


        VideoCall videoCall = videoCalls.get(position);

        Glide.with(context).load(videoCall.getAvatarVideo()).into(holder.imgAvtar);


        holder.imgAvtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickCall.clickVideo(videoCall);
            }
        });


    }

    @Override
    public int getItemCount() {
        return videoCalls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvtar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvtar = itemView.findViewById(R.id.imgAvatar);

        }
    }
}
