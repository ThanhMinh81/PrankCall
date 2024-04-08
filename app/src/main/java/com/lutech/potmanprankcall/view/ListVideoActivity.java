package com.lutech.potmanprankcall.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.Interface.ItemClickListenerVideo;
import com.lutech.potmanprankcall.adapter.ListVideoAdapter;
import com.lutech.potmanprankcall.model.VideoCall;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ListVideoActivity extends AppCompatActivity {


    ImageView icBack;
    RecyclerView rcvListVideo;
    ListVideoAdapter listVideoAdapter;

    ArrayList<VideoCall> videoCalls;
    ItemClickListenerVideo itemClickListenerVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        StatusBarUtils.hideStatusBar(this);

        setContentView(R.layout.activity_list_video);

        icBack = findViewById(R.id.icBack);
        icBack.setOnClickListener(v -> {
            finish();
        });

        rcvListVideo = findViewById(R.id.rcvPlayVideo);

        itemClickListenerVideo = new ItemClickListenerVideo() {
            @Override
            public void clickVideo(VideoCall videoCall) {
                MainActivity.checkSoundAndVibarte();
                Intent intent = new Intent();
                intent.putExtra("Object", videoCall);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        };

        videoCalls = new ArrayList<>();
        getData();
        listVideoAdapter = new ListVideoAdapter(videoCalls, ListVideoActivity.this, itemClickListenerVideo);
        rcvListVideo.setAdapter(listVideoAdapter);
        rcvListVideo.setLayoutManager(new GridLayoutManager(this, 2));

    }


    private void getData() {

        ArrayList<VideoCall> listVideoLocal = new ArrayList<>();

        try {

            // load json
            InputStream inputStream = ListVideoActivity.this.getAssets().open("video_call.json");
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
                listVideoLocal.add(videoCall);
            }
            videoCalls.addAll(listVideoLocal);
            listVideoAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.d("eur0qrqrq", e.toString());
        }

    }


}
