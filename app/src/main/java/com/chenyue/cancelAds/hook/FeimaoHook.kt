package com.chenyue.cancelAds.hook

import android.content.Intent
import de.robv.android.xposed.*
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
                    return true
                }
            }
        )

        XposedHelpers.findAndHookMethod(
            "com.feemoo.base.BaseActivity",
            classLoader,
            "startActivity",
            Intent::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val intent: Intent = param.args[0] as Intent
                    val className = intent.component?.className
                    if (className?.contains("VipOverdueActivity", true) == true) {
                        param.result = true
                        return
                    }
                }
            }
        )
    }

}