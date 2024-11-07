package com.v7878.hooks.nostoragerestrict;

import static com.v7878.hooks.nostoragerestrict.Main.TAG;
import static com.v7878.unsafe.Reflection.getDeclaredMethod;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.v7878.r8.annotations.DoNotObfuscate;
import com.v7878.r8.annotations.DoNotShrink;
import com.v7878.vmtools.Hooks;
import com.v7878.vmtools.Hooks.EntryPointType;

import java.lang.reflect.Method;

@SuppressLint("PrivateApi")
public class DocumentsUIHook {
    private static final String DOCUMENT_STACK = "com.android.documentsui.base.DocumentStack";
    private static final String ACTIVITY_CONFIG = "com.android.documentsui.ActivityConfig";
    private static final String FILES_CONFIG = "com.android.documentsui.files.Config";
    private static final String SHARED = "com.android.documentsui.base.Shared.java";

    public static void init(ClassLoader loader) throws Throwable {
        {
            Class<?> stack = Class.forName(DOCUMENT_STACK, true, loader);
            Method hooker = getDeclaredMethod(DocumentsUIHook.class, "managedModeEnabled", Object.class, Object.class);
            {
                Class<?> config = Class.forName(ACTIVITY_CONFIG, true, loader);
                Method target = getDeclaredMethod(config, "managedModeEnabled", stack);
                Hooks.hook(target, hooker, EntryPointType.DIRECT);
            }
            {
                Class<?> config = Class.forName(FILES_CONFIG, true, loader);
                Method target = getDeclaredMethod(config, "managedModeEnabled", stack);
                Hooks.hook(target, hooker, EntryPointType.DIRECT);
            }
        }
        {
            Class<?> shared = Class.forName(SHARED, true, loader);
            Method hooker = getDeclaredMethod(DocumentsUIHook.class, "shouldRestrictStorageAccessFramework", Activity.class);
            Method target = getDeclaredMethod(shared, "shouldRestrictStorageAccessFramework", Activity.class);
            Hooks.hook(target, hooker, EntryPointType.DIRECT);
        }

        Log.w(TAG, "DocumentsUIHook");
    }

    @SuppressWarnings("unused")
    @DoNotShrink
    @DoNotObfuscate
    private static boolean managedModeEnabled(Object /* ActivityConfig */ thiz,
                                              Object /* DocumentStack */ stack) {
        /*
         * When managed mode is enabled, there will be special UI behaviors:
         * 1) active downloads will be visible in the UI.
         * 2) Android/[data|obb|sandbox] directories will not be hidden.
         */
        return true;
    }

    @SuppressWarnings("unused")
    @DoNotShrink
    @DoNotObfuscate
    private static boolean shouldRestrictStorageAccessFramework(Activity activity) {
        /*
         * Whether the calling app should be restricted in Storage Access Framework or not.
         */
        return false;
    }
}
