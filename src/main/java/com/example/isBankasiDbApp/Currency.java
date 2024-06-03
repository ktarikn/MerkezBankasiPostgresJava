package com.example.isBankasiDbApp;

//Currency format from Turkey's central bank
public class Currency { //class to hold exchange rate data
    private float rate_buy;
    private float rate_sell;
    private float effective_rate_buy;
    private float effective_rate_sell;
    private String code;
    public Currency(float rate_buy, float rate_sell,
                    float effective_rate_buy, float effective_rate_sell, String code) {
        this.rate_buy = rate_buy;
        this.rate_sell = rate_sell;
        this.effective_rate_buy = effective_rate_buy;
        this.effective_rate_sell = effective_rate_sell;
        this.code = code;
    }

    public float getEffective_rate_buy() {
        return effective_rate_buy;
    }

    public float getRate_buy() {
        return rate_buy;
    }

    public float getEffective_rate_sell() {
        return effective_rate_sell;
    }

    public float getRate_sell() {
        return rate_sell;
    }
    public String getCode() {
        return code;
    }
    public String toString() { //returns all data with comma inbetween
        return new StringBuilder().append(getRate_buy()).append(",").append(getRate_sell()).append(",").append(getEffective_rate_buy()).append(",").append(getEffective_rate_sell()).append(",").append(getCode()).toString();
    }
}
