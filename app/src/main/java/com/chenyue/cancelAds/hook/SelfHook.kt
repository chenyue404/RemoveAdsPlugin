package com.chenyue.cancelAds.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
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

        if (XposedHelpers.findMethodExactIfExists(
                "com.chenyue.cancelAds.ui.MainActivity",
                classLoader,
                "a"
            ) != null
        ) {
            XposedHelpers.findAndHookMethod("com.chenyue.cancelAds.ui.MainActivity",
                classLoader,
                "a",
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any {
                        XposedBridge.log(TAG + "isActive")
                        return true
                    }
                })
        }

        XposedHelpers.findAndHookMethod("com.chenyue.cancelAds.ui.MainActivity",
            classLoader,
            "isActive",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    XposedBridge.log(TAG + "isActive")
                    param?.result = true
                    return true
                }
            })
    }
}