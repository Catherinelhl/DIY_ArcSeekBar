package com.cathy.diy_arcseekbar.tools


/**
+--------------------------------------+
+ @author Catherine Liu
+--------------------------------------+
+ 2020/4/20 19:46
+--------------------------------------+
+ Des: 时间工具类
+--------------------------------------+
 */

/**
 * 根据表盘返回的秒数得到HM格式
 *
 * @param time
 * @return
 */
fun convertSecondToHM(time: Int): String? {
    if (time <= 0) {
        return "00:00"
    }
    val hh: String
    val mm: String
    val hours = time / 60
    val minute = time % 60
    hh = if (hours < 10) {
        "0$hours"
    } else {
        hours.toString()
    }
    mm = if (minute < 10) {
        "0$minute"
    } else {
        minute.toString()
    }
    return "$hh:$mm"
}