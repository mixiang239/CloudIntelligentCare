package com.ggg.record.fragment.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggg.data.model.Record;
import com.ggg.record.databinding.ItemHistoryListBinding;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
    private List<Record> records;

    public HistoryListAdapter(List<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public HistoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryListBinding binding = ItemHistoryListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListAdapter.ViewHolder holder, int position) {
        Record item = records.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return records != null ? records.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemHistoryListBinding binding;
        public ViewHolder(ItemHistoryListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Record item) {
            binding.icType.setImageResource(item.getIcResId());
            binding.textTitle.setText(item.getTitle());
            binding.textType.setText(item.getType());
            binding.textDescription.setText(item.getDescription());
            binding.textTime.setText(item.getTime());
        }
    }
}
