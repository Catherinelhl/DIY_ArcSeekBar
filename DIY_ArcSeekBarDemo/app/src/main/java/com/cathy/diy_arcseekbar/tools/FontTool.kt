package com.cathy.diy_arcseekbar.tools

import android.graphics.Typeface
import com.cathy.diy_arcseekbar.base.BasicApplication


/**
+--------------------------------------+
+ @author Catherine Liu
+--------------------------------------+
+ 2020/5/19 10:28
+--------------------------------------+
+ Des:字体加载工具
+--------------------------------------+
 */

object FontTool {
    private var TITLE_BOLD_FONT: Typeface? = null

    private var NUMBER_BOLD_FONT: Typeface? = null

    private var NUMBER_LIGHT_FONT: Typeface? = null


    fun initTitleFont() {
        TITLE_BOLD_FONT = Typeface.createFromAsset(
            BasicApplication.instance.assets, "fonts" +
                    "/huakang_w5.TTF"
        )
    }

    fun initNumberFont() {

        NUMBER_BOLD_FONT = Typeface.createFromAsset(
            BasicApplication.instance.assets, "fonts" +
                    "/Roboto-Bold.ttf"
        )
    }

    fun initNumberLightFont() {
        NUMBER_LIGHT_FONT = Typeface.createFromAsset(
            BasicApplication.instance.assets, "fonts" +
                    "/Roboto-Light.ttf"
        )
    }

    @JvmStatic
    fun getTitleTypeface(): Typeface? {
        if (TITLE_BOLD_FONT == null) {
            initTitleFont()
        }
        return TITLE_BOLD_FONT
    }


    @JvmStatic
    fun getNumberTypeface(): Typeface? {
        if (NUMBER_BOLD_FONT == null) {
            initNumberFont()
        }
        return NUMBER_BOLD_FONT
    }


    @JvmStatic
    fun getNumberLightTypeface(): Typeface? {
        if (NUMBER_LIGHT_FONT == null) {
            initNumberLightFont()
        }
        return NUMBER_LIGHT_FONT
    }
}
