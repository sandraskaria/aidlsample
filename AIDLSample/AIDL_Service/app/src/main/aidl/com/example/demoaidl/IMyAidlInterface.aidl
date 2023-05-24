// IMyAidlInterface.aidl
package com.example.demoaidl;

import com.example.demoaidl.IMyAidlCallback;

interface IMyAidlInterface {
   void subtraction(int first, int second);

   void subscribe(IMyAidlCallback aidlCallback);

   void unSubscribe(IMyAidlCallback aidlCallback);
}