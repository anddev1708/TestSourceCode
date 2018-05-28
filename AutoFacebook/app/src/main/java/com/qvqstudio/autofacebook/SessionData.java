package com.qvqstudio.autofacebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionData {
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    public static String EMPTY_STRING = "";
    public static String TOKEN = "Token";
    public static int EMPTY_INT = 0;

    /**
     * This is constructor
     *
     * @param context
     */

    public SessionData(Context context) {
        pref = context.getSharedPreferences("MyPoshPets", 0);
        editor = pref.edit();

    }

    /**
     * This is used to add Object as JSON String into shared preferences
     *
     * @param key
     * @param value
     */

    public void setObjectAsString(String key, String value) {
        Log.d(key, value);
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * This is used to get Object as JSON String from preferences
     *
     * @param key
     * @return
     */

    public String getObjectAsString(String key) {
        return pref.getString(key, EMPTY_STRING);
    }

    /**
     * This is used to add Object as JSON String into shared preferences
     *
     * @param key
     * @param position
     */

    public void setObjectAsInt(String key, int position) {

        Log.d(key, position + "");
        editor.putInt(key, position);
        editor.commit();
    }

    public int getObjectAsInt(String key) {
        return pref.getInt(key, EMPTY_INT);
    }

}