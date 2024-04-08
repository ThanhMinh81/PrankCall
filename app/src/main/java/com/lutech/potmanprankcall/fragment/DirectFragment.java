package com.lutech.potmanprankcall.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lutech.potmanprankcall.adapter.DirectAdapter;
import com.lutech.potmanprankcall.dao.MessageDao;
import com.lutech.potmanprankcall.Interface.ItemClickListenerMessage;
import com.lutech.potmanprankcall.model.SearchViewModel;
import com.lutech.potmanprankcall.model.UserWithChat;
import com.lutech.potmanprankcall.R;
import com.lutech.potmanprankcall.view.MainActivity;
import com.lutech.potmanprankcall.view.MessagerActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DirectFragment extends Fragment {

    RecyclerView rcvDirect;
    MessageDao messageDao;

    DirectAdapter directAdapter;
    ArrayList<UserWithChat> chatArrayList;
    View view;

    ItemClickListenerMessage itemClickListenerMessage;
//    EditText edSearchView ;

    ArrayList<UserWithChat> searchList;

    SharedPreferences sharedpreferences;


    public DirectFragment() {
    }

    public DirectFragment(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_direct, container, false);
        rcvDirect = view.findViewById(R.id.rcvDirect);
//        edSearchView = view.findViewById(R.id.edSearchView);

        chatArrayList = new ArrayList<>();
        searchList = new ArrayList<>();


        SearchViewModel searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        searchViewModel.getKeyWordSearch().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(String s) {
                searchViewFunc(s);
            }
        });

        sharedpreferences = getContext().getSharedPreferences("mode_setting", Context.MODE_PRIVATE);
        boolean ac = sharedpreferences.getBoolean("bababa", false);
        Log.d("3094702470", ac + " ");

        itemClickListenerMessage = userWithChat -> {

            MainActivity.checkSoundAndVibarte();
            Intent intent = new Intent(getActivity(), MessagerActivity.class);
            intent.putExtra("Object", userWithChat);
            startActivityForResult(intent, 10);
        };

        directAdapter = new DirectAdapter(chatArrayList, itemClickListenerMessage, getContext());

        rcvDirect = view.findViewById(R.id.rcvDirect);
        rcvDirect.setAdapter(directAdapter);
        rcvDirect.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        getData();

        return view;
    }

    private void searchViewFunc(String s) {

        MainActivity.checkSoundAndVibarte();



        if (s.length() == 0) {
            chatArrayList.clear();
            chatArrayList.addAll(searchList);
            directAdapter.notifyDataSetChanged();
        }
        if (s.length() > 0) {
            chatArrayList.clear();
            directAdapter.notifyDataSetChanged();
            for (UserWithChat userWithChat : searchList) {  

                if (userWithChat.getUser().getPersonName().trim().contains(s.toString().trim()) ||
                        userWithChat.getUser().getPersonName().toLowerCase().contains(s.toString().toLowerCase())) {
                    chatArrayList.add(userWithChat);
                    directAdapter.notifyDataSetChanged();
                }
            }
        }

    }


    @SuppressLint("CheckResult")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (data != null) {
                UserWithChat user = (UserWithChat) data.getParcelableExtra("Object");
                fetchDataAndUpdateDatabase(user)
                        .subscribe(() -> {
                            Log.d("fsf3", "Them data thanh cong");
                            getData();
                        }, error -> {
                            Log.e("4523", "Eloioi: " + error.getMessage());
                        });
            }
        }

    }

    private void getData() {

        Observable.fromCallable(() -> messageDao.getJoinChatPersons())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserWithChat>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull List<UserWithChat> userWithChats) {
                        chatArrayList.clear();
                        searchList.clear();
                        searchList.addAll(userWithChats);
                        chatArrayList.addAll(userWithChats);
                        directAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("394u922", e.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public Completable fetchDataAndUpdateDatabase(UserWithChat user) {
        return Completable.fromAction(() -> {
                    messageDao.deleteMessagesByUserId(user.getUser().getId());
                    messageDao.insertList(user.getListChat());
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }


}