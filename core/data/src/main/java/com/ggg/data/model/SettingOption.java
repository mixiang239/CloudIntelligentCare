package com.ggg.data.model;

public class SettingOption {
    private int icResId;
    private String option;
    private String simDes;

    public SettingOption() {
    }

    public SettingOption(int icResId, String option, String simDes) {
        this.icResId = icResId;
        this.option = option;
        this.simDes = simDes;
    }

    public int getIcResId() {
        return icResId;
    }

    public void setIcResId(int icResId) {
        this.icResId = icResId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getSimDes() {
        return simDes;
    }

    public void setSimDes(String simDes) {
        this.simDes = simDes;
    }
}
