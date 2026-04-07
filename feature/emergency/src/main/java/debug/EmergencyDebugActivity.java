package debug;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ggg.data.model.Contact;
import com.ggg.emergency.R;
import com.ggg.emergency.databinding.FragmentEmergencyBinding;
import com.ggg.emergency.fragment.Adapter.EmerContactsAdapter;
import com.ggg.common.Utils.UIUtils;
import com.ggg.common.Utils.VerticalItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class EmergencyDebugActivity extends AppCompatActivity {
    FragmentEmergencyBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // 初始化ViewBinding
        binding = FragmentEmergencyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 准备数据
        List<Contact> data = new ArrayList<>();
        data.add(new Contact("女儿 小芳", "138 8888 6666", "家人"));
        data.add(new Contact("儿子 小明", "139 9999 8888", "家人"));
        data.add(new Contact("社区医院", "120", "医疗"));

        // 初始化RecyclerView
        binding.recyclerviewEmerContacts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewEmerContacts.addItemDecoration(new VerticalItemDecoration(UIUtils.dpToPx(this, 10)));
        binding.recyclerviewEmerContacts.setAdapter(new EmerContactsAdapter(data));

    }
}
