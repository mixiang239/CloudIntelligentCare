package com.ggg.health.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ggg.common.RouterPath;
import com.ggg.health.R;
@Route(path = RouterPath.HEALTH_PATH)
public class HealthFragment extends Fragment {

    public HealthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup interactions
        setupInteractions(view);
    }

    private void setupInteractions(View view) {
        // Quick action buttons
        CardView medicationReminderCard = view.findViewById(R.id.card_medication_reminder);
        CardView healthCheckupCard = view.findViewById(R.id.card_health_checkup);

        medicationReminderCard.setOnClickListener(v -> {
            // Navigate to medication reminder screen
            showMedicationReminderDialog();
        });

        healthCheckupCard.setOnClickListener(v -> {
            // Navigate to health checkup screen
            showHealthCheckupDialog();
        });

        // Weekly steps chart
        setupWeeklyChart(view);
    }

    private void setupWeeklyChart(View view) {
        // Weekly steps data visualization
        // This would be implemented with a custom chart view or library
        // For now, just setup click listeners for each day
        LinearLayout weekContainer = view.findViewById(R.id.weekly_steps_container);
        if (weekContainer != null) {
            for (int i = 0; i < weekContainer.getChildCount(); i++) {
                View dayView = weekContainer.getChildAt(i);
                int finalI = i;
                dayView.setOnClickListener(v -> {
                    // Show day details
                    showDayDetails(finalI);
                });
            }
        }
    }

    private void showMedicationReminderDialog() {
        // Simple dialog for medication reminder
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("服药提醒");
        builder.setMessage("今日已服药 ✓");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void showHealthCheckupDialog() {
        // Simple dialog for health checkup
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("体检预约");
        builder.setMessage("下次体检时间：3月15日");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void showDayDetails(int dayIndex) {
        String[] dayNames = {"一", "二", "三", "四", "五", "六", "日"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(dayNames[dayIndex] + "周步数详情");
        builder.setMessage("当日步数：3,245步");
        builder.setPositiveButton("确定", null);
        builder.show();
    }
}