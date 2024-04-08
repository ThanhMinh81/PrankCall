package com.lutech.potmanprankcall.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class VideoCall implements Parcelable {

    private String idVideo ;
    private String avatarVideo ;
    private String videoCall ;

    public VideoCall() {
    }

    public VideoCall(String idVideo, String avatarVideo, String videoCall) {
        this.idVideo = idVideo;
        this.avatarVideo = avatarVideo;
        this.videoCall = videoCall;
    }

    protected VideoCall(Parcel in) {
        idVideo = in.readString();
        avatarVideo = in.readString();
        videoCall = in.readString();
    }

    public static final Creator<VideoCall> CREATOR = new Creator<VideoCall>() {
        @Override
        public VideoCall createFromParcel(Parcel in) {
            return new VideoCall(in);
        }

        @Override
        public VideoCall[] newArray(int size) {
            return new VideoCall[size];
        }
    };

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String getAvatarVideo() {
        return avatarVideo;
    }

    public void setAvatarVideo(String avatarVideo) {
        this.avatarVideo = avatarVideo;
    }

    public String getVideoCall() {
        return videoCall;
    }

    public void setVideoCall(String videoCall) {
        this.videoCall = videoCall;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idVideo);
        dest.writeString(avatarVideo);
        dest.writeString(videoCall);
    }
}
