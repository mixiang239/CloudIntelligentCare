package com.ggg.data.model;

public class Record {
    private int icResId;
    private String colorValue;
    private String title;
    private String type;
    private String description;
    private String time;

    public Record() {
    }

    public Record(int icResId, String colorValue, String title, String type, String description, String time) {
        this.icResId = icResId;
        this.colorValue = colorValue;
        this.title = title;
        this.type = type;
        this.description = description;
        this.time = time;
    }

    public int getIcResId() {
        return icResId;
    }

    public void setIcResId(int icResId) {
        this.icResId = icResId;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
