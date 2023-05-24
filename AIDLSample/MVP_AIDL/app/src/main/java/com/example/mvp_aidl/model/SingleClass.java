package com.example.mvp_aidl.model;

public class SingleClass {

    public static SingleClass mInstance = null;

    private SingleClass() {

    }
    public static SingleClass getInstance() {
        if (mInstance == null) {
            mInstance = new SingleClass();
        }
        return mInstance;
    }
}
