package com.chenyue.cancelAds.hook

import android.content.Context
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class CoolApkHook : IXposedHookLoadPackage {

    private val PACKAGE_NAME = "com.coolapk.market"
    private val TAG = "酷安-hook-"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val packageName = lpparam.packageName
        val classLoader = lpparam.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

        XposedBridge.log(TAG)

        XposedHelpers.findAndHookMethod(
            "com.coolapk.market.view.splash.SplashActivity",
            classLoader,
            "shouldShowAd",
            Context::class.java,
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    XposedBridge.log(TAG + "shouldShowAd")
                    return false
                }
            }
        )
    }

}