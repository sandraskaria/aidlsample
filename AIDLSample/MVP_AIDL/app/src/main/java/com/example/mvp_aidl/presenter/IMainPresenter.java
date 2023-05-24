package com.example.mvp_aidl.presenter;

import android.content.Context;

public interface IMainPresenter {

    void calculateSum(int first, int second);

    void calculateDiff(int first, int second);

    void bindToService(Context context);

    void showSumResult(int result);

    void notifyToHmi(int data);
}
