package com.vtcstudios.ntqproject.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HIEPKHACH9X on 3/17/2015.
 */
public class People implements Parcelable {
    String mAvatar;
    boolean mIsOnline;
    String mStatus;
    String mTextName;
    int mGender;
    int mAge;
    double mLog;
    double mLat;
    int mNotifications;
    double mDistance;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public People(String user_name, int gender, int age, String avatar, String status, int notifi, Boolean isOnline, double log, double lat, double dis) {
        mTextName = user_name;
        mGender = gender;
        mAge = age;
        mAvatar = avatar;
        mStatus = status;
        mNotifications = notifi;
        mIsOnline = isOnline;
        mLog = log;
        mLat = lat;
        mDistance = dis;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public boolean ismIsOnline() {
        return mIsOnline;
    }

    public void setmIsOnline(boolean mIsOnline) {
        this.mIsOnline = mIsOnline;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmTextName() {
        return mTextName;
    }

    public void setmTextName(String mTextName) {
        this.mTextName = mTextName;
    }

    public int getmGender() {
        return mGender;
    }

    public void setmGender(int mGender) {
        this.mGender = mGender;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public double getmLog() {
        return mLog;
    }

    public void setmLog(double mLog) {
        this.mLog = mLog;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public int getmNotifications() {
        return mNotifications;
    }

    public void setmNotifications(int mNotifications) {
        this.mNotifications = mNotifications;
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }

}
