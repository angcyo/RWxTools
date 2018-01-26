package com.angcyo.rwxtools.main

import android.view.accessibility.AccessibilityEvent
import com.angcyo.library.utils.L
import com.angcyo.uiview.accessibility.AccessibilityInterceptor
import com.angcyo.uiview.accessibility.BaseAccessibilityService

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：用来切换QQ导航栏的拦截器
 * 创建人员：Robi
 * 创建时间：2018/01/26 09:23
 * 修改人员：Robi
 * 修改时间：2018/01/26 09:23
 * 修改备注：
 * Version: 1.0.0
 */
class QQNavInterceptor : AccessibilityInterceptor() {

    companion object {
        /**导航到消息*/
        val NAV_MESSAGE = "0_0_2_0"
        /**联系人*/
        val NAV_CONTACT = "0_0_2_1"
        /**动态*/
        val NAV_DYNAMIC = "0_0_2_2"
    }

    var target = ""

    init {
        filterPackageName = "com.tencent.mobileqq"
    }

    override fun onAccessibilityEvent(accService: BaseAccessibilityService, event: AccessibilityEvent) {
        super.onAccessibilityEvent(accService, event)
        if (event.source == null) {
            return
        }

        L.e("call: QQNavInterceptor -> isInMessage:${isInMessage(event)} isInContact:${isInContact(event)} isInDynamic:${isInDynamic(event)}")

        fun jump() {
            BaseAccessibilityService.nodeFromPath(BaseAccessibilityService.getRootNodeInfo(event.source), target)?.let {
                BaseAccessibilityService.clickNode(it)
            }
        }

        when (target) {
            NAV_MESSAGE -> {
                if (isInMessage(event)) {
                    BaseAccessibilityService.removeInterceptor(this)
                } else {
                    jump()
                }
            }
            NAV_CONTACT -> {
                if (isInContact(event)) {
                    BaseAccessibilityService.removeInterceptor(this)
                } else {
                    jump()
                }
            }
            NAV_DYNAMIC -> {
                if (isInDynamic(event)) {
                    BaseAccessibilityService.removeInterceptor(this)
                } else {
                    jump()
                }
            }
        }
    }

    /**是否已经在消息界面*/
    private fun isInMessage(event: AccessibilityEvent): Boolean {
        val messageNodeInfo = BaseAccessibilityService.nodeFromPath(BaseAccessibilityService.getRootNodeInfo(event.source), "0_0_2_0_2")
        return messageNodeInfo?.isSelected ?: false
    }

    private fun isInContact(event: AccessibilityEvent): Boolean {
        val contactNodeInfo = BaseAccessibilityService.nodeFromPath(BaseAccessibilityService.getRootNodeInfo(event.source), "0_0_2_1_0_2")
        return contactNodeInfo?.isSelected ?: false
    }

    private fun isInDynamic(event: AccessibilityEvent): Boolean {
        val dynamicNodeInfo = BaseAccessibilityService.nodeFromPath(BaseAccessibilityService.getRootNodeInfo(event.source), "0_0_2_2_0_2")
        return dynamicNodeInfo?.isSelected ?: false
    }
}