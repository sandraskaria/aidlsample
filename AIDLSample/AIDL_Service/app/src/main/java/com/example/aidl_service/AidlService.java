package com.example.aidl_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.example.demoaidl.IMyAidlCallback;
import com.example.demoaidl.IMyAidlInterface;

public class AidlService extends Service {
    public AidlService() {
    }

    RemoteCallbackList<IMyAidlCallback> mCallback = new RemoteCallbackList<>();

    int subtract;

    private IMyAidlInterface.Stub myBinder = new IMyAidlInterface.Stub() {
        @Override
        public void subtraction(int first, int second) throws RemoteException {
            subtract = first - second;
            notifyToHmi(subtract);
        }

        @Override
        public void subscribe(IMyAidlCallback aidlCallback) throws RemoteException {
            mCallback.register(aidlCallback);
            Log.d("DEMO", "Register callback");
        }

        @Override
        public void unSubscribe(IMyAidlCallback aidlCallback) throws RemoteException {
           mCallback.unregister(aidlCallback);
            Log.d("DEMO", "Unregister callback");
        }
    };

    private void notifyToHmi(int subtract) {
      int count = mCallback.beginBroadcast();
      for (int i=0;i<count;i++) {
          try {
              mCallback.getBroadcastItem(i).notifySubtractToHmi(subtract);
          } catch (RemoteException e) {
              e.printStackTrace();
              Log.d("DEMO", "notifyToHmi error: " + e.getMessage());
          }
          mCallback.finishBroadcast();
      }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DEMO", "Service onCreate called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DEMO", "Service onStartCommand called");
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
}