package com.lutech.potmanprankcall.adapter;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.Interface.ItemClickListenerMessage;
import com.lutech.potmanprankcall.model.UserWithChat;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class DirectAdapter extends RecyclerView.Adapter<DirectAdapter.ViewHolder> {

    ArrayList<UserWithChat> userWithChats;

    ItemClickListenerMessage itemClickListenerMessage;

    Context context;

    public DirectAdapter(ArrayList<UserWithChat> userWithChats, ItemClickListenerMessage itemClickListenerMessage, Context context) {
        this.userWithChats = userWithChats;
        this.itemClickListenerMessage = itemClickListenerMessage;
        this.context = context;
    }

    @NonNull
    @Override
    public DirectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_layout_mess, parent, false);
        return new DirectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectAdapter.ViewHolder holder, int position) {


        UserWithChat userWithChat = userWithChats.get(position);

        Glide.with(context).load(userWithChat.getUser().getPersonAvt()).into(holder.shapeableImageView);
        Log.d("erw7r0wq523432---3--3-3", userWithChat.getUser().getPersonAvt());


        holder.tvName.setText(userWithChat.getUser().getPersonName());
        if (userWithChat.getListChat().size() > 0) {

            int totalList = userWithChat.getListChat().size() - 1;

            String message = userWithChat.getListChat().get(totalList).getMessageText();
            Log.d("234234234324234", userWithChat.getUser().getPersonName());

            if (message.contains("img")) {
                String[] parts = message.split("<img src='");
                SpannableStringBuilder spannableString = new SpannableStringBuilder();

                for (String part : parts) {
                    if (part.contains("ic_smiling")) {
                        String smilingDrawable = part.substring(0, part.indexOf("'"));

                        int resourceId = context.getResources().getIdentifier(smilingDrawable, "drawable", context.getPackageName());
                        Drawable iconDrawable = null;
                        if (resourceId != 0) {
                            try {
                                XmlResourceParser parser = context.getResources().getXml(resourceId);
                                iconDrawable = Drawable.createFromXml(context.getResources(), parser);
                            } catch (Exception e) {
                                Log.d("Fasfasf", e.toString() + " " + message + " - " + smilingDrawable);
                            }
                        }
                        if (iconDrawable != null) {
                            int iconSize = (int) (holder.tvMess.getTextSize());
                            iconDrawable.setBounds(0, 0, iconSize, iconSize);
                            ImageSpan imageSpan = new ImageSpan(iconDrawable, ImageSpan.ALIGN_BASELINE);
                            int startIndex = spannableString.length();
                            spannableString.append(" ");
                            spannableString.setSpan(imageSpan, startIndex, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    } else {
                        spannableString.append(part);
                    }
                }
                holder.tvMess.setText(spannableString);

            } else {
                holder.tvMess.setText(userWithChat.getListChat().get(totalList).getMessageText());
            }
        } else {
            holder.tvMess.setText("New message");
        }

        holder.constraintLayout.setOnClickListener(view -> {

            itemClickListenerMessage.clickMess(userWithChat);

        });

    }

    @Override
    public int getItemCount() {
        return userWithChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvMess;
        ConstraintLayout constraintLayout;

        ShapeableImageView shapeableImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvMess = itemView.findViewById(R.id.tvMess);
            constraintLayout = itemView.findViewById(R.id.layoutMessager);
            shapeableImageView = itemView.findViewById(R.id.circle);

        }
    }
}

