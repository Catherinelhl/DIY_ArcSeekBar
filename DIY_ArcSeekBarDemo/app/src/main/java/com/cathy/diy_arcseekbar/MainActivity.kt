package com.cathy.diy_arcseekbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cathy.diy_arcseekbar.adapter.WeekAdapter
import com.cathy.diy_arcseekbar.tools.convertSecondToHM
import com.cathy.diy_arcseekbar.tools.getDefaultWeekData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.title)
        initRecycleView()
        initListener()
    }

    private val weekAdapter: WeekAdapter by lazy { WeekAdapter(baseContext, getDefaultWeekData()) }
    private fun initRecycleView() {
        recycle_view.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycle_view.adapter = weekAdapter

    }

    private fun initListener() {
        alarm_seek_bar.setTimeListener(object : AlarmSeekBar.TimeListener {
            override fun onMorningTime(time: Int) {
                tv_get_up_time.text = convertSecondToHM(time)

            }

            override fun onMorningEnd() {
                println("onMorningEnd")
            }

            override fun onSleepEnd() {
                println("onSleepEnd")
            }

            override fun onSleepTime(time: Int) {
                tv_sleep_time.text = convertSecondToHM(time)

            }

        })
    }
}
