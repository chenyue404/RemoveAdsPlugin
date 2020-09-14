package com.chenyue.cancelAds.hook

import android.app.Activity
import de.robv.android.xposed.*
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by chenyue404
 */
class WeiboHook : IXposedHookLoadPackage {

    //微博国际版
    private val PACKAGE_NAME = "com.weico.international"
    private val TAG = "微博-hook-"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        val packageName = lpparam.packageName
        val classLoader = lpparam.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

        XposedBridge.log(TAG)

        val StatusClass =
            XposedHelpers.findClass("com.weico.international.model.sina.Status", classLoader)
        findAndHookMethod("com.weico.international.utility.KotlinExtendKt",
            classLoader,
            "isWeiboUVEAd",
            StatusClass,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    XposedBridge.log(TAG + "isWeiboUVEAd")
                    param.result = false
                }
            })

        val PageInfo =
            XposedHelpers.findClass("com.weico.international.model.sina.PageInfo", classLoader)
        findAndHookMethod("com.weico.international.utility.KotlinUtilKt",
            classLoader,
            "findUVEAd",
            PageInfo,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    XposedBridge.log(TAG + "findUVEAd")
                    param.result = null
                }
            })

        findAndHookMethod(
            "com.weico.international.activity.LogoActivity",
            classLoader,
            "doWhatNext",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    if (param != null) {
                        XposedBridge.log(TAG + "doWhatNext-" + param.result)
                        if (param.result.equals("GDTAD")
                            || param.result.equals("sinaAD")
                        ) {
                            param.result = "main"
                        }
                    }
                }
            })

        findAndHookMethod(
            "com.weico.international.manager.ProcessMonitor",
            classLoader,
            "displayAd",
            Long::class.java,
            Activity::class.java,
            Boolean::class.java,
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    XposedBridge.log(TAG + "displayAd")
                    return true
                }
            }
        )

        findAndHookMethod(
            "com.weico.international.activity.LogoActivity",
            classLoader,
            "triggerPermission",
            Boolean::class.java,
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam): Any {
                    XposedBridge.log(TAG + "triggerPermission")
                    XposedHelpers.callMethod(param.thisObject, "initPermission")
                    return true
                }
            })
    }
}