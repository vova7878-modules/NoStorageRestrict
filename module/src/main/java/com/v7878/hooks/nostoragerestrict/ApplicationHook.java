package com.v7878.hooks.nostoragerestrict;

import static com.v7878.hooks.nostoragerestrict.Constants.DOCUMENTSUI_GOOGLE_PACKAGE_NAME;
import static com.v7878.hooks.nostoragerestrict.Constants.DOCUMENTSUI_PACKAGE_NAME;
import static com.v7878.hooks.nostoragerestrict.Constants.EXTERNALSTORAGE_NAME;
import static com.v7878.hooks.nostoragerestrict.Main.TAG;

import android.util.Log;

import com.v7878.r8.annotations.DoNotShrink;

public class ApplicationHook {
    private static void unknown_package(String package_name) {
        Log.e(TAG, "Unknown package: " + package_name);
    }

    @DoNotShrink
    public static void init(String package_name, ClassLoader loader) throws Throwable {
        Log.w(TAG, "loader: " + loader);
        Log.w(TAG, "start: " + package_name);
        if (package_name.equals(DOCUMENTSUI_PACKAGE_NAME) ||
                package_name.equals(DOCUMENTSUI_GOOGLE_PACKAGE_NAME)) {
            DocumentsUIHook.init(loader);
        } else if (package_name.equals(EXTERNALSTORAGE_NAME)) {
            ExternalStorageHook.init(loader);
        } else {
            unknown_package(package_name);
        }
        Log.w(TAG, "end: " + package_name);
    }
}
