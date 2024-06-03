package com.example.isBankasiDbApp.displays;

import com.example.isBankasiDbApp.RateRecord;

public record ExchangeRecord(String from, float kur1, String to, float kur2, float deger, float donusturumusDeger )
    implements RateRecord {

}
