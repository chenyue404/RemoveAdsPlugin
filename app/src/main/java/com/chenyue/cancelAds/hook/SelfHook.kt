package com.chenyue.cancelAds.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SelfHook : IXposedHookLoadPackage {
    //自我hook
    private val PACKAGE_NAME = "com.chenyue.cancelAds"
    private val TAG = "cancelAds-hook-"
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        val packageName = lpparam.packageName
        val classLoader = lpparam.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

        XposedBridge.log(TAG)

        XposedHelpers.findAndHookMethod("com.chenyue.cancelAds.ui.MainActivity",
                classLoader,
            "a",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        XposedBridge.log(TAG + "isActive")
                        param.result = true
                    }
                })
    }
}