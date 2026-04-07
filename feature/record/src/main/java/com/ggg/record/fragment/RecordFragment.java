package com.ggg.record.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ggg.common.Interface.IHostActivity;
import com.ggg.common.RouterPath;
import com.ggg.common.Utils.UIUtils;
import com.ggg.common.Utils.VerticalItemDecoration;
import com.ggg.data.model.Record;
import com.ggg.record.R;
import com.ggg.record.databinding.FragmentHistoryBinding;
import com.ggg.record.fragment.Adapter.HistoryListAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterPath.RECORD_PATH)
public class RecordFragment extends Fragment {
    private FragmentHistoryBinding binding;

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

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_history, container, false);
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();

        // Setup filter dropdown
        setupFilterDropdown(view);
        
        // Setup interactions
        setupInteractions(view);
    }

    private void loadData() {
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

        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(hostActivity.getHostActivity()));
        binding.recyclerViewHistory.addItemDecoration(new VerticalItemDecoration(UIUtils.dpToPx(hostActivity.getHostActivity(), 12)));
        binding.recyclerViewHistory.setAdapter(new HistoryListAdapter(data));
    }
    private void setupFilterDropdown(View view) {
        String[] timeRanges = {"最近7天", "最近30天", "最近3个月"};

        AppCompatAutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(), 
            android.R.layout.simple_dropdown_item_1line, 
            timeRanges
        );

        autoCompleteTextView.setAdapter(adapter);
        
        autoCompleteTextView.setOnItemClickListener((parent, selectedView, position, id) -> {
            String selected = timeRanges[position];
        });
    }

    private void setupInteractions(View view) {
        // Load more button
        Button loadMoreButton = view.findViewById(R.id.button_load_more);
        loadMoreButton.setOnClickListener(v -> {
            // Load more records
            loadMoreRecords();
        });
    }

    private void showFilterDialog() {
        // Simple dialog for date filter
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("选择时间范围");
        String[] options = {"最近7天", "最近30天", "最近3个月"};
        builder.setItems(options, (dialog, which) -> {
            // Handle selection
            // updateFilterText(which);
        });
        builder.show();
    }

    /*private void updateFilterText(int selection) {
        // Update the selected filter text
        Button filterButton = getView().findViewById(R.id.filter_button);
        String[] options = {"最近7天", "最近30天", "最近3个月"};
        filterButton.setText(options[selection]);
    }*/

    private void loadMoreRecords() {
        // Add more records to the list
        // This would be implemented with actual data loading
    }
}