package debug;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

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

public class SettingDebugActivity extends AppCompatActivity {
    private FragmentSettingsBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        binding.recyclerViewSetting.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewSetting.addItemDecoration(new VerticalItemDecoration(UIUtils.dpToPx(this, 12)));
        binding.recyclerViewSetting.setAdapter(new SettingOuterAdapter(items));
    }
}
