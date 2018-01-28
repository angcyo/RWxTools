package com.angcyo.rwxtools

import android.Manifest
import android.content.Intent
import com.angcyo.rwxtools.iview.MainUIVIew
import com.angcyo.uiview.base.UIBaseView
import com.angcyo.uiview.base.UILayoutActivity

class MainActivity : UILayoutActivity() {
    override fun onLoadView(intent: Intent?) {
        checkPermissions()
        startIView(MainUIVIew().setEnableClipMode(UIBaseView.ClipMode.CLIP_START), false)
    }

    override fun needPermissions(): Array<String> {
        return arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onUIBackPressed() {
        if (BuildConfig.DEBUG) {
            super.onUIBackPressed()
        } else {
            moveTaskToBack()
        }
    }
}
