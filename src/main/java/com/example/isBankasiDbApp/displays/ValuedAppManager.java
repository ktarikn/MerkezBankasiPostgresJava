package com.example.isBankasiDbApp.displays;



import com.example.isBankasiDbApp.Currency;
import com.example.isBankasiDbApp.RateRecord;

//public record ValuedAppManager (float rateBuy,float rateSell, float rateEffectiveBuy, float rateEffectiveSell, String code) implements  RateRecord{

public record ValuedAppManager (Currency rate) implements RateRecord {
}
