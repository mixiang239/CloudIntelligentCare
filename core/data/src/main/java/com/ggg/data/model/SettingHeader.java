package com.ggg.data.model;

public class SettingHeader extends SettingListItem {
    private String header;

    public SettingHeader() {
    }

    public SettingHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int getType() {
        return SettingListItem.TYPE_HEADER;
    }
}
