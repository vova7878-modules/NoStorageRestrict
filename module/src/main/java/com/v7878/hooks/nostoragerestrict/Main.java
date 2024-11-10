package com.v7878.hooks.nostoragerestrict;

import android.util.Log;

import com.github.kr328.zloader.ZygoteLoader;
import com.v7878.r8.annotations.DoNotObfuscate;
import com.v7878.r8.annotations.DoNotObfuscateType;
import com.v7878.r8.annotations.DoNotShrink;
import com.v7878.r8.annotations.DoNotShrinkType;

@DoNotShrinkType
@DoNotObfuscateType
public class Main {
    public static String TAG = "NO_STORAGE_RESTRICT";

    @SuppressWarnings({"unused", "ConfusingMainMethod"})
    @DoNotShrink
    @DoNotObfuscate
    public static void main() throws Throwable {
        Log.w(TAG, "Injected into " + ZygoteLoader.getPackageName());
        LoadedApkHook.init();
        Log.w(TAG, "Done");
    }
}
