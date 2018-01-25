package com.angcyo.rwxtools.main

import android.graphics.Color
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.ImageView
import android.widget.TextView
import com.angcyo.github.utilcode.utils.CmdUtil
import com.angcyo.rwxtools.R
import com.angcyo.uiview.accessibility.BaseAccessibilityService
import com.angcyo.uiview.accessibility.CanvasLayout
import com.angcyo.uiview.accessibility.RAlertTip
import com.angcyo.uiview.accessibility.shape.RectShape
import com.angcyo.uiview.utils.RUtils
import com.angcyo.uiview.utils.ScreenUtil

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/01/24 14:16
 * 修改人员：Robi
 * 修改时间：2018/01/24 14:16
 * 修改备注：
 * Version: 1.0.0
 */
class RAccessibilityService : BaseAccessibilityService() {
    var canvasLayout: CanvasLayout? = null
    val outBoundsRect = Rect()

    var filterLayout: View? = null
    var filterAlertTip: RAlertTip? = null

    override fun onWindowStateChanged(event: AccessibilityEvent) {
        super.onWindowStateChanged(event)
        event.source?.let {
            canvasLayout?.clear()
            showCanvasDebug(it)
        }
        showFilterDialog(event)
    }

    override fun onWindowContentChanged(event: AccessibilityEvent) {
        super.onWindowContentChanged(event)
        event.source?.let {
            canvasLayout?.clear()
            showCanvasDebug(getRootNodeInfo(it))
        }
    }

    /**提示是否需要过滤当前的窗口*/
    fun showFilterDialog(event: AccessibilityEvent) {
        if (!RUtils.canDrawOverlays()) {
            return
        }

        if (filterLayout == null) {
            filterAlertTip = RAlertTip().apply {
                needTouch = true
                windowWidth = -2
                windowHeight = -2
                offsetX = (10 * ScreenUtil.density()).toInt()
                offsetY = (100 * ScreenUtil.density()).toInt()
                windowGravity = Gravity.TOP or Gravity.END
                show(R.layout.alert_filter_dialog_layout) {
                    filterLayout = it
                }
            }
        }

        val appInfo = CmdUtil.getAppInfo(applicationContext, event.packageName.toString())
        filterLayout?.let {
            it.findViewById<ImageView>(R.id.image_view)?.let {
                it.setImageDrawable(appInfo.appIcon)
            }
            it.findViewById<TextView>(R.id.text_view)?.let {
                it.text = appInfo.appName
            }
            it.findViewById<View>(R.id.add_button)?.setOnClickListener {
                //Tip.tip(appInfo.appName)

                serviceInfo?.let {
                    it.packageNames = arrayOf(appInfo.packageName)
                    serviceInfo = it
                }

                filterAlertTip?.hide()
                //filterLayout = null
            }
        }
    }

    /**显示调试信息*/
    fun showCanvasDebug(nodeInfo: AccessibilityNodeInfo) {
        if (!RUtils.canDrawOverlays()) {
            return
        }

        if (canvasLayout == null) {
            RAlertTip().apply {
                show(R.layout.alert_canvas_layout) {
                    canvasLayout = it as CanvasLayout
                }
            }
        }
        canvasLayout?.let { cLayout ->
            for (i in 0 until nodeInfo.childCount) {
                nodeInfo.getChild(i)?.let {
                    it.getBoundsInScreen(outBoundsRect)
                    cLayout.addShape(RectShape().apply {
                        drawRect.set(outBoundsRect)
                        drawText = it.text?.toString() ?: ""
                        drawTextColor = Color.RED
                        textDrawX = outBoundsRect.left.toFloat()
                        textDrawY = outBoundsRect.top.toFloat()
                    })
                    showCanvasDebug(it)
                }
            }
        }
    }
}