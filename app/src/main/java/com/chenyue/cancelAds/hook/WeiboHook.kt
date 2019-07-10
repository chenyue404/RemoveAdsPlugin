package com.chenyue.cancelAds.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by chenyue404
 */
class WeiboHook : IXposedHookLoadPackage {

    //微博国际版
    private val PACKAGE_NAME = "com.weico.international"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        val packageName = lpparam.packageName
        val classLoader = lpparam.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

        findAndHookMethod("com.weico.international.utility.KotlinExtendKt",
            classLoader,
            "isWeiboUVEAd",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.result = false
                }
            })

        findAndHookMethod("com.weico.international.utility.KotlinUtilKt",
            classLoader,
            "findUVEAd",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.result = null
                }
            })

    }
}