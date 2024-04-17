package io.github.jsonmona.kakaotalkmultidevice;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressLint("DiscouragedPrivateApi")
@SuppressWarnings("ConstantConditions")
public class KakaoTalkMultiDevice implements IXposedHookLoadPackage {

    private static final String TAG = KakaoTalkMultiDevice.class.getSimpleName();

    // Packages to spoof
    private static final String[] packages = {
        "com.kakao.talk"
    };

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        String packageName = loadPackageParam.packageName;

        if (Arrays.asList(packages).contains(packageName)) {
            propsToTabS7Plus();
            XposedBridge.log("Spoofed " + packageName + " as Galaxy Tab S7+");
        }
    }

    private static void propsToTabS7Plus() {
        setPropValue("MANUFACTURER", "Samsung");
        setPropValue("MODEL", "SM-T975N");
    }

    private static void setPropValue(String key, Object value) {
        try {
            Log.d(TAG, "Defining prop " + key + " to " + value.toString());
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            XposedBridge.log("Failed to set prop: " + key + "\n" + Log.getStackTraceString(e));
        }
    }
}