package com.alfaco_1.testno1;

class RewardModel {

    private String title;
    private String expiryData;
    private String coupenBody;

    public RewardModel(String title, String expiryData, String coupenBody) {
        this.title = title;
        this.expiryData = expiryData;
        this.coupenBody = coupenBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpiryData() {
        return expiryData;
    }

    public void setExpiryData(String expiryData) {
        this.expiryData = expiryData;
    }

    public String getCoupenBody() {
        return coupenBody;
    }

    public void setCoupenBody(String coupenBody) {
        this.coupenBody = coupenBody;
    }
}
