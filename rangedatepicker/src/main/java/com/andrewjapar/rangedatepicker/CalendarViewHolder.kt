/**
 * Copyright 2020 Andrew Japar (@andrewjapar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.andrewjapar.rangedatepicker

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.calendar_day_view.view.*
import kotlinx.android.synthetic.main.calendar_month_view.view.*
import kotlinx.android.synthetic.main.calendar_week_view.view.*
import java.util.*

internal abstract class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit)
}

internal class MonthViewHolder(private val view: View) : CalendarViewHolder(view) {
    private val name by lazy { view.vMonthName }

    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
        if (item is CalendarEntity.Month) {
            name.text = item.label
        }
    }
}

internal open class WeekViewHolder(private val locale: Locale, private val view: View) :
    CalendarViewHolder(view) {
    private val tvSunday by lazy { view.tvSunday }
    private val tvMonday by lazy { view.tvMonday }
    private val tvTuesday by lazy { view.tvTuesday }
    private val tvWednesday by lazy { view.tvWednesday }
    private val tvThursday by lazy { view.tvThursday }
    private val tvFriday by lazy { view.tvFriday }
    private val tvSaturday by lazy { view.tvSaturday }
    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
        if (locale.language == "vn" || locale.language == "vi-VN" || locale.language == "vi") {
            view.tvSunday.text = view.resources.getText(R.string.calendar_sunday_vn)
            view.tvMonday.text = view.resources.getText(R.string.calendar_monday_vn)
            view.tvTuesday.text = view.resources.getText(R.string.calendar_tuesday_vn)
            view.tvWednesday.text = view.resources.getText(R.string.calendar_wednesday_vn)
            view.tvThursday.text = view.resources.getText(R.string.calendar_thursday_vn)
            view.tvFriday.text = view.resources.getText(R.string.calendar_friday_vn)
            view.tvSaturday.text = view.resources.getText(R.string.calendar_saturday_vn)
        } else {
            tvSunday.text = view.resources.getText(R.string.calendar_sunday)
            tvMonday.text = view.resources.getText(R.string.calendar_monday)
            tvTuesday.text = view.resources.getText(R.string.calendar_thursday)
            tvWednesday.text = view.resources.getText(R.string.calendar_wednesday)
            tvThursday.text = view.resources.getText(R.string.calendar_thursday)
            tvFriday.text = view.resources.getText(R.string.calendar_friday)
            tvSaturday.text = view.resources.getText(R.string.calendar_saturday)
        }

    }
}

internal class DayViewHolder(view: View) : CalendarViewHolder(view) {
    private val name by lazy { view.vDayName }
    private val halfLeftBg by lazy { view.vHalfLeftBg }
    private val halfRightBg by lazy { view.vHalfRightBg }

    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
        if (item is CalendarEntity.Day) {
            name.text = item.label
            when (item.selection) {
                SelectionType.START -> {
                    name.select()
                    halfLeftBg.dehighlight()
                    if (item.isRange) halfRightBg.highlight()
                    else halfRightBg.dehighlight()
                }
                SelectionType.END -> {
                    name.select()
                    halfLeftBg.highlight()
                    halfRightBg.dehighlight()
                }
                SelectionType.BETWEEN -> {
                    name.deselect()
                    halfRightBg.highlight()
                    halfLeftBg.highlight()
                }
                SelectionType.NONE -> {
                    halfLeftBg.dehighlight()
                    halfRightBg.dehighlight()
                    name.deselect()
                }
            }

            name.setTextColor(getFontColor(item))
            if (item.state != DateState.DISABLED) {
                itemView.setOnClickListener {
                    actionListener.invoke(
                        item,
                        adapterPosition
                    )
                }
            } else {
                itemView.setOnClickListener(null)
            }
        }
    }

    private fun getFontColor(item: CalendarEntity.Day): Int {
        return if (item.selection == SelectionType.START || item.selection == SelectionType.END) {
            ContextCompat.getColor(itemView.context, R.color.calendar_day_selected_font)
        } else {
            val color = when (item.state) {
                DateState.DISABLED -> R.color.calendar_day_disabled_font
                DateState.WEEKEND -> R.color.calendar_day_weekend_font
                else -> {
                    if (item.date.isToday()) {
                        R.color.calendar_day_weekend_font
                    } else {
                        R.color.calendar_day_normal_font
                    }
                }
            }
            ContextCompat.getColor(itemView.context, color)
        }
    }

    private fun TextView.select() {
        setBackgroundResource(R.drawable.selected_day_bg)
        setTypeface(null, Typeface.BOLD)
    }

    private fun TextView.deselect() {
        background = null
        setTypeface(null, Typeface.NORMAL);
    }

    private fun View.dehighlight() {
        setBackgroundColor(Color.parseColor("#00000000"))
    }

    private fun View.highlight() {
        setBackgroundColor(Color.parseColor("#1AFF9800"))
    }
}

internal class EmptyViewHolder(view: View) : CalendarViewHolder(view) {
    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
    }

}