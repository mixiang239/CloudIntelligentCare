package com.ggg.home.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ggg.common.Interface.IHostActivity;
import com.ggg.common.RouterPath;
import com.ggg.home.R;
import com.ggg.network.NetworkCallback;
import com.ggg.network.ServerConnectDialog;
import com.ggg.network.ServerConnection;

import org.json.JSONObject;

@Route(path = RouterPath.HOME_PATH)
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private IHostActivity hostActivity;

    private View textRunningStatus;
    private View viewGreenDot;
    private View textNetworkStatus;
    private CardView cardNetwork;
    private View cardStatus;

    private ServerConnectDialog serverDialog;
    private ServerConnection serverConnection;
    private String lastConnectedIP = "192.168.1.100"; // 上次连接的IP

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IHostActivity) {
            hostActivity = (IHostActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " 宿主Activity必须实现IHostActivity接口！");
        }
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化视图引用
        initViews(view);

        // Emergency button click listener
        View emergencyButton = view.findViewById(R.id.card_emergency_button);
        emergencyButton.setOnClickListener(v -> {
            // 跳转到紧急呼救界面
            hostActivity.navigateTo(RouterPath.EMERGENCY_PATH);

        });

        // 网络卡片点击事件 - 弹出连接对话框
        setupNetworkCard(view);

        // Setup other interactions
         setupInteractions(view);
    }

    /**
     * 初始化视图引用
     */
    private void initViews(View view) {
        textRunningStatus = view.findViewById(R.id.text_running_status);
        viewGreenDot = view.findViewById(R.id.view_green_dot);
        textNetworkStatus = view.findViewById(R.id.text_network_status);
        cardNetwork = view.findViewById(R.id.card_network);
        cardStatus = view.findViewById(R.id.card_status);
    }

    /**
     * 设置网络卡片点击事件
     */
    private void setupNetworkCard(View view) {
        CardView networkCard = view.findViewById(R.id.card_network);
        if (networkCard != null) {
            networkCard.setOnClickListener(v -> showServerConnectDialog());
        }
    }

    /**
     * 显示服务器连接对话框
     */
    private void showServerConnectDialog() {
        if (getContext() == null) return;

        serverDialog = new ServerConnectDialog(requireContext());
        serverDialog.setCallback(new ServerConnectDialog.DialogCallback() {
            @Override
            public void onConnected(String ip, int port) {
                lastConnectedIP = ip;
                updateNetworkStatus(true, ip);
                updateConnectionStatus(true);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "已连接到服务器 " + ip + ":" + port, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onConnectFailed(String error) {
                updateNetworkStatus(false, null);
                updateConnectionStatus(false);
            }

            @Override
            public void onDisconnected() {
                updateNetworkStatus(false, null);
                updateConnectionStatus(false);
            }

            @Override
            public void onEmergencyStatus(boolean status, int sequence) {
                if (status) {
                    triggerEmergencyState(sequence);
                } else {
                    releaseEmergencyState();
                }
            }
        });
        serverDialog.show(lastConnectedIP);
    }

    /**
     * 更新网络状态显示
     */
    private void updateNetworkStatus(boolean connected, String ip) {
        if (textNetworkStatus == null) return;

        // 检查 Fragment 是否附加到 Activity
        if (!isAdded() || getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            if (!isAdded()) return;
            if (connected) {
                textNetworkStatus.setVisibility(View.VISIBLE);
                ((android.widget.TextView) textNetworkStatus).setText("已连接");
                ((android.widget.TextView) textNetworkStatus).setTextColor(Color.parseColor("#43A047")); // 绿色
            } else {
                ((android.widget.TextView) textNetworkStatus).setText("未连接");
                ((android.widget.TextView) textNetworkStatus).setTextColor(Color.parseColor("#757575")); // 灰色
            }
        });
    }

    /**
     * 更新主连接状态显示
     */
    private void updateConnectionStatus(boolean connected) {
        if (textRunningStatus == null || viewGreenDot == null) return;

        // 检查 Fragment 是否附加到 Activity
        if (!isAdded() || getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            if (!isAdded()) return;
            if (connected) {
                ((android.widget.TextView) textRunningStatus).setText("服务器已连接");
                ((android.widget.TextView) textRunningStatus).setTextColor(Color.parseColor("#43A047"));
                // 绿色指示灯保持不变
            } else {
                ((android.widget.TextView) textRunningStatus).setText("等待连接");
                ((android.widget.TextView) textRunningStatus).setTextColor(Color.parseColor("#757575"));
            }
        });
    }

    /**
     * 处理收到的服务器消息
     */
    private void handleServerMessage(String message) {
        try {
            JSONObject json = new JSONObject(message);
            String type = json.optString("type", "status_update");

            if ("status_update".equals(type)) {
                boolean status = json.getBoolean("status");
                int sequence = json.optInt("sequence", 0);

                Log.d(TAG, "收到状态更新: status=" + status + ", sequence=" + sequence);

                if (status) {
                    // 紧急状态触发
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (isAdded()) {
                                triggerEmergencyState(sequence);
                            }
                        });
                    }
                } else {
                    // 紧急状态解除
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (isAdded()) {
                                releaseEmergencyState();
                            }
                        });
                    }
                }
            } else if ("text_message".equals(type)) {
                // 文本消息处理
                String content = json.getString("content");
                Log.d(TAG, "收到文本消息: " + content);
            }
        } catch (Exception e) {
            Log.e(TAG, "解析消息失败: " + message, e);
        }
    }

    /**
     * 触发紧急状态
     * 通过 IHostActivity 控制警报，警报页面会自动显示
     */
    protected void triggerEmergencyState(int sequence) {
        Log.w(TAG, "紧急状态触发! 序列号: " + sequence);

        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), "警告: 检测到紧急状态!", Toast.LENGTH_LONG).show();

            // 通过 MainActivity 控制警报（警报页面会自动显示）
            if (hostActivity != null) {
                hostActivity.startAlarm(sequence);
            }
        }
    }

    /**
     * 解除紧急状态
     */
    protected void releaseEmergencyState() {
        Log.d(TAG, "紧急状态解除");

        // 通过 MainActivity 停止警报
        if (hostActivity != null) {
            hostActivity.stopAlarm();
        }
    }

    private void setupInteractions(View view) {
        // 点击心率和步数卡片都跳转到健康界面
        CardView heartRateCard = view.findViewById(R.id.heart_rate_card);
        CardView stepsCard = view.findViewById(R.id.card_steps);

        heartRateCard.setOnClickListener(v -> {
            // 跳转到健康界面
            hostActivity.navigateTo(RouterPath.HEALTH_PATH);
        });

        stepsCard.setOnClickListener(v -> {
            // 跳转到健康界面
            hostActivity.navigateTo(RouterPath.HEALTH_PATH);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 断开服务器连接
        if (serverDialog != null) {
            // 先清除回调，避免在 Fragment 已分离后调用
            serverDialog.setCallback(null);
            serverDialog.disconnect();
        }
    }
}
