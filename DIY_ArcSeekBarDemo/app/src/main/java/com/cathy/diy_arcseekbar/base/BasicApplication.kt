package com.cathy.diy_arcseekbar.base

import android.app.Application
import android.content.Context


/**
+--------------------------------------+
+ @author Catherine Liu
+--------------------------------------+
+ 2020/5/19 10:29
+--------------------------------------+
+ Des:
+--------------------------------------+
 */

class BasicApplication : Application() {
    companion object {
        lateinit var instance: Context
    }

    init {
        instance = this
    }
}