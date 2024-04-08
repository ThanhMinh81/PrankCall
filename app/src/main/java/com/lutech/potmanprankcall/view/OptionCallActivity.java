package com.lutech.potmanprankcall.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.model.User;

import com.google.android.material.imageview.ShapeableImageView;

public class OptionCallActivity extends AppCompatActivity {

    ImageView imgCallMic, imgEndCall, imgAnimation;

    Boolean checkCallVideo = false;
    ShapeableImageView imgAvatarUser;
    TextView tvNameUserCall;
    User user;
    MediaPlayer mediaSoundCall;

    SharedPreferences pref;

    Vibrator vibrate;

    Intent intentCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        StatusBarUtils.hideStatusBar(this);

        setContentView(R.layout.activity_option_call);

        mediaSoundCall = MediaPlayer.create(this, R.raw.ring_phone);

        // get file share context
        pref = OptionCallActivity.this.getSharedPreferences("mode_setting", 0);

        // khoi tao rung
        vibrate = (Vibrator) getSystemService(OptionCallActivity.this.VIBRATOR_SERVICE);

        // check mode sound
        // and run
        checkSoundAndVibarte();
        initWidget();

        // xu ly cac button end call , mic , video call
        handleClickEvent();

        if (checkCallVideo) {
            imgCallMic.setImageResource(R.drawable.ic_callvideo);
            // animation border button call
            startAnimation(imgAnimation);
        } else {
            imgCallMic.setImageResource(R.drawable.ic_call);
            startAnimation(imgAnimation);
        }
    }

    private void handleClickEvent() {
        MainActivity.checkSoundAndVibarte();
        imgCallMic.setOnClickListener(v -> {

            if (mediaSoundCall != null && mediaSoundCall.isPlaying()) {
                mediaSoundCall.stop();
                mediaSoundCall.release();
                mediaSoundCall = null;
            }


            if (checkCallVideo) {
                intentCall = new Intent(OptionCallActivity.this, CallVideoActivity.class);
            } else {
                intentCall = new Intent(OptionCallActivity.this, CallMicActivity.class);

            }

            intentCall.putExtra("Object", user);
            startActivity(intentCall);
            finish();
        });

        imgEndCall.setOnClickListener(v -> {
            if (mediaSoundCall != null && mediaSoundCall.isPlaying()) {
                mediaSoundCall.stop();
                mediaSoundCall.release(); // delete catch
                mediaSoundCall = null;
            }
            MainActivity.checkSoundAndVibarte();
        });

    }

    private void checkSoundAndVibarte() {

        boolean soundClick = pref.getBoolean("sound", false);
        boolean vibarteClick = pref.getBoolean("ring", false);

        if (soundClick && vibarteClick) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {

                vibrate.vibrate(100);

            }

            mediaSoundCall.start();

        } else if (vibarteClick) {
            // rung
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrate.vibrate(100);
            }
        } else if (soundClick) {
            //  sound
            mediaSoundCall.start();

        }

    }

    private void initWidget() {
        checkCallVideo = (Boolean) getIntent().getExtras().get("checkCallVideo");
        user = getIntent().getExtras().getParcelable("Object");
        imgCallMic = this.<ImageView>findViewById(R.id.imgCallMicro);
        imgAvatarUser = findViewById(R.id.circle);
        imgEndCall = findViewById(R.id.imgEndCall);
        imgAnimation = findViewById(R.id.imgAnimation);
        tvNameUserCall = findViewById(R.id.tvNameCall);
        tvNameUserCall.setText(user.getPersonName());

        Glide.with(this).load(user.getPersonAvt()).into(imgAvatarUser);

    }

    public void startAnimation(View view) {
        ImageView imageView = (ImageView) view;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_anim);
        imageView.startAnimation(animation);
    }


}