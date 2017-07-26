package com.nanjing.bus.shuttle.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.nanjing.bus.shuttle.app.AppData;


/**
 * Created by 剑指锁妖塔 on 16-1-12.
 */
public class SharedTools {

    private static final String sharedName = "bus";

    private static final String LINE = "line";

    private static final String START = "start";
    private static final String LAT = "latitude";
    private static final String LON = "longitude";

    private static final String DELAY = "delayMillis";


    private static void setValue(String key, Object s) {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        if (s instanceof String) {
            editor.putString(key, (String) s);
        } else if (s instanceof Integer) {
            editor.putInt(key, (Integer) s);
        } else if (s instanceof Boolean) {
            editor.putBoolean(key, (Boolean) s);
        } else if (s instanceof Long) {
            editor.putLong(key, (Long) s);
        } else if (s instanceof Float) {
            editor.putFloat(key, (Float) s);
        }
        editor.apply();
    }

    private static String getStringValue(String key, String defaultStr) {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        return shared.getString(key, defaultStr);
    }

    private static int getIntValue(String key, int defaultValue) {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        return shared.getInt(key, defaultValue);
    }

    private static long getLongValue(String key) {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        return shared.getLong(key, 0);
    }

    private static boolean getBooleanValue(String key) {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        return shared.getBoolean(key, false);
    }

    public static void setLine(String line) {
        setValue(LINE, line);
    }

    public static String getLine() {
        return getStringValue(LINE, "南京线");
    }

    public static void setStart(boolean start) {
        setValue(START, start);
    }

    public static boolean getStart() {
        return getBooleanValue(START);
    }


    public static void setLAT(double lat) {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        String s = lat + "";
        editor.putString(LAT, s);
        editor.apply();
    }

    public static double getLAT() {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        String s = shared.getString(LAT, "0");
        double dd = Double.parseDouble(s);
        return dd;
    }

    public static void setLON(double lon) {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        String s = lon + "";
        editor.putString(LON, s);
        editor.apply();
    }

    public static double getLON() {
        SharedPreferences shared = AppData.getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        String s = shared.getString(LON, "0");
        double dd;
        try {
            dd = Double.parseDouble(s);
        } catch (Exception e) {
            dd = 0;
        }
        return dd;
    }

    public static void setDelay(int line) {
        setValue(DELAY, line);
    }

    public static int getDelay() {
        return getIntValue(DELAY, 1000);
    }
}
