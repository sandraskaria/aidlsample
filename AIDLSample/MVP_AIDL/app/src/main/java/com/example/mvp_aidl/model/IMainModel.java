package com.example.mvp_aidl.model;

import android.content.Context;

public interface IMainModel {

    void calculateSum(int first, int second);

    void calculateDiff(int first, int second);

    void bindToService(Context context);

}
