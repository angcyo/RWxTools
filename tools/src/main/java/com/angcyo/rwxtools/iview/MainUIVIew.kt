package com.angcyo.rwxtools.iview

import android.os.Bundle
import com.angcyo.rwxtools.R
import com.angcyo.rwxtools.base.BaseItemUIView
import com.angcyo.uiview.RCrashHandler
import com.angcyo.uiview.accessibility.ASTip
import com.angcyo.uiview.accessibility.BaseAccessibilityService
import com.angcyo.uiview.base.Item
import com.angcyo.uiview.base.SingleItem
import com.angcyo.uiview.recycler.RBaseViewHolder

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

                holder.tv(R.id.text_view).text = "状态:${BaseAccessibilityService.isServiceEnabled()}"
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
    }
}