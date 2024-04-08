package com.lutech.potmanprankcall.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;


/// day la bang noi
public class UserWithChat implements Parcelable {

    @Embedded
    private User user;

    @Relation(parentColumn = "id", entityColumn = "userId")
    private List<ChatMessage> listChat;


    public UserWithChat() {
    }


    protected UserWithChat(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        listChat = in.createTypedArrayList(ChatMessage.CREATOR);
    }

    public static final Creator<UserWithChat> CREATOR = new Creator<UserWithChat>() {
        @Override
        public UserWithChat createFromParcel(Parcel in) {
            return new UserWithChat(in);
        }

        @Override
        public UserWithChat[] newArray(int size) {
            return new UserWithChat[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<ChatMessage> getListChat() {
        return listChat;
    }

    public void setListChat(List<ChatMessage> listChat) {
        this.listChat = listChat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(user, i);
        parcel.writeTypedList(listChat);
    }
}
