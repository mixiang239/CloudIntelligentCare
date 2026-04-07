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
import com.ggg.common.Interface.IHostActivity;
import com.ggg.common.RouterPath;
import com.ggg.common.Utils.UIUtils;
import com.ggg.common.Utils.VerticalItemDecoration;
import com.ggg.data.model.Contact;
import com.ggg.emergency.R;
import com.ggg.emergency.databinding.FragmentEmergencyBinding;
import com.ggg.emergency.fragment.Adapter.EmerContactsAdapter;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterPath.EMERGENCY_PATH)
public class EmergencyFragment extends Fragment {
    FragmentEmergencyBinding binding;
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
        Button callButton = binding.buttonCall;
        callButton.setOnClickListener(v -> {
            // Make emergency call
            makeEmergencyCall("120");
        });

        // Setup other interactions
        setupInteractions(view);
    }

    private void setupInteractions(View view) {
        // Quick action buttons
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

    private void showAlertTestDialog() {
        // Simple dialog for alert test
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("警报测试");
        builder.setMessage("正在测试警报音...");
        builder.setPositiveButton("确定", (dialog, which) -> {
            // Play alert sound
            // TODO: Implement alert sound
        });
        builder.show();
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
}