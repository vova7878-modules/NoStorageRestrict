package com.v7878.hooks.nostoragerestrict;

import android.util.Log;

import com.github.kr328.zloader.ZygoteLoader;

public class Main {
    public static String TAG = "NO_STORAGE_RESTRICT";

    @SuppressWarnings({"unused", "ConfusingMainMethod"})
    public static void main() throws Throwable {
        Log.w(TAG, "Injected into " + ZygoteLoader.getPackageName());
        LoadedApkHook.init();
        Log.w(TAG, "Done");
    }
}
