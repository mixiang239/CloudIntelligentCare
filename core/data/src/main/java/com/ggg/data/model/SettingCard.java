package com.ggg.data.model;

import java.util.List;

public class SettingCard extends SettingListItem {
    private List<SettingOption> options;

    public SettingCard() {
    }

    public SettingCard(List<SettingOption> options) {
        this.options = options;
    }

    public List<SettingOption> getOptions() {
        return options;
    }

    public void setOptions(List<SettingOption> options) {
        this.options = options;
    }

    @Override
    public int getType() {
        return SettingListItem.TYPE_CARD;
    }
}
