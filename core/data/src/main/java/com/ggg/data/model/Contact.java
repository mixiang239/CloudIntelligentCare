package com.ggg.data.model;

public class Contact {
    private String name;
    private String number;
    private String group;

    public Contact() {
    }

    public Contact(String name, String number, String group) {
        this.name = name;
        this.number = number;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
