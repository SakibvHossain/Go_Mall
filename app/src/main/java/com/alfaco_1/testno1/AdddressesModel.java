package com.alfaco_1.testno1;

class AdddressesModel {
    private String fullname;
    private String address;
    private String pincode;
    private Boolean selected;

    public AdddressesModel(String fullname, String address, String pincode,Boolean selected) {
        this.fullname = fullname;
        this.address = address;
        this.pincode = pincode;
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
