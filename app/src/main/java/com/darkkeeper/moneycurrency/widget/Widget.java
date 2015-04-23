package com.darkkeeper.moneycurrency.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.darkkeeper.moneycurrency.R;
import com.darkkeeper.moneycurrency.widget.ConfigWidgetActivity;

import java.util.Arrays;

/**
 * Created by user on 4/20/15.
 */
public class Widget extends AppWidgetProvider {
    private static String LOG_TAG = "myLogs";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, sp, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
        // Удаляем Preferences
        SharedPreferences.Editor editor = context.getSharedPreferences(
                ConfigWidgetActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
          //  editor.remove(ConfigActivity.WIDGET_TEXT + widgetID);
          //  editor.remove(ConfigActivity.WIDGET_COLOR + widgetID);
        }
        editor.commit();

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             SharedPreferences sp, int widgetID) {
        Log.d(LOG_TAG, "updateWidget " + widgetID);

        // Читаем параметры Preferences
        //String widgetText = sp.getString(ConfigWidgetActivity.WIDGET_TEXT + widgetID, null);
        String widgetText = "null";
        if (widgetText == null) return;
     //   int widgetColor = sp.getInt(ConfigWidgetActivity.WIDGET_COLOR + widgetID, 0);

        // Настраиваем внешний вид виджета
        RemoteViews widgetView = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        widgetView.setTextViewText(R.id.tv, widgetText);
    //    widgetView.setInt(R.id.tv, "setBackgroundColor", widgetColor);

        // Обновляем виджет
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }
}
