package com.angcyo.rwxtools.iview

import android.view.LayoutInflater
import com.angcyo.rwxtools.R
import com.angcyo.uidemo.layout.base.BaseContentUIView
import com.angcyo.uiview.container.ContentLayout

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

class MainUIVIew : BaseContentUIView() {
    override fun inflateContentLayout(baseContentLayout: ContentLayout?, inflater: LayoutInflater?) {
        inflate(R.layout.activity_main)
    }

}