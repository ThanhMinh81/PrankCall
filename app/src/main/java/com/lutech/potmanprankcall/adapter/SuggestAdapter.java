package com.lutech.potmanprankcall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.Interface.ItemClickListenerSuggestMessage;


import java.util.ArrayList;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.ViewHolder> {

    ArrayList<String> suggestMessList;
    ItemClickListenerSuggestMessage itemClickListenerSuggestMessage;

    Context context;

    public SuggestAdapter(ArrayList<String> suggestMessList, ItemClickListenerSuggestMessage itemClickListenerSuggestMessage, Context context) {
        this.suggestMessList = suggestMessList;
        this.itemClickListenerSuggestMessage = itemClickListenerSuggestMessage;
        this.context = context;
    }

    @NonNull
    @Override
    public SuggestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        View view;

        view = inflater.inflate(R.layout.layout_item_sugess_message, parent, false);


        return new SuggestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestAdapter.ViewHolder holder, int position) {

        String suggestMess = suggestMessList.get(position);

        if (suggestMess.contains("img")) {
            String[] parts = suggestMess.split("<img src='");
            SpannableStringBuilder spannableString = new SpannableStringBuilder();

            try {
                for (String part : parts) {
                    if (part.contains("ic_smiling")) {

                        String smilingDrawable = part.substring(0, part.indexOf("'"));

                        int resourceId = context.getResources().getIdentifier(smilingDrawable, "drawable", context.getPackageName());

                        // cai nay chut nua check api
                        Drawable iconDrawable =  context.getResources().getDrawable(resourceId);
//                        Drawable iDrawable =  ResourcesCompat.getDrawable();



                        if (iconDrawable != null) {

                            Log.d("Fasfasf", "Fasfasfa" + " ");
                            int iconSize = (int) (holder.tvSugess.getTextSize());
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

            } catch (Exception e) {
                Log.d("Fasfasf", e.toString());
            }
            holder.tvSugess.setText(spannableString);
        } else {
            holder.tvSugess.setText(suggestMess);
        }


        holder.linearLayout.setOnClickListener(view -> {
            itemClickListenerSuggestMessage.Suggest(suggestMess);
        });

    }

    @Override
    public int getItemCount() {
        return suggestMessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSugess;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutSuggest);
            tvSugess = itemView.findViewById(R.id.messageSend);
        }
    }
}
