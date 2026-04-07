package com.ggg.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 服务器连接管理类
 * 负责Socket连接、消息发送和接收
 */
public class ServerConnection {
    private static final String TAG = "ServerConnection";
    private static final int DEFAULT_PORT = 8888;

    private Socket socket;
    private PrintWriter writer;
    private Thread connectionThread;
    private NetworkCallback callback;
    private Handler mainHandler;

    private volatile boolean isRunning = false;
    private volatile boolean isConnected = false;
    private String currentServerIP;

    public ServerConnection() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 设置回调接口
     */
    public void setCallback(NetworkCallback callback) {
        this.callback = callback;
    }

    /**
     * 连接到服务器
     * @param ip 服务器IP地址
     */
    public void connect(String ip) {
        connect(ip, DEFAULT_PORT);
    }

    /**
     * 连接到指定服务器和端口
     * @param ip 服务器IP地址
     * @param port 端口号
     */
    public void connect(String ip, int port) {
        if (isConnected) {
            Log.w(TAG, "已经在连接状态，请先断开连接");
            return;
        }

        currentServerIP = ip;
        isRunning = true;

        connectionThread = new Thread(() -> {
            try {
                notifyConnecting();

                socket = new Socket(ip, port);
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                isConnected = true;
                notifyConnected(ip);

                Log.d(TAG, "服务器连接成功: " + ip + ":" + port);

                // 开始监听服务器消息
                listenToServer();

            } catch (IOException e) {
                Log.e(TAG, "服务器连接失败: " + e.getMessage());
                isConnected = false;
                notifyConnectionFailed(e.getMessage());
            }
        });
        connectionThread.start();
    }

    /**
     * 断开服务器连接
     */
    public void disconnect() {
        isRunning = false;
        isConnected = false;

        try {
            if (writer != null) {
                writer.close();
                writer = null;
            }
            if (socket != null && !socket.isClosed()) {
                socket.shutdownOutput();
                socket.close();
            }
            notifyDisconnected();
        } catch (IOException e) {
            Log.e(TAG, "关闭连接时出错: " + e.getMessage());
        }
    }

    /**
     * 发送消息到服务器
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        if (writer != null && isConnected) {
            writer.println(message);
            writer.flush();
            Log.d(TAG, "发送消息: " + message);
        } else {
            Log.w(TAG, "无法发送消息，连接未建立");
        }
    }

    /**
     * 发送JSON消息到服务器
     * @param json JSONObject消息
     */
    public void sendJson(JSONObject json) {
        sendMessage(json.toString());
    }

    /**
     * 发送确认消息
     * @param sequence 序列号
     */
    public void sendAck(int sequence) {
        try {
            JSONObject ack = new JSONObject();
            ack.put("ack", sequence);
            sendJson(ack);
        } catch (Exception e) {
            Log.e(TAG, "发送确认消息失败: " + e.getMessage());
        }
    }

    /**
     * 监听服务器消息
     */
    private void listenToServer() {
        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            String response;
            while (isRunning && isConnected && (response = reader.readLine()) != null) {
                Log.d(TAG, "收到服务器消息: " + response);
                notifyMessageReceived(response);

                // 解析消息并处理
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    // 检查紧急状态
                    if (jsonResponse.has("status")) {
                        boolean status = jsonResponse.getBoolean("status");
                        int sequence = jsonResponse.optInt("sequence", 0);
                        notifyEmergencyStatus(status, sequence);
                    }

                    // 发送确认
                    int sequence = jsonResponse.optInt("sequence", 0);
                    if (sequence > 0) {
                        sendAck(sequence);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "解析服务器数据错误: " + response, e);
                }
            }

        } catch (IOException e) {
            if (isRunning) {
                Log.e(TAG, "服务器连接异常: " + e.getMessage());
            }
        } finally {
            if (isRunning) {
                isConnected = false;
                notifyDisconnected();
            }
        }
    }

    /**
     * 检查是否已连接
     */
    public boolean isConnected() {
        return isConnected && socket != null && !socket.isClosed();
    }

    /**
     * 获取当前连接的服务器IP
     */
    public String getCurrentServerIP() {
        return currentServerIP;
    }

    // ==================== 回调通知 ====================

    private void notifyConnecting() {
        mainHandler.post(() -> {
            if (callback != null) {
                callback.onConnecting();
            }
        });
    }

    private void notifyConnected(String ip) {
        mainHandler.post(() -> {
            if (callback != null) {
                callback.onConnected(ip);
            }
        });
    }

    private void notifyConnectionFailed(String error) {
        mainHandler.post(() -> {
            if (callback != null) {
                callback.onConnectionFailed(error);
            }
        });
    }

    private void notifyMessageReceived(String message) {
        mainHandler.post(() -> {
            if (callback != null) {
                callback.onMessageReceived(message);
            }
        });
    }

    private void notifyDisconnected() {
        mainHandler.post(() -> {
            if (callback != null) {
                callback.onDisconnected();
            }
        });
    }

    /**
     * 通知紧急状态
     */
    private void notifyEmergencyStatus(boolean status, int sequence) {
        mainHandler.post(() -> {
            if (callback != null) {
                callback.onEmergencyStatus(status, sequence);
            }
        });
    }
}
