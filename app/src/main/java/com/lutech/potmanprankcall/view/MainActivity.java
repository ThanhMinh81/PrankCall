package com.lutech.potmanprankcall.view;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.dao.MessageDao;
import com.lutech.potmanprankcall.fragment.CallFragment;
import com.lutech.potmanprankcall.fragment.DirectFragment;
import com.lutech.potmanprankcall.model.SearchViewModel;
import com.lutech.potmanprankcall.database.MessageDatabase;

public class MainActivity extends AppCompatActivity {

    CheckBox cbSetting, cbSound, cbVibrator;
    private NetworkChangeReceiver networkChangeReceiver;

    MessageDatabase messageDatabase;
    MessageDao messageDao;
    RadioButton rbNavigationCall, rbNavigationDirect;
    LinearLayout layoutCheckBoxSetting;

    ImageView imgPlayVideo;

    static SharedPreferences pref;

    // am thanh cua nut cai dat
    static MediaPlayer mediaSetting;

    static Vibrator vibrate;

    TextView tvCallWith;

    ConstraintLayout constraintLayoutToolbar;

    LinearLayout layoutSearchView;

    SearchViewModel searchViewModel;

    EditText edSearchView;
    private int currentApiVersion;

    boolean check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        StatusBarUtils.hideStatusBar(this);


        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);


            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }


        setContentView(R.layout.activity_main2);


        initWidget();


        messageDao = messageDatabase.messageDao();


        handleEventClick();


        rbNavigationCall.setChecked(true);
        changeColorBottomBar("Callwith");

        rbNavigationDirect.setChecked(false);
        rbNavigationCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton = (RadioButton) view;

                if (radioButton.isChecked()) {
                    checkSoundAndVibarte();
                    rbNavigationCall.setChecked(true);
                    rbNavigationDirect.setChecked(false);
                    changeColorBottomBar("Callwith");
                    replaceFragment(new CallFragment(messageDao));
                    changeSizeToolBar(tvCallWith, layoutSearchView, true);
                }


            }
        });

        rbNavigationDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton = (RadioButton) view;

                if (radioButton.isChecked()) {
                    checkSoundAndVibarte();
                    rbNavigationDirect.setChecked(true);
                    rbNavigationCall.setChecked(false);
                    changeColorBottomBar("Message");
                    replaceFragment(new DirectFragment(messageDao));
                    changeSizeToolBar(tvCallWith, layoutSearchView, false);

                }

            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void changeSizeToolBar(TextView tvCall, LinearLayout layoutSearchView, boolean call) {

        if (call) {
            tvCall.setVisibility(View.VISIBLE);
            layoutSearchView.setVisibility(View.GONE);

        } else {
            tvCall.setVisibility(View.GONE);
            layoutSearchView.setVisibility(View.VISIBLE);

        }

    }


    private void handleEventClick() {


        cbSetting.setOnClickListener(v -> {
            CheckBox checkBox1 = (CheckBox) v;

            checkSoundAndVibarte();

            if (checkBox1.isChecked()) {
                layoutCheckBoxSetting.setBackground(ActivityCompat.getDrawable(this, R.drawable.bg_checkbox));
                cbSound.setVisibility(View.VISIBLE);
                cbVibrator.setVisibility(View.VISIBLE);
            } else {
                layoutCheckBoxSetting.setBackground(ActivityCompat.getDrawable(this, R.drawable.bg_checkboxfalse));
                cbSound.setVisibility(View.GONE);
                cbVibrator.setVisibility(View.GONE);
            }
        });


        imgPlayVideo.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();

            Intent intent = new Intent(MainActivity.this, PlayVideoActivity.class);
            startActivity(intent);
        });

        cbSound.setOnClickListener(v -> {

            CheckBox cb = (CheckBox) v;

            if (cb.isChecked()) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("sound", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("sound", false);
                editor.apply();
            }
        });

        cbVibrator.setOnClickListener(v -> {

            CheckBox cb = (CheckBox) v;

            if (cb.isChecked()) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("ring", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("ring", false);
                editor.apply();
            }

        });
    }


    public static void checkSoundAndVibarte() {

        boolean soundClick = pref.getBoolean("sound", false);
        boolean vibarteClick = pref.getBoolean("ring", false);
        Log.d("soundClick", soundClick + " ");
        Log.d("vibarateClick", vibarteClick + " ");


        if (soundClick && vibarteClick) {

            // ca rung va am thanh

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrate.vibrate(100);
            }

            mediaSetting.start();

        } else if (vibarteClick) {
            // rung thoi
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrate.vibrate(100);
            }
        } else if (soundClick) {

            mediaSetting.start();

        }

    }


    private void changeColorBottomBar(String cbSelected) {
        if (cbSelected.contains("Call")) {
            // call dang dc chon
            ImageView imgCall = findViewById(R.id.imgCallWith);
            imgCall.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_IN);
            TextView titleCall = findViewById(R.id.titleCallWith);
            titleCall.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));


            TextView titleMess = findViewById(R.id.titleMessage);
            titleMess.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorIcNav));
            ImageView imgMessage = findViewById(R.id.imgMessage);
            imgMessage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorIcNav), PorterDuff.Mode.SRC_IN);

        } else {
            // Message dang duoc chon
            TextView titleMess = findViewById(R.id.titleMessage);
            titleMess.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            ImageView imgMessage = findViewById(R.id.imgMessage);
            imgMessage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_IN);

            ImageView imgCall = findViewById(R.id.imgCallWith);
            imgCall.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorIcNav), PorterDuff.Mode.SRC_IN);
            TextView titleCall = findViewById(R.id.titleCallWith);
            titleCall.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorIcNav));


        }
    }


    private void initWidget() {

        pref = MainActivity.this.getSharedPreferences("mode_setting", 0);

        vibrate = (Vibrator) getSystemService(MainActivity.this.VIBRATOR_SERVICE);

        messageDatabase = Room.databaseBuilder(this, MessageDatabase.class, "message-database").addCallback(MessageDatabase.roomCallback) // goi den static trong db
                .build();

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        boolean soundClick = pref.getBoolean("sound", false);
        boolean vibarteClick = pref.getBoolean("ring", false);

        cbSetting = findViewById(R.id.cbSetting);
        tvCallWith = findViewById(R.id.tvCallWith);
        cbSound = findViewById(R.id.cbSound);
        cbVibrator = findViewById(R.id.cbRing);
        imgPlayVideo = findViewById(R.id.imgPlayVideo);
        constraintLayoutToolbar = findViewById(R.id.constraintLayoutToolbar);
        layoutSearchView = findViewById(R.id.layoutSearchView);
        edSearchView = findViewById(R.id.edSearchView);

        mediaSetting = MediaPlayer.create(MainActivity.this, R.raw.sound_click);

        rbNavigationCall = this.<RadioButton>findViewById(R.id.rbNavigationCall);
        rbNavigationDirect = this.<RadioButton>findViewById(R.id.rbNavigationDirect);

        edSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchViewModel.setKeyWordSearch(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (soundClick) {
            cbSound.setChecked(true);
        }
        if (vibarteClick) {
            cbVibrator.setChecked(true);
        }

        //check permission and request
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10);
        } else {
        }

//        rbNavigationCall.setChecked(true);
//        changeColorNavigatonBottom(true);
//        rbNavigationDirect.setChecked(false);
//        colorCheckbox(rbNavigationCall);
//        colorCheckbox(rbNavigationDirect);

        RadioButton radioButton = findViewById(R.id.rbNavigationCall);
        Drawable drawable = radioButton.getCompoundDrawables()[1];
        if (drawable != null) {
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        }
        radioButton.setCompoundDrawables(null, drawable, null, null);

        layoutCheckBoxSetting = this.<LinearLayout>findViewById(R.id.layoutCheckbox);

        cbSound.setVisibility(View.GONE);
        cbVibrator.setVisibility(View.GONE);
        replaceFragment(new CallFragment(messageDao));
        changeSizeToolBar(tvCallWith, layoutSearchView, true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //   mo camera
            } else {
                // ko cho mo camera

            }
        }
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();

    }

    private void colorCheckbox(RadioButton checkBox) {
        if (checkBox.isChecked()) {
            checkBox.setTextColor(getResources().getColor(R.color.white));
            Drawable[] drawables = checkBox.getCompoundDrawablesRelative();
            Drawable drawableTop = drawables[1];
            drawableTop.setTint(getResources().getColor(R.color.white));
        } else {
            checkBox.setTextColor(this.getResources().getColor(R.color.colorForCbNotCheck, null));
            Drawable[] drawables = checkBox.getCompoundDrawablesRelative();
            Drawable drawableTop = drawables[1];
            drawableTop.setTint(getResources().getColor(R.color.colorForCbNotCheck, null));
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Khởi tạo BroadcastReceiver
        networkChangeReceiver = new NetworkChangeReceiver();

        // Đăng ký BroadcastReceiver với ACTION_CONNECTIVITY_CHANGE
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy đăng ký BroadcastReceiver khi activity bị pause
        unregisterReceiver(networkChangeReceiver);
    }

}