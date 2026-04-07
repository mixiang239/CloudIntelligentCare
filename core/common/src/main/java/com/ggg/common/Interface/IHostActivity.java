package com.ggg.common.Interface;

import android.content.Context;

public interface IHostActivity {
    void navigateTo(String target);
    Context getHostActivity();

    // ==================== 警报控制 ====================
    void startAlarm(int sequence);
    void stopAlarm();
    boolean isAlarmPlaying();
    void setAlarmCallback(AlarmCallback callback);

    interface AlarmCallback {
        void onAlarmStarted();
        void onAlarmStopped();
        void onAlarmError(String error);
    }
}
