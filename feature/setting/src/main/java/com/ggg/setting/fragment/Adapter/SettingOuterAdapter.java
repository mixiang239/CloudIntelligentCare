package com.ggg.setting.fragment.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ggg.data.model.SettingCard;
import com.ggg.data.model.SettingHeader;
import com.ggg.data.model.SettingListItem;
import com.ggg.data.model.SettingOption;
import com.ggg.setting.databinding.ItemCardBinding;
import com.ggg.setting.databinding.ItemHeaderBinding;

import java.util.List;

public class SettingOuterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SettingListItem> items;

    public SettingOuterAdapter(List<SettingListItem> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SettingListItem.TYPE_HEADER) {
            ItemHeaderBinding binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new HeaderViewHolder(binding);
        } else  {
            ItemCardBinding binding = ItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new CardViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SettingListItem item = items.get(position);
        if (item.getType() == SettingListItem.TYPE_HEADER) {
            ((HeaderViewHolder) holder).bind((SettingHeader) item);
        } else {
            ((CardViewHolder) holder).bind((SettingCard) item);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ItemHeaderBinding binding;
        public HeaderViewHolder(ItemHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SettingHeader item) {
            binding.textHeader.setText(item.getHeader());
        }
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        private final ItemCardBinding binding;
        public CardViewHolder(ItemCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SettingCard item) {
            binding.recyclerViewInner.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            binding.recyclerViewInner.setAdapter(new SettingInnerAdapter(item.getOptions()));
        }
    }
}
