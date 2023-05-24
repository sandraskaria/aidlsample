/**
 * @file        Timer.java
 * @brief       This class contains logic related to timer.
 * @copyright   COPYRIGHT (C) 2018 MITSUBISHI ELECTRIC CORPORATION
 *              ALL RIGHTS RESERVED
 * @author      Jayadev
 */

package com.example.mvp_aidl.model;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;


/**
 * @brief This class contains logic related to timer.
 */
public abstract class Timer {
    /**
     * Static constant to hold the value for handler message. Used to know thr running status of
     * timer.
     */
    private static final int MSG = 1;

    /**
     * Static constant to hold the interval time in milli seconds.
     */
    private static final long INTERVAL = 10;

    /**
     * Variable to hold the running status of timer
     */
    private boolean mRunningStatus = false;

    /**
     * Variable to hold the total timeout time in milli seconds.
     */
    private long mTimeout;

    /**
     * Variable to hold the timeout time temporarily in milli seconds.
     */
    private long mTempTimeout;

    /**
     * Variable to hold the value of total time available.
     */
    private long mStopTimeInFuture;

    /**
     * Handler variable to implement timer logic.
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (Timer.this) {
                if (!mRunningStatus) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onTimerStopped();
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();

                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                    long delay;

                    if (millisLeft < INTERVAL) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than INTERVAL to
                        // complete, trigger onTimerStopped without delay
                        if (delay < 0) {
                            delay = 0;
                        }
                    } else {
                        delay = INTERVAL - lastTickDuration;

                        // special case: user's onTick took more than INTERVAL to
                        // complete, skip to next INTERVAL
                        while (delay < 0) {
                            delay += INTERVAL;
                        }
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };

    /**
     * @brief Parameterized Constructor.
     * @param timeout : Timeout time in milli seconds.
     */
    public Timer(long timeout) {
        mTimeout = timeout;
        mTempTimeout = mTimeout;
    }

    /**
     * @brief Method to start timer.
     */
    public final synchronized void start() {
        if (mRunningStatus) {
            stop();
        }
        mRunningStatus = true;
        if (mTempTimeout <= 0) {
            onTimerStopped();
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mTempTimeout;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }

    /**
     * @brief Method to stop timer.
     */
    public final synchronized void stop() {
        mRunningStatus = false;
        mHandler.removeMessages(MSG);
        mTempTimeout = mTimeout;
    }

    /**
     * @brief This method is triggered when timer is timed out.
     */
    public abstract void onTimerStopped();

    /**
     * @brief This method provides timer running status.
     * @return boolean : Timer running status.
     */
    public boolean isRunning() {
        return mRunningStatus;
    }
}
