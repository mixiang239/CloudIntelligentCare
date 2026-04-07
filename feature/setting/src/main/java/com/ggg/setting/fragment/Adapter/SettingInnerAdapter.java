package com.ggg.setting.fragment.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggg.data.model.SettingOption;
import com.ggg.setting.databinding.ItemSettingOptionBinding;

import java.util.List;

public class SettingInnerAdapter extends RecyclerView.Adapter<SettingInnerAdapter.ViewHolder> {

    private List<SettingOption> options;

    public SettingInnerAdapter(List<SettingOption> options) {
        this.options = options;
    }

    @NonNull
    @Override
    public SettingInnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSettingOptionBinding binding = ItemSettingOptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingInnerAdapter.ViewHolder holder, int position) {
        SettingOption item = options.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return options != null ? options.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSettingOptionBinding binding;
        public ViewHolder(ItemSettingOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SettingOption item) {
            binding.icIcon.setImageResource(item.getIcResId());
            binding.textSettingOption.setText(item.getOption());
            binding.textSimpleDescription.setText(item.getSimDes());
        }
    }
}
