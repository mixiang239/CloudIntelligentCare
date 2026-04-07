package com.ggg.network;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

/**
 * 服务器连接对话框帮助类
 */
public class ServerConnectDialog {

    private Context context;
    private AlertDialog currentDialog;
    private TextView statusTextView;
    private ServerConnection serverConnection;
    private DialogCallback callback;

    public interface DialogCallback {
        void onConnected(String ip, int port);
        void onConnectFailed(String error);
        void onDisconnected();
        default void onEmergencyStatus(boolean status, int sequence) {
            // 默认空实现
        }
    }

    public ServerConnectDialog(Context context) {
        this.context = context;
    }

    public void setCallback(DialogCallback callback) {
        this.callback = callback;
    }

    /**
     * 显示连接对话框
     */
    public void show() {
        show(null);
    }

    /**
     * 显示连接对话框，可指定默认IP
     */
    public void show(String defaultIP) {
        // 如果已有连接，先断开
        if (serverConnection != null && serverConnection.isConnected()) {
            serverConnection.disconnect();
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_server_connect, null);

        TextInputEditText ipEditText = dialogView.findViewById(R.id.edit_server_ip);
        TextInputEditText portEditText = dialogView.findViewById(R.id.edit_server_port);
        statusTextView = dialogView.findViewById(R.id.text_connection_status);

        if (defaultIP != null && !defaultIP.isEmpty()) {
            ipEditText.setText(defaultIP);
        }

        // 添加输入验证
        ipEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                hideStatus();
            }
        });

        currentDialog = new MaterialAlertDialogBuilder(context)
                .setTitle("连接服务器")
                .setView(dialogView)
                .setPositiveButton("连接", null)  // 先设置为null，稍后设置监听器
                .setNegativeButton("取消", (dialog, which) -> {
                    hideStatus();
                })
                .setNeutralButton("断开连接", (dialog, which) -> {
                    disconnect();
                })
                .create();

        currentDialog.setOnShowListener(dialogInterface -> {
            // 设置按钮点击事件
            currentDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String ip = ipEditText.getText() != null ? ipEditText.getText().toString().trim() : "";
                String portStr = portEditText.getText() != null ? portEditText.getText().toString().trim() : "8888";

                // 验证输入
                if (ip.isEmpty()) {
                    showStatus("请输入服务器IP地址", true);
                    return;
                }

                if (!isValidIP(ip)) {
                    showStatus("请输入有效的IP地址格式", true);
                    return;
                }

                int port = 8888;
                try {
                    port = Integer.parseInt(portStr);
                    if (port <= 0 || port > 65535) {
                        showStatus("端口号必须在1-65535之间", true);
                        return;
                    }
                } catch (NumberFormatException e) {
                    showStatus("请输入有效的端口号", true);
                    return;
                }

                // 执行连接
                connectToServer(ip, port);
            });

            // 更新按钮文字
            if (serverConnection != null && serverConnection.isConnected()) {
                currentDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
            } else {
                currentDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
            }
        });

        currentDialog.show();
    }

    /**
     * 连接到服务器
     */
    private void connectToServer(String ip, int port) {
        showStatus("正在连接 " + ip + ":" + port + "...", false);

        serverConnection = new ServerConnection();
        serverConnection.setCallback(new NetworkCallback() {
            @Override
            public void onConnected(String serverIP) {
                hideStatus();
                if (context != null) {
                    Toast.makeText(context, "已连接到服务器 " + serverIP, Toast.LENGTH_SHORT).show();
                }
                if (callback != null) {
                    callback.onConnected(serverIP, port);
                }
                if (currentDialog != null) {
                    currentDialog.dismiss();
                }
            }

            @Override
            public void onConnectionFailed(String errorMessage) {
                showStatus("连接失败: " + errorMessage, true);
                if (callback != null) {
                    callback.onConnectFailed(errorMessage);
                }
            }

            @Override
            public void onMessageReceived(String message) {
                // 消息处理由外部回调处理
            }

            @Override
            public void onEmergencyStatus(boolean status, int sequence) {
                if (callback != null && callback instanceof DialogCallback) {
                    ((DialogCallback) callback).onEmergencyStatus(status, sequence);
                }
            }

            @Override
            public void onDisconnected() {
                if (context != null) {
                    Toast.makeText(context, "服务器连接已断开", Toast.LENGTH_SHORT).show();
                }
                if (callback != null) {
                    callback.onDisconnected();
                }
            }

            @Override
            public void onConnecting() {
                showStatus("正在连接...", false);
            }
        });

        serverConnection.connect(ip, port);
    }

    /**
     * 断开服务器连接
     */
    public void disconnect() {
        if (serverConnection != null) {
            serverConnection.disconnect();
            serverConnection = null;
        }
        hideStatus();
        if (callback != null) {
            callback.onDisconnected();
        }
    }

    /**
     * 获取当前连接对象
     */
    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    /**
     * 检查是否已连接
     */
    public boolean isConnected() {
        return serverConnection != null && serverConnection.isConnected();
    }

    /**
     * 验证IP地址格式
     */
    private boolean isValidIP(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        // 简单的IP格式验证
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipPattern);
    }

    /**
     * 显示状态文本
     */
    private void showStatus(String message, boolean isError) {
        if (statusTextView != null) {
            statusTextView.setVisibility(View.VISIBLE);
            statusTextView.setText(message);
            statusTextView.setTextColor(isError ?
                    0xFFE53935 : 0xFF43A047); // 红色或绿色
        }
    }

    /**
     * 隐藏状态文本
     */
    private void hideStatus() {
        if (statusTextView != null) {
            statusTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
    }
}
