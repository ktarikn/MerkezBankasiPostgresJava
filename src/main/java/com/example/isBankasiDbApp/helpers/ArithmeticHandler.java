package com.example.isBankasiDbApp.helpers;

public class ArithmeticHandler {

    public static Float exchange(Float fromSellRate, Float toBuyRate, Float cash){
        return (cash*fromSellRate)/toBuyRate;
    }
}
