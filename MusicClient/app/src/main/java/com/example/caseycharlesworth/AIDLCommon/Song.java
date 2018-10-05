package com.example.caseycharlesworth.AIDLCommon;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    int mDay = 25;
    int mMonth = 4;
    int mYear = 2018;
    int mCash = 8988;
    int inCash = 8988;
    int outCash = 8988;
    String mDayOfWeek = "Wednesday";

    public Song() {

    }

    public Song(Parcel in) {
        mDay = in.readInt();
        mMonth = in.readInt();
        mYear = in.readInt();
        mCash = in.readInt();
        inCash = in.readInt();
        outCash = in.readInt();
        mDayOfWeek = in.readString();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mDay);
        out.writeInt(mMonth);
        out.writeInt(mYear);
        out.writeInt(mCash);
        out.writeInt(inCash);
        out.writeInt(outCash);
        out.writeString(mDayOfWeek);
    }


    public static final Creator<Song> CREATOR
            = new Creator<Song>() {

        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
