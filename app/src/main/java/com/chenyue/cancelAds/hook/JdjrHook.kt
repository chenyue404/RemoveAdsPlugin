package com.chenyue.cancelAds.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by chenyue404
 */
class JdjrHook : IXposedHookLoadPackage {

    //京东金融
    private val PACKAGE_NAME = "com.jd.jrapp"
    private val TAG = "京东金融-hook-"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        val packageName = lpparam.packageName
        val classLoader = lpparam.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

        XposedBridge.log(TAG)

        findAndHookMethod(
            "com.jd.jrapp.bm.mainbox.main.model.HallWatchDog",
            classLoader,
            "needShowAdvertisementPage",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    XposedBridge.log(TAG + "needShowAdvertisementPage")
                    return false
                }
            }
        )

    }
}