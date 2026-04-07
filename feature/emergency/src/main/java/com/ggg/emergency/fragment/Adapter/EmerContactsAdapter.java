package com.ggg.emergency.fragment.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggg.data.model.Contact;
import com.ggg.emergency.databinding.ItemEmerContactsBinding;

import java.util.List;

public class EmerContactsAdapter extends RecyclerView.Adapter<EmerContactsAdapter.ViewHolder> {
    private List<Contact> contacts;

    public EmerContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public EmerContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemEmerContactsBinding binding = ItemEmerContactsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmerContactsAdapter.ViewHolder holder, int position) {
        Contact item = contacts.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemEmerContactsBinding binding;
        public ViewHolder(ItemEmerContactsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Contact item) {
            binding.textContactName.setText(item.getName());
            binding.textContactNumber.setText(item.getNumber());
            binding.textGroup.setText(item.getGroup());
        }
    }
}
