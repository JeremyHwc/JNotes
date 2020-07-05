package com.jeremy.jnotes.shortcourse.ipc.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
    private String content;
    private boolean isSendSuccess;

    public Message() {
    }

    protected Message(Parcel in) {
        this.content = in.readString();
        this.isSendSuccess = in.readByte() != 0;
    }

    public Message(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeByte(this.isSendSuccess ? (byte) 1 : (byte) 0);
    }

    // 如果需要同步服务端对于数据的修改回客户端，需要添加次方法
    public void readFromParcel(Parcel parcel) {
        content = parcel.readString();
        isSendSuccess = parcel.readByte() != 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSendSuccess() {
        return isSendSuccess;
    }

    public void setSendSuccess(boolean sendSuccess) {
        isSendSuccess = sendSuccess;
    }


}
