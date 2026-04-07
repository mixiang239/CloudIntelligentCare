package com.ggg.alarm.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ggg.alarm.R;
import com.ggg.common.Interface.IHostActivity;
import com.ggg.common.RouterPath;

@Route(path = RouterPath.ALARM_PATH)
public class AlarmFragment extends Fragment {
    private static final String TAG = "AlarmFragment";
    private IHostActivity hostActivity;
    private Vibrator vibrator;
    private boolean isVibrating = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IHostActivity) {
            hostActivity = (IHostActivity) context;
        }
    }

    public AlarmFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        TextView stopButton = view.findViewById(R.id.button_stop_alarm);
        stopButton.setOnClickListener(v -> stopAlarm());

        startVibration();
    }

    private void startVibration() {
        if (getContext() == null) return;
        
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                VibratorManager vibratorManager = (VibratorManager) requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vibratorManager.getDefaultVibrator();
            } else {
                vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
            }
            
            if (vibrator != null && vibrator.hasVibrator()) {
                long[] pattern = {0, 500, 200, 500, 200, 500};
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
                isVibrating = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAlarm() {
        if (vibrator != null && isVibrating) {
            vibrator.cancel();
            isVibrating = false;
        }
        
        // 通过 MainActivity 停止警报（MainActivity 会处理返回首页的逻辑）
        if (hostActivity != null) {
            hostActivity.stopAlarm();
        }
        
        // 清除屏幕常亮等标志
        if (getActivity() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vibrator != null && isVibrating) {
            vibrator.cancel();
        }
        if (getActivity() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
    }
}
