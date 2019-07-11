package com.chenyue.cancelAds.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
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

        val StatusClass = XposedHelpers.findClass("com.weico.international.model.sina.Status", classLoader);
        findAndHookMethod("com.weico.international.utility.KotlinExtendKt",
            classLoader,
            "isWeiboUVEAd",
            StatusClass,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
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
                    param.result = null
                }
            })

//        findAndHookMethod("com.weico.international.activity.NewSplashActivity",
//            classLoader,
//            "requestSplashAd",
//            object : XC_MethodHook() {
//                override fun beforeHookedMethod(param: MethodHookParam) {
//                    Log.e("cancelAds", "requestSplashAd")
//                    XposedHelpers.callMethod(
//                        "com.weico.international.activity.NewSplashActivity",
//                        "next"
//                    );
//                    param.result = null
//                }
//            })
//
//        val FlashAdBuilder = XposedHelpers.findClass("com.weibo.mobileads.controller.Builder.FlashAdBuilder", classLoader);
//        findAndHookMethod("com.weibo.mobileads.controller.AdSdk",
//            classLoader,
//            "initFlashAd",
//            FlashAdBuilder,
//            object : XC_MethodHook() {
//                override fun beforeHookedMethod(param: MethodHookParam) {
//                    Log.e("cancelAds", "initFlashAd")
//                    param.result = null
//                }
//            })


        findAndHookMethod("com.weibo.mobileads.view.FlashAd",
            classLoader,
            "isReady",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.result = false
                }
            })

    }
}