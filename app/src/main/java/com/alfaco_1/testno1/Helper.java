package com.alfaco_1.testno1;

class Helper {
    String email,phone,password;

    public Helper() {
    }

    public Helper(String email, String phone, String password) {
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
