package com.cathy.diy_arcseekbar.tools

import com.cathy.diy_arcseekbar.bean.WeekBean


/**
+--------------------------------------+
+ @author Catherine Liu
+--------------------------------------+
+ 2020/5/19 16:01
+--------------------------------------+
+ Des:数据整理
+--------------------------------------+
 */

fun getDefaultWeekData(): List<WeekBean> {
    var weekList: MutableList<WeekBean> = mutableListOf<WeekBean>()
    weekList.add(WeekBean("M", true))
    weekList.add(WeekBean("T", true))
    weekList.add(WeekBean("W", true))
    weekList.add(WeekBean("T", true))
    weekList.add(WeekBean("F", true))
    weekList.add(WeekBean("S", false))
    weekList.add(WeekBean("S", false))
    return weekList
}
