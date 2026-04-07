package com.ggg.emergency.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import android.util.Log;
import com.ggg.common.Interface.IHostActivity;
import com.ggg.common.RouterPath;
import com.ggg.common.Utils.UIUtils;
import com.ggg.common.Utils.VerticalItemDecoration;
import com.ggg.data.model.Contact;
import com.ggg.emergency.R;
import com.ggg.emergency.databinding.FragmentEmergencyBinding;
import com.ggg.emergency.fragment.Adapter.EmerContactsAdapter;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterPath.EMERGENCY_PATH)
public class EmergencyFragment extends Fragment {
    private static final String TAG = "EmergencyFragment";
    FragmentEmergencyBinding binding;
    private IHostActivity hostActivity;
    private boolean isAlertPlaying = false;

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


    public EmergencyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_emergency, container, false);
        binding = FragmentEmergencyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 同步警报状态
        isAlertPlaying = hostActivity != null && hostActivity.isAlarmPlaying();

        // 注册警报状态回调
        if (hostActivity != null) {
            hostActivity.setAlarmCallback(new IHostActivity.AlarmCallback() {
                @Override
                public void onAlarmStarted() {
                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> {
                            isAlertPlaying = true;
                            Log.d(TAG, "警报已开始");
                        });
                    }
                }

                @Override
                public void onAlarmStopped() {
                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> {
                            isAlertPlaying = false;
                            Log.d(TAG, "警报已停止");
                        });
                    }
                }

                @Override
                public void onAlarmError(String error) {
                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> {
                            isAlertPlaying = false;
                            Log.e(TAG, "警报错误: " + error);
                        });
                    }
                }
            });
        }

        // 准备数据
        List<Contact> data = new ArrayList<>();
        data.add(new Contact("女儿 小芳", "138 8888 6666", "家人"));
        data.add(new Contact("儿子 小明", "139 9999 8888", "家人"));
        data.add(new Contact("社区医院", "120", "医疗"));

        // 初始化RecyclerView
        binding.recyclerviewEmerContacts.setLayoutManager(new LinearLayoutManager(hostActivity.getHostActivity()));
        binding.recyclerviewEmerContacts.addItemDecoration(new VerticalItemDecoration(UIUtils.dpToPx(hostActivity.getHostActivity(), 10)));
        binding.recyclerviewEmerContacts.setAdapter(new EmerContactsAdapter(data));

        // Emergency call button
        MaterialCardView callButton = binding.buttonCall;
        callButton.setOnClickListener(v -> {
            // Make emergency call
            makeEmergencyCall("120");
        });

        // Setup other interactions
        setupInteractions(view);
    }

    private void setupInteractions(View view) {
        CardView alertTestCard = view.findViewById(R.id.card_alert_test);
        CardView safetyReportCard = view.findViewById(R.id.card_safety_report);

        alertTestCard.setOnClickListener(v -> {
            // Show alert test dialog
            showAlertTestDialog();
        });

        safetyReportCard.setOnClickListener(v -> {
            // Send safety report
            sendSafetyReport();
        });

        // View map button
        Button viewMapButton = view.findViewById(R.id.button_view_map);
        viewMapButton.setOnClickListener(v -> {
            // Open map
            openMap();
        });

        // Emergency contact buttons
        // setupContactButtons(view);
    }

//    private void setupContactButtons(View view) {
//        // Daughter
//        Button daughterCallButton = view.findViewById(R.id.daughter_call_button);
//        daughterCallButton.setOnClickListener(v -> {
//            makeEmergencyCall("13888886666");
//        });
//
//        // Son
//        Button sonCallButton = view.findViewById(R.id.son_call_button);
//        sonCallButton.setOnClickListener(v -> {
//            makeEmergencyCall("13999998888");
//        });
//
//        // Hospital
//        Button hospitalCallButton = view.findViewById(R.id.hospital_call_button);
//        hospitalCallButton.setOnClickListener(v -> {
//            makeEmergencyCall("120");
//        });
//
//        // Add contact button
//        Button addContactButton = view.findViewById(R.id.button_add_contact_button);
//        addContactButton.setOnClickListener(v -> {
//            // Navigate to add contact screen
//            // TODO: Implement add contact functionality
//        });
//    }

    private void makeEmergencyCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    /**
     * 显示警报测试对话框
     * 提供播放/停止警报音的控制选项
     */
    private void showAlertTestDialog() {
        if (getContext() == null) return;

        String message = isAlertPlaying ? "警报音正在播放中..." : "点击\"开始\"播放警报测试音";

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("警报测试");
        builder.setMessage(message);

        if (isAlertPlaying) {
            builder.setPositiveButton("停止", (dialog, which) -> {
                stopAlertSound();
            });
        } else {
            builder.setPositiveButton("开始", (dialog, which) -> {
                startAlertSound();
            });
        }

        builder.setNegativeButton("取消", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }

    /**
     * 开始播放警报音（测试模式）
     */
    private void startAlertSound() {
        if (hostActivity != null) {
            hostActivity.startAlarm(0);
            isAlertPlaying = true;
        }
    }

    /**
     * 停止播放警报音
     */
    private void stopAlertSound() {
        if (hostActivity != null) {
            hostActivity.stopAlarm();
            isAlertPlaying = false;
        }
    }

    private void sendSafetyReport() {
        // Send safety report message
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("平安报告");
        builder.setMessage("已发送平安报告给您的紧急联系人");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void openMap() {
        // Open map with current location
        // TODO: Implement map functionality
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 清除警报回调
        if (hostActivity != null) {
            hostActivity.setAlarmCallback(null);
        }
    }
}
