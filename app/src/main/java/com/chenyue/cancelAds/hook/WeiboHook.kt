package com.chenyue.cancelAds.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
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

        val StatusClass = XposedHelpers.findClass("com.weico.international.model.sina.Status", classLoader);
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

        val PageInfo = XposedHelpers.findClass("com.weico.international.model.sina.PageInfo", classLoader);
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

    }
}