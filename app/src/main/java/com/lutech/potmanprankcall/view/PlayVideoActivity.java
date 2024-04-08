package com.lutech.potmanprankcall.view;

import static com.lutech.potmanprankcall.view.StatusBarUtils.*;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.model.VideoCall;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PlayVideoActivity extends AppCompatActivity {
    private VideoView videoViewPlay;
    private ImageView imgHomePlayVideo, imgMenuPlayVideo, imgRepeatVideo, imgPauseVideo, imgNextVideo;
    ProgressBar progressBar;

    public static List<VideoCall> videoList = new ArrayList<>();

    int totalVideo, currentVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        hideStatusBar(this);

        setContentView(R.layout.playvideo_activity);

        initWidget();

        handleClick();

    }

    private void handleClick() {
        // pause
        imgPauseVideo.setOnClickListener(view -> {

            MainActivity.checkSoundAndVibarte();

            if (videoViewPlay.isPlaying()) {
                videoViewPlay.pause();
                imgPauseVideo.setImageResource(R.drawable.ic_play);
            } else {
                videoViewPlay.start();
                imgPauseVideo.setImageResource(R.drawable.ic_pause);
            }

        });
// neu no dang chay thi doi icon
        videoViewPlay.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    Log.d("fwetffiaf", "2323480923480");
                    imgPauseVideo.setImageResource(R.drawable.ic_pause);
                    return true;
                }
                return false;
            }
        });

        videoViewPlay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // phat xong thi pause video do
                imgPauseVideo.setImageResource(R.drawable.ic_play);
            }
        });


        // repeat
        imgRepeatVideo.setOnClickListener(view -> {

            MainActivity.checkSoundAndVibarte();
            videoViewPlay.seekTo(0);
            videoViewPlay.start();
            imgPauseVideo.setImageResource(R.drawable.ic_pause);

        });

        imgMenuPlayVideo.setOnClickListener(view -> {
        Intent intent = new Intent(PlayVideoActivity.this,ListVideoActivity.class);
        startActivity(intent);
        finish();
        });

        imgHomePlayVideo.setOnClickListener(v -> {
            MainActivity.checkSoundAndVibarte();
            finish();
        });


        imgNextVideo.setOnClickListener(view -> {

            MainActivity.checkSoundAndVibarte();

            if (!videoList.isEmpty()) {

                videoViewPlay.pause();
                progressBar.setVisibility(View.VISIBLE);


                if (currentVideo < totalVideo) {
                    currentVideo++;
                    Log.d("ext121212", currentVideo + " ");
                    String nextVideoUri = videoList.get(currentVideo).getVideoCall();
                    videoViewPlay.setVideoURI(Uri.parse(nextVideoUri));
                } else {
                    Log.d("extS1212", currentVideo + " ");
                    String nextVideoUri = videoList.remove((totalVideo - 1)).getVideoCall();
                    videoViewPlay.setVideoURI(Uri.parse(nextVideoUri));
                }


                videoViewPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        progressBar.setVisibility(View.GONE);
                        videoViewPlay.start();
                    }
                });


            }
        });

        imgHomePlayVideo.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();

            try {
                Intent intent = new Intent(PlayVideoActivity.this, ListVideoActivity.class);
                startActivityForResult(intent, 10);
                finish();
            } catch (Exception e) {
                Log.d("|#@32323", e.toString());
            }

        });
    }

    private void initWidget() {
        videoViewPlay = findViewById(R.id.videoViewPlay);
        imgHomePlayVideo = findViewById(R.id.imgHomePlayVideo);
        imgMenuPlayVideo = findViewById(R.id.imgMenuPlayVideo);
        imgRepeatVideo = findViewById(R.id.imgRepeatVideo);
        imgPauseVideo = findViewById(R.id.imgPauseVideo);
        imgNextVideo = findViewById(R.id.imgNextVideo);
        progressBar = findViewById(R.id.processBar);

        progressBar.setVisibility(View.GONE);
//        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);


        // value for listvideo
//        for (User user : CallFragment.videoCalls) {
//            videoList.add(user.getUrlVideo());
//        }

        getData();


        totalVideo = videoList.size();
        currentVideo = 0;


        videoViewPlay.setVideoPath(videoList.get(0).getVideoCall());

        progressBar.setVisibility(View.VISIBLE);


        videoViewPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                videoViewPlay.start();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {

            if (data != null) {
                progressBar.setVisibility(View.VISIBLE);

                VideoCall videoCall = data.getParcelableExtra("Object");

                Log.d("23045235", currentVideo + " ");

                for (int i = 0; i < videoList.size(); i++) {
                    if (videoList.get(i).getIdVideo().equals(videoCall.getIdVideo())) {
                        currentVideo = i;

                    }
                }
                videoViewPlay.setVideoPath(videoCall.getVideoCall());
                videoViewPlay.start();


            }
        }

    }

    private void getData() {


        try {

            // load json
            InputStream inputStream = PlayVideoActivity.this.getAssets().open("video_call.json");
            int size = inputStream.available();
            byte[] buffter = new byte[size];
            inputStream.read(buffter);
            inputStream.close();


            // fetch json

            String json;
            int max;


            json = new String(buffter, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            max = jsonArray.length();

            for (int i = 0; i < max; i++) {

                VideoCall videoCall = new VideoCall();

                JSONObject jsonObject = jsonArray.getJSONObject(i);


                videoCall.setIdVideo(jsonObject.getString("id"));
                videoCall.setAvatarVideo(jsonObject.getString("avatar"));
                videoCall.setVideoCall(jsonObject.getString("videoCall"));
                videoList.add(videoCall);
            }


        } catch (Exception e) {
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoViewPlay != null) {

            videoViewPlay = null;
        }

    }



}
