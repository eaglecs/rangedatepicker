package com.andrewjapar.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andrewjapar.rangedatepicker.CalendarPicker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstCalendarDate = Calendar.getInstance()
//        firstCalendarDate.set(2019, 9, 1)

        val secondCalendarDate = Calendar.getInstance()
        secondCalendarDate.time = firstCalendarDate.time
        secondCalendarDate.add(Calendar.YEAR, -10)

        val thirdCalendarDate = Calendar.getInstance()
        thirdCalendarDate.time = firstCalendarDate.time
        thirdCalendarDate.add(Calendar.MONTH, 10)
        val currentDate = Calendar.getInstance()
        val nextDate = Calendar.getInstance()
        nextDate.add(Calendar.DATE, 10)

        calendar_view.apply {
            setRangeDate(secondCalendarDate.time, thirdCalendarDate.time)
            setSelectionDate(currentDate.time, nextDate.time)
            scrollToDate(currentDate.time)
        }

        calendar_view.setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
            departure_date.text = startLabel
            return_date.text = endLabel
        }

        calendar_view.setOnStartSelectedListener { startDate, label ->
            departure_date.text = label
            return_date.text = "-"
        }
    }
}
