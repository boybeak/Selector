package com.github.boybeak.demo;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by gaoyunfei on 2017/12/28.
 */

public class User extends Human implements Parcelable {

    private static Random sRandom = new Random();

    private Bundle bundle;
    private int type;

    public User() {
        bundle = new Bundle();
        type = sRandom.nextInt() % 2;
        bundle.putInt("type", type);
    }

    protected User(Parcel in) {
        bundle = in.readBundle();
        type = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Bundle bundle() {
        return bundle;
    }

    public int type () {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(bundle);
        parcel.writeInt(type);
    }
}
