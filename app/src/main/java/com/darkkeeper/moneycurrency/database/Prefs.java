package com.darkkeeper.moneycurrency.database;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by user on 4/23/15.
 */
public class Prefs {

    private static SharedPreferences sp;
    private static SharedPreferences widgetSp;
    public static String currentCurrency;
    public static String nextCurrency;
    public static String widgetCurrentCurrency;
    public static String widgetNextCurrency;


    public static final String WIDGET_ID = "widget_id_";
    private static final String ACTIVITY_PREF = "activity_pref";
    private static final String CURRENT_CURRENCY = "current_currency";
    private static final String NEXT_CURRENCY = "next_currency";

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_CURRENT_CURRENCY = "widget_current_currency";
    public final static String WIDGET_NEXT_CURRENCY= "widget_next_currency";


    public static void saveChanges(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CURRENT_CURRENCY, currentCurrency);
        editor.putString(NEXT_CURRENCY, nextCurrency);
        editor.putString(WIDGET_CURRENT_CURRENCY, widgetCurrentCurrency);
        editor.putString(WIDGET_NEXT_CURRENCY, widgetNextCurrency);
        editor.commit();
    }

    public static void loadPrefs(Context context){
        sp = context.getSharedPreferences(ACTIVITY_PREF,Context.MODE_PRIVATE);
        currentCurrency = sp.getString(CURRENT_CURRENCY,"EUR");
        nextCurrency = sp.getString(NEXT_CURRENCY,"USD");
        widgetCurrentCurrency = sp.getString(WIDGET_CURRENT_CURRENCY,"EUR");
        widgetNextCurrency = sp.getString(WIDGET_NEXT_CURRENCY,"USD");
    }

    public static void deleteWidgetFromDb(String key){
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void saveWidgetToDb(int id){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(WIDGET_ID+id, id);
        editor.commit();
    }

}