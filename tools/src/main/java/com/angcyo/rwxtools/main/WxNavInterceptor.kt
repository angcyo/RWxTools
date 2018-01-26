package com.angcyo.rwxtools.main

import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.angcyo.library.utils.L
import com.angcyo.uiview.Root
import com.angcyo.uiview.accessibility.AccessibilityInterceptor
import com.angcyo.uiview.accessibility.BaseAccessibilityService
import com.angcyo.uiview.utils.ScreenUtil

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：微信导航栏切换的拦截器
 * 创建人员：Robi
 * 创建时间：2018/01/26 10:03
 * 修改人员：Robi
 * 修改时间：2018/01/26 10:03
 * 修改备注：
 * Version: 1.0.0
 */
open class WxNavInterceptor : AccessibilityInterceptor() {

    companion object {
        val NAV_WX = "1"
        val NAV_CONTACT = "2"
        val NAV_FIND = "3"
        val NAV_ME = "4"

        var PRE_PATH = "0_"
    }

    var target = ""

    /**微信被踢下线之后, 重新登录, 界面的Path会变*/

    init {
        filterPackageName = "com.tencent.mm"
    }

    protected val tempRect = Rect()


    override fun onAccessibilityEvent(accService: BaseAccessibilityService, event: AccessibilityEvent) {
        super.onAccessibilityEvent(accService, event)
        if (event.source == null) {
            return
        }

        //BaseAccessibilityService.logNodeInfo(BaseAccessibilityService.getRootNodeInfo(event.source))
        val rootNodeInfo = BaseAccessibilityService.getRootNodeInfo(event.source)
        L.e("call: WxNavInterceptor -> isInWxMainPager:${isInWxMainPager(rootNodeInfo)} " +
                "isInWx:${isInWx(rootNodeInfo)} " +
                "isInContact:${isInContact(rootNodeInfo)} " +
                "isInFind:${isInFind(rootNodeInfo)} " +
                "isInMe:${isInMe(rootNodeInfo)}")

        try {
            BaseAccessibilityService.logNodeInfo(rootNodeInfo, Root.createExternalFilePath("acc_log", "${rootNodeInfo.packageName}_${Root.createFileName()}"))
        } catch (e: Exception) {
        }

        if (!isInWxMainPager(rootNodeInfo)) {
            return
        }

        fun jump() {
            BaseAccessibilityService.nodeFromPath(BaseAccessibilityService.getRootNodeInfo(event.source), "${PRE_PATH}${target}")?.let {
                BaseAccessibilityService.clickNode(it)
            }
        }

        when (target) {
            NAV_WX -> {
                if (isInWx(rootNodeInfo)) {
                    BaseAccessibilityService.removeInterceptor(this)
                    onJumpToTarget?.invoke()
                } else {
                    jump()
                }
            }
            NAV_CONTACT -> {
                if (isInContact(rootNodeInfo)) {
                    BaseAccessibilityService.removeInterceptor(this)
                    onJumpToTarget?.invoke()
                } else {
                    jump()
                }
            }
            NAV_FIND -> {
                if (isInFind(rootNodeInfo)) {
                    BaseAccessibilityService.removeInterceptor(this)
                    onJumpToTarget?.invoke()
                } else {
                    jump()
                }
            }
            NAV_ME -> {
                if (isInMe(rootNodeInfo)) {
                    BaseAccessibilityService.removeInterceptor(this)
                    onJumpToTarget?.invoke()
                } else {
                    jump()
                }
            }
        }
    }

    /**判断当前是否在微信主页面*/
    protected fun isInWxMainPager(rootNodeInfo: AccessibilityNodeInfo): Boolean {
        var result = false
        BaseAccessibilityService.nodeFromPath(rootNodeInfo, "0")?.let {
            if (it.className.contains("com.tencent.mm.ui.mogic.WxViewPager")) {
                PRE_PATH = ""
//                if (isListView(rootNodeInfo, "${PRE_PATH}0_0") &&
//                        isListView(rootNodeInfo, "${PRE_PATH}0_2") &&
//                        isListView(rootNodeInfo, "${PRE_PATH}0_3") &&
//                        isListView(rootNodeInfo, "${PRE_PATH}0_4")
//                ) {
                result = true
//                }
            }
        }
        BaseAccessibilityService.nodeFromPath(rootNodeInfo, "0_0")?.let {
            if (it.className.contains("com.tencent.mm.ui.mogic.WxViewPager")) {
                PRE_PATH = "0_"
//                if (isListView(rootNodeInfo, "${PRE_PATH}0_0") &&
//                        isListView(rootNodeInfo, "${PRE_PATH}0_2") &&
//                        isListView(rootNodeInfo, "${PRE_PATH}0_3") &&
//                        isListView(rootNodeInfo, "${PRE_PATH}0_4")
//                ) {
                result = true
//                }
            }
        }
        return result
    }

    /**返回微信的ViewPager页面*/
    protected fun getWxViewPager(rootNodeInfo: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        var nodeInfo: AccessibilityNodeInfo? = null
        BaseAccessibilityService.nodeFromPath(rootNodeInfo, "${PRE_PATH}0")?.let {
            if (it.className.contains("com.tencent.mm.ui.mogic.WxViewPager")) {
                nodeInfo = it
            }
        }
        return nodeInfo
    }

    /**判断指定路径的是否是ListView*/
    protected fun isListView(rootNodeInfo: AccessibilityNodeInfo, path: String): Boolean {
        var result = false

        BaseAccessibilityService.nodeFromPath(rootNodeInfo, path)?.let {
            if (it.className.contains("android.widget.ListView")) {
                result = true
            }
        }
        return result
    }

    protected fun getListView(rootNodeInfo: AccessibilityNodeInfo, index: Int): AccessibilityNodeInfo? {
        var result: AccessibilityNodeInfo? = null
//        BaseAccessibilityService.nodeFromPath(rootNodeInfo, path)?.let {
//            if (it.className.contains("android.widget.ListView")) {
//                result = it
//            }
//        }

        var listIndex = 0 //当前是第几个ListView
        getWxViewPager(rootNodeInfo)?.let {
            for (i in 0 until it.childCount) {
                val child = it.getChild(i)
                //L.e("call: isInTab -> $i ${child.className} ${tempRect.left} ${tempRect.right} $tempRect")
                if (child.className.contains("android.widget.ListView")) {
                    if (listIndex == index) {
                        result = child
                        break
                    }
                    listIndex++
                }
            }
        }
        return result
    }

    protected fun getWxListView(rootNodeInfo: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        return getListView(rootNodeInfo, 0)
    }

    protected fun getContactListView(rootNodeInfo: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        return getListView(rootNodeInfo, 1)
    }

    protected fun getFindListView(rootNodeInfo: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        return getListView(rootNodeInfo, 2)
    }

    protected fun getMeListView(rootNodeInfo: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        return getListView(rootNodeInfo, 3)
    }

    protected fun isInTab(rootNodeInfo: AccessibilityNodeInfo, index: Int /*第几个ListView*/): Boolean {
        var result = false
//        BaseAccessibilityService.nodeFromPath(rootNodeInfo, path)?.let {
//            if (it.className.contains("android.widget.ListView")) {
//                it.getBoundsInScreen(tempRect)
//                if (tempRect.left == 0 && tempRect.right == ScreenUtil.screenWidth) {
//                    result = true
//                }
//            }
//        }
//        var listIndex = 0 //当前是第几个ListView
//        getWxViewPager(rootNodeInfo)?.let {
//            for (i in 0 until it.childCount) {
//                val child = it.getChild(i)
//                //L.e("call: isInTab -> $i ${child.className} ${tempRect.left} ${tempRect.right} $tempRect")
//                if (child.className.contains("android.widget.ListView")) {
//                    child.getBoundsInScreen(tempRect)
//                    if (tempRect.left == 0 && tempRect.right == ScreenUtil.screenWidth) {
//                        if (listIndex == index) {
//                            result = true
//                            break
//                        }
//                    }
//                    listIndex++
//                }
//            }
//        }
        getListView(rootNodeInfo, index)?.let {
            it.getBoundsInScreen(tempRect)
            if (tempRect.left == 0 && tempRect.right == ScreenUtil.screenWidth) {
                result = true
            }
        }

        return result
    }

    /**是否在微信Tab页*/
    protected fun isInWx(rootNodeInfo: AccessibilityNodeInfo): Boolean {
        return isInTab(rootNodeInfo, 0) //第一个ListView是微信页
    }

    /**是否在通讯录Tab页*/
    protected fun isInContact(rootNodeInfo: AccessibilityNodeInfo): Boolean {
        return isInTab(rootNodeInfo, 1)//第二个ListView是通讯录
    }

    protected fun isInFind(rootNodeInfo: AccessibilityNodeInfo): Boolean {
        return isInTab(rootNodeInfo, 2)//第三个ListView是发现
    }

    protected fun isInMe(rootNodeInfo: AccessibilityNodeInfo): Boolean {
        return isInTab(rootNodeInfo, 3)//第四个ListView是我
    }
}