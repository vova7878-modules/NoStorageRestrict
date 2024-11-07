package com.v7878.hooks.nostoragerestrict;

import static android.os.Build.VERSION.SDK_INT;
import static com.v7878.hooks.nostoragerestrict.Main.TAG;
import static com.v7878.unsafe.Reflection.getDeclaredMethod;

import android.annotation.SuppressLint;
import android.util.Log;

import com.v7878.r8.annotations.DoNotObfuscate;
import com.v7878.r8.annotations.DoNotShrink;
import com.v7878.unsafe.Reflection;
import com.v7878.vmtools.Hooks;
import com.v7878.vmtools.Hooks.EntryPointType;

import java.io.File;
import java.lang.reflect.Method;

@SuppressLint("PrivateApi")
public class ExternalStorageHook {
    private static final String ES_PROVIDER = "com.android.externalstorage.ExternalStorageProvider";
    private static final String FS_PROVIDER = "com.android.internal.content.FileSystemProvider";

    private static void init_old(ClassLoader loader) throws Throwable {
        Class<?> es_provider = Class.forName(ES_PROVIDER, true, loader);
        Class<?> fs_provider = Class.forName(FS_PROVIDER, true, loader);

        {
            Method target = Reflection.getDeclaredMethod(es_provider, "shouldBlockFromTree", String.class);
            Method hooker = getDeclaredMethod(ExternalStorageHook.class, "shouldBlock", Object.class, String.class);
            Hooks.hook(target, hooker, EntryPointType.DIRECT);
        }

        {
            Method target = Reflection.getDeclaredMethod(fs_provider, "shouldHide", File.class);
            Method hooker = getDeclaredMethod(ExternalStorageHook.class, "shouldHide", Object.class, Object.class);
            Hooks.hook(target, hooker, EntryPointType.DIRECT);
        }

        Log.w(TAG, "ExternalStorageHook v1");
    }

    private static void init_new(ClassLoader loader) throws Throwable {
        Class<?> es_provider = Class.forName(ES_PROVIDER, true, loader);

        {
            Method target = Reflection.getDeclaredMethod(es_provider, "shouldBlockDirectoryFromTree", String.class);
            Method hooker = getDeclaredMethod(ExternalStorageHook.class, "shouldBlock", Object.class, String.class);
            Hooks.hook(target, hooker, EntryPointType.DIRECT);
        }

        {
            Method target = Reflection.getDeclaredMethod(es_provider, "shouldHideDocument", String.class);
            Method hooker = getDeclaredMethod(ExternalStorageHook.class, "shouldHide", Object.class, Object.class);
            Hooks.hook(target, hooker, EntryPointType.DIRECT);
        }

        Log.w(TAG, "ExternalStorageHook v2");
    }

    public static void init(ClassLoader loader) throws Throwable {
        if (SDK_INT >= 35) {
            init_new(loader);
            return;
        }
        try {
            init_old(loader);
        } catch (NoSuchMethodException exception) {
            init_new(loader);
        }
    }

    @SuppressWarnings("unused")
    @DoNotShrink
    @DoNotObfuscate
    private static boolean shouldBlock(Object /* ExternalStorageProvider */ thiz, String documentId) {
        return false;
    }

    @SuppressWarnings("unused")
    @DoNotShrink
    @DoNotObfuscate
    private static boolean shouldHide(Object /* FileSystem or ExternalStorage Provider */ thiz,
                                      Object /* String or File */ documentId_or_file) {
        return false;
    }
}
