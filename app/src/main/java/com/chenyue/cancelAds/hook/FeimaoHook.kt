package com.chenyue.cancelAds.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by chenyue on 2022/1/26 0026.
 */
class FeimaoHook : IXposedHookLoadPackage {

    private val PACKAGE_NAME = "com.feemoo"
    private val TAG = "飞猫-hook-"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val packageName = lpparam.packageName
        val classLoader = lpparam.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

        XposedBridge.log(TAG)

        XposedHelpers.findAndHookMethod(
            "com.feemoo.activity.main.MainActivity",
            classLoader,
            "initData",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    XposedBridge.log(TAG + "com.feemoo.activity.main.MainActivity#initData")
                    return null
                }
            }
        )

        XposedHelpers.findAndHookMethod(
            "com.feemoo.activity.login.SplashActivity",
            classLoader,
            "loadSplashAd",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    XposedBridge.log(TAG + "com.feemoo.activity.login.SplashActivity#loadSplashAd")
                    return null
                }
            }
        )

        XposedHelpers.findAndHookMethod(
            "com.feemoo.activity.login.SplashActivity",
            classLoader,
            "initGuide",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam): Any? {
                    XposedBridge.log(TAG + "com.feemoo.activity.login.SplashActivity#initGuide")
                    XposedHelpers.callMethod(param.thisObject, "goToMainActivity")
                    return null
                }
            }
        )

        XposedHelpers.findAndHookMethod(
            "com.mob.tools.utils.DeviceHelper",
            classLoader,
            "cx",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    XposedBridge.log(TAG + "com.mob.tools.utils.DeviceHelper#cx")
                    return false
                }
            }
        )

        XposedHelpers.findAndHookMethod(
            "okhttp3.internal.connection.RealConnection",
            classLoader,
            "supportsUrl",
            XposedHelpers.findClass("okhttp3.HttpUrl", classLoader),
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    XposedBridge.log(TAG + "okhttp3.internal.connection.RealConnection#supportsUrl")
                    return true
                }
            }
        )
    }

}