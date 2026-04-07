package com.ggg.data.model;

public abstract class SettingListItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CARD = 1;

    public abstract int getType();
}
