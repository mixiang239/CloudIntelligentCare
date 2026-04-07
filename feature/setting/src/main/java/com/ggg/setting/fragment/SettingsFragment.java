package com.ggg.setting.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ggg.common.Interface.IHostActivity;
import com.ggg.common.RouterPath;
import com.ggg.common.Utils.UIUtils;
import com.ggg.common.Utils.VerticalItemDecoration;
import com.ggg.data.model.SettingCard;
import com.ggg.data.model.SettingHeader;
import com.ggg.data.model.SettingListItem;
import com.ggg.data.model.SettingOption;
import com.ggg.setting.R;
import com.ggg.setting.databinding.FragmentSettingsBinding;
import com.ggg.setting.fragment.Adapter.SettingOuterAdapter;

import java.util.ArrayList;
import java.util.List;
@Route(path = RouterPath.SETTING_PATH)
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    private IHostActivity hostActivity;

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

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();

        // Setup interactions
        // setupInteractions(view);
    }

    private void loadData() {
        // 准备数据
        List<SettingListItem> items = new ArrayList<>();
        items.add(new SettingHeader("账户设置"));
        List<SettingOption> list1 = new ArrayList<>();
        list1.add(new SettingOption(com.ggg.common.R.drawable.ic_contacts, "个人信息", "张大爷"));
        list1.add(new SettingOption(R.drawable.setting_ic_ring, "通知设置", "已开启"));
        items.add(new SettingCard(list1));
        items.add(new SettingHeader("监护设置"));
        List<SettingOption> list2 = new ArrayList<>();
        list2.add(new SettingOption(com.ggg.common.R.drawable.common_ic_shield, "跌倒检测", "高灵敏度"));
        list2.add(new SettingOption(R.drawable.ic_phone, "设备连接", "已连接"));
        list2.add(new SettingOption(R.drawable.ic_sound, "警报音量", "最大"));

        items.add(new SettingCard(list2));
        items.add(new SettingHeader("其他设置"));

        List<SettingOption> list3 = new ArrayList<>();
        list3.add(new SettingOption(com.ggg.common.R.drawable.ic_sleep, "深色模式", "关闭"));
        list3.add(new SettingOption(R.drawable.ic_help, "帮助与反馈", ""));
        items.add(new SettingCard(list3));

        // 采用双层RecyclerView
        binding.recyclerViewSetting.setLayoutManager(new LinearLayoutManager(hostActivity.getHostActivity()));
        binding.recyclerViewSetting.addItemDecoration(new VerticalItemDecoration(UIUtils.dpToPx(hostActivity.getHostActivity(), 12)));
        binding.recyclerViewSetting.setAdapter(new SettingOuterAdapter(items));
    }

    /*private void setupInteractions(View view) {
        // Profile section
        CardView personalInfoCard = view.findViewById(R.id.personal_info_card);
        personalInfoCard.setOnClickListener(v -> {
            // Navigate to personal info screen
            showPersonalInfoDialog();
        });

        // Care settings
        CardView fallDetectionCard = view.findViewById(R.id.fall_detection_card);
        fallDetectionCard.setOnClickListener(v -> {
            // Configure fall detection settings
            showFallDetectionDialog();
        });

        CardView deviceConnectionCard = view.findViewById(R.id.device_connection_card);
        deviceConnectionCard.setOnClickListener(v -> {
            // Manage device connections
            showDeviceConnectionDialog();
        });

        // Other settings
        CardView darkModeCard = view.findViewById(R.id.dark_mode_card);
        darkModeCard.setOnClickListener(v -> {
            // Toggle dark mode
            toggleDarkMode();
        });

        CardView helpCard = view.findViewById(R.id.help_card);
        helpCard.setOnClickListener(v -> {
            // Open help screen
            openHelpScreen();
        });

        // Logout button
        Button logoutButton = view.findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            // Show logout confirmation
            showLogoutConfirmation();
        });

        // Customer service button
        Button serviceButton = view.findViewById(R.id.customer_service_button);
        serviceButton.setOnClickListener(v -> {
            // Call customer service
            callCustomerService();
        });
    }*/

    private void showPersonalInfoDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("个人信息");
        builder.setMessage("姓名: 张大爷\n年龄: 78岁\n监护天数: 365天");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void showFallDetectionDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("跌倒检测");
        builder.setMessage("当前设置: 高灵敏度");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void showDeviceConnectionCard() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("设备连接");
        builder.setMessage("当前状态: 已连接\n设备: 智能手表");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void toggleDarkMode() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("深色模式");
        builder.setMessage("当前状态: 关闭\n是否开启深色模式？");
        builder.setPositiveButton("是", (dialog, which) -> {
            // Toggle dark mode
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    private void openHelpScreen() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("帮助与反馈");
        builder.setMessage("如有问题，请拨打客服热线\n400-888-6666");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void showLogoutConfirmation() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("退出登录");
        builder.setMessage("确定要退出登录吗？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            // Perform logout
            getActivity().finish();
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void callCustomerService() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:400-888-6666"));
        startActivity(intent);
    }
}