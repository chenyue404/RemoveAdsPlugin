package com.chenyue.cancelAds.hook

import android.app.Activity
import android.app.AndroidAppHelper
import android.content.Context
import android.widget.Toast
import com.chenyue.cancelAds.BuildConfig
import de.robv.android.xposed.*
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.net.URLDecoder

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
                    log("isWeiboUVEAd")
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
                    log("findUVEAd")
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
                        log("doWhatNext-" + param.result)
                        if (param.result.equals("GDTAD")
                            || param.result.equals("sinaAD")
                        ) {
                            param.result = "main"
                        }
                    }
                }
            })

        try {
            findAndHookMethod(
                "com.weico.international.manager.ProcessMonitor",
                classLoader,
                "displayAd",
                Long::class.java,
                Activity::class.java,
                Boolean::class.java,
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any {
                        log("displayAd")
                        return true
                    }
                }
            )
        } catch (e: NoSuchMethodError) {
            log("NoSuchMethodError--com.weico.international.manager.ProcessMonitor")
        }


        findAndHookMethod(
            "com.weico.international.activity.LogoActivity",
            classLoader,
            "triggerPermission",
            Boolean::class.java,
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam): Any {
                    log("triggerPermission")
                    XposedHelpers.callMethod(param.thisObject, "initPermission")
                    return true
                }
            })

        findAndHookMethod(
            "com.weico.international.activity.WebviewActivity",
            classLoader,
            "loadUrl",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    var url = param.args[0] as String
                    log(url)
                    val sinaUrl = "://weibo.cn/sinaurl?u="
                    if (url.contains(sinaUrl)) {
                        url = url.substring(url.indexOf(sinaUrl) + sinaUrl.length, url.length)
                        url = URLDecoder.decode(url)
                        log(url)
                        param.args[0] = url
                    } else {
                        Toast.makeText(
                            AndroidAppHelper.currentApplication().applicationContext,
                            "WebviewActivity-$url", Toast.LENGTH_SHORT
                        ).show()
                    }
                    super.beforeHookedMethod(param)
                }
            })

        findAndHookMethod(
            "com.sina.wbs.webkit.WebView",
            classLoader,
            "loadUrl",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    var url = param.args[0] as String
                    log(url)
                    val sinaUrl = "://weibo.cn/sinaurl?u="
                    if (url.contains(sinaUrl)) {
                        url = url.substring(url.indexOf(sinaUrl) + sinaUrl.length, url.length)
                        url = URLDecoder.decode(url)
                        log(url)
                        param.args[0] = url
                    } else {
                        Toast.makeText(
                            AndroidAppHelper.currentApplication().applicationContext,
                            "WebView-$url", Toast.LENGTH_SHORT
                        ).show()
                    }
                    super.beforeHookedMethod(param)
                }
            })

        findAndHookMethod(
            "com.weico.international.browser.BrowserManager",
            classLoader,
            "loadUrl",
            Context::class.java,
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    var url = param.args[1] as String
                    log(url)
                    val sinaUrl = "://weibo.cn/sinaurl?u="
                    if (url.contains(sinaUrl)) {
                        url = url.substring(url.indexOf(sinaUrl) + sinaUrl.length, url.length)
                        url = URLDecoder.decode(url)
                        log(url)
                        param.args[1] = url
                    } else {
                        Toast.makeText(
                            AndroidAppHelper.currentApplication().applicationContext,
                            "BrowserManager-$url", Toast.LENGTH_SHORT
                        ).show()
                    }
                    super.beforeHookedMethod(param)
                }
            })

        findAndHookMethod("com.weico.international.activity.v4.Setting",
            classLoader,
            "loadBoolean",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    log("loadBoolean--$key")
                    when {
                        key == "BOOL_UVE_FEED_AD" -> param.result = false
                        key.startsWith("BOOL_AD_ACTIVITY_BLOCK_") -> param.result = true
                    }
                }
            })

        findAndHookMethod(
            "com.weico.international.activity.v4.Setting",
            classLoader,
            "loadInt",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    log("loadInt--$key")
                    when (key) {
                        "ad_interval" -> param.result = Int.MAX_VALUE
                        "display_ad" -> param.result = 0
                    }
                }
            }
        )

        findAndHookMethod(
            "com.weico.international.activity.v4.Setting",
            classLoader,
            "loadStringSet",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    log("loadStringSet--$key")
                    when (key) {
                        "CYT_DAYS" -> param.result = setOf<String>()
                    }
                }
            }
        )

        findAndHookMethod(
            "com.weico.international.activity.v4.Setting",
            classLoader,
            "loadString",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    log("loadString--$key")
                    when (key) {
                        "video_ad" -> param.result = ""
                    }
                }
            }
        )
    }

    private fun log(log: String) {
        if (BuildConfig.DEBUG) {
            XposedBridge.log("$TAG-$log")
        }
    }
}