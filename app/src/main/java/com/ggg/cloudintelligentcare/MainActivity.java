package com.ggg.cloudintelligentcare;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ggg.cloudintelligentcare.databinding.ActivityMainBinding;
import com.ggg.common.Interface.IHostActivity;
import com.ggg.common.RouterPath;
import com.ggg.emergency.fragment.EmergencyFragment;
import com.ggg.cloudintelligentcare.R;
import com.ggg.health.fragment.HealthFragment;
import com.ggg.home.fragment.HomeFragment;
import com.ggg.record.fragment.RecordFragment;
import com.ggg.setting.fragment.SettingsFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

@Route(path = RouterPath.APP_MAIN_PATH)
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, IHostActivity {

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private EmergencyFragment emergencyFragment;
    private HealthFragment healthFragment;
    private RecordFragment historyFragment;
    private SettingsFragment settingsFragment;

    // ViewBinding
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 初始化ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNav = binding.bottomNavigation;
        bottomNav.setOnNavigationItemSelectedListener(this);

        // 初始化各个碎片
        if (savedInstanceState == null) {
            initFragments();

            // 设置默认显示碎片
            loadFragment(homeFragment, "home", false);
        } else {
            // Restore fragments from saved state
            homeFragment = (HomeFragment) fragmentManager.findFragmentByTag("home");
            emergencyFragment = (EmergencyFragment) fragmentManager.findFragmentByTag("emergency");
            healthFragment = (HealthFragment) fragmentManager.findFragmentByTag("health");
            historyFragment = (RecordFragment) fragmentManager.findFragmentByTag("history");
            settingsFragment = (SettingsFragment) fragmentManager.findFragmentByTag("settings");
        }
    }

    // 初始化各个碎片
    public void initFragments() {
        Fragment fragment = null;
        homeFragment = (HomeFragment) ((fragment = (Fragment) ARouter.getInstance().build(RouterPath.HOME_PATH).navigation()) != null ? fragment : new HomeFragment());
        fragment = null;

        emergencyFragment = (EmergencyFragment) ((fragment = (Fragment) ARouter.getInstance().build(RouterPath.EMERGENCY_PATH).navigation()) != null ? fragment : new EmergencyFragment());
        fragment = null;
        healthFragment = (HealthFragment) ((fragment = (Fragment) ARouter.getInstance().build(RouterPath.HEALTH_PATH).navigation()) != null ? fragment : new HealthFragment());
        fragment = null;
        historyFragment = (RecordFragment) ((fragment = (Fragment) ARouter.getInstance().build(RouterPath.RECORD_PATH).navigation()) != null ? fragment : new RecordFragment());
        fragment = null;
        settingsFragment = (SettingsFragment) ((fragment = (Fragment) ARouter.getInstance().build(RouterPath.SETTING_PATH).navigation()) != null ? fragment : new SettingsFragment());
        fragment = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        String tag = "";

        if (item.getItemId() == R.id.nav_home) {
            selectedFragment = homeFragment;
            tag = "home";
        } else if (item.getItemId() == R.id.nav_emergency) {
            selectedFragment = emergencyFragment;
            tag = "emergency";
        } else if (item.getItemId() == R.id.nav_health) {
            selectedFragment = healthFragment;
            tag = "health";
        } else if (item.getItemId() == R.id.nav_history) {
            selectedFragment = historyFragment;
            tag = "history";
        } else if (item.getItemId() == R.id.nav_settings) {
            selectedFragment = settingsFragment;
            tag = "settings";
        }

        if (selectedFragment != null) {
            binding.textToolbarTitle.setText(item.getTitle());
            loadFragment(selectedFragment, tag, false);
            return true;
        }

        return false;
    }

    public void loadFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void navigateToEmergency() {
        loadFragment(emergencyFragment, "emergency", true);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_emergency);
    }

    public void navigateToHealth() {
        loadFragment(healthFragment, "health", true);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_health);
    }

    public void navigateToHistory() {
        loadFragment(historyFragment, "history", true);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_history);
    }

    public void navigateToSettings() {
        loadFragment(settingsFragment, "settings", true);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_settings);
    }

    @Override
    public void navigateTo(String target) {
        ARouter.getInstance()
                .build(target)
                .navigation();
    }

    @Override
    public Context getHostActivity() {
        return this;
    }
}