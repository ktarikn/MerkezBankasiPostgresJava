package com.example.isBankasiDbApp;

import com.example.isBankasiDbApp.displays.ExchangeRecord;
import com.example.isBankasiDbApp.displays.ValuedAppManager;
import com.example.isBankasiDbApp.helpers.ArithmeticHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//Like an app manager, makes calls and returns records
//Application interacts with this class
@Service
public class DisplayFactory {

    private DbManager dbManager;
    private Currency[] rates;
    public DisplayFactory() {
        dbManager = new DbManager();
        //rates = dbManager.getAllRates();
    }


    public Currency[] getRates() {
        return dbManager.getAllRates();
    }

    public  RateRecord[] menu(){ //displays all values in the database
        rates = dbManager.getAllRates();
        ArrayList<RateRecord> records = new ArrayList<>();
        for (Currency rate : rates) {
            records.add(new ValuedAppManager(rate));
        }

        return records.toArray(new RateRecord[records.size()]);
    }
    public RateRecord[]  deleteRate(String kod){
        dbManager.remove(kod);
        return menu();
    }
    public RateRecord[] updateRate(String kod, float buy, float sell, float ebuy, float esell){
        dbManager.update(buy,sell,ebuy,esell,kod);
        return menu();
    }
    public RateRecord[] addRate(String kod, float buy, float sell, float ebuy, float esell){
        dbManager.add(buy,sell,ebuy,esell,kod);
        return menu();
    }
    public RateRecord exchange(String from, String to,float cash) {
        Currency fromRate = dbManager.getRate(from);
        Currency toRate = dbManager.getRate(to);
        if (fromRate.getEffective_rate_sell() == 0){

            throw new IllegalArgumentException("There is no effective selling value for the rate:" + from);

        }
        if(toRate.getEffective_rate_buy() == 0)
        {
            throw new IllegalArgumentException("There is no effective buying value for the rate:" +to);


        }
        return new ExchangeRecord(from,fromRate.getEffective_rate_sell(),
                to, toRate.getEffective_rate_buy(),
                cash,
                ArithmeticHandler.exchange(fromRate.getEffective_rate_sell() ,
                        toRate.getEffective_rate_buy(), cash));
                // fromRate in satış değerinden satıp toRate in alış değerinden aldı

    }
}
