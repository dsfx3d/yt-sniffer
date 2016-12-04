package dsfx3d.dope.ytp3.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 *  @LocalStore
 *
 *  A @SharedPreferences based key-value storage provider
 *
 * **/


public class LocalStore {

    private SharedPreferences store;

    private L l;

    public LocalStore(Context context) {
        APP = context.getPackageName();
        l = new L("store@"+context.getClass().getSimpleName());
        store = context.getSharedPreferences(APP,Context.MODE_PRIVATE);
        l.p("Added LocalStore `" + APP + "` ", L.VERBOSE);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = store.edit();
        editor.putString(key,value);
        editor.apply();
        l.p("{ "+key+" : "+value+" } added to "+APP,L.VERBOSE);
    }

    public void set(String key, Boolean value) {
        SharedPreferences.Editor editor = store.edit();
        editor.putBoolean(key,value);
        editor.apply();
        l.p("{ "+key+" : "+value+" } added to "+APP,L.VERBOSE);
    }

    public String fetch(String key, String def) {
        l.p("{ "+key+" : "+store.getString(key,def)+" } fetched from "+APP,L.VERBOSE);
        return store.getString(key,def);
    }

    public Boolean fetch(String key, Boolean def) {
        l.p("{ "+key+" : "+store.getBoolean(key,def)+" } fetched from "+APP,L.VERBOSE);
        return store.getBoolean(key,def);
    }

    private String APP;
}
