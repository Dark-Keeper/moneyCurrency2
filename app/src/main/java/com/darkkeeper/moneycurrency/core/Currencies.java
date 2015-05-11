package com.darkkeeper.moneycurrency.core;

import java.util.ArrayList;

/**
 * Created by andreipiatosin on 5/8/15.
 */
public class Currencies {

    private ArrayList<Currency> currencies;

    public Currency getCurrency(int position){
        return currencies.get(position);
    }

    public ArrayList<Currency> getCurrencies () {
        return currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = currencies;
    }

    public int getSize (){
        return currencies.size();
    }


}
