package com.angcyo.rwxtools.iview

import android.os.Bundle
import com.angcyo.rwxtools.R
import com.angcyo.rwxtools.base.BaseItemUIView
import com.angcyo.rwxtools.main.QQNavInterceptor
import com.angcyo.rwxtools.main.WxAutoAcceptInterceptor
import com.angcyo.rwxtools.main.WxNavInterceptor
import com.angcyo.uiview.RCrashHandler
import com.angcyo.uiview.accessibility.ASTip
import com.angcyo.uiview.accessibility.BaseAccessibilityService
import com.angcyo.uiview.accessibility.permission.SettingsCompat
import com.angcyo.uiview.base.Item
import com.angcyo.uiview.base.SingleItem
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.utils.RUtils

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/01/24 11:54
 * 修改人员：Robi
 * 修改时间：2018/01/24 11:54
 * 修改备注：
 * Version: 1.0.0
 */

class MainUIVIew : BaseItemUIView() {
    override fun createItems(items: MutableList<SingleItem>?) {
        items?.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder, posInData: Int, dataBean: Item?) {
                holder.click(R.id.setting_button) {
                    BaseAccessibilityService.openAccessibilityActivity()
                    if (!BaseAccessibilityService.isServiceEnabled()) {
                        ASTip.show()
                    }
                }

                holder.tv(R.id.text_view).text = "状态:${BaseAccessibilityService.isServiceEnabled()}_${SettingsCompat.canDrawOverlays(mActivity)}"

                holder.click(R.id.alert_button) {
                    try {
                        SettingsCompat.manageDrawOverlays(mActivity)
                    } catch (e: Exception) {
                        //Tip.tip("没有找到对应的程序.")
                        RUtils.openAppDetailView(mActivity)
                    }

//                    if (!SettingsCompat.canDrawOverlays(mActivity)) {
//                        SettingsCompat.manageDrawOverlays(mActivity)
//                    } else {
//
//                        RAlertTip().apply {
//                            show(R.layout.alert_canvas_layout) {
//                            }
//
//                        }
//                    }
                }

                holder.click(R.id.open_wx_button) {
                    RUtils.startApp("com.tencent.mm")
                    BaseAccessibilityService.addInterceptor(WxNavInterceptor().apply {
                        target = WxNavInterceptor.NAV_CONTACT
                        onJumpToTarget = {
                            BaseAccessibilityService.clearInterceptor()
                            BaseAccessibilityService.addInterceptor(WxAutoAcceptInterceptor())
                        }
                    })
                }
                holder.click(R.id.open_qq_button) {
                    RUtils.startApp("com.tencent.mobileqq")
                    BaseAccessibilityService.addInterceptor(QQNavInterceptor().apply {
                        target = QQNavInterceptor.NAV_DYNAMIC
                    })
                }
            }

            override fun getItemLayoutId(): Int {
                return R.layout.activity_main
            }
        })
    }

    override fun getTitleString(): String {
        return "R•Sen微信辅助工具"
    }

    override fun onViewShow(bundle: Bundle?) {
        super.onViewShow(bundle)
        mExBaseAdapter.notifyItemChanged(0)
    }

    override fun onViewShowFirst(bundle: Bundle?) {
        super.onViewShowFirst(bundle)
        RCrashHandler.checkCrash(mParentILayout)

//        RAlertTip().apply {
//            needTouch = false
//            show(R.layout.alert_layout) {
//                it.findViewById<View>(R.id.button).setOnClickListener {
//                    Tip.tip(System.nanoTime().toString())
//                }
//            }
//        }
    }
}