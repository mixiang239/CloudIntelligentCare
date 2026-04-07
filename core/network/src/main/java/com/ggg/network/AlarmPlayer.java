package com.ggg.network;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

/**
 * 警报音播放器
 * 用于播放紧急警告音频，支持循环播放和音量控制
 */
public class AlarmPlayer {

    private static final String TAG = "AlarmPlayer";

    private Context context;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private int originalVolume = -1;
    private int originalRingerMode = -1;
    private boolean isPlaying = false;

    private int rawResId = -1;
    private Uri audioUri = null;

    // 音量级别 (0.0 到 1.0)
    private float volumeLevel = 1.0f;

    public interface AlarmCallback {
        void onAlarmStarted();
        void onAlarmStopped();
        void onAlarmError(String error);
    }

    private AlarmCallback callback;

    public AlarmPlayer(Context context) {
        this.context = context;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 设置回调监听器
     */
    public void setCallback(AlarmCallback callback) {
        this.callback = callback;
    }

    /**
     * 设置音频资源（推荐使用 raw 资源）
     * @param resId R.raw.xxx
     */
    public void setAudioResource(int resId) {
        this.rawResId = resId;
        this.audioUri = null;
    }

    /**
     * 设置音频 URI
     * @param uri 音频文件的 URI
     */
    public void setAudioUri(Uri uri) {
        this.audioUri = uri;
        this.rawResId = -1;
    }

    /**
     * 通过资源名称动态设置音频资源
     * @param resName raw 资源名称（不含扩展名和包前缀）
     */
    public void setAudioResourceByName(String resName) {
        if (context == null || resName == null) {
            Log.e(TAG, "Context 或资源名称为 null");
            return;
        }
        int resId = context.getResources().getIdentifier(resName, "raw", context.getPackageName());
        if (resId != 0) {
            setAudioResource(resId);
        } else {
            Log.e(TAG, "未找到资源: " + resName);
        }
    }

    /**
     * 设置音量级别
     * @param level 音量级别 (0.0 到 1.0)
     */
    public void setVolume(float level) {
        this.volumeLevel = Math.max(0.0f, Math.min(1.0f, level));
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.setVolume(volumeLevel, volumeLevel);
        }
    }

    /**
     * 播放警报音
     */
    public void play() {
        if (isPlaying) {
            Log.w(TAG, "警报已在播放中");
            return;
        }

        if (rawResId == -1 && audioUri == null) {
            Log.e(TAG, "未设置音频资源");
            notifyError("未设置音频资源");
            return;
        }

        try {
            releaseMediaPlayer();

            // 保存原始音量并设置为最大音量
            saveOriginalVolume();
            setMaxVolume();

            mediaPlayer = new MediaPlayer();

            // 设置音频属性 - 使用 ALARM 流确保能播放
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mediaPlayer.setAudioAttributes(attributes);

            // 设置为循环播放
            mediaPlayer.setLooping(true);

            // 设置数据源
            if (rawResId != -1) {
                mediaPlayer = MediaPlayer.create(context, rawResId);
                if (mediaPlayer != null) {
                    // MediaPlayer.create 已经准备好，可以直接播放
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    isPlaying = true;
                    notifyStarted();
                } else {
                    notifyError("无法创建 MediaPlayer");
                }
            } else if (audioUri != null) {
                mediaPlayer.setDataSource(context, audioUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
                notifyStarted();
            }

            // 设置错误监听器
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer 错误: what=" + what + ", extra=" + extra);
                stop();
                notifyError("播放错误: " + what);
                return true;
            });

            // 设置循环结束监听器
            mediaPlayer.setOnCompletionListener(mp -> {
                if (!mediaPlayer.isLooping()) {
                    isPlaying = false;
                    notifyStopped();
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "播放失败", e);
            notifyError("播放失败: " + e.getMessage());
        }
    }

    /**
     * 停止警报音
     */
    public void stop() {
        if (!isPlaying && mediaPlayer == null) {
            return;
        }

        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (IllegalStateException e) {
            Log.w(TAG, "MediaPlayer 状态异常", e);
        } finally {
            mediaPlayer = null;
            isPlaying = false;
            // 恢复原始音量
            restoreOriginalVolume();
            notifyStopped();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mediaPlayer != null && isPlaying) {
            try {
                mediaPlayer.pause();
                isPlaying = false;
            } catch (IllegalStateException e) {
                Log.w(TAG, "暂停失败", e);
            }
        }
    }

    /**
     * 恢复播放
     */
    public void resume() {
        if (mediaPlayer != null && !isPlaying) {
            try {
                mediaPlayer.start();
                isPlaying = true;
            } catch (IllegalStateException e) {
                Log.w(TAG, "恢复播放失败", e);
            }
        }
    }

    /**
     * 检查是否正在播放
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 保存原始音量设置
     */
    private void saveOriginalVolume() {
        if (audioManager != null) {
            try {
                originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                originalRingerMode = audioManager.getRingerMode();
            } catch (Exception e) {
                Log.w(TAG, "保存音量设置失败", e);
            }
        }
    }

    /**
     * 设置为最大音量
     */
    private void setMaxVolume() {
        if (audioManager != null) {
            try {
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
                // 设置铃声模式为正常
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } catch (Exception e) {
                Log.w(TAG, "设置最大音量失败", e);
            }
        }
    }

    /**
     * 恢复原始音量设置
     */
    private void restoreOriginalVolume() {
        if (audioManager != null && originalVolume >= 0) {
            try {
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
                if (originalRingerMode >= 0) {
                    audioManager.setRingerMode(originalRingerMode);
                }
            } catch (Exception e) {
                Log.w(TAG, "恢复音量设置失败", e);
            }
        }
    }

    /**
     * 释放 MediaPlayer 资源
     */
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            } catch (IllegalStateException ignored) {
            }
            mediaPlayer = null;
        }
    }

    /**
     * 释放所有资源
     */
    public void release() {
        stop();
        context = null;
        audioManager = null;
    }

    private void notifyStarted() {
        if (callback != null) {
            callback.onAlarmStarted();
        }
    }

    private void notifyStopped() {
        if (callback != null) {
            callback.onAlarmStopped();
        }
    }

    private void notifyError(String error) {
        if (callback != null) {
            callback.onAlarmError(error);
        }
    }
}
