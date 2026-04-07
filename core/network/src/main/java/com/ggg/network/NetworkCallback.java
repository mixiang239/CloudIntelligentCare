package com.ggg.network;

/**
 * 网络连接状态的回调接口
 */
public interface NetworkCallback {
    /**
     * 连接成功回调
     * @param serverIP 服务器IP地址
     */
    void onConnected(String serverIP);

    /**
     * 连接失败回调
     * @param errorMessage 错误信息
     */
    void onConnectionFailed(String errorMessage);

    /**
     * 收到消息回调（JSON格式）
     * @param message 收到的消息内容
     */
    void onMessageReceived(String message);

    /**
     * 连接断开回调
     */
    void onDisconnected();

    /**
     * 连接中回调
     */
    void onConnecting();

    /**
     * 紧急状态回调
     * @param status true=紧急触发, false=紧急解除
     * @param sequence 序列号
     */
    default void onEmergencyStatus(boolean status, int sequence) {
        // 默认空实现，保持向后兼容
    }
}
