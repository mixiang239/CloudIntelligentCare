package com.ggg.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ggg.common.Interface.IHostActivity;
import com.ggg.common.RouterPath;
import com.ggg.home.R;
@Route(path = RouterPath.HOME_PATH)
public class HomeFragment extends Fragment {
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

        // Emergency button click listener
        View emergencyButton = view.findViewById(R.id.card_emergency_button);
        emergencyButton.setOnClickListener(v -> {
            // 跳转到紧急呼救界面
            hostActivity.navigateTo(RouterPath.EMERGENCY_PATH);

        });

        // Setup other interactions
         setupInteractions(view);
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
}