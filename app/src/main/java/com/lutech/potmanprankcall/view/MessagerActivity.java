package com.lutech.potmanprankcall.view;

import static com.lutech.potmanprankcall.view.StatusBarUtils.*;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.adapter.MessageAdapter;
import com.lutech.potmanprankcall.adapter.SuggestAdapter;
import com.lutech.potmanprankcall.Interface.ItemClickListenerSuggestMessage;
import com.lutech.potmanprankcall.model.ChatMessage;
import com.lutech.potmanprankcall.model.UserWithChat;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class MessagerActivity extends AppCompatActivity {

    RecyclerView rcvMess, rcvSuggest;
    MessageAdapter messageAdapter;
    ArrayList<ChatMessage> messageList;
    SuggestAdapter suggestAdapter;
    ArrayList<String> suggesMessagetList;
    ArrayList<String> replyMessageList;
    ItemClickListenerSuggestMessage itemClickListenerSuggestMessage;
    LinearLayout layoutTyping;
    private MediaPlayer mediaSend;
    private MediaPlayer mediaReceive;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    Typewriter typewriter;

    TextView tvTyping;

    ImageView nav_back, imgCallMic, imgVideoCall;

    TextView tvNamePerson;

    ShapeableImageView imgAvatar;

    UserWithChat userWithChat;

    Vibrator vibrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        hideStatusBar(MessagerActivity.this);

        setContentView(R.layout.activity_mess);


        // nhan object va hien thi len giao dien

        userWithChat = getIntent().getExtras().getParcelable("Object");

        initWidget();

        handleEventClick();

        // rcv Message
        initValueRcv();
        // value message
        initValueMessageList();

    }

    private void initWidget() {

        tvNamePerson = findViewById(R.id.tvName);
        imgAvatar = this.<ShapeableImageView>findViewById(R.id.circleAvatar);
        imgCallMic = findViewById(R.id.imgCallMic);
        imgVideoCall = findViewById(R.id.imgVideoCall);

        layoutTyping = this.<LinearLayout>findViewById(R.id.layoutTyping);

        tvTyping = this.<TextView>findViewById(R.id.tvTyping);

        tvTyping.setVisibility(View.GONE);

        nav_back = this.<ImageView>findViewById(R.id.nav_back);

        Glide.with(this).load(userWithChat.getUser().getPersonAvt()).into(imgAvatar);
        tvNamePerson.setText(userWithChat.getUser().getPersonName());
        mediaSend = MediaPlayer.create(this, R.raw.sound_send);
        mediaReceive = MediaPlayer.create(this, R.raw.sound_receiver);
        typewriter = findViewById(R.id.typewriter);
        rcvMess = findViewById(R.id.rcvChat);
        rcvSuggest = findViewById(R.id.rcvSugessMess);
        messageList = new ArrayList<>();
        suggesMessagetList = new ArrayList<>();
        replyMessageList = new ArrayList<>();
        vibrate = (Vibrator) getSystemService(MessagerActivity.this.VIBRATOR_SERVICE);
    }

    private void handleEventClick() {

        itemClickListenerSuggestMessage = sugessMessage -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrate.vibrate(100);
            }

            ChatMessage chatMessage = new ChatMessage();
            addMessageList(sugessMessage, chatMessage);

        };

        imgCallMic.setOnClickListener(v -> {
            MainActivity.checkSoundAndVibarte();
            Intent intent = new Intent(this, OptionCallActivity.class);
            intent.putExtra("checkCallVideo", false);
            intent.putExtra("Object", userWithChat.getUser());
            startActivity(intent);
            finish();
        });

        imgVideoCall.setOnClickListener(v -> {
            MainActivity.checkSoundAndVibarte();
            Intent intent = new Intent(this, OptionCallActivity.class);
            intent.putExtra("checkCallVideo", true);
            intent.putExtra("Object", userWithChat.getUser());
            startActivity(intent);
            finish();
        });

        nav_back.setOnClickListener(view -> {

            sendResultAndFinish();

        });

    }

    private void sendResultAndFinish() {
        MainActivity.checkSoundAndVibarte();
        userWithChat.setListChat(messageList);
        Intent intent = new Intent();
        intent.putExtra("Object", userWithChat);
        setResult(RESULT_OK, intent);
        finish();
    }


    // lay vi tri
    public void addMessageList(String messageSend, ChatMessage chatMessage) {
        if (messageSend != null && chatMessage != null) {

            boolean check = false;

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mediaSend.start();
                    ChatMessage chatMessage1 = new ChatMessage();
                    chatMessage1.setUserId(userWithChat.getUser().getId());
                    chatMessage1.setMessageText(messageSend);
                    chatMessage1.setChecked(true);
                    messageList.add(chatMessage1);

                    rcvMess.scrollToPosition(messageList.size() - 1);


                    messageAdapter.notifyDataSetChanged();

                    typewriter.setVisibility(View.VISIBLE);
                    tvTyping.setVisibility(View.VISIBLE);
                    typewriter.animateText("...");

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mediaReceive.start();
                            String receive = referenMessage(suggesMessagetList.indexOf(messageSend));
                            ChatMessage chatMessage1 = new ChatMessage();
                            chatMessage1.setUserId(userWithChat.getUser().getId());
                            chatMessage1.setMessageText(receive);
                            chatMessage1.setChecked(false);
                            messageList.add(chatMessage1);
                            rcvMess.requestFocus();

                            rcvMess.smoothScrollToPosition(messageList.size() - 1);
                            messageAdapter.notifyDataSetChanged();
                            typewriter.stopAnimation();
                            typewriter.setVisibility(View.GONE);
                            tvTyping.setVisibility(View.GONE);
                        }
                    }, 600);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        sendResultAndFinish();
        super.onBackPressed();
    }


    // reply message
    private String referenMessage(int indexMessageSending) {
        String result = replyMessageList.get(indexMessageSending);
        return result;
    }

    private void initValueMessageList() {
        suggesMessagetList.addAll(Arrays.asList(MessagerActivity.this.getResources().getStringArray(R.array.message_send)));
        replyMessageList.addAll(Arrays.asList(MessagerActivity.this.getResources().getStringArray(R.array.message_reply)));
    }

    private void initValueRcv() {
        messageAdapter = new MessageAdapter(messageList, MessagerActivity.this);
        rcvMess.setAdapter(messageAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagerActivity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);

        rcvMess.setLayoutManager(linearLayoutManager);
        suggestAdapter = new SuggestAdapter(suggesMessagetList, itemClickListenerSuggestMessage, MessagerActivity.this);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        rcvSuggest.setLayoutManager(linearLayoutManager1);
        rcvSuggest.setAdapter(suggestAdapter);
        messageList.addAll(userWithChat.getListChat());
        messageAdapter.notifyDataSetChanged();

    }


}
