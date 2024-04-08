package com.lutech.potmanprankcall.adapter;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.model.ChatMessage;


import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    ArrayList<ChatMessage> chatMessages;

    Context context;

    public MessageAdapter(ArrayList<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view;
        view = inflater.inflate(R.layout.item_layout_message, parent, false);

        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        ChatMessage chatMessage = chatMessages.get(position);

        String messReceive = chatMessage.getMessageText();

        if (messReceive.contains("img")) {
            String[] parts = messReceive.split("<img src='");
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
                            Log.d("Fasfasf", e.toString() + " " + messReceive + " - " + smilingDrawable);
                        }

                    }
                    if (iconDrawable != null) {

                        int iconSize = (int) (holder.tvReceive.getTextSize());
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

            holder.tvReceive.setText(spannableString);

        } else {
            holder.tvReceive.setText(chatMessage.getMessageText());
        }

        if (chatMessage.isChecked()) {

            holder.linearLayout.setGravity(Gravity.END);
            holder.tvReceive.setTextColor(ContextCompat.getColor(context, R.color.white));

            holder.tvReceive.setBackground(AppCompatResources.getDrawable(context, R.drawable.bg_item_message_send));

            Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular);
            holder.tvReceive.setTypeface(typeface, Typeface.NORMAL);


        } else if (!chatMessage.isChecked()) {

            holder.linearLayout.setGravity(Gravity.START);
            holder.tvReceive.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tvReceive.setBackground(AppCompatResources.getDrawable(context, R.drawable.bg_item_message));

            Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular);
            holder.tvReceive.setTypeface(typeface, Typeface.BOLD);
        }

        if (position == chatMessages.size() - 1) {
            Animation slideFromBottom = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_from_bottom);
            holder.linearLayout.setAnimation(slideFromBottom);
        }

    }


    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        TextView tvSend, tvReceive;
        TextView tvReceive;
        LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReceive = itemView.findViewById(R.id.messageRevice);
            linearLayout = itemView.findViewById(R.id.layoutParentMess);

        }
    }
}
