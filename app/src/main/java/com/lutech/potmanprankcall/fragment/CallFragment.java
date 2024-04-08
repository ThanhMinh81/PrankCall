package com.lutech.potmanprankcall.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lutech.potmanprankcall.adapter.CallAdapter;
import com.lutech.potmanprankcall.dao.MessageDao;
import com.lutech.potmanprankcall.Interface.ItemClickListenerCall;
import com.lutech.potmanprankcall.model.User;
import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.view.MainActivity;
import com.lutech.potmanprankcall.view.NetworkUtils;
import com.lutech.potmanprankcall.view.OptionCallActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class CallFragment extends Fragment {


    MessageDao messageDao;
    CallAdapter callAdapter;
    public static ArrayList<User> userArrayList;
    RecyclerView rcvCallFragment;
    ItemClickListenerCall itemClickListenerCall;
    View view;
    Dialog dialogCall;
    private Dialog diagloCall;

    public CallFragment() {
    }

    public CallFragment(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_call, container, false);

        dialogCall = new Dialog(getContext());

        userArrayList = new ArrayList<>();


        getData();

        itemClickListenerCall = new ItemClickListenerCall() {
            @Override
            public void callPerson(User user) {
                MainActivity.checkSoundAndVibarte();
                showDialogCall(user);
            }
        };

        // Kiểm tra kết nối mạng
        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            showNetworkDialog();
        }

        callAdapter = new CallAdapter(userArrayList, this.getContext(), itemClickListenerCall);

        rcvCallFragment = view.findViewById(R.id.rcvFragmentCall);



        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        rcvCallFragment.setLayoutManager(layoutManager);
        rcvCallFragment.setAdapter(callAdapter);
        rcvCallFragment.setLayoutManager(layoutManager);


        return view;



    }

    private void getData() {

        try {
            // load json
            InputStream inputStream = getContext().getAssets().open("data.json");
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

                User user = new User();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                user.setId(jsonObject.getInt("id"));
                user.setPersonName(jsonObject.getString("name"));
                user.setPersonAvt(jsonObject.getString("avatar"));
                user.setUrlVideo(jsonObject.getString("videoCall"));

                // tao 1 table video

                userArrayList.add(user);
            }
            callAdapter.notifyDataSetChanged();


        } catch (Exception e) {
        }

    }

    private void showNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Không có kết nối mạng");
        builder.setMessage("Vui lòng kiểm tra kết nối mạng của bạn.");
        builder.setPositiveButton("Cài đặt",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Chuyển đến cài đặt Wifi
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("Hủy",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogCall(User user) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.WrapContentDialog);
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        final View dialogView = layoutInflater.inflate(R.layout.layout_dialog_call, null);
        ImageView imgAvatar = dialogView.findViewById(R.id.imgAvatar);
        TextView tvName = dialogView.findViewById(R.id.tvName);
        tvName.setText(user.getPersonName());

        Glide.with(getContext()).load(user.getPersonAvt()).into(imgAvatar);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        // mrgintop cho dialog
        params.y = 16;
        window.setAttributes(params);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // cai nay cho no mau trong suot de co the thay duoc icon close
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setGravity(Gravity.CENTER);
        dialog.show();

        ImageView closeCall = dialog.findViewById(R.id.img_closeCall);

        ImageView videoCall = dialog.findViewById(R.id.imgVideoCall);

        ImageView microCall = dialog.findViewById(R.id.imgCallMicro);

        //videocall
        videoCall.setOnClickListener(v -> {

//            MainActivity.checkSoundAndVibarte();
//
            Intent intent = new Intent(getContext(), OptionCallActivity.class);
            intent.putExtra("checkCallVideo", true);
            intent.putExtra("Object", user);
            startActivity(intent);
            getActivity().finish();
            dialog.dismiss();


        });


        microCall.setOnClickListener(v -> {

//            MainActivity.checkSoundAndVibarte();

            Intent intent = new Intent(getContext(), OptionCallActivity.class);
            intent.putExtra("checkCallVideo", false);
            intent.putExtra("Object", user);

            startActivity(intent);
            getActivity().finish();
            dialog.dismiss();


        });


        closeCall.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();

            getActivity().finish();
            dialog.dismiss();

        });


    }


}