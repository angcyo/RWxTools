package com.angcyo.rwxtools

import android.content.Intent
import com.angcyo.rwxtools.iview.MainUIVIew
import com.angcyo.uiview.base.UIBaseView
import com.angcyo.uiview.base.UILayoutActivity

class MainActivity : UILayoutActivity() {
    override fun onLoadView(intent: Intent?) {
        startIView(MainUIVIew().setEnableClipMode(UIBaseView.ClipMode.CLIP_START))
    }
}
