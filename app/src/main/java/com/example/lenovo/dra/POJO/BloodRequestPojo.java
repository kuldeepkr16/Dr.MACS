package com.example.lenovo.dra.POJO;

public class BloodRequestPojo {
    private String from, phone_no, selectedBloodType, selectedUrgency, NOW;

    public BloodRequestPojo() {
    }

    public BloodRequestPojo(String from, String phone_no, String selectedBloodType, String selectedUrgency, String NOW) {
        this.from = from;
        this.phone_no = phone_no;
        this.selectedBloodType = selectedBloodType;
        this.selectedUrgency = selectedUrgency;
        this.NOW = NOW;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getSelectedBloodType() {
        return selectedBloodType;
    }

    public String getSelectedUrgency() {
        return selectedUrgency;
    }

    public String getNOW(){
        return NOW;
    }
}
