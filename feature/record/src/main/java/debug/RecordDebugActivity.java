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
import com.ggg.data.model.Record;
import com.ggg.record.R;
import com.ggg.record.databinding.FragmentHistoryBinding;
import com.ggg.record.fragment.Adapter.HistoryListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecordDebugActivity extends AppCompatActivity {
    private FragmentHistoryBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // 初始化binding
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 准备数据
        List<Record> data = new ArrayList<>();
        data.add(new Record(R.drawable.ic_warning, "#DA6F00", "轻微异常检测", "警告", "检测到轻微摇晃，已自动记录", "今天 10:23"));
        data.add(new Record(R.drawable.ic_warning, "#DA6F00", "轻微异常检测", "警告", "检测到轻微摇晃，已自动记录", "今天 10:23"));
        data.add(new Record(R.drawable.ic_warning, "#DA6F00", "轻微异常检测", "警告", "检测到轻微摇晃，已自动记录", "今天 10:23"));
        data.add(new Record(R.drawable.ic_warning, "#DA6F00", "轻微异常检测", "警告", "检测到轻微摇晃，已自动记录", "今天 10:23"));
        data.add(new Record(R.drawable.ic_warning, "#DA6F00", "轻微异常检测", "警告", "检测到轻微摇晃，已自动记录", "今天 10:23"));
        data.add(new Record(R.drawable.ic_warning, "#DA6F00", "轻微异常检测", "警告", "检测到轻微摇晃，已自动记录", "今天 10:23"));
        data.add(new Record(R.drawable.ic_warning, "#DA6F00", "轻微异常检测", "警告", "检测到轻微摇晃，已自动记录", "今天 10:23"));
        data.add(new Record(R.drawable.ic_warning, "#DA6F00", "轻微异常检测", "警告", "检测到轻微摇晃，已自动记录", "今天 10:23"));

        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewHistory.addItemDecoration(new VerticalItemDecoration(UIUtils.dpToPx(this, 12)));
        binding.recyclerViewHistory.setAdapter(new HistoryListAdapter(data));
    }
}
