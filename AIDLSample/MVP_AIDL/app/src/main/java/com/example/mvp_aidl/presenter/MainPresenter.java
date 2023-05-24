package com.example.mvp_aidl.presenter;

import android.content.Context;

import com.example.mvp_aidl.model.MainModel;
import com.example.mvp_aidl.view.IMainView;

public class MainPresenter implements IMainPresenter{
    MainModel mainModel;
    IMainView mView;

    public MainPresenter(IMainView view) {
        mView = view;
        mainModel = new MainModel(this);
    }

    @Override
    public void calculateSum(int first, int second) {
        mainModel.calculateSum(first,second);
    }

    @Override
    public void calculateDiff(int first, int second) {
       mainModel.calculateDiff(first, second);
    }

    @Override
    public void bindToService(Context context) {
      mainModel.bindToService(context);
    }

    @Override
    public void showSumResult(int result) {
        mView.showSum(result);
    }

    @Override
    public void notifyToHmi(int data) {
       mView.notifyToHmi(data);
    }
}
