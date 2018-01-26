package com.angcyo.rwxtools.main

import android.text.TextUtils
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.angcyo.uiview.accessibility.BaseAccessibilityService

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：微信自动接受好友验证
 * 创建人员：Robi
 * 创建时间：2018/01/26 11:59
 * 修改人员：Robi
 * 修改时间：2018/01/26 11:59
 * 修改备注：
 * Version: 1.0.0
 */
class WxAutoAcceptInterceptor : WxNavInterceptor() {

    override fun onAccessibilityEvent(accService: BaseAccessibilityService, event: AccessibilityEvent) {
        super.onAccessibilityEvent(accService, event)
        event.source?.let {
            val rootNodeInfo = BaseAccessibilityService.getRootNodeInfo(it)

            if (isInNewFriend(rootNodeInfo)) {
                BaseAccessibilityService.removeInterceptor(this)
                onJumpToTarget?.invoke()
                Unit
            } else if (isInContact(rootNodeInfo)) {
                getContactListView(rootNodeInfo)?.let {
                    it.findAccessibilityNodeInfosByText("新的朋友")?.let {
                        if (it.isNotEmpty()) {
                            try {
                                jumpToNewFriend(it[0].parent.parent)
                            } catch (e: Exception) {
                            }
                        }
                    }
                }
            }
        }
    }

    /*跳转到 新的朋友 界面*/
    private fun jumpToNewFriend(nodeInfo: AccessibilityNodeInfo) {
        BaseAccessibilityService.clickNode(nodeInfo)
    }

    /*是否在 新的朋友 界面*/
    private fun isInNewFriend(rootNodeInfo: AccessibilityNodeInfo): Boolean {
        return TextUtils.equals(BaseAccessibilityService.nodeFromPath(rootNodeInfo, "0_1")?.text, "新的朋友")
    }
}