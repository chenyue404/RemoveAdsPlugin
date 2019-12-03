package com.chenyue.cancelAds.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage


class QQMusicHook : IXposedHookLoadPackage {
    //微博国际版
    private val PACKAGE_NAME = "com.tencent.qqmusic"
    private val TAG = "QQMusic-hook-"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {

        val packageName = lpparam?.packageName
        val classLoader = lpparam?.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

        XposedBridge.log(TAG)

        XposedHelpers.findAndHookMethod(
            "com.tencent.qqmusic.business.ad.l",
            classLoader,
            "d",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    XposedBridge.log(TAG + "d")

                    if (param != null) {
                        XposedBridge.log(TAG + "not null")
                        XposedHelpers.callMethod(param.thisObject, "c")
                    }
                }
            })
    }

}