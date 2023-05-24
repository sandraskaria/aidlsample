package com.example.mvp_aidl.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.demoaidl.IMyAidlCallback;
import com.example.demoaidl.IMyAidlInterface;
import com.example.mvp_aidl.presenter.IMainPresenter;


public class MainModel implements IMainModel {


    //    ------------------------AIDL----------------------------
    private IMyAidlInterface iMyAidlInterface;

    // Service connection to establish connection with service.
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                iMyAidlInterface.subscribe(aidlCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d("DEMO", "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                iMyAidlInterface.unSubscribe(aidlCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    // Return callback from service.
    private IMyAidlCallback.Stub aidlCallback = new IMyAidlCallback.Stub() {
        @Override
        public void notifySubtractToHmi(int subtractResult) throws RemoteException {
            mainPresenter.notifyToHmi(subtractResult);
        }
    };

    @Override
    public void bindToService(Context context) {
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.example.RemoteService.BIND");
        serviceIntent.setClassName("com.example.aidl_service", "com.example.aidl_service.AidlService");
        boolean ret = context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d("DEMO", "initService() bound with " + ret);
    }

    @Override
    public void calculateDiff(int first, int second) {
        if (iMyAidlInterface != null) {
            try {
                iMyAidlInterface.subtraction(first, second);
            } catch (RemoteException e) {
                Log.d("DEMO", e.toString());
                e.printStackTrace();
            }
        }
    }


//    ------------------------- MVP ----------------------

    private IMainPresenter mainPresenter;

    public MainModel(IMainPresenter mPresenter) {
        mainPresenter = mPresenter;
    }

    @Override
    public void calculateSum(int first, int second) {
        int sum = first + second;
        mainPresenter.showSumResult(sum);
    }
}
